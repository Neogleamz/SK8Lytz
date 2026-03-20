package com.google.android.gms.internal.mlkit_vision_common;

import android.content.Context;
import android.content.res.Resources;
import android.os.SystemClock;
import com.google.android.gms.dynamite.DynamiteModule;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class rb {

    /* renamed from: k  reason: collision with root package name */
    private static zzp f15823k;

    /* renamed from: l  reason: collision with root package name */
    private static final zzr f15824l = zzr.c("optional-module-barcode", "com.google.android.gms.vision.barcode");

    /* renamed from: a  reason: collision with root package name */
    private final String f15825a;

    /* renamed from: b  reason: collision with root package name */
    private final String f15826b;

    /* renamed from: c  reason: collision with root package name */
    private final kb f15827c;

    /* renamed from: d  reason: collision with root package name */
    private final i9.m f15828d;

    /* renamed from: e  reason: collision with root package name */
    private final j7.j f15829e;

    /* renamed from: f  reason: collision with root package name */
    private final j7.j f15830f;

    /* renamed from: g  reason: collision with root package name */
    private final String f15831g;

    /* renamed from: h  reason: collision with root package name */
    private final int f15832h;

    /* renamed from: i  reason: collision with root package name */
    private final Map f15833i = new HashMap();

    /* renamed from: j  reason: collision with root package name */
    private final Map f15834j = new HashMap();

    public rb(Context context, final i9.m mVar, kb kbVar, String str) {
        this.f15825a = context.getPackageName();
        this.f15826b = i9.c.a(context);
        this.f15828d = mVar;
        this.f15827c = kbVar;
        ec.a();
        this.f15831g = str;
        this.f15829e = i9.g.a().b(new Callable() { // from class: com.google.android.gms.internal.mlkit_vision_common.ob
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return rb.this.a();
            }
        });
        i9.g a9 = i9.g.a();
        mVar.getClass();
        this.f15830f = a9.b(new Callable() { // from class: com.google.android.gms.internal.mlkit_vision_common.pb
            @Override // java.util.concurrent.Callable
            public final Object call() {
                return mVar.a();
            }
        });
        zzr zzrVar = f15824l;
        this.f15832h = zzrVar.containsKey(str) ? DynamiteModule.b(context, (String) zzrVar.get(str)) : -1;
    }

    private static synchronized zzp d() {
        synchronized (rb.class) {
            zzp zzpVar = f15823k;
            if (zzpVar != null) {
                return zzpVar;
            }
            androidx.core.os.j a9 = androidx.core.os.f.a(Resources.getSystem().getConfiguration());
            hb hbVar = new hb();
            for (int i8 = 0; i8 < a9.g(); i8++) {
                hbVar.c(i9.c.b(a9.d(i8)));
            }
            zzp d8 = hbVar.d();
            f15823k = d8;
            return d8;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ String a() {
        return n6.h.a().b(this.f15831g);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final /* synthetic */ void b(jb jbVar, zziv zzivVar, String str) {
        jbVar.b(zzivVar);
        String a9 = jbVar.a();
        fa faVar = new fa();
        faVar.b(this.f15825a);
        faVar.c(this.f15826b);
        faVar.h(d());
        faVar.g(Boolean.TRUE);
        faVar.l(a9);
        faVar.j(str);
        faVar.i(this.f15830f.p() ? (String) this.f15830f.l() : this.f15828d.a());
        faVar.d(10);
        faVar.k(Integer.valueOf(this.f15832h));
        jbVar.d(faVar);
        this.f15827c.a(jbVar);
    }

    public final void c(bc bcVar, final zziv zzivVar) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (this.f15833i.get(zzivVar) != null && elapsedRealtime - ((Long) this.f15833i.get(zzivVar)).longValue() <= TimeUnit.SECONDS.toMillis(30L)) {
            return;
        }
        this.f15833i.put(zzivVar, Long.valueOf(elapsedRealtime));
        int i8 = bcVar.f15353a;
        int i9 = bcVar.f15354b;
        int i10 = bcVar.f15355c;
        int i11 = bcVar.f15356d;
        int i12 = bcVar.f15357e;
        long j8 = bcVar.f15358f;
        int i13 = bcVar.f15359g;
        u7 u7Var = new u7();
        u7Var.d(i8 != -1 ? i8 != 35 ? i8 != 842094169 ? i8 != 16 ? i8 != 17 ? zzii.UNKNOWN_FORMAT : zzii.NV21 : zzii.NV16 : zzii.YV12 : zzii.YUV_420_888 : zzii.BITMAP);
        u7Var.f(i9 != 1 ? i9 != 2 ? i9 != 3 ? i9 != 4 ? zzio.ANDROID_MEDIA_IMAGE : zzio.FILEPATH : zzio.BYTEBUFFER : zzio.BYTEARRAY : zzio.BITMAP);
        u7Var.c(Integer.valueOf(i10));
        u7Var.e(Integer.valueOf(i11));
        u7Var.g(Integer.valueOf(i12));
        u7Var.b(Long.valueOf(j8));
        u7Var.h(Integer.valueOf(i13));
        w7 j9 = u7Var.j();
        b8 b8Var = new b8();
        b8Var.d(j9);
        final jb e8 = sb.e(b8Var);
        final String b9 = this.f15829e.p() ? (String) this.f15829e.l() : n6.h.a().b(this.f15831g);
        i9.g.d().execute(new Runnable() { // from class: com.google.android.gms.internal.mlkit_vision_common.qb
            @Override // java.lang.Runnable
            public final void run() {
                rb.this.b(e8, zzivVar, b9);
            }
        });
    }
}
