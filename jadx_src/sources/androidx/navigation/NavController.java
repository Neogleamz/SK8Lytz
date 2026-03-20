package androidx.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import androidx.activity.OnBackPressedDispatcher;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.i0;
import androidx.navigation.i;
import androidx.navigation.n;
import androidx.navigation.q;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class NavController {

    /* renamed from: a  reason: collision with root package name */
    private final Context f6281a;

    /* renamed from: b  reason: collision with root package name */
    private Activity f6282b;

    /* renamed from: c  reason: collision with root package name */
    private m f6283c;

    /* renamed from: d  reason: collision with root package name */
    j f6284d;

    /* renamed from: e  reason: collision with root package name */
    private Bundle f6285e;

    /* renamed from: f  reason: collision with root package name */
    private Parcelable[] f6286f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f6287g;

    /* renamed from: i  reason: collision with root package name */
    private androidx.lifecycle.j f6289i;

    /* renamed from: j  reason: collision with root package name */
    private f f6290j;

    /* renamed from: h  reason: collision with root package name */
    final Deque<e> f6288h = new ArrayDeque();

    /* renamed from: k  reason: collision with root package name */
    private r f6291k = new r();

    /* renamed from: l  reason: collision with root package name */
    private final CopyOnWriteArrayList<b> f6292l = new CopyOnWriteArrayList<>();

    /* renamed from: m  reason: collision with root package name */
    private final androidx.lifecycle.i f6293m = new androidx.lifecycle.h() { // from class: androidx.navigation.NavController.1
        @Override // androidx.lifecycle.h
        public void c(androidx.lifecycle.j jVar, Lifecycle.Event event) {
            NavController navController = NavController.this;
            if (navController.f6284d != null) {
                for (e eVar : navController.f6288h) {
                    eVar.e(event);
                }
            }
        }
    };

    /* renamed from: n  reason: collision with root package name */
    private final androidx.activity.l f6294n = new a(false);

    /* renamed from: o  reason: collision with root package name */
    private boolean f6295o = true;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends androidx.activity.l {
        a(boolean z4) {
            super(z4);
        }

        @Override // androidx.activity.l
        public void b() {
            NavController.this.m();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a(NavController navController, i iVar, Bundle bundle);
    }

    public NavController(Context context) {
        this.f6281a = context;
        while (true) {
            if (!(context instanceof ContextWrapper)) {
                break;
            } else if (context instanceof Activity) {
                this.f6282b = (Activity) context;
                break;
            } else {
                context = ((ContextWrapper) context).getBaseContext();
            }
        }
        r rVar = this.f6291k;
        rVar.a(new k(rVar));
        this.f6291k.a(new androidx.navigation.a(this.f6281a));
    }

    private boolean a() {
        while (!this.f6288h.isEmpty() && (this.f6288h.peekLast().b() instanceof j) && o(this.f6288h.peekLast().b().q(), true)) {
        }
        if (this.f6288h.isEmpty()) {
            return false;
        }
        i b9 = this.f6288h.peekLast().b();
        i iVar = null;
        if (b9 instanceof androidx.navigation.b) {
            Iterator<e> descendingIterator = this.f6288h.descendingIterator();
            while (true) {
                if (!descendingIterator.hasNext()) {
                    break;
                }
                i b10 = descendingIterator.next().b();
                if (!(b10 instanceof j) && !(b10 instanceof androidx.navigation.b)) {
                    iVar = b10;
                    break;
                }
            }
        }
        HashMap hashMap = new HashMap();
        Iterator<e> descendingIterator2 = this.f6288h.descendingIterator();
        while (descendingIterator2.hasNext()) {
            e next = descendingIterator2.next();
            Lifecycle.State c9 = next.c();
            i b11 = next.b();
            if (b9 != null && b11.q() == b9.q()) {
                Lifecycle.State state = Lifecycle.State.RESUMED;
                if (c9 != state) {
                    hashMap.put(next, state);
                }
                b9 = b9.u();
            } else if (iVar == null || b11.q() != iVar.q()) {
                next.h(Lifecycle.State.CREATED);
            } else {
                if (c9 == Lifecycle.State.RESUMED) {
                    next.h(Lifecycle.State.STARTED);
                } else {
                    Lifecycle.State state2 = Lifecycle.State.STARTED;
                    if (c9 != state2) {
                        hashMap.put(next, state2);
                    }
                }
                iVar = iVar.u();
            }
        }
        for (e eVar : this.f6288h) {
            Lifecycle.State state3 = (Lifecycle.State) hashMap.get(eVar);
            if (state3 != null) {
                eVar.h(state3);
            } else {
                eVar.i();
            }
        }
        e peekLast = this.f6288h.peekLast();
        Iterator<b> it = this.f6292l.iterator();
        while (it.hasNext()) {
            it.next().a(this, peekLast.b(), peekLast.a());
        }
        return true;
    }

    private String d(int[] iArr) {
        j jVar;
        j jVar2 = this.f6284d;
        int i8 = 0;
        while (true) {
            i iVar = null;
            if (i8 >= iArr.length) {
                return null;
            }
            int i9 = iArr[i8];
            if (i8 != 0) {
                iVar = jVar2.G(i9);
            } else if (this.f6284d.q() == i9) {
                iVar = this.f6284d;
            }
            if (iVar == null) {
                return i.p(this.f6281a, i9);
            }
            if (i8 != iArr.length - 1) {
                while (true) {
                    jVar = (j) iVar;
                    if (!(jVar.G(jVar.K()) instanceof j)) {
                        break;
                    }
                    iVar = jVar.G(jVar.K());
                }
                jVar2 = jVar;
            }
            i8++;
        }
    }

    private int g() {
        int i8 = 0;
        for (e eVar : this.f6288h) {
            if (!(eVar.b() instanceof j)) {
                i8++;
            }
        }
        return i8;
    }

    private void k(i iVar, Bundle bundle, n nVar, q.a aVar) {
        boolean z4 = false;
        boolean o5 = (nVar == null || nVar.e() == -1) ? false : o(nVar.e(), nVar.f());
        q e8 = this.f6291k.e(iVar.t());
        Bundle h8 = iVar.h(bundle);
        i b9 = e8.b(iVar, h8, nVar, aVar);
        if (b9 != null) {
            if (!(b9 instanceof androidx.navigation.b)) {
                while (!this.f6288h.isEmpty() && (this.f6288h.peekLast().b() instanceof androidx.navigation.b) && o(this.f6288h.peekLast().b().q(), true)) {
                }
            }
            ArrayDeque arrayDeque = new ArrayDeque();
            if (iVar instanceof j) {
                j jVar = b9;
                while (true) {
                    j u8 = jVar.u();
                    if (u8 != null) {
                        arrayDeque.addFirst(new e(this.f6281a, u8, h8, this.f6289i, this.f6290j));
                        if (!this.f6288h.isEmpty() && this.f6288h.getLast().b() == u8) {
                            o(u8.q(), true);
                        }
                    }
                    if (u8 == null || u8 == iVar) {
                        break;
                    }
                    jVar = u8;
                }
            }
            j b10 = arrayDeque.isEmpty() ? b9 : ((e) arrayDeque.getFirst()).b();
            while (b10 != null && c(b10.q()) == null) {
                b10 = b10.u();
                if (b10 != null) {
                    arrayDeque.addFirst(new e(this.f6281a, b10, h8, this.f6289i, this.f6290j));
                }
            }
            i b11 = arrayDeque.isEmpty() ? b9 : ((e) arrayDeque.getLast()).b();
            while (!this.f6288h.isEmpty() && (this.f6288h.getLast().b() instanceof j) && ((j) this.f6288h.getLast().b()).H(b11.q(), false) == null && o(this.f6288h.getLast().b().q(), true)) {
            }
            this.f6288h.addAll(arrayDeque);
            if (this.f6288h.isEmpty() || this.f6288h.getFirst().b() != this.f6284d) {
                this.f6288h.addFirst(new e(this.f6281a, this.f6284d, h8, this.f6289i, this.f6290j));
            }
            this.f6288h.add(new e(this.f6281a, b9, b9.h(h8), this.f6289i, this.f6290j));
        } else if (nVar != null && nVar.g()) {
            e peekLast = this.f6288h.peekLast();
            if (peekLast != null) {
                peekLast.f(h8);
            }
            z4 = true;
        }
        x();
        if (o5 || b9 != null || z4) {
            a();
        }
    }

    private void l(Bundle bundle) {
        Activity activity;
        ArrayList<String> stringArrayList;
        Bundle bundle2 = this.f6285e;
        if (bundle2 != null && (stringArrayList = bundle2.getStringArrayList("android-support-nav:controller:navigatorState:names")) != null) {
            Iterator<String> it = stringArrayList.iterator();
            while (it.hasNext()) {
                String next = it.next();
                q e8 = this.f6291k.e(next);
                Bundle bundle3 = this.f6285e.getBundle(next);
                if (bundle3 != null) {
                    e8.c(bundle3);
                }
            }
        }
        Parcelable[] parcelableArr = this.f6286f;
        boolean z4 = false;
        if (parcelableArr != null) {
            for (Parcelable parcelable : parcelableArr) {
                NavBackStackEntryState navBackStackEntryState = (NavBackStackEntryState) parcelable;
                i c9 = c(navBackStackEntryState.b());
                if (c9 == null) {
                    throw new IllegalStateException("Restoring the Navigation back stack failed: destination " + i.p(this.f6281a, navBackStackEntryState.b()) + " cannot be found from the current destination " + f());
                }
                Bundle a9 = navBackStackEntryState.a();
                if (a9 != null) {
                    a9.setClassLoader(this.f6281a.getClassLoader());
                }
                this.f6288h.add(new e(this.f6281a, c9, a9, this.f6289i, this.f6290j, navBackStackEntryState.d(), navBackStackEntryState.c()));
            }
            x();
            this.f6286f = null;
        }
        if (this.f6284d == null || !this.f6288h.isEmpty()) {
            a();
            return;
        }
        if (!this.f6287g && (activity = this.f6282b) != null && j(activity.getIntent())) {
            z4 = true;
        }
        if (z4) {
            return;
        }
        k(this.f6284d, bundle, null, null);
    }

    private void x() {
        boolean z4 = true;
        this.f6294n.f((!this.f6295o || g() <= 1) ? false : false);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(boolean z4) {
        this.f6295o = z4;
        x();
    }

    i c(int i8) {
        j jVar = this.f6284d;
        if (jVar == null) {
            return null;
        }
        if (jVar.q() == i8) {
            return this.f6284d;
        }
        j b9 = this.f6288h.isEmpty() ? this.f6284d : this.f6288h.getLast().b();
        return (b9 instanceof j ? b9 : b9.u()).G(i8);
    }

    public e e() {
        if (this.f6288h.isEmpty()) {
            return null;
        }
        return this.f6288h.getLast();
    }

    public i f() {
        e e8 = e();
        if (e8 != null) {
            return e8.b();
        }
        return null;
    }

    public m h() {
        if (this.f6283c == null) {
            this.f6283c = new m(this.f6281a, this.f6291k);
        }
        return this.f6283c;
    }

    public r i() {
        return this.f6291k;
    }

    public boolean j(Intent intent) {
        i.a v8;
        j jVar;
        if (intent == null) {
            return false;
        }
        Bundle extras = intent.getExtras();
        int[] intArray = extras != null ? extras.getIntArray("android-support-nav:controller:deepLinkIds") : null;
        Bundle bundle = new Bundle();
        Bundle bundle2 = extras != null ? extras.getBundle("android-support-nav:controller:deepLinkExtras") : null;
        if (bundle2 != null) {
            bundle.putAll(bundle2);
        }
        if ((intArray == null || intArray.length == 0) && intent.getData() != null && (v8 = this.f6284d.v(new h(intent))) != null) {
            i f5 = v8.f();
            int[] i8 = f5.i();
            bundle.putAll(f5.h(v8.h()));
            intArray = i8;
        }
        if (intArray == null || intArray.length == 0) {
            return false;
        }
        String d8 = d(intArray);
        if (d8 != null) {
            Log.i("NavController", "Could not find destination " + d8 + " in the navigation graph, ignoring the deep link from " + intent);
            return false;
        }
        bundle.putParcelable("android-support-nav:controller:deepLinkIntent", intent);
        int flags = intent.getFlags();
        int i9 = 268435456 & flags;
        if (i9 != 0 && (flags & RecognitionOptions.TEZ_CODE) == 0) {
            intent.addFlags(RecognitionOptions.TEZ_CODE);
            androidx.core.app.s.k(this.f6281a).g(intent).n();
            Activity activity = this.f6282b;
            if (activity != null) {
                activity.finish();
                this.f6282b.overridePendingTransition(0, 0);
            }
            return true;
        } else if (i9 != 0) {
            if (!this.f6288h.isEmpty()) {
                o(this.f6284d.q(), true);
            }
            int i10 = 0;
            while (i10 < intArray.length) {
                int i11 = i10 + 1;
                int i12 = intArray[i10];
                i c9 = c(i12);
                if (c9 == null) {
                    throw new IllegalStateException("Deep Linking failed: destination " + i.p(this.f6281a, i12) + " cannot be found from the current destination " + f());
                }
                k(c9, bundle, new n.a().b(0).c(0).a(), null);
                i10 = i11;
            }
            return true;
        } else {
            j jVar2 = this.f6284d;
            int i13 = 0;
            while (i13 < intArray.length) {
                int i14 = intArray[i13];
                i G = i13 == 0 ? this.f6284d : jVar2.G(i14);
                if (G == null) {
                    throw new IllegalStateException("Deep Linking failed: destination " + i.p(this.f6281a, i14) + " cannot be found in graph " + jVar2);
                }
                if (i13 != intArray.length - 1) {
                    while (true) {
                        jVar = (j) G;
                        if (!(jVar.G(jVar.K()) instanceof j)) {
                            break;
                        }
                        G = jVar.G(jVar.K());
                    }
                    jVar2 = jVar;
                } else {
                    k(G, G.h(bundle), new n.a().g(this.f6284d.q(), true).b(0).c(0).a(), null);
                }
                i13++;
            }
            this.f6287g = true;
            return true;
        }
    }

    public boolean m() {
        if (this.f6288h.isEmpty()) {
            return false;
        }
        return n(f().q(), true);
    }

    public boolean n(int i8, boolean z4) {
        return o(i8, z4) && a();
    }

    boolean o(int i8, boolean z4) {
        boolean z8;
        boolean z9 = false;
        if (this.f6288h.isEmpty()) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        Iterator<e> descendingIterator = this.f6288h.descendingIterator();
        while (true) {
            if (!descendingIterator.hasNext()) {
                z8 = false;
                break;
            }
            i b9 = descendingIterator.next().b();
            q e8 = this.f6291k.e(b9.t());
            if (z4 || b9.q() != i8) {
                arrayList.add(e8);
            }
            if (b9.q() == i8) {
                z8 = true;
                break;
            }
        }
        if (!z8) {
            Log.i("NavController", "Ignoring popBackStack to destination " + i.p(this.f6281a, i8) + " as it was not found on the current back stack");
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext() && ((q) it.next()).e()) {
            e removeLast = this.f6288h.removeLast();
            if (removeLast.getLifecycle().b().f(Lifecycle.State.CREATED)) {
                removeLast.h(Lifecycle.State.DESTROYED);
            }
            f fVar = this.f6290j;
            if (fVar != null) {
                fVar.f(removeLast.f6320f);
            }
            z9 = true;
        }
        x();
        return z9;
    }

    public void p(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        bundle.setClassLoader(this.f6281a.getClassLoader());
        this.f6285e = bundle.getBundle("android-support-nav:controller:navigatorState");
        this.f6286f = bundle.getParcelableArray("android-support-nav:controller:backStack");
        this.f6287g = bundle.getBoolean("android-support-nav:controller:deepLinkHandled");
    }

    public Bundle q() {
        Bundle bundle;
        ArrayList<String> arrayList = new ArrayList<>();
        Bundle bundle2 = new Bundle();
        for (Map.Entry<String, q<? extends i>> entry : this.f6291k.f().entrySet()) {
            String key = entry.getKey();
            Bundle d8 = entry.getValue().d();
            if (d8 != null) {
                arrayList.add(key);
                bundle2.putBundle(key, d8);
            }
        }
        if (arrayList.isEmpty()) {
            bundle = null;
        } else {
            bundle = new Bundle();
            bundle2.putStringArrayList("android-support-nav:controller:navigatorState:names", arrayList);
            bundle.putBundle("android-support-nav:controller:navigatorState", bundle2);
        }
        if (!this.f6288h.isEmpty()) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            Parcelable[] parcelableArr = new Parcelable[this.f6288h.size()];
            int i8 = 0;
            for (e eVar : this.f6288h) {
                parcelableArr[i8] = new NavBackStackEntryState(eVar);
                i8++;
            }
            bundle.putParcelableArray("android-support-nav:controller:backStack", parcelableArr);
        }
        if (this.f6287g) {
            if (bundle == null) {
                bundle = new Bundle();
            }
            bundle.putBoolean("android-support-nav:controller:deepLinkHandled", this.f6287g);
        }
        return bundle;
    }

    public void r(int i8) {
        s(i8, null);
    }

    public void s(int i8, Bundle bundle) {
        t(h().c(i8), bundle);
    }

    public void t(j jVar, Bundle bundle) {
        j jVar2 = this.f6284d;
        if (jVar2 != null) {
            o(jVar2.q(), true);
        }
        this.f6284d = jVar;
        l(bundle);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void u(androidx.lifecycle.j jVar) {
        if (jVar == this.f6289i) {
            return;
        }
        this.f6289i = jVar;
        jVar.getLifecycle().a(this.f6293m);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void v(OnBackPressedDispatcher onBackPressedDispatcher) {
        if (this.f6289i == null) {
            throw new IllegalStateException("You must call setLifecycleOwner() before calling setOnBackPressedDispatcher()");
        }
        this.f6294n.d();
        onBackPressedDispatcher.b(this.f6289i, this.f6294n);
        this.f6289i.getLifecycle().c(this.f6293m);
        this.f6289i.getLifecycle().a(this.f6293m);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w(i0 i0Var) {
        if (this.f6290j == f.g(i0Var)) {
            return;
        }
        if (!this.f6288h.isEmpty()) {
            throw new IllegalStateException("ViewModelStore should be set before setGraph call");
        }
        this.f6290j = f.g(i0Var);
    }
}
