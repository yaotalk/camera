package com.minivision.cameraplat.rest.param.faceset;

import javax.validation.constraints.NotNull;

public class FaceSetUpdateParam {

    @NotNull
    private String faceSetToken;

    private int capacity;

    private String disPlayName;

    private int priority;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getFaceSetToken() {
        return faceSetToken;
    }

    public void setFaceSetToken(String faceSetToken) {
        this.faceSetToken = faceSetToken;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDisPlayName() {
        return disPlayName;
    }

    public void setDisPlayName(String disPlayName) {
        this.disPlayName = disPlayName;
    }

    @Override public String toString() {
        return "FaceSetUpdateParam{" + "faceSetToken='" + faceSetToken + '\'' + ", capacity="
            + capacity + ", disPlayName='" + disPlayName + '\'' + ", priority=" + priority + '}';
    }
}
