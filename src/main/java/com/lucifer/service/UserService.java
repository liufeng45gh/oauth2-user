package com.lucifer.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.lucifer.dao.UserDao;
import com.lucifer.exception.ParamException;
import com.lucifer.model.SearchParam;
import com.lucifer.model.User;
import com.lucifer.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {
	

	@Resource
	private UserDao userDao;
	
	final Logger logger = LoggerFactory.getLogger(this.getClass());

	public User getUserByToken(String token){
		return null;
	}
	
	public Long getUserIdByToken(String token){
//		String _userId = stringRedisTemplate.opsForValue().get(RedisKeyPreConstant.USER_TOKEN_PRE+token);
//		if (null == _userId) {
//			return null;
//		}
//		Long userId = Long.valueOf(_userId);
//		return userId;
		return userDao.getUserIdByToken(token);
	}
	
		
	public Result updateUserInfo(String token, User user) throws IOException{
		 //Long userId = this.getUserIdByToken(token);
		 //userInfo.setUserId(userId);
		Result result = this.updateNick(user);
		if (!result._isOk()) {
			return result;
		}
		 userDao.updateUserInfo(user);
		 
		 return Result.ok();
	}
	

	
	/**
	 * 初始化设置
	 * @param userId
	 */
	public void userInit(Long userId){
		//userDao.setUserPoint(userId, 5);
	}
	
	public Result updateNick(User user) throws IOException{
		User dbUser = userDao.getUserById(user.getId());
		
		if (StringHelper.isEmpty(user.getNickName())) {
			return Result.ok();
		}
		
		if (user.getNickName().equals(dbUser.getNickName())) {
			return Result.ok();
		}
		if (!dbUser.getCanUpNick()) {
			return Result.fail("昵称已经修改过一次以上");
		}
		if (userDao.isNickExist(user.getNickName())) {
			return Result.fail("昵称已被其他人占用");
		}
//		JSONObject jsonObject = sensitiveWordFilter.checkWord(user.getNickName());
//		if (jsonObject.getBoolean("contain")) {
//			 String message = "含有敏感词: "+jsonObject.getJSONArray("keywords");
//			 return Result.fail(message);
//		}
		
		userDao.updateUserNick(user);
		return Result.ok();
	}
	
	public List userCmsSearch(SearchParam param){
		String sql = "select * from user left outer join user_level on user.id =user_level.user_id  where 1=1 ";
		if (!StringHelper.isEmpty(param.getAccount())) {
			sql = sql + "and user.account like '"+param.getAccount()+"%' ";
		}
		if (!StringHelper.isEmpty(param.getNickName())) {
			sql = sql + "and user.nick_name like '"+param.getNickName()+"%' ";
		}
		if (!StringHelper.isEmpty(param.getCity())) {
			sql = sql + "and user.city = '"+param.getCity()+"' ";
		}
		if (!StringHelper.isEmpty(param.getPhone())) {
			sql = sql + "and user.phone = '"+param.getPhone()+"' ";
		}
		if (!StringHelper.isEmpty(param.getSex())) {
			sql = sql + "and user.sex = '"+param.getSex()+"' ";
		}
		if (!StringHelper.isEmpty(param.getBirthday())) {
			sql = sql + "and user.birth = '"+param.getBirthday()+"' ";
		}
		if (null != param.getLevel()) {
			sql = sql + "and user_level = '"+param.getLevel()+"' ";
		}
		sql = sql + "order by user.id desc limit "+param.getOffset()+","+param.getCount();
		
		if (null != param.getCheckRank()) {
			sql = String.format("select * from user where id = (select user_id from user_checkin_rank order by max_in_days desc limit %s,1)", param.getCheckRank().toString());
		}
		
		logger.info("sql is : "+sql);
		
		List<User> userList = userDao.userCmsSearch(sql);
		ObjectMapper objectMapper = new ObjectMapper();
		List list = new ArrayList();
	    for (User user: userList) {
	    	 @SuppressWarnings("unchecked")
	 	    Map<String, Object> objectAsMap = objectMapper.convertValue(user, Map.class);
//	    	 UserLevel userLevel = userDao.getUserLevel(user.getUserId());
//	    	 if (null !=  userLevel) {
//	    		 objectAsMap.put("level", userLevel.getLevel());
//	    	 }
//
//	    	 UserCheckinRank userCheckRank = userCheckinDao.getUserCheckinRankWithIndex(user.getUserId());
//	    	 if (null != userCheckRank) {
//	    		 objectAsMap.put("userCheckRank",userCheckRank.getIndex());
//	    	 }
	    	 list.add(objectAsMap);
	    }
	   
		
		return list;
	}
	
	public Integer userCmsSearchCount(SearchParam param){
		String sql = "select count(*) from user left outer join user_level on user.id =user_level.user_id  where 1=1 ";
		if (!StringHelper.isEmpty(param.getAccount())) {
			sql = sql + "and user.account like '"+param.getAccount()+"%' ";
		}
		if (!StringHelper.isEmpty(param.getNickName())) {
			sql = sql + "and user.nick_name like '"+param.getNickName()+"%' ";
		}
		if (!StringHelper.isEmpty(param.getCity())) {
			sql = sql + "and user.city = '"+param.getCity()+"' ";
		}
		if (!StringHelper.isEmpty(param.getPhone())) {
			sql = sql + "and user.phone = '"+param.getPhone()+"' ";
		}
		if (!StringHelper.isEmpty(param.getSex())) {
			sql = sql + "and user.sex = '"+param.getSex()+"' ";
		}
		if (!StringHelper.isEmpty(param.getBirthday())) {
			sql = sql + "and user.birth = '"+param.getBirthday()+"' ";
		}
		if (null != param.getLevel()) {
			sql = sql + "and user_level = '"+param.getLevel()+"' ";
		}
		
		
		if (null != param.getCheckRank()) {
			return 1;
		}
		
		logger.info("sql is : "+sql);
		
		Integer recordCount  = userDao.userCmsSearchCount(sql);
		
		
		return recordCount;
	}
	
	
	public User getUserInfo(Long id){
		User user = userDao.getUserById(id);
		if (null != user) {
			user.setPassword(null);
			user.setSalt(null);
		}
		return user;
	}

	public User createUser(User user) throws ParamException {
		if (StringHelper.isEmpty(user.getAccount())) {
			 throw new ParamException("账号传入不能为空");
		}
		if (StringHelper.isEmpty(user.getPassword())) {
			throw new ParamException("密码传入不能为空");
		}
		if (!user.getPassword().equals(user.getRePassword())) {
			throw new ParamException("2次密码不一致");
		}
		String salt = RandomUtil.getNextSalt();
		user.setSalt(salt);
		String encrypt_password = Md5Utils.md5(Md5Utils.md5(user.getPassword()) + salt);
		user.setPassword(encrypt_password);
		UUID uuid = UUID.randomUUID();
		user.setUuid(uuid.toString());
		user.setCreatedAt(DateUtils.now());
		user.setUpdatedAt(DateUtils.now());
		userDao.insertUser(user);
		return user;
	}

	

	
	
	
}

