package com.example.minhb.minder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.minhb.minder.R;
import com.example.minhb.minder.TaskAdapter;
import com.example.minhb.minder.TaskContainer;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AddTaskActivity extends BaseActivity {

    RecyclerView tasksRecyclerView;
    List<TaskContainer> tasksList;
    TaskAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setTitle("Tasks List");

        tasksRecyclerView = (RecyclerView) findViewById(R.id.tasksRecyclerView);
        tasksRecyclerView.setHasFixedSize(true);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tasksList = new ArrayList<TaskContainer>();
    }


    @Override
    protected void onStart() {
        super.onStart();

        tasksList.clear();

        ParseQuery<ParseObject> taskQuery = new ParseQuery<ParseObject>("Task");
        taskQuery.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId());
        taskQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null) {
                    if (objects.size() > 0) {
                        for (int i = 0; i < objects.size(); i++) {

                            String taskName = objects.get(i).getString("name");
                            String taskDescription = objects.get(i).getString("description");
                            String taskId = objects.get(i).getObjectId();

                            TaskContainer task = new TaskContainer(taskId, taskName, taskDescription);
                            tasksList.add(task);
                        }
                    } else {
                        TaskContainer exampleTask = new TaskContainer("-1","Add a new task", "You have no active task");
                        tasksList.add(exampleTask);
                    }

                    adapter = new TaskAdapter(tasksList, getApplicationContext());
                    tasksRecyclerView.setAdapter(adapter);
                }
            }
        });
    }

    public void addTask(View view)
    {
        Intent intent = new Intent(this, NewTaskFormActivity.class);
        startActivity(intent);
    }


}
