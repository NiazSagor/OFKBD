package com.ofk.bd.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ofk.bd.Dao.UserInfoDao;
import com.ofk.bd.HelperClass.UserInfo;

@Database(entities = UserInfo.class, version = 1, exportSchema = true)

public abstract class UserInfoDatabase extends RoomDatabase {

    private static final String TAG = "UserInfoDatabase";
    private static UserInfoDatabase instance;

    public abstract UserInfoDao userInfoDao();

    public static synchronized UserInfoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    UserInfoDatabase.class, "user_info")
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

        private UserInfoDao dao;

        public PopulateDatabase(UserInfoDatabase instance) {
            this.dao = instance.userInfoDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Log.d(TAG, "doInBackground: ");
            //dao.insert(new UserInfo("niaz sagor", "01799018683", "niazsagor@gmail.com", 0, 1, 1));
            return null;
        }
    }
}
