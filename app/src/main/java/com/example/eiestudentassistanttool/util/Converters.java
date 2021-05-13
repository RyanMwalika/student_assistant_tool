package com.example.eiestudentassistanttool.util;

import androidx.room.TypeConverter;

import com.example.eiestudentassistanttool.model.Priority;

import java.util.Date;

/* Converts time to and from unix time stamp
 * Converts Priority to and from string*/
public class Converters {

    @TypeConverter
    public static Date fromTimeStamp(Long value){
        return value == null? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimeStamp(Date date){
        return date == null? null : date.getTime();
    }

    @TypeConverter
    public static String fromPriority(Priority priority){
        return priority == null? null : priority.name();
    }

    @TypeConverter
    public static Priority toPriority(String priority){
        return priority == null? null : Priority.valueOf(priority);
    }
}
