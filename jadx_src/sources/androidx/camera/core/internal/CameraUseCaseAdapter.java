package androidx.camera.core.internal;

import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.util.Size;
import android.view.Surface;
import androidx.camera.core.CameraControl;
import androidx.camera.core.a3;
import androidx.camera.core.e1;
import androidx.camera.core.g3;
import androidx.camera.core.impl.CameraControlInternal;
import androidx.camera.core.impl.CameraInternal;
import androidx.camera.core.impl.Config;
import androidx.camera.core.impl.UseCaseConfigFactory;
import androidx.camera.core.impl.d;
import androidx.camera.core.impl.v;
import androidx.camera.core.internal.CameraUseCaseAdapter;
import androidx.camera.core.m;
import androidx.camera.core.p1;
import androidx.camera.core.q2;
import androidx.camera.core.s;
import androidx.camera.core.y1;
import androidx.camera.core.z2;
import androidx.core.util.h;
import b0.k;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import y.n;
import y.o;
import y.q;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class CameraUseCaseAdapter implements m {

    /* renamed from: a  reason: collision with root package name */
    private CameraInternal f2666a;

    /* renamed from: b  reason: collision with root package name */
    private final LinkedHashSet<CameraInternal> f2667b;

    /* renamed from: c  reason: collision with root package name */
    private final o f2668c;

    /* renamed from: d  reason: collision with root package name */
    private final UseCaseConfigFactory f2669d;

    /* renamed from: e  reason: collision with root package name */
    private final a f2670e;

    /* renamed from: g  reason: collision with root package name */
    private g3 f2672g;

    /* renamed from: f  reason: collision with root package name */
    private final List<a3> f2671f = new ArrayList();

    /* renamed from: h  reason: collision with root package name */
    private List<androidx.camera.core.o> f2673h = Collections.emptyList();

    /* renamed from: j  reason: collision with root package name */
    private d f2674j = n.a();

    /* renamed from: k  reason: collision with root package name */
    private final Object f2675k = new Object();

    /* renamed from: l  reason: collision with root package name */
    private boolean f2676l = true;

    /* renamed from: m  reason: collision with root package name */
    private Config f2677m = null;

    /* renamed from: n  reason: collision with root package name */
    private List<a3> f2678n = new ArrayList();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class CameraException extends Exception {
        public CameraException() {
        }

        public CameraException(String str) {
            super(str);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final List<String> f2679a = new ArrayList();

        a(LinkedHashSet<CameraInternal> linkedHashSet) {
            Iterator<CameraInternal> it = linkedHashSet.iterator();
            while (it.hasNext()) {
                this.f2679a.add(it.next().m().c());
            }
        }

        public boolean equals(Object obj) {
            if (obj instanceof a) {
                return this.f2679a.equals(((a) obj).f2679a);
            }
            return false;
        }

        public int hashCode() {
            return this.f2679a.hashCode() * 53;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        v<?> f2680a;

        /* renamed from: b  reason: collision with root package name */
        v<?> f2681b;

        b(v<?> vVar, v<?> vVar2) {
            this.f2680a = vVar;
            this.f2681b = vVar2;
        }
    }

    public CameraUseCaseAdapter(LinkedHashSet<CameraInternal> linkedHashSet, o oVar, UseCaseConfigFactory useCaseConfigFactory) {
        this.f2666a = linkedHashSet.iterator().next();
        LinkedHashSet<CameraInternal> linkedHashSet2 = new LinkedHashSet<>(linkedHashSet);
        this.f2667b = linkedHashSet2;
        this.f2670e = new a(linkedHashSet2);
        this.f2668c = oVar;
        this.f2669d = useCaseConfigFactory;
    }

    private boolean A() {
        boolean z4;
        synchronized (this.f2675k) {
            z4 = true;
            if (this.f2674j.x() != 1) {
                z4 = false;
            }
        }
        return z4;
    }

    private boolean B(List<a3> list) {
        boolean z4 = false;
        boolean z8 = false;
        for (a3 a3Var : list) {
            if (E(a3Var)) {
                z4 = true;
            } else if (D(a3Var)) {
                z8 = true;
            }
        }
        return z4 && !z8;
    }

    private boolean C(List<a3> list) {
        boolean z4 = false;
        boolean z8 = false;
        for (a3 a3Var : list) {
            if (E(a3Var)) {
                z8 = true;
            } else if (D(a3Var)) {
                z4 = true;
            }
        }
        return z4 && !z8;
    }

    private boolean D(a3 a3Var) {
        return a3Var instanceof e1;
    }

    private boolean E(a3 a3Var) {
        return a3Var instanceof y1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void F(Surface surface, SurfaceTexture surfaceTexture, z2.f fVar) {
        surface.release();
        surfaceTexture.release();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ void G(z2 z2Var) {
        final SurfaceTexture surfaceTexture = new SurfaceTexture(0);
        surfaceTexture.setDefaultBufferSize(z2Var.l().getWidth(), z2Var.l().getHeight());
        surfaceTexture.detachFromGLContext();
        final Surface surface = new Surface(surfaceTexture);
        z2Var.v(surface, z.a.a(), new androidx.core.util.a() { // from class: b0.e
            @Override // androidx.core.util.a
            public final void accept(Object obj) {
                CameraUseCaseAdapter.F(surface, surfaceTexture, (z2.f) obj);
            }
        });
    }

    private void I() {
        synchronized (this.f2675k) {
            if (this.f2677m != null) {
                this.f2666a.h().d(this.f2677m);
            }
        }
    }

    static void L(List<androidx.camera.core.o> list, Collection<a3> collection) {
        HashMap hashMap = new HashMap();
        for (androidx.camera.core.o oVar : list) {
            hashMap.put(Integer.valueOf(oVar.c()), oVar);
        }
        for (a3 a3Var : collection) {
            if (a3Var instanceof y1) {
                y1 y1Var = (y1) a3Var;
                androidx.camera.core.o oVar2 = (androidx.camera.core.o) hashMap.get(1);
                if (oVar2 == null) {
                    y1Var.X(null);
                } else {
                    q2 b9 = oVar2.b();
                    Objects.requireNonNull(b9);
                    y1Var.X(new g0.v(b9, oVar2.a()));
                }
            }
        }
    }

    private void M(Map<a3, Size> map, Collection<a3> collection) {
        synchronized (this.f2675k) {
            if (this.f2672g != null) {
                Integer a9 = this.f2666a.m().a();
                boolean z4 = true;
                if (a9 == null) {
                    p1.k("CameraUseCaseAdapter", "The lens facing is null, probably an external.");
                } else if (a9.intValue() != 0) {
                    z4 = false;
                }
                Map<a3, Rect> a10 = k.a(this.f2666a.h().f(), z4, this.f2672g.a(), this.f2666a.m().g(this.f2672g.c()), this.f2672g.d(), this.f2672g.b(), map);
                for (a3 a3Var : collection) {
                    a3Var.J((Rect) h.h(a10.get(a3Var)));
                    a3Var.I(q(this.f2666a.h().f(), map.get(a3Var)));
                }
            }
        }
    }

    private void o() {
        synchronized (this.f2675k) {
            CameraControlInternal h8 = this.f2666a.h();
            this.f2677m = h8.i();
            h8.j();
        }
    }

    private List<a3> p(List<a3> list, List<a3> list2) {
        ArrayList arrayList = new ArrayList(list2);
        boolean C = C(list);
        boolean B = B(list);
        a3 a3Var = null;
        a3 a3Var2 = null;
        for (a3 a3Var3 : list2) {
            if (E(a3Var3)) {
                a3Var = a3Var3;
            } else if (D(a3Var3)) {
                a3Var2 = a3Var3;
            }
        }
        if (C && a3Var == null) {
            arrayList.add(t());
        } else if (!C && a3Var != null) {
            arrayList.remove(a3Var);
        }
        if (B && a3Var2 == null) {
            arrayList.add(s());
        } else if (!B && a3Var2 != null) {
            arrayList.remove(a3Var2);
        }
        return arrayList;
    }

    private static Matrix q(Rect rect, Size size) {
        h.b(rect.width() > 0 && rect.height() > 0, "Cannot compute viewport crop rects zero sized sensor rect.");
        RectF rectF = new RectF(rect);
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0.0f, 0.0f, size.getWidth(), size.getHeight()), rectF, Matrix.ScaleToFit.CENTER);
        matrix.invert(matrix);
        return matrix;
    }

    private Map<a3, Size> r(q qVar, List<a3> list, List<a3> list2, Map<a3, b> map) {
        ArrayList arrayList = new ArrayList();
        String c9 = qVar.c();
        HashMap hashMap = new HashMap();
        for (a3 a3Var : list2) {
            arrayList.add(y.a.a(this.f2668c.a(c9, a3Var.i(), a3Var.c()), a3Var.i(), a3Var.c(), a3Var.g().z(null)));
            hashMap.put(a3Var, a3Var.c());
        }
        if (!list.isEmpty()) {
            HashMap hashMap2 = new HashMap();
            for (a3 a3Var2 : list) {
                b bVar = map.get(a3Var2);
                hashMap2.put(a3Var2.s(qVar, bVar.f2680a, bVar.f2681b), a3Var2);
            }
            Map<v<?>, Size> b9 = this.f2668c.b(c9, arrayList, new ArrayList(hashMap2.keySet()));
            for (Map.Entry entry : hashMap2.entrySet()) {
                hashMap.put((a3) entry.getValue(), b9.get(entry.getKey()));
            }
        }
        return hashMap;
    }

    private e1 s() {
        return new e1.f().i("ImageCapture-Extra").c();
    }

    private y1 t() {
        y1 c9 = new y1.b().i("Preview-Extra").c();
        c9.Y(new y1.d() { // from class: b0.d
            @Override // androidx.camera.core.y1.d
            public final void a(z2 z2Var) {
                CameraUseCaseAdapter.G(z2Var);
            }
        });
        return c9;
    }

    private void u(List<a3> list) {
        synchronized (this.f2675k) {
            if (!list.isEmpty()) {
                this.f2666a.l(list);
                for (a3 a3Var : list) {
                    if (this.f2671f.contains(a3Var)) {
                        a3Var.B(this.f2666a);
                    } else {
                        p1.c("CameraUseCaseAdapter", "Attempting to detach non-attached UseCase: " + a3Var);
                    }
                }
                this.f2671f.removeAll(list);
            }
        }
    }

    public static a w(LinkedHashSet<CameraInternal> linkedHashSet) {
        return new a(linkedHashSet);
    }

    private Map<a3, b> y(List<a3> list, UseCaseConfigFactory useCaseConfigFactory, UseCaseConfigFactory useCaseConfigFactory2) {
        HashMap hashMap = new HashMap();
        for (a3 a3Var : list) {
            hashMap.put(a3Var, new b(a3Var.h(false, useCaseConfigFactory), a3Var.h(true, useCaseConfigFactory2)));
        }
        return hashMap;
    }

    public void H(Collection<a3> collection) {
        synchronized (this.f2675k) {
            u(new ArrayList(collection));
            if (A()) {
                this.f2678n.removeAll(collection);
                try {
                    f(Collections.emptyList());
                } catch (CameraException unused) {
                    throw new IllegalArgumentException("Failed to add extra fake Preview or ImageCapture use case!");
                }
            }
        }
    }

    public void J(List<androidx.camera.core.o> list) {
        synchronized (this.f2675k) {
            this.f2673h = list;
        }
    }

    public void K(g3 g3Var) {
        synchronized (this.f2675k) {
            this.f2672g = g3Var;
        }
    }

    @Override // androidx.camera.core.m
    public CameraControl a() {
        return this.f2666a.h();
    }

    @Override // androidx.camera.core.m
    public s b() {
        return this.f2666a.m();
    }

    public void e(d dVar) {
        synchronized (this.f2675k) {
            if (dVar == null) {
                dVar = n.a();
            }
            if (!this.f2671f.isEmpty() && !this.f2674j.F().equals(dVar.F())) {
                throw new IllegalStateException("Need to unbind all use cases before binding with extension enabled");
            }
            this.f2674j = dVar;
            this.f2666a.e(dVar);
        }
    }

    public void f(Collection<a3> collection) {
        synchronized (this.f2675k) {
            ArrayList<a3> arrayList = new ArrayList();
            for (a3 a3Var : collection) {
                if (this.f2671f.contains(a3Var)) {
                    p1.a("CameraUseCaseAdapter", "Attempting to attach already attached UseCase");
                } else {
                    arrayList.add(a3Var);
                }
            }
            List<a3> arrayList2 = new ArrayList<>(this.f2671f);
            List<a3> emptyList = Collections.emptyList();
            List<a3> emptyList2 = Collections.emptyList();
            if (A()) {
                arrayList2.removeAll(this.f2678n);
                arrayList2.addAll(arrayList);
                emptyList = p(arrayList2, new ArrayList<>(this.f2678n));
                ArrayList arrayList3 = new ArrayList(emptyList);
                arrayList3.removeAll(this.f2678n);
                arrayList.addAll(arrayList3);
                emptyList2 = new ArrayList<>(this.f2678n);
                emptyList2.removeAll(emptyList);
            }
            Map<a3, b> y8 = y(arrayList, this.f2674j.j(), this.f2669d);
            try {
                List<a3> arrayList4 = new ArrayList<>(this.f2671f);
                arrayList4.removeAll(emptyList2);
                Map<a3, Size> r4 = r(this.f2666a.m(), arrayList, arrayList4, y8);
                M(r4, collection);
                L(this.f2673h, collection);
                this.f2678n = emptyList;
                u(emptyList2);
                for (a3 a3Var2 : arrayList) {
                    b bVar = y8.get(a3Var2);
                    a3Var2.y(this.f2666a, bVar.f2680a, bVar.f2681b);
                    a3Var2.L((Size) h.h(r4.get(a3Var2)));
                }
                this.f2671f.addAll(arrayList);
                if (this.f2676l) {
                    this.f2666a.k(arrayList);
                }
                for (a3 a3Var3 : arrayList) {
                    a3Var3.w();
                }
            } catch (IllegalArgumentException e8) {
                throw new CameraException(e8.getMessage());
            }
        }
    }

    public void j(boolean z4) {
        this.f2666a.j(z4);
    }

    public void n() {
        synchronized (this.f2675k) {
            if (!this.f2676l) {
                this.f2666a.k(this.f2671f);
                I();
                for (a3 a3Var : this.f2671f) {
                    a3Var.w();
                }
                this.f2676l = true;
            }
        }
    }

    public void v() {
        synchronized (this.f2675k) {
            if (this.f2676l) {
                this.f2666a.l(new ArrayList(this.f2671f));
                o();
                this.f2676l = false;
            }
        }
    }

    public a x() {
        return this.f2670e;
    }

    public List<a3> z() {
        ArrayList arrayList;
        synchronized (this.f2675k) {
            arrayList = new ArrayList(this.f2671f);
        }
        return arrayList;
    }
}
