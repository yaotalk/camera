package com.minivision.camaraplat.service;

import com.minivision.camaraplat.domain.Scheme;
import com.minivision.camaraplat.domain.Strategy;

import java.util.List;

/**
 * Created by Administrator on 2017/3/13 0013.
 */
public interface StrategyService {
    List<Strategy> findAll();

    Strategy create(Strategy strategy);

    Strategy update(Strategy strategy);

    void delete(String id);

    List<Strategy> findByScheme(Scheme scheme);
}
