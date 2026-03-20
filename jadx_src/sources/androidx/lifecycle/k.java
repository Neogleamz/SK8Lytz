package androidx.lifecycle;

import android.annotation.SuppressLint;
import androidx.lifecycle.Lifecycle;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k extends Lifecycle {

    /* renamed from: j  reason: collision with root package name */
    public static final a f5886j = new a(null);

    /* renamed from: b  reason: collision with root package name */
    private final boolean f5887b;

    /* renamed from: c  reason: collision with root package name */
    private m.a<i, b> f5888c;

    /* renamed from: d  reason: collision with root package name */
    private Lifecycle.State f5889d;

    /* renamed from: e  reason: collision with root package name */
    private final WeakReference<j> f5890e;

    /* renamed from: f  reason: collision with root package name */
    private int f5891f;

    /* renamed from: g  reason: collision with root package name */
    private boolean f5892g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f5893h;

    /* renamed from: i  reason: collision with root package name */
    private ArrayList<Lifecycle.State> f5894i;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(kotlin.jvm.internal.i iVar) {
            this();
        }

        public final Lifecycle.State a(Lifecycle.State state, Lifecycle.State state2) {
            kotlin.jvm.internal.p.e(state, "state1");
            return (state2 == null || state2.compareTo(state) >= 0) ? state : state2;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private Lifecycle.State f5895a;

        /* renamed from: b  reason: collision with root package name */
        private h f5896b;

        public b(i iVar, Lifecycle.State state) {
            kotlin.jvm.internal.p.e(state, "initialState");
            kotlin.jvm.internal.p.b(iVar);
            this.f5896b = m.f(iVar);
            this.f5895a = state;
        }

        public final void a(j jVar, Lifecycle.Event event) {
            kotlin.jvm.internal.p.e(event, "event");
            Lifecycle.State h8 = event.h();
            this.f5895a = k.f5886j.a(this.f5895a, h8);
            h hVar = this.f5896b;
            kotlin.jvm.internal.p.b(jVar);
            hVar.c(jVar, event);
            this.f5895a = h8;
        }

        public final Lifecycle.State b() {
            return this.f5895a;
        }
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public k(j jVar) {
        this(jVar, true);
        kotlin.jvm.internal.p.e(jVar, "provider");
    }

    private k(j jVar, boolean z4) {
        this.f5887b = z4;
        this.f5888c = new m.a<>();
        this.f5889d = Lifecycle.State.INITIALIZED;
        this.f5894i = new ArrayList<>();
        this.f5890e = new WeakReference<>(jVar);
    }

    private final void d(j jVar) {
        Iterator<Map.Entry<i, b>> descendingIterator = this.f5888c.descendingIterator();
        kotlin.jvm.internal.p.d(descendingIterator, "observerMap.descendingIterator()");
        while (descendingIterator.hasNext() && !this.f5893h) {
            Map.Entry<i, b> next = descendingIterator.next();
            kotlin.jvm.internal.p.d(next, "next()");
            i key = next.getKey();
            b value = next.getValue();
            while (value.b().compareTo(this.f5889d) > 0 && !this.f5893h && this.f5888c.contains(key)) {
                Lifecycle.Event a9 = Lifecycle.Event.Companion.a(value.b());
                if (a9 == null) {
                    throw new IllegalStateException("no event down from " + value.b());
                }
                m(a9.h());
                value.a(jVar, a9);
                l();
            }
        }
    }

    private final Lifecycle.State e(i iVar) {
        b value;
        Map.Entry<i, b> q = this.f5888c.q(iVar);
        Lifecycle.State state = null;
        Lifecycle.State b9 = (q == null || (value = q.getValue()) == null) ? null : value.b();
        if (!this.f5894i.isEmpty()) {
            ArrayList<Lifecycle.State> arrayList = this.f5894i;
            state = arrayList.get(arrayList.size() - 1);
        }
        a aVar = f5886j;
        return aVar.a(aVar.a(this.f5889d, b9), state);
    }

    @SuppressLint({"RestrictedApi"})
    private final void f(String str) {
        if (!this.f5887b || l.c.g().b()) {
            return;
        }
        throw new IllegalStateException(("Method " + str + " must be called on the main thread").toString());
    }

    private final void g(j jVar) {
        m.b<i, b>.d h8 = this.f5888c.h();
        kotlin.jvm.internal.p.d(h8, "observerMap.iteratorWithAdditions()");
        while (h8.hasNext() && !this.f5893h) {
            Map.Entry next = h8.next();
            i iVar = (i) next.getKey();
            b bVar = (b) next.getValue();
            while (bVar.b().compareTo(this.f5889d) < 0 && !this.f5893h && this.f5888c.contains(iVar)) {
                m(bVar.b());
                Lifecycle.Event b9 = Lifecycle.Event.Companion.b(bVar.b());
                if (b9 == null) {
                    throw new IllegalStateException("no event up from " + bVar.b());
                }
                bVar.a(jVar, b9);
                l();
            }
        }
    }

    private final boolean i() {
        if (this.f5888c.size() == 0) {
            return true;
        }
        Map.Entry<i, b> e8 = this.f5888c.e();
        kotlin.jvm.internal.p.b(e8);
        Lifecycle.State b9 = e8.getValue().b();
        Map.Entry<i, b> i8 = this.f5888c.i();
        kotlin.jvm.internal.p.b(i8);
        Lifecycle.State b10 = i8.getValue().b();
        return b9 == b10 && this.f5889d == b10;
    }

    private final void k(Lifecycle.State state) {
        Lifecycle.State state2 = this.f5889d;
        if (state2 == state) {
            return;
        }
        if (!((state2 == Lifecycle.State.INITIALIZED && state == Lifecycle.State.DESTROYED) ? false : true)) {
            throw new IllegalStateException(("no event down from " + this.f5889d + " in component " + this.f5890e.get()).toString());
        }
        this.f5889d = state;
        if (this.f5892g || this.f5891f != 0) {
            this.f5893h = true;
            return;
        }
        this.f5892g = true;
        o();
        this.f5892g = false;
        if (this.f5889d == Lifecycle.State.DESTROYED) {
            this.f5888c = new m.a<>();
        }
    }

    private final void l() {
        ArrayList<Lifecycle.State> arrayList = this.f5894i;
        arrayList.remove(arrayList.size() - 1);
    }

    private final void m(Lifecycle.State state) {
        this.f5894i.add(state);
    }

    private final void o() {
        j jVar = this.f5890e.get();
        if (jVar == null) {
            throw new IllegalStateException("LifecycleOwner of this LifecycleRegistry is already garbage collected. It is too late to change lifecycle state.");
        }
        while (true) {
            boolean i8 = i();
            this.f5893h = false;
            if (i8) {
                return;
            }
            Lifecycle.State state = this.f5889d;
            Map.Entry<i, b> e8 = this.f5888c.e();
            kotlin.jvm.internal.p.b(e8);
            if (state.compareTo(e8.getValue().b()) < 0) {
                d(jVar);
            }
            Map.Entry<i, b> i9 = this.f5888c.i();
            if (!this.f5893h && i9 != null && this.f5889d.compareTo(i9.getValue().b()) > 0) {
                g(jVar);
            }
        }
    }

    @Override // androidx.lifecycle.Lifecycle
    public void a(i iVar) {
        j jVar;
        kotlin.jvm.internal.p.e(iVar, "observer");
        f("addObserver");
        Lifecycle.State state = this.f5889d;
        Lifecycle.State state2 = Lifecycle.State.DESTROYED;
        if (state != state2) {
            state2 = Lifecycle.State.INITIALIZED;
        }
        b bVar = new b(iVar, state2);
        if (this.f5888c.n(iVar, bVar) == null && (jVar = this.f5890e.get()) != null) {
            boolean z4 = this.f5891f != 0 || this.f5892g;
            Lifecycle.State e8 = e(iVar);
            this.f5891f++;
            while (bVar.b().compareTo(e8) < 0 && this.f5888c.contains(iVar)) {
                m(bVar.b());
                Lifecycle.Event b9 = Lifecycle.Event.Companion.b(bVar.b());
                if (b9 == null) {
                    throw new IllegalStateException("no event up from " + bVar.b());
                }
                bVar.a(jVar, b9);
                l();
                e8 = e(iVar);
            }
            if (!z4) {
                o();
            }
            this.f5891f--;
        }
    }

    @Override // androidx.lifecycle.Lifecycle
    public Lifecycle.State b() {
        return this.f5889d;
    }

    @Override // androidx.lifecycle.Lifecycle
    public void c(i iVar) {
        kotlin.jvm.internal.p.e(iVar, "observer");
        f("removeObserver");
        this.f5888c.p(iVar);
    }

    public void h(Lifecycle.Event event) {
        kotlin.jvm.internal.p.e(event, "event");
        f("handleLifecycleEvent");
        k(event.h());
    }

    public void j(Lifecycle.State state) {
        kotlin.jvm.internal.p.e(state, "state");
        f("markState");
        n(state);
    }

    public void n(Lifecycle.State state) {
        kotlin.jvm.internal.p.e(state, "state");
        f("setCurrentState");
        k(state);
    }
}
