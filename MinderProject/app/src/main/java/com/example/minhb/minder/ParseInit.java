package com.example.minhb.minder;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;

public class ParseInit extends Application
{
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("a874a0d02751aa3daacaba2368a872aee006b7eb")
                .server("http://52.53.230.153:80/parse/")
                .clientKey("7ad5a5726475896025510831a97b2761985fd2db")
                .build()
        );


        // disable this setting if we do not want user to automatic sign up or sign in
        // ParseUser.enableAutomaticUser();

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

//        parse dashboard pass : IIS7RaCZYzPg
//        dsn: ec2-52-53-230-153.us-west-1.compute.amazonaws.com
    }
}
