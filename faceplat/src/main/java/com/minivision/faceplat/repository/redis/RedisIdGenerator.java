package com.minivision.faceplat.repository.redis;

import java.util.UUID;

public class RedisIdGenerator {
  
  public static final String nextId(){
    return UUID.randomUUID().toString();
  }

}
