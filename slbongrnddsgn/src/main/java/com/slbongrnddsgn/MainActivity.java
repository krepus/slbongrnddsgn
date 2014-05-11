package com.slbongrnddsgn;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends Activity {

    private InterstitialAd interstitial;

    //design input variables

    private WheelLoadFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getString(R.string.MY_AD_UNIT_ID));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.MY_testdevice))
                .build();

        interstitial.loadAd(adRequest);

        //set defaults
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preference, false);
        setContentView(R.layout.activity_main);

        //avoid overlapping fragment
        if (savedInstanceState != null) {
            return;
        }

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();


        mFragment = new WheelLoadFragment();
        ft.add(R.id.fragmentContainer, mFragment);
        ft.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
        //temp
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.preferences_id:
                // Display the fragment as the menu_main content.

                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.About:

                Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.about_dialog);
                dialog.setTitle("About the app");
                TextView text = (TextView) dialog.findViewById(R.id.about_text);
                text.setText("This app will calculate the point load capacity of a slab on ground" +
                        "in accordance with Concrete Society Report TR34 - Concrete " +
                        "industrial ground floors, 3rd edition");

                // ImageView image = (ImageView) dialog.findViewById(R.id.image);
                // image.setImageResource(R.drawable.icon);
                dialog.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //Log.i(TAG, "main onPause");

        //display interstital
        if (interstitial.isLoaded()) {
            interstitial.show();
            mOnPauseDonateClicked = false;
        }

    }

}
