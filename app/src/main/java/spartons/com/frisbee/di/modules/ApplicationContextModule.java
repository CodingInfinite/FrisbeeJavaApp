package spartons.com.frisbee.di.modules;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import spartons.com.frisbee.di.scopes.CustomApplicationScope;
import spartons.com.frisbee.di.qualifiers.ApplicationContextQualifier;

@Module
public class ApplicationContextModule {

    private final Context context;

    public ApplicationContextModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @CustomApplicationScope
    @ApplicationContextQualifier
    Context getContext() {
        return context;
    }
}
