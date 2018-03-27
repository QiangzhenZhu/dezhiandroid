package cn.xiaocool.dezhischool.bean;

/**
 * Created by 10835 on 2018/3/14.
 */

public class GridIcon {
    private int srcId;
    private String name;
    private int viewId;

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public GridIcon(int srcId, String name,int viewId) {
        this.srcId = srcId;
        this.name = name;
        this.viewId = viewId;
    }

    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
