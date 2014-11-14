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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class HomeScreen extends ActionBarActivity implements HomeScreenAdapter.HomeScreenItemClickListener{
    WifiChangeReceiver wifiChangeReceiver = new WifiChangeReceiver();
    private String receivedUsername;
    public Button hello_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);


        this.receivedUsername = getIntent().getStringExtra(LoginActivity.EXTRA_LOGIN_USERNAME);

        ListView listView = (ListView)findViewById(R.id.home_screen_listing);
        HomeScreenAdapter homeScreenAdapter = new HomeScreenAdapter(this, HomeScreen.this, null);
        listView.setAdapter(homeScreenAdapter);

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(wifiChangeReceiver, filter);
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


    public void onStart(){
        super.onStart();
        if(hello_button != null){
            hello_button.setText(receivedUsername);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_events:
                Intent intent = new Intent(getApplicationContext(), EventListActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStop(){
        super.onStop();
        unregisterReceiver(wifiChangeReceiver);
    }

    @Override
    public void onHomeScreenItemClick(View v, int resid) {
        switch(resid){
            case R.id.hello_button:
                String[] placeInfo = WifiChangeReceiver.getMacAddress(this, null);

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
                        String[] placeInfo = WifiChangeReceiver.getMacAddress(getApplicationContext(), null);
                        ((Button)v).setText(placeInfo[4] + "has " + placeInfo[2]);
                    }
                });
                break;
        }

    }

}
