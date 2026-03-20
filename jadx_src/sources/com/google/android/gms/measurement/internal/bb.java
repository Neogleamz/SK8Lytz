package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class bb extends cb {

    /* renamed from: c  reason: collision with root package name */
    private boolean f16351c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public bb(gb gbVar) {
        super(gbVar);
        this.f16446b.s0();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void s() {
        if (!u()) {
            throw new IllegalStateException("Not initialized");
        }
    }

    public final void t() {
        if (this.f16351c) {
            throw new IllegalStateException("Can't initialize twice");
        }
        v();
        this.f16446b.r0();
        this.f16351c = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean u() {
        return this.f16351c;
    }

    protected abstract boolean v();
}
