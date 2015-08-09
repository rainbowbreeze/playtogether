/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
   -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

package it.rainbowbreeze.playtog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import javax.inject.Inject;

import it.rainbowbreeze.playtog.R;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.common.MyApp;
import it.rainbowbreeze.playtog.data.AppPrefsManager;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private View mContent;

    @Inject ILogFacility mLogFacility;
    @Inject AppPrefsManager mAppPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplicationContext()).inject(this);
        mLogFacility.logStartOfActivity(LOG_TAG, this.getClass(), savedInstanceState);

        /*
        // Checks if the user is logged
        if (!mAppPrefsManager.isGPlusLoginDone()) {
            mLogFacility.v(LOG_TAG, "User haven't not signed in, Launching Google+ SignIn");
            startActivity(prepareSignInActivityIntent(false));
            finish();
            return;
        }
        */

        setContentView(R.layout.act_main);

        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_launcher);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.main_navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                return onNavigationDrawerItemSelected(menuItem);
            }
        });

        // Useful for Snackbar calls
        mContent = findViewById(R.id.content);

        // Simulated the selection of the first menu voice
        MenuItem menuItem = mNavigationView.getMenu().findItem(R.id.action_play);
        onNavigationDrawerItemSelected(menuItem);

        // See ActionBarDrawerToggle as per code at
        //  http://www.android4devs.com/2015/06/navigation-view-material-design-support.html
    }

    public boolean onNavigationDrawerItemSelected(MenuItem menuItem) {
        final String TAG_START_GAME = "StartGame";
        int menuId = menuItem.getItemId();
        boolean checkItem;

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment finalFragment = null;
        String finalFragmentTAG = null;
        Intent intent = null;
        checkItem = false;
        switch (menuId) {
            case R.id.action_play:  // Game fragment
                finalFragmentTAG = TAG_START_GAME;
                // Code to save memory, instead of recreating a new Fragment each time
                finalFragment = fragmentManager.findFragmentByTag(TAG_START_GAME);
                if (null == finalFragment) {
                    mLogFacility.v(LOG_TAG, "Creating new instance of StartGameFragment");
                    finalFragment = new StartGameFragment();
                }
                checkItem = true;
                break;
            case R.id.action_stats:
                Snackbar.make(mContent, "Stats aren't implemented yet!", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_profile:  // Profile activity
                intent = prepareSignInActivityIntent(true);
                break;
            case R.id.action_settings:
                Snackbar.make(mContent, "Settings aren't implemented yet!", Snackbar.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        mDrawerLayout.closeDrawers();
        if(checkItem) menuItem.setChecked(true);

        if (null != intent) {
            startActivity(intent);
        } else if (null != finalFragment){
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, finalFragment, finalFragmentTAG)
                    .commit();
        } else {
            return false;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Intent prepareSignInActivityIntent(boolean fromNavigation) {
        Intent intent = new Intent(this, PlusSignInActivity.class);
        intent.putExtra(PlusSignInActivity.EXTRA_LAUNCHNEWACTIVITY, !fromNavigation);
        return intent;
    }
}
