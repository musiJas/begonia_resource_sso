package cn.begonia.sso.ssp.begonia_ssp.service.impl;

import cn.begonia.sso.common.begonia_cmn.common.Result;
import cn.begonia.sso.common.begonia_cmn.entity.SsoUser;
import cn.begonia.sso.ssp.begonia_ssp.entity.User;
import cn.begonia.sso.ssp.begonia_ssp.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	private static List<User> userList;
	
	static {
		userList = new ArrayList<>();
		userList.add(new User(10000L, "13508687083","测试","123456",1,"测试地址","",new Date()));

	}
	
	@Override
	public Result login(String userNo, String password) {
		for (User user : userList) {
			if (user.getUserNo().equals(userNo)) {
				if(user.getPassword().equals(password)) {
					return Result.isOk(new SsoUser( user.getUserNo(),user.getPassword()));
				}
				else {
					return Result.isFail("密码有误");
				}
			}
		}
		return Result.isFail("用户不存在");
	}
}
