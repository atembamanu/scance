
package com.blueman.scanwithit.qrcode.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Serializable
{

    @SerializedName("student_name")
    @Expose
    private String studentName;
    @SerializedName("student_email")
    @Expose
    private String studentEmail;
    @SerializedName("student_class")
    @Expose
    private String studentClass;
    @SerializedName("QR_code")
    @Expose
    private String qRCode;
    private final static long serialVersionUID = 7757215016556868130L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param studentEmail
     * @param studentName
     * @param qRCode
     * @param studentClass
     */
    public Data(String studentName, String studentEmail, String studentClass, String qRCode) {
        super();
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentClass = studentClass;
        this.qRCode = qRCode;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getQRCode() {
        return qRCode;
    }

    public void setQRCode(String qRCode) {
        this.qRCode = qRCode;
    }

}
