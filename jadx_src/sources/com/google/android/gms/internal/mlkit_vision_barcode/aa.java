package com.google.android.gms.internal.mlkit_vision_barcode;

import j8.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class aa implements j8.c {

    /* renamed from: a  reason: collision with root package name */
    static final aa f13273a = new aa();

    /* renamed from: b  reason: collision with root package name */
    private static final j8.b f13274b;

    /* renamed from: c  reason: collision with root package name */
    private static final j8.b f13275c;

    /* renamed from: d  reason: collision with root package name */
    private static final j8.b f13276d;

    /* renamed from: e  reason: collision with root package name */
    private static final j8.b f13277e;

    /* renamed from: f  reason: collision with root package name */
    private static final j8.b f13278f;

    static {
        b.b a9 = j8.b.a("xMin");
        g2 g2Var = new g2();
        g2Var.a(1);
        f13274b = a9.b(g2Var.b()).a();
        b.b a10 = j8.b.a("yMin");
        g2 g2Var2 = new g2();
        g2Var2.a(2);
        f13275c = a10.b(g2Var2.b()).a();
        b.b a11 = j8.b.a("xMax");
        g2 g2Var3 = new g2();
        g2Var3.a(3);
        f13276d = a11.b(g2Var3.b()).a();
        b.b a12 = j8.b.a("yMax");
        g2 g2Var4 = new g2();
        g2Var4.a(4);
        f13277e = a12.b(g2Var4.b()).a();
        b.b a13 = j8.b.a("confidenceScore");
        g2 g2Var5 = new g2();
        g2Var5.a(5);
        f13278f = a13.b(g2Var5.b()).a();
    }

    private aa() {
    }

    public final /* bridge */ /* synthetic */ void a(Object obj, Object obj2) {
        ze zeVar = (ze) obj;
        j8.d dVar = (j8.d) obj2;
        dVar.c(f13274b, zeVar.c());
        dVar.c(f13275c, zeVar.e());
        dVar.c(f13276d, zeVar.b());
        dVar.c(f13277e, zeVar.d());
        dVar.c(f13278f, zeVar.a());
    }
}
