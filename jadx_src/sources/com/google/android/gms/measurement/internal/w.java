package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.text.TextUtils;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class w {

    /* renamed from: a  reason: collision with root package name */
    final String f17056a;

    /* renamed from: b  reason: collision with root package name */
    final String f17057b;

    /* renamed from: c  reason: collision with root package name */
    private final String f17058c;

    /* renamed from: d  reason: collision with root package name */
    final long f17059d;

    /* renamed from: e  reason: collision with root package name */
    final long f17060e;

    /* renamed from: f  reason: collision with root package name */
    final zzba f17061f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public w(f6 f6Var, String str, String str2, String str3, long j8, long j9, Bundle bundle) {
        zzba zzbaVar;
        n6.j.f(str2);
        n6.j.f(str3);
        this.f17056a = str2;
        this.f17057b = str3;
        this.f17058c = TextUtils.isEmpty(str) ? null : str;
        this.f17059d = j8;
        this.f17060e = j9;
        if (j9 != 0 && j9 > j8) {
            f6Var.i().J().b("Event created with reverse previous/current timestamps. appId", x4.t(str2));
        }
        if (bundle == null || bundle.isEmpty()) {
            zzbaVar = new zzba(new Bundle());
        } else {
            Bundle bundle2 = new Bundle(bundle);
            Iterator<String> it = bundle2.keySet().iterator();
            while (it.hasNext()) {
                String next = it.next();
                if (next == null) {
                    f6Var.i().E().a("Param name can't be null");
                } else {
                    Object q02 = f6Var.J().q0(next, bundle2.get(next));
                    if (q02 == null) {
                        f6Var.i().J().b("Param value can't be null", f6Var.B().f(next));
                    } else {
                        f6Var.J().M(bundle2, next, q02);
                    }
                }
                it.remove();
            }
            zzbaVar = new zzba(bundle2);
        }
        this.f17061f = zzbaVar;
    }

    private w(f6 f6Var, String str, String str2, String str3, long j8, long j9, zzba zzbaVar) {
        n6.j.f(str2);
        n6.j.f(str3);
        n6.j.l(zzbaVar);
        this.f17056a = str2;
        this.f17057b = str3;
        this.f17058c = TextUtils.isEmpty(str) ? null : str;
        this.f17059d = j8;
        this.f17060e = j9;
        if (j9 != 0 && j9 > j8) {
            f6Var.i().J().c("Event created with reverse previous/current timestamps. appId, name", x4.t(str2), x4.t(str3));
        }
        this.f17061f = zzbaVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final w a(f6 f6Var, long j8) {
        return new w(f6Var, this.f17058c, this.f17056a, this.f17057b, this.f17059d, j8, this.f17061f);
    }

    public final String toString() {
        String str = this.f17056a;
        String str2 = this.f17057b;
        String valueOf = String.valueOf(this.f17061f);
        return "Event{appId='" + str + "', name='" + str2 + "', params=" + valueOf + "}";
    }
}
