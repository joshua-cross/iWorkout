package com.example.josh.iworkout;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.Calendar;

public class Dates extends Service {

    IBinder mBinder = new LocalBinder();
    ArrayList<String> gymDates = new ArrayList<>();


    @Override
    public void onCreate() {
        System.out.println("Hello. Setting dates.");

        //setting up a tinyDB.
        Context context = Dates.this.getApplicationContext();
        TinyDB tinyDB = new TinyDB(context);
        //if gymdates already has a previous setting, set it to this.
        if(tinyDB.getListString("dates").size() != 0) {
            gymDates = tinyDB.getListString("dates");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public Dates getServerInstance() {
            return Dates.this;
        }
    }

    //setting the dates the user has set for going to the gym.
    public void setGymDates(String date) {
        //boolean that will check if the date is a new date.
        boolean isNew = false;

        //checking if the date is already in the database, if it's not we will not add it.
        for(int i = 0; i < gymDates.size(); i = i + 1) {
            if (gymDates.get(i).equals(date)) {
                isNew = true;
                //exiting for loop.
                break;
            }
        }

        //if the date is not new we will add it to the DB.
        if(!isNew) {
            gymDates.add(date);
            System.out.println("Date: " + date + " has been selected.");
            //updating the dates.
            Context context = Dates.this.getApplicationContext();
            TinyDB tinyDB = new TinyDB(context);
            tinyDB.putListString("dates", gymDates);
        }
    }

    //function that will remove certain dates from the database.
    public void removeDate(String date) {
        //checking the for loop, and removing the date if it exists in the database, if not there is no need to remove it.
        for(int i = 0; i < gymDates.size(); i = i + 1) {
            if(gymDates.get(i).equals(date)) {
                gymDates.remove(i);
                System.out.println("Date: " + date + " has been removed.");
                break;
            }
        } // end for.

        //updating the database.
        Context context = Dates.this.getApplicationContext();
        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putListString("dates", gymDates);

    }

    //getter for gym dates.
    public ArrayList getGymDates() {
        return gymDates;
    }

    //checking if today is in a gym day.
    public boolean isGymDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        String today;

        //converting the day from an integer to a String.
        switch (day){
            case 1:
                today = "Sunday";
                break;

            case 2:
                today = "Monday";
                break;

            case 3:
                today = "Tuesday";
                break;

            case 4:
                today = "Wednesday";
                break;

            case 5:
                today = "Thursday";
                break;

            case 6:
                today = "Friday";
                break;

            case 7:
                today = "Saturday";
                break;

            default:
                today = "not a date...";
                break;

        }

        System.out.println("day of week is: " + today);

        //checking if the string today is equal to one of the days in gymdays.
        for(int i = 0; i < gymDates.size(); i = i + 1) {
            if(today.equals(gymDates.get(i))) {
                System.out.println("today is a gym day.");
                return true;
            }
        }

        //if we got through the for loop without returning we must not be in a gym day.
        return false;
    }
}
