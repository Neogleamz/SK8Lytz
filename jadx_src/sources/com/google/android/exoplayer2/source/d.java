package com.google.android.exoplayer2.source;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import b6.l0;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.source.x;
import com.google.android.exoplayer2.z0;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends com.google.android.exoplayer2.source.c<e> {
    private static final z0 C = new z0.c().f(Uri.EMPTY).a();
    private Set<C0108d> A;
    private x B;

    /* renamed from: l  reason: collision with root package name */
    private final List<e> f10282l;

    /* renamed from: m  reason: collision with root package name */
    private final Set<C0108d> f10283m;

    /* renamed from: n  reason: collision with root package name */
    private Handler f10284n;

    /* renamed from: p  reason: collision with root package name */
    private final List<e> f10285p;
    private final IdentityHashMap<j, e> q;

    /* renamed from: t  reason: collision with root package name */
    private final Map<Object, e> f10286t;

    /* renamed from: w  reason: collision with root package name */
    private final Set<e> f10287w;

    /* renamed from: x  reason: collision with root package name */
    private final boolean f10288x;

    /* renamed from: y  reason: collision with root package name */
    private final boolean f10289y;

    /* renamed from: z  reason: collision with root package name */
    private boolean f10290z;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b extends com.google.android.exoplayer2.a {

        /* renamed from: j  reason: collision with root package name */
        private final int f10291j;

        /* renamed from: k  reason: collision with root package name */
        private final int f10292k;

        /* renamed from: l  reason: collision with root package name */
        private final int[] f10293l;

        /* renamed from: m  reason: collision with root package name */
        private final int[] f10294m;

        /* renamed from: n  reason: collision with root package name */
        private final h2[] f10295n;

        /* renamed from: p  reason: collision with root package name */
        private final Object[] f10296p;
        private final HashMap<Object, Integer> q;

        public b(Collection<e> collection, x xVar, boolean z4) {
            super(z4, xVar);
            int size = collection.size();
            this.f10293l = new int[size];
            this.f10294m = new int[size];
            this.f10295n = new h2[size];
            this.f10296p = new Object[size];
            this.q = new HashMap<>();
            int i8 = 0;
            int i9 = 0;
            int i10 = 0;
            for (e eVar : collection) {
                this.f10295n[i10] = eVar.f10299a.c0();
                this.f10294m[i10] = i8;
                this.f10293l[i10] = i9;
                i8 += this.f10295n[i10].t();
                i9 += this.f10295n[i10].m();
                Object[] objArr = this.f10296p;
                objArr[i10] = eVar.f10300b;
                this.q.put(objArr[i10], Integer.valueOf(i10));
                i10++;
            }
            this.f10291j = i8;
            this.f10292k = i9;
        }

        @Override // com.google.android.exoplayer2.a
        protected Object B(int i8) {
            return this.f10296p[i8];
        }

        @Override // com.google.android.exoplayer2.a
        protected int D(int i8) {
            return this.f10293l[i8];
        }

        @Override // com.google.android.exoplayer2.a
        protected int E(int i8) {
            return this.f10294m[i8];
        }

        @Override // com.google.android.exoplayer2.a
        protected h2 H(int i8) {
            return this.f10295n[i8];
        }

        @Override // com.google.android.exoplayer2.h2
        public int m() {
            return this.f10292k;
        }

        @Override // com.google.android.exoplayer2.h2
        public int t() {
            return this.f10291j;
        }

        @Override // com.google.android.exoplayer2.a
        protected int w(Object obj) {
            Integer num = this.q.get(obj);
            if (num == null) {
                return -1;
            }
            return num.intValue();
        }

        @Override // com.google.android.exoplayer2.a
        protected int x(int i8) {
            return l0.h(this.f10293l, i8 + 1, false, false);
        }

        @Override // com.google.android.exoplayer2.a
        protected int y(int i8) {
            return l0.h(this.f10294m, i8 + 1, false, false);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class c extends com.google.android.exoplayer2.source.a {
        private c() {
        }

        @Override // com.google.android.exoplayer2.source.a
        protected void C(a6.y yVar) {
        }

        @Override // com.google.android.exoplayer2.source.a
        protected void E() {
        }

        @Override // com.google.android.exoplayer2.source.k
        public j b(k.b bVar, a6.b bVar2, long j8) {
            throw new UnsupportedOperationException();
        }

        @Override // com.google.android.exoplayer2.source.k
        public z0 i() {
            return d.C;
        }

        @Override // com.google.android.exoplayer2.source.k
        public void n() {
        }

        @Override // com.google.android.exoplayer2.source.k
        public void p(j jVar) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: com.google.android.exoplayer2.source.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0108d {

        /* renamed from: a  reason: collision with root package name */
        private final Handler f10297a;

        /* renamed from: b  reason: collision with root package name */
        private final Runnable f10298b;

        public C0108d(Handler handler, Runnable runnable) {
            this.f10297a = handler;
            this.f10298b = runnable;
        }

        public void a() {
            this.f10297a.post(this.f10298b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class e {

        /* renamed from: a  reason: collision with root package name */
        public final i f10299a;

        /* renamed from: d  reason: collision with root package name */
        public int f10302d;

        /* renamed from: e  reason: collision with root package name */
        public int f10303e;

        /* renamed from: f  reason: collision with root package name */
        public boolean f10304f;

        /* renamed from: c  reason: collision with root package name */
        public final List<k.b> f10301c = new ArrayList();

        /* renamed from: b  reason: collision with root package name */
        public final Object f10300b = new Object();

        public e(k kVar, boolean z4) {
            this.f10299a = new i(kVar, z4);
        }

        public void a(int i8, int i9) {
            this.f10302d = i8;
            this.f10303e = i9;
            this.f10304f = false;
            this.f10301c.clear();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class f<T> {

        /* renamed from: a  reason: collision with root package name */
        public final int f10305a;

        /* renamed from: b  reason: collision with root package name */
        public final T f10306b;

        /* renamed from: c  reason: collision with root package name */
        public final C0108d f10307c;

        public f(int i8, T t8, C0108d c0108d) {
            this.f10305a = i8;
            this.f10306b = t8;
            this.f10307c = c0108d;
        }
    }

    public d(boolean z4, x xVar, k... kVarArr) {
        this(z4, false, xVar, kVarArr);
    }

    public d(boolean z4, boolean z8, x xVar, k... kVarArr) {
        for (k kVar : kVarArr) {
            b6.a.e(kVar);
        }
        this.B = xVar.b() > 0 ? xVar.i() : xVar;
        this.q = new IdentityHashMap<>();
        this.f10286t = new HashMap();
        this.f10282l = new ArrayList();
        this.f10285p = new ArrayList();
        this.A = new HashSet();
        this.f10283m = new HashSet();
        this.f10287w = new HashSet();
        this.f10288x = z4;
        this.f10289y = z8;
        T(Arrays.asList(kVarArr));
    }

    public d(boolean z4, k... kVarArr) {
        this(z4, new x.a(0), kVarArr);
    }

    public d(k... kVarArr) {
        this(false, kVarArr);
    }

    private void R(int i8, e eVar) {
        int i9;
        if (i8 > 0) {
            e eVar2 = this.f10285p.get(i8 - 1);
            i9 = eVar2.f10303e + eVar2.f10299a.c0().t();
        } else {
            i9 = 0;
        }
        eVar.a(i8, i9);
        W(i8, 1, eVar.f10299a.c0().t());
        this.f10285p.add(i8, eVar);
        this.f10286t.put(eVar.f10300b, eVar);
        N(eVar, eVar.f10299a);
        if (B() && this.q.isEmpty()) {
            this.f10287w.add(eVar);
        } else {
            G(eVar);
        }
    }

    private void U(int i8, Collection<e> collection) {
        for (e eVar : collection) {
            R(i8, eVar);
            i8++;
        }
    }

    private void V(int i8, Collection<k> collection, Handler handler, Runnable runnable) {
        b6.a.a((handler == null) == (runnable == null));
        Handler handler2 = this.f10284n;
        for (k kVar : collection) {
            b6.a.e(kVar);
        }
        ArrayList arrayList = new ArrayList(collection.size());
        for (k kVar2 : collection) {
            arrayList.add(new e(kVar2, this.f10289y));
        }
        this.f10282l.addAll(i8, arrayList);
        if (handler2 != null && !collection.isEmpty()) {
            handler2.obtainMessage(0, new f(i8, arrayList, X(handler, runnable))).sendToTarget();
        } else if (runnable != null && handler != null) {
            handler.post(runnable);
        }
    }

    private void W(int i8, int i9, int i10) {
        while (i8 < this.f10285p.size()) {
            e eVar = this.f10285p.get(i8);
            eVar.f10302d += i9;
            eVar.f10303e += i10;
            i8++;
        }
    }

    private C0108d X(Handler handler, Runnable runnable) {
        if (handler == null || runnable == null) {
            return null;
        }
        C0108d c0108d = new C0108d(handler, runnable);
        this.f10283m.add(c0108d);
        return c0108d;
    }

    private void Y() {
        Iterator<e> it = this.f10287w.iterator();
        while (it.hasNext()) {
            e next = it.next();
            if (next.f10301c.isEmpty()) {
                G(next);
                it.remove();
            }
        }
    }

    private synchronized void Z(Set<C0108d> set) {
        for (C0108d c0108d : set) {
            c0108d.a();
        }
        this.f10283m.removeAll(set);
    }

    private void a0(e eVar) {
        this.f10287w.add(eVar);
        H(eVar);
    }

    private static Object b0(Object obj) {
        return com.google.android.exoplayer2.a.z(obj);
    }

    private static Object d0(Object obj) {
        return com.google.android.exoplayer2.a.A(obj);
    }

    private static Object e0(e eVar, Object obj) {
        return com.google.android.exoplayer2.a.C(eVar.f10300b, obj);
    }

    private Handler f0() {
        return (Handler) b6.a.e(this.f10284n);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean i0(Message message) {
        f fVar;
        int i8 = message.what;
        if (i8 == 0) {
            fVar = (f) l0.j(message.obj);
            this.B = this.B.g(fVar.f10305a, ((Collection) fVar.f10306b).size());
            U(fVar.f10305a, (Collection) fVar.f10306b);
        } else if (i8 == 1) {
            fVar = (f) l0.j(message.obj);
            int i9 = fVar.f10305a;
            int intValue = ((Integer) fVar.f10306b).intValue();
            this.B = (i9 == 0 && intValue == this.B.b()) ? this.B.i() : this.B.c(i9, intValue);
            for (int i10 = intValue - 1; i10 >= i9; i10--) {
                o0(i10);
            }
        } else if (i8 == 2) {
            fVar = (f) l0.j(message.obj);
            x xVar = this.B;
            int i11 = fVar.f10305a;
            x c9 = xVar.c(i11, i11 + 1);
            this.B = c9;
            this.B = c9.g(((Integer) fVar.f10306b).intValue(), 1);
            l0(fVar.f10305a, ((Integer) fVar.f10306b).intValue());
        } else if (i8 != 3) {
            if (i8 == 4) {
                w0();
            } else if (i8 != 5) {
                throw new IllegalStateException();
            } else {
                Z((Set) l0.j(message.obj));
            }
            return true;
        } else {
            fVar = (f) l0.j(message.obj);
            this.B = (x) fVar.f10306b;
        }
        s0(fVar.f10307c);
        return true;
    }

    private void j0(e eVar) {
        if (eVar.f10304f && eVar.f10301c.isEmpty()) {
            this.f10287w.remove(eVar);
            O(eVar);
        }
    }

    private void l0(int i8, int i9) {
        int min = Math.min(i8, i9);
        int max = Math.max(i8, i9);
        int i10 = this.f10285p.get(min).f10303e;
        List<e> list = this.f10285p;
        list.add(i9, list.remove(i8));
        while (min <= max) {
            e eVar = this.f10285p.get(min);
            eVar.f10302d = min;
            eVar.f10303e = i10;
            i10 += eVar.f10299a.c0().t();
            min++;
        }
    }

    private void m0(int i8, int i9, Handler handler, Runnable runnable) {
        b6.a.a((handler == null) == (runnable == null));
        Handler handler2 = this.f10284n;
        List<e> list = this.f10282l;
        list.add(i9, list.remove(i8));
        if (handler2 != null) {
            handler2.obtainMessage(2, new f(i8, Integer.valueOf(i9), X(handler, runnable))).sendToTarget();
        } else if (runnable == null || handler == null) {
        } else {
            handler.post(runnable);
        }
    }

    private void o0(int i8) {
        e remove = this.f10285p.remove(i8);
        this.f10286t.remove(remove.f10300b);
        W(i8, -1, -remove.f10299a.c0().t());
        remove.f10304f = true;
        j0(remove);
    }

    private void q0(int i8, int i9, Handler handler, Runnable runnable) {
        b6.a.a((handler == null) == (runnable == null));
        Handler handler2 = this.f10284n;
        l0.N0(this.f10282l, i8, i9);
        if (handler2 != null) {
            handler2.obtainMessage(1, new f(i8, Integer.valueOf(i9), X(handler, runnable))).sendToTarget();
        } else if (runnable == null || handler == null) {
        } else {
            handler.post(runnable);
        }
    }

    private void r0() {
        s0(null);
    }

    private void s0(C0108d c0108d) {
        if (!this.f10290z) {
            f0().obtainMessage(4).sendToTarget();
            this.f10290z = true;
        }
        if (c0108d != null) {
            this.A.add(c0108d);
        }
    }

    private void t0(x xVar, Handler handler, Runnable runnable) {
        b6.a.a((handler == null) == (runnable == null));
        Handler handler2 = this.f10284n;
        if (handler2 != null) {
            int g02 = g0();
            if (xVar.b() != g02) {
                xVar = xVar.i().g(0, g02);
            }
            handler2.obtainMessage(3, new f(0, xVar, X(handler, runnable))).sendToTarget();
            return;
        }
        if (xVar.b() > 0) {
            xVar = xVar.i();
        }
        this.B = xVar;
        if (runnable == null || handler == null) {
            return;
        }
        handler.post(runnable);
    }

    private void v0(e eVar, h2 h2Var) {
        int t8;
        if (eVar.f10302d + 1 < this.f10285p.size() && (t8 = h2Var.t() - (this.f10285p.get(eVar.f10302d + 1).f10303e - eVar.f10303e)) != 0) {
            W(eVar.f10302d + 1, 0, t8);
        }
        r0();
    }

    private void w0() {
        this.f10290z = false;
        Set<C0108d> set = this.A;
        this.A = new HashSet();
        D(new b(this.f10285p, this.B, this.f10288x));
        f0().obtainMessage(5, set).sendToTarget();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c, com.google.android.exoplayer2.source.a
    public synchronized void C(a6.y yVar) {
        super.C(yVar);
        this.f10284n = new Handler((Handler.Callback) new h5.e(this));
        if (this.f10282l.isEmpty()) {
            w0();
        } else {
            this.B = this.B.g(0, this.f10282l.size());
            U(0, this.f10282l);
            r0();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c, com.google.android.exoplayer2.source.a
    public synchronized void E() {
        super.E();
        this.f10285p.clear();
        this.f10287w.clear();
        this.f10286t.clear();
        this.B = this.B.i();
        Handler handler = this.f10284n;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            this.f10284n = null;
        }
        this.f10290z = false;
        this.A.clear();
        Z(this.f10283m);
    }

    public synchronized void S(int i8, Collection<k> collection, Handler handler, Runnable runnable) {
        V(i8, collection, handler, runnable);
    }

    public synchronized void T(Collection<k> collection) {
        V(this.f10282l.size(), collection, null, null);
    }

    @Override // com.google.android.exoplayer2.source.k
    public j b(k.b bVar, a6.b bVar2, long j8) {
        Object d02 = d0(bVar.f20286a);
        k.b c9 = bVar.c(b0(bVar.f20286a));
        e eVar = this.f10286t.get(d02);
        if (eVar == null) {
            eVar = new e(new c(), this.f10289y);
            eVar.f10304f = true;
            N(eVar, eVar.f10299a);
        }
        a0(eVar);
        eVar.f10301c.add(c9);
        h b9 = eVar.f10299a.b(c9, bVar2, j8);
        this.q.put(b9, eVar);
        Y();
        return b9;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c
    /* renamed from: c0 */
    public k.b I(e eVar, k.b bVar) {
        for (int i8 = 0; i8 < eVar.f10301c.size(); i8++) {
            if (eVar.f10301c.get(i8).f20289d == bVar.f20289d) {
                return bVar.c(e0(eVar, bVar.f20286a));
            }
        }
        return null;
    }

    public synchronized int g0() {
        return this.f10282l.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c
    /* renamed from: h0 */
    public int K(e eVar, int i8) {
        return i8 + eVar.f10303e;
    }

    @Override // com.google.android.exoplayer2.source.k
    public z0 i() {
        return C;
    }

    public synchronized void k0(int i8, int i9, Handler handler, Runnable runnable) {
        m0(i8, i9, handler, runnable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c
    /* renamed from: n0 */
    public void M(e eVar, k kVar, h2 h2Var) {
        v0(eVar, h2Var);
    }

    @Override // com.google.android.exoplayer2.source.k
    public boolean o() {
        return false;
    }

    @Override // com.google.android.exoplayer2.source.k
    public void p(j jVar) {
        e eVar = (e) b6.a.e(this.q.remove(jVar));
        eVar.f10299a.p(jVar);
        eVar.f10301c.remove(((h) jVar).f10439a);
        if (!this.q.isEmpty()) {
            Y();
        }
        j0(eVar);
    }

    public synchronized void p0(int i8, int i9, Handler handler, Runnable runnable) {
        q0(i8, i9, handler, runnable);
    }

    @Override // com.google.android.exoplayer2.source.k
    public synchronized h2 q() {
        return new b(this.f10282l, this.B.b() != this.f10282l.size() ? this.B.i().g(0, this.f10282l.size()) : this.B, this.f10288x);
    }

    public synchronized void u0(x xVar) {
        t0(xVar, null, null);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c, com.google.android.exoplayer2.source.a
    public void y() {
        super.y();
        this.f10287w.clear();
    }

    @Override // com.google.android.exoplayer2.source.c, com.google.android.exoplayer2.source.a
    protected void z() {
    }
}
