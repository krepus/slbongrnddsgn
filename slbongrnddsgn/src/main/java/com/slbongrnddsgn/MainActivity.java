package com.slbongrnddsgn;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;

public class MainActivity extends Activity {

    //design input variables
    MyDouble mhf, mKs, mPu, ma, mx, mDB, mScc;
    int mReoLoc;

    private WheelLoadInputFragment mFragment;
    private android.support.v4.app.FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set defaults
        PreferenceManager.setDefaultValues(getApplicationContext(), R.xml.preference, false);


        /**
         Context mCtxt = this;
         FragmentManager fragmentManager = getSupportFragmentManager();
         mTabHost = new android.support.v4.app.FragmentTabHost(mCtxt);
         mTabHost.setup(mCtxt, fragmentManager, R.id.fragment_container);

         // mTabHost.addTab(mTabHost.newTabSpec("design brief").setIndicator("design brief"),
         //         DesignBriefFragment.class, null);
         mTabHost.addTab(mTabHost.newTabSpec("design input").setIndicator("design input"),
         WheelLoadInputFragment.class, null);
         // mTabHost.addTab(mTabHost.newTabSpec("design out").setIndicator("design out"),
         //      DesignOutFragment.class, null);

         //return mTabHost;

         setContentView(mTabHost);

         */

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();


        mFragment = new WheelLoadInputFragment();
        ft.add(R.id.fragmentContainer, mFragment);
        ft.commit();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
        //temp
    }

}
