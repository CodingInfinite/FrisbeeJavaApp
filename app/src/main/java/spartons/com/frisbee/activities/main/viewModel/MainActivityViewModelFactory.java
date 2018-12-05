package spartons.com.frisbee.activities.main.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import com.google.android.gms.location.FusedLocationProviderClient;
import spartons.com.frisbee.repo.DriverRepo;
import spartons.com.frisbee.repo.MarkerRepo;
import spartons.com.frisbee.util.AppRxSchedulers;
import spartons.com.frisbee.util.GoogleMapHelper;
import spartons.com.frisbee.util.UiHelper;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {

    private final UiHelper uiHelper;
    private final FusedLocationProviderClient locationProviderClient;
    private final AppRxSchedulers appRxSchedulers;
    private final DriverRepo driverRepo;
    private final MarkerRepo markerRepo;
    private final GoogleMapHelper googleMapHelper;

    public MainActivityViewModelFactory(UiHelper uiHelper, FusedLocationProviderClient locationProviderClient, AppRxSchedulers appRxSchedulers, DriverRepo driverRepo, MarkerRepo markerRepo, GoogleMapHelper googleMapHelper) {
        this.uiHelper = uiHelper;
        this.locationProviderClient = locationProviderClient;
        this.appRxSchedulers = appRxSchedulers;
        this.driverRepo = driverRepo;
        this.markerRepo = markerRepo;
        this.googleMapHelper = googleMapHelper;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(uiHelper, locationProviderClient, driverRepo, markerRepo, appRxSchedulers, googleMapHelper);
    }
}
