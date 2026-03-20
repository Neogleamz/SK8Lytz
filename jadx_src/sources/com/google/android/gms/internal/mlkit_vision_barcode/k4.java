package com.google.android.gms.internal.mlkit_vision_barcode;

import j8.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k4 implements j8.c {

    /* renamed from: a  reason: collision with root package name */
    static final k4 f13618a = new k4();

    /* renamed from: b  reason: collision with root package name */
    private static final j8.b f13619b;

    /* renamed from: c  reason: collision with root package name */
    private static final j8.b f13620c;

    /* renamed from: d  reason: collision with root package name */
    private static final j8.b f13621d;

    static {
        b.b a9 = j8.b.a("logEventKey");
        g2 g2Var = new g2();
        g2Var.a(1);
        f13619b = a9.b(g2Var.b()).a();
        b.b a10 = j8.b.a("eventCount");
        g2 g2Var2 = new g2();
        g2Var2.a(2);
        f13620c = a10.b(g2Var2.b()).a();
        b.b a11 = j8.b.a("inferenceDurationStats");
        g2 g2Var3 = new g2();
        g2Var3.a(3);
        f13621d = a11.b(g2Var3.b()).a();
    }

    private k4() {
    }

    public final /* bridge */ /* synthetic */ void a(Object obj, Object obj2) {
        a3 a3Var = (a3) obj;
        j8.d dVar = (j8.d) obj2;
        dVar.c(f13619b, a3Var.a());
        dVar.c(f13620c, a3Var.c());
        dVar.c(f13621d, a3Var.b());
    }
}
