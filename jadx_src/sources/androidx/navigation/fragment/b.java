package androidx.navigation.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.a;
import androidx.navigation.l;
import androidx.navigation.p;
import androidx.navigation.q;
import androidx.navigation.t;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b extends Fragment {

    /* renamed from: p0  reason: collision with root package name */
    private l f6341p0;

    /* renamed from: q0  reason: collision with root package name */
    private Boolean f6342q0 = null;

    /* renamed from: r0  reason: collision with root package name */
    private View f6343r0;

    /* renamed from: s0  reason: collision with root package name */
    private int f6344s0;

    /* renamed from: t0  reason: collision with root package name */
    private boolean f6345t0;

    public static NavController K1(Fragment fragment) {
        for (Fragment fragment2 = fragment; fragment2 != null; fragment2 = fragment2.D()) {
            if (fragment2 instanceof b) {
                return ((b) fragment2).M1();
            }
            Fragment x02 = fragment2.E().x0();
            if (x02 instanceof b) {
                return ((b) x02).M1();
            }
        }
        View T = fragment.T();
        if (T != null) {
            return p.a(T);
        }
        Dialog P1 = fragment instanceof androidx.fragment.app.c ? ((androidx.fragment.app.c) fragment).P1() : null;
        if (P1 == null || P1.getWindow() == null) {
            throw new IllegalStateException("Fragment " + fragment + " does not have a NavController set");
        }
        return p.a(P1.getWindow().getDecorView());
    }

    private int L1() {
        int z4 = z();
        return (z4 == 0 || z4 == -1) ? c.f6346a : z4;
    }

    @Override // androidx.fragment.app.Fragment
    public void A0(Context context, AttributeSet attributeSet, Bundle bundle) {
        super.A0(context, attributeSet, bundle);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, t.f6460p);
        int resourceId = obtainStyledAttributes.getResourceId(t.q, 0);
        if (resourceId != 0) {
            this.f6344s0 = resourceId;
        }
        obtainStyledAttributes.recycle();
        TypedArray obtainStyledAttributes2 = context.obtainStyledAttributes(attributeSet, d.f6363r);
        if (obtainStyledAttributes2.getBoolean(d.f6364s, false)) {
            this.f6345t0 = true;
        }
        obtainStyledAttributes2.recycle();
    }

    @Override // androidx.fragment.app.Fragment
    public void G0(boolean z4) {
        l lVar = this.f6341p0;
        if (lVar != null) {
            lVar.b(z4);
        } else {
            this.f6342q0 = Boolean.valueOf(z4);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void I0(Bundle bundle) {
        super.I0(bundle);
        Bundle q = this.f6341p0.q();
        if (q != null) {
            bundle.putBundle("android-support-nav:fragment:navControllerState", q);
        }
        if (this.f6345t0) {
            bundle.putBoolean("android-support-nav:fragment:defaultHost", true);
        }
        int i8 = this.f6344s0;
        if (i8 != 0) {
            bundle.putInt("android-support-nav:fragment:graphId", i8);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void J0(View view, Bundle bundle) {
        super.J0(view, bundle);
        if (!(view instanceof ViewGroup)) {
            throw new IllegalStateException("created host view " + view + " is not a ViewGroup");
        }
        p.d(view, this.f6341p0);
        if (view.getParent() != null) {
            View view2 = (View) view.getParent();
            this.f6343r0 = view2;
            if (view2.getId() == z()) {
                p.d(this.f6343r0, this.f6341p0);
            }
        }
    }

    @Deprecated
    protected q<? extends a.C0069a> J1() {
        return new a(l1(), q(), L1());
    }

    public final NavController M1() {
        l lVar = this.f6341p0;
        if (lVar != null) {
            return lVar;
        }
        throw new IllegalStateException("NavController is not available before onCreate()");
    }

    protected void N1(NavController navController) {
        navController.i().a(new DialogFragmentNavigator(l1(), q()));
        navController.i().a(J1());
    }

    @Override // androidx.fragment.app.Fragment
    public void n0(Context context) {
        super.n0(context);
        if (this.f6345t0) {
            E().l().u(this).i();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void o0(Fragment fragment) {
        super.o0(fragment);
        ((DialogFragmentNavigator) this.f6341p0.i().d(DialogFragmentNavigator.class)).h(fragment);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        Bundle bundle2;
        l lVar = new l(l1());
        this.f6341p0 = lVar;
        lVar.u(this);
        this.f6341p0.v(k1().getOnBackPressedDispatcher());
        l lVar2 = this.f6341p0;
        Boolean bool = this.f6342q0;
        lVar2.b(bool != null && bool.booleanValue());
        this.f6342q0 = null;
        this.f6341p0.w(getViewModelStore());
        N1(this.f6341p0);
        if (bundle != null) {
            bundle2 = bundle.getBundle("android-support-nav:fragment:navControllerState");
            if (bundle.getBoolean("android-support-nav:fragment:defaultHost", false)) {
                this.f6345t0 = true;
                E().l().u(this).i();
            }
            this.f6344s0 = bundle.getInt("android-support-nav:fragment:graphId");
        } else {
            bundle2 = null;
        }
        if (bundle2 != null) {
            this.f6341p0.p(bundle2);
        }
        int i8 = this.f6344s0;
        if (i8 != 0) {
            this.f6341p0.r(i8);
        } else {
            Bundle p8 = p();
            int i9 = p8 != null ? p8.getInt("android-support-nav:fragment:graphId") : 0;
            Bundle bundle3 = p8 != null ? p8.getBundle("android-support-nav:fragment:startDestinationArgs") : null;
            if (i9 != 0) {
                this.f6341p0.s(i9, bundle3);
            }
        }
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View t0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FragmentContainerView fragmentContainerView = new FragmentContainerView(layoutInflater.getContext());
        fragmentContainerView.setId(L1());
        return fragmentContainerView;
    }

    @Override // androidx.fragment.app.Fragment
    public void v0() {
        super.v0();
        View view = this.f6343r0;
        if (view != null && p.a(view) == this.f6341p0) {
            p.d(this.f6343r0, null);
        }
        this.f6343r0 = null;
    }
}
