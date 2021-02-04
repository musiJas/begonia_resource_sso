package cn.begonia.sso.ssp.begonia_ssp.entity;


import cn.begonia.sso.common.begonia_cmn.entity.SsoUser;

import java.io.Serializable;

public class AuthDto implements Serializable {

	private static final long serialVersionUID = 4587667812642196058L;

	private AuthContent authContent;
	private SsoUser user;

	public AuthDto(AuthContent authContent, SsoUser user) {
		this.authContent = authContent;
		this.user = user;
	}

	public AuthContent getAuthContent() {
		return authContent;
	}

	public void setAuthContent(AuthContent authContent) {
		this.authContent = authContent;
	}

	public SsoUser getUser() {
		return user;
	}

	public void setUser(SsoUser user) {
		this.user = user;
	}
}