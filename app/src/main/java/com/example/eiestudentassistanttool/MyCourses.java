package com.example.eiestudentassistanttool;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eiestudentassistanttool.adapter.CoursesAdapter;
import com.example.eiestudentassistanttool.adapter.CoursesAdapterOnClickListener;
import com.example.eiestudentassistanttool.model.Courses;
import com.example.eiestudentassistanttool.model.CoursesModel;
import com.example.eiestudentassistanttool.model.MyViewModel;
import com.example.eiestudentassistanttool.util.CourseUtils;

import java.util.ArrayList;

public class MyCourses extends AppCompatActivity implements CoursesAdapterOnClickListener{



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_courses);
        ArrayList<CoursesModel> courses = CourseUtils.getInstance(this).getMyCourses();


        RecyclerView myCoursesRecView = findViewById(R.id.myCoursesRecView);
        CoursesAdapterOnClickListener coursesAdapterOnClickListener;
        CoursesAdapter adapter= new CoursesAdapter(MyCourses.this,
                "myCoursesActivity", courses, this);
        myCoursesRecView.setAdapter(adapter);
        myCoursesRecView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setCourses(CourseUtils.getInstance(this).getMyCourses());



    }




    @Override
    public void onToDoClick(CoursesModel coursesModel) {


        MyViewModel.deletedLectures(coursesModel.getCourseNumber()).observe(MyCourses.this,
                integer ->
                        MyViewModel.countLectures(coursesModel.getCourseNumber())
                                .observe(MyCourses.this,
                                integer1 ->
                                        Toast.makeText(MyCourses.this, "Attended " +
                                        integer +" out of " + integer1 + " lectures",
                                        Toast.LENGTH_SHORT).show()));


    }

    @Override
    public void addOnClick(CoursesModel coursesModel) {

    }

    @Override
    public void removeOnClick(CoursesModel coursesModel) {

        Courses myCourse = new Courses(coursesModel.getCourseNumber(), coursesModel.getCourseName());
        MyViewModel.codeExists(myCourse.getCourseId()).observe(this,
                new Observer<Integer>() {
                    @Override
                    public void onChanged(Integer integer) {
                        MyViewModel.deleteCourse(myCourse);
                        Toast.makeText(MyCourses.this, myCourse.getCourseId()
                                + " Deleted", Toast.LENGTH_SHORT).show();
                        CourseUtils.getInstance(MyCourses.this).removeFromMyCourses(coursesModel);
                    }
                });

    }
}


