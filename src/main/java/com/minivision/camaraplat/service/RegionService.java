package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Region;
import com.minivision.camaraplat.service.RegionServiceImpl.TreeNode;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RegionService {
    List<Region> findAll();

    Region create(Region region);

    Region findById(long id);

    Region update(Region region);

    void delete(long id);

    Map<String, TreeNode> groupCameraByRegion();

    List<Region> findNotChildren(Long regionid);

    Set<Region> findChildren(Region region);

}
