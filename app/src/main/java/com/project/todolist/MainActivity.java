package com.project.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.todolist.adaptor.ToDoAdapter;

import com.project.todolist.model.ToDoModel;
import com.project.todolist.utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener{
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdaptor;
    private DatabaseHandler db;
    private List<ToDoModel> taskList;
    private FloatingActionButton fab;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        db=new DatabaseHandler(this);
        db.openDatabase();
        taskList = new ArrayList<>();
        tasksRecyclerView=findViewById(R.id.tasksRecyclerView);

        fab=findViewById(R.id.fab);
        tasksAdaptor=new ToDoAdapter(db,this);
        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdaptor));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksRecyclerView.setAdapter(tasksAdaptor);

        taskList=db.getAllTasks();
        Collections.reverse(taskList);
        tasksAdaptor.setTasks(taskList);

        fab.setOnClickListener(view -> AddNewTask.newInstance().show(getSupportFragmentManager(),AddNewTask.TAG));

    }
    @Override
    public void handleDialogClose(DialogInterface dialog){
        taskList=db.getAllTasks();
        Collections.reverse(taskList);//to show tasks in order we are reversing the order
        tasksAdaptor.setTasks(taskList);
        tasksAdaptor.notifyDataSetChanged();
    }
}