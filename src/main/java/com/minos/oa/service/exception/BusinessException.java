package com.minos.oa.service.exception;

/**
 * 业务逻辑异常
 *
 * @author minos
 * @date 2021/3/18 23:00
 */
public class BusinessException extends RuntimeException {
    /**
     * 异常编码,异常的唯一标识符
     */
    private String code;
    /**
     * 异常的具体文本信息
     */
    private String message;

    public BusinessException(String code, String message) {
        super(code + ":" + message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
