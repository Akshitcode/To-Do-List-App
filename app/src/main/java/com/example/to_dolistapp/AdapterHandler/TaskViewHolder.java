package com.example.to_dolistapp.AdapterHandler;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolistapp.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    private TextView txtTitle, txtDescription, txtDate, txtStatus;
    private CheckBox completedCheckbox;


    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);

        completedCheckbox = itemView.findViewById(R.id.completedCheckBox);
        txtTitle = itemView.findViewById(R.id.txtTitle);
        txtDescription = itemView.findViewById(R.id.txtDescription);
        txtDate = itemView.findViewById(R.id.txtDate);
        txtStatus = itemView.findViewById(R.id.txtStatus);

    }

    public CheckBox getCompletedCheckbox() {
        return completedCheckbox;
    }

    public TextView getTxtTitle() {
        return txtTitle;
    }

    public TextView getTxtDescription() {
        return txtDescription;
    }

    public TextView getTxtDate() {
        return txtDate;
    }

    public TextView getTxtStatus() {
        return txtStatus;
    }
}
