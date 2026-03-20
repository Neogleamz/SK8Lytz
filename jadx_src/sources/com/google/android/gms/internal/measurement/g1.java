package com.google.android.gms.internal.measurement;

import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g1 extends l1 {

    /* renamed from: a  reason: collision with root package name */
    private String f12191a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f12192b;

    /* renamed from: c  reason: collision with root package name */
    private boolean f12193c;

    /* renamed from: d  reason: collision with root package name */
    private zzcl f12194d;

    /* renamed from: e  reason: collision with root package name */
    private byte f12195e;

    @Override // com.google.android.gms.internal.measurement.l1
    public final j1 a() {
        if (this.f12195e != 3 || this.f12191a == null || this.f12194d == null) {
            StringBuilder sb = new StringBuilder();
            if (this.f12191a == null) {
                sb.append(" fileOwner");
            }
            if ((this.f12195e & 1) == 0) {
                sb.append(" hasDifferentDmaOwner");
            }
            if ((this.f12195e & 2) == 0) {
                sb.append(" skipChecks");
            }
            if (this.f12194d == null) {
                sb.append(" filePurpose");
            }
            String valueOf = String.valueOf(sb);
            throw new IllegalStateException("Missing required properties:" + valueOf);
        }
        return new d1(this.f12191a, this.f12194d);
    }

    @Override // com.google.android.gms.internal.measurement.l1
    public final l1 b(zzcl zzclVar) {
        Objects.requireNonNull(zzclVar, "Null filePurpose");
        this.f12194d = zzclVar;
        return this;
    }

    @Override // com.google.android.gms.internal.measurement.l1
    public final l1 c(boolean z4) {
        this.f12192b = false;
        this.f12195e = (byte) (this.f12195e | 1);
        return this;
    }

    @Override // com.google.android.gms.internal.measurement.l1
    public final l1 d(boolean z4) {
        this.f12193c = false;
        this.f12195e = (byte) (this.f12195e | 2);
        return this;
    }

    public final l1 e(String str) {
        this.f12191a = str;
        return this;
    }
}
