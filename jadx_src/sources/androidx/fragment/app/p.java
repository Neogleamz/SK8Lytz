package androidx.fragment.app;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.core.view.c0;
import androidx.fragment.app.x;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.j0;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class p {

    /* renamed from: a  reason: collision with root package name */
    private final j f5652a;

    /* renamed from: b  reason: collision with root package name */
    private final q f5653b;

    /* renamed from: c  reason: collision with root package name */
    private final Fragment f5654c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f5655d = false;

    /* renamed from: e  reason: collision with root package name */
    private int f5656e = -1;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements View.OnAttachStateChangeListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f5657a;

        a(View view) {
            this.f5657a = view;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            this.f5657a.removeOnAttachStateChangeListener(this);
            c0.q0(this.f5657a);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class b {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f5659a;

        static {
            int[] iArr = new int[Lifecycle.State.values().length];
            f5659a = iArr;
            try {
                iArr[Lifecycle.State.RESUMED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5659a[Lifecycle.State.STARTED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5659a[Lifecycle.State.CREATED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f5659a[Lifecycle.State.INITIALIZED.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(j jVar, q qVar, Fragment fragment) {
        this.f5652a = jVar;
        this.f5653b = qVar;
        this.f5654c = fragment;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(j jVar, q qVar, Fragment fragment, FragmentState fragmentState) {
        this.f5652a = jVar;
        this.f5653b = qVar;
        this.f5654c = fragment;
        fragment.f5393c = null;
        fragment.f5395d = null;
        fragment.f5419x = 0;
        fragment.q = false;
        fragment.f5412m = false;
        Fragment fragment2 = fragment.f5403h;
        fragment.f5406j = fragment2 != null ? fragment2.f5399f : null;
        fragment.f5403h = null;
        Bundle bundle = fragmentState.f5532n;
        fragment.f5391b = bundle == null ? new Bundle() : bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(j jVar, q qVar, ClassLoader classLoader, g gVar, FragmentState fragmentState) {
        this.f5652a = jVar;
        this.f5653b = qVar;
        Fragment a9 = gVar.a(classLoader, fragmentState.f5520a);
        this.f5654c = a9;
        Bundle bundle = fragmentState.f5529k;
        if (bundle != null) {
            bundle.setClassLoader(classLoader);
        }
        a9.t1(fragmentState.f5529k);
        a9.f5399f = fragmentState.f5521b;
        a9.f5416p = fragmentState.f5522c;
        a9.f5417t = true;
        a9.C = fragmentState.f5523d;
        a9.E = fragmentState.f5524e;
        a9.F = fragmentState.f5525f;
        a9.K = fragmentState.f5526g;
        a9.f5414n = fragmentState.f5527h;
        a9.H = fragmentState.f5528j;
        a9.G = fragmentState.f5530l;
        a9.f5400f0 = Lifecycle.State.values()[fragmentState.f5531m];
        Bundle bundle2 = fragmentState.f5532n;
        a9.f5391b = bundle2 == null ? new Bundle() : bundle2;
        if (FragmentManager.F0(2)) {
            Log.v("FragmentManager", "Instantiated fragment " + a9);
        }
    }

    private boolean l(View view) {
        if (view == this.f5654c.T) {
            return true;
        }
        for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent()) {
            if (parent == this.f5654c.T) {
                return true;
            }
        }
        return false;
    }

    private Bundle q() {
        Bundle bundle = new Bundle();
        this.f5654c.f1(bundle);
        this.f5652a.j(this.f5654c, bundle, false);
        if (bundle.isEmpty()) {
            bundle = null;
        }
        if (this.f5654c.T != null) {
            s();
        }
        if (this.f5654c.f5393c != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putSparseParcelableArray("android:view_state", this.f5654c.f5393c);
        }
        if (this.f5654c.f5395d != null) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBundle("android:view_registry_state", this.f5654c.f5395d);
        }
        if (!this.f5654c.X) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean("android:user_visible_hint", this.f5654c.X);
        }
        return bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "moveto ACTIVITY_CREATED: " + this.f5654c);
        }
        Fragment fragment = this.f5654c;
        fragment.L0(fragment.f5391b);
        j jVar = this.f5652a;
        Fragment fragment2 = this.f5654c;
        jVar.a(fragment2, fragment2.f5391b, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b() {
        int j8 = this.f5653b.j(this.f5654c);
        Fragment fragment = this.f5654c;
        fragment.R.addView(fragment.T, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c() {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "moveto ATTACHED: " + this.f5654c);
        }
        Fragment fragment = this.f5654c;
        Fragment fragment2 = fragment.f5403h;
        p pVar = null;
        if (fragment2 != null) {
            p m8 = this.f5653b.m(fragment2.f5399f);
            if (m8 == null) {
                throw new IllegalStateException("Fragment " + this.f5654c + " declared target fragment " + this.f5654c.f5403h + " that does not belong to this FragmentManager!");
            }
            Fragment fragment3 = this.f5654c;
            fragment3.f5406j = fragment3.f5403h.f5399f;
            fragment3.f5403h = null;
            pVar = m8;
        } else {
            String str = fragment.f5406j;
            if (str != null && (pVar = this.f5653b.m(str)) == null) {
                throw new IllegalStateException("Fragment " + this.f5654c + " declared target fragment " + this.f5654c.f5406j + " that does not belong to this FragmentManager!");
            }
        }
        if (pVar != null && (FragmentManager.P || pVar.k().f5389a < 1)) {
            pVar.m();
        }
        Fragment fragment4 = this.f5654c;
        fragment4.f5421z = fragment4.f5420y.t0();
        Fragment fragment5 = this.f5654c;
        fragment5.B = fragment5.f5420y.w0();
        this.f5652a.g(this.f5654c, false);
        this.f5654c.M0();
        this.f5652a.b(this.f5654c, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int d() {
        Fragment fragment;
        ViewGroup viewGroup;
        Fragment fragment2 = this.f5654c;
        if (fragment2.f5420y == null) {
            return fragment2.f5389a;
        }
        int i8 = this.f5656e;
        int i9 = b.f5659a[fragment2.f5400f0.ordinal()];
        if (i9 != 1) {
            i8 = i9 != 2 ? i9 != 3 ? i9 != 4 ? Math.min(i8, -1) : Math.min(i8, 0) : Math.min(i8, 1) : Math.min(i8, 5);
        }
        Fragment fragment3 = this.f5654c;
        if (fragment3.f5416p) {
            if (fragment3.q) {
                i8 = Math.max(this.f5656e, 2);
                View view = this.f5654c.T;
                if (view != null && view.getParent() == null) {
                    i8 = Math.min(i8, 2);
                }
            } else {
                i8 = this.f5656e < 4 ? Math.min(i8, fragment3.f5389a) : Math.min(i8, 1);
            }
        }
        if (!this.f5654c.f5412m) {
            i8 = Math.min(i8, 1);
        }
        x.e.b bVar = null;
        if (FragmentManager.P && (viewGroup = (fragment = this.f5654c).R) != null) {
            bVar = x.n(viewGroup, fragment.E()).l(this);
        }
        if (bVar == x.e.b.ADDING) {
            i8 = Math.min(i8, 6);
        } else if (bVar == x.e.b.REMOVING) {
            i8 = Math.max(i8, 3);
        } else {
            Fragment fragment4 = this.f5654c;
            if (fragment4.f5414n) {
                i8 = fragment4.d0() ? Math.min(i8, 1) : Math.min(i8, -1);
            }
        }
        Fragment fragment5 = this.f5654c;
        if (fragment5.W && fragment5.f5389a < 5) {
            i8 = Math.min(i8, 4);
        }
        if (FragmentManager.F0(2)) {
            Log.v("FragmentManager", "computeExpectedState() of " + i8 + " for " + this.f5654c);
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e() {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "moveto CREATED: " + this.f5654c);
        }
        Fragment fragment = this.f5654c;
        if (fragment.f5398e0) {
            fragment.n1(fragment.f5391b);
            this.f5654c.f5389a = 1;
            return;
        }
        this.f5652a.h(fragment, fragment.f5391b, false);
        Fragment fragment2 = this.f5654c;
        fragment2.P0(fragment2.f5391b);
        j jVar = this.f5652a;
        Fragment fragment3 = this.f5654c;
        jVar.c(fragment3, fragment3.f5391b, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        String str;
        if (this.f5654c.f5416p) {
            return;
        }
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "moveto CREATE_VIEW: " + this.f5654c);
        }
        Fragment fragment = this.f5654c;
        LayoutInflater V0 = fragment.V0(fragment.f5391b);
        ViewGroup viewGroup = null;
        Fragment fragment2 = this.f5654c;
        ViewGroup viewGroup2 = fragment2.R;
        if (viewGroup2 != null) {
            viewGroup = viewGroup2;
        } else {
            int i8 = fragment2.E;
            if (i8 != 0) {
                if (i8 == -1) {
                    throw new IllegalArgumentException("Cannot create fragment " + this.f5654c + " for a container view with no id");
                }
                viewGroup = (ViewGroup) fragment2.f5420y.o0().c(this.f5654c.E);
                if (viewGroup == null) {
                    Fragment fragment3 = this.f5654c;
                    if (!fragment3.f5417t) {
                        try {
                            str = fragment3.K().getResourceName(this.f5654c.E);
                        } catch (Resources.NotFoundException unused) {
                            str = "unknown";
                        }
                        throw new IllegalArgumentException("No view found for id 0x" + Integer.toHexString(this.f5654c.E) + " (" + str + ") for fragment " + this.f5654c);
                    }
                }
            }
        }
        Fragment fragment4 = this.f5654c;
        fragment4.R = viewGroup;
        fragment4.R0(V0, viewGroup, fragment4.f5391b);
        View view = this.f5654c.T;
        if (view != null) {
            boolean z4 = false;
            view.setSaveFromParentEnabled(false);
            Fragment fragment5 = this.f5654c;
            fragment5.T.setTag(b1.b.f7947a, fragment5);
            if (viewGroup != null) {
                b();
            }
            Fragment fragment6 = this.f5654c;
            if (fragment6.G) {
                fragment6.T.setVisibility(8);
            }
            if (c0.V(this.f5654c.T)) {
                c0.q0(this.f5654c.T);
            } else {
                View view2 = this.f5654c.T;
                view2.addOnAttachStateChangeListener(new a(view2));
            }
            this.f5654c.i1();
            j jVar = this.f5652a;
            Fragment fragment7 = this.f5654c;
            jVar.m(fragment7, fragment7.T, fragment7.f5391b, false);
            int visibility = this.f5654c.T.getVisibility();
            float alpha = this.f5654c.T.getAlpha();
            if (FragmentManager.P) {
                this.f5654c.A1(alpha);
                Fragment fragment8 = this.f5654c;
                if (fragment8.R != null && visibility == 0) {
                    View findFocus = fragment8.T.findFocus();
                    if (findFocus != null) {
                        this.f5654c.u1(findFocus);
                        if (FragmentManager.F0(2)) {
                            Log.v("FragmentManager", "requestFocus: Saved focused view " + findFocus + " for Fragment " + this.f5654c);
                        }
                    }
                    this.f5654c.T.setAlpha(0.0f);
                }
            } else {
                Fragment fragment9 = this.f5654c;
                if (visibility == 0 && fragment9.R != null) {
                    z4 = true;
                }
                fragment9.f5390a0 = z4;
            }
        }
        this.f5654c.f5389a = 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        Fragment f5;
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "movefrom CREATED: " + this.f5654c);
        }
        Fragment fragment = this.f5654c;
        boolean z4 = true;
        boolean z8 = fragment.f5414n && !fragment.d0();
        if (!(z8 || this.f5653b.o().p(this.f5654c))) {
            String str = this.f5654c.f5406j;
            if (str != null && (f5 = this.f5653b.f(str)) != null && f5.K) {
                this.f5654c.f5403h = f5;
            }
            this.f5654c.f5389a = 0;
            return;
        }
        h<?> hVar = this.f5654c.f5421z;
        if (hVar instanceof j0) {
            z4 = this.f5653b.o().m();
        } else if (hVar.f() instanceof Activity) {
            z4 = true ^ ((Activity) hVar.f()).isChangingConfigurations();
        }
        if (z8 || z4) {
            this.f5653b.o().g(this.f5654c);
        }
        this.f5654c.S0();
        this.f5652a.d(this.f5654c, false);
        for (p pVar : this.f5653b.k()) {
            if (pVar != null) {
                Fragment k8 = pVar.k();
                if (this.f5654c.f5399f.equals(k8.f5406j)) {
                    k8.f5403h = this.f5654c;
                    k8.f5406j = null;
                }
            }
        }
        Fragment fragment2 = this.f5654c;
        String str2 = fragment2.f5406j;
        if (str2 != null) {
            fragment2.f5403h = this.f5653b.f(str2);
        }
        this.f5653b.q(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h() {
        View view;
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "movefrom CREATE_VIEW: " + this.f5654c);
        }
        Fragment fragment = this.f5654c;
        ViewGroup viewGroup = fragment.R;
        if (viewGroup != null && (view = fragment.T) != null) {
            viewGroup.removeView(view);
        }
        this.f5654c.T0();
        this.f5652a.n(this.f5654c, false);
        Fragment fragment2 = this.f5654c;
        fragment2.R = null;
        fragment2.T = null;
        fragment2.f5404h0 = null;
        fragment2.f5405i0.o(null);
        this.f5654c.q = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i() {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "movefrom ATTACHED: " + this.f5654c);
        }
        this.f5654c.U0();
        boolean z4 = false;
        this.f5652a.e(this.f5654c, false);
        Fragment fragment = this.f5654c;
        fragment.f5389a = -1;
        fragment.f5421z = null;
        fragment.B = null;
        fragment.f5420y = null;
        if (fragment.f5414n && !fragment.d0()) {
            z4 = true;
        }
        if (z4 || this.f5653b.o().p(this.f5654c)) {
            if (FragmentManager.F0(3)) {
                Log.d("FragmentManager", "initState called for fragment: " + this.f5654c);
            }
            this.f5654c.X();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        Fragment fragment = this.f5654c;
        if (fragment.f5416p && fragment.q && !fragment.f5418w) {
            if (FragmentManager.F0(3)) {
                Log.d("FragmentManager", "moveto CREATE_VIEW: " + this.f5654c);
            }
            Fragment fragment2 = this.f5654c;
            fragment2.R0(fragment2.V0(fragment2.f5391b), null, this.f5654c.f5391b);
            View view = this.f5654c.T;
            if (view != null) {
                view.setSaveFromParentEnabled(false);
                Fragment fragment3 = this.f5654c;
                fragment3.T.setTag(b1.b.f7947a, fragment3);
                Fragment fragment4 = this.f5654c;
                if (fragment4.G) {
                    fragment4.T.setVisibility(8);
                }
                this.f5654c.i1();
                j jVar = this.f5652a;
                Fragment fragment5 = this.f5654c;
                jVar.m(fragment5, fragment5.T, fragment5.f5391b, false);
                this.f5654c.f5389a = 2;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment k() {
        return this.f5654c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m() {
        ViewGroup viewGroup;
        ViewGroup viewGroup2;
        ViewGroup viewGroup3;
        if (this.f5655d) {
            if (FragmentManager.F0(2)) {
                Log.v("FragmentManager", "Ignoring re-entrant call to moveToExpectedState() for " + k());
                return;
            }
            return;
        }
        try {
            this.f5655d = true;
            while (true) {
                int d8 = d();
                Fragment fragment = this.f5654c;
                int i8 = fragment.f5389a;
                if (d8 == i8) {
                    if (FragmentManager.P && fragment.f5392b0) {
                        if (fragment.T != null && (viewGroup = fragment.R) != null) {
                            x n8 = x.n(viewGroup, fragment.E());
                            if (this.f5654c.G) {
                                n8.c(this);
                            } else {
                                n8.e(this);
                            }
                        }
                        Fragment fragment2 = this.f5654c;
                        FragmentManager fragmentManager = fragment2.f5420y;
                        if (fragmentManager != null) {
                            fragmentManager.D0(fragment2);
                        }
                        Fragment fragment3 = this.f5654c;
                        fragment3.f5392b0 = false;
                        fragment3.y0(fragment3.G);
                    }
                    return;
                } else if (d8 > i8) {
                    switch (i8 + 1) {
                        case 0:
                            c();
                            continue;
                        case 1:
                            e();
                            continue;
                        case 2:
                            j();
                            f();
                            continue;
                        case 3:
                            a();
                            continue;
                        case 4:
                            if (fragment.T != null && (viewGroup2 = fragment.R) != null) {
                                x.n(viewGroup2, fragment.E()).b(x.e.c.f(this.f5654c.T.getVisibility()), this);
                            }
                            this.f5654c.f5389a = 4;
                            continue;
                        case 5:
                            u();
                            continue;
                        case 6:
                            fragment.f5389a = 6;
                            continue;
                        case 7:
                            p();
                            continue;
                        default:
                            continue;
                    }
                } else {
                    switch (i8 - 1) {
                        case -1:
                            i();
                            continue;
                        case 0:
                            g();
                            continue;
                        case 1:
                            h();
                            this.f5654c.f5389a = 1;
                            continue;
                        case 2:
                            fragment.q = false;
                            fragment.f5389a = 2;
                            continue;
                        case 3:
                            if (FragmentManager.F0(3)) {
                                Log.d("FragmentManager", "movefrom ACTIVITY_CREATED: " + this.f5654c);
                            }
                            Fragment fragment4 = this.f5654c;
                            if (fragment4.T != null && fragment4.f5393c == null) {
                                s();
                            }
                            Fragment fragment5 = this.f5654c;
                            if (fragment5.T != null && (viewGroup3 = fragment5.R) != null) {
                                x.n(viewGroup3, fragment5.E()).d(this);
                            }
                            this.f5654c.f5389a = 3;
                            continue;
                        case 4:
                            v();
                            continue;
                        case 5:
                            fragment.f5389a = 5;
                            continue;
                        case 6:
                            n();
                            continue;
                        default:
                            continue;
                    }
                }
            }
        } finally {
            this.f5655d = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n() {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "movefrom RESUMED: " + this.f5654c);
        }
        this.f5654c.a1();
        this.f5652a.f(this.f5654c, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(ClassLoader classLoader) {
        Bundle bundle = this.f5654c.f5391b;
        if (bundle == null) {
            return;
        }
        bundle.setClassLoader(classLoader);
        Fragment fragment = this.f5654c;
        fragment.f5393c = fragment.f5391b.getSparseParcelableArray("android:view_state");
        Fragment fragment2 = this.f5654c;
        fragment2.f5395d = fragment2.f5391b.getBundle("android:view_registry_state");
        Fragment fragment3 = this.f5654c;
        fragment3.f5406j = fragment3.f5391b.getString("android:target_state");
        Fragment fragment4 = this.f5654c;
        if (fragment4.f5406j != null) {
            fragment4.f5408k = fragment4.f5391b.getInt("android:target_req_state", 0);
        }
        Fragment fragment5 = this.f5654c;
        Boolean bool = fragment5.f5397e;
        if (bool != null) {
            fragment5.X = bool.booleanValue();
            this.f5654c.f5397e = null;
        } else {
            fragment5.X = fragment5.f5391b.getBoolean("android:user_visible_hint", true);
        }
        Fragment fragment6 = this.f5654c;
        if (fragment6.X) {
            return;
        }
        fragment6.W = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p() {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "moveto RESUMED: " + this.f5654c);
        }
        View x8 = this.f5654c.x();
        if (x8 != null && l(x8)) {
            boolean requestFocus = x8.requestFocus();
            if (FragmentManager.F0(2)) {
                StringBuilder sb = new StringBuilder();
                sb.append("requestFocus: Restoring focused view ");
                sb.append(x8);
                sb.append(" ");
                sb.append(requestFocus ? "succeeded" : "failed");
                sb.append(" on Fragment ");
                sb.append(this.f5654c);
                sb.append(" resulting in focused view ");
                sb.append(this.f5654c.T.findFocus());
                Log.v("FragmentManager", sb.toString());
            }
        }
        this.f5654c.u1(null);
        this.f5654c.e1();
        this.f5652a.i(this.f5654c, false);
        Fragment fragment = this.f5654c;
        fragment.f5391b = null;
        fragment.f5393c = null;
        fragment.f5395d = null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public FragmentState r() {
        FragmentState fragmentState = new FragmentState(this.f5654c);
        Fragment fragment = this.f5654c;
        if (fragment.f5389a <= -1 || fragmentState.f5532n != null) {
            fragmentState.f5532n = fragment.f5391b;
        } else {
            Bundle q = q();
            fragmentState.f5532n = q;
            if (this.f5654c.f5406j != null) {
                if (q == null) {
                    fragmentState.f5532n = new Bundle();
                }
                fragmentState.f5532n.putString("android:target_state", this.f5654c.f5406j);
                int i8 = this.f5654c.f5408k;
                if (i8 != 0) {
                    fragmentState.f5532n.putInt("android:target_req_state", i8);
                }
            }
        }
        return fragmentState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s() {
        if (this.f5654c.T == null) {
            return;
        }
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        this.f5654c.T.saveHierarchyState(sparseArray);
        if (sparseArray.size() > 0) {
            this.f5654c.f5393c = sparseArray;
        }
        Bundle bundle = new Bundle();
        this.f5654c.f5404h0.e(bundle);
        if (bundle.isEmpty()) {
            return;
        }
        this.f5654c.f5395d = bundle;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void t(int i8) {
        this.f5656e = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u() {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "moveto STARTED: " + this.f5654c);
        }
        this.f5654c.g1();
        this.f5652a.k(this.f5654c, false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v() {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "movefrom STARTED: " + this.f5654c);
        }
        this.f5654c.h1();
        this.f5652a.l(this.f5654c, false);
    }
}
