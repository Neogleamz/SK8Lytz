package androidx.camera.core.impl;

import androidx.camera.core.impl.SessionConfig;
import androidx.camera.core.impl.u;
import androidx.camera.core.p1;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class u {

    /* renamed from: a  reason: collision with root package name */
    private final String f2586a;

    /* renamed from: b  reason: collision with root package name */
    private final Map<String, b> f2587b = new LinkedHashMap();

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        boolean a(b bVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final SessionConfig f2588a;

        /* renamed from: b  reason: collision with root package name */
        private final v<?> f2589b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f2590c = false;

        /* renamed from: d  reason: collision with root package name */
        private boolean f2591d = false;

        b(SessionConfig sessionConfig, v<?> vVar) {
            this.f2588a = sessionConfig;
            this.f2589b = vVar;
        }

        boolean a() {
            return this.f2591d;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public boolean b() {
            return this.f2590c;
        }

        SessionConfig c() {
            return this.f2588a;
        }

        v<?> d() {
            return this.f2589b;
        }

        void e(boolean z4) {
            this.f2591d = z4;
        }

        void f(boolean z4) {
            this.f2590c = z4;
        }
    }

    public u(String str) {
        this.f2586a = str;
    }

    private b i(String str, SessionConfig sessionConfig, v<?> vVar) {
        b bVar = this.f2587b.get(str);
        if (bVar == null) {
            b bVar2 = new b(sessionConfig, vVar);
            this.f2587b.put(str, bVar2);
            return bVar2;
        }
        return bVar;
    }

    private Collection<SessionConfig> j(a aVar) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, b> entry : this.f2587b.entrySet()) {
            if (aVar == null || aVar.a(entry.getValue())) {
                arrayList.add(entry.getValue().c());
            }
        }
        return arrayList;
    }

    private Collection<v<?>> k(a aVar) {
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, b> entry : this.f2587b.entrySet()) {
            if (aVar == null || aVar.a(entry.getValue())) {
                arrayList.add(entry.getValue().d());
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ boolean m(b bVar) {
        return bVar.a() && bVar.b();
    }

    public SessionConfig.f d() {
        SessionConfig.f fVar = new SessionConfig.f();
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, b> entry : this.f2587b.entrySet()) {
            b value = entry.getValue();
            if (value.a() && value.b()) {
                fVar.a(value.c());
                arrayList.add(entry.getKey());
            }
        }
        p1.a("UseCaseAttachState", "Active and attached use case: " + arrayList + " for camera: " + this.f2586a);
        return fVar;
    }

    public Collection<SessionConfig> e() {
        return Collections.unmodifiableCollection(j(new a() { // from class: androidx.camera.core.impl.r
            @Override // androidx.camera.core.impl.u.a
            public final boolean a(u.b bVar) {
                boolean m8;
                m8 = u.m(bVar);
                return m8;
            }
        }));
    }

    public SessionConfig.f f() {
        SessionConfig.f fVar = new SessionConfig.f();
        ArrayList arrayList = new ArrayList();
        for (Map.Entry<String, b> entry : this.f2587b.entrySet()) {
            b value = entry.getValue();
            if (value.b()) {
                fVar.a(value.c());
                arrayList.add(entry.getKey());
            }
        }
        p1.a("UseCaseAttachState", "All use case: " + arrayList + " for camera: " + this.f2586a);
        return fVar;
    }

    public Collection<SessionConfig> g() {
        return Collections.unmodifiableCollection(j(new a() { // from class: androidx.camera.core.impl.s
            @Override // androidx.camera.core.impl.u.a
            public final boolean a(u.b bVar) {
                boolean b9;
                b9 = bVar.b();
                return b9;
            }
        }));
    }

    public Collection<v<?>> h() {
        return Collections.unmodifiableCollection(k(new a() { // from class: androidx.camera.core.impl.t
            @Override // androidx.camera.core.impl.u.a
            public final boolean a(u.b bVar) {
                boolean b9;
                b9 = bVar.b();
                return b9;
            }
        }));
    }

    public boolean l(String str) {
        if (this.f2587b.containsKey(str)) {
            return this.f2587b.get(str).b();
        }
        return false;
    }

    public void p(String str) {
        this.f2587b.remove(str);
    }

    public void q(String str, SessionConfig sessionConfig, v<?> vVar) {
        i(str, sessionConfig, vVar).e(true);
    }

    public void r(String str, SessionConfig sessionConfig, v<?> vVar) {
        i(str, sessionConfig, vVar).f(true);
    }

    public void s(String str) {
        if (this.f2587b.containsKey(str)) {
            b bVar = this.f2587b.get(str);
            bVar.f(false);
            if (bVar.a()) {
                return;
            }
            this.f2587b.remove(str);
        }
    }

    public void t(String str) {
        if (this.f2587b.containsKey(str)) {
            b bVar = this.f2587b.get(str);
            bVar.e(false);
            if (bVar.b()) {
                return;
            }
            this.f2587b.remove(str);
        }
    }

    public void u(String str, SessionConfig sessionConfig, v<?> vVar) {
        if (this.f2587b.containsKey(str)) {
            b bVar = new b(sessionConfig, vVar);
            b bVar2 = this.f2587b.get(str);
            bVar.f(bVar2.b());
            bVar.e(bVar2.a());
            this.f2587b.put(str, bVar);
        }
    }
}
