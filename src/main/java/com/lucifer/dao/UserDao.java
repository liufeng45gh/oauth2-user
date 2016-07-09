package com.lucifer.dao;

import com.lucifer.cache.AppCache;
import com.lucifer.cache.CacheProvider;
import com.lucifer.model.AccessToken;
import com.lucifer.model.User;
import com.lucifer.utils.StringHelper;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class UserDao extends IBatisBaseDao {
	
	static final Logger logger = LoggerFactory.getLogger(UserDao.class);
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	private StringRedisTemplate stringRedisTemplate;
	
	@Autowired
	private AppCache appCache;
	
//	@Caching( evict = { 
//	@CacheEvict(value="userByIdCache",allEntries=true),
//     @CacheEvict(value="userByPhoneCache",allEntries=true),
//     @CacheEvict(value="userByWeiboIdCache",allEntries=true),
//     @CacheEvict(value="userByWeixinIdCache",allEntries=true),
//     @CacheEvict(value="userLevelByIdCache",allEntries=true),
//     @CacheEvict(value="userPointCache",allEntries=true)})
	public void removeAllCacheing(){
		logger.info("removeAllCacheing  has been called!!----");
		String keyPattern = "getUserByPhone:*";
		appCache.removeAll(keyPattern);
		keyPattern = "getUserByWeiboId:*";
		appCache.removeAll(keyPattern);
		keyPattern = "getUserByWeixinId:*";
		appCache.removeAll(keyPattern);
		keyPattern = "getUserById:*";
		appCache.removeAll(keyPattern);
		keyPattern = "getUserPoint:*";
		appCache.removeAll(keyPattern);
		keyPattern = "getUserLevel:*";
		appCache.removeAll(keyPattern);
	}

	//@Cacheable(value="userByPhoneCache", key="'userByPhoneCache:'+#phone")//
	public User getUserByAccount(String account){
		logger.info("----getUserByAccount has been called!--");
		//return (User)sqlSession.selectOne("getUserByPhone", phone);
		return sqlSession.selectOne("getUserByAccount", account);

	}
	
	//@Cacheable(value="userByPhoneCache", key="'userByPhoneCache:'+#phone")// 
	public User getUserByPhone(final String phone){
		logger.info("----getUserByPhone has been called!--");
		//return (User)sqlSession.selectOne("getUserByPhone", phone);
		String key = "getUserByPhone:"+phone;
		return appCache.find(key, new CacheProvider() {
			@Override
			public Object getData() {
				return sqlSession.selectOne("getUserByPhone", phone);
			}
		});
	}
	
		
	//@Cacheable(value="userByWeiboIdCache",key="'userByWeiboIdCache:'+#weiboId" )//
	public User getUserByWeiboId(final String weiboId){
		
		String key = "getUserByWeiboId:"+weiboId;
		return appCache.find(key, new CacheProvider() {
			@Override
			public Object getData() {
				return sqlSession.selectOne("getUserByWeiboId", weiboId);
			}
		});
		//return (User)sqlSession.selectOne("getUserByWeiboId", weiboId);
	}
	
	//@Cacheable(value="userByWeixinIdCache", key="'userByWeixinIdCache:'+#weixinId" )//
	public User getUserByWeixinId(final String weixinId){
		String key = "getUserByWeixinId:"+weixinId;
		return appCache.find(key, new CacheProvider() {
			@Override
			public Object getData() {
				return sqlSession.selectOne("getUserByWeixinId", weixinId);
			}
		});
		//return (User)sqlSession.selectOne("getUserByWeixinId", weixinId);
	}
	
	//@Cacheable(value="userByQqIdCache" ,key="'userByQqIdCache:'+#qqId")
	public User getUserByQqId(String qqId){
		return (User)sqlSession.selectOne("getUserByQqId", qqId);
	}
	

	
	//@CacheEvict(value="userByIdCache",key="#user.getUserId()")// 清空Cache 缓存  
	
//	@Caching( put = { 
//			@CachePut(value="userByPhoneCache",key="'userByPhoneCache:'+#user.getPhone()",condition="#user.getPhone() != null"),
//			@CachePut(value="userByWeiboIdCache",key="'userByWeiboIdCache:'+#user.getWeiboId()", condition="#user.getWeiboId() != null"),
//			@CachePut(value="userByWeixinIdCache",key="'userByWeixinIdCache:'+#user.getWeixinId()",condition="#user.getWeixinId() != null")})
	public User insertUser(User user){		
		//this.removeUserCache(user);
		 
		user.setId(this.nextId());
		//user.setNickName(user.getAccount());
		sqlSession.insert("insertUser",user);
		//redisTemplate.opsForList().leftPush(RedisKeyPreConstant.USER_WILL_INSERT_QUEQUE, user);
		 return user;
		
	}
	
//	@Caching( evict = { @CacheEvict(value="userByIdCache",key="'userByIdCache:'+#user.getUserId()"),
//	         @CacheEvict(value="userByPhoneCache",key="'userByPhoneCache:'+#user.getPhone()",condition="#user.getPhone() != null"),
//	         @CacheEvict(value="userByWeiboIdCache",key="'userByWeiboIdCache:'+#user.getWeiboId()", condition="#user.getWeiboId() != null"),
//	         @CacheEvict(value="userByWeixinIdCache",key="'userByWeixinIdCache:'+#user.getWeixinId()",condition="#user.getWeixinId() != null")})
	public void removeUserCache(User user){
		logger.info("removeUserCache has been called");
		String key = "getUserByPhone:"+user.getPhone();
		appCache.remove(key);
		key = "getUserByWeiboId:"+user.getWeiboId();
		appCache.remove(key);
		key = "getUserByWeixinId:"+user.getWeixinId();
		appCache.remove(key);
		key = "getUserById:"+user.getId();
		appCache.remove(key);
		key = "getUserPoint:"+user.getId();
		appCache.remove(key);
		key = "getUserLevel:"+user.getId();
		appCache.remove(key);
		
	}
	

	
	/**
	 * 修改密码
	 * @param user
	 * @return
	 */
	//@CacheEvict(value="userByIdCache",key="#user.getUserId()")// 清空accountCache 缓存  
	public Integer updatePassword(User user){
		String key = "getUserById:"+user.getId();
		appCache.remove(key);
		return sqlSession.update("updatePassword", user);
	}
	
	/**
	 * 绑定手机
	 * @param user
	 * @return
	 */
	//@CacheEvict(value="userByIdCache",key="#user.getUserId()")// 清空accountCache 缓存 
//	@Caching( evict = { @CacheEvict(value="userByIdCache",key="#user.getUserId()"),
//	         @CacheEvict(value="userByPhoneCache",key="#user.getPhone()") })
	public Integer userBindPhone(User user){	
		User dbUser = this.getUserById(user.getId());
		//删除huan
		this.removeUserCache(dbUser);
		return sqlSession.update("userBindPhone", user);
	}
	
	public Integer updateUserWeiboId(User user) {
		User dbUser = this.getUserById(user.getId());
		//删除huan
		this.removeUserCache(dbUser);
		return sqlSession.update("updateUserWeiboId", user);
	}
	
	public Integer updateUserWeixinId(User user) {
		User dbUser = this.getUserById(user.getId());
		//删除huan
		this.removeUserCache(dbUser);
		return sqlSession.update("updateUserWeixinId", user);
	}
	
	public Integer updateUserQqId(User user) {
		User dbUser = this.getUserById(user.getId());
		//删除huan
		this.removeUserCache(dbUser);
		return sqlSession.update("updateUserQqId", user);
	}
	
	

	//@Cacheable(value="userByIdCache" ,key="'userByIdCache:'+#userId")// 
	public User getUserById(final Long userId){
		String key = "getUserById:"+userId;
		return appCache.find(key, new CacheProvider() {
			@Override
			public Object getData() {
				return sqlSession.selectOne("getUserById", userId);
			}
		});
		//return sqlSession.selectOne("getUserById", userId);
	}
	

	//@CacheEvict(value="userByIdCache",key="#user.getUserId()")// 清空accountCache 缓存  
	public Integer updateUserInfo(User user){
		User dbUser = this.getUserById(user.getId());
		this.removeUserCache(dbUser);
		Integer updateCount = sqlSession.update("updateUserInfo",user);		
		return updateCount;
	}
	

	
	public Boolean isNickExist(String nickName){
		Integer resultCount = sqlSession.selectOne("userCountByNickName", nickName);
		if (resultCount>0) {
			return true;
		}
		return false;
	}
	
	public Integer updateUserNick(User user){
		return sqlSession.update("updateUserNick",user);
	}
	
	public Integer initUserNick(User user){
		return sqlSession.update("initUserNick",user);
	}
	
	public Integer allUserCount(){
		Integer resultCount = sqlSession.selectOne("allUserCount");
		return resultCount;
	}



	
	public List<User> userCmsSearch(String sql){
		return this.sqlSession.selectList("userCmsSearch", sql);
	}
	
	public Integer userCmsSearchCount(String sql){
		return this.sqlSession.selectOne("userCmsSearchCount",sql);
	}
	
	public List<User> getAllThirdPartUserList(){
		return this.sqlSession.selectList("getAllThirdPartUserList");
	}
	
	public List<User> getAllNeedBindPhoneUserList(){
		return this.sqlSession.selectList("getAllNeedBindPhoneUserList");
	}
	
	public List<User> getAllPhoneOnlyUserList(){
		return this.sqlSession.selectList("getAllPhoneOnlyUserList");
	}


	public Long getUserIdByToken(String token){
		return sqlSession.selectOne("getUserIdByToken",token);
	}

	public AccessToken resetUserLoginToken(Long userId){
		AccessToken accessToken = new AccessToken();
		String token = RandomStringUtils.randomAlphanumeric(20);
		String code = RandomStringUtils.randomAlphanumeric(20);
		accessToken.setAccessToken(token);
		accessToken.setUserId(userId);
		accessToken.setCode(code);

		this.sqlSession.update("resetUserLoginToken",accessToken);
		return accessToken;
	}

	public void removeToken(String token){
		this.sqlSession.update("removeToken",token);
	}

	public AccessToken getAccessTokenByCode(String code){
		return this.sqlSession.selectOne("getAccessTokenByCode",code);
	}

	public AccessToken getAccessTokenByToken(String accessToken){
		return this.sqlSession.selectOne("getAccessTokenByToken",accessToken);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<User> getUserInfoList(Map params){
		return sqlSession.selectList("getUserInfoList", params);
	}

	@SuppressWarnings("rawtypes")
	public Integer getUserInfoListCount(Map params){
		return (Integer)sqlSession.selectOne("getUserInfoListCount", params);
	}


	public void setUserBlock(User user){
		sqlSession.update("setUserBlock",user);
	}





}
