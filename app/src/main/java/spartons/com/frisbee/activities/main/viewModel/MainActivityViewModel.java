package spartons.com.frisbee.activities.main.viewModel;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Looper;
import android.support.v4.util.Pair;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import spartons.com.frisbee.lsitener.FirebaseObjectValueListener;
import spartons.com.frisbee.lsitener.LatLngInterpolator;
import spartons.com.frisbee.models.Driver;
import spartons.com.frisbee.repo.DriverRepo;
import spartons.com.frisbee.repo.MarkerRepo;
import spartons.com.frisbee.util.*;

import java.util.List;

public class MainActivityViewModel extends ViewModel implements FirebaseObjectValueListener {

    private static final String ONLINE_DRIVERS = "online_drivers";
    private static final String TAG = MainActivityViewModel.class.getSimpleName();

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(ONLINE_DRIVERS);
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MediatorLiveData<String> _reverseGeocodeResult = new MediatorLiveData<>();
    private MediatorLiveData<Location> _currentLocation = new MediatorLiveData<>();
    private MediatorLiveData<Pair<String, MarkerOptions>> _addNewMarker = new MediatorLiveData<>();

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
    private final DriverRepo driverRepo;
    private final GoogleMapHelper googleMapHelper;
    private final MarkerRepo markerRepo;
    private final AppRxSchedulers appRxSchedulers;
    private FirebaseValueEventListenerHelper valueEventListener;

    public LiveData<String> reverseGeocodeResult = _reverseGeocodeResult;
    public LiveData<Location> currentLocation = _currentLocation;
    public LiveData<Pair<String, MarkerOptions>> addNewMarker = _addNewMarker;

    public MainActivityViewModel(UiHelper uiHelper, FusedLocationProviderClient locationProviderClient, DriverRepo driverRepo, MarkerRepo markerRepo, AppRxSchedulers appRxSchedulers, GoogleMapHelper googleMapHelper) {
        this.uiHelper = uiHelper;
        this.locationProviderClient = locationProviderClient;
        this.driverRepo = driverRepo;
        this.markerRepo = markerRepo;
        this.appRxSchedulers = appRxSchedulers;
        this.googleMapHelper = googleMapHelper;
        valueEventListener = new FirebaseValueEventListenerHelper(this);
        databaseReference.addChildEventListener(valueEventListener);
    }

    @SuppressLint("MissingPermission")
    public void requestLocationUpdates() {
        locationProviderClient.requestLocationUpdates(uiHelper.getLocationRequest(), locationCallback, Looper.myLooper());
    }

    @Override
    public void onDriverOnline(Driver driver) {
        if (driverRepo.insert(driver)) {
            MarkerOptions markerOptions = googleMapHelper.getDriverMarkerOptions(new LatLng(driver.lat, driver.lng), driver.angle);
            _addNewMarker.setValue(new Pair<>(driver.getId(), markerOptions));
        }
    }

    public void insertDriverMarker(String key, Marker marker) {
        markerRepo.insert(key, marker);
    }

    @Override
    public void onDriverChanged(Driver driver) {
        Driver fetchedDriver = driverRepo.get(driver.getId());
        if (fetchedDriver == null) return;
        fetchedDriver.update(driver.lat, driver.lng, driver.angle);
        Marker marker = markerRepo.get(fetchedDriver.getId());
        if (marker == null) return;
        marker.setRotation(fetchedDriver.angle + 90);
        MarkerAnimationHelper.animateMarkerToGB(marker, new LatLng(fetchedDriver.lat, fetchedDriver.lng), new LatLngInterpolator.Spherical());
    }

    @Override
    public void onDriverOffline(Driver driver) {
        compositeDisposable.add(driverRepo.remove(driver.getId())
                .subscribe(b -> {
                    if (b)
                        markerRepo.remove(driver.getId());
                }, Throwable::printStackTrace));
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
        databaseReference.removeEventListener(valueEventListener);
        databaseReference = null;
        compositeDisposable = null;
        locationCallback = null;
    }
}