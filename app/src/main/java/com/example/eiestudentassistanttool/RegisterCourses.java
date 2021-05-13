package com.example.eiestudentassistanttool;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.example.eiestudentassistanttool.adapter.CoursesAdapter;
import com.example.eiestudentassistanttool.adapter.CoursesAdapterOnClickListener;
import com.example.eiestudentassistanttool.model.Courses;
import com.example.eiestudentassistanttool.model.CoursesModel;
import com.example.eiestudentassistanttool.model.MyViewModel;
import com.example.eiestudentassistanttool.util.CourseUtils;

import java.util.ArrayList;

public class RegisterCourses extends AppCompatActivity implements CoursesAdapterOnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_courses);


        RecyclerView coursesRecView = findViewById(R.id.coursesRecView);
        ArrayList<CoursesModel> courses = CourseUtils.getInstance(RegisterCourses.this).getAllCourses();

        CoursesAdapter adapter = new CoursesAdapter(this, "allCoursesActivity", courses, this);
        coursesRecView.setAdapter(adapter);
        coursesRecView.setLayoutManager(new LinearLayoutManager(this));
        adapter.setCourses(CourseUtils.getInstance(this).getAllCourses());
    }

    @Override
    public void onToDoClick(CoursesModel coursesModel) {

    }

    @Override
    public void addOnClick(CoursesModel coursesModel) {
        Courses myCourse = new Courses(coursesModel.getCourseNumber(),coursesModel.getCourseName());
        MyViewModel.codeExists(myCourse.courseId).observe(RegisterCourses.this,
                integer -> {
                    if (integer == 0){
                        MyViewModel.insert(myCourse);
                        Toast.makeText(this, "Course Added", Toast.LENGTH_SHORT).show();
                        CourseUtils.getInstance(this).addToMyCourses(coursesModel);
                    }else {
                        Toast.makeText(this, "Course Already Added", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    @Override
    public void removeOnClick(CoursesModel coursesModel) {

    }
}