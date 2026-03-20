package androidx.appcompat.view;

import android.view.View;
import android.view.animation.Interpolator;
import androidx.core.view.i0;
import androidx.core.view.j0;
import androidx.core.view.k0;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h {

    /* renamed from: c  reason: collision with root package name */
    private Interpolator f814c;

    /* renamed from: d  reason: collision with root package name */
    j0 f815d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f816e;

    /* renamed from: b  reason: collision with root package name */
    private long f813b = -1;

    /* renamed from: f  reason: collision with root package name */
    private final k0 f817f = new a();

    /* renamed from: a  reason: collision with root package name */
    final ArrayList<i0> f812a = new ArrayList<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends k0 {

        /* renamed from: a  reason: collision with root package name */
        private boolean f818a = false;

        /* renamed from: b  reason: collision with root package name */
        private int f819b = 0;

        a() {
        }

        @Override // androidx.core.view.j0
        public void b(View view) {
            int i8 = this.f819b + 1;
            this.f819b = i8;
            if (i8 == h.this.f812a.size()) {
                j0 j0Var = h.this.f815d;
                if (j0Var != null) {
                    j0Var.b(null);
                }
                d();
            }
        }

        @Override // androidx.core.view.k0, androidx.core.view.j0
        public void c(View view) {
            if (this.f818a) {
                return;
            }
            this.f818a = true;
            j0 j0Var = h.this.f815d;
            if (j0Var != null) {
                j0Var.c(null);
            }
        }

        void d() {
            this.f819b = 0;
            this.f818a = false;
            h.this.b();
        }
    }

    public void a() {
        if (this.f816e) {
            Iterator<i0> it = this.f812a.iterator();
            while (it.hasNext()) {
                it.next().c();
            }
            this.f816e = false;
        }
    }

    void b() {
        this.f816e = false;
    }

    public h c(i0 i0Var) {
        if (!this.f816e) {
            this.f812a.add(i0Var);
        }
        return this;
    }

    public h d(i0 i0Var, i0 i0Var2) {
        this.f812a.add(i0Var);
        i0Var2.j(i0Var.d());
        this.f812a.add(i0Var2);
        return this;
    }

    public h e(long j8) {
        if (!this.f816e) {
            this.f813b = j8;
        }
        return this;
    }

    public h f(Interpolator interpolator) {
        if (!this.f816e) {
            this.f814c = interpolator;
        }
        return this;
    }

    public h g(j0 j0Var) {
        if (!this.f816e) {
            this.f815d = j0Var;
        }
        return this;
    }

    public void h() {
        if (this.f816e) {
            return;
        }
        Iterator<i0> it = this.f812a.iterator();
        while (it.hasNext()) {
            i0 next = it.next();
            long j8 = this.f813b;
            if (j8 >= 0) {
                next.f(j8);
            }
            Interpolator interpolator = this.f814c;
            if (interpolator != null) {
                next.g(interpolator);
            }
            if (this.f815d != null) {
                next.h(this.f817f);
            }
            next.l();
        }
        this.f816e = true;
    }
}
