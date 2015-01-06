package it.rainbowbreeze.playtog.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import it.rainbowbreeze.playtog.R;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.common.MyApp;
import it.rainbowbreeze.playtog.data.AppPrefsManager;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

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

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //mNavigationDrawerFragment.selectItem(2);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment finalFragment = null;
        Intent intent = null;
        switch (position) {
            case 0:  // Game fragment
                finalFragment = new StartGameFragment();
                break;
            case 2:  // Profile activity
                intent = prepareSignInActivityIntent(true);
                break;
            default:
                finalFragment = PlaceholderFragment.newInstance(position + 1);
        }
        if (null != intent) {
            startActivity(intent);
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, finalFragment)
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        String[] sectionTitles = getResources().getStringArray(R.array.navigation_arrSections);
        mTitle = sectionTitles[number - 1];  // number is 1-based, while array index is 0-based
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
