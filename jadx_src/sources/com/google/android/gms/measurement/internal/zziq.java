package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.EnumMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zziq {

    /* renamed from: c  reason: collision with root package name */
    public static final zziq f17272c = new zziq(null, null, 100);

    /* renamed from: a  reason: collision with root package name */
    private final EnumMap<zza, zzip> f17273a;

    /* renamed from: b  reason: collision with root package name */
    private final int f17274b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum zza {
        AD_STORAGE("ad_storage"),
        ANALYTICS_STORAGE("analytics_storage"),
        AD_USER_DATA("ad_user_data"),
        AD_PERSONALIZATION("ad_personalization");
        

        /* renamed from: a  reason: collision with root package name */
        public final String f17280a;

        zza(String str) {
            this.f17280a = str;
        }
    }

    public zziq(Boolean bool, Boolean bool2, int i8) {
        EnumMap<zza, zzip> enumMap = new EnumMap<>(zza.class);
        this.f17273a = enumMap;
        enumMap.put((EnumMap<zza, zzip>) zza.AD_STORAGE, (zza) d(bool));
        enumMap.put((EnumMap<zza, zzip>) zza.ANALYTICS_STORAGE, (zza) d(bool2));
        this.f17274b = i8;
    }

    private zziq(EnumMap<zza, zzip> enumMap, int i8) {
        EnumMap<zza, zzip> enumMap2 = new EnumMap<>(zza.class);
        this.f17273a = enumMap2;
        enumMap2.putAll(enumMap);
        this.f17274b = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static char a(zzip zzipVar) {
        if (zzipVar != null) {
            int ordinal = zzipVar.ordinal();
            if (ordinal != 1) {
                if (ordinal != 2) {
                    return ordinal != 3 ? '-' : '1';
                }
                return '0';
            }
            return '+';
        }
        return '-';
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzip c(char c9) {
        return c9 != '+' ? c9 != '0' ? c9 != '1' ? zzip.UNINITIALIZED : zzip.GRANTED : zzip.DENIED : zzip.DEFAULT;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzip d(Boolean bool) {
        return bool == null ? zzip.UNINITIALIZED : bool.booleanValue() ? zzip.GRANTED : zzip.DENIED;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzip e(String str) {
        return str == null ? zzip.UNINITIALIZED : str.equals("granted") ? zzip.GRANTED : str.equals("denied") ? zzip.DENIED : zzip.UNINITIALIZED;
    }

    public static zziq f(Bundle bundle, int i8) {
        zza[] zzaVarArr;
        if (bundle == null) {
            return new zziq(null, null, i8);
        }
        EnumMap enumMap = new EnumMap(zza.class);
        zzaVarArr = zzir.STORAGE.f17284a;
        for (zza zzaVar : zzaVarArr) {
            enumMap.put((EnumMap) zzaVar, (zza) e(bundle.getString(zzaVar.f17280a)));
        }
        return new zziq(enumMap, i8);
    }

    public static zziq g(zzip zzipVar, zzip zzipVar2, int i8) {
        EnumMap enumMap = new EnumMap(zza.class);
        enumMap.put((EnumMap) zza.AD_STORAGE, (zza) zzipVar);
        enumMap.put((EnumMap) zza.ANALYTICS_STORAGE, (zza) zzipVar2);
        return new zziq(enumMap, -10);
    }

    public static zziq i(String str, int i8) {
        EnumMap enumMap = new EnumMap(zza.class);
        if (str == null) {
            str = BuildConfig.FLAVOR;
        }
        zza[] c9 = zzir.STORAGE.c();
        for (int i9 = 0; i9 < c9.length; i9++) {
            int i10 = i9 + 2;
            enumMap.put((EnumMap) c9[i9], (zza) (i10 < str.length() ? c(str.charAt(i10)) : zzip.UNINITIALIZED));
        }
        return new zziq(enumMap, i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String j(int i8) {
        return i8 != -30 ? i8 != -20 ? i8 != -10 ? i8 != 0 ? i8 != 30 ? i8 != 90 ? i8 != 100 ? "OTHER" : "UNKNOWN" : "REMOTE_CONFIG" : "1P_INIT" : "1P_API" : "MANIFEST" : "API" : "TCF";
    }

    public static String k(Bundle bundle) {
        zza[] zzaVarArr;
        String string;
        zzaVarArr = zzir.STORAGE.f17284a;
        int length = zzaVarArr.length;
        int i8 = 0;
        while (true) {
            Boolean bool = null;
            if (i8 >= length) {
                return null;
            }
            zza zzaVar = zzaVarArr[i8];
            if (bundle.containsKey(zzaVar.f17280a) && (string = bundle.getString(zzaVar.f17280a)) != null) {
                if (string.equals("granted")) {
                    bool = Boolean.TRUE;
                } else if (string.equals("denied")) {
                    bool = Boolean.FALSE;
                }
                if (bool == null) {
                    return string;
                }
            }
            i8++;
        }
    }

    public static boolean l(int i8, int i9) {
        return ((i8 == -20 && i9 == -30) || ((i8 == -30 && i9 == -20) || i8 == i9)) || i8 < i9;
    }

    public static zziq q(String str) {
        return i(str, 100);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String r(zzip zzipVar) {
        int ordinal = zzipVar.ordinal();
        if (ordinal != 2) {
            if (ordinal != 3) {
                return null;
            }
            return "granted";
        }
        return "denied";
    }

    public final boolean A() {
        return m(zza.AD_STORAGE);
    }

    public final boolean B() {
        return m(zza.ANALYTICS_STORAGE);
    }

    public final boolean C() {
        for (zzip zzipVar : this.f17273a.values()) {
            if (zzipVar != zzip.UNINITIALIZED) {
                return true;
            }
        }
        return false;
    }

    public final int b() {
        return this.f17274b;
    }

    public final boolean equals(Object obj) {
        zza[] zzaVarArr;
        if (obj instanceof zziq) {
            zziq zziqVar = (zziq) obj;
            zzaVarArr = zzir.STORAGE.f17284a;
            for (zza zzaVar : zzaVarArr) {
                if (this.f17273a.get(zzaVar) != zziqVar.f17273a.get(zzaVar)) {
                    return false;
                }
            }
            return this.f17274b == zziqVar.f17274b;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0047  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x004a A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.google.android.gms.measurement.internal.zziq h(com.google.android.gms.measurement.internal.zziq r9) {
        /*
            r8 = this;
            java.util.EnumMap r0 = new java.util.EnumMap
            java.lang.Class<com.google.android.gms.measurement.internal.zziq$zza> r1 = com.google.android.gms.measurement.internal.zziq.zza.class
            r0.<init>(r1)
            com.google.android.gms.measurement.internal.zzir r1 = com.google.android.gms.measurement.internal.zzir.STORAGE
            com.google.android.gms.measurement.internal.zziq$zza[] r1 = com.google.android.gms.measurement.internal.zzir.f(r1)
            int r2 = r1.length
            r3 = 0
        Lf:
            if (r3 >= r2) goto L4d
            r4 = r1[r3]
            java.util.EnumMap<com.google.android.gms.measurement.internal.zziq$zza, com.google.android.gms.measurement.internal.zzip> r5 = r8.f17273a
            java.lang.Object r5 = r5.get(r4)
            com.google.android.gms.measurement.internal.zzip r5 = (com.google.android.gms.measurement.internal.zzip) r5
            java.util.EnumMap<com.google.android.gms.measurement.internal.zziq$zza, com.google.android.gms.measurement.internal.zzip> r6 = r9.f17273a
            java.lang.Object r6 = r6.get(r4)
            com.google.android.gms.measurement.internal.zzip r6 = (com.google.android.gms.measurement.internal.zzip) r6
            if (r5 != 0) goto L26
            goto L35
        L26:
            if (r6 != 0) goto L29
            goto L45
        L29:
            com.google.android.gms.measurement.internal.zzip r7 = com.google.android.gms.measurement.internal.zzip.UNINITIALIZED
            if (r5 != r7) goto L2e
            goto L35
        L2e:
            if (r6 != r7) goto L31
            goto L45
        L31:
            com.google.android.gms.measurement.internal.zzip r7 = com.google.android.gms.measurement.internal.zzip.DEFAULT
            if (r5 != r7) goto L37
        L35:
            r5 = r6
            goto L45
        L37:
            if (r6 != r7) goto L3a
            goto L45
        L3a:
            com.google.android.gms.measurement.internal.zzip r7 = com.google.android.gms.measurement.internal.zzip.DENIED
            if (r5 == r7) goto L44
            if (r6 != r7) goto L41
            goto L44
        L41:
            com.google.android.gms.measurement.internal.zzip r5 = com.google.android.gms.measurement.internal.zzip.GRANTED
            goto L45
        L44:
            r5 = r7
        L45:
            if (r5 == 0) goto L4a
            r0.put(r4, r5)
        L4a:
            int r3 = r3 + 1
            goto Lf
        L4d:
            com.google.android.gms.measurement.internal.zziq r9 = new com.google.android.gms.measurement.internal.zziq
            r1 = 100
            r9.<init>(r0, r1)
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.measurement.internal.zziq.h(com.google.android.gms.measurement.internal.zziq):com.google.android.gms.measurement.internal.zziq");
    }

    public final int hashCode() {
        int i8 = this.f17274b * 17;
        for (zzip zzipVar : this.f17273a.values()) {
            i8 = (i8 * 31) + zzipVar.hashCode();
        }
        return i8;
    }

    public final boolean m(zza zzaVar) {
        return this.f17273a.get(zzaVar) != zzip.DENIED;
    }

    public final boolean n(zziq zziqVar, zza... zzaVarArr) {
        for (zza zzaVar : zzaVarArr) {
            if (!zziqVar.m(zzaVar) && m(zzaVar)) {
                return true;
            }
        }
        return false;
    }

    public final Bundle o() {
        Bundle bundle = new Bundle();
        for (Map.Entry<zza, zzip> entry : this.f17273a.entrySet()) {
            String r4 = r(entry.getValue());
            if (r4 != null) {
                bundle.putString(entry.getKey().f17280a, r4);
            }
        }
        return bundle;
    }

    public final zziq p(zziq zziqVar) {
        zza[] zzaVarArr;
        EnumMap enumMap = new EnumMap(zza.class);
        zzaVarArr = zzir.STORAGE.f17284a;
        for (zza zzaVar : zzaVarArr) {
            zzip zzipVar = this.f17273a.get(zzaVar);
            if (zzipVar == zzip.UNINITIALIZED) {
                zzipVar = zziqVar.f17273a.get(zzaVar);
            }
            if (zzipVar != null) {
                enumMap.put((EnumMap) zzaVar, (zza) zzipVar);
            }
        }
        return new zziq(enumMap, this.f17274b);
    }

    public final boolean s(zziq zziqVar, zza... zzaVarArr) {
        for (zza zzaVar : zzaVarArr) {
            zzip zzipVar = this.f17273a.get(zzaVar);
            zzip zzipVar2 = zziqVar.f17273a.get(zzaVar);
            zzip zzipVar3 = zzip.DENIED;
            if (zzipVar == zzipVar3 && zzipVar2 != zzipVar3) {
                return true;
            }
        }
        return false;
    }

    public final zzip t() {
        zzip zzipVar = this.f17273a.get(zza.AD_STORAGE);
        return zzipVar == null ? zzip.UNINITIALIZED : zzipVar;
    }

    public final String toString() {
        zza[] zzaVarArr;
        int ordinal;
        String str;
        StringBuilder sb = new StringBuilder("source=");
        sb.append(j(this.f17274b));
        zzaVarArr = zzir.STORAGE.f17284a;
        for (zza zzaVar : zzaVarArr) {
            sb.append(",");
            sb.append(zzaVar.f17280a);
            sb.append("=");
            zzip zzipVar = this.f17273a.get(zzaVar);
            if (zzipVar == null || (ordinal = zzipVar.ordinal()) == 0) {
                sb.append("uninitialized");
            } else {
                if (ordinal == 1) {
                    str = "default";
                } else if (ordinal == 2) {
                    str = "denied";
                } else if (ordinal == 3) {
                    str = "granted";
                }
                sb.append(str);
            }
        }
        return sb.toString();
    }

    public final boolean u(zziq zziqVar) {
        return s(zziqVar, (zza[]) this.f17273a.keySet().toArray(new zza[0]));
    }

    public final zzip v() {
        zzip zzipVar = this.f17273a.get(zza.ANALYTICS_STORAGE);
        return zzipVar == null ? zzip.UNINITIALIZED : zzipVar;
    }

    public final Boolean w() {
        zzip zzipVar = this.f17273a.get(zza.AD_STORAGE);
        if (zzipVar != null) {
            int ordinal = zzipVar.ordinal();
            if (ordinal != 1) {
                if (ordinal == 2) {
                    return Boolean.FALSE;
                }
                if (ordinal != 3) {
                    return null;
                }
            }
            return Boolean.TRUE;
        }
        return null;
    }

    public final Boolean x() {
        zzip zzipVar = this.f17273a.get(zza.ANALYTICS_STORAGE);
        if (zzipVar != null) {
            int ordinal = zzipVar.ordinal();
            if (ordinal != 1) {
                if (ordinal == 2) {
                    return Boolean.FALSE;
                }
                if (ordinal != 3) {
                    return null;
                }
            }
            return Boolean.TRUE;
        }
        return null;
    }

    public final String y() {
        int ordinal;
        StringBuilder sb = new StringBuilder("G1");
        for (zza zzaVar : zzir.STORAGE.c()) {
            zzip zzipVar = this.f17273a.get(zzaVar);
            char c9 = '-';
            if (zzipVar != null && (ordinal = zzipVar.ordinal()) != 0) {
                if (ordinal != 1) {
                    if (ordinal == 2) {
                        c9 = '0';
                    } else if (ordinal != 3) {
                    }
                }
                c9 = '1';
            }
            sb.append(c9);
        }
        return sb.toString();
    }

    public final String z() {
        StringBuilder sb = new StringBuilder("G1");
        for (zza zzaVar : zzir.STORAGE.c()) {
            sb.append(a(this.f17273a.get(zzaVar)));
        }
        return sb.toString();
    }
}
