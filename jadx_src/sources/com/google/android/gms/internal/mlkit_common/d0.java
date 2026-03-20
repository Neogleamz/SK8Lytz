package com.google.android.gms.internal.mlkit_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d0 extends g0 {

    /* renamed from: a  reason: collision with root package name */
    private final String f12967a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f12968b;

    /* renamed from: c  reason: collision with root package name */
    private final int f12969c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ d0(String str, boolean z4, int i8, c0 c0Var) {
        this.f12967a = str;
        this.f12968b = z4;
        this.f12969c = i8;
    }

    @Override // com.google.android.gms.internal.mlkit_common.g0
    public final int a() {
        return this.f12969c;
    }

    @Override // com.google.android.gms.internal.mlkit_common.g0
    public final String b() {
        return this.f12967a;
    }

    @Override // com.google.android.gms.internal.mlkit_common.g0
    public final boolean c() {
        return this.f12968b;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof g0) {
            g0 g0Var = (g0) obj;
            if (this.f12967a.equals(g0Var.b()) && this.f12968b == g0Var.c() && this.f12969c == g0Var.a()) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return ((((this.f12967a.hashCode() ^ 1000003) * 1000003) ^ (true != this.f12968b ? 1237 : 1231)) * 1000003) ^ this.f12969c;
    }

    public final String toString() {
        String str = this.f12967a;
        boolean z4 = this.f12968b;
        int i8 = this.f12969c;
        return "MLKitLoggingOptions{libraryName=" + str + ", enableFirelog=" + z4 + ", firelogEventType=" + i8 + "}";
    }
}
