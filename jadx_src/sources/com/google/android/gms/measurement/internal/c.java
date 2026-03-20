package com.google.android.gms.measurement.internal;

import com.google.android.gms.internal.measurement.zzfh$zzd;
import com.google.android.gms.internal.measurement.zzfh$zzf;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class c {

    /* renamed from: a  reason: collision with root package name */
    String f16354a;

    /* renamed from: b  reason: collision with root package name */
    int f16355b;

    /* renamed from: c  reason: collision with root package name */
    Boolean f16356c;

    /* renamed from: d  reason: collision with root package name */
    Boolean f16357d;

    /* renamed from: e  reason: collision with root package name */
    Long f16358e;

    /* renamed from: f  reason: collision with root package name */
    Long f16359f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public c(String str, int i8) {
        this.f16354a = str;
        this.f16355b = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Boolean b(double d8, zzfh$zzd zzfh_zzd) {
        try {
            return h(new BigDecimal(d8), zzfh_zzd, Math.ulp(d8));
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Boolean c(long j8, zzfh$zzd zzfh_zzd) {
        try {
            return h(new BigDecimal(j8), zzfh_zzd, 0.0d);
        } catch (NumberFormatException unused) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Boolean d(Boolean bool, boolean z4) {
        if (bool == null) {
            return null;
        }
        return Boolean.valueOf(bool.booleanValue() != z4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Boolean e(String str, zzfh$zzd zzfh_zzd) {
        if (nb.g0(str)) {
            try {
                return h(new BigDecimal(str), zzfh_zzd, 0.0d);
            } catch (NumberFormatException unused) {
                return null;
            }
        }
        return null;
    }

    private static Boolean f(String str, zzfh$zzf.zza zzaVar, boolean z4, String str2, List<String> list, String str3, x4 x4Var) {
        boolean startsWith;
        if (str == null) {
            return null;
        }
        if (zzaVar == zzfh$zzf.zza.IN_LIST) {
            if (list == null || list.isEmpty()) {
                return null;
            }
        } else if (str2 == null) {
            return null;
        }
        if (!z4 && zzaVar != zzfh$zzf.zza.REGEXP) {
            str = str.toUpperCase(Locale.ENGLISH);
        }
        switch (zb.f17248a[zzaVar.ordinal()]) {
            case 1:
                if (str3 == null) {
                    return null;
                }
                try {
                    return Boolean.valueOf(Pattern.compile(str3, z4 ? 0 : 66).matcher(str).matches());
                } catch (PatternSyntaxException unused) {
                    if (x4Var != null) {
                        x4Var.J().b("Invalid regular expression in REGEXP audience filter. expression", str3);
                    }
                    return null;
                }
            case 2:
                startsWith = str.startsWith(str2);
                break;
            case 3:
                startsWith = str.endsWith(str2);
                break;
            case 4:
                startsWith = str.contains(str2);
                break;
            case 5:
                startsWith = str.equals(str2);
                break;
            case 6:
                if (list != null) {
                    startsWith = list.contains(str);
                    break;
                } else {
                    return null;
                }
            default:
                return null;
        }
        return Boolean.valueOf(startsWith);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Boolean g(String str, zzfh$zzf zzfh_zzf, x4 x4Var) {
        List<String> list;
        n6.j.l(zzfh_zzf);
        if (str == null || !zzfh_zzf.P() || zzfh_zzf.H() == zzfh$zzf.zza.UNKNOWN_MATCH_TYPE) {
            return null;
        }
        zzfh$zzf.zza H = zzfh_zzf.H();
        zzfh$zzf.zza zzaVar = zzfh$zzf.zza.IN_LIST;
        if (H == zzaVar) {
            if (zzfh_zzf.m() == 0) {
                return null;
            }
        } else if (!zzfh_zzf.O()) {
            return null;
        }
        zzfh$zzf.zza H2 = zzfh_zzf.H();
        boolean M = zzfh_zzf.M();
        String K = (M || H2 == zzfh$zzf.zza.REGEXP || H2 == zzaVar) ? zzfh_zzf.K() : zzfh_zzf.K().toUpperCase(Locale.ENGLISH);
        if (zzfh_zzf.m() == 0) {
            list = null;
        } else {
            List<String> L = zzfh_zzf.L();
            if (!M) {
                ArrayList arrayList = new ArrayList(L.size());
                for (String str2 : L) {
                    arrayList.add(str2.toUpperCase(Locale.ENGLISH));
                }
                L = Collections.unmodifiableList(arrayList);
            }
            list = L;
        }
        return f(str, H2, M, K, list, H2 == zzfh$zzf.zza.REGEXP ? K : null, x4Var);
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0080, code lost:
        if (r3 != null) goto L24;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static java.lang.Boolean h(java.math.BigDecimal r8, com.google.android.gms.internal.measurement.zzfh$zzd r9, double r10) {
        /*
            Method dump skipped, instructions count: 277
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.c.h(java.math.BigDecimal, com.google.android.gms.internal.measurement.zzfh$zzd, double):java.lang.Boolean");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int a();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean i();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean j();
}
