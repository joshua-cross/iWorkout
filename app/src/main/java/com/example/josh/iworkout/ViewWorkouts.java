package com.example.josh.iworkout;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ViewWorkouts extends AppCompatActivity {

    //variables needed to connect to the dates service.
    boolean mBounded;
    Workouts mServer;

    //the workouts.
    ArrayList<String> workouts = new ArrayList<>();

    //the excercises.
    ArrayList<ArrayList<String>> excercises = new ArrayList<>();

    ArrayList<TextView> workoutText = new ArrayList<>();
    ArrayList<TextView> excersiseText = new ArrayList<>();

    int counter = 0;

    String currentWorkout = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_workouts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayShowTitleEnabled(false);

    }



    //function that draws the workouts and the excersises to the activity.
    public void draw() {
        for(int i = 0; i < workouts.size(); i = i + 1) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.layout);

            TextView tv = new TextView(this);
            tv.setText(workouts.get(i));
            tv.setTextSize(18.0f);
            tv.setTextColor(getResources().getColor(R.color.mainText));
            tv.setBackgroundColor(getResources().getColor(R.color.white));
            tv.setPadding(20, 30, 5, 10);
            tv.setId(i);

            workoutText.add(tv);

            String allExc = "";

            currentWorkout = workouts.get(i).toString();

            //excercises is an array of arrays so here we are going through the length of the ith array.
            for(int x = 0; x < excercises.get(i).size(); x = x + 1) {
                //we are ammending the string, and adding each of the excercises in the specific array.
                allExc += excercises.get(i).get(x);
                //if we are not at the end of the array we will add a comma, else we will not.
                if(x != excercises.get(i).size() - 1) {
                    allExc += ", ";
                }
            }

            TextView exc = new TextView(this);
            exc.setTextSize(16.0f);
            exc.setText(allExc);
            exc.setBackgroundColor(getResources().getColor(R.color.white));
            exc.setPadding(40, 0, 5, 10);
            exc.setTextColor(getResources().getColor(R.color.mainText));

            excersiseText.add(exc);

            TextView blank = new TextView(this);
            blank.setTextSize(6.0f);
            //blank.setPadding(0, 2, 0, 2);

            layout.addView(workoutText.get(i));
            layout.addView(excersiseText.get(i));
            //creating a space between the workouts.
            layout.addView(blank);


            counter = counter + 1;
        }

        for (int i = 0; i < workoutText.size(); i = i + 1) {
            workoutText.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println(workouts.get(view.getId()));


                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, Workouts.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    };

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(ViewWorkouts.this, "Service is disconnected", 1000).show();
            mBounded = false;
            mServer = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(ViewWorkouts.this, "Service is connected", 1000).show();
            mBounded = true;
            Workouts.LocalBinder mLocalBinder = (Workouts.LocalBinder)service;
            mServer = mLocalBinder.getServerInstance();


            Context context = ViewWorkouts.this.getApplicationContext();
            //Context context = MainActivity.this.getApplicationContext();
            TinyDB tinydb = new TinyDB(context);

            //mServer.checkWorkout();
            //getting the workouts from the Workouts service.
            workouts = mServer.getWorkouts();

            //getting excercises from the workouts service
            excercises = mServer.getExcercises();

            draw();



        }
    };


    //for the drop down menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String menu1 = getResources().getString(R.string.menu1);
        String menu2 = getResources().getString(R.string.menu2);
        String menu3 = getResources().getString(R.string.menu3);
        //String menu4 = getResources().getString(R.string.menuText4);
        //String menu5 = getResources().getString(R.string.menuText5);

        if(item.toString().equals(menu1)) {
            Intent days = new Intent (this, MainActivity.class);
            startActivity(days);
        } else if(item.toString().equals(menu2)) {
            Intent addWorkout = new Intent (this, AddWorkout.class);
            startActivity(addWorkout);
        }

        else if(item.toString().equals(menu3)) {
            Intent editPage = new Intent (this, ViewWorkouts.class);
            startActivity(editPage);
        }
        /*
        else if(item.toString().equals(menu4)) {
            Intent editPage = new Intent (this, Edit.class);
            startActivity(editPage);
        } else if(item.toString().equals(menu5)) {
            Intent editPage = new Intent (this, EditGoalsAndSleepTimes.class);
            startActivity(editPage);
        }
        */


        return super.onOptionsItemSelected(item);
    }
}
