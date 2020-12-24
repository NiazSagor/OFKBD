package com.ofk.bd.Repository;

import com.ofk.bd.Interface.ActivityPicLoadCallback;
import com.ofk.bd.Interface.CategoryCallback;
import com.ofk.bd.Interface.DisplayCourseLoadCallback;
import com.ofk.bd.Interface.QuizOptionLoadCallback;
import com.ofk.bd.Interface.QuizQuestionLoadCallback;
import com.ofk.bd.Interface.SectionVideoLoadCallback;
import com.ofk.bd.Interface.VideoLoadCallback;
import com.ofk.bd.Task.ActivityPicTask;
import com.ofk.bd.Task.ActivityVideoTask;
import com.ofk.bd.Task.BlogTask;
import com.ofk.bd.Task.CourseTask;
import com.ofk.bd.Task.QuizOptionTask;
import com.ofk.bd.Task.QuizQuestionTask;
import com.ofk.bd.Task.SectionVideoTask;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public class CommonRepository {

    private static final String TAG = "CommonRepository";

    private final ExecutorService executor;

    public CommonRepository(ExecutorService executor) {
        this.executor = executor;
    }

    public void getAllCoursesFromSection(DisplayCourseLoadCallback callback, String sectionName) {
        executor.submit(new CourseTask(callback, sectionName));
    }

    public void getActivityPhotos(ActivityPicLoadCallback callback, String entryNode) {
        executor.submit(new ActivityPicTask(callback, entryNode));
    }

    public void getActivityVideo(VideoLoadCallback callback) {
        executor.submit(new ActivityVideoTask(callback));
    }

    public void getBlogCategories(CategoryCallback callback) {
        try {
            callback.onCategoryCallback(executor.submit(new BlogTask()).get());
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void getSectionWithVideos(SectionVideoLoadCallback callback, String categoryName, String courseName) {
        executor.submit(new SectionVideoTask(callback, categoryName, courseName));
    }

    public void getQuizQuestions(QuizQuestionLoadCallback callback, String categoryName, String courseName) {
        executor.submit(new QuizQuestionTask(callback, categoryName, courseName));
    }

    public void getQuizOptions(QuizOptionLoadCallback callback, String categoryName, String courseName, String question) {
        executor.submit(new QuizOptionTask(callback, categoryName, courseName, question));
    }
}
