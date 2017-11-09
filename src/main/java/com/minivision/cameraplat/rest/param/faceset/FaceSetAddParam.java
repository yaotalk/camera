package com.minivision.cameraplat.rest.param.faceset;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class FaceSetAddParam {

    @NotNull(message = "name is required")
    private String disPlayName;

    @Min(1)
    private int capacity = 1000;

    private int priority;

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getDisPlayName() {
        return disPlayName;
    }

    public void setDisPlayName(String disPlayName) {
        this.disPlayName = disPlayName;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override public String toString() {
        return "FaceSetAddParam{" + "disPlayName='" + disPlayName + '\'' + ", capacity=" + capacity
            + ", priority=" + priority + '}';
    }
}
