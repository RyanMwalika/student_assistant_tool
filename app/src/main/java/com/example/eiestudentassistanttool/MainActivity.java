package com.example.eiestudentassistanttool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.eiestudentassistanttool.adapter.RecyclerViewAdapter;
import com.example.eiestudentassistanttool.adapter.RecyclerViewOnClickListener;
import com.example.eiestudentassistanttool.data.MyRepository;
import com.example.eiestudentassistanttool.model.MyViewModel;
import com.example.eiestudentassistanttool.model.SharedViewModel;
import com.example.eiestudentassistanttool.model.Task;
import com.example.eiestudentassistanttool.util.CourseUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewOnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "ITEM";
    private MyViewModel myViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Menu menu;
    LiveData<List<Task>> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);



        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                ,R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        assert drawerLayout != null;
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);



        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.
                from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        myViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.
                getApplication()).create(MyViewModel.class);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);




        myViewModel.getAllTasks().observe(this, tasks -> {
            recyclerViewAdapter = new RecyclerViewAdapter(tasks, this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            showBottomSheetDialog();
        });

        taskList =  myViewModel.getAllTasks();
    }



    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }




    @Override
    public void onToDoClick(Task task) {
        Log.d("Click", "onToDoClick: " + task.getTask());
        sharedViewModel.setSelectedItem(task);
        sharedViewModel.setIsEdit(true);
        showBottomSheetDialog();
    }

    @Override
    public void radioBtnClick(Task task) {
        Log.d("Click", "onRadioBtn: " + task.getTask());
        task.setDeleted(1);
        MyViewModel.update(task);
        //MyViewModel.delete(task);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_menu, menu);
        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };
        menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setQueryHint("Enter Course Code e.g. 552");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                int code = Integer.parseInt(query);
                MyViewModel.getTasksForCourse(code).observe( (LifecycleOwner) getActivity(MainActivity.this), tasks -> {
                    recyclerViewAdapter = new RecyclerViewAdapter(tasks,
                            (RecyclerViewOnClickListener) getActivity(MainActivity.this));
                    recyclerView.setAdapter(recyclerViewAdapter);
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.sortDateAscending:
                MyViewModel.taskSortByDateAscending().observe(this, tasks -> {
                    recyclerViewAdapter = new RecyclerViewAdapter(tasks
                            , this);
                    recyclerView.setAdapter(recyclerViewAdapter);
                });
                Toast.makeText(this, "Sort by Date Ascending", Toast.LENGTH_SHORT).show();
                break;

            case R.id.sortDateDescending:
                MyViewModel.taskSortByDateDescending().observe(this,tasks -> {
                    recyclerViewAdapter = new RecyclerViewAdapter(tasks,
                            this);
                    recyclerView.setAdapter(recyclerViewAdapter);
                });
                Toast.makeText(this, "Sort by Date Descending", Toast.LENGTH_SHORT).show();
                break;

            case R.id.sortPriorityAscending:
                MyViewModel.taskSortByPriorityAscending().observe(this, tasks -> {
                    recyclerViewAdapter = new RecyclerViewAdapter(tasks,
                            this);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    Toast.makeText(this, "Sort by Priority Ascending", Toast.LENGTH_SHORT).show();
                });
                break;
            case R.id.sortPriorityDescending:
                MyViewModel.taskSortByPriorityDescending().observe(this, tasks -> {
                    recyclerViewAdapter = new RecyclerViewAdapter(tasks,
                            this);
                    recyclerView.setAdapter(recyclerViewAdapter);
                    Toast.makeText(this, "Sort by Priority Descending", Toast.LENGTH_SHORT).show();
                });
                break;
            default:
                break;
        }

        return true;
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



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.navRegCourses:
                Intent intent = new Intent(MainActivity.this, RegisterCourses.class);
                startActivity(intent);
                break;
            case R.id.navMyCourses:
                Intent intent1 = new Intent(MainActivity.this, MyCourses.class);
                startActivity(intent1);
                break;
            default:
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}