package androidx.window.layout;

import android.annotation.SuppressLint;
import android.app.Activity;
import androidx.window.extensions.layout.WindowLayoutComponent;
import cj.a0;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ExtensionWindowLayoutInfoBackend implements WindowBackend {
    private final Map<Activity, MulticastConsumer> activityToListeners;
    private final WindowLayoutComponent component;
    private final ReentrantLock extensionWindowBackendLock;
    private final Map<androidx.core.util.a<WindowLayoutInfo>, Activity> listenerToActivity;

    @SuppressLint({"NewApi"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class MulticastConsumer implements Consumer<androidx.window.extensions.layout.WindowLayoutInfo> {
        private final Activity activity;
        private WindowLayoutInfo lastKnownValue;
        private final ReentrantLock multicastConsumerLock;
        private final Set<androidx.core.util.a<WindowLayoutInfo>> registeredListeners;

        public MulticastConsumer(Activity activity) {
            p.e(activity, "activity");
            this.activity = activity;
            this.multicastConsumerLock = new ReentrantLock();
            this.registeredListeners = new LinkedHashSet();
        }

        @Override // java.util.function.Consumer
        public void accept(androidx.window.extensions.layout.WindowLayoutInfo windowLayoutInfo) {
            p.e(windowLayoutInfo, "value");
            ReentrantLock reentrantLock = this.multicastConsumerLock;
            reentrantLock.lock();
            try {
                this.lastKnownValue = ExtensionsWindowLayoutInfoAdapter.INSTANCE.translate$window_release(this.activity, windowLayoutInfo);
                Iterator<T> it = this.registeredListeners.iterator();
                while (it.hasNext()) {
                    ((androidx.core.util.a) it.next()).accept(this.lastKnownValue);
                }
                a0 a0Var = a0.a;
            } finally {
                reentrantLock.unlock();
            }
        }

        public final void addListener(androidx.core.util.a<WindowLayoutInfo> aVar) {
            p.e(aVar, "listener");
            ReentrantLock reentrantLock = this.multicastConsumerLock;
            reentrantLock.lock();
            try {
                WindowLayoutInfo windowLayoutInfo = this.lastKnownValue;
                if (windowLayoutInfo != null) {
                    aVar.accept(windowLayoutInfo);
                }
                this.registeredListeners.add(aVar);
            } finally {
                reentrantLock.unlock();
            }
        }

        public final boolean isEmpty() {
            return this.registeredListeners.isEmpty();
        }

        public final void removeListener(androidx.core.util.a<WindowLayoutInfo> aVar) {
            p.e(aVar, "listener");
            ReentrantLock reentrantLock = this.multicastConsumerLock;
            reentrantLock.lock();
            try {
                this.registeredListeners.remove(aVar);
            } finally {
                reentrantLock.unlock();
            }
        }
    }

    public ExtensionWindowLayoutInfoBackend(WindowLayoutComponent windowLayoutComponent) {
        p.e(windowLayoutComponent, "component");
        this.component = windowLayoutComponent;
        this.extensionWindowBackendLock = new ReentrantLock();
        this.activityToListeners = new LinkedHashMap();
        this.listenerToActivity = new LinkedHashMap();
    }

    @Override // androidx.window.layout.WindowBackend
    public void registerLayoutChangeCallback(Activity activity, Executor executor, androidx.core.util.a<WindowLayoutInfo> aVar) {
        a0 a0Var;
        p.e(activity, "activity");
        p.e(executor, "executor");
        p.e(aVar, "callback");
        ReentrantLock reentrantLock = this.extensionWindowBackendLock;
        reentrantLock.lock();
        try {
            MulticastConsumer multicastConsumer = this.activityToListeners.get(activity);
            if (multicastConsumer == null) {
                a0Var = null;
            } else {
                multicastConsumer.addListener(aVar);
                this.listenerToActivity.put(aVar, activity);
                a0Var = a0.a;
            }
            if (a0Var == null) {
                MulticastConsumer multicastConsumer2 = new MulticastConsumer(activity);
                this.activityToListeners.put(activity, multicastConsumer2);
                this.listenerToActivity.put(aVar, activity);
                multicastConsumer2.addListener(aVar);
                this.component.addWindowLayoutInfoListener(activity, multicastConsumer2);
            }
            a0 a0Var2 = a0.a;
        } finally {
            reentrantLock.unlock();
        }
    }

    @Override // androidx.window.layout.WindowBackend
    public void unregisterLayoutChangeCallback(androidx.core.util.a<WindowLayoutInfo> aVar) {
        p.e(aVar, "callback");
        ReentrantLock reentrantLock = this.extensionWindowBackendLock;
        reentrantLock.lock();
        try {
            Activity activity = this.listenerToActivity.get(aVar);
            if (activity == null) {
                return;
            }
            MulticastConsumer multicastConsumer = this.activityToListeners.get(activity);
            if (multicastConsumer == null) {
                return;
            }
            multicastConsumer.removeListener(aVar);
            if (multicastConsumer.isEmpty()) {
                this.component.removeWindowLayoutInfoListener(multicastConsumer);
            }
            a0 a0Var = a0.a;
        } finally {
            reentrantLock.unlock();
        }
    }
}
