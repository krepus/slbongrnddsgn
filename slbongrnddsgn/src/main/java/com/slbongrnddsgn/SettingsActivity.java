package com.slbongrnddsgn;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.EditText;

/**
 * Created by josua.arnigo on 8/07/13.
 */
public class SettingsActivity extends Activity {

    PrefFragment prefFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactlayout);


        prefFragment =  new PrefFragment();
        getFragmentManager().beginTransaction().replace(R.id.fragmentContainer_settings,
              prefFragment ).commit();

    }



}
