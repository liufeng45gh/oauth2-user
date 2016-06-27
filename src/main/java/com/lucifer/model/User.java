package com.lucifer.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.lucifer.utils.Constant;
import com.lucifer.utils.DateTimeSerializer;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1183367815938163456L;
	
	static final Logger logger = LoggerFactory.getLogger(User.class);

	// id        自增长(长整,以下所有id字段皆为该规则)
	private Long userId;
	
	// uuid      uuid生成规则(App侧调用)
	private String uuid;
	
	// cloud_id  云平台绑定ID(LeanCloud),允许为空
	private String cloudId;
	
	// weixin_id 微信绑定ID,允许为空
	private String weixinId;
	
	// weibo_id  微博绑定ID,允许为空
	private String weiboId;
	
	// qq_id     QQ绑定ID,允许为空
	private String qqId;
	

	
	// account   账号(系统自动生成规则QQ+?  WX+?  WB+? Phone+?) 极客学院的案例:weibo_1xvrf8hs
	private String account;
	
	// phone     绑定手机,允许为空
	private String phone;
	
	// mail      绑定邮箱,允许为空
	private String mail;
	
	// password  密码,(加密后的密码,非明文),允许为空
	private String password;
	
	//重复密码
	private String rePassword;
	
	// salt      加密盐,6位随机数字字母组合,允许为空
	private String salt;
	
	// app_tag   在不同的App上的激活标签
	private String role;
	
	//验证码
	private String code;
	
	//第三方token
	private String accessToken;
	
	// nick_name 昵称
	private String nickName;
	
	// avatar    头像图片路径
	private String avatar;
	
	// true_name 真实姓名
	private String trueName;
	
	// sex       性别 1:男 2:女 3:保密
	private String sex;
	
	// province  所在省份
	private String province;
	
	// city      所在城市
	private String city;
	
	// card_id   身份证号码
	private String cardId;
	
	// birth     出生年月日
	
	private Date birth;
	
	// fans_count 用户的粉丝数
	private Integer fansCount;

	// followers  用户关注的人数
	private Integer followsCount;
	
	private Integer nickUpTimes;
	

	

	

	
	// 创建时间（记录）
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	protected  Date createdAt;
	
	// 更新时间（记录）
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	
	protected Date updatedAt;
	

	
//	// level     用户等级
//	private String level;
//	
//	// points    用户积分
//	private Integer points;
	
	public User() {
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	public String getWeixinId() {
		return weixinId;
	}

	public void setWeixinId(String weixinId) {
		this.weixinId = weixinId;
	}

	public String getWeiboId() {
		return weiboId;
	}

	public void setWeiboId(String weiboId) {
		this.weiboId = weiboId;
	}

	public String getQqId() {
		return qqId;
	}

	public void setQqId(String qqId) {
		this.qqId = qqId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRePassword() {
		return rePassword;
	}

	public void setRePassword(String rePassword) {
		this.rePassword = rePassword;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonSerialize(using=DateSerializer.class)
	public Date getBirth() {
		return birth;
	}
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")	
	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public Integer getNickUpTimes() {
		return nickUpTimes;
	}

	public void setNickUpTimes(Integer nickUpTimes) {
		this.nickUpTimes = nickUpTimes;
	}
	
	public boolean getCanUpNick(){
		if (null == nickUpTimes) {
			return true;
		}
		if (nickUpTimes<1) {
			return true;
		}
		return false;
	}
		
	public void setCanUpNick(Boolean canUpNick){
		//do nothing
	}

	

	public Integer getFansCount() {
		return fansCount;
	}

	public void setFansCount(Integer fansCount) {
		this.fansCount = fansCount;
	}




	
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using=DateTimeSerializer.class)
	public Date getCreatedAt() {
		if (null == createdAt) {
			createdAt = Constant.firstOnlineDate;
		}
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using=DateTimeSerializer.class)
	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	



	
}
