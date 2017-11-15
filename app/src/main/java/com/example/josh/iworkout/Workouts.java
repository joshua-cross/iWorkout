package com.example.josh.iworkout;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class Workouts extends Service {

    IBinder mBinder = new LocalBinder();

    //array list which will store the name of each of the workouts.
    ArrayList<String> workouts = new ArrayList<>();


    //Stings which will contain all the names of the excercises for all workouts.
    ArrayList<ArrayList<String>> excercises = new ArrayList<>();
    //boolean list which tracks if the excercise is a body weight excercies or a weighted excercise (true = bodyweight).
    ArrayList<ArrayList<Boolean>> isBodyWeight = new ArrayList<>();
    //integer array which will track the current excercies if it's weighted, if the excercies is body weight this will be set to 0.
    ArrayList<ArrayList<Integer>> excerciseWeight = new ArrayList<>();

    //the names, weights, and isBodyWeight for all the tinyDBs for each workout


    //String for the current workout that has been created.
    String currWorkout;

    //arraylist for the current excercies which is being created by the user.
    ArrayList<String> currExcercises = new ArrayList<>();

    //arraylist for the current body weight which is being created by the user.
    ArrayList<Boolean> currIsBodyWeight = new ArrayList<>();

    //arraylist for the weights of the excercieses in the workouts which is being created by the yusers.
    ArrayList<Integer> currWeights = new ArrayList<>();

    //the workout which was selected in the ViewWorkouts activity.
    int selectedWorkout = 0;

    @Override
    public void onCreate() {

        //setting up a tinyDB.
        Context context = Workouts.this.getApplicationContext();
        TinyDB tinyDB = new TinyDB(context);

        //if the user has submitted a workout.
        if(tinyDB.getListString("workouts").size() != 0) {
            workouts = tinyDB.getListString("workouts");
            //looping through the workouts array.
            for (int i = 0; i < workouts.size(); i = i + 1) {
                //a string which will be what the excersise array for each workout was called (workoutname + Excercises.)
                String sExcercises = workouts.get(i) + "Excercises";
                String sWeights = workouts.get(i) + "Weights";
                String sBodyWeight = workouts.get(i) + "BodyWeight";
                System.out.println(sExcercises + " " + sWeights + " " + sBodyWeight);
                System.out.println("Workout: " + workouts.get(i));
                //if the excercises DB exists then we will add it to the arraylist of arraylists for excercises.
                if(tinyDB.getListString(sExcercises).size() != 0) {
                    excercises.add(tinyDB.getListString(sExcercises));
                }

                //doing the same for body weights and weights.
                if(tinyDB.getListInt(sWeights).size() != 0) {
                    excerciseWeight.add(tinyDB.getListInt(sWeights));
                }

                if(tinyDB.getListBoolean(sBodyWeight).size() != 0) {
                    isBodyWeight.add(tinyDB.getListBoolean(sBodyWeight));
                }
            }

            System.out.println(excercises);
        }

        //tinyDB.putListBoolean();
    }

    //adding a workout.
    public void addWorkout(String workoutName) {
        currWorkout = workoutName;
    }

    //method to check name of current workout.
    public void checkWorkout() {
        System.out.println(currWorkout);
    }

    //adding excercises to the specific workout using dynamic tinyDBs.
    public void addExcercise(String excerciseName) {

        //String for the current workouts excercies so we can save it to a dynamic tinyDB.
        String workout = currWorkout + "Excercises";
        System.out.println(workout);

        //adding to the currentExcercises.
        currExcercises.add(excerciseName);

        //setting up a tinyDB.
        Context context = Workouts.this.getApplicationContext();
        TinyDB tinyDB = new TinyDB(context);

        //adding the currentExcercises
        tinyDB.putListString(workout, currExcercises);

    }

    //adding if each of the excercies is a bodyweight excercise using a dynamic tinyDB
    public void addIsBodyWeight(boolean weighted) {

        //String for the current workouts excercies so we can save it to a dynamic tinyDB.
        String workout = currWorkout + "BodyWeight";
        System.out.println(workout);

        //adding to the isBodyWeight array.
        currIsBodyWeight.add(weighted);

        //setting up a tinyDB.
        Context context = Workouts.this.getApplicationContext();
        TinyDB tinyDB = new TinyDB(context);

        //adding the currentExcercises
        tinyDB.putListString(workout, currExcercises);
    }

    public void addWeight(int uWeight) {
        //String for the current workouts excercies so we can save it to a dynamic tinyDB.
        String workout = currWorkout + "Weight";
        System.out.println(workout);

        //adding to the isBodyWeight array.
        currWeights.add(uWeight);

        //setting up a tinyDB.
        Context context = Workouts.this.getApplicationContext();
        TinyDB tinyDB = new TinyDB(context);

        //adding the currentExcercises
        tinyDB.putListString(workout, currExcercises);
    }


    //when the user presses the finish button we will save the current workout to the array.
    public void finished() {
        //adding to the workouts arrayList.
        workouts.add(currWorkout);
        //setting up a tinyDB.
        Context context = Workouts.this.getApplicationContext();
        TinyDB tinyDB = new TinyDB(context);

        tinyDB.putListString("workouts", workouts);

        //clearing the current excercies when a new workout is pressed; as these are new excercises for a new workout.
        currExcercises.clear();
        currIsBodyWeight.clear();
        currWeights.clear();
    }

    //getter for the workouts.
    public ArrayList getWorkouts() {
        return workouts;
    }

    //getter for the excercies
    public ArrayList getExcercises() {
        return excercises;
    }

    //getter for the weights
    public ArrayList getWeightd() {
        return excerciseWeight;
    }

    //getter for isbodyweight
    public ArrayList getIsBodyWeight() {
        return isBodyWeight;
    }

    //setter for the selectedWorkout
    public void setWorkouts(int workoutID) {
        selectedWorkout = workoutID;
    }

    //getter for the selected workout.
    public String getSelectedWorkout() {
        return workouts.get(selectedWorkout);
    }

    //getter for the selecred workout id.
    public int getSelectedWorkoutID() {
        System.out.println("SelectedWorkoutID: " + selectedWorkout);
        return selectedWorkout;
    }



    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public Workouts getServerInstance() {
            return Workouts.this;
        }
    }
}
