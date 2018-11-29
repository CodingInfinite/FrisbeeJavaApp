package spartons.com.frisbee.di.modules;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import spartons.com.frisbee.di.scopes.CustomApplicationScope;
import spartons.com.frisbee.di.qualifiers.ApplicationContextQualifier;
import spartons.com.frisbee.util.AppRxSchedulers;
import spartons.com.frisbee.util.GoogleMapHelper;
import spartons.com.frisbee.util.UiHelper;

@Module(includes = {ApplicationContextModule.class})
public class UtilModule {

    @Provides
    @CustomApplicationScope
    public UiHelper uiHelper(@ApplicationContextQualifier Context context) {
        return new UiHelper(context);
    }

    @Provides
    @CustomApplicationScope
    public AppRxSchedulers appRxSchedulers() {
        return new AppRxSchedulers();
    }

    @Provides
    @CustomApplicationScope
    public GoogleMapHelper googleMapHelper(@ApplicationContextQualifier Context context) {
        return new GoogleMapHelper(context.getResources());
    }
}
