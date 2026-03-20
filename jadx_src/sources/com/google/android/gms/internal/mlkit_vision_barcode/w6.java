package com.google.android.gms.internal.mlkit_vision_barcode;

import j8.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class w6 implements j8.c {

    /* renamed from: a  reason: collision with root package name */
    static final w6 f14161a = new w6();

    /* renamed from: b  reason: collision with root package name */
    private static final j8.b f14162b;

    /* renamed from: c  reason: collision with root package name */
    private static final j8.b f14163c;

    /* renamed from: d  reason: collision with root package name */
    private static final j8.b f14164d;

    /* renamed from: e  reason: collision with root package name */
    private static final j8.b f14165e;

    static {
        b.b a9 = j8.b.a("imageFormat");
        g2 g2Var = new g2();
        g2Var.a(1);
        f14162b = a9.b(g2Var.b()).a();
        b.b a10 = j8.b.a("originalImageSize");
        g2 g2Var2 = new g2();
        g2Var2.a(2);
        f14163c = a10.b(g2Var2.b()).a();
        b.b a11 = j8.b.a("compressedImageSize");
        g2 g2Var3 = new g2();
        g2Var3.a(3);
        f14164d = a11.b(g2Var3.b()).a();
        b.b a12 = j8.b.a("isOdmlImage");
        g2 g2Var4 = new g2();
        g2Var4.a(4);
        f14165e = a12.b(g2Var4.b()).a();
    }

    private w6() {
    }

    public final /* bridge */ /* synthetic */ void a(Object obj, Object obj2) {
        vb vbVar = (vb) obj;
        j8.d dVar = (j8.d) obj2;
        dVar.c(f14162b, vbVar.a());
        dVar.c(f14163c, vbVar.b());
        dVar.c(f14164d, (Object) null);
        dVar.c(f14165e, (Object) null);
    }
}
