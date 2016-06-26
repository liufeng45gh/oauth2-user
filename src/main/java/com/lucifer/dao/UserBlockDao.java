package com.lucifer.dao;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.lucifer.cache.AppCache;
import com.lucifer.cache.CacheProvider;
import com.lucifer.model.UserBlock;
import com.lucifer.model.UserBlockSearchParam;
import com.lucifer.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserBlockDao extends IBatisBaseDao{
	
	private static final Logger logger = LoggerFactory.getLogger(UserBlockDao.class);
	
	@Autowired
	private AppCache appCache;
	
	public List<UserBlock> getUserBlockList(UserBlockSearchParam userBlockSearchParam, Integer page, Integer count){
		Integer offset = (page-1)*count;
		Map<String, Object> paramMap = new ObjectMapper().convertValue(userBlockSearchParam, Map.class);
        paramMap.put("offset",offset);
        paramMap.put("count",count);
		return this.sqlSession.selectList("getUserBlockList",paramMap);
	}
	
	public Integer getUserBlockListCount(UserBlockSearchParam userBlockSearchParam){
		return this.sqlSession.selectOne("getUserBlockListCount",userBlockSearchParam);
	}
	
	public UserBlock insertUserBlock(UserBlock userBlock){
		userBlock.setCreatedAt(DateUtils.now());
		userBlock.setUpdatedAt(DateUtils.now());
		this.sqlSession.insert("insertUserBlock",userBlock);
		String key = this.getUserBlockByKey(userBlock.getUserId());
		appCache.set(key, userBlock);
		return userBlock;
	}
	
	public Integer delUserBlock(Long userId){
		Integer deletedCount = this.sqlSession.delete("delUserBlock",userId);
		String key = this.getUserBlockByKey(userId);
		appCache.remove(key);
		return deletedCount;
	}
	
	public UserBlock getUserBlockFromCache(Long userId){
		logger.info("----getUserBlockFromCache has been called!--");
		//return (User)sqlSession.selectOne("getUserByPhone", phone);
		String key = this.getUserBlockByKey(userId);
		return appCache.find(key, new CacheProvider() {
			@Override
			public Object getData() {
				return null;
			}
		});
	}
	
	public String getUserBlockByKey(Long userId) {
		String key = "getUserBlockFromCache:"+userId;
		return key;
	}

}
