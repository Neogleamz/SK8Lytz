package androidx.camera.lifecycle;

import androidx.camera.core.a3;
import androidx.camera.core.g3;
import androidx.camera.core.internal.CameraUseCaseAdapter;
import androidx.camera.core.o;
import androidx.core.util.h;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.i;
import androidx.lifecycle.j;
import androidx.lifecycle.r;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class LifecycleCameraRepository {

    /* renamed from: a  reason: collision with root package name */
    private final Object f2927a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private final Map<a, LifecycleCamera> f2928b = new HashMap();

    /* renamed from: c  reason: collision with root package name */
    private final Map<LifecycleCameraRepositoryObserver, Set<a>> f2929c = new HashMap();

    /* renamed from: d  reason: collision with root package name */
    private final ArrayDeque<j> f2930d = new ArrayDeque<>();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LifecycleCameraRepositoryObserver implements i {

        /* renamed from: a  reason: collision with root package name */
        private final LifecycleCameraRepository f2931a;

        /* renamed from: b  reason: collision with root package name */
        private final j f2932b;

        LifecycleCameraRepositoryObserver(j jVar, LifecycleCameraRepository lifecycleCameraRepository) {
            this.f2932b = jVar;
            this.f2931a = lifecycleCameraRepository;
        }

        j a() {
            return this.f2932b;
        }

        @r(Lifecycle.Event.ON_DESTROY)
        public void onDestroy(j jVar) {
            this.f2931a.l(jVar);
        }

        @r(Lifecycle.Event.ON_START)
        public void onStart(j jVar) {
            this.f2931a.h(jVar);
        }

        @r(Lifecycle.Event.ON_STOP)
        public void onStop(j jVar) {
            this.f2931a.i(jVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        static a a(j jVar, CameraUseCaseAdapter.a aVar) {
            return new androidx.camera.lifecycle.a(jVar, aVar);
        }

        public abstract CameraUseCaseAdapter.a b();

        public abstract j c();
    }

    private LifecycleCameraRepositoryObserver d(j jVar) {
        synchronized (this.f2927a) {
            for (LifecycleCameraRepositoryObserver lifecycleCameraRepositoryObserver : this.f2929c.keySet()) {
                if (jVar.equals(lifecycleCameraRepositoryObserver.a())) {
                    return lifecycleCameraRepositoryObserver;
                }
            }
            return null;
        }
    }

    private boolean f(j jVar) {
        synchronized (this.f2927a) {
            LifecycleCameraRepositoryObserver d8 = d(jVar);
            if (d8 == null) {
                return false;
            }
            for (a aVar : this.f2929c.get(d8)) {
                if (!((LifecycleCamera) h.h(this.f2928b.get(aVar))).o().isEmpty()) {
                    return true;
                }
            }
            return false;
        }
    }

    private void g(LifecycleCamera lifecycleCamera) {
        synchronized (this.f2927a) {
            j n8 = lifecycleCamera.n();
            a a9 = a.a(n8, lifecycleCamera.f().x());
            LifecycleCameraRepositoryObserver d8 = d(n8);
            Set<a> hashSet = d8 != null ? this.f2929c.get(d8) : new HashSet<>();
            hashSet.add(a9);
            this.f2928b.put(a9, lifecycleCamera);
            if (d8 == null) {
                LifecycleCameraRepositoryObserver lifecycleCameraRepositoryObserver = new LifecycleCameraRepositoryObserver(n8, this);
                this.f2929c.put(lifecycleCameraRepositoryObserver, hashSet);
                n8.getLifecycle().a(lifecycleCameraRepositoryObserver);
            }
        }
    }

    private void j(j jVar) {
        synchronized (this.f2927a) {
            LifecycleCameraRepositoryObserver d8 = d(jVar);
            if (d8 == null) {
                return;
            }
            for (a aVar : this.f2929c.get(d8)) {
                ((LifecycleCamera) h.h(this.f2928b.get(aVar))).q();
            }
        }
    }

    private void m(j jVar) {
        synchronized (this.f2927a) {
            for (a aVar : this.f2929c.get(d(jVar))) {
                LifecycleCamera lifecycleCamera = this.f2928b.get(aVar);
                if (!((LifecycleCamera) h.h(lifecycleCamera)).o().isEmpty()) {
                    lifecycleCamera.s();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(LifecycleCamera lifecycleCamera, g3 g3Var, List<o> list, Collection<a3> collection) {
        synchronized (this.f2927a) {
            h.a(!collection.isEmpty());
            j n8 = lifecycleCamera.n();
            for (a aVar : this.f2929c.get(d(n8))) {
                LifecycleCamera lifecycleCamera2 = (LifecycleCamera) h.h(this.f2928b.get(aVar));
                if (!lifecycleCamera2.equals(lifecycleCamera) && !lifecycleCamera2.o().isEmpty()) {
                    throw new IllegalArgumentException("Multiple LifecycleCameras with use cases are registered to the same LifecycleOwner.");
                }
            }
            try {
                lifecycleCamera.f().K(g3Var);
                lifecycleCamera.f().J(list);
                lifecycleCamera.d(collection);
                if (n8.getLifecycle().b().f(Lifecycle.State.STARTED)) {
                    h(n8);
                }
            } catch (CameraUseCaseAdapter.CameraException e8) {
                throw new IllegalArgumentException(e8.getMessage());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LifecycleCamera b(j jVar, CameraUseCaseAdapter cameraUseCaseAdapter) {
        LifecycleCamera lifecycleCamera;
        synchronized (this.f2927a) {
            h.b(this.f2928b.get(a.a(jVar, cameraUseCaseAdapter.x())) == null, "LifecycleCamera already exists for the given LifecycleOwner and set of cameras");
            if (jVar.getLifecycle().b() == Lifecycle.State.DESTROYED) {
                throw new IllegalArgumentException("Trying to create LifecycleCamera with destroyed lifecycle.");
            }
            lifecycleCamera = new LifecycleCamera(jVar, cameraUseCaseAdapter);
            if (cameraUseCaseAdapter.z().isEmpty()) {
                lifecycleCamera.q();
            }
            g(lifecycleCamera);
        }
        return lifecycleCamera;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LifecycleCamera c(j jVar, CameraUseCaseAdapter.a aVar) {
        LifecycleCamera lifecycleCamera;
        synchronized (this.f2927a) {
            lifecycleCamera = this.f2928b.get(a.a(jVar, aVar));
        }
        return lifecycleCamera;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Collection<LifecycleCamera> e() {
        Collection<LifecycleCamera> unmodifiableCollection;
        synchronized (this.f2927a) {
            unmodifiableCollection = Collections.unmodifiableCollection(this.f2928b.values());
        }
        return unmodifiableCollection;
    }

    void h(j jVar) {
        ArrayDeque<j> arrayDeque;
        synchronized (this.f2927a) {
            if (f(jVar)) {
                if (!this.f2930d.isEmpty()) {
                    j peek = this.f2930d.peek();
                    if (!jVar.equals(peek)) {
                        j(peek);
                        this.f2930d.remove(jVar);
                        arrayDeque = this.f2930d;
                    }
                    m(jVar);
                }
                arrayDeque = this.f2930d;
                arrayDeque.push(jVar);
                m(jVar);
            }
        }
    }

    void i(j jVar) {
        synchronized (this.f2927a) {
            this.f2930d.remove(jVar);
            j(jVar);
            if (!this.f2930d.isEmpty()) {
                m(this.f2930d.peek());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k() {
        synchronized (this.f2927a) {
            for (a aVar : this.f2928b.keySet()) {
                LifecycleCamera lifecycleCamera = this.f2928b.get(aVar);
                lifecycleCamera.r();
                i(lifecycleCamera.n());
            }
        }
    }

    void l(j jVar) {
        synchronized (this.f2927a) {
            LifecycleCameraRepositoryObserver d8 = d(jVar);
            if (d8 == null) {
                return;
            }
            i(jVar);
            for (a aVar : this.f2929c.get(d8)) {
                this.f2928b.remove(aVar);
            }
            this.f2929c.remove(d8);
            d8.a().getLifecycle().c(d8);
        }
    }
}
