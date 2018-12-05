package spartons.com.frisbee.di.component;

import dagger.Component;
import spartons.com.frisbee.di.modules.RepositoryModule;
import spartons.com.frisbee.di.modules.UtilModule;
import spartons.com.frisbee.di.scopes.CustomApplicationScope;
import spartons.com.frisbee.repo.DriverRepo;
import spartons.com.frisbee.repo.MarkerRepo;
import spartons.com.frisbee.util.AppRxSchedulers;
import spartons.com.frisbee.util.GoogleMapHelper;
import spartons.com.frisbee.util.UiHelper;

@CustomApplicationScope
@Component(modules = {UtilModule.class, RepositoryModule.class})
public interface AppComponent {

    UiHelper uiHelper();

    AppRxSchedulers appRxSchedulers();

    GoogleMapHelper googleMapHelper();

    DriverRepo driverRepo();

    MarkerRepo markerRepo();
}
