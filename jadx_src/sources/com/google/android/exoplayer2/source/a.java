package com.google.android.exoplayer2.source;

import android.os.Handler;
import android.os.Looper;
import com.google.android.exoplayer2.drm.h;
import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.source.l;
import j4.t1;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a implements k {

    /* renamed from: a  reason: collision with root package name */
    private final ArrayList<k.c> f10253a = new ArrayList<>(1);

    /* renamed from: b  reason: collision with root package name */
    private final HashSet<k.c> f10254b = new HashSet<>(1);

    /* renamed from: c  reason: collision with root package name */
    private final l.a f10255c = new l.a();

    /* renamed from: d  reason: collision with root package name */
    private final h.a f10256d = new h.a();

    /* renamed from: e  reason: collision with root package name */
    private Looper f10257e;

    /* renamed from: f  reason: collision with root package name */
    private h2 f10258f;

    /* renamed from: g  reason: collision with root package name */
    private t1 f10259g;

    /* JADX INFO: Access modifiers changed from: protected */
    public final t1 A() {
        return (t1) b6.a.h(this.f10259g);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean B() {
        return !this.f10254b.isEmpty();
    }

    protected abstract void C(a6.y yVar);

    /* JADX INFO: Access modifiers changed from: protected */
    public final void D(h2 h2Var) {
        this.f10258f = h2Var;
        Iterator<k.c> it = this.f10253a.iterator();
        while (it.hasNext()) {
            it.next().a(this, h2Var);
        }
    }

    protected abstract void E();

    @Override // com.google.android.exoplayer2.source.k
    public final void a(k.c cVar, a6.y yVar, t1 t1Var) {
        Looper myLooper = Looper.myLooper();
        Looper looper = this.f10257e;
        b6.a.a(looper == null || looper == myLooper);
        this.f10259g = t1Var;
        h2 h2Var = this.f10258f;
        this.f10253a.add(cVar);
        if (this.f10257e == null) {
            this.f10257e = myLooper;
            this.f10254b.add(cVar);
            C(yVar);
        } else if (h2Var != null) {
            r(cVar);
            cVar.a(this, h2Var);
        }
    }

    @Override // com.google.android.exoplayer2.source.k
    public final void c(k.c cVar) {
        this.f10253a.remove(cVar);
        if (!this.f10253a.isEmpty()) {
            g(cVar);
            return;
        }
        this.f10257e = null;
        this.f10258f = null;
        this.f10259g = null;
        this.f10254b.clear();
        E();
    }

    @Override // com.google.android.exoplayer2.source.k
    public final void e(Handler handler, l lVar) {
        b6.a.e(handler);
        b6.a.e(lVar);
        this.f10255c.g(handler, lVar);
    }

    @Override // com.google.android.exoplayer2.source.k
    public final void f(l lVar) {
        this.f10255c.C(lVar);
    }

    @Override // com.google.android.exoplayer2.source.k
    public final void g(k.c cVar) {
        boolean z4 = !this.f10254b.isEmpty();
        this.f10254b.remove(cVar);
        if (z4 && this.f10254b.isEmpty()) {
            y();
        }
    }

    @Override // com.google.android.exoplayer2.source.k
    public final void l(Handler handler, com.google.android.exoplayer2.drm.h hVar) {
        b6.a.e(handler);
        b6.a.e(hVar);
        this.f10256d.g(handler, hVar);
    }

    @Override // com.google.android.exoplayer2.source.k
    public final void m(com.google.android.exoplayer2.drm.h hVar) {
        this.f10256d.t(hVar);
    }

    @Override // com.google.android.exoplayer2.source.k
    public final void r(k.c cVar) {
        b6.a.e(this.f10257e);
        boolean isEmpty = this.f10254b.isEmpty();
        this.f10254b.add(cVar);
        if (isEmpty) {
            z();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final h.a s(int i8, k.b bVar) {
        return this.f10256d.u(i8, bVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final h.a u(k.b bVar) {
        return this.f10256d.u(0, bVar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final l.a v(int i8, k.b bVar, long j8) {
        return this.f10255c.F(i8, bVar, j8);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final l.a w(k.b bVar) {
        return this.f10255c.F(0, bVar, 0L);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final l.a x(k.b bVar, long j8) {
        b6.a.e(bVar);
        return this.f10255c.F(0, bVar, j8);
    }

    protected void y() {
    }

    protected void z() {
    }
}
