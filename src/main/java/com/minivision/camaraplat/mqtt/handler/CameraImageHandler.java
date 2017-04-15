package com.minivision.camaraplat.mqtt.handler;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.minivision.camaraplat.domain.Camera;
import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.domain.MonitorImage;
import com.minivision.camaraplat.domain.Strategy;
import com.minivision.camaraplat.domain.record.MonitorRecord;
import com.minivision.camaraplat.domain.record.SnapshotRecord;
import com.minivision.camaraplat.faceplat.client.FacePlatClient;
import com.minivision.camaraplat.faceplat.ex.FacePlatException;
import com.minivision.camaraplat.faceplat.result.detect.DetectResult;
import com.minivision.camaraplat.faceplat.result.detect.DetectedFace;
import com.minivision.camaraplat.faceplat.result.detect.SearchResult;
import com.minivision.camaraplat.faceplat.result.detect.SearchResult.Result;
import com.minivision.camaraplat.mqtt.message.Packet;
import com.minivision.camaraplat.mqtt.message.Packet.Head;
import com.minivision.camaraplat.mqtt.message.Packet.Head.Code;
import com.minivision.camaraplat.mqtt.message.PacketUtils;
import com.minivision.camaraplat.mqtt.service.PublishMessageTemplate;
import com.minivision.camaraplat.repository.MonitorRecordRepository;
import com.minivision.camaraplat.repository.SnapshotRecordRepository;
import com.minivision.camaraplat.service.CameraService;
import com.minivision.camaraplat.service.FaceService;
import com.minivision.camaraplat.util.CommonUtils;

@Component
public class CameraImageHandler {
  @Autowired
  private CameraService cameraService;
  @Autowired
  private FacePlatClient facePlatClient;
  @Autowired
  private FaceService faceService;
  @Autowired
  private MonitorRecordRepository monitorRepository;
  @Autowired
  private SnapshotRecordRepository snapshotRespository;
  
  @Autowired
  private SimpMessagingTemplate template;
  
  @Autowired
  private PublishMessageTemplate mqttTemplate;
  
  @Value("${system.store.snapshot}")
  private String imageFilePath = ".";

  private String imageFileType = "jpg";
  
  private static final Logger logger = LoggerFactory.getLogger(CameraImageHandler.class);
  
  private Map<String, Long> lastSaveTime = new HashMap<>();
  
