package com.google.android.gms.internal.mlkit_common;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class m {

    /* renamed from: a  reason: collision with root package name */
    private final Object f12991a;

    /* renamed from: b  reason: collision with root package name */
    private final Object f12992b;

    /* renamed from: c  reason: collision with root package name */
    private final Object f12993c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public m(Object obj, Object obj2, Object obj3) {
        this.f12991a = obj;
        this.f12992b = obj2;
        this.f12993c = obj3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final IllegalArgumentException a() {
        String valueOf = String.valueOf(this.f12991a);
        String valueOf2 = String.valueOf(this.f12992b);
        String valueOf3 = String.valueOf(this.f12991a);
        String valueOf4 = String.valueOf(this.f12993c);
        return new IllegalArgumentException("Multiple entries with same key: " + valueOf + "=" + valueOf2 + " and " + valueOf3 + "=" + valueOf4);
    }
}
