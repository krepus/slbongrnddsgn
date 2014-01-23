package com.slbongrnddsgn;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.EditText;

/**
 * Created by josua.arnigo on 8/07/13.
 */
public class SettingsActivity extends PreferenceActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);


        //setup edittext filters
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                if (source instanceof SpannableStringBuilder) {
                    SpannableStringBuilder sourceAsSpannableBuilder = (SpannableStringBuilder) source;
                    for (int i = end - 1; i >= start; i--) {
                        char currentChar = source.charAt(i);
                        if (!Character.isDigit(currentChar) && !Character.isSpaceChar(currentChar)) {
                            sourceAsSpannableBuilder.delete(i, i + 1);
                        }
                    }
                    return source;
                } else {
                    StringBuilder filteredStringBuilder = new StringBuilder();
                    for (int i = 0; i < end; i++) {
                        char currentChar = source.charAt(i);
                        if (Character.isDigit(currentChar) || Character.isSpaceChar(currentChar)) {
                            filteredStringBuilder.append(currentChar);
                        }
                    }
                    return filteredStringBuilder.toString();
                }
            }
        };

        EditText editText1 = ((EditTextPreference) findPreference(getString(R.string.BARLIST))).getEditText();
        editText1.setFilters(new InputFilter[]{inputFilter});


        //
        //set preferences default values
        //PreferenceManager.setDefaultValues(this, R.xml.preference, false);


        //set summaries
        SharedPreferences sp = getPreferenceScreen().getSharedPreferences();
        Preference preference = findPreference(getString(R.string.BARLIST));
        preference.setSummary(sp.getString(getString(R.string.BARLIST), ""));

        preference = findPreference(getString(R.string.COVER));
        preference.setSummary(sp.getString(getString(R.string.COVER), ""));

        preference = findPreference(getString(R.string.FC));
        preference.setSummary(sp.getString(getString(R.string.FC), ""));

        preference = findPreference(getString(R.string.FYMAIN));
        preference.setSummary(sp.getString(getString(R.string.FYMAIN), ""));


    }

    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {

        //set summary only if

        if (!key.equals(getString(R.string.BARLIST_RESET))) {
            Preference preference = findPreference(key);
            preference.setSummary(sp.getString(key, ""));
        }

        //update some key variables when ff happens

        if (key.equals(getString(R.string.BARLIST))) {
            SharedPreferences.Editor ed = sp.edit();
            ed.putString(getString(R.string.BARLIST_RESET), "true");
            ed.commit();
        }
        /*if (key.equals(getString(R.string.UNIT))) {
            SharedPreferences.Editor ed = sp.edit();
            ed.putString(getString(R.string.UNIT_CHANGED), "true");
            ed.commit();
        }*/


    }


    @Override
    protected void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }
}
