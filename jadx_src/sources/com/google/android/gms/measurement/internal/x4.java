package com.google.android.gms.measurement.internal;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.gms.internal.measurement.yd;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x4 extends d7 {

    /* renamed from: c  reason: collision with root package name */
    private char f17111c;

    /* renamed from: d  reason: collision with root package name */
    private long f17112d;

    /* renamed from: e  reason: collision with root package name */
    private String f17113e;

    /* renamed from: f  reason: collision with root package name */
    private final z4 f17114f;

    /* renamed from: g  reason: collision with root package name */
    private final z4 f17115g;

    /* renamed from: h  reason: collision with root package name */
    private final z4 f17116h;

    /* renamed from: i  reason: collision with root package name */
    private final z4 f17117i;

    /* renamed from: j  reason: collision with root package name */
    private final z4 f17118j;

    /* renamed from: k  reason: collision with root package name */
    private final z4 f17119k;

    /* renamed from: l  reason: collision with root package name */
    private final z4 f17120l;

    /* renamed from: m  reason: collision with root package name */
    private final z4 f17121m;

    /* renamed from: n  reason: collision with root package name */
    private final z4 f17122n;

    /* JADX INFO: Access modifiers changed from: package-private */
    public x4(f6 f6Var) {
        super(f6Var);
        this.f17111c = (char) 0;
        this.f17112d = -1L;
        this.f17114f = new z4(this, 6, false, false);
        this.f17115g = new z4(this, 6, true, false);
        this.f17116h = new z4(this, 6, false, true);
        this.f17117i = new z4(this, 5, false, false);
        this.f17118j = new z4(this, 5, true, false);
        this.f17119k = new z4(this, 5, false, true);
        this.f17120l = new z4(this, 4, false, false);
        this.f17121m = new z4(this, 3, false, false);
        this.f17122n = new z4(this, 2, false, false);
    }

    private static String C(String str) {
        if (TextUtils.isEmpty(str)) {
            return BuildConfig.FLAVOR;
        }
        int lastIndexOf = str.lastIndexOf(46);
        return lastIndexOf == -1 ? (yd.a() && c0.F0.a(null).booleanValue()) ? BuildConfig.FLAVOR : str : str.substring(0, lastIndexOf);
    }

    private final String N() {
        String str;
        synchronized (this) {
            if (this.f17113e == null) {
                this.f17113e = this.f16485a.M() != null ? this.f16485a.M() : "FA";
            }
            n6.j.l(this.f17113e);
            str = this.f17113e;
        }
        return str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static Object t(String str) {
        if (str == null) {
            return null;
        }
        return new y4(str);
    }

    private static String u(boolean z4, Object obj) {
        String str;
        String className;
        String str2 = BuildConfig.FLAVOR;
        if (obj == null) {
            return BuildConfig.FLAVOR;
        }
        if (obj instanceof Integer) {
            obj = Long.valueOf(((Integer) obj).intValue());
        }
        int i8 = 0;
        if (obj instanceof Long) {
            if (z4) {
                Long l8 = (Long) obj;
                if (Math.abs(l8.longValue()) < 100) {
                    return String.valueOf(obj);
                }
                if (String.valueOf(obj).charAt(0) == '-') {
                    str2 = "-";
                }
                String valueOf = String.valueOf(Math.abs(l8.longValue()));
                long round = Math.round(Math.pow(10.0d, valueOf.length() - 1));
                long round2 = Math.round(Math.pow(10.0d, valueOf.length()) - 1.0d);
                return str2 + round + "..." + str2 + round2;
            }
            return String.valueOf(obj);
        } else if (obj instanceof Boolean) {
            return String.valueOf(obj);
        } else {
            if (!(obj instanceof Throwable)) {
                if (!(obj instanceof y4)) {
                    return z4 ? "-" : String.valueOf(obj);
                }
                str = ((y4) obj).f17174a;
                return str;
            }
            Throwable th = (Throwable) obj;
            StringBuilder sb = new StringBuilder(z4 ? th.getClass().getName() : th.toString());
            String C = C(f6.class.getCanonicalName());
            StackTraceElement[] stackTrace = th.getStackTrace();
            int length = stackTrace.length;
            while (true) {
                if (i8 >= length) {
                    break;
                }
                StackTraceElement stackTraceElement = stackTrace[i8];
                if (!stackTraceElement.isNativeMethod() && (className = stackTraceElement.getClassName()) != null && C(className).equals(C)) {
                    sb.append(": ");
                    sb.append(stackTraceElement);
                    break;
                }
                i8++;
            }
            return sb.toString();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String v(boolean z4, String str, Object obj, Object obj2, Object obj3) {
        String str2 = BuildConfig.FLAVOR;
        if (str == null) {
            str = BuildConfig.FLAVOR;
        }
        String u8 = u(z4, obj);
        String u9 = u(z4, obj2);
        String u10 = u(z4, obj3);
        StringBuilder sb = new StringBuilder();
        if (!TextUtils.isEmpty(str)) {
            sb.append(str);
            str2 = ": ";
        }
        String str3 = ", ";
        if (!TextUtils.isEmpty(u8)) {
            sb.append(str2);
            sb.append(u8);
            str2 = ", ";
        }
        if (TextUtils.isEmpty(u9)) {
            str3 = str2;
        } else {
            sb.append(str2);
            sb.append(u9);
        }
        if (!TextUtils.isEmpty(u10)) {
            sb.append(str3);
            sb.append(u10);
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean A(int i8) {
        return Log.isLoggable(N(), i8);
    }

    public final z4 D() {
        return this.f17121m;
    }

    public final z4 E() {
        return this.f17114f;
    }

    public final z4 F() {
        return this.f17116h;
    }

    public final z4 G() {
        return this.f17115g;
    }

    public final z4 H() {
        return this.f17120l;
    }

    public final z4 I() {
        return this.f17122n;
    }

    public final z4 J() {
        return this.f17117i;
    }

    public final z4 K() {
        return this.f17119k;
    }

    public final z4 L() {
        return this.f17118j;
    }

    public final String M() {
        Pair<String, Long> a9;
        if (f().f16604f == null || (a9 = f().f16604f.a()) == null || a9 == h5.B) {
            return null;
        }
        String valueOf = String.valueOf(a9.second);
        return valueOf + ":" + ((String) a9.first);
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

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void h() {
        super.h();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ x4 i() {
        return super.i();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void j() {
        super.j();
    }

    @Override // com.google.android.gms.measurement.internal.e7
    public final /* bridge */ /* synthetic */ void k() {
        super.k();
    }

    @Override // com.google.android.gms.measurement.internal.e7, com.google.android.gms.measurement.internal.f7
    public final /* bridge */ /* synthetic */ a6 l() {
        return super.l();
    }

    @Override // com.google.android.gms.measurement.internal.d7
    protected final boolean r() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void w(int i8, String str) {
        Log.println(i8, N(), str);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void x(int i8, boolean z4, boolean z8, String str, Object obj, Object obj2, Object obj3) {
        String str2;
        if (!z4 && A(i8)) {
            w(i8, v(false, str, obj, obj2, obj3));
        }
        if (z8 || i8 < 5) {
            return;
        }
        n6.j.l(str);
        a6 E = this.f16485a.E();
        if (E == null) {
            str2 = "Scheduler not set. Not logging error/warn";
        } else if (E.q()) {
            if (i8 < 0) {
                i8 = 0;
            }
            if (i8 >= 9) {
                i8 = 8;
            }
            E.B(new w4(this, i8, str, obj, obj2, obj3));
            return;
        } else {
            str2 = "Scheduler not initialized. Not logging error/warn";
        }
        w(6, str2);
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
