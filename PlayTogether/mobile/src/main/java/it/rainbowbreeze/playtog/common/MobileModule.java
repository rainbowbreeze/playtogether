package it.rainbowbreeze.playtog.common;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger modules for classes that don't need an Application context
 * Created by alfredomorresi on 31/10/14.
 */
@Module (
        complete = true,
        library = true
)
public class MobileModule {
    private final Context mAppContent;

    public MobileModule(Context appContent) {
        mAppContent = appContent;
    }

    @Provides @Singleton public ILogFacility provideLogFacility () {
        return new LogFacility();
    }
}
