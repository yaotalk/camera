package com.minivision.cameraplat.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.minivision.cameraplat.rest.result.system.CameraResult;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class CameraGroup extends IdEntity{

    private String groupName;

    @JsonIgnore
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE,CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Camera> cameras;

    @Transient
    @JsonUnwrapped
    private List<CameraResult> cameraResults = new ArrayList<>();

    public List<CameraResult> getCameraResults() {
        for(Camera camera : this.cameras){
            CameraResult cameraResult = new CameraResult();
            cameraResult.setId(camera.getId());
            cameraResult.setUsername(camera.getUsername());
            cameraResult.setPassword(camera.getPassword());
            cameraResult.setDeviceIp(camera.getIp());
            cameraResult.setDeviceName(camera.getDeviceName());
            cameraResult.setDevicePort(camera.getPort());
            cameraResult.setType(camera.getType());
            cameraResult.setRtspPort(camera.getRtspPort());
            cameraResult.setWebPort(camera.getWebPort());
            cameraResults.add(cameraResult);
        }
        return cameraResults;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    @Override public String toString() {
        return "CameraGroup{" + "groupName='" + groupName + '\'' + ", cameras=" + cameras + ", id="
            + id + '}';
    }
}
