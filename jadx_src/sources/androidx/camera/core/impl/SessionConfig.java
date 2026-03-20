package androidx.camera.core.impl;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.params.InputConfiguration;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.b;
import androidx.camera.core.impl.f;
import androidx.camera.core.p1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SessionConfig {

    /* renamed from: a  reason: collision with root package name */
    private final List<e> f2487a;

    /* renamed from: b  reason: collision with root package name */
    private final List<CameraDevice.StateCallback> f2488b;

    /* renamed from: c  reason: collision with root package name */
    private final List<CameraCaptureSession.StateCallback> f2489c;

    /* renamed from: d  reason: collision with root package name */
    private final List<y.h> f2490d;

    /* renamed from: e  reason: collision with root package name */
    private final List<c> f2491e;

    /* renamed from: f  reason: collision with root package name */
    private final androidx.camera.core.impl.f f2492f;

    /* renamed from: g  reason: collision with root package name */
    private InputConfiguration f2493g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum SessionError {
        SESSION_ERROR_SURFACE_NEEDS_RESET,
        SESSION_ERROR_UNKNOWN
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        final Set<e> f2497a = new LinkedHashSet();

        /* renamed from: b  reason: collision with root package name */
        final f.a f2498b = new f.a();

        /* renamed from: c  reason: collision with root package name */
        final List<CameraDevice.StateCallback> f2499c = new ArrayList();

        /* renamed from: d  reason: collision with root package name */
        final List<CameraCaptureSession.StateCallback> f2500d = new ArrayList();

        /* renamed from: e  reason: collision with root package name */
        final List<c> f2501e = new ArrayList();

        /* renamed from: f  reason: collision with root package name */
        final List<y.h> f2502f = new ArrayList();

        /* renamed from: g  reason: collision with root package name */
        InputConfiguration f2503g;

        a() {
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends a {
        public static b o(v<?> vVar) {
            d J = vVar.J(null);
            if (J != null) {
                b bVar = new b();
                J.a(vVar, bVar);
                return bVar;
            }
            throw new IllegalStateException("Implementation is missing option unpacker for " + vVar.w(vVar.toString()));
        }

        public b a(Collection<CameraDevice.StateCallback> collection) {
            for (CameraDevice.StateCallback stateCallback : collection) {
                e(stateCallback);
            }
            return this;
        }

        public b b(Collection<y.h> collection) {
            this.f2498b.a(collection);
            return this;
        }

        public b c(List<CameraCaptureSession.StateCallback> list) {
            for (CameraCaptureSession.StateCallback stateCallback : list) {
                j(stateCallback);
            }
            return this;
        }

        public b d(y.h hVar) {
            this.f2498b.c(hVar);
            if (!this.f2502f.contains(hVar)) {
                this.f2502f.add(hVar);
            }
            return this;
        }

        public b e(CameraDevice.StateCallback stateCallback) {
            if (this.f2499c.contains(stateCallback)) {
                return this;
            }
            this.f2499c.add(stateCallback);
            return this;
        }

        public b f(c cVar) {
            this.f2501e.add(cVar);
            return this;
        }

        public b g(Config config) {
            this.f2498b.e(config);
            return this;
        }

        public b h(DeferrableSurface deferrableSurface) {
            this.f2497a.add(e.a(deferrableSurface).a());
            return this;
        }

        public b i(y.h hVar) {
            this.f2498b.c(hVar);
            return this;
        }

        public b j(CameraCaptureSession.StateCallback stateCallback) {
            if (this.f2500d.contains(stateCallback)) {
                return this;
            }
            this.f2500d.add(stateCallback);
            return this;
        }

        public b k(DeferrableSurface deferrableSurface) {
            this.f2497a.add(e.a(deferrableSurface).a());
            this.f2498b.f(deferrableSurface);
            return this;
        }

        public b l(String str, Object obj) {
            this.f2498b.g(str, obj);
            return this;
        }

        public SessionConfig m() {
            return new SessionConfig(new ArrayList(this.f2497a), this.f2499c, this.f2500d, this.f2502f, this.f2501e, this.f2498b.h(), this.f2503g);
        }

        public b n() {
            this.f2497a.clear();
            this.f2498b.i();
            return this;
        }

        public List<y.h> p() {
            return Collections.unmodifiableList(this.f2502f);
        }

        public b q(Config config) {
            this.f2498b.o(config);
            return this;
        }

        public b r(InputConfiguration inputConfiguration) {
            this.f2503g = inputConfiguration;
            return this;
        }

        public b s(int i8) {
            this.f2498b.p(i8);
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        void a(SessionConfig sessionConfig, SessionError sessionError);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface d {
        void a(v<?> vVar, b bVar);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class e {

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static abstract class a {
            public abstract e a();

            public abstract a b(String str);

            public abstract a c(List<DeferrableSurface> list);

            public abstract a d(int i8);
        }

        public static a a(DeferrableSurface deferrableSurface) {
            return new b.C0017b().e(deferrableSurface).c(Collections.emptyList()).b(null).d(-1);
        }

        public abstract String b();

        public abstract List<DeferrableSurface> c();

        public abstract DeferrableSurface d();

        public abstract int e();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f extends a {

        /* renamed from: k  reason: collision with root package name */
        private static final List<Integer> f2504k = Arrays.asList(1, 5, 3);

        /* renamed from: h  reason: collision with root package name */
        private final e0.c f2505h = new e0.c();

        /* renamed from: i  reason: collision with root package name */
        private boolean f2506i = true;

        /* renamed from: j  reason: collision with root package name */
        private boolean f2507j = false;

        private List<DeferrableSurface> e() {
            ArrayList arrayList = new ArrayList();
            for (e eVar : this.f2497a) {
                arrayList.add(eVar.d());
                for (DeferrableSurface deferrableSurface : eVar.c()) {
                    arrayList.add(deferrableSurface);
                }
            }
            return arrayList;
        }

        private int g(int i8, int i9) {
            List<Integer> list = f2504k;
            return list.indexOf(Integer.valueOf(i8)) >= list.indexOf(Integer.valueOf(i9)) ? i8 : i9;
        }

        public void a(SessionConfig sessionConfig) {
            androidx.camera.core.impl.f h8 = sessionConfig.h();
            if (h8.g() != -1) {
                this.f2507j = true;
                this.f2498b.p(g(h8.g(), this.f2498b.m()));
            }
            this.f2498b.b(sessionConfig.h().f());
            this.f2499c.addAll(sessionConfig.b());
            this.f2500d.addAll(sessionConfig.i());
            this.f2498b.a(sessionConfig.g());
            this.f2502f.addAll(sessionConfig.j());
            this.f2501e.addAll(sessionConfig.c());
            if (sessionConfig.e() != null) {
                this.f2503g = sessionConfig.e();
            }
            this.f2497a.addAll(sessionConfig.f());
            this.f2498b.l().addAll(h8.e());
            if (!e().containsAll(this.f2498b.l())) {
                p1.a("ValidatingBuilder", "Invalid configuration due to capture request surfaces are not a subset of surfaces");
                this.f2506i = false;
            }
            this.f2498b.e(h8.d());
        }

        public <T> void b(Config.a<T> aVar, T t8) {
            this.f2498b.d(aVar, t8);
        }

        public SessionConfig c() {
            if (this.f2506i) {
                ArrayList arrayList = new ArrayList(this.f2497a);
                this.f2505h.d(arrayList);
                return new SessionConfig(arrayList, this.f2499c, this.f2500d, this.f2502f, this.f2501e, this.f2498b.h(), this.f2503g);
            }
            throw new IllegalArgumentException("Unsupported session configuration combination");
        }

        public void d() {
            this.f2497a.clear();
            this.f2498b.i();
        }

        public boolean f() {
            return this.f2507j && this.f2506i;
        }
    }

    SessionConfig(List<e> list, List<CameraDevice.StateCallback> list2, List<CameraCaptureSession.StateCallback> list3, List<y.h> list4, List<c> list5, androidx.camera.core.impl.f fVar, InputConfiguration inputConfiguration) {
        this.f2487a = list;
        this.f2488b = Collections.unmodifiableList(list2);
        this.f2489c = Collections.unmodifiableList(list3);
        this.f2490d = Collections.unmodifiableList(list4);
        this.f2491e = Collections.unmodifiableList(list5);
        this.f2492f = fVar;
        this.f2493g = inputConfiguration;
    }

    public static SessionConfig a() {
        return new SessionConfig(new ArrayList(), new ArrayList(0), new ArrayList(0), new ArrayList(0), new ArrayList(0), new f.a().h(), null);
    }

    public List<CameraDevice.StateCallback> b() {
        return this.f2488b;
    }

    public List<c> c() {
        return this.f2491e;
    }

    public Config d() {
        return this.f2492f.d();
    }

    public InputConfiguration e() {
        return this.f2493g;
    }

    public List<e> f() {
        return this.f2487a;
    }

    public List<y.h> g() {
        return this.f2492f.b();
    }

    public androidx.camera.core.impl.f h() {
        return this.f2492f;
    }

    public List<CameraCaptureSession.StateCallback> i() {
        return this.f2489c;
    }

    public List<y.h> j() {
        return this.f2490d;
    }

    public List<DeferrableSurface> k() {
        ArrayList arrayList = new ArrayList();
        for (e eVar : this.f2487a) {
            arrayList.add(eVar.d());
            for (DeferrableSurface deferrableSurface : eVar.c()) {
                arrayList.add(deferrableSurface);
            }
        }
        return Collections.unmodifiableList(arrayList);
    }

    public int l() {
        return this.f2492f.g();
    }
}
