package androidx.camera.lifecycle;

import a0.f;
import android.content.Context;
import androidx.camera.core.a3;
import androidx.camera.core.g3;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.internal.CameraUseCaseAdapter;
import androidx.camera.core.m;
import androidx.camera.core.o;
import androidx.camera.core.r;
import androidx.camera.core.t;
import androidx.camera.core.x;
import androidx.camera.core.y;
import androidx.concurrent.futures.c;
import androidx.core.util.h;
import androidx.lifecycle.j;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import y.c0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: h  reason: collision with root package name */
    private static final e f2939h = new e();

    /* renamed from: c  reason: collision with root package name */
    private com.google.common.util.concurrent.d<x> f2942c;

    /* renamed from: f  reason: collision with root package name */
    private x f2945f;

    /* renamed from: g  reason: collision with root package name */
    private Context f2946g;

    /* renamed from: a  reason: collision with root package name */
    private final Object f2940a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private y.b f2941b = null;

    /* renamed from: d  reason: collision with root package name */
    private com.google.common.util.concurrent.d<Void> f2943d = f.h(null);

    /* renamed from: e  reason: collision with root package name */
    private final LifecycleCameraRepository f2944e = new LifecycleCameraRepository();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements a0.c<Void> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ c.a f2947a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ x f2948b;

        a(c.a aVar, x xVar) {
            this.f2947a = aVar;
            this.f2948b = xVar;
        }

        @Override // a0.c
        /* renamed from: a */
        public void c(Void r22) {
            this.f2947a.c(this.f2948b);
        }

        @Override // a0.c
        public void onFailure(Throwable th) {
            this.f2947a.f(th);
        }
    }

    private e() {
    }

    public static com.google.common.util.concurrent.d<e> f(final Context context) {
        h.h(context);
        return f.o(f2939h.g(context), new n.a() { // from class: androidx.camera.lifecycle.d
            @Override // n.a
            public final Object apply(Object obj) {
                e h8;
                h8 = e.h(context, (x) obj);
                return h8;
            }
        }, z.a.a());
    }

    private com.google.common.util.concurrent.d<x> g(Context context) {
        synchronized (this.f2940a) {
            com.google.common.util.concurrent.d<x> dVar = this.f2942c;
            if (dVar != null) {
                return dVar;
            }
            final x xVar = new x(context, this.f2941b);
            com.google.common.util.concurrent.d<x> a9 = androidx.concurrent.futures.c.a(new c.InterfaceC0024c() { // from class: androidx.camera.lifecycle.c
                @Override // androidx.concurrent.futures.c.InterfaceC0024c
                public final Object a(c.a aVar) {
                    Object j8;
                    j8 = e.this.j(xVar, aVar);
                    return j8;
                }
            });
            this.f2942c = a9;
            return a9;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ e h(Context context, x xVar) {
        e eVar = f2939h;
        eVar.k(xVar);
        eVar.l(androidx.camera.core.impl.utils.e.a(context));
        return eVar;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ Object j(final x xVar, c.a aVar) {
        synchronized (this.f2940a) {
            f.b(a0.d.a(this.f2943d).f(new a0.a() { // from class: androidx.camera.lifecycle.b
                @Override // a0.a
                public final com.google.common.util.concurrent.d apply(Object obj) {
                    com.google.common.util.concurrent.d h8;
                    Void r22 = (Void) obj;
                    h8 = x.this.h();
                    return h8;
                }
            }, z.a.a()), new a(aVar, xVar), z.a.a());
        }
        return "ProcessCameraProvider-initializeCameraX";
    }

    private void k(x xVar) {
        this.f2945f = xVar;
    }

    private void l(Context context) {
        this.f2946g = context;
    }

    m d(j jVar, t tVar, g3 g3Var, List<o> list, a3... a3VarArr) {
        androidx.camera.core.impl.d dVar;
        androidx.camera.core.impl.d a9;
        androidx.camera.core.impl.utils.m.a();
        t.a c9 = t.a.c(tVar);
        int length = a3VarArr.length;
        int i8 = 0;
        while (true) {
            dVar = null;
            if (i8 >= length) {
                break;
            }
            t H = a3VarArr[i8].g().H(null);
            if (H != null) {
                Iterator<r> it = H.c().iterator();
                while (it.hasNext()) {
                    c9.a(it.next());
                }
            }
            i8++;
        }
        LinkedHashSet<CameraInternal> a10 = c9.b().a(this.f2945f.e().a());
        if (a10.isEmpty()) {
            throw new IllegalArgumentException("Provided camera selector unable to resolve a camera for the given use case");
        }
        LifecycleCamera c10 = this.f2944e.c(jVar, CameraUseCaseAdapter.w(a10));
        Collection<LifecycleCamera> e8 = this.f2944e.e();
        for (a3 a3Var : a3VarArr) {
            for (LifecycleCamera lifecycleCamera : e8) {
                if (lifecycleCamera.p(a3Var) && lifecycleCamera != c10) {
                    throw new IllegalStateException(String.format("Use case %s already bound to a different lifecycle.", a3Var));
                }
            }
        }
        if (c10 == null) {
            c10 = this.f2944e.b(jVar, new CameraUseCaseAdapter(a10, this.f2945f.d(), this.f2945f.g()));
        }
        Iterator<r> it2 = tVar.c().iterator();
        while (it2.hasNext()) {
            r next = it2.next();
            if (next.a() != r.f2788a && (a9 = c0.a(next.a()).a(c10.b(), this.f2946g)) != null) {
                if (dVar != null) {
                    throw new IllegalArgumentException("Cannot apply multiple extended camera configs at the same time.");
                }
                dVar = a9;
            }
        }
        c10.e(dVar);
        if (a3VarArr.length == 0) {
            return c10;
        }
        this.f2944e.a(c10, g3Var, list, Arrays.asList(a3VarArr));
        return c10;
    }

    public m e(j jVar, t tVar, a3... a3VarArr) {
        return d(jVar, tVar, null, Collections.emptyList(), a3VarArr);
    }

    public void m() {
        androidx.camera.core.impl.utils.m.a();
        this.f2944e.k();
    }
}
