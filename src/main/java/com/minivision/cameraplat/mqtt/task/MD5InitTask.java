package com.minivision.cameraplat.mqtt.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.minivision.cameraplat.domain.Face;
import com.minivision.cameraplat.domain.FaceTmp;
import com.minivision.cameraplat.mqtt.task.checkthread.CheckRepeatTaskContext;
import com.minivision.cameraplat.mqtt.task.checkthread.InitMD5Thread;
import com.minivision.cameraplat.param.PageParam;
import com.minivision.cameraplat.service.FaceService;

@Component
public class MD5InitTask implements Runnable {

	private static final Logger logger = LoggerFactory
			.getLogger(MD5InitTask.class);

	@Value("${system.store.people}")
	private String basePicPath;

	private String taskId;

	private int taskStatus;
	
	private String taskName;

	@Autowired
	private FaceService faceService;

	@Autowired
	private SimpMessagingTemplate messageTemplate;
	
	private CheckRepeatTaskContext checkRepeatTaskContext;

	@Autowired
	private BatchTaskContext batchTaskContext;

	private String faceSetToken;

	private int stillRepeatNumber;

	private int totalNum;

	private int processedNum;
	
	private static boolean continued;

	private boolean isInitingMD5;

	@Override
	public void run() {
		taskStatus = BatchTask.RUNNING;
		faceService.deleteTaskData(null);
		
		processedNum = 0;
		taskName = "findMd5EmptyNum";
		checkRepeatTaskContext.setStatusText("Querying the number of pictures without MD5");
		messageTemplate.convertAndSend("/w/task/"+taskId, this);
		int count = faceService.checkIfHasNotDetected(faceSetToken);
		totalNum = count;
		if(count == 0){
			taskStatus = BatchTask.DONE;
			taskName = "md5AllHave";
			messageTemplate.convertAndSend("/w/task/"+taskId, this);
			checkRepeatTaskContext.setStatusText("All the pictures are recorded in MD5 and begin counting the same picture records of MD5");
			checkRepeatTaskContext.startCheckRepeat(faceSetToken);
			return;
		}

		taskName="saveMd5Record";
		
		int pageSize = 10000;
		InitMD5Thread.setBasePicPath(basePicPath);
		InitMD5Thread.setFaceService(faceService);
		InitMD5Thread.setTotalCount(count);
		InitMD5Thread.setMd5InitTask(this);
		InitMD5Thread.resetCount();
		continued = true;
		PageParam pageParam = new PageParam();
		int page = count / pageSize + (count % pageSize == 0 ? 0 : 1);
		checkRepeatTaskContext.setStatusText("Data that has no MD5 record is saved to the record table to be processed");
		messageTemplate.convertAndSend("/w/task/"+taskId, this);
		
		// 首先将查询的结果保存到一张临时表中
		for (int i = 0; i < page; i++) {
			if(!continued){
				return;
			}
			pageParam.setOffset(i * pageSize);
			pageParam.setLimit(pageSize);
			List<Face> list = faceService.getNotDetectedData(faceSetToken,
					pageParam);
			List<FaceTmp> listTmp = new ArrayList<FaceTmp>();
			for (Face face : list) {
				listTmp.add(new FaceTmp(face.getId(), taskId));
				//faceService.save(new FaceTmp(face.getId(), taskId));
			}
			faceService.save(listTmp);
			processedNum+=listTmp.size();
			messageTemplate.convertAndSend("/w/task/"+taskId, this);
		}
		
		taskName="caculatePicMd5";
		checkRepeatTaskContext.setStatusText("Calculating the MD5 value of the picture");

		
		processedNum = 0;
		messageTemplate.convertAndSend("/w/task/"+taskId, this);
		// 然后查询临时表，进行数据处理
		for (int i = 0; i < page; i++) {
			if(!continued){
				return;
			}
			pageParam.setOffset(i * pageSize);
			pageParam.setLimit(pageSize);
			List<Face> list = faceService.getToProcess(taskId, pageParam,false);
			for (Face face : list) {
				if(!continued){
					return;
				}
				batchTaskContext.getWorker().submit(new InitMD5Thread(face));
			}
		}

	}
	
	public void sendCurrentTask(){
		messageTemplate.convertAndSend("/w/task/"+taskId, this);
	}

	public String getFaceSetToken() {
		return faceSetToken;
	}

	public void setFaceSetToken(String faceSetToken) {
		this.faceSetToken = faceSetToken;
	}

	public boolean isInitingMD5() {
		return isInitingMD5;
	}

	public void setInitingMD5(boolean isInitingMD5) {
		this.isInitingMD5 = isInitingMD5;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public int getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}

	public int getStillRepeatNumber() {
		return stillRepeatNumber;
	}

	public void setStillRepeatNumber(int stillRepeatNumber) {
		this.stillRepeatNumber = stillRepeatNumber;
	}

	public int getProcessedNum() {
		return processedNum;
	}

	public void setProcessedNum(int processedNum) {
		this.processedNum = processedNum;
	}

	public boolean isContinued() {
		return continued;
	}

	public synchronized void finished() {
		continued = false;
		taskStatus = BatchTask.DONE;
		faceService.deleteTaskData(taskId);
		taskName = "md5AllHave";
		sendCurrentTask();
		checkRepeatTaskContext.setStatusText("After the picture MD5 is finished, begin to check the same pictures with MD5");
		checkRepeatTaskContext.startCheckRepeat(faceSetToken);
		
	}
	
	public synchronized void setContinued(boolean flag) {
		continued = flag;
		if (!flag) {
			taskStatus = BatchTask.DONE;
			faceService.deleteTaskData(taskId);
		}
	}

	public void setCheckRepeatTaskContext(
			CheckRepeatTaskContext checkRepeatTaskContext) {
		this.checkRepeatTaskContext = checkRepeatTaskContext;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}
	
	

}
