package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class cg extends hg {

    /* renamed from: a  reason: collision with root package name */
    private String f13352a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f13353b;

    /* renamed from: c  reason: collision with root package name */
    private int f13354c;

    /* renamed from: d  reason: collision with root package name */
    private byte f13355d;

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.hg
    public final hg a(boolean z4) {
        this.f13353b = true;
        this.f13355d = (byte) (1 | this.f13355d);
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.hg
    public final hg b(int i8) {
        this.f13354c = 1;
        this.f13355d = (byte) (this.f13355d | 2);
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.hg
    public final ig c() {
        String str;
        if (this.f13355d != 3 || (str = this.f13352a) == null) {
            StringBuilder sb = new StringBuilder();
            if (this.f13352a == null) {
                sb.append(" libraryName");
            }
            if ((this.f13355d & 1) == 0) {
                sb.append(" enableFirelog");
            }
            if ((this.f13355d & 2) == 0) {
                sb.append(" firelogEventType");
            }
            throw new IllegalStateException("Missing required properties:".concat(sb.toString()));
        }
        return new eg(str, this.f13353b, this.f13354c, null);
    }

    public final hg d(String str) {
        this.f13352a = str;
        return this;
    }
}
