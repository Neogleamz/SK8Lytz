package com.google.android.exoplayer2;

import com.google.android.exoplayer2.h2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class e implements y1 {

    /* renamed from: a  reason: collision with root package name */
    protected final h2.d f9640a = new h2.d();

    private int S() {
        int J = J();
        if (J == 1) {
            return 0;
        }
        return J;
    }

    private void T(int i8) {
        U(F(), -9223372036854775807L, i8, true);
    }

    private void V(long j8, int i8) {
        U(F(), j8, i8, false);
    }

    private void W(int i8, int i9) {
        U(i8, -9223372036854775807L, i9, false);
    }

    private void X(int i8) {
        int Q = Q();
        if (Q == -1) {
            return;
        }
        if (Q == F()) {
            T(i8);
        } else {
            W(Q, i8);
        }
    }

    @Override // com.google.android.exoplayer2.y1
    public final boolean D() {
        return Q() != -1;
    }

    @Override // com.google.android.exoplayer2.y1
    public final boolean H() {
        h2 K = K();
        return !K.u() && K.r(F(), this.f9640a).f9778j;
    }

    @Override // com.google.android.exoplayer2.y1
    public final boolean O() {
        h2 K = K();
        return !K.u() && K.r(F(), this.f9640a).h();
    }

    public final long P() {
        h2 K = K();
        if (K.u()) {
            return -9223372036854775807L;
        }
        return K.r(F(), this.f9640a).f();
    }

    public final int Q() {
        h2 K = K();
        if (K.u()) {
            return -1;
        }
        return K.i(F(), S(), M());
    }

    public final int R() {
        h2 K = K();
        if (K.u()) {
            return -1;
        }
        return K.p(F(), S(), M());
    }

    public abstract void U(int i8, long j8, int i9, boolean z4);

    @Override // com.google.android.exoplayer2.y1
    public final void j(int i8, long j8) {
        U(i8, j8, 10, false);
    }

    @Override // com.google.android.exoplayer2.y1
    public final void o() {
        W(F(), 4);
    }

    @Override // com.google.android.exoplayer2.y1
    public final boolean p() {
        return R() != -1;
    }

    @Override // com.google.android.exoplayer2.y1
    public final int r() {
        return K().t();
    }

    @Override // com.google.android.exoplayer2.y1
    public final void seekTo(long j8) {
        V(j8, 5);
    }

    @Override // com.google.android.exoplayer2.y1
    public final boolean x() {
        h2 K = K();
        return !K.u() && K.r(F(), this.f9640a).f9777h;
    }

    @Override // com.google.android.exoplayer2.y1
    public final void y() {
        X(8);
    }
}
