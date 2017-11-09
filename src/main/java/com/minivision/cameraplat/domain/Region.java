package com.minivision.cameraplat.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@ApiModel
public class Region extends IdEntity{
  private String name; // 区域名称
  @ManyToOne(optional = true)
  @JoinColumn(name = "parent_id", nullable = true)
  @ApiModelProperty(hidden = true)
  private Region parentNode; // 父节点

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  public Region getParentNode() {
    return parentNode;
  }

  public Region setParentNode(Region parentNode) {
    this.parentNode = parentNode;
    return this;
  }
  
  public String getFullName(){
    if(parentNode!=null){
      return parentNode.getFullName()+"/"+name;
    }else{
      return name;
    }
  }
  @Override public String toString() {
    return "Area { id='" + id + '\''+ ", Name='" + name + '\'' + ", ParentNode=" + (parentNode==null?null:parentNode.getId()) + '}';
  }
}
