package it.rainbowbreeze.playtog.common;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.playtog.data.AppPrefsManager;
import it.rainbowbreeze.playtog.data.PlayerDao;
import it.rainbowbreeze.playtog.logic.BackendHelper;
import it.rainbowbreeze.playtog.logic.GPlusCommunicationService;
import it.rainbowbreeze.playtog.logic.GPlusHelper;
import it.rainbowbreeze.playtog.logic.GameManager;
import it.rainbowbreeze.playtog.logic.GcmIntentService;
import it.rainbowbreeze.playtog.logic.MainThreadBus;
import it.rainbowbreeze.playtog.logic.actions.ActionsManager;
import it.rainbowbreeze.playtog.ui.JoinGameActivity;
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
                JoinGameActivity.class,
                MainActivity.class,
                PlusSignInActivity.class,
                StartGameFragment.class,
                GcmIntentService.class,
                GPlusCommunicationService.class,
        },
        // Forces validates modules and injections at compile time.
        // If true, includes also additional modules that will complete the dependency graph
        //  using the includes statement for the class included in the injects statement
        complete = true,
        // OLD comment: True because it declares @Provides not used inside the class, but outside.
        // Once the code is finished, it should be possible to set to false and have
        //  all the consuming classes in the injects statement
        library = false
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
    public GameManager provideGameManager(
            ILogFacility logFacility,
            PlayerDao playerDao,
            AppPrefsManager appPrefsManager,
            BackendHelper backendHelper,
            Bus bus) {
        return new GameManager(logFacility, playerDao, appPrefsManager, backendHelper, bus);
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

    @Provides @Singleton
    public ActionsManager provideActionsManager(
            @ForApplication Context appContext,
            ILogFacility logFacility,
            BackendHelper backendHelper,
            GameManager gameManager) {
        return new ActionsManager(appContext, logFacility, backendHelper, gameManager);
    }

    @Provides @Singleton
    public GPlusHelper provideGPlusHelper(ILogFacility logFacility) {
        return new GPlusHelper(logFacility);
    }

    @Provides @Singleton
    public BackendHelper provideBackendHelper(
            @ForApplication Context appContext,
            ILogFacility logFacility,
            AppPrefsManager appPrefsManager) {
        return new BackendHelper(appContext, logFacility, appPrefsManager);
    }

}
