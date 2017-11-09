package com.minivision.cameraplat.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class EntranceGuard extends IdEntity {

   @Column(unique = true)
   private String serialNumber;
   private int deviceType;
   private int gates;
   private String ip;
   private int devicePort;
   private int controlAddress;
   private int controlType;
   private String controlPassword;
   @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE,CascadeType.MERGE}, fetch = FetchType.EAGER)
   private List<Door> doors;

   public List<Door> getDoors() {
      return doors;
   }

   @Entity(name = "door")
   public  static class Door extends IdEntity{
      private int doorNumber;
      public Door() {
      }
      public Door(int doorNumber) {
         this.doorNumber = doorNumber;
      }

      public int getDoorNumber() {
         return doorNumber;
      }

      public void setDoorNumber(int doorNumber) {
         this.doorNumber = doorNumber;
      }
   }

   public void setDoors(List<Door> doors) {
      this.doors = doors;
   }

   public int getDeviceType() {
      return deviceType;
   }

   public void setDeviceType(int deviceType) {
      this.deviceType = deviceType;
   }

   public int getGates() {
      return gates;
   }

   public void setGates(int gates) {
      this.gates = gates;
   }

   public String getIp() {
      return ip;
   }

   public void setIp(String ip) {
      this.ip = ip;
   }

   public int getDevicePort() {
      return devicePort;
   }

   public void setDevicePort(int devicePort) {
      this.devicePort = devicePort;
   }

   public int getControlAddress() {
      return controlAddress;
   }

   public void setControlAddress(int controlAddress) {
      this.controlAddress = controlAddress;
   }

   public int getControlType() {
      return controlType;
   }

   public void setControlType(int controlType) {
      this.controlType = controlType;
   }

   public String getControlPassword() {
      return controlPassword;
   }

   public void setControlPassword(String controlPassword) {
      this.controlPassword = controlPassword;
   }
   public String getSerialNumber() {
      return serialNumber;
   }

   public void setSerialNumber(String serialNumber) {
      this.serialNumber = serialNumber;
   }

   @Override public String toString() {
      return "Access{ id= " + id + '\'' + ",DeviceNo='" + serialNumber + '\'' + ", DeviceType="
          + deviceType + ", Door Amount=" + gates + ", ip='" + ip + '\'' + ", DevicePort=" + devicePort
          + ", Controller Address =" + controlAddress + ", Controller Type =" + controlType
          + ", Controller Password ='" + controlPassword + '\'' + '}';
   }
}
