package com.example.to_dolistapp;

import static android.app.ProgressDialog.show;

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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.to_dolistapp.AdapterHandler.TaskRecyclerAdapter;
import com.example.to_dolistapp.DatabaseHandler.TaskClass;
import com.example.to_dolistapp.DatabaseHandler.ToDoTaskDatabase;

import java.util.ArrayList;


public class CategoryFragment extends Fragment implements TaskRecyclerAdapter.TaskIsClickedInterface {


    private LinearLayout alltask, personalTask, workTask, meetingTask, shoppingTask, studyTask
            , partyTask, workoutTask, defaultTask;

    private ConstraintLayout categoriresView;

    private EditText edtTitle, edtDescription, edtDate, edtTime;
    private TextView txtCategory, statusUpdate;
    private Button btnStatus;

    private View view;
    private RecyclerView recyclerView;
    private ToDoTaskDatabase toDoTaskDatabase;
    private Spinner filterSpinner;
    String[] Filter = {"All", "New", "In-Progress","Completed"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.task_category, container, false);


        categoriresView = (ConstraintLayout) view.findViewById(R.id.categories);
        alltask = (LinearLayout) view.findViewById(R.id.allTasks);
        personalTask = (LinearLayout) view.findViewById(R.id.personalTasks);
        workTask = (LinearLayout) view.findViewById(R.id.workTask);
        meetingTask = (LinearLayout) view.findViewById(R.id.meetingTask);
        shoppingTask = (LinearLayout) view.findViewById(R.id.shoppingTask);
        studyTask = (LinearLayout) view.findViewById(R.id.studyTask);
        partyTask = (LinearLayout) view.findViewById(R.id.partyTask);
        workoutTask = (LinearLayout) view.findViewById(R.id.workoutTasks);
        defaultTask = (LinearLayout) view.findViewById(R.id.defaultTasks);

        view.findViewById(R.id.recyclerViewId1).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.taskDisplay).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.filterLayout).setVisibility(View.INVISIBLE);

        personalTask.findViewById(R.id.personalTasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showTheTasks("Personal");
            }
        });
        alltask.findViewById(R.id.allTasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showTheTasks("All");
            }
        });
        workoutTask.findViewById(R.id.workoutTasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showTheTasks("Workout");
            }
        });
        workTask.findViewById(R.id.workTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showTheTasks("Work");
            }
        });
        meetingTask.findViewById(R.id.meetingTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showTheTasks("Meeting");
            }
        });
        studyTask.findViewById(R.id.studyTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showTheTasks("Study");
            }
        });
        defaultTask.findViewById(R.id.defaultTasks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showTheTasks("Default");
            }
        });
        partyTask.findViewById(R.id.partyTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showTheTasks("Party");
            }
        });
        shoppingTask.findViewById(R.id.shoppingTask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                showTheTasks("Shopping");
            }
        });

        return view;
    }

    private void showTheTasks(String category) {
        categoriresView.setVisibility(View.INVISIBLE);
        view.findViewById(R.id.recyclerViewId1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.taskDisplay).setVisibility(View.VISIBLE);
        view.findViewById(R.id.filterLayout).setVisibility(View.VISIBLE);

        filterSpinner = (Spinner) view.findViewById(R.id.filterSpin);
        ArrayAdapter filterAdapter = new ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, Filter);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filterSpinner.setAdapter(filterAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view1, int position, long id) {
                ArrayList<TaskClass> taskClasses = toDoTaskDatabase.returnAllTasks(Filter[position], category);

                recyclerView = view.findViewById(R.id.recyclerViewId1);
                recyclerView.setAdapter(new TaskRecyclerAdapter(taskClasses, CategoryFragment.this));
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toDoTaskDatabase = new ToDoTaskDatabase(requireContext());

        view.findViewById(R.id.taskDisplay).setVisibility(View.INVISIBLE);

        ArrayList<TaskClass> taskClasses = toDoTaskDatabase.returnAllTasks();

        recyclerView = view.findViewById(R.id.recyclerViewId1);
        recyclerView.setAdapter(new TaskRecyclerAdapter(taskClasses, CategoryFragment.this));
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
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

    @Override
    public void taskIsClicked(ArrayList<TaskClass> taskClasses, TaskClass taskClass) {
        view.findViewById(R.id.recyclerViewId1).setVisibility(View.INVISIBLE);
        view.findViewById(R.id.taskDisplay).setVisibility(View.VISIBLE);
        showTaskDialog(taskClass);
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
                        taskClass.getCategory(),statusUpdate.getText().toString());

                ToDoTaskDatabase toDoTaskDatabase = new ToDoTaskDatabase(requireContext());
                toDoTaskDatabase.modifyTask(taskClass.getId(),taskClass1);
                ArrayList<TaskClass> taskClasses = toDoTaskDatabase.returnAllTasks();
                TaskRecyclerAdapter taskRecyclerAdapter = (TaskRecyclerAdapter) recyclerView.getAdapter();
                taskRecyclerAdapter.addTask(taskClasses);
                recyclerView.setAdapter(new TaskRecyclerAdapter(taskClasses, CategoryFragment.this));

            }
        });


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
                recyclerView.setAdapter(new TaskRecyclerAdapter(taskClasses, CategoryFragment.this));
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
        MainActivity mainActivity = (MainActivity) getActivity();

        if (mainActivity != null && mainActivity instanceof MainActivity) {

            mainActivity.onFragmentInteraction(taskClass);
        }
    }

    private void handleBackPressed() {
        if(view.findViewById(R.id.recyclerViewId1).getVisibility() == View.VISIBLE){
            categoriresView.setVisibility(View.VISIBLE);
            view.findViewById(R.id.filterLayout).setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            view.findViewById(R.id.taskDisplay).setVisibility(View.INVISIBLE);
        }
        else {
            view.findViewById(R.id.recyclerViewId1).setVisibility(View.VISIBLE);
            view.findViewById(R.id.filterLayout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.taskDisplay).setVisibility(View.INVISIBLE);
        }
        getParentFragmentManager().popBackStack();
    }
}