package cn.begonia.sso.ssp.begonia_ssp.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * 应用
 */
@Data
@Builder
public class App implements Serializable {

	private static final long serialVersionUID = 14358427303197385L;

	/** 名称 */
	private String name;
	/** 应用唯一标识 */
	private String clientId;
	/** 应用密钥 */
	private String appSecret;

	public App() {
		super();
	}

	public App(String name, String clientId, String appSecret) {
		super();
		this.name = name;
		this.clientId = clientId;
		this.appSecret = appSecret;
	}


}
