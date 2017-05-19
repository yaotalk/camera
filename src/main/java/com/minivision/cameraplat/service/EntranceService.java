package com.minivision.cameraplat.service;

import com.minivision.cameraplat.domain.EntranceGuard;

import java.util.List;

public interface EntranceService {

    List<EntranceGuard> findAll();

    EntranceGuard findBySerialNumber(String serialNumber);

    void save(EntranceGuard entranceGuard);

    void delete(EntranceGuard entranceGuard);

    void update(EntranceGuard entranceGuard);

    List<EntranceGuard.Door> findByDoorIds(List<Long> ids);

}
