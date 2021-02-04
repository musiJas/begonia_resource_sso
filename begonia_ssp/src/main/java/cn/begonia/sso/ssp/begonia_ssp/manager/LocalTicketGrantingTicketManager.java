package cn.begonia.sso.ssp.begonia_ssp.manager;

import cn.begonia.sso.common.begonia_cmn.entity.SsoUser;
import cn.begonia.sso.ssp.begonia_ssp.common.ExpirationPolicy;
import cn.begonia.sso.ssp.begonia_ssp.common.TicketGrantingTicketManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 本地登录凭证管理
 * 
 * @author Joe
 */
@Component
@ConditionalOnProperty(name = "sso.session.manager", havingValue = "local")
public class LocalTicketGrantingTicketManager implements TicketGrantingTicketManager, ExpirationPolicy {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Value("${sso.timeout}")
    private int timeout;

	private Map<String, DummyTgt> tgtMap = new ConcurrentHashMap<>();

	@Override
	public void create(String tgt, SsoUser user) {
		tgtMap.put(tgt, new DummyTgt(user, System.currentTimeMillis() + getExpiresIn() * 1000));
	}

	@Override
	public SsoUser get(String tgt) {
		DummyTgt dummyTgt = tgtMap.get(tgt);
		long currentTime = System.currentTimeMillis();
		if (dummyTgt == null || currentTime > dummyTgt.expired) {
			return null;
		}
		dummyTgt.expired = currentTime + getExpiresIn() * 1000;
		return dummyTgt.user;
	}

	@Override
	public void remove(String tgt) {
		tgtMap.remove(tgt);
	}

	@Scheduled(cron = SCHEDULED_CRON)
	@Override
	public void verifyExpired() {
		tgtMap.forEach((tgt, dummyTgt) -> {
			if (System.currentTimeMillis() > dummyTgt.expired) {
				tgtMap.remove(tgt);
				logger.debug("TGT : " + tgt + "已失效");
			}
		});
	}

	@Override
	public int getExpiresIn() {
		return timeout;
	}
	
	private class DummyTgt {
		private SsoUser user;
		private long expired;

		public DummyTgt(SsoUser user, long expired) {
			super();
			this.user = user;
			this.expired = expired;
		}
	}
}
