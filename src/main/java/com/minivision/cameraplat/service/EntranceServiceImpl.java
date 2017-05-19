package com.minivision.cameraplat.service;

import com.minivision.cameraplat.domain.Camera;
import com.minivision.cameraplat.domain.EntranceGuard;
import com.minivision.cameraplat.domain.EntranceGuard.Door;
import com.minivision.cameraplat.repository.CameraRepository;
import com.minivision.cameraplat.repository.DoorRepository;
import com.minivision.cameraplat.repository.EntranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

//import com.minivision.cameraplat.repository.DoorRepository;
//import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional
public class EntranceServiceImpl implements EntranceService {

    @Autowired
    private EntranceRepository entranceRepository;

    @Autowired
    private DoorRepository doorRepository;

    @Autowired
    private CameraRepository cameraRepository;

    @Override public List<EntranceGuard> findAll() {
        return entranceRepository.findAll();
    }

    @Override public void save(EntranceGuard entranceGuard) {
            List<Door> doors = new ArrayList<>();
            for(int i =1;i<= entranceGuard.getGates();i++){
                 Door door = new Door(i);
                 doors.add(door);
            }
            entranceGuard.setDoors(doors);
            entranceRepository.save(entranceGuard);
    }

    @Override public void delete(EntranceGuard entranceGuard) {
           EntranceGuard old_entranceGuard = entranceRepository.findOne(entranceGuard.getId());
           List<Door> doors = old_entranceGuard.getDoors();
           List<Long> ids = new ArrayList<>();
           if(doors !=null) {
            for(Door door : doors){
                ids.add(door.getId());
              }
              // doorRepository.deleteByIdIn(ids);
           }
           entranceGuard.setDoors(null);
           entranceRepository.delete(entranceGuard);
    }

    @Override public void update(EntranceGuard entranceGuard) {
           EntranceGuard oldEntranceGuard = entranceRepository.findOne(entranceGuard.getId());
           int oldGates = oldEntranceGuard.getGates();
           int count = oldGates-entranceGuard.getGates();
           List<Door> doorList = oldEntranceGuard.getDoors();
           List<Long> idlist = new ArrayList<>();
           if(count > 0){
               for(int i=oldGates-1;i>=entranceGuard.getGates();i--){
                   Door door = doorList.get(i);
                   idlist.add(door.getId());
                   List<Camera> cameras = cameraRepository.findBydoorsId(door.getId());
                   for(Camera camera : cameras){
                       camera.getDoors().remove(door);
                   }
                   doorList.remove(i);
               }
               doorRepository.deleteByIdIn(idlist);
           }
           else if(count < 0){
                for(int i=oldGates;i < -count+oldGates;i++){
                     Door door = new Door(i+1);
                     doorList.add(door);
                }
           }
           entranceGuard.setDoors(doorList);
           entranceRepository.save(entranceGuard);
    }

    @Override public List<Door> findByDoorIds(List<Long> ids) {
          return doorRepository.findByIdIn(ids);
    }

    @Override
    public EntranceGuard findBySerialNumber(String serialNumber){
        return entranceRepository.findBySerialNumber(serialNumber);
    }


}
