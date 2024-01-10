package com.example.to_dolistapp;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.to_dolistapp.AdapterHandler.TaskRecyclerAdapter;
import com.example.to_dolistapp.DatabaseHandler.TaskClass;
import com.example.to_dolistapp.DatabaseHandler.ToDoTaskDatabase;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements TaskRecyclerAdapter.TaskIsClickedInterface {


//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    private RecyclerView recyclerView;
    private ToDoTaskDatabase toDoTaskDatabase;

    private View view;
    private EditText edtTitle, edtDescription, edtDate, edtTime;
    private TextView txtCategory, statusUpdate;

    private Button btnStatus;
    private Spinner filterSpinner;
    String[] Filter = {"All", "New", "In-Progress","Completed"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_home, container, false);
        toDoTaskDatabase = new ToDoTaskDatabase(requireContext());

        view.findViewById(R.id.taskDisplay).setVisibility(View.INVISIBLE);

        filterSpinner = (Spinner) view.findViewById(R.id.filterSpin);
        ArrayAdapter filterAdapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, Filter);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filterSpinner.setAdapter(filterAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {
                ArrayList<TaskClass> taskClasses;
                if(Filter[position] == "All") {
                    taskClasses = toDoTaskDatabase.returnAllTasks();
                }
                else {
                    taskClasses = toDoTaskDatabase.returnAllTasks(Filter[position]);
                }
                recyclerView = view.findViewById(R.id.recyclerViewId1);
                recyclerView.setAdapter(new TaskRecyclerAdapter(taskClasses, HomeFragment.this));
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPressed();
            }
        });
    }

    public void updateRecyclerView(){
//        toDoTaskDatabase = new ToDoTaskDatabase(requireContext());
//        ArrayList<TaskClass> taskClasses = toDoTaskDatabase.returnAllTasks();
//        TaskRecyclerAdapter taskRecyclerAdapter = (TaskRecyclerAdapter) recyclerView.getAdapter();
//        taskRecyclerAdapter.addTask(taskClasses);

//        Toast.makeText(requireContext(), "hua hua", Toast.LENGTH_SHORT).show();

    }


    @Override
    public void taskIsClicked(ArrayList<TaskClass> taskClasses, TaskClass taskClass) {
        view.findViewById(R.id.recyclerViewId1).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.filterLayout).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.taskDisplay).setVisibility(View.VISIBLE);
        showTaskDialog(taskClass);
    }

    @Override
    public void taskIsClickedLong(ArrayList<TaskClass> taskClasses, TaskClass taskClass) {

        String alertTitle = getString(R.string.query);
        String setPositiveButtonTitle = getString(R.string.positiveTitle);
        String setNegativeButtonTitle = getString(R.string.negativeTitle);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(requireContext());
        alertBuilder.setTitle(alertTitle);
        alertBuilder.setPositiveButton(setPositiveButtonTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ToDoTaskDatabase toDoTaskDatabase = new ToDoTaskDatabase(requireContext());
                toDoTaskDatabase.deleteTaskFromDatabaseByID(taskClass.getId());

                ArrayList<TaskClass> taskClasses1 = toDoTaskDatabase.returnAllTasks();

                TaskRecyclerAdapter taskRecyclerAdapter = (TaskRecyclerAdapter) recyclerView.getAdapter();
                taskRecyclerAdapter.addTask(taskClasses1);
                recyclerView.setAdapter(new TaskRecyclerAdapter(taskClasses, HomeFragment.this));
                dialog.dismiss();

            }
        });
        alertBuilder.setNegativeButton(setNegativeButtonTitle, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                callMainActivityFunction(taskClass);
                dialog.dismiss();
            }
        });

        alertBuilder.setCancelable(false);
        alertBuilder.create().show();
    }

    private void callMainActivityFunction(TaskClass taskClass) {
        // Obtain a reference to the MainActivity
        MainActivity mainActivity = (MainActivity) getActivity();

        // Check if the activity is not null and is of the expected type
        if (mainActivity != null && mainActivity instanceof MainActivity) {
            // Call the function defined in MainActivity

            mainActivity.onFragmentInteraction(taskClass);
        }
    }

    private void showTaskDialog(TaskClass taskClass) {

        edtTitle = (EditText) view.findViewById(R.id.edt_Title);
        edtDescription =(EditText) view.findViewById(R.id.edt_Task_Description);
        edtTime = (EditText) view.findViewById(R.id.editTextTime);
        edtDate = (EditText) view.findViewById(R.id.editTextDate);
        txtCategory = (TextView) view.findViewById(R.id.category);
        btnStatus = (Button) view.findViewById(R.id.btnStatus);
        statusUpdate = (TextView) view.findViewById(R.id.updateStatus);

        edtTitle.setText(taskClass.getTaskTitle());
        edtDescription.setText(taskClass.getDescription());
        edtDate.setText(taskClass.getDate());
        edtTime.setText(taskClass.getTime());
        statusUpdate.setText(taskClass.getStatus());
        if(statusUpdate.getText().toString().equals("Completed")) {
            btnStatus.setText("Completed");
            btnStatus.setBackgroundColor(Color.MAGENTA);
        }

        txtCategory.setText(taskClass.getCategory());

        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(statusUpdate.getText().toString().equals("New")){
                    statusUpdate.setText("In-Progress");
                    btnStatus.setText("Mark as finish");
                }else if(statusUpdate.getText().toString().equals("In-Progress")){
                    statusUpdate.setText("Completed");
                    btnStatus.setText("Completed");
                    btnStatus.setBackgroundColor(Color.MAGENTA);
                }
                else if(statusUpdate.getText().toString().equals("Completed")) {
                    btnStatus.setText("Completed");
                    btnStatus.setBackgroundColor(Color.MAGENTA);
                }

                TaskClass taskClass1 = new TaskClass(0,taskClass.getTaskTitle(),taskClass.getDescription(),taskClass.getDate(),taskClass.getTime(),
                        taskClass.getCategory(), statusUpdate.getText().toString());

                ToDoTaskDatabase toDoTaskDatabase = new ToDoTaskDatabase(requireContext());
                toDoTaskDatabase.modifyTask(taskClass.getId(),taskClass1);
                ArrayList<TaskClass> taskClasses = toDoTaskDatabase.returnAllTasks();
                TaskRecyclerAdapter taskRecyclerAdapter = (TaskRecyclerAdapter) recyclerView.getAdapter();
                taskRecyclerAdapter.addTask(taskClasses);
                recyclerView.setAdapter(new TaskRecyclerAdapter(taskClasses, HomeFragment.this));

            }
        });




    }
    private void handleBackPressed() {
        if(view.findViewById(R.id.recyclerViewId1).getVisibility() == View.VISIBLE){
            getActivity().finish();
        }
        view.findViewById(R.id.recyclerViewId1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.filterLayout).setVisibility(View.VISIBLE);
        view.findViewById(R.id.taskDisplay).setVisibility(View.INVISIBLE);
        getParentFragmentManager().popBackStack();
    }



}