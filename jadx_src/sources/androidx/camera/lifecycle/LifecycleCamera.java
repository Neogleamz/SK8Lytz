package androidx.camera.lifecycle;

import android.os.Build;
import androidx.camera.core.CameraControl;
import androidx.camera.core.a3;
import androidx.camera.core.internal.CameraUseCaseAdapter;
import androidx.camera.core.m;
import androidx.camera.core.s;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.i;
import androidx.lifecycle.j;
import androidx.lifecycle.r;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class LifecycleCamera implements i, m {

    /* renamed from: b  reason: collision with root package name */
    private final j f2922b;

    /* renamed from: c  reason: collision with root package name */
    private final CameraUseCaseAdapter f2923c;

    /* renamed from: a  reason: collision with root package name */
    private final Object f2921a = new Object();

    /* renamed from: d  reason: collision with root package name */
    private volatile boolean f2924d = false;

    /* renamed from: e  reason: collision with root package name */
    private boolean f2925e = false;

    /* renamed from: f  reason: collision with root package name */
    private boolean f2926f = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LifecycleCamera(j jVar, CameraUseCaseAdapter cameraUseCaseAdapter) {
        this.f2922b = jVar;
        this.f2923c = cameraUseCaseAdapter;
        if (jVar.getLifecycle().b().f(Lifecycle.State.STARTED)) {
            cameraUseCaseAdapter.n();
        } else {
            cameraUseCaseAdapter.v();
        }
        jVar.getLifecycle().a(this);
    }

    @Override // androidx.camera.core.m
    public CameraControl a() {
        return this.f2923c.a();
    }

    @Override // androidx.camera.core.m
    public s b() {
        return this.f2923c.b();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(Collection<a3> collection) {
        synchronized (this.f2921a) {
            this.f2923c.f(collection);
        }
    }

    public void e(androidx.camera.core.impl.d dVar) {
        this.f2923c.e(dVar);
    }

    public CameraUseCaseAdapter f() {
        return this.f2923c;
    }

    public j n() {
        j jVar;
        synchronized (this.f2921a) {
            jVar = this.f2922b;
        }
        return jVar;
    }

    public List<a3> o() {
        List<a3> unmodifiableList;
        synchronized (this.f2921a) {
            unmodifiableList = Collections.unmodifiableList(this.f2923c.z());
        }
        return unmodifiableList;
    }

    @r(Lifecycle.Event.ON_DESTROY)
    public void onDestroy(j jVar) {
        synchronized (this.f2921a) {
            CameraUseCaseAdapter cameraUseCaseAdapter = this.f2923c;
            cameraUseCaseAdapter.H(cameraUseCaseAdapter.z());
        }
    }

    @r(Lifecycle.Event.ON_PAUSE)
    public void onPause(j jVar) {
        if (Build.VERSION.SDK_INT >= 24) {
            this.f2923c.j(false);
        }
    }

    @r(Lifecycle.Event.ON_RESUME)
    public void onResume(j jVar) {
        if (Build.VERSION.SDK_INT >= 24) {
            this.f2923c.j(true);
        }
    }

    @r(Lifecycle.Event.ON_START)
    public void onStart(j jVar) {
        synchronized (this.f2921a) {
            if (!this.f2925e && !this.f2926f) {
                this.f2923c.n();
                this.f2924d = true;
            }
        }
    }

    @r(Lifecycle.Event.ON_STOP)
    public void onStop(j jVar) {
        synchronized (this.f2921a) {
            if (!this.f2925e && !this.f2926f) {
                this.f2923c.v();
                this.f2924d = false;
            }
        }
    }

    public boolean p(a3 a3Var) {
        boolean contains;
        synchronized (this.f2921a) {
            contains = this.f2923c.z().contains(a3Var);
        }
        return contains;
    }

    public void q() {
        synchronized (this.f2921a) {
            if (this.f2925e) {
                return;
            }
            onStop(this.f2922b);
            this.f2925e = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r() {
        synchronized (this.f2921a) {
            CameraUseCaseAdapter cameraUseCaseAdapter = this.f2923c;
            cameraUseCaseAdapter.H(cameraUseCaseAdapter.z());
        }
    }

    public void s() {
        synchronized (this.f2921a) {
            if (this.f2925e) {
                this.f2925e = false;
                if (this.f2922b.getLifecycle().b().f(Lifecycle.State.STARTED)) {
                    onStart(this.f2922b);
                }
            }
        }
    }
}
