package com.minivision.cameraplat.mqtt.handler;

import com.google.common.collect.Lists;
import com.minivision.cameraplat.domain.*;
import com.minivision.cameraplat.domain.record.MonitorRecord;
import com.minivision.cameraplat.domain.record.MonitorRecord.FaceInfo;
import com.minivision.cameraplat.domain.record.SnapshotRecord;
import com.minivision.cameraplat.faceplat.client.FacePlatClient;
import com.minivision.cameraplat.faceplat.ex.FacePlatException;
import com.minivision.cameraplat.faceplat.result.detect.DetectResult;
import com.minivision.cameraplat.faceplat.result.detect.DetectedFace;
import com.minivision.cameraplat.faceplat.result.detect.SearchResult;
import com.minivision.cameraplat.faceplat.result.detect.SearchResult.Result;
import com.minivision.cameraplat.mqtt.message.Packet;
import com.minivision.cameraplat.mqtt.message.Packet.Head;
import com.minivision.cameraplat.mqtt.message.Packet.Head.Code;
import com.minivision.cameraplat.mqtt.message.PacketUtils;
import com.minivision.cameraplat.mqtt.service.PublishMessageTemplate;
import com.minivision.cameraplat.mvc.ex.ServiceException;
import com.minivision.cameraplat.repository.EntranceRepository;
import com.minivision.cameraplat.repository.MonitorRecordRepository;
import com.minivision.cameraplat.repository.SnapshotRecordRepository;
import com.minivision.cameraplat.service.CameraService;
import com.minivision.cameraplat.service.FaceService;
import com.minivision.cameraplat.util.CommonUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

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
  private EntranceRepository entranceRepository;
  @Autowired
  private SimpMessagingTemplate template;

  @Autowired
  private PublishMessageTemplate mqttTemplate;

  @Value("${system.store.snapshot}")
  private String imageFilePath = ".";

  private String imageFileType = "jpg";

  @Value("${faceNum:5}")
  private int faceNum;

  @Value("${system.faceSetPriority:true}")
  private boolean faceSetPriority;

  private static final Logger logger = LoggerFactory.getLogger(CameraImageHandler.class);

  private Map<String, Long> lastSaveTime = new ConcurrentHashMap<>();

  private Map<Long, TempRecognizedResult> tempRecognizedResults = new ConcurrentHashMap<>();


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


  private void onRecognized(Camera camera, MonitorImage image, SnapshotRecord snapshotRecord, String faceToken,List<FaceInfo> faceInfos){
    logger.info("reconized a face[{}], confidence:{}", faceToken, snapshotRecord.getConfidence());
    //黑名单命中
    if(camera.getStrategy().getType() == Strategy.StrategyType.BlackList || camera.getStrategy().getType() == Strategy.StrategyType.DynaminFaceRepo){
      int intervals = camera.getStrategy().getIntervals();
      saveSnapShot(image, snapshotRecord);
      Long lastSave = lastSaveTime.get(faceToken);
      if(lastSave == null){
        lastSave = 0l;
      }
      long now = System.currentTimeMillis();
      
      //now小于lastSave时，说明系统时间有更改
      if(now - lastSave < 0 || now - lastSave > intervals * 1000){
        Face face = faceService.find(faceToken);
        saveAlarm(snapshotRecord, face,faceInfos);
        lastSaveTime.put(faceToken, now);
        logger.info("get a record, face:{}", face);
      }else{
        Face face = faceService.find(faceToken);
        logger.info("discard a record, face:{}, lastSaveTime:{}",  face, new Date(lastSave));
      }
    }else if(camera.getStrategy().isSnapshot()){
      saveSnapShot(image, snapshotRecord);
    }

  }

  private void onStranger(Camera camera, MonitorImage image, SnapshotRecord snapshotRecord, String faceToken){
    logger.info("detected a stranger, confidence:{}", snapshotRecord.getConfidence());
    if(camera.getStrategy().getType() == Strategy.StrategyType.DynaminFaceRepo){
   //   Set<FaceSet> faceSets = camera.getFaceSets();
   //   Assert.isTrue(!faceSets.isEmpty(), "Create a faceset first!");
   //   FaceSet set = faceSets.iterator().next();
      FaceSet set = camera.getFaceSet();
      Assert.isTrue(set != null, "Create a faceset first!");
      try {
        faceService.save(faceToken, set.getToken(), image.getImage());
        logger.warn("dynamic save face {}",image.getTrackId());
        saveSnapShot(image, snapshotRecord);
        Face face = faceService.find(faceToken);
        saveAlarm(snapshotRecord, face,null);
      } catch (ServiceException e) {
        e.printStackTrace();
      }
      return;
    }
    
    
    //白名单未命中底库
    if(camera.getStrategy().getType() == Strategy.StrategyType.WhiteList){
      saveSnapShot(image, snapshotRecord);
      saveAlarm(snapshotRecord, null,null);
    }else if(camera.getStrategy().isSnapshot()){
      saveSnapShot(image, snapshotRecord);
    }
  }

  private void saveSnapShot(MonitorImage image, SnapshotRecord snapshotRecord){
    saveMonitorImage(image);
    snapshotRecord.setPhotoFileName(image.getFileName());
    snapshotRespository.save(snapshotRecord);
    template.convertAndSend("/c/snapshot", snapshotRecord);
  }

  private void saveAlarm(SnapshotRecord snapshotRecord, Face face,List<FaceInfo> faceInfos){
    MonitorRecord record = new MonitorRecord(snapshotRecord);
    if(face!=null){
      record.setFace(face);
    }
    monitorRepository.save(record);
    record.setFaceInfos(faceInfos);
    template.convertAndSend("/c/alarm", record);
  }
  
  public void onMultiFace(int cameraId){
    logger.info("multiFace, cameraId: {}", cameraId);
    Camera snapCamera = cameraService.findByid(cameraId);
    Map<String, Object> data = new HashMap<>();
    data.put("cameraId", cameraId);
    data.put("padId", snapCamera.getPadId());
    template.convertAndSend("/c/multiFace", data);
  }

  public void onRecComplete(int cameraId, int trackId){
    long key = (long) cameraId << 32 | trackId;
    //logger.warn("recComplete trackId:{},key is {},temp is {}",trackId,key,tempRecognizedResults.get(key));
    TempRecognizedResult temp = tempRecognizedResults.remove(key);
    if(temp!=null){
      //logger.warn("receive notify is {},trackId:{}",temp.getFaceToken(),trackId);
      onStranger(temp.camera, temp.image, temp.snapshotRecord, temp.getFaceToken());
    }
  }

  public void onImageReceive(MonitorImage image, String clientId) throws FacePlatException{

    int cameraId = image.getCameraId();
    int trackId = image.getTrackId();

    Camera camera = cameraService.findByid(cameraId);
    if(camera == null){
      return;
    }

    DetectResult detectResult = facePlatClient.detect(image.getImage(), true);
    if(detectResult.getFaces() == null || detectResult.getFaces().isEmpty()){
      //未检测到人脸
      return;
    }

    DetectedFace detectedFace = detectResult.getFaces().get(0);
    Strategy strategy = camera.getStrategy();
    Set<FaceSet> faceSets = camera.getFaceSets();
    double threshold = strategy.getCompareThreshold();

    SnapshotRecord snapshotRecord = new SnapshotRecord(image, detectedFace);

    double confidence = 0;
    String faceToken = null;
    String faceSetToken = null;
    Camera snapCamera = cameraService.findByid(snapshotRecord.getCameraId());
    snapshotRecord.setIsOut(snapCamera.getIsOut());
    snapshotRecord.setPadId(snapCamera.getPadId());
    snapshotRecord.setDoorNumber(snapCamera.getDoorNumber());
    List<Long> ids = snapCamera.getDoors().stream().map(IdEntity::getId).collect(Collectors.toList());
    Set<EntranceGuard> entranceGuards = entranceRepository.findBydoorsIdIn(ids);
    snapshotRecord.setEntranceGuards(entranceGuards);
    List<FaceInfo> faces = new ArrayList<>();
    if(faceSets != null && !faceSets.isEmpty()){
      List<TemFaceResult> temFaceResults = Lists.newArrayList();
      List<TemFaceResult> recFaceResults =  Lists.newArrayList();
      for(FaceSet fSet: faceSets){
        SearchResult result = facePlatClient.search(fSet.getToken(), detectedFace.getFaceToken(),1);
        List<Result> list = result.getResults();
        if(list!=null && list.size()>0){
          double thisConfidence = list.get(0).getConfidence()*100;
          TemFaceResult temFaceResult = new TemFaceResult(thisConfidence,fSet.getToken(),list.get(0).getFaceToken(),fSet.getPriority());
          temFaceResults.add(temFaceResult);
          if(thisConfidence > threshold){
              recFaceResults.add(temFaceResult);
          }
//          if(thisConfidence > confidence){
//            confidence = thisConfidence;
//            faceToken = list.get(0).getFaceToken();
//            faceSetToken = fSet.getToken();
//          }
//          List<String> faceTokens = list.stream().map(Result::getFaceToken).collect(Collectors.toList());
//          Map<String,Face> map =  faceService.findFaceByFaceTokens(faceTokens).stream().collect(Collectors.toMap(Face::getId,Face->Face));
//          for(Result searchResult : list){
//             Face face = map.get(searchResult.getFaceToken());
//             FaceInfo faceInfo = new FaceInfo(face,(float) searchResult.getConfidence());
//             faces.add(faceInfo);
//          }
        }
      }
      if(recFaceResults.isEmpty()){
           confidence = temFaceResults.stream().max(Comparator.comparingDouble(o -> o.confidence)).map(
               TemFaceResult::getConfidence).orElse(0.00D);
           snapshotRecord.setConfidence((float) confidence);
      }
      else{
          TemFaceResult temFaceResult;
          if(faceSetPriority){
              temFaceResult =  recFaceResults.stream().min(Comparator.comparingInt(o -> o.priority)).get();
          }
          else {
             temFaceResult = recFaceResults.stream().max(Comparator.comparingDouble(
                TemFaceResult::getConfidence)).get();
          }
          confidence  = temFaceResult.getConfidence();
          faceToken = temFaceResult.getFaceToken();
          faceSetToken = temFaceResult.faceSetToken;
          snapshotRecord.setConfidence((float) confidence);
          Head head = PacketUtils.getInstance().buildNotifyHead(Code.REC_INFO);
          Map<String, Object> body = new HashMap<>();
          body.put("cameraId", cameraId);
          body.put("trackId", trackId);
          body.put("recResult", 1);

          Packet<Map<String, Object>> packet = new Packet<>(head, body);
          mqttTemplate.sendTo("/d/"+clientId, packet, false);

          onRecognized(camera, image, snapshotRecord, faceToken, faces);
          logger.warn("detect a face[{}] in faceset[{}], confidence:{},trackId:{}", faceToken, faceSetToken, confidence,trackId);
          tempRecognizedResults.remove(trackId);
          return;
      }
      //识别成功
//      if(confidence > threshold){
//        //notify camera
//
//        Head head = PacketUtils.getInstance().buildNotifyHead(Code.REC_INFO);
//        Map<String, Object> body = new HashMap<>();
//        body.put("cameraId", cameraId);
//        body.put("trackId", trackId);
//        body.put("recResult", 1);
//
//        Packet<Map<String, Object>> packet = new Packet<>(head, body);
//        mqttTemplate.sendTo("/d/"+clientId, packet, false);
//
//        onRecognized(camera, image, snapshotRecord, faceToken, faces);
//        tempRecognizedResults.remove(trackId);
//        return;
//      }

    }


    //陌生人  
    long key = (long) cameraId << 32 | trackId;

    TempRecognizedResult result = tempRecognizedResults.get(key);
    if(result == null){
      result = new TempRecognizedResult(camera);
      //logger.warn("put a temp in map {},key:{},trackId:{}",result,key,trackId);
      tempRecognizedResults.put(key, result);
    }
    int times = result.addTimes();
    if(times >= strategy.getRetryCounts()){
      //重试后确认为陌生人
      onStranger(camera, image, snapshotRecord, detectedFace.getFaceToken());
      tempRecognizedResults.remove(key);

    }else{
      //TODO INFO
      logger.warn("stranger: trackId:{}, times:{},  faceToken:{}", trackId, times, detectedFace.getFaceToken());
      result.setSnapshotRecord(snapshotRecord);
      result.setImage(image);
      result.setFaceToken(detectedFace.getFaceToken());
      result.setConfidence(confidence);
      result.setExpireTime(System.currentTimeMillis() + strategy.getSnapInterval() * 3);
    }
  }

  @Scheduled(fixedRate = 3000)
  public void checkExpire() {
    long start = System.nanoTime();
    for (Entry<Long, TempRecognizedResult> entry : tempRecognizedResults.entrySet()) {
      TempRecognizedResult temp = entry.getValue();
      long expireTime = temp.getExpireTime();
        //long aliveTime = System.currentTimeMillis() - temp.getTimeStamp();
        if (temp != null && System.currentTimeMillis() > expireTime) {
          logger.warn("delete an temp in map{},key:{}",temp,entry.getKey());
          temp = tempRecognizedResults.remove(entry.getKey());
            // check again in case of anyone else removed it
            if (temp != null) {
              onStranger(temp.camera, temp.getImage(), temp.getSnapshotRecord(), temp.getFaceToken());
            }
        }
    }
    long duration = System.nanoTime() - start;
    logger.trace("@@@ TempRecognizedResults TimeoutChecker finished in {}ns.", duration);
  }

  public class TempRecognizedResult{
    private MonitorImage image;
    private SnapshotRecord snapshotRecord;
    private String faceToken;
    private double confidence;
    private Camera camera;

    private AtomicInteger times = new AtomicInteger();
    private long expireTime;
    
    public TempRecognizedResult(Camera camera){
      this.camera = camera;
    }

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
    public Camera getCamera() {
      return camera;
    }
    public void setCamera(Camera camera) {
      this.camera = camera;
    }
  }

  class TemFaceResult{

    private double confidence;

    private String faceSetToken;

    private String faceToken;

    private int priority;

    public TemFaceResult(double confidence, String faceSetToken, String faceToken,int priority) {
      this.confidence = confidence;
      this.faceSetToken = faceSetToken;
      this.faceToken = faceToken;
      this.priority = priority;
    }

    public int getPriority() {
      return priority;
    }

    public void setPriority(int priority) {
      this.priority = priority;
    }

    public double getConfidence() {
      return confidence;
    }

    public void setConfidence(double confidence) {
      this.confidence = confidence;
    }

    public String getFaceSetToken() {
      return faceSetToken;
    }

    public void setFaceSetToken(String faceSetToken) {
      this.faceSetToken = faceSetToken;
    }

    public String getFaceToken() {
      return faceToken;
    }

    public void setFaceToken(String faceToken) {
      this.faceToken = faceToken;
    }
  }


}
