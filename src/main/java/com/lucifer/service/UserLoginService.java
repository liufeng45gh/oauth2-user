package com.lucifer.service;


import com.lucifer.dao.UserDao;
import com.lucifer.exception.LoginException;
import com.lucifer.exception.NoAuthException;
import com.lucifer.model.AccessToken;
import com.lucifer.model.User;
import com.lucifer.utils.*;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserLoginService {

	
	@Resource
	private UserService userService;
	
	@Resource
	private UserDao userDao;
	
	
	private static  Log log = LogFactory.getLog(UserLoginService.class);


	/**
	 * 总登陆接口
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation= Propagation.REQUIRED)
	public Result login(User user) throws Exception{
		if (!StringHelper.isEmpty(user.getPhone())) {//手机号登录
			return this.loginByPhone(user);
		}
		if (!StringHelper.isEmpty(user.getWeiboId())) {//微博登录
			return this.loginByWeiBo(user);
		}
		if (!StringHelper.isEmpty(user.getWeixinId())) {//微信登陆
			return this.loginByWeiXin(user);
		}
		if (!StringHelper.isEmpty(user.getQqId())) {
			return this.loginByQQ(user);
		}
		return Result.fail();
	}
	
	/**
	 * 手机号登录
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	public Result loginByPhone(User user) throws Exception{

		User dbUser = userDao.getUserByPhone(user.getPhone());
		if  (null == dbUser)  {
			return Result.fail("用户未找到");
		}
		String md5Password = Md5Utils.md5(Md5Utils.md5(user.getPassword())+dbUser.getSalt());
		if (!md5Password.equals(dbUser.getPassword())) {
			return Result.fail("密码错误");
		}

		AccessToken accessToken = userDao.resetUserLoginToken(dbUser.getUserId());
		return this.loginSuccess(dbUser,accessToken.getAccessToken());
//		dbUser.setPassword(user.getPassword());
		//user.setPassword(Md5Utils.md5(user.getPassword()));
		
//		String md5Password = Md5Utils.md5(Md5Utils.md5(user.getPassword())+dbUser.getSalt());
//		if (md5Password.equals(dbUser.getPassword())) {
//			
//		}		
//		return Result.fail("密码错误");
	}

	/**
	 * 手机号登录
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public AccessToken oauth2LoginByPhone(User user) throws Exception{

		User dbUser = userDao.getUserByPhone(user.getPhone());
		if  (null == dbUser)  {
			throw new LoginException("用户未找到");
		}
		String md5Password = Md5Utils.md5(Md5Utils.md5(user.getPassword())+dbUser.getSalt());
		if (!md5Password.equals(dbUser.getPassword())) {
			throw new LoginException("密码错误");
		}

		AccessToken accessToken = userDao.resetUserLoginToken(dbUser.getUserId());
		return accessToken;
//		dbUser.setPassword(user.getPassword());
		//user.setPassword(Md5Utils.md5(user.getPassword()));

//		String md5Password = Md5Utils.md5(Md5Utils.md5(user.getPassword())+dbUser.getSalt());
//		if (md5Password.equals(dbUser.getPassword())) {
//
//		}
//		return Result.fail("密码错误");
	}


	/**
	 * 微博登录
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public Result loginByWeiBo(User user) throws Exception{
		String uid = this.getWeiboUidByAccessToken(user.getAccessToken());
		if (!uid.equals(user.getWeiboId())) {
			throw new NoAuthException("微博认证失败,uid 与 传入weiboId 不一致");
		}

		User dbUser = userDao.getUserByWeiboId(user.getWeiboId());

		if (null==dbUser) {	
			String account = "wb_"+RandomUtil.getNextAccount();
			user.setAccount(account);
			UUID uuid = UUID.randomUUID();
			user.setUuid(uuid.toString());
			user.setCreatedAt(DateUtils.now());
			user.setUpdatedAt(DateUtils.now());
			JSONObject weiBoUserInfo = this.getWeiboUserInfo(user.getAccessToken(), user.getWeiboId());
			user.setAvatar(weiBoUserInfo.getString("profile_image_url"));
			userDao.insertUser(user);
			//user.setUserId(Long.valueOf(id));
			dbUser =  userDao.getUserByWeiboId(user.getWeiboId());
			
			//初始化
			userService.userInit(dbUser.getUserId());
		}
		AccessToken accessToken  = userDao.resetUserLoginToken(dbUser.getUserId());
		return  this.loginSuccess(dbUser,accessToken.getAccessToken());
		
	}
	/**
	 * 返回token
	 * @param user
	 * @return
	 * @throws IOException 
	 */
	public Result loginSuccess(User user,String token) throws IOException{
		//String token = RandomStringUtils.randomAlphanumeric(20);
		//stringRedisTemplate.opsForValue().set(RedisKeyPreConstant.USER_TOKEN_PRE+token, user.getUserId().toString());
		//stringRedisTemplate.expire(RedisKeyPreConstant.USER_TOKEN_PRE+token, 10, TimeUnit.DAYS);
		Map resultMap = new HashMap();
		resultMap.put("user", user);
		resultMap.put("token", token);
		user.setPassword(null);
		return Result.ok(resultMap);
	}
	
	public JSONObject  getWeiboUserInfo(String accessToken,String uid) throws IOException, KeyManagementException, NoSuchAlgorithmException, JSONException {
		String url = "https://api.weibo.com/2/users/show.json?access_token="+accessToken+"&uid="+uid;
		String result = HttpsUtil.getAsString(url, "utf-8"); 
		log.info(result);
		return new JSONObject(result);
	}
	
	public String getWeiboUidByAccessToken(String accessToken) throws KeyManagementException, NoSuchAlgorithmException, IOException, JSONException {
		String url = "https://api.weibo.com/2/account/get_uid.json?access_token="+accessToken;
		String jsonString = HttpsUtil.getAsString(url, "utf-8");
		log.info("getWeiboUidByAccessToken body is : "+jsonString);
		JSONObject jsonObject =  new JSONObject(jsonString);
		return jsonObject.getString("uid");
	}
	
	/**
	 * 微信登陆
	 * @param user
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public Result loginByWeiXin(User user) throws Exception{
		Boolean isAvailable = this.checkWeixinToken(user.getAccessToken(), user.getWeixinId());
		if (!isAvailable) {
			return Result.fail("auth  is fail");
		}
//		Result result =   userCenterService.thirdLogin(user);
//		if (!result._isOk()) {
//			return result;
//		}
		//String token = (String)result.getData();
		User dbUser = userDao.getUserByWeixinId(user.getWeixinId());
		

		if (null==dbUser) {		
			String account = "wx_"+RandomUtil.getNextAccount();
			user.setAccount(account);
			UUID uuid = UUID.randomUUID();
			user.setUuid(uuid.toString());
			user.setCreatedAt(DateUtils.now());
			user.setUpdatedAt(DateUtils.now());
			this.syncWeixinUserInfo(user);
			userDao.insertUser(user);
			//user.setUserId(Long.valueOf(id));
			
			//user.setNickName("用户"+user.getUserId());
			//userDao.initUserNick(user);
			//userDao.updateUserInfo(user);
			//user.setUserId(userId);
			//初始化
			userService.userInit(user.getUserId());
			dbUser =  userDao.getUserByWeixinId(user.getWeixinId());
		}
		AccessToken accessToken =  userDao.resetUserLoginToken(dbUser.getUserId());
		return this.loginSuccess(dbUser,accessToken.getAccessToken());
	}
	
	public Boolean checkWeixinToken(String accessToken,String openId) throws HttpException, IOException, JSONException {
		HttpClient httpClient = new HttpClient();
		GetMethod get = new GetMethod("https://api.weixin.qq.com/sns/auth?access_token="+accessToken+"&openid="+openId);
		httpClient.executeMethod(get);
		Header[] headers = get.getResponseHeaders();
		int statusCode = get.getStatusCode();
		log.info("statusCode:"+statusCode);
		for(Header h : headers){
			//log.info(h);
		}
		String result = new String(get.getResponseBodyAsString().getBytes("utf8")); 
		log.info(result);
		JSONObject resultJ=  new JSONObject(result);
		if  (0 == resultJ.getInt("errcode"))  {
			return true;
		}
		return false;
	}
	
	private void syncWeixinUserInfo(User user) throws  IOException, JSONException {
		JSONObject jsonObject = this.getWeixinUserInfo(user.getAccessToken(), user.getWeixinId());
		user.setAvatar(jsonObject.getString("headimgurl"));
		//user.setNickName(jsonObject.getString("nickname"));
		//user.setCity(jsonObject.getString("city"));
		//user.setProvince(jsonObject.getString("province"));
	}
	
	private JSONObject getWeixinUserInfo(String accessToken,String openId) throws HttpException, IOException, JSONException {
		HttpClient httpClient = new HttpClient();
		GetMethod get = new GetMethod("https://api.weixin.qq.com/sns/userinfo?access_token="+accessToken+"&openid="+openId);
		httpClient.executeMethod(get);
		Header[] headers = get.getResponseHeaders();
		int statusCode = get.getStatusCode();
		log.info("statusCode:"+statusCode);
		for(Header h : headers){
			//log.info(h);
		}
		String result = new String(get.getResponseBodyAsString().getBytes("utf8")); 
		log.info(result);
		JSONObject resultJ=  new JSONObject(result);
		return resultJ;
	}
	
	public Result loginByQQ(User user) throws Exception{
		QQInfo qqInfo = QQInfo.getQQInfo(user.getAccessToken(),user.getQqId());
//		if (qqInfo == null) {
//			throw new NoAuthException("qq认证失败,openId 与 传入qqId 不一致");
//		}
		
		
		if (null == qqInfo) {
			return Result.fail("auth  is fail");
		}

		//String token = (String)result.getData();
		
		User dbUser = userDao.getUserByQqId(user.getQqId());
		
		//按nhid查找匹配用户
		if (null==dbUser) {
			dbUser = userDao.getUserByNhId(user.getNhId());
			if (null != dbUser) {
				dbUser.setQqId(user.getQqId());
				userDao.updateUserQqId(dbUser);
			}	
		}
		
		if (null==dbUser) {		
			String account = "qq_"+RandomUtil.getNextAccount();
			user.setAccount(account);
			UUID uuid = UUID.randomUUID();
			user.setUuid(uuid.toString());
			user.setCreatedAt(DateUtils.now());
			user.setUpdatedAt(DateUtils.now());
			//QQInfo qqInfo = new QQInfo(user.getAccessToken(),openIDObj.getUserOpenID());
			user.setAvatar(qqInfo.getAvatar());
			userDao.insertUser(user);
			//user.setUserId(Long.valueOf(id));
			
			//user.setNickName("用户"+user.getUserId());
			//userDao.initUserNick(user);
			//userDao.updateUserInfo(user);
			//user.setUserId(userId);
			//初始化
			userService.userInit(user.getUserId());
			dbUser =  userDao.getUserByQqId(user.getQqId());
		}
		AccessToken accessToken =   userDao.resetUserLoginToken(dbUser.getUserId());
		return this.loginSuccess(dbUser,accessToken.getAccessToken());
	}

	
	public void logout(String token){
//		stringRedisTemplate.delete(RedisKeyPreConstant.USER_TOKEN_PRE+token);
		userDao.removeToken(token);
	}

	public AccessToken getAccessTokenByCode(String code){
		return userDao.getAccessTokenByCode(code);
	}

	public AccessToken getAccessTokenByToken(String accessToken){
		return userDao.getAccessTokenByToken(accessToken);
	}

}
