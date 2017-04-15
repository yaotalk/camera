package com.minivision.camaraplat.rest.result.system;

public class CameraResult {

    private long id;
    private String deviceName;
    private String deviceIp;
    private String username;
    private String password;
    private int devicePort;
    private int rtspPort;
    private int webPort;
    private Long analyserId;
    private Long regionId;
    private int  type;
    private boolean isOnline ;
    public CameraResult() {
    }

    public CameraResult(long id, String deviceName, String deviceIp, String username,
        String password, int devicePort, int rtspPort, int webPort, Long analyserId,
        Long regionId, int type, boolean isOnline) {
        this.id = id;
        this.deviceName = deviceName;
        this.deviceIp = deviceIp;
        this.username = username;
        this.password = password;
        this.devicePort = devicePort;
        this.rtspPort = rtspPort;
        this.webPort = webPort;
        this.analyserId = analyserId;
        this.regionId = regionId;
        this.type = type;
        this.isOnline = isOnline;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDevicePort() {
        return devicePort;
    }

    public void setDevicePort(int devicePort) {
        this.devicePort = devicePort;
    }

    public int getRtspPort() {
        return rtspPort;
    }

    public void setRtspPort(int rtspPort) {
        this.rtspPort = rtspPort;
    }

    public int getWebPort() {
        return webPort;
    }

    public void setWebPort(int webPort) {
        this.webPort = webPort;
    }

    public Long getAnalyserId() {
        return analyserId;
    }

    public void setAnalyserId(Long analyserId) {
        this.analyserId = analyserId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
