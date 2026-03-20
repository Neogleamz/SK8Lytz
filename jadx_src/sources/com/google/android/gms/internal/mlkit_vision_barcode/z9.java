package com.google.android.gms.internal.mlkit_vision_barcode;

import j8.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class z9 implements j8.c {

    /* renamed from: a  reason: collision with root package name */
    static final z9 f14285a = new z9();

    /* renamed from: b  reason: collision with root package name */
    private static final j8.b f14286b;

    /* renamed from: c  reason: collision with root package name */
    private static final j8.b f14287c;

    /* renamed from: d  reason: collision with root package name */
    private static final j8.b f14288d;

    /* renamed from: e  reason: collision with root package name */
    private static final j8.b f14289e;

    /* renamed from: f  reason: collision with root package name */
    private static final j8.b f14290f;

    /* renamed from: g  reason: collision with root package name */
    private static final j8.b f14291g;

    static {
        b.b a9 = j8.b.a("appName");
        g2 g2Var = new g2();
        g2Var.a(1);
        f14286b = a9.b(g2Var.b()).a();
        b.b a10 = j8.b.a("sessionId");
        g2 g2Var2 = new g2();
        g2Var2.a(2);
        f14287c = a10.b(g2Var2.b()).a();
        b.b a11 = j8.b.a("startZoomLevel");
        g2 g2Var3 = new g2();
        g2Var3.a(3);
        f14288d = a11.b(g2Var3.b()).a();
        b.b a12 = j8.b.a("endZoomLevel");
        g2 g2Var4 = new g2();
        g2Var4.a(4);
        f14289e = a12.b(g2Var4.b()).a();
        b.b a13 = j8.b.a("durationMs");
        g2 g2Var5 = new g2();
        g2Var5.a(5);
        f14290f = a13.b(g2Var5.b()).a();
        b.b a14 = j8.b.a("predictedArea");
        g2 g2Var6 = new g2();
        g2Var6.a(6);
        f14291g = a14.b(g2Var6.b()).a();
    }

    private z9() {
    }

    public final /* bridge */ /* synthetic */ void a(Object obj, Object obj2) {
        af afVar = (af) obj;
        j8.d dVar = (j8.d) obj2;
        dVar.c(f14286b, afVar.e());
        dVar.c(f14287c, afVar.f());
        dVar.c(f14288d, afVar.c());
        dVar.c(f14289e, afVar.b());
        dVar.c(f14290f, afVar.d());
        dVar.c(f14291g, afVar.a());
    }
}
