package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class v4 extends w1 {

    /* renamed from: b  reason: collision with root package name */
    private boolean f17047b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v4(f6 f6Var) {
        super(f6Var);
        this.f16485a.k();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void t() {
        if (!x()) {
            throw new IllegalStateException("Not initialized");
        }
    }

    public final void u() {
        if (this.f17047b) {
            throw new IllegalStateException("Can't initialize twice");
        }
        if (y()) {
            return;
        }
        this.f16485a.P();
        this.f17047b = true;
    }

    public final void v() {
        if (this.f17047b) {
            throw new IllegalStateException("Can't initialize twice");
        }
        w();
        this.f16485a.P();
        this.f17047b = true;
    }

    protected void w() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean x() {
        return this.f17047b;
    }

    protected abstract boolean y();
}
