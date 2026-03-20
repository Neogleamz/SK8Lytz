package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class ra {

    /* renamed from: a  reason: collision with root package name */
    private static final pa f12488a = c();

    /* renamed from: b  reason: collision with root package name */
    private static final pa f12489b = new oa();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static pa a() {
        return f12488a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static pa b() {
        return f12489b;
    }

    private static pa c() {
        try {
            return (pa) Class.forName("com.google.protobuf.NewInstanceSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }
}
