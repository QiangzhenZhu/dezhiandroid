package cn.xiaocool.dezhischool.bean;

/**
 * Created by THB on 2016/6/28.
 */
public class Child {
    private String userid;
    private String fullname;
    private String username;
    private String phone;
    private String childName;
    private boolean isChecked;
    private boolean flag;
    private boolean isHeader;
    private boolean isSelectedAll;

    public Child(String userid, String fullname, String username) {
        this.userid = userid;
        this.fullname = fullname;
        this.username = username;
        this.isChecked = false;
    }

    public Child(String fullname,boolean flag,boolean isHeader) {
        this.fullname = fullname;
        this.flag = flag;
        this.isHeader = isHeader;
    }

    public boolean isSelectedAll() {
        return isSelectedAll;
    }

    public void setSelectedAll(boolean selectedAll) {
        isSelectedAll = selectedAll;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean isHeader() {
        return isHeader;
    }

    public void setHeader(boolean header) {
        isHeader = header;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public void toggle() {
        this.isChecked = !this.isChecked;
    }

    public boolean getChecked() {
        return this.isChecked;
    }

    public String getUserid() {
        return userid;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUsername() {
        return username;
    }
}
