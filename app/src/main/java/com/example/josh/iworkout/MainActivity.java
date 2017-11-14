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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    //variables needed to connect to the dates service.
    boolean mBounded;
    Dates mServer;

    //the check boxes from the website.
    CheckBox days[] = new CheckBox[7];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        ActionBar app_bar = getSupportActionBar();
        app_bar.setDisplayShowTitleEnabled(false);

        // the confirm button from the layout.
        Button confirm = (Button) findViewById(R.id.confirm);

        days[0] = (CheckBox) findViewById(R.id.Monday);
        days[1] = (CheckBox) findViewById(R.id.Tuesday);
        days[2] = (CheckBox) findViewById(R.id.Wednesday);
        days[3] = (CheckBox) findViewById(R.id.Thursday);
        days[4] = (CheckBox) findViewById(R.id.Friday);
        days[5] = (CheckBox) findViewById(R.id.Saturday);
        days[6] = (CheckBox) findViewById(R.id.Sunday);

        //when the confirm button is pressed.
        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //making sure at least 1 day a week has been selected, so we can print an error message if one is not.
                if(days[0].isChecked() || days[1].isChecked() || days[2].isChecked() ||
                        days[3].isChecked() || days[4].isChecked() ||
                        days[5].isChecked() || days[6].isChecked()) {

                    //Checking which days have been checked by the user; if the dates are not selected then they will be removed from the array.
                    if (days[0].isChecked()) {
                        mServer.setGymDates("Monday");
                    } else {
                        mServer.removeDate("Monday");
                    }
                    if (days[1].isChecked()) {
                        mServer.setGymDates("Tuesday");
                    } else {
                        mServer.removeDate("Tuesday");
                    }
                    if (days[2].isChecked()) {
                        mServer.setGymDates("Wednesday");
                    } else {
                        mServer.removeDate("Wednesday");
                    }
                    if (days[3].isChecked()) {
                        mServer.setGymDates("Thursday");
                    } else {
                        mServer.removeDate("Thursday");
                    }
                    if (days[4].isChecked()) {
                        mServer.setGymDates("Friday");
                    } else {
                        mServer.removeDate("Friday");
                    }
                    if (days[5].isChecked()) {
                        mServer.setGymDates("Saturday");
                    } else {
                        mServer.removeDate("Saturday");
                    }
                    if (days[6].isChecked()) {
                        mServer.setGymDates("Sunday");
                    } else {
                        mServer.removeDate("Sunday");
                    }

                    boolean gymDay = mServer.isGymDay();
                }
                //else nothing has been selected.
                else {
                    //making text view give an error message.
                    TextView error = (TextView) findViewById(R.id.error);
                    error.setText("Please select atleast 1 day.");
                }
            }
        });

    }


    //connecting to the Dates service.

    @Override
    protected void onStart() {
        super.onStart();
        Intent mIntent = new Intent(this, Dates.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
    };

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Toast.makeText(MainActivity.this, "Service is disconnected", 1000).show();
            mBounded = false;
            mServer = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Toast.makeText(MainActivity.this, "Service is connected", 1000).show();
            mBounded = true;
            Dates.LocalBinder mLocalBinder = (Dates.LocalBinder)service;
            mServer = mLocalBinder.getServerInstance();


            Context context = MainActivity.this.getApplicationContext();
            //Context context = MainActivity.this.getApplicationContext();
            TinyDB tinydb = new TinyDB(context);

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
