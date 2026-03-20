package androidx.fragment.app;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.e;
import androidx.core.view.c0;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class x {

    /* renamed from: a  reason: collision with root package name */
    private final ViewGroup f5767a;

    /* renamed from: b  reason: collision with root package name */
    final ArrayList<e> f5768b = new ArrayList<>();

    /* renamed from: c  reason: collision with root package name */
    final ArrayList<e> f5769c = new ArrayList<>();

    /* renamed from: d  reason: collision with root package name */
    boolean f5770d = false;

    /* renamed from: e  reason: collision with root package name */
    boolean f5771e = false;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ d f5772a;

        a(d dVar) {
            this.f5772a = dVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (x.this.f5768b.contains(this.f5772a)) {
                this.f5772a.e().c(this.f5772a.f().T);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ d f5774a;

        b(d dVar) {
            this.f5774a = dVar;
        }

        @Override // java.lang.Runnable
        public void run() {
            x.this.f5768b.remove(this.f5774a);
            x.this.f5769c.remove(this.f5774a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class c {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f5776a;

        /* renamed from: b  reason: collision with root package name */
        static final /* synthetic */ int[] f5777b;

        static {
            int[] iArr = new int[e.b.values().length];
            f5777b = iArr;
            try {
                iArr[e.b.ADDING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f5777b[e.b.REMOVING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f5777b[e.b.NONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[e.c.values().length];
            f5776a = iArr2;
            try {
                iArr2[e.c.REMOVED.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f5776a[e.c.VISIBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                f5776a[e.c.GONE.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                f5776a[e.c.INVISIBLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d extends e {

        /* renamed from: h  reason: collision with root package name */
        private final p f5778h;

        d(e.c cVar, e.b bVar, p pVar, androidx.core.os.e eVar) {
            super(cVar, bVar, pVar.k(), eVar);
            this.f5778h = pVar;
        }

        @Override // androidx.fragment.app.x.e
        public void c() {
            super.c();
            this.f5778h.m();
        }

        @Override // androidx.fragment.app.x.e
        void l() {
            if (g() == e.b.ADDING) {
                Fragment k8 = this.f5778h.k();
                View findFocus = k8.T.findFocus();
                if (findFocus != null) {
                    k8.u1(findFocus);
                    if (FragmentManager.F0(2)) {
                        Log.v("FragmentManager", "requestFocus: Saved focused view " + findFocus + " for Fragment " + k8);
                    }
                }
                View m12 = f().m1();
                if (m12.getParent() == null) {
                    this.f5778h.b();
                    m12.setAlpha(0.0f);
                }
                if (m12.getAlpha() == 0.0f && m12.getVisibility() == 0) {
                    m12.setVisibility(4);
                }
                m12.setAlpha(k8.I());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e {

        /* renamed from: a  reason: collision with root package name */
        private c f5779a;

        /* renamed from: b  reason: collision with root package name */
        private b f5780b;

        /* renamed from: c  reason: collision with root package name */
        private final Fragment f5781c;

        /* renamed from: d  reason: collision with root package name */
        private final List<Runnable> f5782d = new ArrayList();

        /* renamed from: e  reason: collision with root package name */
        private final HashSet<androidx.core.os.e> f5783e = new HashSet<>();

        /* renamed from: f  reason: collision with root package name */
        private boolean f5784f = false;

        /* renamed from: g  reason: collision with root package name */
        private boolean f5785g = false;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements e.b {
            a() {
            }

            @Override // androidx.core.os.e.b
            public void a() {
                e.this.b();
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public enum b {
            NONE,
            ADDING,
            REMOVING
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public enum c {
            REMOVED,
            VISIBLE,
            GONE,
            INVISIBLE;

            /* JADX INFO: Access modifiers changed from: package-private */
            public static c f(int i8) {
                if (i8 != 0) {
                    if (i8 != 4) {
                        if (i8 == 8) {
                            return GONE;
                        }
                        throw new IllegalArgumentException("Unknown visibility " + i8);
                    }
                    return INVISIBLE;
                }
                return VISIBLE;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public static c h(View view) {
                return (view.getAlpha() == 0.0f && view.getVisibility() == 0) ? INVISIBLE : f(view.getVisibility());
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            public void c(View view) {
                int i8;
                int i9 = c.f5776a[ordinal()];
                if (i9 == 1) {
                    ViewGroup viewGroup = (ViewGroup) view.getParent();
                    if (viewGroup != null) {
                        if (FragmentManager.F0(2)) {
                            Log.v("FragmentManager", "SpecialEffectsController: Removing view " + view + " from container " + viewGroup);
                        }
                        viewGroup.removeView(view);
                        return;
                    }
                    return;
                }
                if (i9 == 2) {
                    if (FragmentManager.F0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to VISIBLE");
                    }
                    i8 = 0;
                } else if (i9 != 3) {
                    if (i9 != 4) {
                        return;
                    }
                    if (FragmentManager.F0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to INVISIBLE");
                    }
                    view.setVisibility(4);
                    return;
                } else {
                    if (FragmentManager.F0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Setting view " + view + " to GONE");
                    }
                    i8 = 8;
                }
                view.setVisibility(i8);
            }
        }

        e(c cVar, b bVar, Fragment fragment, androidx.core.os.e eVar) {
            this.f5779a = cVar;
            this.f5780b = bVar;
            this.f5781c = fragment;
            eVar.c(new a());
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public final void a(Runnable runnable) {
            this.f5782d.add(runnable);
        }

        final void b() {
            if (h()) {
                return;
            }
            this.f5784f = true;
            if (this.f5783e.isEmpty()) {
                c();
                return;
            }
            Iterator it = new ArrayList(this.f5783e).iterator();
            while (it.hasNext()) {
                ((androidx.core.os.e) it.next()).a();
            }
        }

        public void c() {
            if (this.f5785g) {
                return;
            }
            if (FragmentManager.F0(2)) {
                Log.v("FragmentManager", "SpecialEffectsController: " + this + " has called complete.");
            }
            this.f5785g = true;
            for (Runnable runnable : this.f5782d) {
                runnable.run();
            }
        }

        public final void d(androidx.core.os.e eVar) {
            if (this.f5783e.remove(eVar) && this.f5783e.isEmpty()) {
                c();
            }
        }

        public c e() {
            return this.f5779a;
        }

        public final Fragment f() {
            return this.f5781c;
        }

        b g() {
            return this.f5780b;
        }

        final boolean h() {
            return this.f5784f;
        }

        final boolean i() {
            return this.f5785g;
        }

        public final void j(androidx.core.os.e eVar) {
            l();
            this.f5783e.add(eVar);
        }

        final void k(c cVar, b bVar) {
            b bVar2;
            int i8 = c.f5777b[bVar.ordinal()];
            if (i8 != 1) {
                if (i8 != 2) {
                    if (i8 == 3 && this.f5779a != c.REMOVED) {
                        if (FragmentManager.F0(2)) {
                            Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.f5781c + " mFinalState = " + this.f5779a + " -> " + cVar + ". ");
                        }
                        this.f5779a = cVar;
                        return;
                    }
                    return;
                }
                if (FragmentManager.F0(2)) {
                    Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.f5781c + " mFinalState = " + this.f5779a + " -> REMOVED. mLifecycleImpact  = " + this.f5780b + " to REMOVING.");
                }
                this.f5779a = c.REMOVED;
                bVar2 = b.REMOVING;
            } else if (this.f5779a != c.REMOVED) {
                return;
            } else {
                if (FragmentManager.F0(2)) {
                    Log.v("FragmentManager", "SpecialEffectsController: For fragment " + this.f5781c + " mFinalState = REMOVED -> VISIBLE. mLifecycleImpact = " + this.f5780b + " to ADDING.");
                }
                this.f5779a = c.VISIBLE;
                bVar2 = b.ADDING;
            }
            this.f5780b = bVar2;
        }

        void l() {
        }

        public String toString() {
            return "Operation {" + Integer.toHexString(System.identityHashCode(this)) + "} {mFinalState = " + this.f5779a + "} {mLifecycleImpact = " + this.f5780b + "} {mFragment = " + this.f5781c + "}";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public x(ViewGroup viewGroup) {
        this.f5767a = viewGroup;
    }

    private void a(e.c cVar, e.b bVar, p pVar) {
        synchronized (this.f5768b) {
            androidx.core.os.e eVar = new androidx.core.os.e();
            e h8 = h(pVar.k());
            if (h8 != null) {
                h8.k(cVar, bVar);
                return;
            }
            d dVar = new d(cVar, bVar, pVar, eVar);
            this.f5768b.add(dVar);
            dVar.a(new a(dVar));
            dVar.a(new b(dVar));
        }
    }

    private e h(Fragment fragment) {
        Iterator<e> it = this.f5768b.iterator();
        while (it.hasNext()) {
            e next = it.next();
            if (next.f().equals(fragment) && !next.h()) {
                return next;
            }
        }
        return null;
    }

    private e i(Fragment fragment) {
        Iterator<e> it = this.f5769c.iterator();
        while (it.hasNext()) {
            e next = it.next();
            if (next.f().equals(fragment) && !next.h()) {
                return next;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static x n(ViewGroup viewGroup, FragmentManager fragmentManager) {
        return o(viewGroup, fragmentManager.y0());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static x o(ViewGroup viewGroup, y yVar) {
        int i8 = b1.b.f7948b;
        Object tag = viewGroup.getTag(i8);
        if (tag instanceof x) {
            return (x) tag;
        }
        x a9 = yVar.a(viewGroup);
        viewGroup.setTag(i8, a9);
        return a9;
    }

    private void q() {
        Iterator<e> it = this.f5768b.iterator();
        while (it.hasNext()) {
            e next = it.next();
            if (next.g() == e.b.ADDING) {
                next.k(e.c.f(next.f().m1().getVisibility()), e.b.NONE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(e.c cVar, p pVar) {
        if (FragmentManager.F0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing add operation for fragment " + pVar.k());
        }
        a(cVar, e.b.ADDING, pVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(p pVar) {
        if (FragmentManager.F0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing hide operation for fragment " + pVar.k());
        }
        a(e.c.GONE, e.b.NONE, pVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(p pVar) {
        if (FragmentManager.F0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing remove operation for fragment " + pVar.k());
        }
        a(e.c.REMOVED, e.b.REMOVING, pVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(p pVar) {
        if (FragmentManager.F0(2)) {
            Log.v("FragmentManager", "SpecialEffectsController: Enqueuing show operation for fragment " + pVar.k());
        }
        a(e.c.VISIBLE, e.b.NONE, pVar);
    }

    abstract void f(List<e> list, boolean z4);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g() {
        if (this.f5771e) {
            return;
        }
        if (!c0.V(this.f5767a)) {
            j();
            this.f5770d = false;
            return;
        }
        synchronized (this.f5768b) {
            if (!this.f5768b.isEmpty()) {
                ArrayList arrayList = new ArrayList(this.f5769c);
                this.f5769c.clear();
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    e eVar = (e) it.next();
                    if (FragmentManager.F0(2)) {
                        Log.v("FragmentManager", "SpecialEffectsController: Cancelling operation " + eVar);
                    }
                    eVar.b();
                    if (!eVar.i()) {
                        this.f5769c.add(eVar);
                    }
                }
                q();
                ArrayList arrayList2 = new ArrayList(this.f5768b);
                this.f5768b.clear();
                this.f5769c.addAll(arrayList2);
                Iterator it2 = arrayList2.iterator();
                while (it2.hasNext()) {
                    ((e) it2.next()).l();
                }
                f(arrayList2, this.f5770d);
                this.f5770d = false;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        String str;
        String str2;
        boolean V = c0.V(this.f5767a);
        synchronized (this.f5768b) {
            q();
            Iterator<e> it = this.f5768b.iterator();
            while (it.hasNext()) {
                it.next().l();
            }
            Iterator it2 = new ArrayList(this.f5769c).iterator();
            while (it2.hasNext()) {
                e eVar = (e) it2.next();
                if (FragmentManager.F0(2)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("SpecialEffectsController: ");
                    if (V) {
                        str2 = BuildConfig.FLAVOR;
                    } else {
                        str2 = "Container " + this.f5767a + " is not attached to window. ";
                    }
                    sb.append(str2);
                    sb.append("Cancelling running operation ");
                    sb.append(eVar);
                    Log.v("FragmentManager", sb.toString());
                }
                eVar.b();
            }
            Iterator it3 = new ArrayList(this.f5768b).iterator();
            while (it3.hasNext()) {
                e eVar2 = (e) it3.next();
                if (FragmentManager.F0(2)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("SpecialEffectsController: ");
                    if (V) {
                        str = BuildConfig.FLAVOR;
                    } else {
                        str = "Container " + this.f5767a + " is not attached to window. ";
                    }
                    sb2.append(str);
                    sb2.append("Cancelling pending operation ");
                    sb2.append(eVar2);
                    Log.v("FragmentManager", sb2.toString());
                }
                eVar2.b();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void k() {
        if (this.f5771e) {
            this.f5771e = false;
            g();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e.b l(p pVar) {
        e h8 = h(pVar.k());
        e.b g8 = h8 != null ? h8.g() : null;
        e i8 = i(pVar.k());
        return (i8 == null || !(g8 == null || g8 == e.b.NONE)) ? g8 : i8.g();
    }

    public ViewGroup m() {
        return this.f5767a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p() {
        synchronized (this.f5768b) {
            q();
            this.f5771e = false;
            int size = this.f5768b.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                e eVar = this.f5768b.get(size);
                e.c h8 = e.c.h(eVar.f().T);
                e.c e8 = eVar.e();
                e.c cVar = e.c.VISIBLE;
                if (e8 == cVar && h8 != cVar) {
                    this.f5771e = eVar.f().f0();
                    break;
                }
                size--;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(boolean z4) {
        this.f5770d = z4;
    }
}
