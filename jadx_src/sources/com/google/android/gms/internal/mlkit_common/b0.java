package com.google.android.gms.internal.mlkit_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b0 extends f0 {

    /* renamed from: a  reason: collision with root package name */
    private String f12963a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f12964b;

    /* renamed from: c  reason: collision with root package name */
    private int f12965c;

    /* renamed from: d  reason: collision with root package name */
    private byte f12966d;

    @Override // com.google.android.gms.internal.mlkit_common.f0
    public final f0 a(boolean z4) {
        this.f12964b = true;
        this.f12966d = (byte) (1 | this.f12966d);
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_common.f0
    public final f0 b(int i8) {
        this.f12965c = 1;
        this.f12966d = (byte) (this.f12966d | 2);
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_common.f0
    public final g0 c() {
        String str;
        if (this.f12966d != 3 || (str = this.f12963a) == null) {
            StringBuilder sb = new StringBuilder();
            if (this.f12963a == null) {
                sb.append(" libraryName");
            }
            if ((this.f12966d & 1) == 0) {
                sb.append(" enableFirelog");
            }
            if ((this.f12966d & 2) == 0) {
                sb.append(" firelogEventType");
            }
            throw new IllegalStateException("Missing required properties:".concat(sb.toString()));
        }
        return new d0(str, this.f12964b, this.f12965c, null);
    }

    public final f0 d(String str) {
        this.f12963a = "common";
        return this;
    }
}
