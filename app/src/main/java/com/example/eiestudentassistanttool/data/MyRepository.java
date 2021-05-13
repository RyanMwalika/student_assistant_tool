package com.example.eiestudentassistanttool.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.eiestudentassistanttool.model.Courses;
import com.example.eiestudentassistanttool.model.Task;
import com.example.eiestudentassistanttool.util.MyRoomDatabase;

import java.util.List;

public class MyRepository {
    private final MyDao myDao;
    private LiveData<List<Task>> allTasks;
    private LiveData<List<Courses>> allCourses;

    public MyRepository(Application application){
        MyRoomDatabase database = MyRoomDatabase.getDatabase(application);
        myDao = database.myDao();
        allTasks = myDao.getTasks();
        allCourses = myDao.getCourses();
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    public LiveData<List<Courses>> getAllCourses(){
        return allCourses;
    }

    public void insert(Task task){
        /* Using Executor Service so that insertion happens in background threads*/
        MyRoomDatabase.databaseWriterExecutor.execute(()-> myDao.insertTask(task));
    }

    public LiveData<List<Task>> taskSortByDateAscending(){
        return myDao.taskSortByDateAscending();
    }

    public LiveData<List<Task>> taskSortByDateDescending(){
        return myDao.taskSortByDateDescending();
    }

    public LiveData<List<Task>> taskSortByPriorityAscending(){
        return myDao.taskSortByPriorityAscending();
    }

    public LiveData<List<Task>> taskSortByPriorityDescending(){
        return myDao.taskSortByPriorityDescending();
    }

    public LiveData<List<Task>> getTasksForCourse(int id){
        return myDao.getTasksForCourse(id);
    }

    public LiveData<Integer> deletedLectures(int id){
        return myDao.deletedLectures(id);
    }







    public void insert(Courses courses){
        MyRoomDatabase.databaseWriterExecutor.execute(()-> myDao.insertCourse(courses));
    }

    public LiveData<Task> getTask(long id){
        return myDao.getTask(id);
    }


    public LiveData<Courses> getCourse(long id){
        return myDao.getCourse(id);
    }

    public void updateTask(Task task){
        MyRoomDatabase.databaseWriterExecutor.execute(()-> myDao.update(task));
    }

    public void updateCourse(Courses courses){
        MyRoomDatabase.databaseWriterExecutor.execute(()-> myDao.updateCourse(courses));
    }

    public void deleteTask(Task task){
        MyRoomDatabase.databaseWriterExecutor.execute(()-> myDao.delete(task));
    }

    public void deleteCourse(Courses courses){
        MyRoomDatabase.databaseWriterExecutor.execute(()-> myDao.deleteCourse(courses));
    }

    public LiveData<Integer> codeExists(int code){
        return myDao.codeExists(code);
    }

    public LiveData<Integer> countLectures(int code){
        return myDao.countLectures(code);
    }


    public LiveData<List<Integer>> getCourseCodes() {
        return myDao.getCourseCodes();
    }

}
