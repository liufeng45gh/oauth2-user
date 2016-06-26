package com.lucifer.dao;

import com.lucifer.utils.IdUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class IBatisBaseDao {	
	//config
	@Autowired
	protected SqlSession sqlSession;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	public Long nextId(String sequence){
		return stringRedisTemplate.opsForValue().increment(sequence, 1L);
	}

	public Long nextId(){
		return IdUtil.nextId();
	}
		
}
