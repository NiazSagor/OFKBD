package com.ofk.bd.Utility;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ofk.bd.Model.DisplayCourse;

public class DatabaseUtility {

    public static FirebaseRecyclerOptions<DisplayCourse> getRecommendedCourseQuery(String section) {
        Query query =
                FirebaseDatabase.getInstance().getReference("Section").child(section);
        return new FirebaseRecyclerOptions.Builder<DisplayCourse>()
                .setQuery(query, DisplayCourse.class)
                .build();
    }

}
