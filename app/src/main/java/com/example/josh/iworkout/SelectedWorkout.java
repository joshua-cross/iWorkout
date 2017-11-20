package com.example.josh.iworkout;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;


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

    //an array for all the weight text views.
    ArrayList<TextView> weightArray = new ArrayList<>();

    ArrayList<Button> startArray = new ArrayList<>();

    //an array for linear layouts for everything we wish to add to the excercise.
    ArrayList<LinearLayout> layouts = new ArrayList<>();

    //the overlay which will be displayed when the user presses a button.
    ConstraintLayout overlay;

    //the ID of the selected excercise.
    int selectedExcercise = 0;

    //the button which will create the superset.
    Button superset;

    //the button that determines when the user has finished creating there superset.
    Button createSuperset;

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
    Button start;
    EditText newWeight;
    EditText newReps;
    TextView repsText;
    TextView weightText;

    //the amount of minutes the user is performing an excercise.
    int minutes = 0;
    //the amount of seconds the user is performing an excercise.
    int seconds = 0;

    //boolean that checks if the user is in a dialog.
    boolean isWorkingOut = false;
    int reps = 0;

    //the default
    int rest = 60000;

    //the base resting time is 1.0f.
    double baseRest = 1.0;

    //when the create superset button is clicked.
    boolean isSelectingSuperset = false;


    //an array list for the current superset; it will hold the id's of the selected linear arrays so we know which excercises to delete and add as a superset.
    ArrayList<Integer> currSuperSet = new ArrayList<>();

    //an arraylist that holds all the supersets.
    ArrayList<ArrayList<Integer>> supersets = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_workout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayShowTitleEnabled(false);

        workout = (TextView) findViewById(R.id.WorkoutName);
        superset = (Button) findViewById(R.id.createSuperset);

        createSuperset = (Button) findViewById(R.id.finishedSuperset);

        //starting the counter, however this will only be ran when an excersise is chosen
        secondCounter();

        //overlay = (ConstraintLayout) findViewById(R.id.overlay);
        final Context context = this;

        superset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //System.out.println("Hmmm");
                isSelectingSuperset = true;
            }
        });

        createSuperset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //the layout for the activity.
                final LinearLayout layout = (LinearLayout) findViewById(R.id.excerciseLayout);

                //adding the previous created superset to the supersets ArrayList.
                supersets.add(currSuperSet);

                String excerciseText = "";
                ArrayList<String> weightText = new ArrayList<>();

                int id = mServer.getSelectedWorkoutID();


                //temporary string that holds the weight of the specific excercise.
                String tempWeight;

                //going through the currently selected excercises in the superset.
                for (int i = 0; i < currSuperSet.size(); i = i + 1) {
                    //removing the selected layouts from the activity.
                    layout.removeView(layouts.get(currSuperSet.get(i)));
                    if(i == 0) {
                        excerciseText += excercises.get(id).get(currSuperSet.get(i));
                    } else {
                        excerciseText += ", " + excercises.get(id).get(currSuperSet.get(i));
                    }

                    //setting the weight to be the excercise followed by the weights.
                    tempWeight = excercises.get(id).get(currSuperSet.get(i)) + "weights: " +
                            mServer.getStartWeight(id, currSuperSet.get(i)) + " -> " +
                            mServer.getCurrentPreviousWeight(id, currSuperSet.get(i));

                    //adding the temp string to the weightText arraylist
                    weightText.add(tempWeight);

                }

                //adding a new layout with the new text to the screen.
                LinearLayout tempLayout = new LinearLayout(context);
                tempLayout.setBackgroundColor(getResources().getColor(R.color.white));
                LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.bottomMargin = 30;
                layoutParams.leftMargin = 10;
                layoutParams.rightMargin = 10;
                tempLayout.setOrientation(LinearLayout.VERTICAL);
                tempLayout.setLayoutParams(layoutParams);
                layout.addView(tempLayout);

                //layouts.add(tempLayout);


                TextView tv = new TextView(context);
                tv.setText(excerciseText);
                //tv.setText("chuck");
                tv.setTextSize(18.0f);
                tv.setTextColor(getResources().getColor(R.color.mainText));
                //tv.setBackgroundColor(getResources().getColor(R.color.white));
                tv.setPadding(20, 30, 5, 10);
                tv.setId(excerciseArray.size() + 1);

                tempLayout.addView(tv);
                //TextView tempWeight = new TextView(this);

                for(int i = 0; i < weightText.size(); i = i + 1) {
                    TextView tvWeight = new TextView(context);
                    tvWeight.setText(weightText.get(i));
                    //tv.setText("chuck");
                    tvWeight.setTextSize(18.0f);
                    tvWeight.setTextColor(getResources().getColor(R.color.mainText));
                    //tv.setBackgroundColor(getResources().getColor(R.color.white));
                    tvWeight.setPadding(20, 30, 5, 10);
                    tvWeight.setId(excerciseArray.size() + 1);

                    tempLayout.addView(tvWeight);
                }

                Button start = new Button(context);
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
                start.setId(excerciseArray.size() + 1);
                //start.setPadding(100, 0, 5, 10);

                startArray.add(start);

                tempLayout.addView(start);

                //clearing the weight array.
                weightText.clear();

                //clearing the current superset
                currSuperSet.clear();

            }
        });

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
            tempLayout.setOrientation(LinearLayout.VERTICAL);
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

            TextView tempWeight = new TextView(this);

            String sTemp = "";

            //setting a String that will represent the change the users has made to the weight.
            //if the starting action and the previous action is the same we will assume that the user has made no difference in weight and we will not print the previous weight.
            if(mServer.getStartWeight(id, i) == mServer.getCurrentPreviousWeight(id, i)) {
                sTemp = String.valueOf(mServer.getStartWeight(id, i));
            }
            //else they are not the same so we will print of both values.
            else {
                sTemp = mServer.getStartWeight(id, i) + " -> " + mServer.getCurrentPreviousWeight(id, i);
            }

            tempWeight.setText(sTemp);
            //tv.setText("chuck");
            tempWeight.setTextSize(18.0f);
            tempWeight.setTextColor(getResources().getColor(R.color.mainText));
            //tv.setBackgroundColor(getResources().getColor(R.color.white));
            tempWeight.setPadding(20, 30, 5, 10);
            tempLayout.setId(i);

            weightArray.add(tempWeight);

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
            tempLayout.addView(weightArray.get(i));
            tempLayout.addView(startArray.get(i));

        }

        for (int i = 0; i < startArray.size(); i = i + 1) {

            layouts.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //only going to do the following if the user has indicated that they want to create a superset.
                    if(isSelectingSuperset) {

                        //When a layout is clicked we will change the colour from white to red to inform the user that they have selected a excercise.
                        final int tempSelected = view.getId();
                        ValueAnimator anim = new ValueAnimator();
                        anim.setIntValues(getResources().getColor(R.color.white), getResources().getColor(R.color.red));
                        anim.setEvaluator(new ArgbEvaluator());
                        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                layouts.get(tempSelected).setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
                            }
                        });

                        anim.setDuration(1000);
                        anim.start();

                        //adding the selected ID to the superset actionarray
                        currSuperSet.add(view.getId());
                    }

                }
            });
            startArray.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    setCounter = 0;

                    //the integer that counts the number of reps for the current escersise, this will be used to calculate the rest time.
                    //int reps = 0;

                    System.out.println(startArray.get(view.getId()));
                    //mServer.setWorkouts(view.getId());


                    //the user has clicked on an excercise therefore we can assume that they have started.
                    //isWorkingOut = true;

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
                    time = (TextView) custom.findViewById(R.id.Time);
                    currentExcercise = (TextView)custom.findViewById(R.id.currentExcercise);
                    nextSet = (Button)custom.findViewById(R.id.NextSet);
                    finished = (Button)custom.findViewById(R.id.Finished);
                    counter = (TextView)custom.findViewById(R.id.CurrentSet);
                    start = (Button)custom.findViewById(R.id.StartExcercise);
                    newReps = (EditText) custom.findViewById(R.id.newReps);
                    newWeight = (EditText) custom.findViewById(R.id.newWeight);
                    weightText = (TextView) custom.findViewById(R.id.weightText);
                    repsText = (TextView) custom.findViewById(R.id.repsText);

                    //the weight of this excercise
                    int currentWeight = mServer.getCurrentPreviousWeight(mServer.getSelectedWorkoutID(), selectedExcercise);

                    //getting the weight of the current excercise.
                    String newWeightText = String.valueOf(currentWeight);

                    //setting the value of newWeight to be the previous weight entered by the user.
                    newWeight.setText(newWeightText);

                    //new rep text
                    String newRepText = String.valueOf(reps);

                    //setting the reps text to be whatever number of reps was typed last.
                    newReps.setText(newRepText);

                    //setting the current excercise to be the excercise that we have chosen.
                    currentExcercise.setText(excercises.get(mServer.getSelectedWorkoutID()).get(selectedExcercise) + "test");

                    //setting the counter to be 0 originally.
                    counter.setText("Current set: " + setCounter);

                    custom.setTitle("Excersise");

                    //setting the time to be the minutes and seconds variables.
                    time.setText("Elapsed time: " + minutes + ":" + seconds);

                    /*
                    //on click for the 2 editTexts so we can delete the default message.
                    newWeight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newWeight.setText("");
                        }
                    });

                    newReps.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            newReps.setText("");
                        }
                    });
                    */

                    //when the start button is pressed we want the timer to start.
                    start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isWorkingOut = true;

                            //resetting rest back to a minute to ensure that we recalculate each time from a minute.
                            rest = 60000;

                            baseRest = 1.0f;

                            //getting what was typed in newWeights, and newReps.
                            String sNewReps = newReps.getText().toString();
                            String sNewWeight = newWeight.getText().toString();

                            //checking if the 2 inputs newReps and newWeights are empty.
                            if(!sNewReps.isEmpty() && !sNewWeight.isEmpty()) {
                                //converting the 2 strings to ints
                                int iNewReps = Integer.parseInt(sNewReps);
                                int iNewWeight = Integer.parseInt(sNewWeight);

                                //checking if what they have entered is valid
                                if(iNewReps > 0 && iNewWeight > 0) {
                                    //resetting the text in the 2 edit texts for next time they are visible.
                                    //newReps.setText("Reps:");
                                    //newWeight.setText("Weight:");

                                    //setting the new previous weight to be the new weight entered by the user.
                                    mServer.setPreviousExcerciseWeight(mServer.getSelectedWorkoutID(), selectedExcercise, iNewWeight);

                                    //calculating the time that should be added due to the reps.
                                    double repsChangeRate = iNewReps / 10.0;

                                    //checking if newReps is more than or less than 10
                                    if (iNewReps > 10) {
                                        baseRest = baseRest + repsChangeRate;
                                    } else if(iNewReps < 10) {
                                        baseRest = baseRest - repsChangeRate;
                                    }
                                    //else we are at 10 so add/subtract nothing
                                    else {
                                        baseRest = baseRest + 0.0;
                                    }


                                    //setting the reps to be what was in the newReps edit text.
                                    reps = iNewReps;


                                    //calculating if the new weight is an increase or a decrease.
                                    //if the new weight is more then it's an icnrease.
                                    if(iNewWeight > mServer.getStartWeight(mServer.getSelectedWorkoutID(), selectedExcercise)) {
                                        //getting the percent increase.
                                        double percentIncreaseWeight = (double)iNewWeight / (double)mServer.getStartWeight(mServer.getSelectedWorkoutID(), selectedExcercise);
                                        baseRest = baseRest + percentIncreaseWeight;

                                    } else {
                                        //the percent decrease in weight.
                                        double percentDecreaseWeight = iNewWeight / (double)mServer.getStartWeight(mServer.getSelectedWorkoutID(), selectedExcercise);
                                        //adding this to the base rate for the rest time
                                        baseRest = baseRest - percentDecreaseWeight;
                                    }



                                    counter.setVisibility(View.VISIBLE);
                                    time.setVisibility(View.VISIBLE);
                                    nextSet.setVisibility(View.VISIBLE);
                                    finished.setVisibility(View.VISIBLE);
                                    start.setVisibility(View.INVISIBLE);
                                    newWeight.setVisibility(View.INVISIBLE);
                                    newReps.setVisibility(View.INVISIBLE);
                                    weightText.setVisibility(View.INVISIBLE);
                                    repsText.setVisibility(View.INVISIBLE);
                                }
                            }

                        }
                    });

                    //when the nextset button has been pressed.
                    nextSet.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //string that displays the duration of the previous excercise.
                            String temp = "set " + setCounter + " time was: " + minutes + ":" + seconds;

                            //adding the minutes and seconds to the layout so the user can see them after they have finished the escercise.
                            addText(temp, selectedExcercise);

                            setCounter = setCounter + 1;
                            counter.setText("Current set: " + setCounter);

                            //resetting the minutes and seconds.
                            seconds = 0;
                            minutes = 0;

                            //making everything that's not the time remaining invisible.
                            counter.setVisibility(View.INVISIBLE);
                            nextSet.setVisibility(View.INVISIBLE);
                            finished.setVisibility(View.INVISIBLE);

                            //stop working out as a timer is about to show.
                            isWorkingOut = false;

                            System.out.println("baserest: " + baseRest);

                            //a temporary float which will multiply rest by the baseRest.
                            double tempRest = 60000.0 * baseRest;

                            //calculating the time we must rest based on the value of based rest
                            rest = (int) Math.round(tempRest);

                            System.out.println("rest: " + tempRest);



                            //starting a counter which will determine when the editTexts and the start button will become visible again.
                            new CountDownTimer(rest, 1000) {

                                public void onTick(long millisUntilFinished) {
                                    System.out.println("seconds remaining: " + millisUntilFinished / 1000);
                                    time.setText("Rest time remaining: " + millisUntilFinished / 1000);
                                }

                                public void onFinish() {
                                    System.out.println("done!");
                                    //when the timers finished we want to set the start button and the edit texts back to true.
                                    newWeight.setVisibility(View.VISIBLE);
                                    newReps.setVisibility(View.VISIBLE);
                                    weightText.setVisibility(View.VISIBLE);
                                    repsText.setVisibility(View.VISIBLE);
                                    time.setVisibility(View.INVISIBLE);
                                    start.setVisibility(View.VISIBLE);
                                }
                            }.start();
                        }

                    });

                    //when the finished button is pressed.
                    finished.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            int id = mServer.getSelectedWorkoutID();

                            //the value of the selectedExcercise text before the finished button has been pressed.
                            String excerciseValue = excercises.get(mServer.getSelectedWorkoutID()).get(selectedExcercise).toString();
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

                            //when the finished button is pressed this means the user has finished the specific excercise.
                            isWorkingOut = false;

                            //going to add the times to the layout.


                            custom.dismiss();

                        }
                    });
                    custom.show();


                }
            });
        }

    }

    //a method that adds a string to a specific layout
    public void addText(String textToAdd, int layoutID) {
        TextView tv = new TextView(this);
        //setting the text for this text view to be what was inputted to the method.
        tv.setText(textToAdd);
        tv.setTextSize(18.0f);
        tv.setTextColor(getResources().getColor(R.color.mainText));
        //tv.setBackgroundColor(getResources().getColor(R.color.white));
        tv.setPadding(20, 5, 5, 5);

        //getting the layout from the ID given and adding the textview to this.
        layouts.get(layoutID).addView(tv);
    }

    //timer for every second so we can see if it's past sleep time.
    public void secondCounter()
    {

        //int millis = ((hours*60)+mins)*60000; // Need milliseconds to use Timer
        int millis = 1000;


        //Had to use handler instead of Java timer as this did not allow for the GUI to be updated.
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(isWorkingOut) {
                    seconds = seconds + 1;
                    //if the time is set.
                    if(time != null) {
                        time.setText("Elapsed time: " + minutes + ":" + seconds);
                    }

                    //if we have reacher over 60 seconds then we are going to add a 1 to the minutes and reset the seconds variavle
                    if(seconds > 60) {
                        minutes = minutes + 1;
                        seconds = 0;
                    }
                }

                //resetting the timer.
                resetTimer();

            }

        }, millis);
    }

    //resetting the timer.
    public void resetTimer() {
        secondCounter();
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
