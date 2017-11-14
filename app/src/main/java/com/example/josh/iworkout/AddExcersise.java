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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddExcersise extends AppCompatActivity {


    //variables needed to connect to the dates service.
    boolean mBounded;
    Workouts mServer;

    //integer that counts the amount of excersises in a specific workout.
    int excersiseCount = 0;

    //displaying the current excersise count
    TextView currExc;

    //the next and finished buttons
    Button next;
    Button finished;

    //the excercise name and weight user input.
    EditText excName;
    EditText excWeight;

    //the tickboxes on the activity.
    CheckBox bodyWeight;
    CheckBox freeWeight;

    //the error message.
    TextView error;

    TextView tWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_excersise);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayShowTitleEnabled(false);

        error = (TextView) findViewById(R.id.excError);
        currExc = (TextView) findViewById(R.id.currentExc);
        currExc.setText("Excersise: " + excersiseCount);

        finished = (Button) findViewById(R.id.finished);
        next = (Button) findViewById(R.id.nextExcersise);

        excName = (EditText) findViewById(R.id.Exc);
        excWeight = (EditText) findViewById(R.id.currentWeight);

        bodyWeight = (CheckBox) findViewById(R.id.bodyWeight);
        freeWeight = (CheckBox) findViewById(R.id.weighted);

        tWeight = (TextView) findViewById(R.id.tWeight);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String exc = excName.getText().toString();
                //int weight = Integer.parseInt(excWeight.getText().toString());
                boolean isBodyWeight = bodyWeight.isChecked();
                boolean isWeighed = freeWeight.isChecked();

                System.out.println("exc: " + exc + " weight: " + excWeight.getText().toString() + " isBodyWeight: " + isBodyWeight + " isFreeWeight: " + isWeighed);

                //adding an excersise using the inputs.
                boolean added = addExcersise(exc, excWeight.getText().toString(), isBodyWeight, isWeighed);

                //if we successfully added an excercise.
                if(added) {
                    error.setText("");
                    bodyWeight.setChecked(false);
                    freeWeight.setChecked(false);
                    excWeight.setText("");
                    excName.setText("");
                    //incrementing the excercise count
                    excersiseCount = excersiseCount + 1;
                    //setting the count text to be this.
                    currExc.setText("Excersise: " + excersiseCount);
                    //ensuring everything is visible and usable.
                    excWeight.setVisibility(View.VISIBLE);
                    tWeight.setVisibility(View.VISIBLE);
                    //excWeight.setClickable(true);
                    //excWeight.setFocusable(true);
                }

            }
        });

        finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //only finish the excersise adding process if we have atleast 1 excersise.
                if(excersiseCount != 0) {

                    mServer.finished();
                    //going to another activity.
                    Intent intent = new Intent(AddExcersise.this, MainActivity.class);
                    startActivity(intent);

                } else {
                    error.setText("Please add at least 1 excersise");
                }



            }
        });

        bodyWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //deactivating the weight when the bodyWeight check box is selected.
                if(bodyWeight.isChecked()) {
                    //excWeight.setClickable(false);
                    //excWeight.setFocusable(false);
                    excWeight.setVisibility(View.INVISIBLE);
                    tWeight.setVisibility(View.INVISIBLE);
                } else {
                    //excWeight.setClickable(true);
                    //excWeight.setFocusable(true);
                    excWeight.setVisibility(View.VISIBLE);
                    tWeight.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    //method to be called when either of the buttons is called which will add the specified excercise to the Workouts service.
    private boolean addExcersise(String name, String weight, boolean isBodyWeight, boolean isWeight) {
        //firstly, if we have both the isWeight, and isBodyWeight ticked then we will display an error message and do nothing.
        if(isBodyWeight && isWeight) {
            error.setText("Please only tick one box.");
            return false;
        }
        //else we have only ticked one box and we can continue.
        else {
            //if we have filled out the form properly.
            //first we will check what checkbox has been pressed.
            //if it is the free weight box then we will check all fields including weight.
            if(isWeight) {
                if (!name.isEmpty() && !weight.isEmpty()) {
                    //converting the value of weight into an int.
                    int iWeight = Integer.parseInt(weight);
                    //if iWeight is 0 then it's not a valid weight.
                    if(iWeight == 0) {
                        error.setText("Weight cannot be 0.");
                        return false;
                    }

                    mServer.addExcercise(name);
                    mServer.addIsBodyWeight(true);
                    mServer.addWeight(iWeight);

                    return true;

                }
                //else we have not filled out the form properly so we will set an error message and return.
                else {
                    error.setText("Please fill out all fields.");
                    return false;
                }

            //if it's bodyweight we only have to check the
            } else if(isBodyWeight) {
                if(!name.isEmpty()) {

                    //body weight is selected so the weight is 0.
                    mServer.addExcercise(name);
                    mServer.addIsBodyWeight(false);
                    mServer.addWeight(0);

                    return true;

                } else {
                    error.setText("Please fill out all fields.");
                    return false;
                }
            }
            //else neither of the boxes
            else {
                error.setText("Please fill out all fields.");
                return false;
            }
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
            Toast.makeText(AddExcersise.this, "Service is disconnected", 1000).show();
            mBounded = false;
            mServer = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(AddExcersise.this, "Service is connected", 1000).show();
            mBounded = true;
            Workouts.LocalBinder mLocalBinder = (Workouts.LocalBinder)service;
            mServer = mLocalBinder.getServerInstance();


            Context context = AddExcersise.this.getApplicationContext();
            //Context context = MainActivity.this.getApplicationContext();
            TinyDB tinydb = new TinyDB(context);

            mServer.checkWorkout();

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
        //String menu3 = getResources().getString(R.string.menuText3);
        //String menu4 = getResources().getString(R.string.menuText4);
        //String menu5 = getResources().getString(R.string.menuText5);

        if(item.toString().equals(menu1)) {
            Intent days = new Intent (this, MainActivity.class);
            startActivity(days);
        } else if(item.toString().equals(menu2)) {
            Intent addWorkout = new Intent (this, AddWorkout.class);
            startActivity(addWorkout);
        }
        /*
        else if(item.toString().equals(menu3)) {
            Intent editPage = new Intent (this, ChartYearlyPreview.class);
            startActivity(editPage);
        } else if(item.toString().equals(menu4)) {
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
