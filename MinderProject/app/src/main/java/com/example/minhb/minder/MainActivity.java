package com.example.minhb.minder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Welcome to Minder");

//        if(ParseUser.getCurrentUser() != null)
//            showUserTasksActivity();

        ParseUser.logOut();

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }


    public void authenticationForm(View view)
    {
        int buttonId = view.getId();
        if(buttonId == R.id.signUpFormButton)
        {
            LinearLayout signUpLinearLayout = findViewById(R.id.signUpLinearLayout);
            signUpLinearLayout.setVisibility(View.VISIBLE);
        }
        else if(buttonId == R.id.logInFormButton)
        {
            LinearLayout logInLinearLayout = findViewById(R.id.logInLinearLayout);
            logInLinearLayout.setVisibility(View.VISIBLE);
        }

        View parent = (View)view.getParent();
        parent.setVisibility(View.INVISIBLE);
    }



    public void onSignUp(View view)
    {
        TextView username = findViewById(R.id.signUpUsernameEditText);
        TextView password = findViewById(R.id.signUpPasswordEditText);
        TextView email = findViewById(R.id.signUpEmailEditText);

        if(!username.getText().toString().matches("") && !password.getText().toString().matches("") && !email.getText().toString().matches(""))
        {
            ParseUser user = new ParseUser();
            user.setUsername(username.getText().toString());
            user.setPassword(password.getText().toString());
            user.setEmail(email.getText().toString());

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if(e == null)
                        showUserTasksActivity();
                    else
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }
        else
            Toast.makeText(this, "Sign Up Error!", Toast.LENGTH_LONG).show();
    }


    public void onLogIn(View view)
    {
        TextView username = findViewById(R.id.logInUsernameEditText);
        TextView password = findViewById(R.id.logInPasswordEditText);

        if(!username.getText().toString().matches("") && !password.getText().toString().matches(""))
        {
            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if(user != null && e == null)
                        showUserTasksActivity();
                    else
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private void showUserTasksActivity()
    {
        Intent intent = new Intent(this, AddTaskActivity.class);
        startActivity(intent);
    }




    //TODO: (1)refactor
    /*
    * MAIN: simplify the authentication process
    * NEWTASKFORM: redo weather selection option for icons instead of strings*/

    //TODO: (2)UI/UX
    /*
    * MAIN: dress up the landing page, sign up and login
    * ADDTASK: each task will be a card with name, description and intent date with a row of selected weather icons
    * each card will have a weather forecast icon as back ground if forecast is within 5 days of the intent date
    * NEWTASKFORM: dress up the new/edit task form*/
}
