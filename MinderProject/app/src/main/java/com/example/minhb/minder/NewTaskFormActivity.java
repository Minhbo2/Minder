package com.example.minhb.minder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NewTaskFormActivity extends AppCompatActivity{


    TextView taskName;
    TextView taskDescription;
    ParseObject task;
    CalendarView calendarView;
    String selectedDate;
    List<String> favorableConditions;
    LinearLayout mainFormLinearLayout;
    LinearLayout weatherConditionLinearLayout;
    TextView weatherOptionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task_form);

        setTitle("Add a new task");

        taskName = findViewById(R.id.formTaskNameEditText);
        taskDescription = findViewById(R.id.formTaskDescEditText);
        calendarView = findViewById(R.id.calendarView);
        weatherOptionTextView = findViewById(R.id.weatherOptionTextView);
        weatherConditionLinearLayout = (LinearLayout) findViewById(R.id.weatherConditionLineaLayout);
        mainFormLinearLayout = (LinearLayout) findViewById(R.id.mainFormLinearLayout);

//        SimpleDateFormat formatDate = new SimpleDateFormat("EEE, MMM d, ''yy");
//        String currentDate = formatDate.format(new Date());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = Integer.toString(month + 1) + "/" + Integer.toString(dayOfMonth) + "/" + Integer.toString(year);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();


        Intent intent = getIntent();
        String taskId = intent.getStringExtra("TaskId");

        if(taskId != null)
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Task");
            query.whereEqualTo("objectId", taskId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> objects, ParseException e) {
                    if(e == null)
                    {
                        if(objects.size() > 0)
                        {
                            task = objects.get(0);
                            taskName.setText(task.getString("name"));
                            taskDescription.setText(task.getString("description"));
                            weatherOptionTextView.setText(objects.get(0).getJSONArray("weatherConditions").toString());

                            // calendar sample code usages: https://www.codota.com/code/java/methods/android.widget.CalendarView/setDate
                            selectedDate = task.getString("intentDate");
                            String[] parts = selectedDate.split("/");

                            int month = Integer.parseInt(parts[0]);
                            int day = Integer.parseInt(parts[1]);
                            int year = Integer.parseInt(parts[2]);

                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, (month - 1));
                            calendar.set(Calendar.DAY_OF_MONTH, day);

                            long milliTime = calendar.getTimeInMillis();
                            calendarView.setDate(milliTime);
                        }
                    }
                }
            });
        }
    }


    public void createTask(View view)
    {
        if (!taskName.getText().toString().matches("") && !taskDescription.getText().toString().matches(""))
        {
            if(task != null)
            {
                task.put("name", taskName.getText().toString());
                task.put("description", taskDescription.getText().toString());
                task.put("intentDate", selectedDate);
                task.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null)
                        {
                            Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
            else
            {
                String[] weatherConditions = new String[favorableConditions.size()];
                for(int i = 0; i < favorableConditions.size(); i++)
                    weatherConditions[i] = favorableConditions.get(i);

                JSONArray jsonArray= null;
                try {
                    jsonArray = new JSONArray(weatherConditions);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ParseObject newTask = new ParseObject("Task");
                newTask.put("name", taskName.getText().toString());
                newTask.put("description", taskDescription.getText().toString());
                newTask.put("intentDate", selectedDate);
                newTask.put("weatherConditions", jsonArray);
                newTask.put("userId", ParseUser.getCurrentUser().getObjectId());
                newTask.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
                            startActivity(intent);
                        } else
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


    public void selectWeather(View view)
    {
        favorableConditions = new ArrayList<>();

        mainFormLinearLayout.setVisibility(View.INVISIBLE);
        weatherConditionLinearLayout.setVisibility(View.VISIBLE);

        int weatherConditionCount =  weatherConditionLinearLayout.getChildCount();

        for(int i = 0; i < weatherConditionCount; i++)
        {
            final View currentCondition = weatherConditionLinearLayout.getChildAt(i);
            if(currentCondition instanceof android.support.v7.widget.AppCompatTextView) {
                currentCondition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        populateCondition((TextView) v);
                    }
                });
            }
        }
    }



    private void populateCondition(TextView textView)
    {
        String condition = textView.getText().toString();
        favorableConditions.add(condition);

        Log.i("Current Condition: ", favorableConditions.toString());
    }




    public void backButton(View view)
    {
        mainFormLinearLayout.setVisibility(View.VISIBLE);
        weatherConditionLinearLayout.setVisibility(View.INVISIBLE);

        if(favorableConditions.size() > 0)
            weatherOptionTextView.setText(favorableConditions.toString());
    }
}
