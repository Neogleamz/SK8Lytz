package x;

import android.util.Size;
import android.view.Surface;
import androidx.camera.core.h0;
import androidx.camera.core.impl.DeferrableSurface;
import androidx.camera.core.l1;
import androidx.camera.core.m2;
import androidx.camera.core.s1;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import y.h0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l {

    /* renamed from: a  reason: collision with root package name */
    private final Set<Integer> f23727a = new HashSet();

    /* renamed from: b  reason: collision with root package name */
    private final Set<l1> f23728b = new HashSet();

    /* renamed from: c  reason: collision with root package name */
    private a0 f23729c = null;

    /* renamed from: d  reason: collision with root package name */
    m2 f23730d;

    /* renamed from: e  reason: collision with root package name */
    private b f23731e;

    /* renamed from: f  reason: collision with root package name */
    private a f23732f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {

        /* renamed from: a  reason: collision with root package name */
        private y.h f23733a;

        /* renamed from: b  reason: collision with root package name */
        private DeferrableSurface f23734b;

        /* JADX INFO: Access modifiers changed from: package-private */
        public static a g(Size size, int i8) {
            return new x.b(size, i8, new g0.c());
        }

        void a() {
            this.f23734b.c();
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public y.h b() {
            return this.f23733a;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract int c();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract g0.c<a0> d();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract Size e();

        /* JADX INFO: Access modifiers changed from: package-private */
        public DeferrableSurface f() {
            return this.f23734b;
        }

        void h(y.h hVar) {
            this.f23733a = hVar;
        }

        void i(Surface surface) {
            androidx.core.util.h.k(this.f23734b == null, "The surface is already set.");
            this.f23734b = new h0(surface);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b {
        static b d(int i8) {
            return new c(new g0.c(), new g0.c(), i8);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract int a();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract g0.c<l1> b();

        /* JADX INFO: Access modifiers changed from: package-private */
        public abstract g0.c<a0> c();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c(y.g0 g0Var) {
        l1 f5 = g0Var.f();
        Objects.requireNonNull(f5);
        e(f5);
    }

    private void d(l1 l1Var) {
        Object c9 = l1Var.e1().a().c(this.f23729c.g());
        Objects.requireNonNull(c9);
        int intValue = ((Integer) c9).intValue();
        boolean contains = this.f23727a.contains(Integer.valueOf(intValue));
        androidx.core.util.h.k(contains, "Received an unexpected stage id" + intValue);
        this.f23727a.remove(Integer.valueOf(intValue));
        if (this.f23727a.isEmpty()) {
            this.f23729c.l();
            this.f23729c = null;
        }
        this.f23731e.b().accept(l1Var);
    }

    public int b() {
        androidx.camera.core.impl.utils.m.a();
        androidx.core.util.h.k(this.f23730d != null, "The ImageReader is not initialized.");
        return this.f23730d.h();
    }

    void e(l1 l1Var) {
        androidx.camera.core.impl.utils.m.a();
        if (this.f23729c == null) {
            this.f23728b.add(l1Var);
        } else {
            d(l1Var);
        }
    }

    void f(a0 a0Var) {
        androidx.camera.core.impl.utils.m.a();
        boolean z4 = true;
        androidx.core.util.h.k(b() > 0, "Too many acquire images. Close image to be able to process next.");
        if (this.f23729c != null && !this.f23727a.isEmpty()) {
            z4 = false;
        }
        androidx.core.util.h.k(z4, "The previous request is not complete");
        this.f23729c = a0Var;
        this.f23727a.addAll(a0Var.f());
        this.f23731e.c().accept(a0Var);
        for (l1 l1Var : this.f23728b) {
            d(l1Var);
        }
        this.f23728b.clear();
    }

    public void g() {
        androidx.camera.core.impl.utils.m.a();
        m2 m2Var = this.f23730d;
        if (m2Var != null) {
            m2Var.k();
        }
        a aVar = this.f23732f;
        if (aVar != null) {
            aVar.a();
        }
    }

    public void h(h0.a aVar) {
        androidx.camera.core.impl.utils.m.a();
        androidx.core.util.h.k(this.f23730d != null, "The ImageReader is not initialized.");
        this.f23730d.l(aVar);
    }

    public b i(a aVar) {
        this.f23732f = aVar;
        Size e8 = aVar.e();
        s1 s1Var = new s1(e8.getWidth(), e8.getHeight(), aVar.c(), 4);
        this.f23730d = new m2(s1Var);
        aVar.h(s1Var.l());
        Surface surface = s1Var.getSurface();
        Objects.requireNonNull(surface);
        aVar.i(surface);
        s1Var.a(new k(this), z.a.d());
        aVar.d().a(new j(this));
        b d8 = b.d(aVar.c());
        this.f23731e = d8;
        return d8;
    }
}
