package cn.begonia.sso.ssp.begonia_ssp.service;


import cn.begonia.sso.common.begonia_cmn.common.Result;
import cn.begonia.sso.common.begonia_cmn.entity.SsoUser;

/**
 * 用户服务接口
 * 
 * @author Joe
 */
public interface UserService {
	
	/**
	 * 登录
	 * 
	 * @param userNo
	 *            登录名
	 * @param password
	 *            密码
	 * @return
	 */
	public Result login(String userNo, String password);
}
