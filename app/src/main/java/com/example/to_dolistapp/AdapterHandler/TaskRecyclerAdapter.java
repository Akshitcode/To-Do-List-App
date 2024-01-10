package com.example.to_dolistapp.AdapterHandler;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolistapp.DatabaseHandler.TaskClass;
import com.example.to_dolistapp.HomeFragment;
import com.example.to_dolistapp.MainActivity;
import com.example.to_dolistapp.R;

import java.util.ArrayList;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskViewHolder> {


    ArrayList<TaskClass> taskClasses;

    public interface TaskIsClickedInterface {
        void taskIsClicked(ArrayList<TaskClass> taskClasses, TaskClass taskClass);

        void taskIsClickedLong(ArrayList<TaskClass> taskClasses, TaskClass taskClass);
    }

    private TaskIsClickedInterface taskIsClickedInterface;

    public TaskRecyclerAdapter(ArrayList<TaskClass> taskClasses,TaskIsClickedInterface taskIsClickedInterface){
        this.taskClasses = taskClasses;
        this.taskIsClickedInterface = taskIsClickedInterface;
    }


    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.task_view_holder, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.getTxtTitle().setText(taskClasses.get(position).getTaskTitle());
        holder.getTxtDate().setText(taskClasses.get(position).getDate());
        holder.getTxtDescription().setText(taskClasses.get(position).getDescription()+"...");
        holder.getTxtStatus().setText(taskClasses.get(position).getStatus());

        if(taskClasses.get(position).getStatus().equals("Completed")){
            holder.getCompletedCheckbox().setChecked(true);
            holder.getCompletedCheckbox().setClickable(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
  //              taskClasses.get(holder.getAdapterPosition()).setChecked(1);
                taskIsClickedInterface.taskIsClicked(taskClasses, taskClasses.get(holder.getAdapterPosition()));

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                taskIsClickedInterface.taskIsClickedLong(taskClasses, taskClasses.get(holder.getAdapterPosition()));
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskClasses.size();
    }

    public void addTask(ArrayList<TaskClass> taskClasses1) {
        taskClasses.clear();
        taskClasses.addAll(taskClasses1);
        notifyItemInserted(taskClasses.size() - 1);
    }
}
