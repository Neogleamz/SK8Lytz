package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class i2 implements v3 {

    /* renamed from: a  reason: collision with root package name */
    private static final i2 f14781a = new i2();

    private i2() {
    }

    public static i2 c() {
        return f14781a;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.v3
    public final boolean a(Class cls) {
        return p2.class.isAssignableFrom(cls);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.v3
    public final u3 b(Class cls) {
        if (p2.class.isAssignableFrom(cls)) {
            try {
                return (u3) p2.n(cls.asSubclass(p2.class)).H(3, null, null);
            } catch (Exception e8) {
                throw new RuntimeException("Unable to get message info for ".concat(cls.getName()), e8);
            }
        }
        throw new IllegalArgumentException("Unsupported message type: ".concat(cls.getName()));
    }
}
