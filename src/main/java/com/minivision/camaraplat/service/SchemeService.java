package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Scheme;
import java.util.List;

public interface SchemeService {
    List<Scheme> findAll();

    Scheme findOne(long id);

    Scheme create(Scheme scheme);

    Scheme update(Scheme scheme);

    void delete(String id);
}
