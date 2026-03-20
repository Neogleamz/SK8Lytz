package com.google.android.gms.internal.mlkit_vision_barcode;

import android.content.Context;
import android.content.res.Resources;
import android.os.SystemClock;
import com.google.android.gms.dynamite.DynamiteModule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class qg {

    /* renamed from: k  reason: collision with root package name */
    private static zzcv f13939k;

    /* renamed from: l  reason: collision with root package name */
    private static final zzcx f13940l = zzcx.c("optional-module-barcode", "com.google.android.gms.vision.barcode");

    /* renamed from: a  reason: collision with root package name */
    private final String f13941a;

    /* renamed from: b  reason: collision with root package name */
    private final String f13942b;

    /* renamed from: c  reason: collision with root package name */
    private final gg f13943c;

    /* renamed from: d  reason: collision with root package name */
    private final i9.m f13944d;

    /* renamed from: e  reason: collision with root package name */
    private final j7.j f13945e;

    /* renamed from: f  reason: collision with root package name */
    private final j7.j f13946f;

    /* renamed from: g  reason: collision with root package name */
    private final String f13947g;

    /* renamed from: h  reason: collision with root package name */
    private final int f13948h;

    /* renamed from: i  reason: collision with root package name */
    private final Map f13949i = new HashMap();

    /* renamed from: j  reason: collision with root package name */
    private final Map f13950j = new HashMap();

    public qg(Context context, final i9.m mVar, gg ggVar, String str) {
        this.f13941a = context.getPackageName();
        this.f13942b = i9.c.a(context);
        this.f13944d = mVar;
        this.f13943c = ggVar;
        ch.a();
        this.f13947g = str;
        this.f13945e = i9.g.a().b(new Callable() { // from class: com.google.android.gms.internal.mlkit_vision_barcode.kg
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return qg.this.b();
            }
        });
        i9.g a9 = i9.g.a();
        mVar.getClass();
        this.f13946f = a9.b(new Callable() { // from class: com.google.android.gms.internal.mlkit_vision_barcode.lg
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return mVar.a();
            }
        });
        zzcx zzcxVar = f13940l;
        this.f13948h = zzcxVar.containsKey(str) ? DynamiteModule.b(context, (String) zzcxVar.get(str)) : -1;
    }

    static long a(List list, double d8) {
        return ((Long) list.get(Math.max(((int) Math.ceil((d8 / 100.0d) * list.size())) - 1, 0))).longValue();
    }

    private static synchronized zzcv i() {
        synchronized (qg.class) {
            zzcv zzcvVar = f13939k;
            if (zzcvVar != null) {
                return zzcvVar;
            }
            androidx.core.os.j a9 = androidx.core.os.f.a(Resources.getSystem().getConfiguration());
            d1 d1Var = new d1();
            for (int i8 = 0; i8 < a9.g(); i8++) {
                d1Var.e(i9.c.b(a9.d(i8)));
            }
            zzcv g8 = d1Var.g();
            f13939k = g8;
            return g8;
        }
    }

    private final String j() {
        return this.f13945e.p() ? (String) this.f13945e.l() : n6.h.a().b(this.f13947g);
    }

    private final boolean k(zzpk zzpkVar, long j8, long j9) {
        return this.f13949i.get(zzpkVar) == null || j8 - ((Long) this.f13949i.get(zzpkVar)).longValue() > TimeUnit.SECONDS.toMillis(30L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ String b() {
        return n6.h.a().b(this.f13947g);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void c(fg fgVar, zzpk zzpkVar, String str) {
        fgVar.a(zzpkVar);
        String b9 = fgVar.b();
        df dfVar = new df();
        dfVar.b(this.f13941a);
        dfVar.c(this.f13942b);
        dfVar.h(i());
        dfVar.g(Boolean.TRUE);
        dfVar.l(b9);
        dfVar.j(str);
        dfVar.i(this.f13946f.p() ? (String) this.f13946f.l() : this.f13944d.a());
        dfVar.d(10);
        dfVar.k(Integer.valueOf(this.f13948h));
        fgVar.c(dfVar);
        this.f13943c.a(fgVar);
    }

    public final void d(fg fgVar, zzpk zzpkVar) {
        e(fgVar, zzpkVar, j());
    }

    public final void e(final fg fgVar, final zzpk zzpkVar, final String str) {
        i9.g.d().execute(new Runnable() { // from class: com.google.android.gms.internal.mlkit_vision_barcode.mg
            @Override // java.lang.Runnable
            public final void run() {
                qg.this.c(fgVar, zzpkVar, str);
            }
        });
    }

    public final void f(pg pgVar, zzpk zzpkVar) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (k(zzpkVar, elapsedRealtime, 30L)) {
            this.f13949i.put(zzpkVar, Long.valueOf(elapsedRealtime));
            e(pgVar.zza(), zzpkVar, j());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void g(zzpk zzpkVar, com.google.mlkit.vision.barcode.internal.h hVar) {
        i1 i1Var = (i1) this.f13950j.get(zzpkVar);
        if (i1Var != null) {
            for (Object obj : i1Var.k()) {
                ArrayList<Long> arrayList = new ArrayList(i1Var.d(obj));
                Collections.sort(arrayList);
                nb nbVar = new nb();
                long j8 = 0;
                for (Long l8 : arrayList) {
                    j8 += l8.longValue();
                }
                nbVar.a(Long.valueOf(j8 / arrayList.size()));
                nbVar.c(Long.valueOf(a(arrayList, 100.0d)));
                nbVar.f(Long.valueOf(a(arrayList, 75.0d)));
                nbVar.d(Long.valueOf(a(arrayList, 50.0d)));
                nbVar.b(Long.valueOf(a(arrayList, 25.0d)));
                nbVar.e(Long.valueOf(a(arrayList, 0.0d)));
                e(hVar.a(obj, arrayList.size(), nbVar.g()), zzpkVar, j());
            }
            this.f13950j.remove(zzpkVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void h(final zzpk zzpkVar, Object obj, long j8, final com.google.mlkit.vision.barcode.internal.h hVar) {
        if (!this.f13950j.containsKey(zzpkVar)) {
            this.f13950j.put(zzpkVar, zzbz.p());
        }
        ((i1) this.f13950j.get(zzpkVar)).a(obj, Long.valueOf(j8));
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (k(zzpkVar, elapsedRealtime, 30L)) {
            this.f13949i.put(zzpkVar, Long.valueOf(elapsedRealtime));
            i9.g.d().execute(new Runnable() { // from class: com.google.android.gms.internal.mlkit_vision_barcode.og
                @Override // java.lang.Runnable
                public final void run() {
                    qg.this.g(zzpkVar, hVar);
                }
            });
        }
    }
}
