package androidx.fragment.app;

import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Lifecycle;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class n extends androidx.viewpager.widget.a {

    /* renamed from: c  reason: collision with root package name */
    private final FragmentManager f5647c;

    /* renamed from: d  reason: collision with root package name */
    private final int f5648d;

    /* renamed from: e  reason: collision with root package name */
    private r f5649e;

    /* renamed from: f  reason: collision with root package name */
    private Fragment f5650f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f5651g;

    @Deprecated
    public n(FragmentManager fragmentManager) {
        this(fragmentManager, 0);
    }

    public n(FragmentManager fragmentManager, int i8) {
        this.f5649e = null;
        this.f5650f = null;
        this.f5647c = fragmentManager;
        this.f5648d = i8;
    }

    private static String x(int i8, long j8) {
        return "android:switcher:" + i8 + ":" + j8;
    }

    @Override // androidx.viewpager.widget.a
    public void b(ViewGroup viewGroup, int i8, Object obj) {
        Fragment fragment = (Fragment) obj;
        if (this.f5649e == null) {
            this.f5649e = this.f5647c.l();
        }
        this.f5649e.m(fragment);
        if (fragment.equals(this.f5650f)) {
            this.f5650f = null;
        }
    }

    @Override // androidx.viewpager.widget.a
    public void d(ViewGroup viewGroup) {
        r rVar = this.f5649e;
        if (rVar != null) {
            if (!this.f5651g) {
                try {
                    this.f5651g = true;
                    rVar.l();
                } finally {
                    this.f5651g = false;
                }
            }
            this.f5649e = null;
        }
    }

    @Override // androidx.viewpager.widget.a
    public Object j(ViewGroup viewGroup, int i8) {
        if (this.f5649e == null) {
            this.f5649e = this.f5647c.l();
        }
        long w8 = w(i8);
        Fragment i02 = this.f5647c.i0(x(viewGroup.getId(), w8));
        if (i02 != null) {
            this.f5649e.h(i02);
        } else {
            i02 = v(i8);
            this.f5649e.b(viewGroup.getId(), i02, x(viewGroup.getId(), w8));
        }
        if (i02 != this.f5650f) {
            i02.w1(false);
            if (this.f5648d == 1) {
                this.f5649e.t(i02, Lifecycle.State.STARTED);
            } else {
                i02.C1(false);
            }
        }
        return i02;
    }

    @Override // androidx.viewpager.widget.a
    public boolean k(View view, Object obj) {
        return ((Fragment) obj).T() == view;
    }

    @Override // androidx.viewpager.widget.a
    public void n(Parcelable parcelable, ClassLoader classLoader) {
    }

    @Override // androidx.viewpager.widget.a
    public Parcelable o() {
        return null;
    }

    @Override // androidx.viewpager.widget.a
    public void q(ViewGroup viewGroup, int i8, Object obj) {
        Fragment fragment = (Fragment) obj;
        Fragment fragment2 = this.f5650f;
        if (fragment != fragment2) {
            if (fragment2 != null) {
                fragment2.w1(false);
                if (this.f5648d == 1) {
                    if (this.f5649e == null) {
                        this.f5649e = this.f5647c.l();
                    }
                    this.f5649e.t(this.f5650f, Lifecycle.State.STARTED);
                } else {
                    this.f5650f.C1(false);
                }
            }
            fragment.w1(true);
            if (this.f5648d == 1) {
                if (this.f5649e == null) {
                    this.f5649e = this.f5647c.l();
                }
                this.f5649e.t(fragment, Lifecycle.State.RESUMED);
            } else {
                fragment.C1(true);
            }
            this.f5650f = fragment;
        }
    }

    @Override // androidx.viewpager.widget.a
    public void t(ViewGroup viewGroup) {
        if (viewGroup.getId() != -1) {
            return;
        }
        throw new IllegalStateException("ViewPager with adapter " + this + " requires a view id");
    }

    public abstract Fragment v(int i8);

    public long w(int i8) {
        return i8;
    }
}
