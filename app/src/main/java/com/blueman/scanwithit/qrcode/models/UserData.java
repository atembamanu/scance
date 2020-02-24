
package com.blueman.scanwithit.qrcode.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData implements Serializable
{

    @SerializedName("data")
    @Expose
    private Data data;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = -1306292729543675743L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public UserData() {
    }

    /**
     * 
     * @param code
     * @param data
     * @param message
     */
    public UserData(Data data, Integer code, String message) {
        super();
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
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
