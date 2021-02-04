package cn.begonia.sso.ssp.begonia_ssp.common;

/**
 * 含时效
 * 
 * @author Joe
 */
public interface Expiration {
	
	/**
	 * 时效（秒）
	 * @return
	 */
	int getExpiresIn();
}
