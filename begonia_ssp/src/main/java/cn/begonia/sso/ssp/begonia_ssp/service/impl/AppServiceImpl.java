package cn.begonia.sso.ssp.begonia_ssp.service.impl;

import cn.begonia.sso.common.begonia_cmn.common.Result;
import cn.begonia.sso.ssp.begonia_ssp.entity.App;
import cn.begonia.sso.ssp.begonia_ssp.service.AppService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("appService")
public class AppServiceImpl implements AppService {

	private static List<App> appList;

	static {
		appList = new ArrayList<>();
		appList.add(new App("服务端1", "server1", "123456"));
		appList.add(new App("客户端1", "mgr", "123456"));
		appList.add(new App("客户端2", "per", "123456"));
	}

	@Override
	public boolean exists(String clientId) {
		return appList.stream().anyMatch(app -> app.getClientId().equals(clientId));
	}

	@Override
	public Result validate(String clientId, String appSecret) {
		for (App app : appList) {
			if (app.getClientId().equals(clientId)) {
				if (app.getAppSecret().equals(appSecret)) {
					return Result.isOk();
				}
				else {
					return Result.isFail("appSecret有误");
				}
			}
		}
		return Result.isFail("appId不存在");
	}
}
