package com.example.eiestudentassistanttool.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eiestudentassistanttool.R;
import com.example.eiestudentassistanttool.model.Task;
import com.example.eiestudentassistanttool.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private final List<Task> taskList;

    private final RecyclerViewOnClickListener recyclerViewOnClickListener;

    public RecyclerViewAdapter(List<Task> taskList,
                               RecyclerViewOnClickListener recyclerViewOnClickListener) {
        this.taskList = taskList;
        this.recyclerViewOnClickListener = recyclerViewOnClickListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = taskList.get(position);
        String formatted = Utils.formatDate(task.getDueDate());

        ColorStateList colorStateList = new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled}
        },
                new int[]{
                        Color.LTGRAY, //disabled state
                        Utils.priorityColor(task)
                });

        holder.task.setText(task.getTask());
        holder.todayChip.setText(formatted);
        holder.todayChip.setTextColor(Utils.priorityColor(task));
        holder.todayChip.setChipIconTint(colorStateList);
        holder.radioButton.setButtonTintList(colorStateList);
        holder.code.setText(String.valueOf(task.getCourseCode()));


    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RadioButton radioButton;
        public AppCompatTextView task, code;
        public Chip todayChip;
        RecyclerViewOnClickListener recViewOnClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.todo_radio_button);
            task = itemView.findViewById(R.id.todo_row_todo);
            todayChip = itemView.findViewById(R.id.todo_row_chip);
            code = itemView.findViewById(R.id.todo_row_code);
            this.recViewOnClickListener = recyclerViewOnClickListener;

            //sets a click listener on all objects/rows in our recycler view
            itemView.setOnClickListener(this);

            radioButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            /*
            Overrides onclick event(viewholder extends onclicklistener) and passes my own
            click listener I created(Interface) and passes the adapter position and current clicked object.
            Any class that implements ReyclerViewOnClickListener(Interface) will have position of object and
            the object itself.
            */
            int id = v.getId();
            Task currentTask = taskList.get(getAdapterPosition());
            if (id == R.id.todo_row_layout){
                recViewOnClickListener.onToDoClick(currentTask);
            }
            if (id == R.id.todo_radio_button){
                recViewOnClickListener.radioBtnClick(currentTask);
            }
        }
    }
}

