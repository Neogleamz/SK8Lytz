package com.google.android.gms.internal.measurement;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w9 implements wa {

    /* renamed from: b  reason: collision with root package name */
    private static final fa f12637b = new v9();

    /* renamed from: a  reason: collision with root package name */
    private final fa f12638a;

    public w9() {
        this(new x9(v8.c(), b()));
    }

    private w9(fa faVar) {
        this.f12638a = (fa) a9.f(faVar, "messageInfoFactory");
    }

    private static fa b() {
        try {
            return (fa) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception unused) {
            return f12637b;
        }
    }

    private static boolean c(ga gaVar) {
        return y9.f12686a[gaVar.zzb().ordinal()] != 1;
    }

    @Override // com.google.android.gms.internal.measurement.wa
    public final <T> xa<T> a(Class<T> cls) {
        ab.o(cls);
        ga a9 = this.f12638a.a(cls);
        if (a9.a()) {
            return x8.class.isAssignableFrom(cls) ? na.i(ab.u(), p8.b(), a9.zza()) : na.i(ab.f(), p8.a(), a9.zza());
        } else if (x8.class.isAssignableFrom(cls)) {
            boolean c9 = c(a9);
            pa b9 = ra.b();
            n9 c10 = n9.c();
            tb<?, ?> u8 = ab.u();
            return c9 ? la.m(cls, a9, b9, c10, u8, p8.b(), da.b()) : la.m(cls, a9, b9, c10, u8, null, da.b());
        } else {
            boolean c11 = c(a9);
            pa a10 = ra.a();
            n9 a11 = n9.a();
            tb<?, ?> f5 = ab.f();
            return c11 ? la.m(cls, a9, a10, a11, f5, p8.a(), da.a()) : la.m(cls, a9, a10, a11, f5, null, da.a());
        }
    }
}
