package spartons.com.frisbee.collections;

import android.annotation.TargetApi;
import android.os.Build;
import io.reactivex.Observable;
import spartons.com.frisbee.models.Driver;
import spartons.com.frisbee.util.AppRxSchedulers;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DriverCollection {

    private final AppRxSchedulers appRxSchedulers;

    private List<Driver> driverList = new CopyOnWriteArrayList<>();

    @Inject
    public DriverCollection(AppRxSchedulers appRxSchedulers) {
        this.appRxSchedulers = appRxSchedulers;
    }

    public boolean insertDriver(Driver driver) {
        return driverList.add(driver);
    }

    private Observable<Boolean> removeDriver(Driver driver) {
        return Observable.fromIterable(driverList)
                .subscribeOn(appRxSchedulers.computation())
                .filter(driver1 -> driver.getId().equals(driver1.getId()))
                .map(driver1 -> driverList.remove(driver1))
                .observeOn(appRxSchedulers.mainThread());
    }

    public Observable<Boolean> removeDriver(String id) {
        Driver driver = getDriverWithId(id);
        if (driver != null)
            return removeDriver(driver);
        return Observable.just(false);
    }

    public List<Driver> allDriver() {
        return driverList;
    }

    public Driver getDriverWithId(String id) {
        Driver driver1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            driver1 = getStreamAbleDriver(id);
        else
            driver1 = getSynchronousDriver(id);
        return driver1;
    }

    private Driver getSynchronousDriver(String driverId) {
        for (Driver driver : driverList)
            if (driverId.equals(driver.getId()))
                return driver;
        return null;
    }

    @TargetApi(Build.VERSION_CODES.N)
    private Driver getStreamAbleDriver(String driverId) {
        try {
            return driverList
                    .stream()
                    .filter(driver -> driver.getId().equals(driverId))
                    .collect(toSingleton());
        } catch (Exception __) {
            return null;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private <T> Collector<T, ?, T> toSingleton() {
        return Collectors.collectingAndThen(
                Collectors.toList(),
                list -> {
                    if (list.size() != 1)
                        throw new IllegalStateException("No item found");
                    return list.get(0);
                }
        );
    }

    public Driver getNearestDriver(double lat, double lng) {
        List<Driver> filteredDriverList = sortDriversByDistance(lat, lng);
        if (filteredDriverList.size() == 0)
            return null;
        return filteredDriverList.get(0);
    }

    private List<Driver> sortDriversByDistance(final double curLat, final double curLng) {
        List<Driver> tempDrivers = new ArrayList<>(driverList);
        Collections.sort(tempDrivers, (driver1, driver2) -> {
            double distance1 = measureDriverDistanceInMeters(driver1.lat, driver1.lng, curLat, curLng);
            double distance2 = measureDriverDistanceInMeters(driver2.lat, driver2.lng, curLat, curLng);
            return Double.compare(distance1, distance2);
        });
        return tempDrivers;
    }

    private double measureDriverDistanceInMeters(double driverLatitude, double driverLongitude, double currentLatitude, double currentLongitude) {
        return 1000.0 * (6371.0 * Math.acos(Math.cos(Math.toRadians(currentLatitude)) * Math.cos(Math.toRadians(driverLatitude)) * Math.cos(Math.toRadians(driverLongitude) - Math.toRadians(currentLongitude)) + Math.sin(Math.toRadians(currentLatitude)) * Math.sin(Math.toRadians(driverLatitude))));
    }
}
