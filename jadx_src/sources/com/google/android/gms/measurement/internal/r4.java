package com.google.android.gms.measurement.internal;

import android.content.Context;
import com.google.android.gms.internal.measurement.dg;
import com.google.android.gms.measurement.internal.zziq;
import java.math.BigInteger;
import java.util.List;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class r4 extends v4 {

    /* renamed from: c  reason: collision with root package name */
    private String f16918c;

    /* renamed from: d  reason: collision with root package name */
    private String f16919d;

    /* renamed from: e  reason: collision with root package name */
    private int f16920e;

    /* renamed from: f  reason: collision with root package name */
    private String f16921f;

    /* renamed from: g  reason: collision with root package name */
    private String f16922g;

    /* renamed from: h  reason: collision with root package name */
    private long f16923h;

    /* renamed from: i  reason: collision with root package name */
    private long f16924i;

    /* renamed from: j  reason: collision with root package name */
    private List<String> f16925j;

    /* renamed from: k  reason: collision with root package name */
    private String f16926k;

    /* renamed from: l  reason: collision with root package name */
    private int f16927l;

    /* renamed from: m  reason: collision with root package name */
    private String f16928m;

    /* renamed from: n  reason: collision with root package name */
    private String f16929n;

    /* renamed from: o  reason: collision with root package name */
    private String f16930o;

    /* renamed from: p  reason: collision with root package name */
    private long f16931p;
    private String q;

    /* JADX INFO: Access modifiers changed from: package-private */
    public r4(f6 f6Var, long j8) {
        super(f6Var);
        this.f16931p = 0L;
        this.q = null;
        this.f16924i = j8;
    }

    private final String H() {
        z4 L;
        String str;
        if (dg.a() && a().r(c0.f16396m0)) {
            L = i().I();
            str = "Disabled IID for tests.";
        } else {
            try {
                Class<?> loadClass = zza().getClassLoader().loadClass("com.google.firebase.analytics.FirebaseAnalytics");
                if (loadClass == null) {
                    return null;
                }
                try {
                    Object invoke = loadClass.getDeclaredMethod("getInstance", Context.class).invoke(null, zza());
                    if (invoke == null) {
                        return null;
                    }
                    try {
                        return (String) loadClass.getDeclaredMethod("getFirebaseInstanceId", new Class[0]).invoke(invoke, new Object[0]);
                    } catch (Exception unused) {
                        L = i().K();
                        str = "Failed to retrieve Firebase Instance Id";
                    }
                } catch (Exception unused2) {
                    L = i().L();
                    str = "Failed to obtain Firebase Analytics instance";
                }
            } catch (ClassNotFoundException unused3) {
                return null;
            }
        }
        L.a(str);
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int A() {
        t();
        return this.f16927l;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final int B() {
        t();
        return this.f16920e;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String C() {
        t();
        return this.f16929n;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String D() {
        t();
        n6.j.l(this.f16918c);
        return this.f16918c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String E() {
        k();
        t();
        n6.j.l(this.f16928m);
        return this.f16928m;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final List<String> F() {
        return this.f16925j;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final void G() {
        String format;
        k();
        if (f().J().m(zziq.zza.ANALYTICS_STORAGE)) {
            byte[] bArr = new byte[16];
            g().U0().nextBytes(bArr);
            format = String.format(Locale.US, "%032x", new BigInteger(1, bArr));
        } else {
            i().D().a("Analytics Storage consent is not granted");
            format = null;
        }
        z4 D = i().D();
        Object[] objArr = new Object[1];
        objArr[0] = format == null ? "null" : "not null";
        D.a(String.format("Resetting session stitching token to %s", objArr));
        this.f16930o = format;
        this.f16931p = zzb().a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final boolean I(String str) {
        String str2 = this.q;
        boolean z4 = (str2 == null || str2.equals(str)) ? false : true;
        this.q = str;
        return z4;
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ e a() {
        return super.a();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ d b() {
        return super.b();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ x c() {
        return super.c();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ s4 e() {
        return super.e();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ h5 f() {
        return super.f();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ sb g() {
        return super.g();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    @Override // com.google.android.gms.measurement.internal.w1, com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ a m() {
        return super.m();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ r4 n() {
        return super.n();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ q4 o() {
        return super.o();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ h7 p() {
        return super.p();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ z8 q() {
        return super.q();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ f9 r() {
        return super.r();
    }

    @Override // com.google.android.gms.measurement.internal.w1
    public final /* bridge */ /* synthetic */ na s() {
        return super.s();
    }

    /* JADX WARN: Can't wrap try/catch for region: R(22:1|(1:3)(6:68|69|(1:71)(2:86|(1:88))|72|73|(22:75|(1:77)(1:84)|79|80|5|(1:67)(1:9)|10|11|13|(1:15)(1:57)|16|(1:18)|19|20|(1:22)(1:54)|23|(1:25)|(3:27|(1:29)(1:32)|30)|33|(3:35|(1:37)(3:39|(3:42|(1:44)|40)|45)|38)|(1:47)|(2:49|50)(2:52|53)))|4|5|(1:7)|67|10|11|13|(0)(0)|16|(0)|19|20|(0)(0)|23|(0)|(0)|33|(0)|(0)|(0)(0)) */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x018a, code lost:
        r2 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x018b, code lost:
        i().E().c("Fetching Google App Id failed with exception. appId", com.google.android.gms.measurement.internal.x4.t(r0), r2);
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00a9  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00c7  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00e2  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x00e9  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x00f0  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00f7  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x010c  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0113  */
    /* JADX WARN: Removed duplicated region for block: B:46:0x011e  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0120  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0127  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x014a  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x014b  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0154 A[Catch: IllegalStateException -> 0x018a, TryCatch #3 {IllegalStateException -> 0x018a, blocks: (B:51:0x012f, B:55:0x014c, B:57:0x0154, B:59:0x016d, B:61:0x0181, B:63:0x0186, B:62:0x0184), top: B:92:0x012f }] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x016d A[Catch: IllegalStateException -> 0x018a, TryCatch #3 {IllegalStateException -> 0x018a, blocks: (B:51:0x012f, B:55:0x014c, B:57:0x0154, B:59:0x016d, B:61:0x0181, B:63:0x0186, B:62:0x0184), top: B:92:0x012f }] */
    /* JADX WARN: Removed duplicated region for block: B:69:0x01ab  */
    /* JADX WARN: Removed duplicated region for block: B:80:0x01df  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x01e3  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01ee  */
    @Override // com.google.android.gms.measurement.internal.v4
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void w() {
        /*
            Method dump skipped, instructions count: 520
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.r4.w():void");
    }

    @Override // com.google.android.gms.measurement.internal.v4
    protected final boolean y() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:49:0x018e  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0190  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.measurement.internal.zzn z(java.lang.String r51) {
        /*
            Method dump skipped, instructions count: 585
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.r4.z(java.lang.String):com.google.android.gms.measurement.internal.zzn");
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ Context zza() {
        return super.zza();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ u6.d zzb() {
        return super.zzb();
    }
}
