package com.minivision.faceplat.rest.result.faceset;

import java.util.List;

import com.minivision.faceplat.entity.FaceSet;

public class SetListResult {

  private List<FaceSet> faceSets;
  
  private long total;

  public List<FaceSet> getFaceSets() {
    return faceSets;
  }

  public void setFaceSets(List<FaceSet> faceSets) {
    this.faceSets = faceSets;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }
  
}
