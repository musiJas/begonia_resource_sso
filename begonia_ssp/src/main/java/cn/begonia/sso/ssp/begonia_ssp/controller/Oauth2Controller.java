package cn.begonia.sso.ssp.begonia_ssp.controller;

import cn.begonia.sso.common.begonia_cmn.common.Result;
import cn.begonia.sso.common.begonia_cmn.entity.SsoUser;
import cn.begonia.sso.common.begonia_cmn.util.CookieUtils;
import cn.begonia.sso.flt.begonia_flt.common.Oauth2Constant;
import cn.begonia.sso.flt.begonia_flt.common.SessionAccessToken;
import cn.begonia.sso.flt.begonia_flt.common.SsoConstants;
import cn.begonia.sso.flt.begonia_flt.enums.GrantTypeEnum;
import cn.begonia.sso.ssp.begonia_ssp.common.AccessTokenManager;
import cn.begonia.sso.ssp.begonia_ssp.common.CodeManager;
import cn.begonia.sso.ssp.begonia_ssp.common.RefreshTokenManager;
import cn.begonia.sso.ssp.begonia_ssp.common.TicketGrantingTicketManager;
import cn.begonia.sso.ssp.begonia_ssp.entity.AuthContent;
import cn.begonia.sso.ssp.begonia_ssp.entity.AuthDto;
import cn.begonia.sso.ssp.begonia_ssp.entity.RefreshTokenContent;
import cn.begonia.sso.ssp.begonia_ssp.service.AppService;
import cn.begonia.sso.ssp.begonia_ssp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Oauth2服务管理
 * 
 * @author Joe
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/oauth2")
public class Oauth2Controller {
	
	@Autowired
	private AppService appService;
	@Autowired
	private UserService userService;

	@Autowired
	private CodeManager codeManager;
	@Autowired
	private AccessTokenManager accessTokenManager;
	@Autowired
	private RefreshTokenManager refreshTokenManager;
	@Autowired
	private TicketGrantingTicketManager ticketGrantingTicketManager;
	
	/**
	 * 获取accessToken
	 * 
	 * @param clientId
	 * @param appSecret
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/access_token", method = RequestMethod.GET)
	public Result getAccessToken(
			@RequestParam(value = Oauth2Constant.GRANT_TYPE, required = true) String grantType,
			@RequestParam(value = Oauth2Constant.CLIENT_ID, required = true) String clientId,
			@RequestParam(value = Oauth2Constant.CLIENT_SECRET, required = true) String appSecret,
			@RequestParam(value = Oauth2Constant.AUTH_CODE, required = false) String code,
			@RequestParam(value = Oauth2Constant.USERNAME, required = false) String username,
			@RequestParam(value = Oauth2Constant.PASSWORD, required = false) String password) {
		
		// 校验基本参数
		Result result = validateParam(grantType, code, username, password);
		if (result.getCode()!=200) {
			return result;
		}

		// 校验应用
		Result   appResult = appService.validate(clientId, appSecret);
		if (appResult.getCode()!=200) {
			return appResult;
		}

		// 校验授权
		Result authResult = validateAuth(grantType, code, username, password);
		if (authResult.getCode()!=200) {
			return authResult;
		}
		AuthDto authDto = (AuthDto) authResult.getObj();
		
		// 生成RpcAccessToken返回
		return Result.isOk(generateRpcAccessToken(authDto.getAuthContent(), authDto.getUser(), clientId, null));
	}
	
	private Result validateParam(String grantType, String code, String username, String password) {
		if (GrantTypeEnum.AUTHORIZATION_CODE.getValue().equals(grantType)) {
			if (StringUtils.isEmpty(code)) {
				return Result.isFail("code不能为空");
			}
		}
		else if (GrantTypeEnum.PASSWORD.getValue().equals(grantType)) {
			if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
				return Result.isFail("username和password不能为空");
			}
		}
		else {
			return Result.isFail("授权方式不支持");
		}
		return Result.isOk();
	}
	
	private Result  validateAuth(String grantType, String code, String username, String password) {
		AuthDto authDto = null;
		if (GrantTypeEnum.AUTHORIZATION_CODE.getValue().equals(grantType)) {
			AuthContent authContent = codeManager.validate(code);
			if (authContent == null) {
				return Result.isFail("code有误或已过期");
			}

			SsoUser user = ticketGrantingTicketManager.get(authContent.getTgt());
			if (user == null) {
				return Result.isFail("服务端session已过期");
			}
			authDto = new AuthDto(authContent, user);
		}else if (GrantTypeEnum.PASSWORD.getValue().equals(grantType)) {
			// app通过此方式由客户端代理转发http请求到服务端获取accessToken
			Result  loginResult = userService.login(username, password);
			if (loginResult.getCode()!=200) {
				return Result.isFail(loginResult.getMsg());
			}
			SsoUser user = (SsoUser) loginResult.getObj();
			String tgt = ticketGrantingTicketManager.generate((SsoUser) loginResult.getObj());
			AuthContent authContent = new AuthContent(tgt, false, null);
			
			authDto = new AuthDto(authContent, user);
		}
		return Result.isOk(authDto);
	}
	
	/**
	 * 刷新accessToken，并延长TGT超时时间
	 * 
	 * accessToken刷新结果有两种：
	 * 1. 若accessToken已超时，那么进行refreshToken会生成一个新的accessToken，新的超时时间；
	 * 2. 若accessToken未超时，那么进行refreshToken不会改变accessToken，但超时时间会刷新，相当于续期accessToken。
	 * 
	 * @param appId
	 * @param refreshToken
	 * @return
	 */
	@RequestMapping(value = "/refresh_token", method = RequestMethod.GET)
	public Result refreshToken(
			@RequestParam(value = Oauth2Constant.CLIENT_ID, required = true) String appId,
			@RequestParam(value = Oauth2Constant.REFRESH_TOKEN, required = true) String refreshToken) {
		if(!appService.exists(appId)) {
			return Result.isFail("非法应用");
		}
		
		RefreshTokenContent refreshTokenContent = refreshTokenManager.validate(refreshToken);
		if (refreshTokenContent == null || !appId.equals(refreshTokenContent.getAppId())) {
			return Result.isFail("refreshToken有误或已过期");
		}

		SsoUser user = ticketGrantingTicketManager.get(refreshTokenContent.getTgt());
		if (user == null) {
			return Result.isFail("服务端session已过期");
		}

		return Result.isOk(
				generateRpcAccessToken(refreshTokenContent, user, appId, refreshTokenContent.getAccessToken()));
	}
	
	private SessionAccessToken generateRpcAccessToken(AuthContent authContent, SsoUser user, String appId,
													   String accessToken) {
		String newAccessToken = accessToken;
		if (newAccessToken == null || !accessTokenManager.refresh(newAccessToken)) {
			newAccessToken = accessTokenManager.generate(authContent);
		}
		String refreshToken = refreshTokenManager.generate(authContent, newAccessToken, appId);
		long expirationTime = System.currentTimeMillis() + accessTokenManager.getExpiresIn() * 1000;
		return new SessionAccessToken(newAccessToken,  refreshToken, user,accessTokenManager.getExpiresIn(),new Date(expirationTime));
	}




}