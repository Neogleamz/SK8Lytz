package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class da {

    /* renamed from: a  reason: collision with root package name */
    private static final ba f12142a = c();

    /* renamed from: b  reason: collision with root package name */
    private static final ba f12143b = new ea();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ba a() {
        return f12142a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static ba b() {
        return f12143b;
    }

    private static ba c() {
        try {
            return (ba) Class.forName("com.google.protobuf.MapFieldSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }
}
