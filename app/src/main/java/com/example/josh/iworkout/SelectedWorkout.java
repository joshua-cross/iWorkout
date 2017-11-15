package com.example.josh.iworkout;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;


import org.w3c.dom.Text;

import java.lang.reflect.Array;
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

    //an array for linear layouts for everything we wish to add to the excercise.
    ArrayList<LinearLayout> layouts = new ArrayList<>();

    //the overlay which will be displayed when the user presses a button.
    ConstraintLayout overlay;

    //the ID of the selected excercise.
    int selectedExcercise = 0;

    //the Finished and the next set buttons
    //Button nextSet;
    //Button finished;

    //a counter for how many sets have been selected.
    int setCounter = 0;

    Button cust;
    Dialog custom;
    TextView time;
    TextView currentExcercise;
    TextView txt;
    TextView counter;
    Button nextSet;
    Button finished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayShowTitleEnabled(false);

        workout = (TextView) findViewById(R.id.WorkoutName);

        //overlay = (ConstraintLayout) findViewById(R.id.overlay);



        //when the finished button has been pressed.
    }

    /*
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Set the dialog title
        builder.setTitle("Test")
                // Set the action buttons
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        return builder.create();
    }
    */

    public void draw() {

        //the workouts ID.
        int id = mServer.getSelectedWorkoutID();

        //ArrayList<ArrayList<String>> excercises = new ArrayList<>();
        //excercises = mServer.getExcercises();
        final LinearLayout layout = (LinearLayout) findViewById(R.id.excerciseLayout);


        for(int i = 0; i < excercises.get(id).size(); i = i + 1) {

            LinearLayout tempLayout = new LinearLayout(this);
            tempLayout.setBackgroundColor(getResources().getColor(R.color.white));
            LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.bottomMargin = 30;
            layoutParams.leftMargin = 10;
            layoutParams.rightMargin = 10;
            tempLayout.setLayoutParams(layoutParams);
            layout.addView(tempLayout);

            layouts.add(tempLayout);

            TextView tv = new TextView(this);
            tv.setText(excercises.get(id).get(i));
            //tv.setText("chuck");
            tv.setTextSize(18.0f);
            tv.setTextColor(getResources().getColor(R.color.mainText));
            //tv.setBackgroundColor(getResources().getColor(R.color.white));
            tv.setPadding(20, 30, 5, 10);
            tv.setId(i);

            Button start = new Button(this);
            //LinearLayout.LayoutParams params = start.getLayoutParams();
            LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //start.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            //params.addRule(LinearLayout.ALIGN_PARENT_RIGHT);
            //params.leftMargin = 700;
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

            tempLayout.addView(excerciseArray.get(i));
            tempLayout.addView(startArray.get(i));

        }

        for (int i = 0; i < startArray.size(); i = i + 1) {
            startArray.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setCounter = 0;

                    System.out.println(startArray.get(view.getId()));
                    //mServer.setWorkouts(view.getId());


                    //overlay.setVisibility(View.VISIBLE);
                    //layout.setVisibility(View.INVISIBLE);

                    selectedExcercise = view.getId();

                    //setting the text for the previous action.
                    //going to another activity.
                    //Intent intent = new Intent(ViewWorkouts.this, SelectedWorkout.class);
                    //startActivity(intent);

                    // TODO Auto-generated method stub
                    //getting all the elements from the dialog.xml file.
                    custom = new Dialog(SelectedWorkout.this);
                    custom.setContentView(R.layout.dialog);
                    time = (TextView) custom.findViewById(R.id.textView);
                    currentExcercise = (TextView)custom.findViewById(R.id.currentExcercise);
                    nextSet = (Button)custom.findViewById(R.id.NextSet);
                    finished = (Button)custom.findViewById(R.id.Finished);
                    counter = (TextView)custom.findViewById(R.id.CurrentSet);

                    //setting the current excercise to be the excercise that we have chosen.
                    currentExcercise.setText(excercises.get(mServer.getSelectedWorkoutID()).get(selectedExcercise) + "test");

                    //setting the counter to be 0 originally.
                    counter.setText("Current set: " + setCounter);

                    custom.setTitle("Excersise");

                    //when the nextset button has been pressed.
                    nextSet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            setCounter = setCounter + 1;
                            counter.setText("Current set: " + setCounter);
                        }

                    });

                    //when the finished button is pressed.
                    finished.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            int id = mServer.getSelectedWorkoutID();

                            //the value of the selectedExcercise text before the finished button has been pressed.
                            String excerciseValue = excercises.get(id).get(selectedExcercise).toString();
                            excerciseValue += "\n" + setCounter + " sets have been completed.";

                            excerciseArray.get(selectedExcercise).setText(excerciseValue);

                            //adding the number of sets to the end of this.

                            excerciseArray.get(selectedExcercise);
                            // TODO Auto-generated method stub


                            //changing the color from white to green using the value animator to inform the user that they have finished a certain excersise.
                            ValueAnimator anim = new ValueAnimator();
                            anim.setIntValues(getResources().getColor(R.color.white), getResources().getColor(R.color.green));
                            anim.setEvaluator(new ArgbEvaluator());
                            anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    layouts.get(selectedExcercise).setBackgroundColor((Integer)valueAnimator.getAnimatedValue());
                                }
                            });

                            anim.setDuration(1000);
                            anim.start();

                            startArray.get(selectedExcercise).setVisibility(View.INVISIBLE);

                            custom.dismiss();

                        }
                    });
                    custom.show();


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
