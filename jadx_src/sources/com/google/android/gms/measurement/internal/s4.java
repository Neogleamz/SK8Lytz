package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class s4 {

    /* renamed from: b  reason: collision with root package name */
    private static final AtomicReference<String[]> f16962b = new AtomicReference<>();

    /* renamed from: c  reason: collision with root package name */
    private static final AtomicReference<String[]> f16963c = new AtomicReference<>();

    /* renamed from: d  reason: collision with root package name */
    private static final AtomicReference<String[]> f16964d = new AtomicReference<>();

    /* renamed from: a  reason: collision with root package name */
    private final f7.g f16965a;

    public s4(f7.g gVar) {
        this.f16965a = gVar;
    }

    private static String d(String str, String[] strArr, String[] strArr2, AtomicReference<String[]> atomicReference) {
        String str2;
        n6.j.l(strArr);
        n6.j.l(strArr2);
        n6.j.l(atomicReference);
        n6.j.a(strArr.length == strArr2.length);
        for (int i8 = 0; i8 < strArr.length; i8++) {
            if (Objects.equals(str, strArr[i8])) {
                synchronized (atomicReference) {
                    String[] strArr3 = atomicReference.get();
                    if (strArr3 == null) {
                        strArr3 = new String[strArr2.length];
                        atomicReference.set(strArr3);
                    }
                    if (strArr3[i8] == null) {
                        strArr3[i8] = strArr2[i8] + "(" + strArr[i8] + ")";
                    }
                    str2 = strArr3[i8];
                }
                return str2;
            }
        }
        return str;
    }

    private final String e(Object[] objArr) {
        if (objArr == null) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Object obj : objArr) {
            String a9 = obj instanceof Bundle ? a((Bundle) obj) : String.valueOf(obj);
            if (a9 != null) {
                if (sb.length() != 1) {
                    sb.append(", ");
                }
                sb.append(a9);
            }
        }
        sb.append("]");
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String a(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        if (this.f16965a.zza()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Bundle[{");
            for (String str : bundle.keySet()) {
                if (sb.length() != 8) {
                    sb.append(", ");
                }
                sb.append(f(str));
                sb.append("=");
                Object obj = bundle.get(str);
                sb.append(obj instanceof Bundle ? e(new Object[]{obj}) : obj instanceof Object[] ? e((Object[]) obj) : obj instanceof ArrayList ? e(((ArrayList) obj).toArray()) : String.valueOf(obj));
            }
            sb.append("}]");
            return sb.toString();
        }
        return bundle.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String b(zzbf zzbfVar) {
        if (zzbfVar == null) {
            return null;
        }
        if (this.f16965a.zza()) {
            StringBuilder sb = new StringBuilder();
            sb.append("origin=");
            sb.append(zzbfVar.f17265c);
            sb.append(",name=");
            sb.append(c(zzbfVar.f17263a));
            sb.append(",params=");
            zzba zzbaVar = zzbfVar.f17264b;
            sb.append(zzbaVar != null ? !this.f16965a.zza() ? zzbaVar.toString() : a(zzbaVar.D0()) : null);
            return sb.toString();
        }
        return zzbfVar.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String c(String str) {
        if (str == null) {
            return null;
        }
        return !this.f16965a.zza() ? str : d(str, f7.o.f19855c, f7.o.f19853a, f16962b);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String f(String str) {
        if (str == null) {
            return null;
        }
        return !this.f16965a.zza() ? str : d(str, f7.n.f19850b, f7.n.f19849a, f16963c);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final String g(String str) {
        if (str == null) {
            return null;
        }
        if (this.f16965a.zza()) {
            if (str.startsWith("_exp_")) {
                return "experiment_id(" + str + ")";
            }
            return d(str, f7.q.f19858b, f7.q.f19857a, f16964d);
        }
        return str;
    }
}
