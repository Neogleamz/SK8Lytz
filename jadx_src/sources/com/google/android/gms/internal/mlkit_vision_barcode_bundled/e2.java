package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e2 {

    /* renamed from: a  reason: collision with root package name */
    private static final c2 f14749a = new d2();

    /* renamed from: b  reason: collision with root package name */
    private static final c2 f14750b;

    static {
        c2 c2Var;
        try {
            c2Var = (c2) Class.forName("com.google.protobuf.ExtensionSchemaFull").getDeclaredConstructor(new Class[0]).newInstance(new Object[0]);
        } catch (Exception unused) {
            c2Var = null;
        }
        f14750b = c2Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static c2 a() {
        c2 c2Var = f14750b;
        if (c2Var != null) {
            return c2Var;
        }
        throw new IllegalStateException("Protobuf runtime is not correctly loaded.");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static c2 b() {
        return f14749a;
    }
}
