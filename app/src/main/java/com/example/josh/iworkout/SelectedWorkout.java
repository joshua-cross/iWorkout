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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class SelectedWorkout extends AppCompatActivity {

    //variables needed to connect to the dates service.
    boolean mBounded;
    Workouts mServer;

    //the text view which will store the name of the current workout.
    TextView workout;
    ArrayList<ArrayList<String>> excercises = new ArrayList<>();

    ArrayList<TextView> excerciseArray = new ArrayList<>();

    ArrayList<Button> startArray = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayShowTitleEnabled(false);

        workout = (TextView) findViewById(R.id.WorkoutName);
    }

    public void draw() {

        //the workouts ID.
        int id = mServer.getSelectedWorkoutID();

        //ArrayList<ArrayList<String>> excercises = new ArrayList<>();
        //excercises = mServer.getExcercises();
        LinearLayout layout = (LinearLayout) findViewById(R.id.excerciseLayout);


        for(int i = 0; i < excercises.get(id).size(); i = i + 1) {

            TextView tv = new TextView(this);
            tv.setText(excercises.get(id).get(i));
            //tv.setText("chuck");
            tv.setTextSize(18.0f);
            tv.setTextColor(getResources().getColor(R.color.mainText));
            tv.setBackgroundColor(getResources().getColor(R.color.white));
            tv.setPadding(20, 30, 5, 10);
            tv.setId(i);

            Button start = new Button(this);
            //LinearLayout.LayoutParams params = start.getLayoutParams();
            LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //start.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            //params.addRule(LinearLayout.ALIGN_PARENT_RIGHT);
            params.leftMargin = 700;
            params.topMargin = 10;
            params.bottomMargin = 30;
            start.setLayoutParams(params);
            //params.addRule(LinearLayout.ALIGN_PARENT_RIGHT);
            start.setText("Start");
            start.setTextSize(16.0f);
            start.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            start.setBackgroundColor(getResources().getColor(R.color.green));
            start.setTextColor(getResources().getColor(R.color.white));
            start.setId(i);
            //start.setPadding(100, 0, 5, 10);

            excerciseArray.add(tv);
            startArray.add(start);

            layout.addView(excerciseArray.get(i));
            layout.addView(startArray.get(i));

        }

        for (int i = 0; i < startArray.size(); i = i + 1) {
            startArray.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    System.out.println(startArray.get(view.getId()));
                    mServer.setWorkouts(view.getId());

                    //going to another activity.
                    //Intent intent = new Intent(ViewWorkouts.this, SelectedWorkout.class);
                    //startActivity(intent);

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
            Toast.makeText(SelectedWorkout.this, "Service is disconnected", 1000).show();
            mBounded = false;
            mServer = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(SelectedWorkout.this, "Service is connected", 1000).show();
            mBounded = true;
            Workouts.LocalBinder mLocalBinder = (Workouts.LocalBinder)service;
            mServer = mLocalBinder.getServerInstance();


            Context context = SelectedWorkout.this.getApplicationContext();
            //Context context = MainActivity.this.getApplicationContext();
            TinyDB tinydb = new TinyDB(context);

            //mServer.checkWorkout();
            //getting the workouts from the Workouts service.
            //workouts = mServer.getWorkouts();

            //getting excercises from the workouts service
            //excercises = mServer.getExcercises();

            workout.setText(mServer.getSelectedWorkout());

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
