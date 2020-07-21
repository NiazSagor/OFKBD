package com.ofk.bd.HelperClass;

import java.util.List;

public class SixInOne {
    List<Course> list;

    public SixInOne(List<Course> list) {
        this.list = list;
    }

    public List<Course> getList() {
        return list;
    }

    public void setList(List<Course> list) {
        this.list = list;
    }
}
