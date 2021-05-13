package com.example.eiestudentassistanttool;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.eiestudentassistanttool.model.Courses;
import com.example.eiestudentassistanttool.model.MyViewModel;
import com.example.eiestudentassistanttool.model.Priority;
import com.example.eiestudentassistanttool.model.SharedViewModel;
import com.example.eiestudentassistanttool.model.Task;
import com.example.eiestudentassistanttool.util.CourseUtils;
import com.example.eiestudentassistanttool.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BottomSheetFragment extends BottomSheetDialogFragment implements
            View.OnClickListener {
        private EditText enterToDo;
        private ImageButton calendarButton, priorityButton, saveToDoButton;
        private RadioGroup priorityRadioGroup;
        private RadioButton selectedRadioButton;
        private int selectedButtonId;
        private CalendarView calendarView;
        private Group calendarGroup;
        private Date dueDate;
        private Spinner spinner;
        Calendar calendar = Calendar.getInstance();
        private SharedViewModel sharedViewModel;
        private boolean isEdit;
        private Priority priority;
        int code;


        public BottomSheetFragment() {
        }


        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.bottom_sheet, container, false);
            calendarGroup = view.findViewById(R.id.calendar_group);
            calendarView = view.findViewById(R.id.calendar_view);
            calendarButton = view.findViewById(R.id.today_calendar_button);
            enterToDo = view.findViewById(R.id.enter_todo_et);
            saveToDoButton = view.findViewById(R.id.save_todo_button);
            priorityButton = view.findViewById(R.id.priority_todo_button);
            priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);
            spinner = view.findViewById(R.id.spinner);

            Chip todayChip = view.findViewById(R.id.today_chip);
            todayChip.setOnClickListener(this);
            Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
            tomorrowChip.setOnClickListener(this);
            Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
            nextWeekChip.setOnClickListener(this);


            return view;
        }

        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            Integer[] codes = new Integer[]{502, 512, 522, 552, 582};
            ArrayList<Integer> codeList = new ArrayList<>();

            MyViewModel.getCourseCodes().observe(BottomSheetFragment.this,
                    new Observer<List<Integer>>() {
                @Override
                public void onChanged(List<Integer> integers) {
                    codeList.addAll(integers);
                    ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(),
                            android.R.layout.simple_spinner_item, codeList);
                    spinner.setAdapter(adapter);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // code = (int) parent.getSelectedItem();
                            code = (int) parent.getItemAtPosition(position);
                            Log.d("Last", "onItemSelected: code is " +  code);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            });

//            ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getContext(),
//                    android.R.layout.simple_spinner_item, codes);
//            spinner.setAdapter(adapter);
//            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                   // code = (int) parent.getSelectedItem();
//                    code = (int) parent.getItemAtPosition(position);
//                    Log.d("Last", "onItemSelected: code is " +  code);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                }
//            });

            calendarButton.setOnClickListener(v -> {
                calendarGroup.setVisibility(
                        calendarGroup.getVisibility() == View.GONE? View.VISIBLE : View.GONE);
                Utils.hideSoftKeyboard(view);
            });
            calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
                calendar.clear();
                calendar.set(year, month, dayOfMonth);
                dueDate = calendar.getTime();
                Log.d("Calendar", "onViewCreated: month " + (month+1) + " day " + dayOfMonth);
            });

            priorityButton.setOnClickListener(v -> {
                Utils.hideSoftKeyboard(v);
                priorityRadioGroup.setVisibility(priorityRadioGroup.getVisibility() == View.GONE?
                        View.VISIBLE: View.GONE);
                priorityRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                    if (priorityRadioGroup.getVisibility() == View.VISIBLE){
                        selectedButtonId = checkedId;
                        selectedRadioButton = view.findViewById(selectedButtonId);
                        if (selectedRadioButton.getId() == R.id.radioButton_class){
                            priority = Priority.CLASS;
                        }else if (selectedRadioButton.getId() == R.id.radioButton_exam){
                            priority = Priority.EXAM;
                        }
                        else if (selectedRadioButton.getId() == R.id.radioButton_assignment){
                            priority = Priority.HOMEWORK;
                        }else {
                            priority = Priority.HOMEWORK;
                        }
                    } else {
                        priority = Priority.HOMEWORK;
                    }
                });
            });

            saveToDoButton.setOnClickListener(v -> {
                String task = enterToDo.getText().toString().trim();
                if (!TextUtils.isEmpty(task) && dueDate != null && priority != null && code != 0){
                    Task myTask = new Task(task, priority,
                            dueDate, Calendar
                            .getInstance().getTime(), code);

                    if (isEdit){
                        Task updateTask = sharedViewModel.getSelectedItem().getValue();
                        updateTask.setTask(task);
                        updateTask.setDateCreated(Calendar.getInstance().getTime());
                        updateTask.setDueDate(dueDate);
                        updateTask.setPriority(priority);
                        updateTask.setCourseCode(code);
                        MyViewModel.update(updateTask);
                        sharedViewModel.setIsEdit(false);
                    }else {
                        MyViewModel.insert(myTask);
                    }
                    enterToDo.setText("");
                    if (this.isVisible()){
                        this.dismiss();
                    }
                } else {
                    Snackbar.make(saveToDoButton, R.string.empty_field, Snackbar.LENGTH_LONG);

                }
            });

            sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        }

        @Override
        public void onResume() {
            super.onResume();
            if (sharedViewModel.getSelectedItem().getValue() != null){
                isEdit = sharedViewModel.getIsEdit();
                Task task = sharedViewModel.getSelectedItem().getValue();
                Log.d("Share", "onViewCreated: "+ task.getTask());
                enterToDo.setText(task.getTask());
            }
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.today_chip:
                    //set date to today
                    calendar.add(Calendar.DAY_OF_YEAR, 0);
                    dueDate = calendar.getTime();
                    Log.d("Due", "onClick: " + dueDate.toString());
                    break;
                case R.id.tomorrow_chip:
                    calendar.add(Calendar.DAY_OF_YEAR, 1);
                    dueDate = calendar.getTime();
                    Log.d("Due", "onClick: " + dueDate.toString());
                    break;
                case R.id.next_week_chip:
                    calendar.add(Calendar.DAY_OF_YEAR, 7);
                    dueDate = calendar.getTime();
                    Log.d("Due", "onClick: " + dueDate.toString());
                    break;
            }
        }

    }
