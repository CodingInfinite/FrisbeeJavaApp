package spartons.com.frisbee.activities.main.viewModel;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import spartons.com.frisbee.util.AppRxSchedulers;
import spartons.com.frisbee.util.UiHelper;

import java.util.List;

public class MainActivityViewModel extends ViewModel {

    private static final String TAG = MainActivityViewModel.class.getSimpleName();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MediatorLiveData<String> _reverseGeocodeResult = new MediatorLiveData<>();
    private MediatorLiveData<Location> _currentLocation = new MediatorLiveData<>();

    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult.getLastLocation() == null) return;
            _currentLocation.postValue(locationResult.getLastLocation());
        }
    };

    private final UiHelper uiHelper;
    private final FusedLocationProviderClient locationProviderClient;
    private final AppRxSchedulers appRxSchedulers;

    public LiveData<String> reverseGeocodeResult = _reverseGeocodeResult;
    public LiveData<Location> currentLocation = _currentLocation;

    public MainActivityViewModel(UiHelper uiHelper, FusedLocationProviderClient locationProviderClient, AppRxSchedulers appRxSchedulers) {
        this.uiHelper = uiHelper;
        this.locationProviderClient = locationProviderClient;
        this.appRxSchedulers = appRxSchedulers;
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates() {
        locationProviderClient.requestLocationUpdates(uiHelper.getLocationRequest(), locationCallback, Looper.myLooper());
    }

    public void makeReverseGeocodeRequest(LatLng latLng, Geocoder geocoder) {
        compositeDisposable.add(Observable.<String>create(emitter -> {
            try {
                List<Address> result = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                if (result != null && result.size() > 0) {
                    Address address = result.get(0);
                    emitter.onNext(address.getAddressLine(0).concat(" , ").concat(address.getLocality()));
                }
            } catch (Exception e) {
                emitter.onError(e);
            } finally {
                emitter.onComplete();
            }
        }).subscribeOn(appRxSchedulers.io())
                .subscribe(placeName -> _reverseGeocodeResult.postValue(placeName), Throwable::printStackTrace));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
        locationProviderClient.removeLocationUpdates(locationCallback);
        compositeDisposable = null;
        locationCallback = null;
    }
}
