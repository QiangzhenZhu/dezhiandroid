package cn.xiaocool.dezhischool.utils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/25.
 */
public class StringJoint {

    public static String arrayJointchar(ArrayList<String> stringArrayList,String separator){
        String upString = "null";
        for (int i = 0; i < stringArrayList.size(); i++) {

            upString = stringArrayList.get(i) + separator + upString;
        }

        upString = upString.substring(0, upString.length() - 5);


        return  upString;
    }

}
