package com.mmednet.library.http.parse;

public enum HttpCode {

    ERROR_DATA(-3, "数据异常"),
    ERROR_UNKNOWN(-2, "未知错误"),
    ERROR_NET(-1, "网络异常"),
    SUCCESS(0, "SUCCESS"),
    FAILURE(1, "系统错误"),
    INVALID_PARA(11, "无效参数"),
    OVERTIME(12, "请求超时"),
    INVALID_TOKEN(13, "无效TOKEN"),
    INVALID_SIGN(14, "签名错误"),
    NO_DATA(15, "没有数据"),
    NO_USER(16, "该用户不存在"),
    ERROR_PWD(17, "手机号或密码错误"),
    ERROR_400(400, "请求出错"),
    ERROR_404(404, "无效请求路径"),
    ERROR_500(500, "服务内部异常"),
    AUTH_INVALID(20001, "授权权限不足"),
    API_MISSING(40001, "缺少必选参数"),
    API_INVALID(40002, "非法参数"),
    BUSINESS_AUTH_INVALID(40006, "业务处理失败"),
    BUSINESS_FAILURE(40004, "权限不足"),
    INVALID_VERSION(1000, "版本过时"),
    KICKOUT(6000,"已在其他端登录");

    private int code;
    private String description;

    HttpCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}
