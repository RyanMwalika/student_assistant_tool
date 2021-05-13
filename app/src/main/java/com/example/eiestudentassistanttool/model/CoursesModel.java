package com.example.eiestudentassistanttool.model;

public class CoursesModel {
    private String courseName;
    private int courseNumber;
    boolean isExpanded;

    public CoursesModel(int courseNumber, String courseName) {
        this.courseName = courseName;
        this.courseNumber = courseNumber;
        isExpanded = false;
    }

    @Override
    public String toString() {
        return "CoursesModel{" +
                "courseName='" + courseName + '\'' +
                ", courseNumber='" + courseNumber + '\'' +
                '}';
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }


}
