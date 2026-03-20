package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class i4 implements u3 {

    /* renamed from: a  reason: collision with root package name */
    private final x3 f14782a;

    /* renamed from: b  reason: collision with root package name */
    private final String f14783b;

    /* renamed from: c  reason: collision with root package name */
    private final Object[] f14784c;

    /* renamed from: d  reason: collision with root package name */
    private final int f14785d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public i4(x3 x3Var, String str, Object[] objArr) {
        this.f14782a = x3Var;
        this.f14783b = str;
        this.f14784c = objArr;
        char charAt = str.charAt(0);
        if (charAt < 55296) {
            this.f14785d = charAt;
            return;
        }
        int i8 = charAt & 8191;
        int i9 = 1;
        int i10 = 13;
        while (true) {
            int i11 = i9 + 1;
            char charAt2 = str.charAt(i9);
            if (charAt2 < 55296) {
                this.f14785d = i8 | (charAt2 << i10);
                return;
            }
            i8 |= (charAt2 & 8191) << i10;
            i10 += 13;
            i9 = i11;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.u3
    public final int a() {
        return (this.f14785d & 1) != 0 ? 1 : 2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String b() {
        return this.f14783b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object[] c() {
        return this.f14784c;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.u3
    public final x3 zza() {
        return this.f14782a;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.u3
    public final boolean zzb() {
        return (this.f14785d & 2) == 2;
    }
}
