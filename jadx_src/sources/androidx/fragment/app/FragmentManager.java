package androidx.fragment.app;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import androidx.activity.OnBackPressedDispatcher;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.IntentSenderRequest;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.d;
import androidx.fragment.app.r;
import androidx.fragment.app.s;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.i0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class FragmentManager {
    private static boolean O = false;
    static boolean P = true;
    private androidx.activity.result.b<IntentSenderRequest> A;
    private androidx.activity.result.b<String[]> B;
    private boolean D;
    private boolean E;
    private boolean F;
    private boolean G;
    private boolean H;
    private ArrayList<androidx.fragment.app.a> I;
    private ArrayList<Boolean> J;
    private ArrayList<Fragment> K;
    private ArrayList<p> L;
    private androidx.fragment.app.l M;

    /* renamed from: b  reason: collision with root package name */
    private boolean f5461b;

    /* renamed from: d  reason: collision with root package name */
    ArrayList<androidx.fragment.app.a> f5463d;

    /* renamed from: e  reason: collision with root package name */
    private ArrayList<Fragment> f5464e;

    /* renamed from: g  reason: collision with root package name */
    private OnBackPressedDispatcher f5466g;

    /* renamed from: l  reason: collision with root package name */
    private ArrayList<m> f5471l;

    /* renamed from: r  reason: collision with root package name */
    private androidx.fragment.app.h<?> f5476r;

    /* renamed from: s  reason: collision with root package name */
    private androidx.fragment.app.e f5477s;

    /* renamed from: t  reason: collision with root package name */
    private Fragment f5478t;

    /* renamed from: u  reason: collision with root package name */
    Fragment f5479u;

    /* renamed from: z  reason: collision with root package name */
    private androidx.activity.result.b<Intent> f5484z;

    /* renamed from: a  reason: collision with root package name */
    private final ArrayList<n> f5460a = new ArrayList<>();

    /* renamed from: c  reason: collision with root package name */
    private final q f5462c = new q();

    /* renamed from: f  reason: collision with root package name */
    private final androidx.fragment.app.i f5465f = new androidx.fragment.app.i(this);

    /* renamed from: h  reason: collision with root package name */
    private final androidx.activity.l f5467h = new c(false);

    /* renamed from: i  reason: collision with root package name */
    private final AtomicInteger f5468i = new AtomicInteger();

    /* renamed from: j  reason: collision with root package name */
    private final Map<String, Bundle> f5469j = Collections.synchronizedMap(new HashMap());

    /* renamed from: k  reason: collision with root package name */
    private final Map<String, Object> f5470k = Collections.synchronizedMap(new HashMap());

    /* renamed from: m  reason: collision with root package name */
    private Map<Fragment, HashSet<androidx.core.os.e>> f5472m = Collections.synchronizedMap(new HashMap());

    /* renamed from: n  reason: collision with root package name */
    private final s.g f5473n = new d();

    /* renamed from: o  reason: collision with root package name */
    private final androidx.fragment.app.j f5474o = new androidx.fragment.app.j(this);

    /* renamed from: p  reason: collision with root package name */
    private final CopyOnWriteArrayList<androidx.fragment.app.m> f5475p = new CopyOnWriteArrayList<>();
    int q = -1;

    /* renamed from: v  reason: collision with root package name */
    private androidx.fragment.app.g f5480v = null;

    /* renamed from: w  reason: collision with root package name */
    private androidx.fragment.app.g f5481w = new e();

    /* renamed from: x  reason: collision with root package name */
    private y f5482x = null;

    /* renamed from: y  reason: collision with root package name */
    private y f5483y = new f();
    ArrayDeque<LaunchedFragmentInfo> C = new ArrayDeque<>();
    private Runnable N = new g();

    /* renamed from: androidx.fragment.app.FragmentManager$6  reason: invalid class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class AnonymousClass6 implements androidx.lifecycle.h {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ String f5485a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ androidx.fragment.app.o f5486b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ Lifecycle f5487c;

        /* renamed from: d  reason: collision with root package name */
        final /* synthetic */ FragmentManager f5488d;

        @Override // androidx.lifecycle.h
        public void c(androidx.lifecycle.j jVar, Lifecycle.Event event) {
            Bundle bundle;
            if (event == Lifecycle.Event.ON_START && (bundle = (Bundle) this.f5488d.f5469j.get(this.f5485a)) != null) {
                this.f5486b.a(this.f5485a, bundle);
                this.f5488d.q(this.f5485a);
            }
            if (event == Lifecycle.Event.ON_DESTROY) {
                this.f5487c.c(this);
                this.f5488d.f5470k.remove(this.f5485a);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LaunchedFragmentInfo implements Parcelable {
        public static final Parcelable.Creator<LaunchedFragmentInfo> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        String f5489a;

        /* renamed from: b  reason: collision with root package name */
        int f5490b;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<LaunchedFragmentInfo> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public LaunchedFragmentInfo createFromParcel(Parcel parcel) {
                return new LaunchedFragmentInfo(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public LaunchedFragmentInfo[] newArray(int i8) {
                return new LaunchedFragmentInfo[i8];
            }
        }

        LaunchedFragmentInfo(Parcel parcel) {
            this.f5489a = parcel.readString();
            this.f5490b = parcel.readInt();
        }

        LaunchedFragmentInfo(String str, int i8) {
            this.f5489a = str;
            this.f5490b = i8;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeString(this.f5489a);
            parcel.writeInt(this.f5490b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements androidx.activity.result.a<ActivityResult> {
        a() {
        }

        @Override // androidx.activity.result.a
        /* renamed from: b */
        public void a(ActivityResult activityResult) {
            LaunchedFragmentInfo pollFirst = FragmentManager.this.C.pollFirst();
            if (pollFirst == null) {
                Log.w("FragmentManager", "No IntentSenders were started for " + this);
                return;
            }
            String str = pollFirst.f5489a;
            int i8 = pollFirst.f5490b;
            Fragment i9 = FragmentManager.this.f5462c.i(str);
            if (i9 != null) {
                i9.l0(i8, activityResult.b(), activityResult.a());
                return;
            }
            Log.w("FragmentManager", "Intent Sender result delivered for unknown Fragment " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements androidx.activity.result.a<Map<String, Boolean>> {
        b() {
        }

        @Override // androidx.activity.result.a
        @SuppressLint({"SyntheticAccessor"})
        /* renamed from: b */
        public void a(Map<String, Boolean> map) {
            StringBuilder sb;
            String[] strArr = (String[]) map.keySet().toArray(new String[0]);
            ArrayList arrayList = new ArrayList(map.values());
            int[] iArr = new int[arrayList.size()];
            for (int i8 = 0; i8 < arrayList.size(); i8++) {
                iArr[i8] = ((Boolean) arrayList.get(i8)).booleanValue() ? 0 : -1;
            }
            LaunchedFragmentInfo pollFirst = FragmentManager.this.C.pollFirst();
            if (pollFirst == null) {
                sb = new StringBuilder();
                sb.append("No permissions were requested for ");
                sb.append(this);
            } else {
                String str = pollFirst.f5489a;
                int i9 = pollFirst.f5490b;
                Fragment i10 = FragmentManager.this.f5462c.i(str);
                if (i10 != null) {
                    i10.H0(i9, strArr, iArr);
                    return;
                }
                sb = new StringBuilder();
                sb.append("Permission request result delivered for unknown Fragment ");
                sb.append(str);
            }
            Log.w("FragmentManager", sb.toString());
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends androidx.activity.l {
        c(boolean z4) {
            super(z4);
        }

        @Override // androidx.activity.l
        public void b() {
            FragmentManager.this.B0();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d implements s.g {
        d() {
        }

        @Override // androidx.fragment.app.s.g
        public void a(Fragment fragment, androidx.core.os.e eVar) {
            if (eVar.b()) {
                return;
            }
            FragmentManager.this.c1(fragment, eVar);
        }

        @Override // androidx.fragment.app.s.g
        public void b(Fragment fragment, androidx.core.os.e eVar) {
            FragmentManager.this.f(fragment, eVar);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e extends androidx.fragment.app.g {
        e() {
        }

        @Override // androidx.fragment.app.g
        public Fragment a(ClassLoader classLoader, String str) {
            return FragmentManager.this.t0().b(FragmentManager.this.t0().f(), str, null);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class f implements y {
        f() {
        }

        @Override // androidx.fragment.app.y
        public x a(ViewGroup viewGroup) {
            return new androidx.fragment.app.b(viewGroup);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class g implements Runnable {
        g() {
        }

        @Override // java.lang.Runnable
        public void run() {
            FragmentManager.this.a0(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ ViewGroup f5498a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ View f5499b;

        /* renamed from: c  reason: collision with root package name */
        final /* synthetic */ Fragment f5500c;

        h(ViewGroup viewGroup, View view, Fragment fragment) {
            this.f5498a = viewGroup;
            this.f5499b = view;
            this.f5500c = fragment;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            this.f5498a.endViewTransition(this.f5499b);
            animator.removeListener(this);
            Fragment fragment = this.f5500c;
            View view = fragment.T;
            if (view == null || !fragment.G) {
                return;
            }
            view.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class i implements androidx.fragment.app.m {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ Fragment f5502a;

        i(Fragment fragment) {
            this.f5502a = fragment;
        }

        @Override // androidx.fragment.app.m
        public void a(FragmentManager fragmentManager, Fragment fragment) {
            this.f5502a.o0(fragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class j implements androidx.activity.result.a<ActivityResult> {
        j() {
        }

        @Override // androidx.activity.result.a
        /* renamed from: b */
        public void a(ActivityResult activityResult) {
            LaunchedFragmentInfo pollFirst = FragmentManager.this.C.pollFirst();
            if (pollFirst == null) {
                Log.w("FragmentManager", "No Activities were started for result for " + this);
                return;
            }
            String str = pollFirst.f5489a;
            int i8 = pollFirst.f5490b;
            Fragment i9 = FragmentManager.this.f5462c.i(str);
            if (i9 != null) {
                i9.l0(i8, activityResult.b(), activityResult.a());
                return;
            }
            Log.w("FragmentManager", "Activity result delivered for unknown Fragment " + str);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class k extends f.a<IntentSenderRequest, ActivityResult> {
        k() {
        }

        @Override // f.a
        /* renamed from: d */
        public Intent a(Context context, IntentSenderRequest intentSenderRequest) {
            Bundle bundleExtra;
            Intent intent = new Intent("androidx.activity.result.contract.action.INTENT_SENDER_REQUEST");
            Intent a9 = intentSenderRequest.a();
            if (a9 != null && (bundleExtra = a9.getBundleExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE")) != null) {
                intent.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", bundleExtra);
                a9.removeExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE");
                if (a9.getBooleanExtra("androidx.fragment.extra.ACTIVITY_OPTIONS_BUNDLE", false)) {
                    intentSenderRequest = new IntentSenderRequest.a(intentSenderRequest.d()).b(null).c(intentSenderRequest.c(), intentSenderRequest.b()).a();
                }
            }
            intent.putExtra("androidx.activity.result.contract.extra.INTENT_SENDER_REQUEST", intentSenderRequest);
            if (FragmentManager.F0(2)) {
                Log.v("FragmentManager", "CreateIntent created the following intent: " + intent);
            }
            return intent;
        }

        @Override // f.a
        /* renamed from: e */
        public ActivityResult c(int i8, Intent intent) {
            return new ActivityResult(i8, intent);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class l {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface m {
        void a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface n {
        boolean a(ArrayList<androidx.fragment.app.a> arrayList, ArrayList<Boolean> arrayList2);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class o implements n {

        /* renamed from: a  reason: collision with root package name */
        final String f5505a;

        /* renamed from: b  reason: collision with root package name */
        final int f5506b;

        /* renamed from: c  reason: collision with root package name */
        final int f5507c;

        o(String str, int i8, int i9) {
            this.f5505a = str;
            this.f5506b = i8;
            this.f5507c = i9;
        }

        @Override // androidx.fragment.app.FragmentManager.n
        public boolean a(ArrayList<androidx.fragment.app.a> arrayList, ArrayList<Boolean> arrayList2) {
            Fragment fragment = FragmentManager.this.f5479u;
            if (fragment == null || this.f5506b >= 0 || this.f5505a != null || !fragment.q().Y0()) {
                return FragmentManager.this.a1(arrayList, arrayList2, this.f5505a, this.f5506b, this.f5507c);
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class p implements Fragment.g {

        /* renamed from: a  reason: collision with root package name */
        final boolean f5509a;

        /* renamed from: b  reason: collision with root package name */
        final androidx.fragment.app.a f5510b;

        /* renamed from: c  reason: collision with root package name */
        private int f5511c;

        p(androidx.fragment.app.a aVar, boolean z4) {
            this.f5509a = z4;
            this.f5510b = aVar;
        }

        @Override // androidx.fragment.app.Fragment.g
        public void a() {
            this.f5511c++;
        }

        @Override // androidx.fragment.app.Fragment.g
        public void b() {
            int i8 = this.f5511c - 1;
            this.f5511c = i8;
            if (i8 != 0) {
                return;
            }
            this.f5510b.f5545t.j1();
        }

        void c() {
            androidx.fragment.app.a aVar = this.f5510b;
            aVar.f5545t.t(aVar, this.f5509a, false, false);
        }

        void d() {
            boolean z4 = this.f5511c > 0;
            for (Fragment fragment : this.f5510b.f5545t.s0()) {
                fragment.y1(null);
                if (z4 && fragment.f0()) {
                    fragment.I1();
                }
            }
            androidx.fragment.app.a aVar = this.f5510b;
            aVar.f5545t.t(aVar, this.f5509a, !z4, true);
        }

        public boolean e() {
            return this.f5511c == 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean F0(int i8) {
        return O || Log.isLoggable("FragmentManager", i8);
    }

    private boolean G0(Fragment fragment) {
        return (fragment.O && fragment.P) || fragment.A.n();
    }

    private void L(Fragment fragment) {
        if (fragment == null || !fragment.equals(g0(fragment.f5399f))) {
            return;
        }
        fragment.d1();
    }

    private void O0(k0.b<Fragment> bVar) {
        int size = bVar.size();
        for (int i8 = 0; i8 < size; i8++) {
            Fragment q = bVar.q(i8);
            if (!q.f5412m) {
                View m12 = q.m1();
                q.f5394c0 = m12.getAlpha();
                m12.setAlpha(0.0f);
            }
        }
    }

    private void S(int i8) {
        try {
            this.f5461b = true;
            this.f5462c.d(i8);
            Q0(i8, false);
            if (P) {
                for (x xVar : r()) {
                    xVar.j();
                }
            }
            this.f5461b = false;
            a0(true);
        } catch (Throwable th) {
            this.f5461b = false;
            throw th;
        }
    }

    private void V() {
        if (this.H) {
            this.H = false;
            p1();
        }
    }

    private void X() {
        if (P) {
            for (x xVar : r()) {
                xVar.j();
            }
        } else if (!this.f5472m.isEmpty()) {
            for (Fragment fragment : this.f5472m.keySet()) {
                m(fragment);
                R0(fragment);
            }
        }
    }

    private void Z(boolean z4) {
        if (this.f5461b) {
            throw new IllegalStateException("FragmentManager is already executing transactions");
        }
        if (this.f5476r == null) {
            if (!this.G) {
                throw new IllegalStateException("FragmentManager has not been attached to a host.");
            }
            throw new IllegalStateException("FragmentManager has been destroyed");
        } else if (Looper.myLooper() != this.f5476r.g().getLooper()) {
            throw new IllegalStateException("Must be called from main thread of fragment host");
        } else {
            if (!z4) {
                o();
            }
            if (this.I == null) {
                this.I = new ArrayList<>();
                this.J = new ArrayList<>();
            }
            this.f5461b = true;
            try {
                f0(null, null);
            } finally {
                this.f5461b = false;
            }
        }
    }

    private boolean Z0(String str, int i8, int i9) {
        a0(false);
        Z(true);
        Fragment fragment = this.f5479u;
        if (fragment == null || i8 >= 0 || str != null || !fragment.q().Y0()) {
            boolean a12 = a1(this.I, this.J, str, i8, i9);
            if (a12) {
                this.f5461b = true;
                try {
                    e1(this.I, this.J);
                } finally {
                    p();
                }
            }
            q1();
            V();
            this.f5462c.b();
            return a12;
        }
        return true;
    }

    private int b1(ArrayList<androidx.fragment.app.a> arrayList, ArrayList<Boolean> arrayList2, int i8, int i9, k0.b<Fragment> bVar) {
        int i10 = i9;
        for (int i11 = i9 - 1; i11 >= i8; i11--) {
            androidx.fragment.app.a aVar = arrayList.get(i11);
            boolean booleanValue = arrayList2.get(i11).booleanValue();
            if (aVar.H() && !aVar.F(arrayList, i11 + 1, i9)) {
                if (this.L == null) {
                    this.L = new ArrayList<>();
                }
                p pVar = new p(aVar, booleanValue);
                this.L.add(pVar);
                aVar.J(pVar);
                if (booleanValue) {
                    aVar.A();
                } else {
                    aVar.B(false);
                }
                i10--;
                if (i11 != i10) {
                    arrayList.remove(i11);
                    arrayList.add(i10, aVar);
                }
                d(bVar);
            }
        }
        return i10;
    }

    private static void c0(ArrayList<androidx.fragment.app.a> arrayList, ArrayList<Boolean> arrayList2, int i8, int i9) {
        while (i8 < i9) {
            androidx.fragment.app.a aVar = arrayList.get(i8);
            if (arrayList2.get(i8).booleanValue()) {
                aVar.w(-1);
                aVar.B(i8 == i9 + (-1));
            } else {
                aVar.w(1);
                aVar.A();
            }
            i8++;
        }
    }

    private void d(k0.b<Fragment> bVar) {
        int i8 = this.q;
        if (i8 < 1) {
            return;
        }
        int min = Math.min(i8, 5);
        for (Fragment fragment : this.f5462c.n()) {
            if (fragment.f5389a < min) {
                S0(fragment, min);
                if (fragment.T != null && !fragment.G && fragment.f5390a0) {
                    bVar.add(fragment);
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:110:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00c5  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0143  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x019d  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x01be  */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4, types: [int, boolean] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void d0(java.util.ArrayList<androidx.fragment.app.a> r18, java.util.ArrayList<java.lang.Boolean> r19, int r20, int r21) {
        /*
            Method dump skipped, instructions count: 450
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentManager.d0(java.util.ArrayList, java.util.ArrayList, int, int):void");
    }

    private void e1(ArrayList<androidx.fragment.app.a> arrayList, ArrayList<Boolean> arrayList2) {
        if (arrayList.isEmpty()) {
            return;
        }
        if (arrayList.size() != arrayList2.size()) {
            throw new IllegalStateException("Internal error with the back stack records");
        }
        f0(arrayList, arrayList2);
        int size = arrayList.size();
        int i8 = 0;
        int i9 = 0;
        while (i8 < size) {
            if (!arrayList.get(i8).f5679r) {
                if (i9 != i8) {
                    d0(arrayList, arrayList2, i9, i8);
                }
                i9 = i8 + 1;
                if (arrayList2.get(i8).booleanValue()) {
                    while (i9 < size && arrayList2.get(i9).booleanValue() && !arrayList.get(i9).f5679r) {
                        i9++;
                    }
                }
                d0(arrayList, arrayList2, i8, i9);
                i8 = i9 - 1;
            }
            i8++;
        }
        if (i9 != size) {
            d0(arrayList, arrayList2, i9, size);
        }
    }

    private void f0(ArrayList<androidx.fragment.app.a> arrayList, ArrayList<Boolean> arrayList2) {
        int indexOf;
        int indexOf2;
        ArrayList<p> arrayList3 = this.L;
        int size = arrayList3 == null ? 0 : arrayList3.size();
        int i8 = 0;
        while (i8 < size) {
            p pVar = this.L.get(i8);
            if (arrayList == null || pVar.f5509a || (indexOf2 = arrayList.indexOf(pVar.f5510b)) == -1 || arrayList2 == null || !arrayList2.get(indexOf2).booleanValue()) {
                if (pVar.e() || (arrayList != null && pVar.f5510b.F(arrayList, 0, arrayList.size()))) {
                    this.L.remove(i8);
                    i8--;
                    size--;
                    if (arrayList == null || pVar.f5509a || (indexOf = arrayList.indexOf(pVar.f5510b)) == -1 || arrayList2 == null || !arrayList2.get(indexOf).booleanValue()) {
                        pVar.d();
                    }
                }
                i8++;
            } else {
                this.L.remove(i8);
                i8--;
                size--;
            }
            pVar.c();
            i8++;
        }
    }

    private void f1() {
        if (this.f5471l != null) {
            for (int i8 = 0; i8 < this.f5471l.size(); i8++) {
                this.f5471l.get(i8).a();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int h1(int i8) {
        if (i8 != 4097) {
            if (i8 != 4099) {
                return i8 != 8194 ? 0 : 4097;
            }
            return 4099;
        }
        return 8194;
    }

    private void k0() {
        if (P) {
            for (x xVar : r()) {
                xVar.k();
            }
        } else if (this.L != null) {
            while (!this.L.isEmpty()) {
                this.L.remove(0).d();
            }
        }
    }

    private boolean l0(ArrayList<androidx.fragment.app.a> arrayList, ArrayList<Boolean> arrayList2) {
        synchronized (this.f5460a) {
            if (this.f5460a.isEmpty()) {
                return false;
            }
            int size = this.f5460a.size();
            boolean z4 = false;
            for (int i8 = 0; i8 < size; i8++) {
                z4 |= this.f5460a.get(i8).a(arrayList, arrayList2);
            }
            this.f5460a.clear();
            this.f5476r.g().removeCallbacks(this.N);
            return z4;
        }
    }

    private void m(Fragment fragment) {
        HashSet<androidx.core.os.e> hashSet = this.f5472m.get(fragment);
        if (hashSet != null) {
            Iterator<androidx.core.os.e> it = hashSet.iterator();
            while (it.hasNext()) {
                it.next().a();
            }
            hashSet.clear();
            w(fragment);
            this.f5472m.remove(fragment);
        }
    }

    private androidx.fragment.app.l n0(Fragment fragment) {
        return this.M.i(fragment);
    }

    private void n1(Fragment fragment) {
        ViewGroup p02 = p0(fragment);
        if (p02 == null || fragment.r() + fragment.u() + fragment.G() + fragment.H() <= 0) {
            return;
        }
        int i8 = b1.b.f7949c;
        if (p02.getTag(i8) == null) {
            p02.setTag(i8, fragment);
        }
        ((Fragment) p02.getTag(i8)).z1(fragment.F());
    }

    private void o() {
        if (K0()) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState");
        }
    }

    private void p() {
        this.f5461b = false;
        this.J.clear();
        this.I.clear();
    }

    private ViewGroup p0(Fragment fragment) {
        ViewGroup viewGroup = fragment.R;
        if (viewGroup != null) {
            return viewGroup;
        }
        if (fragment.E > 0 && this.f5477s.d()) {
            View c9 = this.f5477s.c(fragment.E);
            if (c9 instanceof ViewGroup) {
                return (ViewGroup) c9;
            }
        }
        return null;
    }

    private void p1() {
        for (androidx.fragment.app.p pVar : this.f5462c.k()) {
            V0(pVar);
        }
    }

    private void q1() {
        synchronized (this.f5460a) {
            boolean z4 = true;
            if (this.f5460a.isEmpty()) {
                this.f5467h.f((m0() <= 0 || !I0(this.f5478t)) ? false : false);
            } else {
                this.f5467h.f(true);
            }
        }
    }

    private Set<x> r() {
        HashSet hashSet = new HashSet();
        for (androidx.fragment.app.p pVar : this.f5462c.k()) {
            ViewGroup viewGroup = pVar.k().R;
            if (viewGroup != null) {
                hashSet.add(x.o(viewGroup, y0()));
            }
        }
        return hashSet;
    }

    private Set<x> s(ArrayList<androidx.fragment.app.a> arrayList, int i8, int i9) {
        ViewGroup viewGroup;
        HashSet hashSet = new HashSet();
        while (i8 < i9) {
            Iterator<r.a> it = arrayList.get(i8).f5665c.iterator();
            while (it.hasNext()) {
                Fragment fragment = it.next().f5682b;
                if (fragment != null && (viewGroup = fragment.R) != null) {
                    hashSet.add(x.n(viewGroup, this));
                }
            }
            i8++;
        }
        return hashSet;
    }

    private void u(Fragment fragment) {
        Animator animator;
        if (fragment.T != null) {
            d.C0056d c9 = androidx.fragment.app.d.c(this.f5476r.f(), fragment, !fragment.G, fragment.F());
            if (c9 == null || (animator = c9.f5619b) == null) {
                if (c9 != null) {
                    fragment.T.startAnimation(c9.f5618a);
                    c9.f5618a.start();
                }
                fragment.T.setVisibility((!fragment.G || fragment.c0()) ? 0 : 8);
                if (fragment.c0()) {
                    fragment.v1(false);
                }
            } else {
                animator.setTarget(fragment.T);
                if (!fragment.G) {
                    fragment.T.setVisibility(0);
                } else if (fragment.c0()) {
                    fragment.v1(false);
                } else {
                    ViewGroup viewGroup = fragment.R;
                    View view = fragment.T;
                    viewGroup.startViewTransition(view);
                    c9.f5619b.addListener(new h(viewGroup, view, fragment));
                }
                c9.f5619b.start();
            }
        }
        D0(fragment);
        fragment.f5392b0 = false;
        fragment.y0(fragment.G);
    }

    private void w(Fragment fragment) {
        fragment.T0();
        this.f5474o.n(fragment, false);
        fragment.R = null;
        fragment.T = null;
        fragment.f5404h0 = null;
        fragment.f5405i0.o(null);
        fragment.q = false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Fragment z0(View view) {
        Object tag = view.getTag(b1.b.f7947a);
        if (tag instanceof Fragment) {
            return (Fragment) tag;
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void A(Configuration configuration) {
        for (Fragment fragment : this.f5462c.n()) {
            if (fragment != null) {
                fragment.N0(configuration);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i0 A0(Fragment fragment) {
        return this.M.l(fragment);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean B(MenuItem menuItem) {
        if (this.q < 1) {
            return false;
        }
        for (Fragment fragment : this.f5462c.n()) {
            if (fragment != null && fragment.O0(menuItem)) {
                return true;
            }
        }
        return false;
    }

    void B0() {
        a0(true);
        if (this.f5467h.c()) {
            Y0();
        } else {
            this.f5466g.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void C() {
        this.E = false;
        this.F = false;
        this.M.o(false);
        S(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void C0(Fragment fragment) {
        if (F0(2)) {
            Log.v("FragmentManager", "hide: " + fragment);
        }
        if (fragment.G) {
            return;
        }
        fragment.G = true;
        fragment.f5392b0 = true ^ fragment.f5392b0;
        n1(fragment);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean D(Menu menu, MenuInflater menuInflater) {
        if (this.q < 1) {
            return false;
        }
        ArrayList<Fragment> arrayList = null;
        boolean z4 = false;
        for (Fragment fragment : this.f5462c.n()) {
            if (fragment != null && H0(fragment) && fragment.Q0(menu, menuInflater)) {
                if (arrayList == null) {
                    arrayList = new ArrayList<>();
                }
                arrayList.add(fragment);
                z4 = true;
            }
        }
        if (this.f5464e != null) {
            for (int i8 = 0; i8 < this.f5464e.size(); i8++) {
                Fragment fragment2 = this.f5464e.get(i8);
                if (arrayList == null || !arrayList.contains(fragment2)) {
                    fragment2.u0();
                }
            }
        }
        this.f5464e = arrayList;
        return z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void D0(Fragment fragment) {
        if (fragment.f5412m && G0(fragment)) {
            this.D = true;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void E() {
        this.G = true;
        a0(true);
        X();
        S(-1);
        this.f5476r = null;
        this.f5477s = null;
        this.f5478t = null;
        if (this.f5466g != null) {
            this.f5467h.d();
            this.f5466g = null;
        }
        androidx.activity.result.b<Intent> bVar = this.f5484z;
        if (bVar != null) {
            bVar.c();
            this.A.c();
            this.B.c();
        }
    }

    public boolean E0() {
        return this.G;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void F() {
        S(1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void G() {
        for (Fragment fragment : this.f5462c.n()) {
            if (fragment != null) {
                fragment.W0();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void H(boolean z4) {
        for (Fragment fragment : this.f5462c.n()) {
            if (fragment != null) {
                fragment.X0(z4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean H0(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        return fragment.e0();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void I(Fragment fragment) {
        Iterator<androidx.fragment.app.m> it = this.f5475p.iterator();
        while (it.hasNext()) {
            it.next().a(this, fragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean I0(Fragment fragment) {
        if (fragment == null) {
            return true;
        }
        FragmentManager fragmentManager = fragment.f5420y;
        return fragment.equals(fragmentManager.x0()) && I0(fragmentManager.f5478t);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean J(MenuItem menuItem) {
        if (this.q < 1) {
            return false;
        }
        for (Fragment fragment : this.f5462c.n()) {
            if (fragment != null && fragment.Y0(menuItem)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean J0(int i8) {
        return this.q >= i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void K(Menu menu) {
        if (this.q < 1) {
            return;
        }
        for (Fragment fragment : this.f5462c.n()) {
            if (fragment != null) {
                fragment.Z0(menu);
            }
        }
    }

    public boolean K0() {
        return this.E || this.F;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void L0(Fragment fragment, String[] strArr, int i8) {
        if (this.B == null) {
            this.f5476r.j(fragment, strArr, i8);
            return;
        }
        this.C.addLast(new LaunchedFragmentInfo(fragment.f5399f, i8));
        this.B.a(strArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void M() {
        S(5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void M0(Fragment fragment, @SuppressLint({"UnknownNullness"}) Intent intent, int i8, Bundle bundle) {
        if (this.f5484z == null) {
            this.f5476r.m(fragment, intent, i8, bundle);
            return;
        }
        this.C.addLast(new LaunchedFragmentInfo(fragment.f5399f, i8));
        if (intent != null && bundle != null) {
            intent.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", bundle);
        }
        this.f5484z.a(intent);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void N(boolean z4) {
        for (Fragment fragment : this.f5462c.n()) {
            if (fragment != null) {
                fragment.b1(z4);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void N0(Fragment fragment, @SuppressLint({"UnknownNullness"}) IntentSender intentSender, int i8, Intent intent, int i9, int i10, int i11, Bundle bundle) {
        Intent intent2;
        if (this.A == null) {
            this.f5476r.n(fragment, intentSender, i8, intent, i9, i10, i11, bundle);
            return;
        }
        if (bundle != null) {
            if (intent == null) {
                intent2 = new Intent();
                intent2.putExtra("androidx.fragment.extra.ACTIVITY_OPTIONS_BUNDLE", true);
            } else {
                intent2 = intent;
            }
            if (F0(2)) {
                Log.v("FragmentManager", "ActivityOptions " + bundle + " were added to fillInIntent " + intent2 + " for fragment " + fragment);
            }
            intent2.putExtra("androidx.activity.result.contract.extra.ACTIVITY_OPTIONS_BUNDLE", bundle);
        } else {
            intent2 = intent;
        }
        IntentSenderRequest a9 = new IntentSenderRequest.a(intentSender).b(intent2).c(i10, i9).a();
        this.C.addLast(new LaunchedFragmentInfo(fragment.f5399f, i8));
        if (F0(2)) {
            Log.v("FragmentManager", "Fragment " + fragment + "is launching an IntentSender for result ");
        }
        this.A.a(a9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean O(Menu menu) {
        boolean z4 = false;
        if (this.q < 1) {
            return false;
        }
        for (Fragment fragment : this.f5462c.n()) {
            if (fragment != null && H0(fragment) && fragment.c1(menu)) {
                z4 = true;
            }
        }
        return z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void P() {
        q1();
        L(this.f5479u);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void P0(Fragment fragment) {
        if (!this.f5462c.c(fragment.f5399f)) {
            if (F0(3)) {
                Log.d("FragmentManager", "Ignoring moving " + fragment + " to state " + this.q + "since it is not added to " + this);
                return;
            }
            return;
        }
        R0(fragment);
        View view = fragment.T;
        if (view != null && fragment.f5390a0 && fragment.R != null) {
            float f5 = fragment.f5394c0;
            if (f5 > 0.0f) {
                view.setAlpha(f5);
            }
            fragment.f5394c0 = 0.0f;
            fragment.f5390a0 = false;
            d.C0056d c9 = androidx.fragment.app.d.c(this.f5476r.f(), fragment, true, fragment.F());
            if (c9 != null) {
                Animation animation = c9.f5618a;
                if (animation != null) {
                    fragment.T.startAnimation(animation);
                } else {
                    c9.f5619b.setTarget(fragment.T);
                    c9.f5619b.start();
                }
            }
        }
        if (fragment.f5392b0) {
            u(fragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Q() {
        this.E = false;
        this.F = false;
        this.M.o(false);
        S(7);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Q0(int i8, boolean z4) {
        androidx.fragment.app.h<?> hVar;
        if (this.f5476r == null && i8 != -1) {
            throw new IllegalStateException("No activity");
        }
        if (z4 || i8 != this.q) {
            this.q = i8;
            if (P) {
                this.f5462c.r();
            } else {
                for (Fragment fragment : this.f5462c.n()) {
                    P0(fragment);
                }
                for (androidx.fragment.app.p pVar : this.f5462c.k()) {
                    Fragment k8 = pVar.k();
                    if (!k8.f5390a0) {
                        P0(k8);
                    }
                    if (k8.f5414n && !k8.d0()) {
                        this.f5462c.q(pVar);
                    }
                }
            }
            p1();
            if (this.D && (hVar = this.f5476r) != null && this.q == 7) {
                hVar.o();
                this.D = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void R() {
        this.E = false;
        this.F = false;
        this.M.o(false);
        S(5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void R0(Fragment fragment) {
        S0(fragment, this.q);
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x0053, code lost:
        if (r2 != 5) goto L26;
     */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x006d  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0072  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x0077  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0160  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void S0(androidx.fragment.app.Fragment r11, int r12) {
        /*
            Method dump skipped, instructions count: 407
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentManager.S0(androidx.fragment.app.Fragment, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void T() {
        this.F = true;
        this.M.o(true);
        S(4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void T0() {
        if (this.f5476r == null) {
            return;
        }
        this.E = false;
        this.F = false;
        this.M.o(false);
        for (Fragment fragment : this.f5462c.n()) {
            if (fragment != null) {
                fragment.j0();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void U() {
        S(2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void U0(FragmentContainerView fragmentContainerView) {
        View view;
        for (androidx.fragment.app.p pVar : this.f5462c.k()) {
            Fragment k8 = pVar.k();
            if (k8.E == fragmentContainerView.getId() && (view = k8.T) != null && view.getParent() == null) {
                k8.R = fragmentContainerView;
                pVar.b();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void V0(androidx.fragment.app.p pVar) {
        Fragment k8 = pVar.k();
        if (k8.W) {
            if (this.f5461b) {
                this.H = true;
                return;
            }
            k8.W = false;
            if (P) {
                pVar.m();
            } else {
                R0(k8);
            }
        }
    }

    public void W(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        int size;
        int size2;
        String str2 = str + "    ";
        this.f5462c.e(str, fileDescriptor, printWriter, strArr);
        ArrayList<Fragment> arrayList = this.f5464e;
        if (arrayList != null && (size2 = arrayList.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Fragments Created Menus:");
            for (int i8 = 0; i8 < size2; i8++) {
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i8);
                printWriter.print(": ");
                printWriter.println(this.f5464e.get(i8).toString());
            }
        }
        ArrayList<androidx.fragment.app.a> arrayList2 = this.f5463d;
        if (arrayList2 != null && (size = arrayList2.size()) > 0) {
            printWriter.print(str);
            printWriter.println("Back Stack:");
            for (int i9 = 0; i9 < size; i9++) {
                androidx.fragment.app.a aVar = this.f5463d.get(i9);
                printWriter.print(str);
                printWriter.print("  #");
                printWriter.print(i9);
                printWriter.print(": ");
                printWriter.println(aVar.toString());
                aVar.y(str2, printWriter);
            }
        }
        printWriter.print(str);
        printWriter.println("Back Stack Index: " + this.f5468i.get());
        synchronized (this.f5460a) {
            int size3 = this.f5460a.size();
            if (size3 > 0) {
                printWriter.print(str);
                printWriter.println("Pending Actions:");
                for (int i10 = 0; i10 < size3; i10++) {
                    printWriter.print(str);
                    printWriter.print("  #");
                    printWriter.print(i10);
                    printWriter.print(": ");
                    printWriter.println(this.f5460a.get(i10));
                }
            }
        }
        printWriter.print(str);
        printWriter.println("FragmentManager misc state:");
        printWriter.print(str);
        printWriter.print("  mHost=");
        printWriter.println(this.f5476r);
        printWriter.print(str);
        printWriter.print("  mContainer=");
        printWriter.println(this.f5477s);
        if (this.f5478t != null) {
            printWriter.print(str);
            printWriter.print("  mParent=");
            printWriter.println(this.f5478t);
        }
        printWriter.print(str);
        printWriter.print("  mCurState=");
        printWriter.print(this.q);
        printWriter.print(" mStateSaved=");
        printWriter.print(this.E);
        printWriter.print(" mStopped=");
        printWriter.print(this.F);
        printWriter.print(" mDestroyed=");
        printWriter.println(this.G);
        if (this.D) {
            printWriter.print(str);
            printWriter.print("  mNeedMenuInvalidate=");
            printWriter.println(this.D);
        }
    }

    public void W0(int i8, int i9) {
        if (i8 >= 0) {
            Y(new o(null, i8, i9), false);
            return;
        }
        throw new IllegalArgumentException("Bad id: " + i8);
    }

    public void X0(String str, int i8) {
        Y(new o(str, -1, i8), false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void Y(n nVar, boolean z4) {
        if (!z4) {
            if (this.f5476r == null) {
                if (!this.G) {
                    throw new IllegalStateException("FragmentManager has not been attached to a host.");
                }
                throw new IllegalStateException("FragmentManager has been destroyed");
            }
            o();
        }
        synchronized (this.f5460a) {
            if (this.f5476r == null) {
                if (!z4) {
                    throw new IllegalStateException("Activity has been destroyed");
                }
                return;
            }
            this.f5460a.add(nVar);
            j1();
        }
    }

    public boolean Y0() {
        return Z0(null, -1, 0);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean a0(boolean z4) {
        Z(z4);
        boolean z8 = false;
        while (l0(this.I, this.J)) {
            this.f5461b = true;
            try {
                e1(this.I, this.J);
                p();
                z8 = true;
            } catch (Throwable th) {
                p();
                throw th;
            }
        }
        q1();
        V();
        this.f5462c.b();
        return z8;
    }

    boolean a1(ArrayList<androidx.fragment.app.a> arrayList, ArrayList<Boolean> arrayList2, String str, int i8, int i9) {
        int i10;
        ArrayList<androidx.fragment.app.a> arrayList3 = this.f5463d;
        if (arrayList3 == null) {
            return false;
        }
        if (str == null && i8 < 0 && (i9 & 1) == 0) {
            int size = arrayList3.size() - 1;
            if (size < 0) {
                return false;
            }
            arrayList.add(this.f5463d.remove(size));
            arrayList2.add(Boolean.TRUE);
        } else {
            if (str != null || i8 >= 0) {
                int size2 = arrayList3.size() - 1;
                while (size2 >= 0) {
                    androidx.fragment.app.a aVar = this.f5463d.get(size2);
                    if ((str != null && str.equals(aVar.D())) || (i8 >= 0 && i8 == aVar.f5547v)) {
                        break;
                    }
                    size2--;
                }
                if (size2 < 0) {
                    return false;
                }
                if ((i9 & 1) != 0) {
                    while (true) {
                        size2--;
                        if (size2 < 0) {
                            break;
                        }
                        androidx.fragment.app.a aVar2 = this.f5463d.get(size2);
                        if (str == null || !str.equals(aVar2.D())) {
                            if (i8 < 0 || i8 != aVar2.f5547v) {
                                break;
                            }
                        }
                    }
                }
                i10 = size2;
            } else {
                i10 = -1;
            }
            if (i10 == this.f5463d.size() - 1) {
                return false;
            }
            for (int size3 = this.f5463d.size() - 1; size3 > i10; size3--) {
                arrayList.add(this.f5463d.remove(size3));
                arrayList2.add(Boolean.TRUE);
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b0(n nVar, boolean z4) {
        if (z4 && (this.f5476r == null || this.G)) {
            return;
        }
        Z(z4);
        if (nVar.a(this.I, this.J)) {
            this.f5461b = true;
            try {
                e1(this.I, this.J);
            } finally {
                p();
            }
        }
        q1();
        V();
        this.f5462c.b();
    }

    void c1(Fragment fragment, androidx.core.os.e eVar) {
        HashSet<androidx.core.os.e> hashSet = this.f5472m.get(fragment);
        if (hashSet != null && hashSet.remove(eVar) && hashSet.isEmpty()) {
            this.f5472m.remove(fragment);
            if (fragment.f5389a < 5) {
                w(fragment);
                R0(fragment);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d1(Fragment fragment) {
        if (F0(2)) {
            Log.v("FragmentManager", "remove: " + fragment + " nesting=" + fragment.f5419x);
        }
        boolean z4 = !fragment.d0();
        if (!fragment.H || z4) {
            this.f5462c.s(fragment);
            if (G0(fragment)) {
                this.D = true;
            }
            fragment.f5414n = true;
            n1(fragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(androidx.fragment.app.a aVar) {
        if (this.f5463d == null) {
            this.f5463d = new ArrayList<>();
        }
        this.f5463d.add(aVar);
    }

    public boolean e0() {
        boolean a02 = a0(true);
        k0();
        return a02;
    }

    void f(Fragment fragment, androidx.core.os.e eVar) {
        if (this.f5472m.get(fragment) == null) {
            this.f5472m.put(fragment, new HashSet<>());
        }
        this.f5472m.get(fragment).add(eVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.fragment.app.p g(Fragment fragment) {
        if (F0(2)) {
            Log.v("FragmentManager", "add: " + fragment);
        }
        androidx.fragment.app.p v8 = v(fragment);
        fragment.f5420y = this;
        this.f5462c.p(v8);
        if (!fragment.H) {
            this.f5462c.a(fragment);
            fragment.f5414n = false;
            if (fragment.T == null) {
                fragment.f5392b0 = false;
            }
            if (G0(fragment)) {
                this.D = true;
            }
        }
        return v8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment g0(String str) {
        return this.f5462c.f(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g1(Parcelable parcelable) {
        androidx.fragment.app.p pVar;
        if (parcelable == null) {
            return;
        }
        FragmentManagerState fragmentManagerState = (FragmentManagerState) parcelable;
        if (fragmentManagerState.f5512a == null) {
            return;
        }
        this.f5462c.t();
        Iterator<FragmentState> it = fragmentManagerState.f5512a.iterator();
        while (it.hasNext()) {
            FragmentState next = it.next();
            if (next != null) {
                Fragment h8 = this.M.h(next.f5521b);
                if (h8 != null) {
                    if (F0(2)) {
                        Log.v("FragmentManager", "restoreSaveState: re-attaching retained " + h8);
                    }
                    pVar = new androidx.fragment.app.p(this.f5474o, this.f5462c, h8, next);
                } else {
                    pVar = new androidx.fragment.app.p(this.f5474o, this.f5462c, this.f5476r.f().getClassLoader(), q0(), next);
                }
                Fragment k8 = pVar.k();
                k8.f5420y = this;
                if (F0(2)) {
                    Log.v("FragmentManager", "restoreSaveState: active (" + k8.f5399f + "): " + k8);
                }
                pVar.o(this.f5476r.f().getClassLoader());
                this.f5462c.p(pVar);
                pVar.t(this.q);
            }
        }
        for (Fragment fragment : this.M.k()) {
            if (!this.f5462c.c(fragment.f5399f)) {
                if (F0(2)) {
                    Log.v("FragmentManager", "Discarding retained Fragment " + fragment + " that was not found in the set of active Fragments " + fragmentManagerState.f5512a);
                }
                this.M.n(fragment);
                fragment.f5420y = this;
                androidx.fragment.app.p pVar2 = new androidx.fragment.app.p(this.f5474o, this.f5462c, fragment);
                pVar2.t(1);
                pVar2.m();
                fragment.f5414n = true;
                pVar2.m();
            }
        }
        this.f5462c.u(fragmentManagerState.f5513b);
        if (fragmentManagerState.f5514c != null) {
            this.f5463d = new ArrayList<>(fragmentManagerState.f5514c.length);
            int i8 = 0;
            while (true) {
                BackStackState[] backStackStateArr = fragmentManagerState.f5514c;
                if (i8 >= backStackStateArr.length) {
                    break;
                }
                androidx.fragment.app.a a9 = backStackStateArr[i8].a(this);
                if (F0(2)) {
                    Log.v("FragmentManager", "restoreAllState: back stack #" + i8 + " (index " + a9.f5547v + "): " + a9);
                    PrintWriter printWriter = new PrintWriter(new w("FragmentManager"));
                    a9.z("  ", printWriter, false);
                    printWriter.close();
                }
                this.f5463d.add(a9);
                i8++;
            }
        } else {
            this.f5463d = null;
        }
        this.f5468i.set(fragmentManagerState.f5515d);
        String str = fragmentManagerState.f5516e;
        if (str != null) {
            Fragment g02 = g0(str);
            this.f5479u = g02;
            L(g02);
        }
        ArrayList<String> arrayList = fragmentManagerState.f5517f;
        if (arrayList != null) {
            for (int i9 = 0; i9 < arrayList.size(); i9++) {
                Bundle bundle = fragmentManagerState.f5518g.get(i9);
                bundle.setClassLoader(this.f5476r.f().getClassLoader());
                this.f5469j.put(arrayList.get(i9), bundle);
            }
        }
        this.C = new ArrayDeque<>(fragmentManagerState.f5519h);
    }

    public void h(androidx.fragment.app.m mVar) {
        this.f5475p.add(mVar);
    }

    public Fragment h0(int i8) {
        return this.f5462c.g(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int i() {
        return this.f5468i.getAndIncrement();
    }

    public Fragment i0(String str) {
        return this.f5462c.h(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Parcelable i1() {
        int size;
        k0();
        X();
        a0(true);
        this.E = true;
        this.M.o(true);
        ArrayList<FragmentState> v8 = this.f5462c.v();
        BackStackState[] backStackStateArr = null;
        if (v8.isEmpty()) {
            if (F0(2)) {
                Log.v("FragmentManager", "saveAllState: no fragments!");
            }
            return null;
        }
        ArrayList<String> w8 = this.f5462c.w();
        ArrayList<androidx.fragment.app.a> arrayList = this.f5463d;
        if (arrayList != null && (size = arrayList.size()) > 0) {
            backStackStateArr = new BackStackState[size];
            for (int i8 = 0; i8 < size; i8++) {
                backStackStateArr[i8] = new BackStackState(this.f5463d.get(i8));
                if (F0(2)) {
                    Log.v("FragmentManager", "saveAllState: adding back stack #" + i8 + ": " + this.f5463d.get(i8));
                }
            }
        }
        FragmentManagerState fragmentManagerState = new FragmentManagerState();
        fragmentManagerState.f5512a = v8;
        fragmentManagerState.f5513b = w8;
        fragmentManagerState.f5514c = backStackStateArr;
        fragmentManagerState.f5515d = this.f5468i.get();
        Fragment fragment = this.f5479u;
        if (fragment != null) {
            fragmentManagerState.f5516e = fragment.f5399f;
        }
        fragmentManagerState.f5517f.addAll(this.f5469j.keySet());
        fragmentManagerState.f5518g.addAll(this.f5469j.values());
        fragmentManagerState.f5519h = new ArrayList<>(this.C);
        return fragmentManagerState;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:13:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0028  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x003b  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:37:? A[RETURN, SYNTHETIC] */
    @android.annotation.SuppressLint({"SyntheticAccessor"})
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void j(androidx.fragment.app.h<?> r3, androidx.fragment.app.e r4, androidx.fragment.app.Fragment r5) {
        /*
            Method dump skipped, instructions count: 267
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.fragment.app.FragmentManager.j(androidx.fragment.app.h, androidx.fragment.app.e, androidx.fragment.app.Fragment):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment j0(String str) {
        return this.f5462c.i(str);
    }

    void j1() {
        synchronized (this.f5460a) {
            ArrayList<p> arrayList = this.L;
            boolean z4 = (arrayList == null || arrayList.isEmpty()) ? false : true;
            boolean z8 = this.f5460a.size() == 1;
            if (z4 || z8) {
                this.f5476r.g().removeCallbacks(this.N);
                this.f5476r.g().post(this.N);
                q1();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k(Fragment fragment) {
        if (F0(2)) {
            Log.v("FragmentManager", "attach: " + fragment);
        }
        if (fragment.H) {
            fragment.H = false;
            if (fragment.f5412m) {
                return;
            }
            this.f5462c.a(fragment);
            if (F0(2)) {
                Log.v("FragmentManager", "add from attach: " + fragment);
            }
            if (G0(fragment)) {
                this.D = true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k1(Fragment fragment, boolean z4) {
        ViewGroup p02 = p0(fragment);
        if (p02 == null || !(p02 instanceof FragmentContainerView)) {
            return;
        }
        ((FragmentContainerView) p02).setDrawDisappearingViewsLast(!z4);
    }

    public r l() {
        return new androidx.fragment.app.a(this);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l1(Fragment fragment, Lifecycle.State state) {
        if (fragment.equals(g0(fragment.f5399f)) && (fragment.f5421z == null || fragment.f5420y == this)) {
            fragment.f5400f0 = state;
            return;
        }
        throw new IllegalArgumentException("Fragment " + fragment + " is not an active fragment of FragmentManager " + this);
    }

    public int m0() {
        ArrayList<androidx.fragment.app.a> arrayList = this.f5463d;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void m1(Fragment fragment) {
        if (fragment == null || (fragment.equals(g0(fragment.f5399f)) && (fragment.f5421z == null || fragment.f5420y == this))) {
            Fragment fragment2 = this.f5479u;
            this.f5479u = fragment;
            L(fragment2);
            L(this.f5479u);
            return;
        }
        throw new IllegalArgumentException("Fragment " + fragment + " is not an active fragment of FragmentManager " + this);
    }

    boolean n() {
        boolean z4 = false;
        for (Fragment fragment : this.f5462c.l()) {
            if (fragment != null) {
                z4 = G0(fragment);
                continue;
            }
            if (z4) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.fragment.app.e o0() {
        return this.f5477s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o1(Fragment fragment) {
        if (F0(2)) {
            Log.v("FragmentManager", "show: " + fragment);
        }
        if (fragment.G) {
            fragment.G = false;
            fragment.f5392b0 = !fragment.f5392b0;
        }
    }

    public final void q(String str) {
        this.f5469j.remove(str);
    }

    public androidx.fragment.app.g q0() {
        androidx.fragment.app.g gVar = this.f5480v;
        if (gVar != null) {
            return gVar;
        }
        Fragment fragment = this.f5478t;
        return fragment != null ? fragment.f5420y.q0() : this.f5481w;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public q r0() {
        return this.f5462c;
    }

    public List<Fragment> s0() {
        return this.f5462c.n();
    }

    void t(androidx.fragment.app.a aVar, boolean z4, boolean z8, boolean z9) {
        if (z4) {
            aVar.B(z9);
        } else {
            aVar.A();
        }
        ArrayList arrayList = new ArrayList(1);
        ArrayList arrayList2 = new ArrayList(1);
        arrayList.add(aVar);
        arrayList2.add(Boolean.valueOf(z4));
        if (z8 && this.q >= 1) {
            s.B(this.f5476r.f(), this.f5477s, arrayList, arrayList2, 0, 1, true, this.f5473n);
        }
        if (z9) {
            Q0(this.q, true);
        }
        for (Fragment fragment : this.f5462c.l()) {
            if (fragment != null && fragment.T != null && fragment.f5390a0 && aVar.E(fragment.E)) {
                float f5 = fragment.f5394c0;
                if (f5 > 0.0f) {
                    fragment.T.setAlpha(f5);
                }
                if (z9) {
                    fragment.f5394c0 = 0.0f;
                } else {
                    fragment.f5394c0 = -1.0f;
                    fragment.f5390a0 = false;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.fragment.app.h<?> t0() {
        return this.f5476r;
    }

    public String toString() {
        Object obj;
        StringBuilder sb = new StringBuilder((int) RecognitionOptions.ITF);
        sb.append("FragmentManager{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(" in ");
        Fragment fragment = this.f5478t;
        if (fragment != null) {
            sb.append(fragment.getClass().getSimpleName());
            sb.append("{");
            obj = this.f5478t;
        } else {
            androidx.fragment.app.h<?> hVar = this.f5476r;
            if (hVar == null) {
                sb.append("null");
                sb.append("}}");
                return sb.toString();
            }
            sb.append(hVar.getClass().getSimpleName());
            sb.append("{");
            obj = this.f5476r;
        }
        sb.append(Integer.toHexString(System.identityHashCode(obj)));
        sb.append("}");
        sb.append("}}");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public LayoutInflater.Factory2 u0() {
        return this.f5465f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.fragment.app.p v(Fragment fragment) {
        androidx.fragment.app.p m8 = this.f5462c.m(fragment.f5399f);
        if (m8 != null) {
            return m8;
        }
        androidx.fragment.app.p pVar = new androidx.fragment.app.p(this.f5474o, this.f5462c, fragment);
        pVar.o(this.f5476r.f().getClassLoader());
        pVar.t(this.q);
        return pVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.fragment.app.j v0() {
        return this.f5474o;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment w0() {
        return this.f5478t;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void x(Fragment fragment) {
        if (F0(2)) {
            Log.v("FragmentManager", "detach: " + fragment);
        }
        if (fragment.H) {
            return;
        }
        fragment.H = true;
        if (fragment.f5412m) {
            if (F0(2)) {
                Log.v("FragmentManager", "remove from detach: " + fragment);
            }
            this.f5462c.s(fragment);
            if (G0(fragment)) {
                this.D = true;
            }
            n1(fragment);
        }
    }

    public Fragment x0() {
        return this.f5479u;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y() {
        this.E = false;
        this.F = false;
        this.M.o(false);
        S(4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public y y0() {
        y yVar = this.f5482x;
        if (yVar != null) {
            return yVar;
        }
        Fragment fragment = this.f5478t;
        return fragment != null ? fragment.f5420y.y0() : this.f5483y;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void z() {
        this.E = false;
        this.F = false;
        this.M.o(false);
        S(0);
    }
}
