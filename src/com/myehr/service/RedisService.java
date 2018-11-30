package com.myehr.service;

/**
 * Redis 操作接口
 * @author Administrator
 *
 */

public interface RedisService {

	public String get(String key);
	
	public byte[] get(byte[] byteKey);
 	
	public String set(String key, String value);
	
	public String set(byte[] byteKey, byte[] byteValue);
	
	public long del(byte[] byteKey);
	
	public String hget(String hkey, String key) ;

	public long hset(String hkey, String key, String value);

}