  private Map<Long, tempRecognizedResult> tempRecognizedResults = new ConcurrentHashMap<>();
  
  
  private void saveMonitorImage(MonitorImage image){
    if(image.getFileName() == null){
      //String fileName = image.getSerialNo() + "_" + image.getCameraId() + "_" + getDatePath() + "." + imageFileType;
      String fileName = "camera_"+image.getCameraId() + "/" + CommonUtils.getDatePath() + "/" + image.getSerialNo() + "." + imageFileType; 
      File imgFile = new File(imageFilePath, fileName);
      try {
        //FileUtils.touch(imgFile);
        FileUtils.writeByteArrayToFile(imgFile, image.getImage());
        image.setFileName(fileName);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  
  private void onRecognized(Strategy strategy, MonitorImage image, SnapshotRecord snapshotRecord, String faceToken){
    logger.info("reconized a face[{}], confidence:{}", faceToken, snapshotRecord.getConfidence());
    //黑名单命中
    if(strategy.isBlackList()){
      int intervals = strategy.getIntervals();
      saveMonitorImage(image);
      snapshotRespository.save(snapshotRecord);
      Long lastSave = lastSaveTime.get(faceToken);
      if(lastSave == null){
        lastSave = 0l;
      }
      if(System.currentTimeMillis() - lastSave > intervals * 1000){
        MonitorRecord record = new MonitorRecord(snapshotRecord);
        Face face = faceService.find(faceToken);
        record.setFace(face);
        monitorRepository.save(record);
        lastSaveTime.put(faceToken, System.currentTimeMillis());
        template.convertAndSend("/c/alarm", record);
        logger.info("get a record, face:{}", face);
      }else{
        Face face = faceService.find(faceToken);
        logger.info("discard a record, face:{}, lastSaveTime:{}",  face, new Date(lastSave));
      }
    }else if(strategy.isSnapshot()){
      saveMonitorImage(image);
      snapshotRespository.save(snapshotRecord);
    }
    
  }
  
  private void onStranger(Strategy strategy, MonitorImage image, SnapshotRecord snapshotRecord){
    logger.info("detected a stranger, confidence:{}", snapshotRecord.getConfidence());
    //白名单未命中底库
    if(!strategy.isBlackList()){
      saveMonitorImage(image);
      snapshotRespository.save(snapshotRecord);
      MonitorRecord record = new MonitorRecord(snapshotRecord);
      monitorRepository.save(record);
      template.convertAndSend("/c/alarm", record);
    }else if(strategy.isSnapshot()){
      saveMonitorImage(image);
      snapshotRespository.save(snapshotRecord);
    }
  }
  
  public void onRecComplete(int cameraId, int trackId){
    long key = cameraId << 32 | trackId;
    tempRecognizedResult temp = tempRecognizedResults.remove(key);
    if(temp!=null){
      onStranger(temp.strategy, temp.image, temp.snapshotRecord);
    }
  }
  
  public void onImageReceive(MonitorImage image, String clientId) throws FacePlatException{
    
    int cameraId = image.getCameraId();
    int trackId = image.getTrackId();
    
    Camera camera = cameraService.findByid(cameraId);
    if(camera == null){
      return;
    }
    
    DetectResult detectResult = facePlatClient.detect(image.getImage());
    if(detectResult.getFaces() == null || detectResult.getFaces().isEmpty()){
      //未检测到人脸
      return;
    }
    
    DetectedFace detectedFace = detectResult.getFaces().get(0);
    Strategy strategy = camera.getStrategy();
    Set<FaceSet> faceSets = camera.getFaceSets();
    int threshold = strategy.getCompareThreshold();
    
    SnapshotRecord snapshotRecord = new SnapshotRecord(image, detectedFace);
    
    double confidence = 0;
    String faceToken = null;
    String faceSetToken = null;
    
    if(faceSets != null && !faceSets.isEmpty()){
      for(FaceSet fSet: faceSets){
        SearchResult result = facePlatClient.search(fSet.getToken(), detectedFace.getFaceToken());
        List<Result> list = result.getResults();
        if(list!=null && list.size()>0){
          double thisConfidence = list.get(0).getConfidence()*100;
          if(thisConfidence > confidence){
            confidence = thisConfidence;
            faceToken = list.get(0).getFaceToken();
            faceSetToken = fSet.getToken();
          }
        }
      }
      
      snapshotRecord.setConfidence((float) confidence);
      logger.info("detect a face[{}] in faceset[{}], confidence:{}", faceToken, faceSetToken, confidence);
      //识别成功
      if(confidence > threshold){
        //notify camera
        
        Head head = PacketUtils.getInstance().buildNotifyHead(Code.REC_INFO);
        Map<String, Object> body = new HashMap<>();
        body.put("cameraId", cameraId);
        body.put("trackId", trackId);
        body.put("recResult", 1);
        
        Packet<Map<String, Object>> packet = new Packet<>(head, body);
        mqttTemplate.sendTo("/d/"+clientId, packet, false);
        
        onRecognized(strategy, image, snapshotRecord, faceToken);
        tempRecognizedResults.remove(trackId);
        return;
      }
      
    }
    
    
    //陌生人  
    long key = cameraId << 32 | trackId;
    
    tempRecognizedResult result = tempRecognizedResults.get(key);
    if(result == null){
      result = new tempRecognizedResult();
      tempRecognizedResults.put(key, result);
    }
    int times = result.addTimes();
    if(times >= strategy.getRetryCounts()){
      //重试后确认为陌生人
      onStranger(strategy, image, snapshotRecord);
      tempRecognizedResults.remove(key);
      
    }else{
      logger.info("stranger: trackId:{}, times:{},  faceToken:{}", trackId, times, faceToken);
      result.setSnapshotRecord(snapshotRecord);
      result.setImage(image);
      result.setFaceToken(faceToken);
      result.setConfidence(confidence);
      result.setExpireTime(System.currentTimeMillis() + strategy.getSnapInterval() * 3);
      result.setStrategy(strategy);
    }
    
  }
  
  @Scheduled(fixedRate = 3000)
  public void checkExpire() {
    long start = System.nanoTime();
    for (Entry<Long, tempRecognizedResult> entry : tempRecognizedResults.entrySet()) {
      tempRecognizedResult temp = entry.getValue();
      long expireTime = temp.getExpireTime();
        //long aliveTime = System.currentTimeMillis() - temp.getTimeStamp();
        if (temp != null && System.currentTimeMillis() > expireTime) {
          temp = tempRecognizedResults.remove(entry.getKey());
            // check again in case of anyone else removed it
            if (temp != null) {
              onStranger(temp.getStrategy(), temp.getImage(), temp.getSnapshotRecord());
            }
        }
    }
    long duration = System.nanoTime() - start;
    logger.trace("@@@ TempRecognizedResults TimeoutChecker finished in {}ns.", duration);
  }
  
  public class tempRecognizedResult{
    private MonitorImage image;
    private SnapshotRecord snapshotRecord;
    private String faceToken;
    private double confidence;
    private Strategy strategy;
    
    private AtomicInteger times = new AtomicInteger();
    private long expireTime;
    
    public MonitorImage getImage() {
      return image;
    }
    public void setImage(MonitorImage image) {
      this.image = image;
    }
    public SnapshotRecord getSnapshotRecord() {
      return snapshotRecord;
    }
    public void setSnapshotRecord(SnapshotRecord snapshotRecord) {
      this.snapshotRecord = snapshotRecord;
    }
    public String getFaceToken() {
      return faceToken;
    }
    public void setFaceToken(String faceToken) {
      this.faceToken = faceToken;
    }
    public double getConfidence() {
      return confidence;
    }
    public void setConfidence(double confidence) {
      this.confidence = confidence;
    }
    
    public int getTimes(){
      return times.get();
    }
    
    public int addTimes(){
      return times.incrementAndGet();
    }
    public long getExpireTime() {
      return expireTime;
    }
    public void setExpireTime(long expireTime) {
      this.expireTime = expireTime;
    }
    public Strategy getStrategy() {
      return strategy;
    }
    public void setStrategy(Strategy strategy) {
      this.strategy = strategy;
    }
  }

}
