package com.example.to_dolistapp.DatabaseHandler;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ToDoTaskDatabase extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "taskDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TASK_TABLE = "taskCreated";
    private static final String ID_KEY = "id";
    private static final String TITLE_KEY = "title";
    private static final String DESCRIPTION_KEY = "description";
    private static final String DATE_KEY = "date";
    private static final String TIME_KEY = "time";
    private static final String CATEGORY_KEY = "category";
    private static final String STATUS_KEY = "status";

    public ToDoTaskDatabase(@Nullable Context context) {
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createDatabaseSQL = "create table " + TASK_TABLE +"(" + ID_KEY + " integer primary key autoincrement, "
                + TITLE_KEY + " varchar(255)" + ", " + DESCRIPTION_KEY + " text, " + DATE_KEY + " date, " + TIME_KEY+" time, "+
                CATEGORY_KEY+ " varchar(50), "+ STATUS_KEY + " varchar(50) )";
        sqLiteDatabase.execSQL(createDatabaseSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("drop table if EXISTS " + TASK_TABLE);
        onCreate(sqLiteDatabase);

    }
    public void saveTask(TaskClass taskClass) {
        SQLiteDatabase database = getWritableDatabase();
        String addSQLCommand = "insert into " + TASK_TABLE +
                " values(null,'" + taskClass.getTaskTitle() + "', '" + taskClass.getDescription()+"', '"+
                taskClass.getDate()+"', '"+taskClass.getTime()+"', '"+taskClass.getCategory()+"', '"+
                taskClass.getStatus()+ "')";
        database.execSQL(addSQLCommand);
        database.close();
    }

    public void deleteTaskFromDatabaseByID(int id) {

        SQLiteDatabase database = getWritableDatabase();
        String deleteSQLCommand = "delete from " + TASK_TABLE +
                " where " + ID_KEY + "=" + id;
        database.execSQL(deleteSQLCommand);
        database.close();
    }

    public void modifyTask(int id, TaskClass taskClass) {
        SQLiteDatabase database = getWritableDatabase();
        String modifySQLCommand = "UPDATE " + TASK_TABLE +
                " SET " + TITLE_KEY + "='" + taskClass.getTaskTitle() + "', " +
                DESCRIPTION_KEY + "='" + taskClass.getDescription() + "', " +
                DATE_KEY + "='" + taskClass.getDate() + "', " +
                TIME_KEY + "='" + taskClass.getTime() + "', " +
                CATEGORY_KEY + "='" + taskClass.getCategory() + "', " +
                STATUS_KEY + "='" + taskClass.getStatus() + "' " +
                " WHERE " + ID_KEY + " = " + id;
        database.execSQL(modifySQLCommand);
        database.close();
    }

    public ArrayList<TaskClass> returnAllTasks() {
        SQLiteDatabase database = getWritableDatabase();
        String sqlQueryCommand = "Select * from " + TASK_TABLE + " ORDER BY "+ DATE_KEY+" ASC, "+
                TIME_KEY+ " ASC";
        Cursor cursor = database.rawQuery(sqlQueryCommand, null);

        ArrayList<TaskClass> taskClasses = new ArrayList<>();
        while (cursor.moveToNext()) {

            TaskClass currentTaskClass = new TaskClass(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),cursor.getString(3),
                    cursor.getString(4),cursor.getString(5),cursor.getString(6));
            taskClasses.add(currentTaskClass);
        }
        database.close();
        return taskClasses;
    }

    public ArrayList<TaskClass> returnAllTasks(String status) {
        SQLiteDatabase database = getWritableDatabase();
        String sqlQueryCommand = "Select * from " + TASK_TABLE + " WHERE " + STATUS_KEY +
                " = '" +status + "' ORDER BY "+ DATE_KEY+" ASC, "+
                TIME_KEY+ " ASC";
        Cursor cursor = database.rawQuery(sqlQueryCommand, null);

        ArrayList<TaskClass> taskClasses = new ArrayList<>();
        while (cursor.moveToNext()) {

            TaskClass currentTaskClass = new TaskClass(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),cursor.getString(3),
                    cursor.getString(4),cursor.getString(5),cursor.getString(6));
            taskClasses.add(currentTaskClass);
        }
        database.close();
        return taskClasses;
    }
    public ArrayList<TaskClass> returnAllTasks(String status, String category) {
        SQLiteDatabase database = getWritableDatabase();
        String sqlQueryCommand;
        if(status == "All"){
            sqlQueryCommand = "Select * from " + TASK_TABLE +
                    " WHERE " + CATEGORY_KEY + " = '"+ category +"' ORDER BY "+ DATE_KEY+" ASC, "+
                    TIME_KEY+ " ASC";
        }else{
            sqlQueryCommand = "Select * from " + TASK_TABLE +
                    " WHERE " + STATUS_KEY + " = '"+ status+"' AND " + CATEGORY_KEY + " = '"+ category +"' ORDER BY "+ DATE_KEY+" ASC, "+
                    TIME_KEY+ " ASC";
        }

        Cursor cursor = database.rawQuery(sqlQueryCommand, null);

        ArrayList<TaskClass> taskClasses = new ArrayList<>();
        while (cursor.moveToNext()) {

            TaskClass currentTaskClass = new TaskClass(Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1), cursor.getString(2),cursor.getString(3),
                    cursor.getString(4),cursor.getString(5),cursor.getString(6));
            taskClasses.add(currentTaskClass);
        }
        database.close();
        return taskClasses;
    }
}
