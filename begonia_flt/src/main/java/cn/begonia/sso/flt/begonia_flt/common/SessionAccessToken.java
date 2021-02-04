package cn.begonia.sso.flt.begonia_flt.common;

import cn.begonia.sso.common.begonia_cmn.entity.SsoUser;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SessionAccessToken  implements Serializable {
    private static final long serialVersionUID = 7055561253289915525L;
    private  String  accessToken;
    private  String  refreshToken;
    private  SsoUser user;
    private  Integer  expireIn; //持续时间
    private  Date  expireTime; //失效时间

    public boolean isExpired() {
        return System.currentTimeMillis() > expireTime.getTime();
    }
}
