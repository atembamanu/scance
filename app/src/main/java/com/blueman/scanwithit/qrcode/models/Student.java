package com.blueman.scanwithit.qrcode.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Student {
    @Expose
    @SerializedName("id") private int id;
    @Expose
    @SerializedName("student_email") private String student_email;
    @Expose
    @SerializedName("student_name") private String student_name;
    @Expose
    @SerializedName("student_class") private String student_class;
    @Expose
    @SerializedName("security_word") private String security_word;
    @Expose
    @SerializedName("QR_code") private String QR_code;
    @Expose
    @SerializedName("student_pass") private String getStudent_pass;
    @Expose
    @SerializedName("message") private String message;
    @Expose
    @SerializedName("code") private int code;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudent_email() {
        return student_email;
    }

    public void setStudent_email(String student_email) {
        this.student_email = student_email;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getSecurity_word() {
        return security_word;
    }

    public void setSecurity_word(String security_word) {
        this.security_word = security_word;
    }

    public String getQR_code() {
        return QR_code;
    }

    public void setQR_code(String QR_code) {
        this.QR_code = QR_code;
    }

    public String getGetStudent_pass() {
        return getStudent_pass;
    }

    public void setGetStudent_pass(String getStudent_pass) {
        this.getStudent_pass = getStudent_pass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
