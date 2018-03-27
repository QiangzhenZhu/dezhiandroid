package cn.xiaocool.dezhischool.bean;

/**
 * Created by THB on 2016/6/28.
 */
import java.util.ArrayList;
import java.util.List;

public class Group {
    private String id;
    private String title;
    private ArrayList<Child> children;
    private boolean isChecked;

    public Group(String id, String title) {
        this.id =id;
        this.title = title;
        children = new ArrayList<Child>();
        this.isChecked = false;
    }
    public List<Child> getChildren(){
        return children;
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

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void addChildrenItem(Child child) {
        children.add(child);
    }

    public int getChildrenCount() {
        return children.size();
    }

    public Child getChildItem(int index) {
        return children.get(index);
    }
}
