package com.example.eiestudentassistanttool.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


    public class SharedViewModel extends ViewModel {
        private final MutableLiveData<Task> selectedItem = new MutableLiveData<>();
        private boolean isEdit;


        public void setSelectedItem(Task task){
            selectedItem.setValue(task);
        }

        public boolean getIsEdit() {
            return isEdit;
        }

        public void setIsEdit(boolean edit) {
            isEdit = edit;
        }

        public LiveData<Task> getSelectedItem(){
            return selectedItem;
        }
    }

