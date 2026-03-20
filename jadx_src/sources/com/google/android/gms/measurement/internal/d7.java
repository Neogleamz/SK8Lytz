package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class d7 extends e7 {

    /* renamed from: b  reason: collision with root package name */
    private boolean f16457b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d7(f6 f6Var) {
        super(f6Var);
        this.f16485a.k();
    }

    protected void m() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void n() {
        if (!q()) {
            throw new IllegalStateException("Not initialized");
        }
    }

    public final void o() {
        if (this.f16457b) {
            throw new IllegalStateException("Can't initialize twice");
        }
        if (r()) {
            return;
        }
        this.f16485a.P();
        this.f16457b = true;
    }

    public final void p() {
        if (this.f16457b) {
            throw new IllegalStateException("Can't initialize twice");
        }
        m();
        this.f16485a.P();
        this.f16457b = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean q() {
        return this.f16457b;
    }

    protected abstract boolean r();
}
