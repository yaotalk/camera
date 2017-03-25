package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Scheme;

public interface PeriodService {
    void delete(Long period);
    void save(Scheme.Period period);
}
