package com.myehr.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.myehr.service.RedisService;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisServiceImpl implements RedisService {

	private static Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
	
	private JedisPool jedisPool;
	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}
	
	public Jedis getResource(){
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			// xxx 业务代码
			// 原来代码：pool.returnResource(jedis);，应该放在finally块中，否则每次发生异常将导致一个jedis对象没有被t
		} catch (RuntimeException e) {
			logger.error(e.getMessage(),e);
			if(jedis != null ) {
				jedisPool.returnBrokenResource(jedis);
			}
			return null;
			
		} finally{
			if(jedis != null ) {
				jedisPool.returnResource(jedis);
			}
		}
		return jedis;
	}
	
	@Override
	public long del(byte[] byteKey) {
		return getResource()==null?0:getResource().del(byteKey);
	}

	@Override
	public String get(String key) {
		return getResource()==null?null: getResource().get(key);
	}
	
	@Override
	public byte[] get(byte[] byteKey) {
		return getResource()==null?null: getResource().get(byteKey);
	}
 
	@Override
	public String set(String key, String value) {
		return  getResource()==null?null: getResource().set(key,value);
	}
	
	@Override
	public String set(byte[] byteKey, byte[] byteValue) {
		return getResource()==null?null: getResource().set(byteKey,byteValue);
	}
 
	@Override
	public String hget(String hkey, String key) {
		return getResource()==null?null: getResource().hget(hkey, key);
	}
 
	@Override
	public long hset(String hkey, String key, String value) {
		return getResource()==null?null: getResource().hset(hkey, key,value);
	}

}
