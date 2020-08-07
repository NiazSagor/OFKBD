package com.ofk.bd.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ofk.bd.Dao.UserProgressDao;
import com.ofk.bd.HelperClass.UserInfo;
import com.ofk.bd.HelperClass.UserProgressClass;

@Database(entities = {UserProgressClass.class, UserInfo.class}, version = 2, exportSchema = true)
public abstract class UserProgressDatabase extends RoomDatabase {

    private static UserProgressDatabase instance;

    public abstract UserProgressDao userProgressDao();

    public static synchronized UserProgressDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    UserProgressDatabase.class, "user_progress")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDatabase(instance).execute();
        }
    };

    public static class PopulateDatabase extends AsyncTask<Void, Void, Void> {

        private UserProgressDao dao;

        public PopulateDatabase(UserProgressDatabase instance) {
            this.dao = instance.userProgressDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //dao.insert(new UserProgressClass("dummy", "dummy", "dummy", "", 0, 0));
            dao.insert(new UserInfo("Niaz Sagor", "+8801799018683", "niazsagor@gmail.com", 0, 0, 0));
            return null;
        }
    }
}
