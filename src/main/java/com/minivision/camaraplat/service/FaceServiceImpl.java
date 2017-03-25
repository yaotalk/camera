package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Face;
import com.minivision.camaraplat.domain.FaceSet;
import com.minivision.camaraplat.repository.FaceSetRepository;
import com.minivision.camaraplat.repository.FaceRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2017/3/16 0016.
 */
@Transactional
@Service
@ConfigurationProperties(prefix="myprops")
public class FaceServiceImpl implements FaceService {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    @Autowired
    private FaceRepository faceRepository;

    @Autowired
    private FaceSetRepository faceSetRepository;

    @Autowired
    private FacePlatService facePlatService;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    private String filepath;

    /**
     * 保存人脸
     * @param face
     * @param mfile
     * @param facesetToken
     * @return
     */
    @Override public String save(Face face,MultipartFile mfile,String facesetToken) {
        String fileType = mfile.getOriginalFilename().substring(mfile.getOriginalFilename().lastIndexOf("."));
        if(!StringUtils.isEmpty(facesetToken)){
            FaceSet faceSet = faceSetRepository.findOne(facesetToken);
            face.setFaceSet(faceSet);
        }
            String name = face.getName()+new SimpleDateFormat("YYYYMMddHHmmssSSS").format(new Date());
            File file = new File(filepath);
            if(file.exists()) {
                if (!file.isDirectory()) {
                    throw new RuntimeException("无法创建文件,文件已存在且不是文件夹类型");
                }
            }
             else  file.mkdir();
             File imgfile = new File(file.getAbsolutePath()+File.separator+name+fileType);
             face.setImgpath(imgfile.getPath());
             logger.info("____________________________存储路径为:   "+imgfile.getAbsolutePath());
             logger.info("____________________________存储文件名为:  "+imgfile.getName());
             String faceToken;
                try {
                    mfile.transferTo(imgfile);
                    faceToken = facePlatService.faceDetect(imgfile);
                }
                catch (IOException e){
                    logger.info(e.getMessage());
                    return "图片上传失败！";
                }
                catch (Exception e){
                    imgfile.delete();
                    logger.info(e.getMessage());
                    return "注册失败，未检测到人脸！";
                }
                if(!StringUtils.isEmpty(faceToken)) {
                    logger.info("人脸检测完成，开始进行人脸入库_____________");
                    try {
                        Map map = facePlatService.addFace(facesetToken,faceToken);
                    } catch (Exception e) {
                        imgfile.delete();
                        return "注册失败："+e.getMessage();
                    }
                    face.setId(faceToken);
                    faceRepository.save(face);
                    logger.info("人脸入库完成_____________");
                }
                 return "success";
    }

    @Override public String update(Face face, MultipartFile mfile, String facesetToken) {
                String fileType = mfile.getOriginalFilename().substring(mfile.getOriginalFilename().lastIndexOf("."));
                if(!StringUtils.isEmpty(facesetToken)){
                    FaceSet faceSet = faceSetRepository.findOne(facesetToken);
                    face.setFaceSet(faceSet);
                }
                String name = face.getName()+new SimpleDateFormat("YYYYMMddHHmmssSSS").format(new Date());
                File file = new File(filepath);
                if(file.exists()) {
                    if (!file.isDirectory()) {
                        throw new RuntimeException("无法创建文件,文件已存在且不是文件夹类型");
                    }
                }
                else  file.mkdir();
                File imgfile = new File(file.getAbsolutePath()+File.separator+name+fileType);
                Face oldFace = faceRepository.findOne(face.getId());
                String oldPath = oldFace.getImgpath();
                logger.info("以前的路径为"+oldPath);
                face.setImgpath(imgfile.getPath());
                logger.info("____________________________存储路径为:   "+imgfile.getAbsolutePath());
                logger.info("____________________________存储文件名为:  "+imgfile.getName());
                String faceToken;
                try {
                    mfile.transferTo(imgfile);
                    faceToken = facePlatService.faceDetect(imgfile);
                    //删除以前的图片
                    File oldImage = new File(oldPath);
                    if(oldImage.exists()){
                          oldImage.delete();
                    }
                }
                catch (IOException e){
                    logger.info(e.getMessage());
                    return "图片上传失败！";
                }
                catch (Exception e){
                    imgfile.delete();
                    logger.info(e.getMessage());
                    return "注册失败，未检测到人脸！";
                }
                if(!StringUtils.isEmpty(faceToken)) {
                    logger.info("人脸检测完成，开始进行人脸入库_____________");
                    try {
                        //添加新脸
                        Map addmap = facePlatService.addFace(facesetToken,faceToken);
                        logger.info("新人脸添加成功_____________");
                        //删除旧脸
                        String oldfaceToken = oldFace.getId();
                        Map delmap = facePlatService.delFace(facesetToken,oldfaceToken);
                        logger.info("旧人脸删除成功_____________");
                    } catch (Exception e) {
                        imgfile.delete();
                        return "注册失败："+e.getMessage();
                    }
                    faceRepository.delete(oldFace.getId());
                    face.setId(faceToken);
                    faceRepository.save(face);
                    logger.info("人脸入库完成_____________");
                }
                return "success";
    }

    /**
     * 删除人脸
     * @param facesetToken
     * @param faceToken
     */
    public void delete(String facesetToken,String faceToken){
        try {
            facePlatService.delFace(facesetToken,faceToken);
        } catch (Exception e) {
            e.printStackTrace();
             return;
        }
             faceRepository.deleteByIdIn(Arrays.asList(faceToken.split(",")));
        }

}
