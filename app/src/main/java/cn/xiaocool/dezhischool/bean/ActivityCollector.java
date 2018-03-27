package cn.xiaocool.dezhischool.bean;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10835 on 2018/3/14.
 */

public class ActivityCollector {
    private static ActivityCollector activityCollector;
    public static List<Activity> activities;
    private ActivityCollector(){
        activities = new ArrayList<>();
    }
    public static void getActivites(){
        if (activityCollector == null){
            synchronized (ActivityCollector.class){
                if (activityCollector == null){
                    activityCollector = new ActivityCollector();
                }
            }
        }
    }
    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public  static void finshAll(){
        for (Activity activity:activities
             ) {

            if(!activity.isFinishing()){
                activity.finish();
            }
        }
    }


}
