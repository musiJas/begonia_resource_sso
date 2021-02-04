package cn.begonia.sso.ssp.begonia_ssp.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = -4318302303645119911L;
    private  Long id;
    private  String userNo;
    private  String userName;
    private  String password;
    private  Integer  gender;
    private  String  address;
    private  String  other;
    private Date  createGmt;



}
