package spartons.com.frisbee.repo;

import io.reactivex.Observable;
import spartons.com.frisbee.collections.DriverCollection;
import spartons.com.frisbee.models.Driver;

import javax.inject.Inject;
import java.util.List;

public class DriverRepo {

    private final DriverCollection driverCollection;

    @Inject
    public DriverRepo(DriverCollection driverCollection) {
        this.driverCollection = driverCollection;
    }

    public boolean insert(Driver driver) {
        return driverCollection.insertDriver(driver);
    }

    public Observable<Boolean> remove(String s) {
        return driverCollection.removeDriver(s);
    }

    public Driver get(String s) {
        return driverCollection.getDriverWithId(s);
    }

    public List<Driver> allItems() {
        return driverCollection.allDriver();
    }
}
