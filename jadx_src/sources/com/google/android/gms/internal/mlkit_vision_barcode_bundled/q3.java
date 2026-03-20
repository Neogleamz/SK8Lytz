package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class q3 implements s4 {

    /* renamed from: b  reason: collision with root package name */
    private static final v3 f14836b = new o3();

    /* renamed from: a  reason: collision with root package name */
    private final v3 f14837a;

    public q3() {
        v3 v3Var;
        v3[] v3VarArr = new v3[2];
        v3VarArr[0] = i2.c();
        try {
            v3Var = (v3) Class.forName("com.google.protobuf.DescriptorMessageInfoFactory").getDeclaredMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
        } catch (Exception unused) {
            v3Var = f14836b;
        }
        v3VarArr[1] = v3Var;
        p3 p3Var = new p3(v3VarArr);
        byte[] bArr = y2.f14888d;
        this.f14837a = p3Var;
    }

    private static boolean b(u3 u3Var) {
        return u3Var.a() + (-1) != 1;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_barcode_bundled.s4
    public final r4 a(Class cls) {
        d4 a9;
        m3 c9;
        h5 V;
        c2 a10;
        s3 a11;
        h5 V2;
        c2 a12;
        t4.d(cls);
        u3 b9 = this.f14837a.b(cls);
        if (b9.zzb()) {
            if (p2.class.isAssignableFrom(cls)) {
                V2 = t4.W();
                a12 = e2.b();
            } else {
                V2 = t4.V();
                a12 = e2.a();
            }
            return b4.j(V2, a12, b9.zza());
        }
        if (p2.class.isAssignableFrom(cls)) {
            boolean b10 = b(b9);
            a9 = e4.b();
            c9 = m3.d();
            V = t4.W();
            a10 = b10 ? e2.b() : null;
            a11 = t3.b();
        } else {
            boolean b11 = b(b9);
            a9 = e4.a();
            c9 = m3.c();
            V = t4.V();
            a10 = b11 ? e2.a() : null;
            a11 = t3.a();
        }
        return a4.H(cls, b9, a9, c9, V, a10, a11);
    }
}
