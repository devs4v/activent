package com.agnostix.activent.activent;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class HomeScreen extends ActionBarActivity implements HomeScreenAdapter.HomeScreenItemClickListener{

    public static final String EXTRA_SIGN_OUT = "activent.login.sign.out";
Alarm a;
    WifiChangeReceiver wifiChangeReceiver = new WifiChangeReceiver();
    private String receivedUsername;
    private Button hello_button;
    private View tile_sign_in;
    private View tile_agenda;
    private View tile_places;
    private View tile_info;

    protected void onDestroy(){
        unregisterReceiver(a);
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
a=new Alarm();
        this.registerReceiver(a,new IntentFilter("android.intent.action.TIME_TICK"));
a.SetAlarm(getApplicationContext());


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);

        PrefHelper prefHelper = new PrefHelper(this);
        this.receivedUsername = prefHelper.getValueOrDefault(PrefHelper.PREF_USERNAME, "none");

        ListView listView = (ListView)findViewById(R.id.home_screen_listing);
        HomeScreenAdapter homeScreenAdapter = new HomeScreenAdapter(this, HomeScreen.this, null);
        listView.setAdapter(homeScreenAdapter);

        /*IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.wifi.supplicant.STATE_CHANGE");
        registerReceiver(wifiChangeReceiver, filter);*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);

        MenuItem menuItem = menu.findItem(R.id.action_events);
        //if(username != "none"){
            menuItem.setTitle("Signed in as: " + receivedUsername);
        //}
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_events:
                intent = new Intent(getApplicationContext(), EventListActivity.class);
                startActivity(intent);
                break;
            case R.id.action_sign:
                intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra(EXTRA_SIGN_OUT, "sign_out");
                startActivity(intent);
                this.finish();
                break;
            case R.id.action_emails:
                intent = new Intent(getApplicationContext(), Mails.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStop(){
        super.onStop();
        try{
            unregisterReceiver(wifiChangeReceiver);
        }catch(Exception e){
            Log.d("HomeScreen", "No Receiver Registered!");
        }
    }

    @Override
    public void onHomeScreenItemClick(View v, int resid) {
        switch(resid){
            case R.id.hello_button:
                String[] placeInfo = WifiChangeReceiver.getMacAddress(this);

                ((Button)v).setText(placeInfo[4] + "has " + placeInfo[2]);
                //Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                //startActivity(intent);
                break;
        }
    }

    @Override
    public void setUIObjects(View v, int objectToBeAdded) {
        switch(objectToBeAdded){
            case R.id.hello_button:
                this.hello_button = (Button)v.findViewById(R.id.hello_button);
                this.hello_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] placeInfo = WifiChangeReceiver.getMacAddress(getApplicationContext());
                        ((Button)v).setText(placeInfo[4] + "has " + placeInfo[2]);
                    }
                });
                break;
            case R.id.home_screen_tile_agenda:
                this.tile_agenda = v.findViewById(R.id.home_screen_tile_agenda);
                this.tile_agenda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.home_screen_tile_sign_in:
                this.tile_sign_in = v.findViewById(R.id.home_screen_tile_sign_in);
                this.tile_sign_in.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                        //startActivity(intent);
                        Toast.makeText(getApplicationContext(), "You are already signed in!", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.home_screen_tile_places:
                this.tile_places = v.findViewById(R.id.home_screen_tile_places);
                this.tile_places.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), PlacesActivity.class);
                        startActivity(intent);
                    }
                });
                break;
            case R.id.home_screen_tile_info:
                this.tile_agenda = v.findViewById(R.id.home_screen_tile_info);
                this.tile_agenda.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "Created by the Agnostix Team.\nThank you for using!", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }

    }

}
