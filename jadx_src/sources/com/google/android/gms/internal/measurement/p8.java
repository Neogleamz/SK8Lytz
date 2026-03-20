package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class p8 {

    /* renamed from: a  reason: collision with root package name */
    private static final n8<?> f12442a = new m8();

    /* renamed from: b  reason: collision with root package name */
    private static final n8<?> f12443b = c();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static n8<?> a() {
        n8<?> n8Var = f12443b;
        if (n8Var != null) {
            return n8Var;
        }
        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static n8<?> b() {
        return f12442a;
    }

    private static n8<?> c() {
        try {
            return (n8) Class.forName("com.google.protobuf.ExtensionSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            return null;
        }
    }
}
