package spartons.com.frisbee.collections;

import android.annotation.TargetApi;
import android.os.Build;
import io.reactivex.Observable;
import spartons.com.frisbee.models.Driver;
import spartons.com.frisbee.util.AppRxSchedulers;

import javax.inject.Inject;
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
}

