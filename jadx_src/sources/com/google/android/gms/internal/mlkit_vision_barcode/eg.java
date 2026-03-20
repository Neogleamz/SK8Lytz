package com.google.android.gms.internal.mlkit_vision_barcode;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class eg extends ig {

    /* renamed from: a  reason: collision with root package name */
    private final String f13430a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f13431b;

    /* renamed from: c  reason: collision with root package name */
    private final int f13432c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public /* synthetic */ eg(String str, boolean z4, int i8, dg dgVar) {
        this.f13430a = str;
        this.f13431b = z4;
        this.f13432c = i8;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.ig
    public final int a() {
        return this.f13432c;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.ig
    public final String b() {
        return this.f13430a;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode.ig
    public final boolean c() {
        return this.f13431b;
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ig) {
            ig igVar = (ig) obj;
            if (this.f13430a.equals(igVar.b()) && this.f13431b == igVar.c() && this.f13432c == igVar.a()) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return ((((this.f13430a.hashCode() ^ 1000003) * 1000003) ^ (true != this.f13431b ? 1237 : 1231)) * 1000003) ^ this.f13432c;
    }

    public final String toString() {
        String str = this.f13430a;
        boolean z4 = this.f13431b;
        int i8 = this.f13432c;
        return "MLKitLoggingOptions{libraryName=" + str + ", enableFirelog=" + z4 + ", firelogEventType=" + i8 + "}";
    }
}
