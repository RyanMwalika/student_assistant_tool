package com.example.eiestudentassistanttool.util;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.eiestudentassistanttool.MyCourses;
import com.example.eiestudentassistanttool.model.Courses;
import com.example.eiestudentassistanttool.model.CoursesModel;
import com.example.eiestudentassistanttool.model.MyViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.prefs.Preferences;

public class CourseUtils {
    private static final String ALL_COURSES_KEY = "all_course";
    private static final String MY_COURSES_KEY = "my_courses";



    private static CourseUtils instance;
    //shared preferences are used to store persistent(non-volatile data)
    private final SharedPreferences sharedPreferences;
    SharedPreferences countSettings;

   

    public CourseUtils(Context context){
        sharedPreferences = context.getSharedPreferences("alternate_db",
                Context.MODE_PRIVATE);
        countSettings  = context.getSharedPreferences("count", 0);

        if(null == getAllCourses()){
            initData();

        }

        //Creates editor for shared preferences that will be used in if statement to initialise data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Instantiates Gson that is used to convert objects to String values(Serialization)
        Gson gson = new Gson();

        if(null == getMyCourses()){
            editor.putString(MY_COURSES_KEY, gson.toJson(new ArrayList<CoursesModel>()));
            editor.commit();
        }


    }

    public ArrayList<CoursesModel> getMyCourses() {
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<CoursesModel>>(){}.getType();
        return gson.fromJson(sharedPreferences.getString(MY_COURSES_KEY, null), type);
    }



    public ArrayList<CoursesModel> getAllCourses() {
        Gson gson = new Gson();
        //Get type token to be passed to fromJson method
        Type type = new TypeToken<ArrayList<CoursesModel>>(){}.getType();
        //converts string file to object, in this case it is an array list
        return gson.fromJson(sharedPreferences.getString(ALL_COURSES_KEY, null), type);
    }

    public boolean addToMyCourses(CoursesModel course){
        ArrayList<CoursesModel> cours = getMyCourses();
        if (null != cours){
            if (cours.add(course)) {
                Gson gson = new Gson();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(MY_COURSES_KEY);
                editor.putString(MY_COURSES_KEY, gson.toJson(cours));
                editor.commit();
                return true;
            }
        }
        return false;
    }

    public boolean removeFromMyCourses(CoursesModel course){
        ArrayList<CoursesModel> cours = getMyCourses();
        if (null != cours){
            for (CoursesModel b: cours){
                if (b.getCourseNumber() == course.getCourseNumber()){
                    if (cours.remove(b)){
                        Gson gson = new Gson();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove(MY_COURSES_KEY);
                        editor.putString(MY_COURSES_KEY, gson.toJson(cours));
                        editor.commit();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Activity getActivity(Context context)
    {
        if (context == null)
        {
            return null;
        }
        else if (context instanceof ContextWrapper)
        {
            if (context instanceof Activity)
            {
                return (Activity) context;
            }
            else
            {
                return getActivity(((ContextWrapper) context).getBaseContext());
            }
        }

        return null;
    }


    private void initData() {
        //List of fifth year coursesModels.
        ArrayList<CoursesModel> coursesModels = new ArrayList<>();
        coursesModels.add(new CoursesModel(502,"Applied Electronics"));
        coursesModels.add(new CoursesModel(552, "Microwaves and Antennae"));
        coursesModels.add(new CoursesModel(542, "Power Electronics and Variable Speed Drives"));
        coursesModels.add(new CoursesModel(582, "Management for Engineers"));
        coursesModels.add(new CoursesModel(522,"Telecommunications"));
        coursesModels.add(new CoursesModel(532,"Power Systems"));
        coursesModels.add(new CoursesModel(512, "Control Systems"));

        //edits Shared Preference object
        SharedPreferences.Editor editor= sharedPreferences.edit();
        //instantiate Gson and uses it to convert our object(ArrayList) to a String(Serialize)
        Gson gson = new Gson();
        editor.putString(ALL_COURSES_KEY, gson.toJson(coursesModels));
        //applies changes to editor. Hover over ,.commit() method to compare it to .apply()
        //I am using commit because the data won't block UI for long and we need the data instantly.
        editor.commit();
    }

    public static CourseUtils getInstance(Context context) {
        if (null == instance) {
            instance = new CourseUtils(context);
        }
        return instance;
    }



}
