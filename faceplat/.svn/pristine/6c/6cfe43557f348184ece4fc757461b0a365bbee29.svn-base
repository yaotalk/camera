package com.minivision.faceplat.redis;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.minivision.faceplat.entity.Face;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class RedisTest {

  @Autowired
  private StringRedisTemplate stringRedisTemplate;

  @Autowired
  private RedisTemplate<String, Face> faceTemplate;

  @Test
  public void test() throws Exception {
    stringRedisTemplate.opsForValue().set("aaa", "111");
    Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
  }

  @Test
  public void test1() throws Exception {
    Face face = new Face();
    face.setToken("1233445");
    faceTemplate.opsForValue().set("face1", face);
    Assert.assertEquals("1233445", faceTemplate.opsForValue().get("face1").getToken());
  }

}
