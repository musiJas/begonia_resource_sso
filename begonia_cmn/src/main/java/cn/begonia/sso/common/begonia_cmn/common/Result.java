package cn.begonia.sso.common.begonia_cmn.common;

import lombok.*;

import java.io.Serializable;

/**
 * DTO 数据传输使用 用于上下层之间和接口之间
 *
 * */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Result  implements Serializable {
    private static final long serialVersionUID = -6920657309095452217L;
    private  int  code;
    private  String  msg;
    private  Object  obj;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Object getObj() {
        return obj;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Result(ResultEnum  re){
        this.code=re.getCode();
        this.msg=re.getMsg();
    }

    public Result(ResultEnum  re, Object obj){
        this.code=re.getCode();
        this.msg=re.getMsg();
        this.obj=obj;
    }

    public  static Result isOk(){
        return  new Result(ResultEnum.SUCCESS);
    }
    public  static Result isOk(Object obj){
        return  new Result(ResultEnum.SUCCESS,obj);
    }

    public  static Result isFail(){
        return  new Result(ResultEnum.FAIL);
    }
    public  static Result isFail(String msg){
        return  new Result(ResultEnum.FAIL,msg);
    }

}
