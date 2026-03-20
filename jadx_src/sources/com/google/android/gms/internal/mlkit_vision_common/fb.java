package com.google.android.gms.internal.mlkit_vision_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class fb extends lb {

    /* renamed from: a  reason: collision with root package name */
    private String f15476a;

    /* renamed from: b  reason: collision with root package name */
    private boolean f15477b;

    /* renamed from: c  reason: collision with root package name */
    private int f15478c;

    /* renamed from: d  reason: collision with root package name */
    private byte f15479d;

    @Override // com.google.android.gms.internal.mlkit_vision_common.lb
    public final lb a(boolean z4) {
        this.f15477b = true;
        this.f15479d = (byte) (1 | this.f15479d);
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.lb
    public final lb b(int i8) {
        this.f15478c = 1;
        this.f15479d = (byte) (this.f15479d | 2);
        return this;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.lb
    public final mb c() {
        String str;
        if (this.f15479d != 3 || (str = this.f15476a) == null) {
            StringBuilder sb = new StringBuilder();
            if (this.f15476a == null) {
                sb.append(" libraryName");
            }
            if ((this.f15479d & 1) == 0) {
                sb.append(" enableFirelog");
            }
            if ((this.f15479d & 2) == 0) {
                sb.append(" firelogEventType");
            }
            throw new IllegalStateException("Missing required properties:".concat(sb.toString()));
        }
        return new ib(str, this.f15477b, this.f15478c, null);
    }

    public final lb d(String str) {
        this.f15476a = "vision-common";
        return this;
    }
}
