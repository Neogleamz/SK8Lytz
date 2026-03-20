package com.google.android.gms.internal.measurement;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class va implements ga {

    /* renamed from: a  reason: collision with root package name */
    private final ia f12586a;

    /* renamed from: b  reason: collision with root package name */
    private final String f12587b;

    /* renamed from: c  reason: collision with root package name */
    private final Object[] f12588c;

    /* renamed from: d  reason: collision with root package name */
    private final int f12589d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public va(ia iaVar, String str, Object[] objArr) {
        this.f12586a = iaVar;
        this.f12587b = str;
        this.f12588c = objArr;
        char charAt = str.charAt(0);
        if (charAt < 55296) {
            this.f12589d = charAt;
            return;
        }
        int i8 = charAt & 8191;
        int i9 = 13;
        int i10 = 1;
        while (true) {
            int i11 = i10 + 1;
            char charAt2 = str.charAt(i10);
            if (charAt2 < 55296) {
                this.f12589d = i8 | (charAt2 << i9);
                return;
            }
            i8 |= (charAt2 & 8191) << i9;
            i9 += 13;
            i10 = i11;
        }
    }

    @Override // com.google.android.gms.internal.measurement.ga
    public final boolean a() {
        return (this.f12589d & 2) == 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String b() {
        return this.f12587b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object[] c() {
        return this.f12588c;
    }

    @Override // com.google.android.gms.internal.measurement.ga
    public final ia zza() {
        return this.f12586a;
    }

    @Override // com.google.android.gms.internal.measurement.ga
    public final zzlr zzb() {
        int i8 = this.f12589d;
        return (i8 & 1) != 0 ? zzlr.PROTO2 : (i8 & 4) == 4 ? zzlr.EDITIONS : zzlr.PROTO3;
    }
}
