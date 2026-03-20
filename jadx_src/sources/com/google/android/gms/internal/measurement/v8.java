package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.x8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class v8 implements fa {

    /* renamed from: a  reason: collision with root package name */
    private static final v8 f12585a = new v8();

    private v8() {
    }

    public static v8 c() {
        return f12585a;
    }

    @Override // com.google.android.gms.internal.measurement.fa
    public final ga a(Class<?> cls) {
        if (!x8.class.isAssignableFrom(cls)) {
            String name = cls.getName();
            throw new IllegalArgumentException("Unsupported message type: " + name);
        }
        try {
            return (ga) x8.o(cls.asSubclass(x8.class)).r(x8.f.f12664c, null, null);
        } catch (Exception e8) {
            String name2 = cls.getName();
            throw new RuntimeException("Unable to get message info for " + name2, e8);
        }
    }

    @Override // com.google.android.gms.internal.measurement.fa
    public final boolean b(Class<?> cls) {
        return x8.class.isAssignableFrom(cls);
    }
}
