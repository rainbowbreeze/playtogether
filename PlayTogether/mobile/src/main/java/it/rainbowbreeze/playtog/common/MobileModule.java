package it.rainbowbreeze.playtog.common;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.playtog.data.AppPrefsManager;
import it.rainbowbreeze.playtog.data.PlayerDao;
import it.rainbowbreeze.playtog.logic.MainThreadBus;
import it.rainbowbreeze.playtog.logic.MatchManager;
import it.rainbowbreeze.playtog.ui.MainActivity;
import it.rainbowbreeze.playtog.ui.PlusSignInActivity;
import it.rainbowbreeze.playtog.ui.StartGameFragment;

/**
 * Dagger modules for classes that don't need an Application context
 * Created by alfredomorresi on 31/10/14.
 */
@Module (
        injects = {
                MyApp.class,
                MainActivity.class,
                PlusSignInActivity.class,
                StartGameFragment.class,
        },
        // Forces validates modules and injections at compile time.
        // If true, includes also additional modules that will complete the dependency graph
        //  using the includes statement for the class included in the injects statement
        complete = true,
        // True because it declares @Provides not used inside the class, but outside.
        // Once the code is finished, it should be possible to set to false and have
        //  all the consuming classes in the injects statement
        library = true
)
public class MobileModule {
    private final Context mAppContent;

    public MobileModule(Context appContent) {
        mAppContent = appContent;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton @ForApplication public Context provideApplicationContext () {
        return mAppContent;
    }

    @Provides @Singleton public ILogFacility provideLogFacility () {
        return new LogFacility();
    }

    @Provides @Singleton
    public AppPrefsManager provideAppPrefsManager(
            @ForApplication Context appContext,
            ILogFacility logFacility) {
        return new AppPrefsManager(appContext, logFacility);
    }

    @Provides @Singleton
    public MatchManager provideMatchManager(
            ILogFacility logFacility,
            PlayerDao playerDao,
            AppPrefsManager appPrefsManager,
            Bus bus) {
        return new MatchManager(logFacility, playerDao, appPrefsManager, bus);
    }

    @Provides @Singleton
    public Bus provideBus() {
        return new MainThreadBus();
    }

    @Provides @Singleton
    public PlayerDao providePlayerDao(
            @ForApplication Context appContext,
            ILogFacility logFacility) {
        return new PlayerDao(appContext, logFacility);
    }

}
