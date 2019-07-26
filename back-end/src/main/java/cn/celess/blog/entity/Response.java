package cn.celess.blog.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author : xiaohai
 * @date : 2019/03/28 15:24
 */
@Data
public class Response implements Serializable {
    private int code;
    private String msg;
    private Object result;
    private long date;

    public Response() {
    }

    public Response(int code, String msg, Object result, long date) {
        this.code = code;
        this.msg = msg;
        this.result = result;
        this.date = date;
    }

}
