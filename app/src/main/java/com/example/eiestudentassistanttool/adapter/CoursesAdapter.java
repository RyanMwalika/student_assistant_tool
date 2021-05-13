package com.example.eiestudentassistanttool.adapter;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eiestudentassistanttool.R;
import com.example.eiestudentassistanttool.model.CoursesModel;
import com.example.eiestudentassistanttool.util.CourseUtils;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private ArrayList<CoursesModel> courses = new ArrayList<>();
    private Context context;
    private String parentActivity;
    private final CoursesAdapterOnClickListener coursesAdapterOnClickListener;

    public CoursesAdapter(Context context, String parentActivity, ArrayList<CoursesModel> coursesList, CoursesAdapterOnClickListener coursesAdapterOnClickListener) {
        this.context = context;
        this.parentActivity = parentActivity;
        this.courses = coursesList;
        this.coursesAdapterOnClickListener = coursesAdapterOnClickListener;
    }


    public void setCourses(ArrayList<CoursesModel> courses) {
        this.courses = courses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_courses, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txtCourseName.setText(courses.get(position).getCourseName());
        holder.txtCourseId.setText((String.valueOf(courses.get(position).getCourseNumber())));


        if (courses.get(position).isExpanded()) {
            TransitionManager.beginDelayedTransition(holder.parent);
            holder.expandedRelLayout.setVisibility(View.VISIBLE);
            holder.downArrow.setVisibility(View.GONE);

            switch (parentActivity) {
                case "allCoursesActivity":
                    holder.btnRemove.setVisibility(View.GONE);
                    holder.btnAdd.setVisibility(View.VISIBLE);
                    holder.percentage.setVisibility(View.GONE);
                    holder.attendance.setVisibility(View.GONE);
                    break;
                case "myCoursesActivity":
                    holder.btnRemove.setVisibility(View.VISIBLE);
                    holder.btnAdd.setVisibility(View.GONE);
                    holder.percentage.setVisibility(View.GONE);
                    holder.attendance.setVisibility(View.GONE);

                    break;
                default:
                    break;
            }

        } else {
            TransitionManager.beginDelayedTransition(holder.parent);
            holder.expandedRelLayout.setVisibility(View.GONE);
            holder.downArrow.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return courses.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private MaterialCardView parent;
        private TextView txtCourseName, txtCourseId, btnRemove, btnAdd, percentage, attendance;
        private RelativeLayout expandedRelLayout;
        private ImageView upArrow, downArrow;
        CoursesAdapterOnClickListener courseAdapterOnClickListener;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            txtCourseName = itemView.findViewById(R.id.txtCourseName);
            txtCourseId = itemView.findViewById(R.id.txtCourseId);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            expandedRelLayout = itemView.findViewById(R.id.expandedRelLayout);
            upArrow = itemView.findViewById(R.id.btnUpArrow);
            downArrow = itemView.findViewById(R.id.btnDownArrow);
            percentage = itemView.findViewById(R.id.percentageAttendance);
            attendance = itemView.findViewById(R.id.attendance);
            this.courseAdapterOnClickListener = coursesAdapterOnClickListener;
            downArrow.setOnClickListener(v -> {
                CoursesModel course = courses.get(getAdapterPosition());
                course.setExpanded(!course.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });

            upArrow.setOnClickListener(v -> {
                CoursesModel course = courses.get(getAdapterPosition());
                course.setExpanded(!course.isExpanded());
                notifyItemChanged(getAdapterPosition());
            });

            itemView.setOnClickListener(this);
            btnAdd.setOnClickListener(this);
            btnRemove.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.parent){
                courseAdapterOnClickListener.onToDoClick(courses.get(getAdapterPosition()));
            } else if (id == R.id.btnAdd){
                courseAdapterOnClickListener.addOnClick(courses.get(getAdapterPosition()));
            }else if (id == R.id.btnRemove){
                courseAdapterOnClickListener.removeOnClick(courses.get(getAdapterPosition()));
            }

        }
    }
}
