package androidx.window.java.layout;

import android.app.Activity;
import androidx.core.util.a;
import androidx.window.layout.WindowInfoTracker;
import androidx.window.layout.WindowLayoutInfo;
import bk.b;
import cj.a0;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import kotlin.jvm.internal.p;
import kotlinx.coroutines.CoroutineStart;
import xj.e1;
import xj.f;
import xj.h0;
import xj.k1;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class WindowInfoTrackerCallbackAdapter implements WindowInfoTracker {
    private final Map<a<?>, k1> consumerToJobMap;
    private final ReentrantLock lock;
    private final WindowInfoTracker tracker;

    public WindowInfoTrackerCallbackAdapter(WindowInfoTracker windowInfoTracker) {
        p.e(windowInfoTracker, "tracker");
        this.tracker = windowInfoTracker;
        this.lock = new ReentrantLock();
        this.consumerToJobMap = new LinkedHashMap();
    }

    private final <T> void addListener(Executor executor, a<T> aVar, b<? extends T> bVar) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            if (this.consumerToJobMap.get(aVar) == null) {
                this.consumerToJobMap.put(aVar, f.d(h0.a(e1.a(executor)), (fj.f) null, (CoroutineStart) null, new WindowInfoTrackerCallbackAdapter$addListener$1$1(bVar, aVar, null), 3, (Object) null));
            }
            a0 a0Var = a0.a;
        } finally {
            reentrantLock.unlock();
        }
    }

    private final void removeListener(a<?> aVar) {
        ReentrantLock reentrantLock = this.lock;
        reentrantLock.lock();
        try {
            k1 k1Var = this.consumerToJobMap.get(aVar);
            if (k1Var != null) {
                k1.a.a(k1Var, (CancellationException) null, 1, (Object) null);
            }
            this.consumerToJobMap.remove(aVar);
        } finally {
            reentrantLock.unlock();
        }
    }

    public final void addWindowLayoutInfoListener(Activity activity, Executor executor, a<WindowLayoutInfo> aVar) {
        p.e(activity, "activity");
        p.e(executor, "executor");
        p.e(aVar, "consumer");
        addListener(executor, aVar, this.tracker.windowLayoutInfo(activity));
    }

    public final void removeWindowLayoutInfoListener(a<WindowLayoutInfo> aVar) {
        p.e(aVar, "consumer");
        removeListener(aVar);
    }

    @Override // androidx.window.layout.WindowInfoTracker
    public b<WindowLayoutInfo> windowLayoutInfo(Activity activity) {
        p.e(activity, "activity");
        return this.tracker.windowLayoutInfo(activity);
    }
}
