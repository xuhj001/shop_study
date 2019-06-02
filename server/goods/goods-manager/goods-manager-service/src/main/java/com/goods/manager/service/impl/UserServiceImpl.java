package com.goods.manager.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.goods.manager.dao.JedisClient;
import com.goods.manager.mapper.MuserMapper;
import com.goods.manager.mapper.TbUserMapper;
import com.goods.manager.pojo.TbUserExample.Criteria;
import com.goods.manager.pojo.TbUser;
import com.goods.manager.pojo.TbUserExample;
import com.goods.manager.pojo.Muser;
import com.goods.manager.pojo.MuserExample;
import com.goods.manager.service.UserService;

import com.goods.tools.common.util.CookieUtils;
import com.goods.tools.common.util.ExceptionUtil;
import com.goods.tools.common.util.HttpClientUtil;
import com.goods.tools.common.util.JsonUtils;
import com.goods.tools.common.util.TaotaoResult;

@Service
public class UserServiceImpl implements UserService {

	@Value("${SSO_BASE_URL}")
	public String SSO_BASE_URL;// 单点登录系统的主地址
	@Value("${SSO_LOGIN_URL}")
	public String SSO_LOGIN_URL;// 单点系统的登录服务地址
	@Value("${SSO_TOKEN_URL}")
	private String SSO_TOKEN_URL;// token获取地址

	@Value("${REDIS_USER_SESSION_KEY}")
	private String REDIS_USER_SESSION_KEY;// redis中的key
	@Value("${SSO_SESSION_EXPIRE}")
	private String SSO_SESSION_EXPIRE;// redis中key的过期时间
	@Autowired
	private JedisClient jedisClient;// 缓存
	@Autowired
	private TbUserMapper tbUserMapper;

	public TaotaoResult login(String username, String password, Integer type, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			TbUserExample tbUserExample = new TbUserExample();
			com.goods.manager.pojo.TbUserExample.Criteria criteria = tbUserExample.createCriteria();
			criteria.andUsernameEqualTo(username);
			List<TbUser> list = tbUserMapper.selectByExample(tbUserExample);
			if (list == null || list.size() == 0)
				return TaotaoResult.build(400, "用户不存在");
			TbUser user = list.get(0);
			if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
				// 密码错误
				return TaotaoResult.build(400, "密码错误");
			}
			// 判断用户身份
			if (user.getSeller() != type) {
				switch (type) {
				case 1:
					return TaotaoResult.build(400, "该用户不是卖家，请先注册成卖家");
				case 2:
					return TaotaoResult.build(400, "该用户不是管理员");
				}
			}
			// 加入redis中
			// 生成token
			String token = UUID.randomUUID().toString();
			// 保存用户之前，把用户对象中的密码清空。
			// 只返回一些不隐秘的信息
			user.setPassword(null);
			// 把用户信息写入redis
			jedisClient.set(REDIS_USER_SESSION_KEY + ":" + token, JsonUtils.objectToJson(user));
			// 设置session的过期时间
			jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, Integer.parseInt(SSO_SESSION_EXPIRE));

			// 添加写cookie的逻辑，cookie的有效期是关闭浏览器就失效。
			// 网页版需要加入cookies中,即卖家需要加入cookies中
			// 只有卖家进行登录时才需要加入cookies中
			CookieUtils.setCookie(request, response, "TT_TOKEN", token);
			CookieUtils.setCookie(request, response, "userId", user.getId() + "");
			// 移动端不需要
			// 返回token
			request.setAttribute("user", user);
			return TaotaoResult.ok(token);
		} catch (Exception e) {
			e.printStackTrace();
			return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
		}

	}

	// 获取cookies中token的信息，通过redis获取
	public TbUser getUserByToken(String token) {
		String string = jedisClient.get(REDIS_USER_SESSION_KEY + ":" + token);
		if (!StringUtils.isEmpty(string)) {
			// redis中不为空
			TbUser tbUser = JsonUtils.jsonToPojo(string, TbUser.class);
			return tbUser;
		}
		return null;
	}

	// 用户登出
	public TaotaoResult logout(HttpServletRequest request, HttpServletResponse response) {
		try {
			CookieUtils.deleteCookie(request, response, "TT_TOKEN");
			return TaotaoResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return TaotaoResult.build(400, "登出失败，请稍后重试");
	}

}
