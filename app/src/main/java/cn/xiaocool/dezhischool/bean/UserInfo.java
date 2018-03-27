package cn.xiaocool.dezhischool.bean;

import android.content.Context;

import java.io.Serializable;

import cn.xiaocool.dezhischool.db.sp.UserSp;

/**
 * Created by Administrator on 2016/9/1 0001.
 */
public class UserInfo  implements Serializable {

    /**
     * id : 599
     * name : 校长
     * phone : 15888708668
     * sex : 1
     * schoolid : 1
     * birthday : 198001
     * weixin : pengshuguo
     * photo : weixiaotong.png
     * status : 1
     * last_login_time : 2000-01-01 00:00:00
     * age : -195985
     */

    private String id;
    private String name;
    private String phone;
    private String sex;
    private String schoolid;
    private String birthday;
    private String weixin;
    private String photo;
    private String status;
    private String last_login_time;
    private int age;
    public UserInfo(Context context) {
        readData(context);
    }

    public UserInfo() {

    }


    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    private String userPassword;// 用户密码

    public String getUserIdTemp() {
        return userIdTemp;
    }

    public void setUserIdTemp(String userIdTemp) {
        this.userIdTemp = userIdTemp;
    }

    private String userIdTemp;

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    private String schoolId;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    private String classId;

    public void readData(Context context) {
        UserSp sp = new UserSp(context);
        sp.read(this);
    }

    public void writeData(Context context) {
        UserSp sp = new UserSp(context);
        sp.write(this);
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSchoolid() {
        return schoolid;
    }

    public void setSchoolid(String schoolid) {
        this.schoolid = schoolid;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
