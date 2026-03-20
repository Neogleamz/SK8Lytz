package com.google.android.gms.common.api.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.UnsupportedApiCallException;
import com.google.android.gms.common.api.a;
import com.google.android.gms.common.api.c;
import com.google.android.gms.common.api.internal.c;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r implements c.a, c.b {

    /* renamed from: b */
    private final a.f f11681b;

    /* renamed from: c */
    private final l6.b f11682c;

    /* renamed from: d */
    private final j f11683d;

    /* renamed from: g */
    private final int f11686g;

    /* renamed from: h */
    private final l6.b0 f11687h;

    /* renamed from: j */
    private boolean f11688j;

    /* renamed from: n */
    final /* synthetic */ b f11692n;

    /* renamed from: a */
    private final Queue f11680a = new LinkedList();

    /* renamed from: e */
    private final Set f11684e = new HashSet();

    /* renamed from: f */
    private final Map f11685f = new HashMap();

    /* renamed from: k */
    private final List f11689k = new ArrayList();

    /* renamed from: l */
    private ConnectionResult f11690l = null;

    /* renamed from: m */
    private int f11691m = 0;

    public r(b bVar, com.google.android.gms.common.api.b bVar2) {
        Handler handler;
        Context context;
        Handler handler2;
        this.f11692n = bVar;
        handler = bVar.f11619t;
        a.f m8 = bVar2.m(handler.getLooper(), this);
        this.f11681b = m8;
        this.f11682c = bVar2.i();
        this.f11683d = new j();
        this.f11686g = bVar2.l();
        if (!m8.m()) {
            this.f11687h = null;
            return;
        }
        context = bVar.f11611g;
        handler2 = bVar.f11619t;
        this.f11687h = bVar2.n(context, handler2);
    }

    public static /* bridge */ /* synthetic */ void B(r rVar, s sVar) {
        if (rVar.f11689k.contains(sVar) && !rVar.f11688j) {
            if (rVar.f11681b.isConnected()) {
                rVar.i();
            } else {
                rVar.E();
            }
        }
    }

    public static /* bridge */ /* synthetic */ void C(r rVar, s sVar) {
        Handler handler;
        Handler handler2;
        Feature feature;
        Feature[] g8;
        if (rVar.f11689k.remove(sVar)) {
            handler = rVar.f11692n.f11619t;
            handler.removeMessages(15, sVar);
            handler2 = rVar.f11692n.f11619t;
            handler2.removeMessages(16, sVar);
            feature = sVar.f11694b;
            ArrayList arrayList = new ArrayList(rVar.f11680a.size());
            for (g0 g0Var : rVar.f11680a) {
                if ((g0Var instanceof l6.q) && (g8 = ((l6.q) g0Var).g(rVar)) != null && u6.b.b(g8, feature)) {
                    arrayList.add(g0Var);
                }
            }
            int size = arrayList.size();
            for (int i8 = 0; i8 < size; i8++) {
                g0 g0Var2 = (g0) arrayList.get(i8);
                rVar.f11680a.remove(g0Var2);
                g0Var2.b(new UnsupportedApiCallException(feature));
            }
        }
    }

    public static /* bridge */ /* synthetic */ boolean N(r rVar, boolean z4) {
        return rVar.q(false);
    }

    private final Feature b(Feature[] featureArr) {
        if (featureArr != null && featureArr.length != 0) {
            Feature[] k8 = this.f11681b.k();
            if (k8 == null) {
                k8 = new Feature[0];
            }
            k0.a aVar = new k0.a(k8.length);
            for (Feature feature : k8) {
                aVar.put(feature.t(), Long.valueOf(feature.u()));
            }
            for (Feature feature2 : featureArr) {
                Long l8 = (Long) aVar.get(feature2.t());
                if (l8 == null || l8.longValue() < feature2.u()) {
                    return feature2;
                }
            }
        }
        return null;
    }

    private final void c(ConnectionResult connectionResult) {
        for (l6.d0 d0Var : this.f11684e) {
            d0Var.b(this.f11682c, connectionResult, n6.i.a(connectionResult, ConnectionResult.f11523e) ? this.f11681b.f() : null);
        }
        this.f11684e.clear();
    }

    public final void g(Status status) {
        Handler handler;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        h(status, null, false);
    }

    private final void h(Status status, Exception exc, boolean z4) {
        Handler handler;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        if ((status == null) == (exc == null)) {
            throw new IllegalArgumentException("Status XOR exception should be null");
        }
        Iterator it = this.f11680a.iterator();
        while (it.hasNext()) {
            g0 g0Var = (g0) it.next();
            if (!z4 || g0Var.f11653a == 2) {
                if (status != null) {
                    g0Var.a(status);
                } else {
                    g0Var.b(exc);
                }
                it.remove();
            }
        }
    }

    private final void i() {
        ArrayList arrayList = new ArrayList(this.f11680a);
        int size = arrayList.size();
        for (int i8 = 0; i8 < size; i8++) {
            g0 g0Var = (g0) arrayList.get(i8);
            if (!this.f11681b.isConnected()) {
                return;
            }
            if (o(g0Var)) {
                this.f11680a.remove(g0Var);
            }
        }
    }

    public final void j() {
        D();
        c(ConnectionResult.f11523e);
        n();
        Iterator it = this.f11685f.values().iterator();
        while (it.hasNext()) {
            l6.u uVar = (l6.u) it.next();
            if (b(uVar.f21779a.c()) == null) {
                try {
                    uVar.f21779a.d(this.f11681b, new j7.k<>());
                } catch (DeadObjectException unused) {
                    d(3);
                    this.f11681b.c("DeadObjectException thrown while calling register listener method.");
                } catch (RemoteException unused2) {
                }
            }
            it.remove();
        }
        i();
        l();
    }

    public final void k(int i8) {
        Handler handler;
        Handler handler2;
        long j8;
        Handler handler3;
        Handler handler4;
        long j9;
        n6.y yVar;
        D();
        this.f11688j = true;
        this.f11683d.c(i8, this.f11681b.l());
        b bVar = this.f11692n;
        handler = bVar.f11619t;
        handler2 = bVar.f11619t;
        Message obtain = Message.obtain(handler2, 9, this.f11682c);
        j8 = this.f11692n.f11605a;
        handler.sendMessageDelayed(obtain, j8);
        b bVar2 = this.f11692n;
        handler3 = bVar2.f11619t;
        handler4 = bVar2.f11619t;
        Message obtain2 = Message.obtain(handler4, 11, this.f11682c);
        j9 = this.f11692n.f11606b;
        handler3.sendMessageDelayed(obtain2, j9);
        yVar = this.f11692n.f11613j;
        yVar.c();
        for (l6.u uVar : this.f11685f.values()) {
            uVar.f21781c.run();
        }
    }

    private final void l() {
        Handler handler;
        Handler handler2;
        Handler handler3;
        long j8;
        handler = this.f11692n.f11619t;
        handler.removeMessages(12, this.f11682c);
        b bVar = this.f11692n;
        handler2 = bVar.f11619t;
        handler3 = bVar.f11619t;
        Message obtainMessage = handler3.obtainMessage(12, this.f11682c);
        j8 = this.f11692n.f11607c;
        handler2.sendMessageDelayed(obtainMessage, j8);
    }

    private final void m(g0 g0Var) {
        g0Var.d(this.f11683d, P());
        try {
            g0Var.c(this);
        } catch (DeadObjectException unused) {
            d(1);
            this.f11681b.c("DeadObjectException thrown while running ApiCallRunner.");
        }
    }

    private final void n() {
        Handler handler;
        Handler handler2;
        if (this.f11688j) {
            handler = this.f11692n.f11619t;
            handler.removeMessages(11, this.f11682c);
            handler2 = this.f11692n.f11619t;
            handler2.removeMessages(9, this.f11682c);
            this.f11688j = false;
        }
    }

    private final boolean o(g0 g0Var) {
        boolean z4;
        Handler handler;
        Handler handler2;
        long j8;
        Handler handler3;
        Handler handler4;
        long j9;
        Handler handler5;
        Handler handler6;
        Handler handler7;
        long j10;
        if (!(g0Var instanceof l6.q)) {
            m(g0Var);
            return true;
        }
        l6.q qVar = (l6.q) g0Var;
        Feature b9 = b(qVar.g(this));
        if (b9 == null) {
            m(g0Var);
            return true;
        }
        String name = this.f11681b.getClass().getName();
        String t8 = b9.t();
        long u8 = b9.u();
        Log.w("GoogleApiManager", name + " could not execute call because it requires feature (" + t8 + ", " + u8 + ").");
        z4 = this.f11692n.f11620w;
        if (!z4 || !qVar.f(this)) {
            qVar.b(new UnsupportedApiCallException(b9));
            return true;
        }
        s sVar = new s(this.f11682c, b9, null);
        int indexOf = this.f11689k.indexOf(sVar);
        if (indexOf >= 0) {
            s sVar2 = (s) this.f11689k.get(indexOf);
            handler5 = this.f11692n.f11619t;
            handler5.removeMessages(15, sVar2);
            b bVar = this.f11692n;
            handler6 = bVar.f11619t;
            handler7 = bVar.f11619t;
            Message obtain = Message.obtain(handler7, 15, sVar2);
            j10 = this.f11692n.f11605a;
            handler6.sendMessageDelayed(obtain, j10);
            return false;
        }
        this.f11689k.add(sVar);
        b bVar2 = this.f11692n;
        handler = bVar2.f11619t;
        handler2 = bVar2.f11619t;
        Message obtain2 = Message.obtain(handler2, 15, sVar);
        j8 = this.f11692n.f11605a;
        handler.sendMessageDelayed(obtain2, j8);
        b bVar3 = this.f11692n;
        handler3 = bVar3.f11619t;
        handler4 = bVar3.f11619t;
        Message obtain3 = Message.obtain(handler4, 16, sVar);
        j9 = this.f11692n.f11606b;
        handler3.sendMessageDelayed(obtain3, j9);
        ConnectionResult connectionResult = new ConnectionResult(2, null);
        if (p(connectionResult)) {
            return false;
        }
        this.f11692n.g(connectionResult, this.f11686g);
        return false;
    }

    private final boolean p(ConnectionResult connectionResult) {
        Object obj;
        k kVar;
        Set set;
        k kVar2;
        obj = b.f11604z;
        synchronized (obj) {
            b bVar = this.f11692n;
            kVar = bVar.f11617n;
            if (kVar != null) {
                set = bVar.f11618p;
                if (set.contains(this.f11682c)) {
                    kVar2 = this.f11692n.f11617n;
                    kVar2.s(connectionResult, this.f11686g);
                    return true;
                }
            }
            return false;
        }
    }

    public final boolean q(boolean z4) {
        Handler handler;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        if (this.f11681b.isConnected() && this.f11685f.size() == 0) {
            if (!this.f11683d.e()) {
                this.f11681b.c("Timing out service connection.");
                return true;
            }
            if (z4) {
                l();
            }
            return false;
        }
        return false;
    }

    public static /* bridge */ /* synthetic */ l6.b w(r rVar) {
        return rVar.f11682c;
    }

    public static /* bridge */ /* synthetic */ void y(r rVar, Status status) {
        rVar.g(status);
    }

    public final void D() {
        Handler handler;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        this.f11690l = null;
    }

    public final void E() {
        Handler handler;
        ConnectionResult connectionResult;
        n6.y yVar;
        Context context;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        if (this.f11681b.isConnected() || this.f11681b.e()) {
            return;
        }
        try {
            b bVar = this.f11692n;
            yVar = bVar.f11613j;
            context = bVar.f11611g;
            int b9 = yVar.b(context, this.f11681b);
            if (b9 != 0) {
                ConnectionResult connectionResult2 = new ConnectionResult(b9, null);
                String name = this.f11681b.getClass().getName();
                String obj = connectionResult2.toString();
                Log.w("GoogleApiManager", "The service for " + name + " is not available: " + obj);
                H(connectionResult2, null);
                return;
            }
            b bVar2 = this.f11692n;
            a.f fVar = this.f11681b;
            u uVar = new u(bVar2, fVar, this.f11682c);
            if (fVar.m()) {
                ((l6.b0) n6.j.l(this.f11687h)).v(uVar);
            }
            try {
                this.f11681b.g(uVar);
            } catch (SecurityException e8) {
                e = e8;
                connectionResult = new ConnectionResult(10);
                H(connectionResult, e);
            }
        } catch (IllegalStateException e9) {
            e = e9;
            connectionResult = new ConnectionResult(10);
        }
    }

    public final void F(g0 g0Var) {
        Handler handler;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        if (this.f11681b.isConnected()) {
            if (o(g0Var)) {
                l();
                return;
            } else {
                this.f11680a.add(g0Var);
                return;
            }
        }
        this.f11680a.add(g0Var);
        ConnectionResult connectionResult = this.f11690l;
        if (connectionResult == null || !connectionResult.D0()) {
            E();
        } else {
            H(this.f11690l, null);
        }
    }

    public final void G() {
        this.f11691m++;
    }

    public final void H(ConnectionResult connectionResult, Exception exc) {
        Handler handler;
        n6.y yVar;
        boolean z4;
        Status h8;
        Status h9;
        Status h10;
        Handler handler2;
        Handler handler3;
        long j8;
        Handler handler4;
        Status status;
        Handler handler5;
        Handler handler6;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        l6.b0 b0Var = this.f11687h;
        if (b0Var != null) {
            b0Var.x();
        }
        D();
        yVar = this.f11692n.f11613j;
        yVar.c();
        c(connectionResult);
        if ((this.f11681b instanceof p6.e) && connectionResult.t() != 24) {
            this.f11692n.f11608d = true;
            b bVar = this.f11692n;
            handler5 = bVar.f11619t;
            handler6 = bVar.f11619t;
            handler5.sendMessageDelayed(handler6.obtainMessage(19), 300000L);
        }
        if (connectionResult.t() == 4) {
            status = b.f11603y;
            g(status);
        } else if (this.f11680a.isEmpty()) {
            this.f11690l = connectionResult;
        } else if (exc != null) {
            handler4 = this.f11692n.f11619t;
            n6.j.d(handler4);
            h(null, exc, false);
        } else {
            z4 = this.f11692n.f11620w;
            if (!z4) {
                h8 = b.h(this.f11682c, connectionResult);
                g(h8);
                return;
            }
            h9 = b.h(this.f11682c, connectionResult);
            h(h9, null, true);
            if (this.f11680a.isEmpty() || p(connectionResult) || this.f11692n.g(connectionResult, this.f11686g)) {
                return;
            }
            if (connectionResult.t() == 18) {
                this.f11688j = true;
            }
            if (!this.f11688j) {
                h10 = b.h(this.f11682c, connectionResult);
                g(h10);
                return;
            }
            b bVar2 = this.f11692n;
            handler2 = bVar2.f11619t;
            handler3 = bVar2.f11619t;
            Message obtain = Message.obtain(handler3, 9, this.f11682c);
            j8 = this.f11692n.f11605a;
            handler2.sendMessageDelayed(obtain, j8);
        }
    }

    public final void I(ConnectionResult connectionResult) {
        Handler handler;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        a.f fVar = this.f11681b;
        String name = fVar.getClass().getName();
        String valueOf = String.valueOf(connectionResult);
        fVar.c("onSignInFailed for " + name + " with " + valueOf);
        H(connectionResult, null);
    }

    public final void J(l6.d0 d0Var) {
        Handler handler;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        this.f11684e.add(d0Var);
    }

    public final void K() {
        Handler handler;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        if (this.f11688j) {
            E();
        }
    }

    public final void L() {
        Handler handler;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        g(b.f11602x);
        this.f11683d.d();
        for (c.a aVar : (c.a[]) this.f11685f.keySet().toArray(new c.a[0])) {
            F(new f0(aVar, new j7.k()));
        }
        c(new ConnectionResult(4));
        if (this.f11681b.isConnected()) {
            this.f11681b.h(new q(this));
        }
    }

    public final void M() {
        Handler handler;
        com.google.android.gms.common.a aVar;
        Context context;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        if (this.f11688j) {
            n();
            b bVar = this.f11692n;
            aVar = bVar.f11612h;
            context = bVar.f11611g;
            g(aVar.g(context) == 18 ? new Status(21, "Connection timed out waiting for Google Play services update to complete.") : new Status(22, "API failed to connect while resuming due to an unknown error."));
            this.f11681b.c("Timing out connection while resuming.");
        }
    }

    public final boolean O() {
        return this.f11681b.isConnected();
    }

    public final boolean P() {
        return this.f11681b.m();
    }

    public final boolean a() {
        return q(true);
    }

    @Override // l6.c
    public final void d(int i8) {
        Handler handler;
        Handler handler2;
        Looper myLooper = Looper.myLooper();
        handler = this.f11692n.f11619t;
        if (myLooper == handler.getLooper()) {
            k(i8);
            return;
        }
        handler2 = this.f11692n.f11619t;
        handler2.post(new o(this, i8));
    }

    @Override // l6.h
    public final void e(ConnectionResult connectionResult) {
        H(connectionResult, null);
    }

    @Override // l6.c
    public final void f(Bundle bundle) {
        Handler handler;
        Handler handler2;
        Looper myLooper = Looper.myLooper();
        handler = this.f11692n.f11619t;
        if (myLooper == handler.getLooper()) {
            j();
            return;
        }
        handler2 = this.f11692n.f11619t;
        handler2.post(new n(this));
    }

    public final int r() {
        return this.f11686g;
    }

    public final int s() {
        return this.f11691m;
    }

    public final ConnectionResult t() {
        Handler handler;
        handler = this.f11692n.f11619t;
        n6.j.d(handler);
        return this.f11690l;
    }

    public final a.f v() {
        return this.f11681b;
    }

    public final Map x() {
        return this.f11685f;
    }
}
