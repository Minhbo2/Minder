package com.example.minhb.minder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder>{

    List<TaskContainer> tasksList;
    Context context;


    public TaskAdapter(List<TaskContainer> tasksList, Context context) {
        this.tasksList = tasksList;
        this.context = context;
    }




    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        // creating a new ViewHolder with task_layout for layout
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);

        final RecyclerView taskRecyclerView = parent.findViewById(R.id.tasksRecyclerView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final int taskPosition = taskRecyclerView.getChildAdapterPosition(v);
                String selectedTaskObj = tasksList.get(taskPosition).getTaskId();

                // make sure the selected task is not an example empty task.
                if(!selectedTaskObj.equals("-1")) {
                    Intent intent = new Intent(context, NewTaskFormActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("TaskId", selectedTaskObj);
                    context.startActivity(intent);
                }
            }
        });


        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                final int taskPosition = taskRecyclerView.getChildAdapterPosition(v);
                TaskContainer task = tasksList.get(taskPosition);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
                query.whereEqualTo("name", task.getTaskName());
                query.whereEqualTo("description", task.getTaskDescription());
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        if(objects.size() > 0 && e == null)
                        {
                            for(ParseObject obj: objects)
                            {
                                obj.deleteInBackground(new DeleteCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            tasksList.remove(taskPosition);
                                            notifyDataSetChanged();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

                return true;
            }
        });


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        TaskContainer task = tasksList.get(position);

        holder.taskName.setText(task.getTaskName());
        holder.taskDescription.setText(task.getTaskDescription());
    }


    @Override
    public int getItemCount() {
        return tasksList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder
    {

        public TextView taskName;
        public TextView taskDescription;

        public ViewHolder(View itemView) {
            super(itemView);

            // on create, find textview for task name and description
            taskName = (TextView) itemView.findViewById(R.id.taskNameTextView);
            taskDescription = (TextView) itemView.findViewById(R.id.taskDescrTextView);
        }
    }
}


