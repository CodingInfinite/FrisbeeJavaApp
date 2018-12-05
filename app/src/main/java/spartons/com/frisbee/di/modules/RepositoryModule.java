package spartons.com.frisbee.di.modules;

import dagger.Module;
import dagger.Provides;
import spartons.com.frisbee.collections.DriverCollection;
import spartons.com.frisbee.collections.MarkerCollection;
import spartons.com.frisbee.di.scopes.CustomApplicationScope;
import spartons.com.frisbee.repo.DriverRepo;
import spartons.com.frisbee.repo.MarkerRepo;
import spartons.com.frisbee.util.AppRxSchedulers;

@Module(includes = {UtilModule.class})
public class RepositoryModule {

    @Provides
    @CustomApplicationScope
    public DriverCollection driverCollection(AppRxSchedulers appRxSchedulers) {
        return new DriverCollection(appRxSchedulers);
    }

    @Provides
    @CustomApplicationScope
    public MarkerCollection markerCollection() {
        return new MarkerCollection();
    }

    @Provides
    @CustomApplicationScope
    public DriverRepo driverRepo(DriverCollection driverCollection) {
        return new DriverRepo(driverCollection);
    }

    public MarkerRepo markerRepo(MarkerCollection markerCollection) {
        return new MarkerRepo(markerCollection);
    }
}
