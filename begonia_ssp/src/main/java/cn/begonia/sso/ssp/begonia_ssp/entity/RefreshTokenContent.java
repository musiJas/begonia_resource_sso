package cn.begonia.sso.ssp.begonia_ssp.entity;

public class RefreshTokenContent extends AccessTokenContent {

	private static final long serialVersionUID = -1332598459045608781L;

	private String accessToken;
	
	private String appId;

	public RefreshTokenContent(String tgt, boolean sendLogoutRequest, String redirectUri, String accessToken,
                               String appId) {
		super(tgt, sendLogoutRequest, redirectUri);
		this.accessToken = accessToken;
		this.appId = appId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}
}