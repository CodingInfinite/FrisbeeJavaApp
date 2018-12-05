package spartons.com.frisbee.lsitener;

import spartons.com.frisbee.models.Driver;

public interface FirebaseObjectValueListener {
    void onDriverOnline(Driver driver);

    void onDriverChanged(Driver driver);

    void onDriverOffline(Driver driver);
}
