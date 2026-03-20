package com.google.android.gms.internal.mlkit_vision_barcode;

import j8.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class v7 implements j8.c {

    /* renamed from: a  reason: collision with root package name */
    static final v7 f14112a = new v7();

    /* renamed from: b  reason: collision with root package name */
    private static final j8.b f14113b;

    /* renamed from: c  reason: collision with root package name */
    private static final j8.b f14114c;

    /* renamed from: d  reason: collision with root package name */
    private static final j8.b f14115d;

    /* renamed from: e  reason: collision with root package name */
    private static final j8.b f14116e;

    /* renamed from: f  reason: collision with root package name */
    private static final j8.b f14117f;

    static {
        b.b a9 = j8.b.a("inferenceCommonLogEvent");
        g2 g2Var = new g2();
        g2Var.a(1);
        f14113b = a9.b(g2Var.b()).a();
        b.b a10 = j8.b.a("options");
        g2 g2Var2 = new g2();
        g2Var2.a(2);
        f14114c = a10.b(g2Var2.b()).a();
        b.b a11 = j8.b.a("detectedBarcodeFormats");
        g2 g2Var3 = new g2();
        g2Var3.a(3);
        f14115d = a11.b(g2Var3.b()).a();
        b.b a12 = j8.b.a("detectedBarcodeValueTypes");
        g2 g2Var4 = new g2();
        g2Var4.a(4);
        f14116e = a12.b(g2Var4.b()).a();
        b.b a13 = j8.b.a("imageInfo");
        g2 g2Var5 = new g2();
        g2Var5.a(5);
        f14117f = a13.b(g2Var5.b()).a();
    }

    private v7() {
    }

    public final /* bridge */ /* synthetic */ void a(Object obj, Object obj2) {
        xc xcVar = (xc) obj;
        j8.d dVar = (j8.d) obj2;
        dVar.c(f14113b, xcVar.d());
        dVar.c(f14114c, xcVar.e());
        dVar.c(f14115d, xcVar.a());
        dVar.c(f14116e, xcVar.b());
        dVar.c(f14117f, xcVar.c());
    }
}
