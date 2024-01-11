package com.example.to_dolistapp;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.util.Objects.isNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.to_dolistapp.AdapterHandler.TaskRecyclerAdapter;
import com.example.to_dolistapp.DatabaseHandler.TaskClass;
import com.example.to_dolistapp.DatabaseHandler.ToDoTaskDatabase;
import com.example.to_dolistapp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private TextView txtStatus, txtNewEdit;
    private EditText edtTitle, edtDescription, edtDate, edtTime;
    private Button btnCancel, btnBack, btnNext, btnSave;
    private LinearLayout btnSetDate, btnSetTime, categoryLayout, statusLayout;

    private Spinner categorySpinner;

    String[] Categories = {"All", "Default", "Personal", "Work", "Meeting", "Shopping", "Party", "Study", "Workout"};
    String category;


    private ToDoTaskDatabase toDoTaskDatabase;
//    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        setTitle("My Tasks");


        replaceFragment(new HomeFragment());

//        recyclerView = findViewById(R.id.recyclerViewId);

        toDoTaskDatabase = new ToDoTaskDatabase(MainActivity.this);
//        ArrayList<TaskClass> taskClasses = toDoTaskDatabase.returnAllTasks();

//        recyclerView.setAdapter(new TaskRecyclerAdapter(taskClasses));
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            if( item.getItemId() == R.id.home ) {
                replaceFragment(new HomeFragment());
            } else if ( item.getItemId() == R.id.task_category ) {
                replaceFragment(new CategoryFragment());
            }

            return true;
        });

        binding.addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddTaskDialog(new TaskClass(1, null,null,null,null,null,null));
            }
        });

    }

    public void showAddTaskDialog(TaskClass taskClass1) {
        ConstraintLayout createNewTaskDialog = findViewById(R.id.createNewTaskDialog);

        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.create_task_dialog,createNewTaskDialog);

        edtTitle = (EditText) view.findViewById(R.id.edt_Title);
        edtDescription =(EditText) view.findViewById(R.id.edt_Task_Description);
        edtTime = (EditText) view.findViewById(R.id.editTextTime);
        edtDate = (EditText) view.findViewById(R.id.editTextDate);
        txtStatus = (TextView) view.findViewById(R.id.status);
        txtNewEdit = (TextView) view.findViewById(R.id.txtNewEdit);
        btnSetDate = (LinearLayout) view.findViewById(R.id.setDate);
        btnSetTime = (LinearLayout) view.findViewById(R.id.setTime);
        categoryLayout= (LinearLayout) view.findViewById(R.id.categoryLayout);
        statusLayout = (LinearLayout) view.findViewById(R.id.statusLayout);

        btnSave = (Button) view.findViewById(R.id.save);
        btnBack = (Button) view.findViewById(R.id.back);
        btnCancel = (Button) view.findViewById(R.id.cancel);
        btnNext = (Button) view.findViewById(R.id.next);


        txtNewEdit.setText("Add New Task");

        categorySpinner = (Spinner) view.findViewById(R.id.categorySpin);


        int taskId;
        if(taskClass1.getTaskTitle() !=null){
            edtTitle.setText(taskClass1.getTaskTitle());
            edtDescription.setText(taskClass1.getDescription());
            edtDate.setText(taskClass1.getDate());
            edtTime.setText(taskClass1.getTime());
            txtStatus.setText(taskClass1.getStatus());
            txtNewEdit.setText("Edit Task");
            taskId = taskClass1.getId();
        }

        ArrayAdapter categoryAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, Categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        categorySpinner.setAdapter(categoryAdapter);

        btnSave.setVisibility(View.INVISIBLE);
        btnBack.setVisibility(View.INVISIBLE);
        btnSetDate.setVisibility(View.INVISIBLE);
        btnSetTime.setVisibility(View.INVISIBLE);
        categoryLayout.setVisibility(View.INVISIBLE);
        statusLayout.setVisibility(View.INVISIBLE);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = Categories[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();


        btnSetDate.findViewById(R.id.setDate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });


        btnSetTime.findViewById(R.id.setTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime();
            }
        });

        btnNext.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtTitle.getText().toString().equals("") || edtTitle.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Fill all the details!", Toast.LENGTH_SHORT).show();
                }else {
                    btnSave.setVisibility(View.VISIBLE);
                    btnBack.setVisibility(View.VISIBLE);
                    btnSetDate.setVisibility(View.VISIBLE);
                    btnSetTime.setVisibility(View.VISIBLE);
                    categoryLayout.setVisibility(View.VISIBLE);
                    statusLayout.setVisibility(View.VISIBLE);

                    edtTitle.setVisibility(View.INVISIBLE);
                    edtDescription.setVisibility(View.INVISIBLE);
                    btnCancel.setVisibility(View.INVISIBLE);
                    btnNext.setVisibility(View.INVISIBLE);
                }
            }
        });
        btnBack.findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    btnSave.setVisibility(View.INVISIBLE);
                    btnBack.setVisibility(View.INVISIBLE);
                    btnSetDate.setVisibility(View.INVISIBLE);
                    btnSetTime.setVisibility(View.INVISIBLE);
                    categoryLayout.setVisibility(View.INVISIBLE);
                    statusLayout.setVisibility(View.INVISIBLE);

                    edtTitle.setVisibility(View.VISIBLE);
                    edtDescription.setVisibility(View.VISIBLE);
                    btnCancel.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
            }
        });
        btnCancel.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });
        btnSave.findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtDate.getText().toString().equals(null) || edtTime.getText().toString().equals(null)) {
                    Toast.makeText(MainActivity.this, "Fill all the details!!", Toast.LENGTH_SHORT).show();
                }
                else if(taskClass1.getTaskTitle() != null || taskClass1.getDescription()!=null){
                    TaskClass taskClass = new TaskClass(0,edtTitle.getText().toString(),edtDescription.getText().toString(),
                            edtDate.getText().toString(),edtTime.getText().toString(), category,txtStatus.getText().toString());
                    toDoTaskDatabase.modifyTask(taskClass1.getId(), taskClass);
                    alertDialog.dismiss();
                    HomeFragment homeFragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,homeFragment).commit();
                    homeFragment.updateRecyclerView();
                }
                else{
                    TaskClass taskClass = new TaskClass(0,edtTitle.getText().toString(),edtDescription.getText().toString(),
                            edtDate.getText().toString(),edtTime.getText().toString(), category,"New");
                    toDoTaskDatabase.saveTask(taskClass);

                    HomeFragment homeFragment = new HomeFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,homeFragment).commit();
                    homeFragment.updateRecyclerView();

                    alertDialog.dismiss();
                }
            }
        });

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.setCancelable(false);
        alertDialog.show();

    }

    private void setDate() {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
//                Calendar calendar1 = Calendar.getInstance();
//                calendar1.set(Calendar.YEAR, year);
//                calendar1.set(Calendar.MONTH, month);
//                calendar1.set(Calendar.DATE, date);
//
//                CharSequence charSequence = DateFormat.format("EEEE dd MMM yyy",calendar1);
//                edtDate.setText(charSequence);
                String showText = date+"-"+ (month+1) +"-"+year;
                edtDate.setText(showText);
            }
        },year,month,date);

        datePickerDialog.show();
    }

    private void setTime() {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, min);

                CharSequence charSequence = DateFormat.format("hh:mm a",calendar1);
                edtTime.setText(charSequence);
//                String showText = hour+":"+min;
//                edtTime.setText(showText);
            }
        },hour,min,true);

        timePickerDialog.show();
    }



    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    public void onFragmentInteraction(TaskClass taskClass) {

        showAddTaskDialog(taskClass);
    }
    public void SetTitle(String title) {

        setTitle(title);
    }
}