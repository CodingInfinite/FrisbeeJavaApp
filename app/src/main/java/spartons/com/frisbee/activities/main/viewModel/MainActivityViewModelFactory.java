package spartons.com.frisbee.activities.main.viewModel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;
import com.google.android.gms.location.FusedLocationProviderClient;
import spartons.com.frisbee.util.AppRxSchedulers;
import spartons.com.frisbee.util.UiHelper;

public class MainActivityViewModelFactory implements ViewModelProvider.Factory {

    private final UiHelper uiHelper;
    private final FusedLocationProviderClient locationProviderClient;
    private final AppRxSchedulers appRxSchedulers;

    public MainActivityViewModelFactory(UiHelper uiHelper, FusedLocationProviderClient locationProviderClient, AppRxSchedulers appRxSchedulers) {
        this.uiHelper = uiHelper;
        this.locationProviderClient = locationProviderClient;
        this.appRxSchedulers = appRxSchedulers;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainActivityViewModel(uiHelper, locationProviderClient, appRxSchedulers);
    }
}
