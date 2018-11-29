package spartons.com.frisbee.application;

import android.app.Application;
import spartons.com.frisbee.di.component.AppComponent;
import spartons.com.frisbee.di.component.DaggerAppComponent;
import spartons.com.frisbee.di.modules.ApplicationContextModule;

public class MyCustomApplication extends Application {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent
                .builder()
                .applicationContextModule(new ApplicationContextModule(this))
                .build();
    }

    public AppComponent appComponent() {
        return appComponent;
    }
}
