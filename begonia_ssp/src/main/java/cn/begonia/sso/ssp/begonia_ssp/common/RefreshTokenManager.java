package cn.begonia.sso.ssp.begonia_ssp.common;

import cn.begonia.sso.ssp.begonia_ssp.entity.AuthContent;
import cn.begonia.sso.ssp.begonia_ssp.entity.RefreshTokenContent;

import java.util.UUID;

/**
 * 刷新凭证refreshToken管理抽象
 * 
 * @author Joe
 */
public interface RefreshTokenManager extends Expiration {

	/**
	 * 生成refreshToken
	 * 
	 * @param authContent
	 * @param accessToken
	 * @param appId
	 * @return
	 */
	default String generate(AuthContent authContent, String accessToken, String appId) {
		String resfreshToken = "RT-" + UUID.randomUUID().toString().replaceAll("-", "");
		create(resfreshToken, new RefreshTokenContent(authContent.getTgt(), authContent.isSendLogoutRequest(),
				authContent.getRedirectUri(), accessToken, appId));
		return resfreshToken;
	}

	/**
	 * 生成refreshToken
	 * 
	 * @param refreshToken
	 * @param refreshTokenContent
	 */
	void create(String refreshToken, RefreshTokenContent refreshTokenContent);

	/**
	 * 验证refreshToken有效性，无论有效性与否，都remove掉
	 * 
	 * @param refreshToken
	 * @return
	 */
	RefreshTokenContent validate(String refreshToken);
}
