package cn.begonia.sso.common.begonia_cmn.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class SsoUser  implements Serializable {

    private static final long serialVersionUID = 6966636987364868787L;
    private  Long  id;
    private  String   userNo;
    private  String  password;
    private  String   userName;
    private  Integer  gender;
    private  Date  createGmt;
    private  Date  updateGmt;

    public  SsoUser(String userNo,String password){
        this.userNo=userNo;
        this.password=password;
    }

}
