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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        if(getUsername().equals("none")){
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            this.finish();
            return;
        }

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
        String username = getUsername();
        if(username != "none"){
            menuItem.setTitle("Signed in as: " + username);
        }
        return true;
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


    public String getUsername(){
        SharedPreferences sharedPreferences = HomeScreen.this.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = "none";
        String resourceUsername = getResources().getString(R.string.pref_key_username);

        String value = sharedPreferences.getString(resourceUsername, defaultValue);

        return value;
    }

    public void setUsername(String username){
        SharedPreferences sharedPreferences = HomeScreen.this.getPreferences(Context.MODE_PRIVATE);

        String resourceUsername = getResources().getString(R.string.pref_key_username);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(resourceUsername, username);
        editor.commit();

    }

    public void deleteUsername(){
        SharedPreferences sharedPreferences = HomeScreen.this.getPreferences(Context.MODE_PRIVATE);

        String resourceUsername = getResources().getString(R.string.pref_key_username);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.remove(resourceUsername);
        editor.commit();

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
}
