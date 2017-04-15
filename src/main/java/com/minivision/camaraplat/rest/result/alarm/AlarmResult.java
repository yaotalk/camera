package com.minivision.camaraplat.rest.result.alarm;

public class AlarmResult {

        private long id;
        private long emergengyTime;
        private String logType;
        private String username;
        private String sex;
        private String idCard;
        private long cameraId;
        private float confidence;
        private FacePostion facePosition;
        private String panoramicUrl;
        private String userImgUrl;

    public AlarmResult() {
    }

    public FacePostion getFacePosition() {
        return facePosition;
    }

    public void setFacePosition(FacePostion facePosition) {
        this.facePosition = facePosition;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmergengyTime() {
        return emergengyTime+"";
    }

    public void setEmergengyTime(long emergengyTime) {
        this.emergengyTime = emergengyTime;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public long getCameraId() {
        return cameraId;
    }

    public void setCameraId(long cameraId) {
        this.cameraId = cameraId;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(float confidence) {
        this.confidence = confidence;
    }

    public String getPanoramicUrl() {
        return panoramicUrl;
    }

    public void setPanoramicUrl(String panoramicUrl) {
        this.panoramicUrl = panoramicUrl;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }
    public static  class FacePostion {
        private int top;
        private int left;
        private int width;
        private int height;

        public FacePostion(int top, int left, int width, int height) {
            this.top = top;
            this.left = left;
            this.width = width;
            this.height = height;
        }

        public int getTop() {
            return top;
        }

        public void setTop(int top) {
            this.top = top;
        }

        public int getLeft() {
            return left;
        }

        public void setLeft(int left) {
            this.left = left;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
