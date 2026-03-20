package com.google.android.gms.measurement.internal;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m8 implements Runnable {

    /* renamed from: a  reason: collision with root package name */
    private final /* synthetic */ v f16794a;

    /* renamed from: b  reason: collision with root package name */
    private final /* synthetic */ h7 f16795b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m8(h7 h7Var, v vVar) {
        this.f16794a = vVar;
        this.f16795b = h7Var;
    }

    @Override // java.lang.Runnable
    public final void run() {
        if (this.f16795b.f().x(this.f16794a)) {
            this.f16795b.r().S(false);
        } else {
            this.f16795b.i().H().b("Lower precedence consent source ignored, proposed source", Integer.valueOf(this.f16794a.a()));
        }
    }
}
