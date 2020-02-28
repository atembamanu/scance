
package com.blueman.scanwithit.qrcode.models.attendanceStatus;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attendance implements Serializable
{

    @SerializedName("data")
    @Expose
    private List<String> data = null;
    @SerializedName("code")
    @Expose
    private Integer code;
    @SerializedName("message")
    @Expose
    private String message;
    private final static long serialVersionUID = 7937547482767620225L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Attendance() {
    }

    /**
     * 
     * @param code
     * @param data
     * @param message
     */
    public Attendance(List<String> data, Integer code, String message) {
        super();
        this.data = data;
        this.code = code;
        this.message = message;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
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
