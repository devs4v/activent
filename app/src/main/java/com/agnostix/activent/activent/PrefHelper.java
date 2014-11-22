package com.agnostix.activent.activent;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shivam on 11/22/2014.
 */
public class PrefHelper {
    //Preference file
    private static final String PREF_FILE_KEY = "agnostix.activent.activent.shared_pref_file";

    //Keys for different settings
    public static final String PREF_USERNAME = "agnostix.activent.activent.signed_in_username";


    Context context;
    SharedPreferences preferences;
    SharedPreferences.Editor prefEditor;

    public PrefHelper(Context context){
        this.context = context;
        this.preferences = context.getSharedPreferences(PREF_FILE_KEY, Context.MODE_PRIVATE);
        this.prefEditor = preferences.edit();
    }

    public String getValueOrDefault(String key, String defaultValue){
        return preferences.getString(key, defaultValue);
    }

    public void putValueFor(String key, String value){
        prefEditor.putString(key, value)
                .commit();
    }

    public void clearValueFor(String key){
        prefEditor.remove(key)
                .commit();
    }
}
