package com.example.eiestudentassistanttool.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.example.eiestudentassistanttool.model.Courses;
import com.example.eiestudentassistanttool.model.Task;

import java.util.List;

@Dao
public interface MyDao {
    @Insert
    void insertTask(Task task);

    @Query("DELETE FROM task_table")
    void deleteAllTasks();

    @Query("SELECT * FROM task_table WHERE deleted = 0 ORDER BY task_table.created_at DESC")
    LiveData<List<Task>> getTasks();

    @Query("SELECT * FROM task_table WHERE task_table.task_id == :id")
    LiveData<Task> getTask(long id);

    @Update
    void update(Task task);

    @Delete
    void delete(Task task);

    @Query("SELECT COUNT(*)  FROM task_table WHERE deleted = 1 AND priority LIKE 'CLASS' AND course_code == :code")
    LiveData<Integer> deletedLectures(int code);

    @Query("SELECT COUNT(*) FROM task_table WHERE task_table.course_code == :code AND priority LIKE 'CLASS'")
    LiveData<Integer> countLectures(int code);



    @Query("SELECT * FROM task_table WHERE deleted = 0 ORDER BY due_date ASC")
    LiveData<List<Task>> taskSortByDateAscending();

    @Query("SELECT * FROM task_table WHERE deleted = 0 ORDER BY due_date DESC")
    LiveData<List<Task>> taskSortByDateDescending();

    @Query("SELECT * FROM task_table WHERE deleted = 0 ORDER BY priority ASC")
    LiveData<List<Task>> taskSortByPriorityAscending();

    @Query("SELECT * FROM task_table WHERE deleted = 0 ORDER BY priority DESC")
    LiveData<List<Task>> taskSortByPriorityDescending();

    @Query("SELECT * FROM task_table WHERE deleted = 0 AND task_table.course_code == :id ORDER BY due_date ASC")
    LiveData<List<Task>> getTasksForCourse(int id);



    @Insert
    void insertCourse(Courses course);

    @Query("DELETE FROM courses")
    void deleteAllCourses();

    @Query("SELECT * FROM courses")
    LiveData<List<Courses>> getCourses();

    @Query("SELECT * FROM courses WHERE course_Id == :code")
    LiveData<Courses> getCourse(long code);

    @Update
    void updateCourse(Courses course);

    @Delete
    void deleteCourse(Courses course);

    @Query("SELECT * FROM task_table WHERE course_code == :code")
    @Transaction
    LiveData<List<Task>> getTasksForCourse(long code);

    @Query("SELECT COUNT(*) FROM courses WHERE courses.course_Id == :code")
    LiveData<Integer> codeExists(int code);

    @Query("SELECT course_Id FROM courses ORDER BY course_Id ASC")
    LiveData<List<Integer>> getCourseCodes();



}
