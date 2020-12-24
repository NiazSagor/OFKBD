package com.ofk.bd.Task;

import com.ofk.bd.Model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class BlogTask implements Callable<List<Course>> {

    private final List<Course> blogList = new ArrayList<>();

    @Override
    public List<Course> call() throws Exception {

        blogList.add(new Course("অনুপ্রেরণামূলক"));
        blogList.add(new Course("গল্প"));
        blogList.add(new Course("টিপস এন্ড ট্রিকস"));
        blogList.add(new Course("দক্ষতা উন্নয়নমূলক"));
        blogList.add(new Course("সচেতনতামূলক"));
        blogList.add(new Course("ইংরেজি"));

        return blogList;
    }
}
