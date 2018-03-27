package cn.xiaocool.dezhischool.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10835 on 2018/2/26.
 */

public class SingleBabyInfos {
    private static SingleBabyInfos mBabyInfos = new SingleBabyInfos();
    private List<BabyInfo> infos;
    private SingleBabyInfos(){
        infos = new ArrayList<>();
    }
    public static SingleBabyInfos getmBabyInfos(){
        return mBabyInfos;
    }
    public List<BabyInfo> getInfos(){
        return mBabyInfos.infos;
    }
}
