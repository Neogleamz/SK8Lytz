package com.google.android.gms.internal.mlkit_vision_barcode;

import j8.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class q6 implements j8.c {

    /* renamed from: a  reason: collision with root package name */
    static final q6 f13923a = new q6();

    /* renamed from: b  reason: collision with root package name */
    private static final j8.b f13924b;

    /* renamed from: c  reason: collision with root package name */
    private static final j8.b f13925c;

    /* renamed from: d  reason: collision with root package name */
    private static final j8.b f13926d;

    /* renamed from: e  reason: collision with root package name */
    private static final j8.b f13927e;

    /* renamed from: f  reason: collision with root package name */
    private static final j8.b f13928f;

    /* renamed from: g  reason: collision with root package name */
    private static final j8.b f13929g;

    static {
        b.b a9 = j8.b.a("maxMs");
        g2 g2Var = new g2();
        g2Var.a(1);
        f13924b = a9.b(g2Var.b()).a();
        b.b a10 = j8.b.a("minMs");
        g2 g2Var2 = new g2();
        g2Var2.a(2);
        f13925c = a10.b(g2Var2.b()).a();
        b.b a11 = j8.b.a("avgMs");
        g2 g2Var3 = new g2();
        g2Var3.a(3);
        f13926d = a11.b(g2Var3.b()).a();
        b.b a12 = j8.b.a("firstQuartileMs");
        g2 g2Var4 = new g2();
        g2Var4.a(4);
        f13927e = a12.b(g2Var4.b()).a();
        b.b a13 = j8.b.a("medianMs");
        g2 g2Var5 = new g2();
        g2Var5.a(5);
        f13928f = a13.b(g2Var5.b()).a();
        b.b a14 = j8.b.a("thirdQuartileMs");
        g2 g2Var6 = new g2();
        g2Var6.a(6);
        f13929g = a14.b(g2Var6.b()).a();
    }

    private q6() {
    }

    public final /* bridge */ /* synthetic */ void a(Object obj, Object obj2) {
        pb pbVar = (pb) obj;
        j8.d dVar = (j8.d) obj2;
        dVar.c(f13924b, pbVar.c());
        dVar.c(f13925c, pbVar.e());
        dVar.c(f13926d, pbVar.a());
        dVar.c(f13927e, pbVar.b());
        dVar.c(f13928f, pbVar.d());
        dVar.c(f13929g, pbVar.f());
    }
}
