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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import javax.inject.Inject;

import it.rainbowbreeze.playtog.R;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.common.MyApp;
import it.rainbowbreeze.playtog.data.AppPrefsManager;


public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private NavigationView mNavigationView;

    @Inject ILogFacility mLogFacility;
    @Inject AppPrefsManager mAppPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MyApp) getApplicationContext()).inject(this);
        mLogFacility.logStartOfActivity(LOG_TAG, this.getClass(), savedInstanceState);

        // Checks if the user is logged
        if (!mAppPrefsManager.isGPlusLoginDone()) {
            mLogFacility.v(LOG_TAG, "User haven't not signed in, Launching Google+ SignIn");
            startActivity(prepareSignInActivityIntent(false));
            finish();
            return;
        }

        setContentView(R.layout.act_main);

        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNavigationView = (NavigationView) findViewById(R.id.main_navigation);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                return onNavigationDrawerItemSelected(menuItem.getItemId());
            }
        });
    }

    public boolean onNavigationDrawerItemSelected(int menuId) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment finalFragment = null;
        Intent intent = null;
        switch (menuId) {
            case R.id.action_play:  // Game fragment
                finalFragment = new StartGameFragment();
                break;
            case R.id.action_stats:
                Toast.makeText(this, "Not implemented yet!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_profile:  // Profile activity
                intent = prepareSignInActivityIntent(true);
                break;
            default:
                break;
        }
        if (null != intent) {
            startActivity(intent);
        } else if (null != finalFragment){
            fragmentManager.beginTransaction()
                    .replace(R.id.main_container, finalFragment)
                    .commit();
        } else {
            return false;
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
        if (id == R.id.action_settings) {
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
