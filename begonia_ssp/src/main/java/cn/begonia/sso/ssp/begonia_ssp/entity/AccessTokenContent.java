package cn.begonia.sso.ssp.begonia_ssp.entity;

public class AccessTokenContent extends AuthContent {

	private static final long serialVersionUID = -1332598459045608781L;

	public AccessTokenContent(String tgt, boolean sendLogoutRequest, String redirectUri) {
		super(tgt, sendLogoutRequest, redirectUri);
	}
}