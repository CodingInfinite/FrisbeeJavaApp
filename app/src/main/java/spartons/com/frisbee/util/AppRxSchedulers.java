package spartons.com.frisbee.util;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

public class AppRxSchedulers {

    @Inject
    public AppRxSchedulers() {

    }

    public Scheduler io() {
        return Schedulers.io();
    }

    public Scheduler computation() {
        return Schedulers.computation();
    }

    public Scheduler mainThread() {
        return AndroidSchedulers.mainThread();
    }

    public Scheduler newThread() {
        return Schedulers.newThread();
    }
}
