package com.mmednet.library.http.parse;

import com.google.gson.annotations.Expose;

/**
 * Title:HttpResult
 * <p>
 * Description:网络响应结果
 * </p>
 * Author Jming.L
 * Date 2019/5/31 10:17
 */
public class HttpResult {

    private String result;
    private int status;
    private String msg;
    private long timestamp;
    private Condition apiCondition;
    private HttpCode httpCode;

    public HttpResult() {
        this.httpCode = HttpCode.ERROR_UNKNOWN;
        this.status = HttpCode.ERROR_UNKNOWN.getCode();
    }

    public static class Condition {

        private String apiCode;
        private String apiMsg;

        public String getApiCode() {
            return apiCode == null ? "" : apiCode;
        }

        public void setApiCode(String apiCode) {
            this.apiCode = apiCode;
        }

        public String getApiMsg() {
            return apiMsg == null ? "" : apiMsg;
        }

        public void setApiMsg(String apiMsg) {
            this.apiMsg = apiMsg;
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Condition getApiCondition() {
        return apiCondition == null ? new Condition() : apiCondition;
    }

    public void setApiCondition(Condition apiCondition) {
        this.apiCondition = apiCondition;
    }

    public HttpCode getHttpCode() {
        return httpCode == null ? HttpCode.ERROR_UNKNOWN : httpCode;
    }

    public void setHttpCode(HttpCode httpCode) {
        this.httpCode = httpCode;
    }
}
