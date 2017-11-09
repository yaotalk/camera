package com.minivision.faceplat.repository.redis;

import java.nio.ByteBuffer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class FutureSerializer implements RedisSerializer<float[]>{
  
  @Override
  public byte[] serialize(float[] future) throws SerializationException {
    if(future == null){
      return null;
    }
    ByteBuffer buffer = ByteBuffer.allocate(future.length * 4);
    for(float f: future){
      buffer.putFloat(f);
    }
    return buffer.array();
  }

  @Override
  public float[] deserialize(byte[] bytes) throws SerializationException {
    if(bytes == null){
      return null;
    }
    ByteBuffer buffer = ByteBuffer.wrap(bytes);
    int size = bytes.length / 4;
    
    float[] fs = new float[size];
    for(int i=0;i<size;i++){
      fs[i] = buffer.getFloat();
    }
    return fs;
  }
  
  
}
