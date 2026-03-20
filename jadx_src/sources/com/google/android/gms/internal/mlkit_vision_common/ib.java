package com.google.android.gms.internal.mlkit_vision_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class ib extends mb {

    /* renamed from: a  reason: collision with root package name */
    private final String f15575a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f15576b;

    /* renamed from: c  reason: collision with root package name */
    private final int f15577c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ ib(String str, boolean z4, int i8, gb gbVar) {
        this.f15575a = str;
        this.f15576b = z4;
        this.f15577c = i8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.mb
    public final int a() {
        return this.f15577c;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.mb
    public final String b() {
        return this.f15575a;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_common.mb
    public final boolean c() {
        return this.f15576b;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof mb) {
            mb mbVar = (mb) obj;
            if (this.f15575a.equals(mbVar.b()) && this.f15576b == mbVar.c() && this.f15577c == mbVar.a()) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return ((((this.f15575a.hashCode() ^ 1000003) * 1000003) ^ (true != this.f15576b ? 1237 : 1231)) * 1000003) ^ this.f15577c;
    }

    public final String toString() {
        String str = this.f15575a;
        boolean z4 = this.f15576b;
        int i8 = this.f15577c;
        return "MLKitLoggingOptions{libraryName=" + str + ", enableFirelog=" + z4 + ", firelogEventType=" + i8 + "}";
    }
}
