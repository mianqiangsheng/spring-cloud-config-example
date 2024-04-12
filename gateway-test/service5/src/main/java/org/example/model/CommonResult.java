package org.example.model;

/**
 * @author ：li zhen
 * @description:
 * @date ：2024/3/19 15:39
 */
public class CommonResult {

    private String message;
    private Integer code;

    public CommonResult(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public CommonResult() {
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
