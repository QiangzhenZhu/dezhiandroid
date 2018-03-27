package cn.xiaocool.dezhischool.db.sp;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

import cn.xiaocool.dezhischool.bean.UserInfo;

/**
 * Created by hzh on 17/3/21.
 */

public class UserSp extends BaseSp<UserInfo> {

    public UserSp(Context context) {
        super(context, "user_sp");
    }

    @Override
    public void read(UserInfo user) {
        // 安全检查
        if (user == null) {
            user = new UserInfo();
        }
        if (getSP().contains("userId")) {
            user.setId(getSP().getString("userId", ""));
        }
        if (getSP().contains("classid")){
            user.setClassId(getSP().getString("classid", ""));
        }
        if (getSP().contains("schoolid")){
            user.setSchoolId(getSP().getString("schoolid", ""));
        }
        if (getSP().contains("userIdTemp")) {
            user.setUserIdTemp(getSP().getString("userIdTemp", ""));
        }
        if (getSP().contains("userPhone")) {
            user.setPhone(getSP().getString("userPhone", ""));
        }
//        if (getSP().contains("userCode")) {
//            user.setUserCode(getSP().getString("userCode", ""));
//        }
        if (getSP().contains("userName")) {
            user.setName(getSP().getString("userName", ""));
        }
        if (getSP().contains("userPassword")) {
            user.setUserPassword(getSP().getString("userPassword", ""));
        }
        if (getSP().contains("userGender")) {
            user.setSex(getSP().getString("userGender", ""));
        }



    }

    @Override
    public UserInfo read() {
        UserInfo result = null;
        result = new UserInfo();
        read(result);
        return result;
    }

    @Override
    public void write(UserInfo user) {
        SharedPreferences.Editor editor = getSP().edit();
        if (!user.getId().equals("")) {
            editor.putString("userId", user.getId());
        }

        if (!user.getClassId().equals("")) {
            editor.putString("classid", user.getClassId());
        }
        if (!user.getSchoolId().equals("")) {
            editor.putString("schoolid", user.getSchoolId());
        }

        if (!user.getUserIdTemp().equals("")) {
            editor.putString("userIdTemp", user.getUserIdTemp());
        }
        if (!user.getPhone().equals("")) {
            editor.putString("userPhone", user.getPhone());
        }

        if (!user.getName().equals("")) {
            editor.putString("userName", user.getName());
        }
        if (!user.getUserPassword().equals("")) {
            editor.putString("userPassword", user.getUserPassword());
        }
        editor.commit();
    }

    @Override
    public void clear() {
        SharedPreferences.Editor editor = getSP().edit();
        editor.clear();
        editor.commit();
    }
}
