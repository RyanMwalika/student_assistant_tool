package com.example.eiestudentassistanttool.adapter;

import com.example.eiestudentassistanttool.model.CoursesModel;

public interface CoursesAdapterOnClickListener {
    void onToDoClick(CoursesModel coursesModel);

    void addOnClick(CoursesModel coursesModel);

    void removeOnClick(CoursesModel coursesModel);
}
