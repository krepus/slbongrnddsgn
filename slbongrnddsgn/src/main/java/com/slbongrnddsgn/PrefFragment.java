package com.slbongrnddsgn;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.widget.EditText;

/**
 * Created by josua.arnigo on 24/01/14.
 */
public class PrefFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
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

        preference = findPreference(getString(R.string.PHI_C));
        preference.setSummary(sp.getString(getString(R.string.PHI_C), ""));

        preference = findPreference(getString(R.string.PHI_S));
        preference.setSummary(sp.getString(getString(R.string.PHI_S), ""));
    }

    public void onSharedPreferenceChanged(SharedPreferences sp, String key) {


        //set summaries
        Preference preference = findPreference(key);
        preference.setSummary(sp.getString(key, ""));


    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }


}


