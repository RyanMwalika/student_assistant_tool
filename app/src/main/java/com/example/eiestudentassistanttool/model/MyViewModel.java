package com.example.eiestudentassistanttool.model;

import android.app.Application;

import androidx.annotation.AnyThread;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.eiestudentassistanttool.data.MyRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MyViewModel extends AndroidViewModel {

    public static MyRepository repository;
    public final LiveData<List<Task>> allTasks;
    public final LiveData<List<Courses>> allCourses;
    private final MutableLiveData<Integer> totalLectures = new MutableLiveData<>();
    private final MutableLiveData<Integer> attendedLectures = new MutableLiveData<>();
    private final MutableLiveData<Integer> percentageAttendance = new MediatorLiveData<>();

    public MyViewModel(@NonNull Application application) {
        super(application);
        repository = new MyRepository(application);
        allTasks = repository.getAllTasks();
        allCourses = repository.getAllCourses();
    }

    public LiveData<List<Task>> getAllTasks(){ return allTasks; }
    public static void insert(Task task) { repository.insert(task); }
    public LiveData<Task> getTask(Long id) { return repository.getTask(id); }
    public static void update(Task task) { repository.updateTask(task); }
    public static void delete(Task task) { repository.deleteTask(task); }
    public static LiveData<List<Task>>  taskSortByDateAscending(){
        return repository.taskSortByDateAscending(); }
    public static LiveData<List<Task>>  taskSortByDateDescending(){
        return repository.taskSortByDateDescending(); }
    public static LiveData<List<Task>>  taskSortByPriorityAscending(){
        return repository.taskSortByPriorityAscending(); }
    public static LiveData<List<Task>>  taskSortByPriorityDescending(){
        return repository.taskSortByPriorityDescending();
    }
    public static LiveData<List<Task>> getTasksForCourse(int id){
        return repository.getTasksForCourse(id);
    }
    public static LiveData<Integer> countLectures(int code){
        return  repository.countLectures(code);
    }

    public static LiveData<Integer> deletedLectures(int code){
        return  repository.deletedLectures(code);
    }

    public LiveData <List<Courses>> getAllCourses(){ return allCourses; }
    public static void insert(Courses courses) { repository.insert(courses); }
    public LiveData<Courses> getCourse(Long id) { return repository.getCourse(id); }
    public static void updateCourse(Courses courses) { repository.updateCourse(courses); }
    public static void deleteCourse(Courses courses) { repository.deleteCourse(courses); }
    public static LiveData<Integer> codeExists(int code){
        return repository.codeExists(code);
    }
    public static LiveData<List<Integer>> getCourseCodes() {
        return repository.getCourseCodes();
    }

}
