package com.google.android.exoplayer2.source;

import com.google.android.exoplayer2.h2;
import com.google.android.exoplayer2.source.k;
import com.google.android.exoplayer2.z0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a0 extends c<Void> {

    /* renamed from: m  reason: collision with root package name */
    private static final Void f10260m = null;

    /* renamed from: l  reason: collision with root package name */
    protected final k f10261l;

    /* JADX INFO: Access modifiers changed from: protected */
    public a0(k kVar) {
        this.f10261l = kVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c, com.google.android.exoplayer2.source.a
    public final void C(a6.y yVar) {
        super.C(yVar);
        Y();
    }

    protected k.b P(k.b bVar) {
        return bVar;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c
    /* renamed from: Q */
    public final k.b I(Void r12, k.b bVar) {
        return P(bVar);
    }

    protected long R(long j8) {
        return j8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c
    /* renamed from: S */
    public final long J(Void r12, long j8) {
        return R(j8);
    }

    protected int T(int i8) {
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c
    /* renamed from: U */
    public final int K(Void r12, int i8) {
        return T(i8);
    }

    protected abstract void V(h2 h2Var);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.exoplayer2.source.c
    /* renamed from: W */
    public final void M(Void r12, k kVar, h2 h2Var) {
        V(h2Var);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void X() {
        N(f10260m, this.f10261l);
    }

    protected void Y() {
        X();
    }

    @Override // com.google.android.exoplayer2.source.k
    public z0 i() {
        return this.f10261l.i();
    }

    @Override // com.google.android.exoplayer2.source.k
    public boolean o() {
        return this.f10261l.o();
    }

    @Override // com.google.android.exoplayer2.source.k
    public h2 q() {
        return this.f10261l.q();
    }
}
