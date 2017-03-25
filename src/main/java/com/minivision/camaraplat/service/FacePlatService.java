package com.minivision.camaraplat.service;
import com.minivision.camaraplat.domain.FaceSet;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacePlatService {
    private final static Logger logger = Logger.getLogger(FacePlatService.class.getName());
    @Autowired
    private RestClient restClient;
    @Value(value = "${faceservice.address}")
    private String address;
    @Value(value = "${faceservice.port}")
    private String port;
    @Value(value = "${faceservice.apiKey}")
    private String apiKey;
    @Value(value = "${faceservice.apiSecret}")
    private String apiSecret;
    /**
     * 新建人脸库
     * @param faceSet
     * @return
     */
    public String createFaceset(FaceSet faceSet) {
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<String,Object>();
        String creatFacesetUrl =  "http://"+address+":"+port+"/api/v1/faceset/create";
        map.add("apiKey",apiKey);
        map.add("apiSecret",apiSecret);
        map.add("displayName",faceSet.getName());
        logger.info("开始调用人脸平台新增人脸库接口：");
        Map<String,String> res  = restClient.post(creatFacesetUrl,map,HashMap.class);
        logger.info("成功结束新增人脸库接口");
        if(res != null){
           return res.get("facesetToken");
        }
        return null;
    }

    /**
     * 人脸检测:提取特征值
     * @param mfile
     * @return
     */

    public String faceDetect(File mfile) throws Exception{

        String  facedetectUrl =  "http://"+address+":"+port+"/api/v1/detect";
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<String,Object>();
        map.add("apiKey",apiKey);
        map.add("apiSecret",apiSecret);
        map.add("imageFile",new FileSystemResource(mfile.getAbsolutePath()));
        logger.info("开始调用人脸平台人脸检测接口：");
        Map res = null;
        try {
             res = restClient.post(facedetectUrl, map, HashMap.class);
        }catch (Exception e){
            logger.info("调动检测接口错误"+e.getMessage());
            throw new Exception("调动检测接口错误");
        }
        if(res != null && ((List)res.get("faces")).size() == 0){
            logger.info("图片未检测到人脸");
            throw new Exception("图片未检测到人脸");
        }
        logger.info("成功结束调用人脸检测接口");
        List list = (List)res.get("faces");
        String  faceToken = "";
        if(list.size() > 0){
            Map facemap = (Map)list.get(0);
            faceToken = facemap.get("faceToken").toString();
        }
        return faceToken;
    }

    /**
     * 添加人脸
     * @param facetokens
     * @param facesetToken
     * @return
     * @throws Exception
     */
    public Map addFace(String facesetToken,String facetokens) throws Exception{
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<String,Object>();
        map.add("apiKey",apiKey);
        map.add("apiSecret",apiSecret);
        map.add("facesetToken",facesetToken);
        map.add("faceTokens",facetokens);
        logger.info("开始调用人脸平台添加人脸接口：");
        String addfaceUrl =  "http://"+address+":"+port+"/api/v1/faceset/addface";
        Map res = null;
        try{
           res =  restClient.post(addfaceUrl,map,HashMap.class);
        }
        catch (Exception e){
            throw new Exception("调动检测接口错误");
        }
       if(res.get("errorMessage")!= null){
            String errormsg = res.get("errorMessage").toString();
            if(errormsg.indexOf("FACESET_NOT_EXIST") > 0 ){
                throw new Exception("人脸库不存在！");
            }
            else throw new Exception("参数错误");
        }
        else if(res.get("failureDetail") == null) {
           logger.info("成功结束调用人脸平台添加人脸接口");
           return res;
         }
         else {
            throw new Exception("添加到人脸库失败");
        }

    }

    /**
     * 查看人脸库详细信息
     * @param facesetToken
     * @return
     * @throws Exception
     */
    public Map getFaceSetDetail(String facesetToken) throws Exception{
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<String,Object>();
        map.add("apiKey",apiKey);
        map.add("apiSecret",apiSecret);
        map.add("facesetToken",facesetToken);
        String getdetailUrl =  "http://"+address+":"+port+"/api/v1/faceset/getdetail";
        Map res = null;
        try {
            res = restClient.post(getdetailUrl, map, HashMap.class);
        }catch (Exception e){
            logger.info("调动查看人脸库详细接口错误"+e.getMessage());
            throw new Exception("调动查看人脸库详细接口错误");
        }
        if(res != null && res.get("faceCount")!=null) {
            return res;
        }
        else  throw new Exception("返回信息错误");
    }


    /**
     * 删除人脸库
     * @param facesetToken
     * @param force
     * @return
     * @throws Exception
     */
    public Map delFaceset(String facesetToken,boolean force) throws Exception{
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<String,Object>();
        map.add("apiKey",apiKey);
        map.add("apiSecret",apiSecret);
        map.add("facesetToken",facesetToken);
        map.add("force",force);
        String delFacesetUrl =  "http://"+address+":"+port+"/api/v1/faceset/delete";
        Map res = null;
        try {
            res = restClient.post(delFacesetUrl, map, HashMap.class);
        }catch (Exception e){
            logger.info("调动删除人脸库接口错误"+e.getMessage());
            throw new Exception("调动删除人脸库详细接口错误");
        }
        if(res != null && res.get("errorMessage") ==null) {
            return res;
        }
        else  throw new Exception("返回信息错误");
    }


    /**
     * 删除指定人脸库人脸
     * @param facesetToken
     * @param faceTokens
     * @return
     * @throws Exception
     */
    public Map delFace(String facesetToken,String faceTokens) throws Exception{
        LinkedMultiValueMap<String,Object> map = new LinkedMultiValueMap<String,Object>();
        map.add("apiKey",apiKey);
        map.add("apiSecret",apiSecret);
        map.add("facesetToken",facesetToken);
        map.add("faceTokens",faceTokens);
        String delFaceUrl =  "http://"+address+":"+port+"/api/v1/faceset/removeface";
        Map res = null;
        try {
            res = restClient.post(delFaceUrl, map, HashMap.class);
        }catch (Exception e){
            logger.info("调动删除人脸接口错误"+e.getMessage());
            throw new Exception("调动删除人脸详细接口错误");
        }
        if(res != null && res.get("errorMessage") ==null) {
            return res;
        }
        else  throw new Exception("返回信息错误");
    }
}
