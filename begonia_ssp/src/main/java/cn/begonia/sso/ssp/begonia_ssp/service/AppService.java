package cn.begonia.sso.ssp.begonia_ssp.service;


import cn.begonia.sso.common.begonia_cmn.common.Result;

/**
 * 应用服务接口
 * 
 * @author Joe
 */
public interface AppService {

	boolean exists(String appId);
	
	Result validate(String appId, String appSecret);
}
