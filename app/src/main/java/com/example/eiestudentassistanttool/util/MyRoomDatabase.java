package com.example.eiestudentassistanttool.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.eiestudentassistanttool.data.MyDao;
import com.example.eiestudentassistanttool.model.Courses;
import com.example.eiestudentassistanttool.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@TypeConverters({Converters.class})
@Database(entities = {Courses.class, Task.class}, version = 1, exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase {
    public static final int NUMBER_OF_THREADS = 4;
    private static volatile MyRoomDatabase INSTANCE;
    public static final String DATABASE_NAME = "tool_database";
    public static final ExecutorService databaseWriterExecutor = Executors.
            newFixedThreadPool(NUMBER_OF_THREADS);
    public static final RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriterExecutor.execute(()->{
                        //invoke Dao and write
                        MyDao myDao = INSTANCE.myDao();
                        myDao.deleteAllTasks();
                        myDao.deleteAllCourses();// clean slate

                    });
                }
            };

    public static MyRoomDatabase getDatabase (final Context context){
        if (INSTANCE == null){
            synchronized (MyRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyRoomDatabase
                                    .class,
                            DATABASE_NAME).addCallback(sRoomDatabaseCallback).build();
                }
            }
        }
        return INSTANCE;
    }
    public abstract MyDao myDao();
}
