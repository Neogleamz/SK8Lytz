package androidx.fragment.app;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.a0;
import androidx.lifecycle.f0;
import androidx.lifecycle.i0;
import androidx.lifecycle.j0;
import androidx.lifecycle.k0;
import androidx.lifecycle.l0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Fragment implements ComponentCallbacks, View.OnCreateContextMenuListener, androidx.lifecycle.j, j0, androidx.lifecycle.e, s1.d {

    /* renamed from: o0  reason: collision with root package name */
    static final Object f5388o0 = new Object();
    Fragment B;
    int C;
    int E;
    String F;
    boolean G;
    boolean H;
    boolean K;
    boolean L;
    boolean O;
    private boolean Q;
    ViewGroup R;
    View T;
    boolean W;
    e Y;

    /* renamed from: a0  reason: collision with root package name */
    boolean f5390a0;

    /* renamed from: b  reason: collision with root package name */
    Bundle f5391b;

    /* renamed from: b0  reason: collision with root package name */
    boolean f5392b0;

    /* renamed from: c  reason: collision with root package name */
    SparseArray<Parcelable> f5393c;

    /* renamed from: c0  reason: collision with root package name */
    float f5394c0;

    /* renamed from: d  reason: collision with root package name */
    Bundle f5395d;

    /* renamed from: d0  reason: collision with root package name */
    LayoutInflater f5396d0;

    /* renamed from: e  reason: collision with root package name */
    Boolean f5397e;

    /* renamed from: e0  reason: collision with root package name */
    boolean f5398e0;

    /* renamed from: g  reason: collision with root package name */
    Bundle f5401g;

    /* renamed from: g0  reason: collision with root package name */
    androidx.lifecycle.k f5402g0;

    /* renamed from: h  reason: collision with root package name */
    Fragment f5403h;

    /* renamed from: h0  reason: collision with root package name */
    v f5404h0;

    /* renamed from: j0  reason: collision with root package name */
    f0.b f5407j0;

    /* renamed from: k  reason: collision with root package name */
    int f5408k;

    /* renamed from: k0  reason: collision with root package name */
    s1.c f5409k0;

    /* renamed from: l0  reason: collision with root package name */
    private int f5411l0;

    /* renamed from: m  reason: collision with root package name */
    boolean f5412m;

    /* renamed from: n  reason: collision with root package name */
    boolean f5414n;

    /* renamed from: p  reason: collision with root package name */
    boolean f5416p;
    boolean q;

    /* renamed from: t  reason: collision with root package name */
    boolean f5417t;

    /* renamed from: w  reason: collision with root package name */
    boolean f5418w;

    /* renamed from: x  reason: collision with root package name */
    int f5419x;

    /* renamed from: y  reason: collision with root package name */
    FragmentManager f5420y;

    /* renamed from: z  reason: collision with root package name */
    h<?> f5421z;

    /* renamed from: a  reason: collision with root package name */
    int f5389a = -1;

    /* renamed from: f  reason: collision with root package name */
    String f5399f = UUID.randomUUID().toString();

    /* renamed from: j  reason: collision with root package name */
    String f5406j = null;

    /* renamed from: l  reason: collision with root package name */
    private Boolean f5410l = null;
    FragmentManager A = new k();
    boolean P = true;
    boolean X = true;
    Runnable Z = new a();

    /* renamed from: f0  reason: collision with root package name */
    Lifecycle.State f5400f0 = Lifecycle.State.RESUMED;

    /* renamed from: i0  reason: collision with root package name */
    androidx.lifecycle.p<androidx.lifecycle.j> f5405i0 = new androidx.lifecycle.p<>();

    /* renamed from: m0  reason: collision with root package name */
    private final AtomicInteger f5413m0 = new AtomicInteger();

    /* renamed from: n0  reason: collision with root package name */
    private final ArrayList<f> f5415n0 = new ArrayList<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class InstantiationException extends RuntimeException {
        public InstantiationException(String str, Exception exc) {
            super(str, exc);
        }
    }

    @SuppressLint({"BanParcelableUsage, ParcelClassLoader"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        final Bundle f5423a;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.ClassLoaderCreator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel, null);
            }

            @Override // android.os.Parcelable.ClassLoaderCreator
            /* renamed from: b */
            public SavedState createFromParcel(Parcel parcel, ClassLoader classLoader) {
                return new SavedState(parcel, classLoader);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: c */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        SavedState(Parcel parcel, ClassLoader classLoader) {
            Bundle readBundle = parcel.readBundle();
            this.f5423a = readBundle;
            if (classLoader == null || readBundle == null) {
                return;
            }
            readBundle.setClassLoader(classLoader);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeBundle(this.f5423a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Fragment.this.I1();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Runnable {
        b() {
        }

        @Override // java.lang.Runnable
        public void run() {
            Fragment.this.e(false);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ x f5426a;

        c(x xVar) {
            this.f5426a = xVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            this.f5426a.g();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends androidx.fragment.app.e {
        d() {
        }

        @Override // androidx.fragment.app.e
        public View c(int i8) {
            View view = Fragment.this.T;
            if (view != null) {
                return view.findViewById(i8);
            }
            throw new IllegalStateException("Fragment " + Fragment.this + " does not have a view");
        }

        @Override // androidx.fragment.app.e
        public boolean d() {
            return Fragment.this.T != null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e {

        /* renamed from: a  reason: collision with root package name */
        View f5429a;

        /* renamed from: b  reason: collision with root package name */
        Animator f5430b;

        /* renamed from: c  reason: collision with root package name */
        boolean f5431c;

        /* renamed from: d  reason: collision with root package name */
        int f5432d;

        /* renamed from: e  reason: collision with root package name */
        int f5433e;

        /* renamed from: f  reason: collision with root package name */
        int f5434f;

        /* renamed from: g  reason: collision with root package name */
        int f5435g;

        /* renamed from: h  reason: collision with root package name */
        int f5436h;

        /* renamed from: i  reason: collision with root package name */
        ArrayList<String> f5437i;

        /* renamed from: j  reason: collision with root package name */
        ArrayList<String> f5438j;

        /* renamed from: k  reason: collision with root package name */
        Object f5439k = null;

        /* renamed from: l  reason: collision with root package name */
        Object f5440l;

        /* renamed from: m  reason: collision with root package name */
        Object f5441m;

        /* renamed from: n  reason: collision with root package name */
        Object f5442n;

        /* renamed from: o  reason: collision with root package name */
        Object f5443o;

        /* renamed from: p  reason: collision with root package name */
        Object f5444p;
        Boolean q;

        /* renamed from: r  reason: collision with root package name */
        Boolean f5445r;

        /* renamed from: s  reason: collision with root package name */
        androidx.core.app.r f5446s;

        /* renamed from: t  reason: collision with root package name */
        androidx.core.app.r f5447t;

        /* renamed from: u  reason: collision with root package name */
        float f5448u;

        /* renamed from: v  reason: collision with root package name */
        View f5449v;

        /* renamed from: w  reason: collision with root package name */
        boolean f5450w;

        /* renamed from: x  reason: collision with root package name */
        g f5451x;

        /* renamed from: y  reason: collision with root package name */
        boolean f5452y;

        e() {
            Object obj = Fragment.f5388o0;
            this.f5440l = obj;
            this.f5441m = null;
            this.f5442n = obj;
            this.f5443o = null;
            this.f5444p = obj;
            this.f5448u = 1.0f;
            this.f5449v = null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static abstract class f {
        private f() {
        }

        abstract void a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface g {
        void a();

        void b();
    }

    public Fragment() {
        W();
    }

    private int B() {
        Lifecycle.State state = this.f5400f0;
        return (state == Lifecycle.State.INITIALIZED || this.B == null) ? state.ordinal() : Math.min(state.ordinal(), this.B.B());
    }

    private void W() {
        this.f5402g0 = new androidx.lifecycle.k(this);
        this.f5409k0 = s1.c.a(this);
        this.f5407j0 = null;
    }

    @Deprecated
    public static Fragment Y(Context context, String str, Bundle bundle) {
        try {
            Fragment newInstance = androidx.fragment.app.g.d(context.getClassLoader(), str).getConstructor(new Class[0]).newInstance(new Object[0]);
            if (bundle != null) {
                bundle.setClassLoader(newInstance.getClass().getClassLoader());
                newInstance.t1(bundle);
            }
            return newInstance;
        } catch (IllegalAccessException e8) {
            throw new InstantiationException("Unable to instantiate fragment " + str + ": make sure class name exists, is public, and has an empty constructor that is public", e8);
        } catch (java.lang.InstantiationException e9) {
            throw new InstantiationException("Unable to instantiate fragment " + str + ": make sure class name exists, is public, and has an empty constructor that is public", e9);
        } catch (NoSuchMethodException e10) {
            throw new InstantiationException("Unable to instantiate fragment " + str + ": could not find Fragment constructor", e10);
        } catch (InvocationTargetException e11) {
            throw new InstantiationException("Unable to instantiate fragment " + str + ": calling Fragment constructor caused an exception", e11);
        }
    }

    private e i() {
        if (this.Y == null) {
            this.Y = new e();
        }
        return this.Y;
    }

    private void o1() {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "moveto RESTORE_VIEW_STATE: " + this);
        }
        if (this.T != null) {
            p1(this.f5391b);
        }
        this.f5391b = null;
    }

    @Deprecated
    public LayoutInflater A(Bundle bundle) {
        h<?> hVar = this.f5421z;
        if (hVar != null) {
            LayoutInflater i8 = hVar.i();
            androidx.core.view.h.b(i8, this.A.u0());
            return i8;
        }
        throw new IllegalStateException("onGetLayoutInflater() cannot be executed until the Fragment is attached to the FragmentManager.");
    }

    public void A0(Context context, AttributeSet attributeSet, Bundle bundle) {
        this.Q = true;
        h<?> hVar = this.f5421z;
        Activity e8 = hVar == null ? null : hVar.e();
        if (e8 != null) {
            this.Q = false;
            z0(e8, attributeSet, bundle);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A1(float f5) {
        i().f5448u = f5;
    }

    public void B0(boolean z4) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void B1(ArrayList<String> arrayList, ArrayList<String> arrayList2) {
        i();
        e eVar = this.Y;
        eVar.f5437i = arrayList;
        eVar.f5438j = arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int C() {
        e eVar = this.Y;
        if (eVar == null) {
            return 0;
        }
        return eVar.f5436h;
    }

    public boolean C0(MenuItem menuItem) {
        return false;
    }

    @Deprecated
    public void C1(boolean z4) {
        if (!this.X && z4 && this.f5389a < 5 && this.f5420y != null && Z() && this.f5398e0) {
            FragmentManager fragmentManager = this.f5420y;
            fragmentManager.V0(fragmentManager.v(this));
        }
        this.X = z4;
        this.W = this.f5389a < 5 && !z4;
        if (this.f5391b != null) {
            this.f5397e = Boolean.valueOf(z4);
        }
    }

    public final Fragment D() {
        return this.B;
    }

    public void D0(Menu menu) {
    }

    public boolean D1(String str) {
        h<?> hVar = this.f5421z;
        if (hVar != null) {
            return hVar.l(str);
        }
        return false;
    }

    public final FragmentManager E() {
        FragmentManager fragmentManager = this.f5420y;
        if (fragmentManager != null) {
            return fragmentManager;
        }
        throw new IllegalStateException("Fragment " + this + " not associated with a fragment manager.");
    }

    public void E0(boolean z4) {
    }

    public void E1(@SuppressLint({"UnknownNullness"}) Intent intent) {
        F1(intent, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean F() {
        e eVar = this.Y;
        if (eVar == null) {
            return false;
        }
        return eVar.f5431c;
    }

    public void F0(Menu menu) {
    }

    public void F1(@SuppressLint({"UnknownNullness"}) Intent intent, Bundle bundle) {
        h<?> hVar = this.f5421z;
        if (hVar != null) {
            hVar.m(this, intent, -1, bundle);
            return;
        }
        throw new IllegalStateException("Fragment " + this + " not attached to Activity");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int G() {
        e eVar = this.Y;
        if (eVar == null) {
            return 0;
        }
        return eVar.f5434f;
    }

    public void G0(boolean z4) {
    }

    @Deprecated
    public void G1(@SuppressLint({"UnknownNullness"}) Intent intent, int i8, Bundle bundle) {
        if (this.f5421z != null) {
            E().M0(this, intent, i8, bundle);
            return;
        }
        throw new IllegalStateException("Fragment " + this + " not attached to Activity");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int H() {
        e eVar = this.Y;
        if (eVar == null) {
            return 0;
        }
        return eVar.f5435g;
    }

    @Deprecated
    public void H0(int i8, String[] strArr, int[] iArr) {
    }

    @Deprecated
    public void H1(@SuppressLint({"UnknownNullness"}) IntentSender intentSender, int i8, Intent intent, int i9, int i10, int i11, Bundle bundle) {
        if (this.f5421z == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        if (FragmentManager.F0(2)) {
            Log.v("FragmentManager", "Fragment " + this + " received the following in startIntentSenderForResult() requestCode: " + i8 + " IntentSender: " + intentSender + " fillInIntent: " + intent + " options: " + bundle);
        }
        E().N0(this, intentSender, i8, intent, i9, i10, i11, bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public float I() {
        e eVar = this.Y;
        if (eVar == null) {
            return 1.0f;
        }
        return eVar.f5448u;
    }

    public void I0(Bundle bundle) {
    }

    public void I1() {
        if (this.Y == null || !i().f5450w) {
            return;
        }
        if (this.f5421z == null) {
            i().f5450w = false;
        } else if (Looper.myLooper() != this.f5421z.g().getLooper()) {
            this.f5421z.g().postAtFrontOfQueue(new b());
        } else {
            e(true);
        }
    }

    public Object J() {
        e eVar = this.Y;
        if (eVar == null) {
            return null;
        }
        Object obj = eVar.f5442n;
        return obj == f5388o0 ? v() : obj;
    }

    public void J0(View view, Bundle bundle) {
    }

    public final Resources K() {
        return l1().getResources();
    }

    public void K0(Bundle bundle) {
        this.Q = true;
    }

    public Object L() {
        e eVar = this.Y;
        if (eVar == null) {
            return null;
        }
        Object obj = eVar.f5440l;
        return obj == f5388o0 ? s() : obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L0(Bundle bundle) {
        this.A.T0();
        this.f5389a = 3;
        this.Q = false;
        k0(bundle);
        if (this.Q) {
            o1();
            this.A.y();
            return;
        }
        throw new z("Fragment " + this + " did not call through to super.onActivityCreated()");
    }

    public Object M() {
        e eVar = this.Y;
        if (eVar == null) {
            return null;
        }
        return eVar.f5443o;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void M0() {
        Iterator<f> it = this.f5415n0.iterator();
        while (it.hasNext()) {
            it.next().a();
        }
        this.f5415n0.clear();
        this.A.j(this.f5421z, f(), this);
        this.f5389a = 0;
        this.Q = false;
        n0(this.f5421z.f());
        if (this.Q) {
            this.f5420y.I(this);
            this.A.z();
            return;
        }
        throw new z("Fragment " + this + " did not call through to super.onAttach()");
    }

    public Object N() {
        e eVar = this.Y;
        if (eVar == null) {
            return null;
        }
        Object obj = eVar.f5444p;
        return obj == f5388o0 ? M() : obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void N0(Configuration configuration) {
        onConfigurationChanged(configuration);
        this.A.A(configuration);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<String> O() {
        ArrayList<String> arrayList;
        e eVar = this.Y;
        return (eVar == null || (arrayList = eVar.f5437i) == null) ? new ArrayList<>() : arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean O0(MenuItem menuItem) {
        if (this.G) {
            return false;
        }
        if (p0(menuItem)) {
            return true;
        }
        return this.A.B(menuItem);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ArrayList<String> P() {
        ArrayList<String> arrayList;
        e eVar = this.Y;
        return (eVar == null || (arrayList = eVar.f5438j) == null) ? new ArrayList<>() : arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void P0(Bundle bundle) {
        this.A.T0();
        this.f5389a = 1;
        this.Q = false;
        if (Build.VERSION.SDK_INT >= 19) {
            this.f5402g0.a(new androidx.lifecycle.h() { // from class: androidx.fragment.app.Fragment.5
                @Override // androidx.lifecycle.h
                public void c(androidx.lifecycle.j jVar, Lifecycle.Event event) {
                    View view;
                    if (event != Lifecycle.Event.ON_STOP || (view = Fragment.this.T) == null) {
                        return;
                    }
                    view.cancelPendingInputEvents();
                }
            });
        }
        this.f5409k0.d(bundle);
        onCreate(bundle);
        this.f5398e0 = true;
        if (this.Q) {
            this.f5402g0.h(Lifecycle.Event.ON_CREATE);
            return;
        }
        throw new z("Fragment " + this + " did not call through to super.onCreate()");
    }

    public final String Q(int i8) {
        return K().getString(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean Q0(Menu menu, MenuInflater menuInflater) {
        boolean z4 = false;
        if (this.G) {
            return false;
        }
        if (this.O && this.P) {
            z4 = true;
            s0(menu, menuInflater);
        }
        return z4 | this.A.D(menu, menuInflater);
    }

    public final String R() {
        return this.F;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void R0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        this.A.T0();
        this.f5418w = true;
        this.f5404h0 = new v(this, getViewModelStore());
        View t02 = t0(layoutInflater, viewGroup, bundle);
        this.T = t02;
        if (t02 == null) {
            if (this.f5404h0.c()) {
                throw new IllegalStateException("Called getViewLifecycleOwner() but onCreateView() returned null");
            }
            this.f5404h0 = null;
            return;
        }
        this.f5404h0.b();
        k0.a(this.T, this.f5404h0);
        l0.a(this.T, this.f5404h0);
        s1.e.a(this.T, this.f5404h0);
        this.f5405i0.o(this.f5404h0);
    }

    @Deprecated
    public final Fragment S() {
        String str;
        Fragment fragment = this.f5403h;
        if (fragment != null) {
            return fragment;
        }
        FragmentManager fragmentManager = this.f5420y;
        if (fragmentManager == null || (str = this.f5406j) == null) {
            return null;
        }
        return fragmentManager.g0(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void S0() {
        this.A.E();
        this.f5402g0.h(Lifecycle.Event.ON_DESTROY);
        this.f5389a = 0;
        this.Q = false;
        this.f5398e0 = false;
        onDestroy();
        if (this.Q) {
            return;
        }
        throw new z("Fragment " + this + " did not call through to super.onDestroy()");
    }

    public View T() {
        return this.T;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void T0() {
        this.A.F();
        if (this.T != null && this.f5404h0.getLifecycle().b().f(Lifecycle.State.CREATED)) {
            this.f5404h0.a(Lifecycle.Event.ON_DESTROY);
        }
        this.f5389a = 1;
        this.Q = false;
        v0();
        if (this.Q) {
            androidx.loader.app.a.b(this).c();
            this.f5418w = false;
            return;
        }
        throw new z("Fragment " + this + " did not call through to super.onDestroyView()");
    }

    public androidx.lifecycle.j U() {
        v vVar = this.f5404h0;
        if (vVar != null) {
            return vVar;
        }
        throw new IllegalStateException("Can't access the Fragment View's LifecycleOwner when getView() is null i.e., before onCreateView() or after onDestroyView()");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void U0() {
        this.f5389a = -1;
        this.Q = false;
        w0();
        this.f5396d0 = null;
        if (this.Q) {
            if (this.A.E0()) {
                return;
            }
            this.A.E();
            this.A = new k();
            return;
        }
        throw new z("Fragment " + this + " did not call through to super.onDetach()");
    }

    public LiveData<androidx.lifecycle.j> V() {
        return this.f5405i0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LayoutInflater V0(Bundle bundle) {
        LayoutInflater x02 = x0(bundle);
        this.f5396d0 = x02;
        return x02;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void W0() {
        onLowMemory();
        this.A.G();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void X() {
        W();
        this.f5399f = UUID.randomUUID().toString();
        this.f5412m = false;
        this.f5414n = false;
        this.f5416p = false;
        this.q = false;
        this.f5417t = false;
        this.f5419x = 0;
        this.f5420y = null;
        this.A = new k();
        this.f5421z = null;
        this.C = 0;
        this.E = 0;
        this.F = null;
        this.G = false;
        this.H = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void X0(boolean z4) {
        B0(z4);
        this.A.H(z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean Y0(MenuItem menuItem) {
        if (this.G) {
            return false;
        }
        if (this.O && this.P && C0(menuItem)) {
            return true;
        }
        return this.A.J(menuItem);
    }

    public final boolean Z() {
        return this.f5421z != null && this.f5412m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Z0(Menu menu) {
        if (this.G) {
            return;
        }
        if (this.O && this.P) {
            D0(menu);
        }
        this.A.K(menu);
    }

    public final boolean a0() {
        return this.H;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a1() {
        this.A.M();
        if (this.T != null) {
            this.f5404h0.a(Lifecycle.Event.ON_PAUSE);
        }
        this.f5402g0.h(Lifecycle.Event.ON_PAUSE);
        this.f5389a = 6;
        this.Q = false;
        onPause();
        if (this.Q) {
            return;
        }
        throw new z("Fragment " + this + " did not call through to super.onPause()");
    }

    public final boolean b0() {
        return this.G;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b1(boolean z4) {
        E0(z4);
        this.A.N(z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c0() {
        e eVar = this.Y;
        if (eVar == null) {
            return false;
        }
        return eVar.f5452y;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean c1(Menu menu) {
        boolean z4 = false;
        if (this.G) {
            return false;
        }
        if (this.O && this.P) {
            z4 = true;
            F0(menu);
        }
        return z4 | this.A.O(menu);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean d0() {
        return this.f5419x > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d1() {
        boolean I0 = this.f5420y.I0(this);
        Boolean bool = this.f5410l;
        if (bool == null || bool.booleanValue() != I0) {
            this.f5410l = Boolean.valueOf(I0);
            G0(I0);
            this.A.P();
        }
    }

    void e(boolean z4) {
        ViewGroup viewGroup;
        FragmentManager fragmentManager;
        e eVar = this.Y;
        g gVar = null;
        if (eVar != null) {
            eVar.f5450w = false;
            g gVar2 = eVar.f5451x;
            eVar.f5451x = null;
            gVar = gVar2;
        }
        if (gVar != null) {
            gVar.b();
        } else if (!FragmentManager.P || this.T == null || (viewGroup = this.R) == null || (fragmentManager = this.f5420y) == null) {
        } else {
            x n8 = x.n(viewGroup, fragmentManager);
            n8.p();
            if (z4) {
                this.f5421z.g().post(new c(n8));
            } else {
                n8.g();
            }
        }
    }

    public final boolean e0() {
        FragmentManager fragmentManager;
        return this.P && ((fragmentManager = this.f5420y) == null || fragmentManager.H0(this.B));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e1() {
        this.A.T0();
        this.A.a0(true);
        this.f5389a = 7;
        this.Q = false;
        onResume();
        if (!this.Q) {
            throw new z("Fragment " + this + " did not call through to super.onResume()");
        }
        androidx.lifecycle.k kVar = this.f5402g0;
        Lifecycle.Event event = Lifecycle.Event.ON_RESUME;
        kVar.h(event);
        if (this.T != null) {
            this.f5404h0.a(event);
        }
        this.A.Q();
    }

    public final boolean equals(Object obj) {
        return super.equals(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.fragment.app.e f() {
        return new d();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean f0() {
        e eVar = this.Y;
        if (eVar == null) {
            return false;
        }
        return eVar.f5450w;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f1(Bundle bundle) {
        I0(bundle);
        this.f5409k0.e(bundle);
        Parcelable i12 = this.A.i1();
        if (i12 != null) {
            bundle.putParcelable("android:support:fragments", i12);
        }
    }

    public void g(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.print(str);
        printWriter.print("mFragmentId=#");
        printWriter.print(Integer.toHexString(this.C));
        printWriter.print(" mContainerId=#");
        printWriter.print(Integer.toHexString(this.E));
        printWriter.print(" mTag=");
        printWriter.println(this.F);
        printWriter.print(str);
        printWriter.print("mState=");
        printWriter.print(this.f5389a);
        printWriter.print(" mWho=");
        printWriter.print(this.f5399f);
        printWriter.print(" mBackStackNesting=");
        printWriter.println(this.f5419x);
        printWriter.print(str);
        printWriter.print("mAdded=");
        printWriter.print(this.f5412m);
        printWriter.print(" mRemoving=");
        printWriter.print(this.f5414n);
        printWriter.print(" mFromLayout=");
        printWriter.print(this.f5416p);
        printWriter.print(" mInLayout=");
        printWriter.println(this.q);
        printWriter.print(str);
        printWriter.print("mHidden=");
        printWriter.print(this.G);
        printWriter.print(" mDetached=");
        printWriter.print(this.H);
        printWriter.print(" mMenuVisible=");
        printWriter.print(this.P);
        printWriter.print(" mHasMenu=");
        printWriter.println(this.O);
        printWriter.print(str);
        printWriter.print("mRetainInstance=");
        printWriter.print(this.K);
        printWriter.print(" mUserVisibleHint=");
        printWriter.println(this.X);
        if (this.f5420y != null) {
            printWriter.print(str);
            printWriter.print("mFragmentManager=");
            printWriter.println(this.f5420y);
        }
        if (this.f5421z != null) {
            printWriter.print(str);
            printWriter.print("mHost=");
            printWriter.println(this.f5421z);
        }
        if (this.B != null) {
            printWriter.print(str);
            printWriter.print("mParentFragment=");
            printWriter.println(this.B);
        }
        if (this.f5401g != null) {
            printWriter.print(str);
            printWriter.print("mArguments=");
            printWriter.println(this.f5401g);
        }
        if (this.f5391b != null) {
            printWriter.print(str);
            printWriter.print("mSavedFragmentState=");
            printWriter.println(this.f5391b);
        }
        if (this.f5393c != null) {
            printWriter.print(str);
            printWriter.print("mSavedViewState=");
            printWriter.println(this.f5393c);
        }
        if (this.f5395d != null) {
            printWriter.print(str);
            printWriter.print("mSavedViewRegistryState=");
            printWriter.println(this.f5395d);
        }
        Fragment S = S();
        if (S != null) {
            printWriter.print(str);
            printWriter.print("mTarget=");
            printWriter.print(S);
            printWriter.print(" mTargetRequestCode=");
            printWriter.println(this.f5408k);
        }
        printWriter.print(str);
        printWriter.print("mPopDirection=");
        printWriter.println(F());
        if (r() != 0) {
            printWriter.print(str);
            printWriter.print("getEnterAnim=");
            printWriter.println(r());
        }
        if (u() != 0) {
            printWriter.print(str);
            printWriter.print("getExitAnim=");
            printWriter.println(u());
        }
        if (G() != 0) {
            printWriter.print(str);
            printWriter.print("getPopEnterAnim=");
            printWriter.println(G());
        }
        if (H() != 0) {
            printWriter.print(str);
            printWriter.print("getPopExitAnim=");
            printWriter.println(H());
        }
        if (this.R != null) {
            printWriter.print(str);
            printWriter.print("mContainer=");
            printWriter.println(this.R);
        }
        if (this.T != null) {
            printWriter.print(str);
            printWriter.print("mView=");
            printWriter.println(this.T);
        }
        if (n() != null) {
            printWriter.print(str);
            printWriter.print("mAnimatingAway=");
            printWriter.println(n());
        }
        if (getContext() != null) {
            androidx.loader.app.a.b(this).a(str, fileDescriptor, printWriter, strArr);
        }
        printWriter.print(str);
        printWriter.println("Child " + this.A + ":");
        FragmentManager fragmentManager = this.A;
        fragmentManager.W(str + "  ", fileDescriptor, printWriter, strArr);
    }

    public final boolean g0() {
        return this.f5414n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g1() {
        this.A.T0();
        this.A.a0(true);
        this.f5389a = 5;
        this.Q = false;
        onStart();
        if (!this.Q) {
            throw new z("Fragment " + this + " did not call through to super.onStart()");
        }
        androidx.lifecycle.k kVar = this.f5402g0;
        Lifecycle.Event event = Lifecycle.Event.ON_START;
        kVar.h(event);
        if (this.T != null) {
            this.f5404h0.a(event);
        }
        this.A.R();
    }

    public Context getContext() {
        h<?> hVar = this.f5421z;
        if (hVar == null) {
            return null;
        }
        return hVar.f();
    }

    @Override // androidx.lifecycle.e
    public f0.b getDefaultViewModelProviderFactory() {
        if (this.f5420y != null) {
            if (this.f5407j0 == null) {
                Application application = null;
                Context applicationContext = l1().getApplicationContext();
                while (true) {
                    if (!(applicationContext instanceof ContextWrapper)) {
                        break;
                    } else if (applicationContext instanceof Application) {
                        application = (Application) applicationContext;
                        break;
                    } else {
                        applicationContext = ((ContextWrapper) applicationContext).getBaseContext();
                    }
                }
                if (application == null && FragmentManager.F0(3)) {
                    Log.d("FragmentManager", "Could not find Application instance from Context " + l1().getApplicationContext() + ", you will not be able to use AndroidViewModel with the default ViewModelProvider.Factory");
                }
                this.f5407j0 = new a0(application, this, p());
            }
            return this.f5407j0;
        }
        throw new IllegalStateException("Can't access ViewModels from detached fragment");
    }

    @Override // androidx.lifecycle.j
    public Lifecycle getLifecycle() {
        return this.f5402g0;
    }

    @Override // s1.d
    public final androidx.savedstate.a getSavedStateRegistry() {
        return this.f5409k0.b();
    }

    @Override // androidx.lifecycle.j0
    public i0 getViewModelStore() {
        if (this.f5420y != null) {
            if (B() != Lifecycle.State.INITIALIZED.ordinal()) {
                return this.f5420y.A0(this);
            }
            throw new IllegalStateException("Calling getViewModelStore() before a Fragment reaches onCreate() when using setMaxLifecycle(INITIALIZED) is not supported");
        }
        throw new IllegalStateException("Can't access ViewModels from detached fragment");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean h0() {
        Fragment D = D();
        return D != null && (D.g0() || D.h0());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h1() {
        this.A.T();
        if (this.T != null) {
            this.f5404h0.a(Lifecycle.Event.ON_STOP);
        }
        this.f5402g0.h(Lifecycle.Event.ON_STOP);
        this.f5389a = 4;
        this.Q = false;
        onStop();
        if (this.Q) {
            return;
        }
        throw new z("Fragment " + this + " did not call through to super.onStop()");
    }

    public final int hashCode() {
        return super.hashCode();
    }

    public final boolean i0() {
        FragmentManager fragmentManager = this.f5420y;
        if (fragmentManager == null) {
            return false;
        }
        return fragmentManager.K0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i1() {
        J0(this.T, this.f5391b);
        this.A.U();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment j(String str) {
        return str.equals(this.f5399f) ? this : this.A.j0(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j0() {
        this.A.T0();
    }

    @Deprecated
    public final void j1(String[] strArr, int i8) {
        if (this.f5421z != null) {
            E().L0(this, strArr, i8);
            return;
        }
        throw new IllegalStateException("Fragment " + this + " not attached to Activity");
    }

    public final FragmentActivity k() {
        h<?> hVar = this.f5421z;
        if (hVar == null) {
            return null;
        }
        return (FragmentActivity) hVar.e();
    }

    @Deprecated
    public void k0(Bundle bundle) {
        this.Q = true;
    }

    public final FragmentActivity k1() {
        FragmentActivity k8 = k();
        if (k8 != null) {
            return k8;
        }
        throw new IllegalStateException("Fragment " + this + " not attached to an activity.");
    }

    public boolean l() {
        Boolean bool;
        e eVar = this.Y;
        if (eVar == null || (bool = eVar.f5445r) == null) {
            return true;
        }
        return bool.booleanValue();
    }

    @Deprecated
    public void l0(int i8, int i9, Intent intent) {
        if (FragmentManager.F0(2)) {
            Log.v("FragmentManager", "Fragment " + this + " received the following in onActivityResult(): requestCode: " + i8 + " resultCode: " + i9 + " data: " + intent);
        }
    }

    public final Context l1() {
        Context context = getContext();
        if (context != null) {
            return context;
        }
        throw new IllegalStateException("Fragment " + this + " not attached to a context.");
    }

    public boolean m() {
        Boolean bool;
        e eVar = this.Y;
        if (eVar == null || (bool = eVar.q) == null) {
            return true;
        }
        return bool.booleanValue();
    }

    @Deprecated
    public void m0(Activity activity) {
        this.Q = true;
    }

    public final View m1() {
        View T = T();
        if (T != null) {
            return T;
        }
        throw new IllegalStateException("Fragment " + this + " did not return a View from onCreateView() or this was called before onCreateView().");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View n() {
        e eVar = this.Y;
        if (eVar == null) {
            return null;
        }
        return eVar.f5429a;
    }

    public void n0(Context context) {
        this.Q = true;
        h<?> hVar = this.f5421z;
        Activity e8 = hVar == null ? null : hVar.e();
        if (e8 != null) {
            this.Q = false;
            m0(e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n1(Bundle bundle) {
        Parcelable parcelable;
        if (bundle == null || (parcelable = bundle.getParcelable("android:support:fragments")) == null) {
            return;
        }
        this.A.g1(parcelable);
        this.A.C();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Animator o() {
        e eVar = this.Y;
        if (eVar == null) {
            return null;
        }
        return eVar.f5430b;
    }

    @Deprecated
    public void o0(Fragment fragment) {
    }

    @Override // android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        this.Q = true;
    }

    public void onCreate(Bundle bundle) {
        this.Q = true;
        n1(bundle);
        if (this.A.J0(1)) {
            return;
        }
        this.A.C();
    }

    @Override // android.view.View.OnCreateContextMenuListener
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        k1().onCreateContextMenu(contextMenu, view, contextMenuInfo);
    }

    public void onDestroy() {
        this.Q = true;
    }

    @Override // android.content.ComponentCallbacks
    public void onLowMemory() {
        this.Q = true;
    }

    public void onPause() {
        this.Q = true;
    }

    public void onResume() {
        this.Q = true;
    }

    public void onStart() {
        this.Q = true;
    }

    public void onStop() {
        this.Q = true;
    }

    public final Bundle p() {
        return this.f5401g;
    }

    public boolean p0(MenuItem menuItem) {
        return false;
    }

    final void p1(Bundle bundle) {
        SparseArray<Parcelable> sparseArray = this.f5393c;
        if (sparseArray != null) {
            this.T.restoreHierarchyState(sparseArray);
            this.f5393c = null;
        }
        if (this.T != null) {
            this.f5404h0.d(this.f5395d);
            this.f5395d = null;
        }
        this.Q = false;
        K0(bundle);
        if (this.Q) {
            if (this.T != null) {
                this.f5404h0.a(Lifecycle.Event.ON_CREATE);
                return;
            }
            return;
        }
        throw new z("Fragment " + this + " did not call through to super.onViewStateRestored()");
    }

    public final FragmentManager q() {
        if (this.f5421z != null) {
            return this.A;
        }
        throw new IllegalStateException("Fragment " + this + " has not been attached yet.");
    }

    public Animation q0(int i8, boolean z4, int i9) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q1(View view) {
        i().f5429a = view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int r() {
        e eVar = this.Y;
        if (eVar == null) {
            return 0;
        }
        return eVar.f5432d;
    }

    public Animator r0(int i8, boolean z4, int i9) {
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r1(int i8, int i9, int i10, int i11) {
        if (this.Y == null && i8 == 0 && i9 == 0 && i10 == 0 && i11 == 0) {
            return;
        }
        i().f5432d = i8;
        i().f5433e = i9;
        i().f5434f = i10;
        i().f5435g = i11;
    }

    public Object s() {
        e eVar = this.Y;
        if (eVar == null) {
            return null;
        }
        return eVar.f5439k;
    }

    public void s0(Menu menu, MenuInflater menuInflater) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s1(Animator animator) {
        i().f5430b = animator;
    }

    @Deprecated
    public void startActivityForResult(@SuppressLint({"UnknownNullness"}) Intent intent, int i8) {
        G1(intent, i8, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.core.app.r t() {
        e eVar = this.Y;
        if (eVar == null) {
            return null;
        }
        return eVar.f5446s;
    }

    public View t0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i8 = this.f5411l0;
        if (i8 != 0) {
            return layoutInflater.inflate(i8, viewGroup, false);
        }
        return null;
    }

    public void t1(Bundle bundle) {
        if (this.f5420y != null && i0()) {
            throw new IllegalStateException("Fragment already added and state has been saved");
        }
        this.f5401g = bundle;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder((int) RecognitionOptions.ITF);
        sb.append(getClass().getSimpleName());
        sb.append("{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append("}");
        sb.append(" (");
        sb.append(this.f5399f);
        if (this.C != 0) {
            sb.append(" id=0x");
            sb.append(Integer.toHexString(this.C));
        }
        if (this.F != null) {
            sb.append(" tag=");
            sb.append(this.F);
        }
        sb.append(")");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int u() {
        e eVar = this.Y;
        if (eVar == null) {
            return 0;
        }
        return eVar.f5433e;
    }

    public void u0() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u1(View view) {
        i().f5449v = view;
    }

    public Object v() {
        e eVar = this.Y;
        if (eVar == null) {
            return null;
        }
        return eVar.f5441m;
    }

    public void v0() {
        this.Q = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v1(boolean z4) {
        i().f5452y = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.core.app.r w() {
        e eVar = this.Y;
        if (eVar == null) {
            return null;
        }
        return eVar.f5447t;
    }

    public void w0() {
        this.Q = true;
    }

    public void w1(boolean z4) {
        if (this.P != z4) {
            this.P = z4;
            if (this.O && Z() && !b0()) {
                this.f5421z.o();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View x() {
        e eVar = this.Y;
        if (eVar == null) {
            return null;
        }
        return eVar.f5449v;
    }

    public LayoutInflater x0(Bundle bundle) {
        return A(bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x1(int i8) {
        if (this.Y == null && i8 == 0) {
            return;
        }
        i();
        this.Y.f5436h = i8;
    }

    public final Object y() {
        h<?> hVar = this.f5421z;
        if (hVar == null) {
            return null;
        }
        return hVar.h();
    }

    public void y0(boolean z4) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y1(g gVar) {
        i();
        e eVar = this.Y;
        g gVar2 = eVar.f5451x;
        if (gVar == gVar2) {
            return;
        }
        if (gVar != null && gVar2 != null) {
            throw new IllegalStateException("Trying to set a replacement startPostponedEnterTransition on " + this);
        }
        if (eVar.f5450w) {
            eVar.f5451x = gVar;
        }
        if (gVar != null) {
            gVar.a();
        }
    }

    public final int z() {
        return this.C;
    }

    @Deprecated
    public void z0(Activity activity, AttributeSet attributeSet, Bundle bundle) {
        this.Q = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void z1(boolean z4) {
        if (this.Y == null) {
            return;
        }
        i().f5431c = z4;
    }
}
