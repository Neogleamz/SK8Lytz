package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import com.google.android.gms.measurement.internal.zziq;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class v {

    /* renamed from: f  reason: collision with root package name */
    public static final v f17035f = new v(null, 100);

    /* renamed from: a  reason: collision with root package name */
    private final int f17036a;

    /* renamed from: b  reason: collision with root package name */
    private final String f17037b;

    /* renamed from: c  reason: collision with root package name */
    private final Boolean f17038c;

    /* renamed from: d  reason: collision with root package name */
    private final String f17039d;

    /* renamed from: e  reason: collision with root package name */
    private final EnumMap<zziq.zza, zzip> f17040e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public v(Boolean bool, int i8) {
        this(bool, i8, (Boolean) null, (String) null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public v(Boolean bool, int i8, Boolean bool2, String str) {
        EnumMap<zziq.zza, zzip> enumMap = new EnumMap<>(zziq.zza.class);
        this.f17040e = enumMap;
        enumMap.put((EnumMap<zziq.zza, zzip>) zziq.zza.AD_USER_DATA, (zziq.zza) zziq.d(bool));
        this.f17036a = i8;
        this.f17037b = l();
        this.f17038c = bool2;
        this.f17039d = str;
    }

    private v(EnumMap<zziq.zza, zzip> enumMap, int i8, Boolean bool, String str) {
        EnumMap<zziq.zza, zzip> enumMap2 = new EnumMap<>(zziq.zza.class);
        this.f17040e = enumMap2;
        enumMap2.putAll(enumMap);
        this.f17036a = i8;
        this.f17037b = l();
        this.f17038c = bool;
        this.f17039d = str;
    }

    public static v b(Bundle bundle, int i8) {
        zziq.zza[] c9;
        if (bundle == null) {
            return new v(null, i8);
        }
        EnumMap enumMap = new EnumMap(zziq.zza.class);
        for (zziq.zza zzaVar : zzir.DMA.c()) {
            enumMap.put((EnumMap) zzaVar, (zziq.zza) zziq.e(bundle.getString(zzaVar.f17280a)));
        }
        return new v(enumMap, i8, bundle.containsKey("is_dma_region") ? Boolean.valueOf(bundle.getString("is_dma_region")) : null, bundle.getString("cps_display_str"));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static v c(zzip zzipVar, int i8) {
        EnumMap enumMap = new EnumMap(zziq.zza.class);
        enumMap.put((EnumMap) zziq.zza.AD_USER_DATA, (zziq.zza) zzipVar);
        return new v(enumMap, -10, (Boolean) null, (String) null);
    }

    public static v d(String str) {
        if (str == null || str.length() <= 0) {
            return f17035f;
        }
        String[] split = str.split(":");
        int parseInt = Integer.parseInt(split[0]);
        EnumMap enumMap = new EnumMap(zziq.zza.class);
        zziq.zza[] c9 = zzir.DMA.c();
        int length = c9.length;
        int i8 = 1;
        int i9 = 0;
        while (i9 < length) {
            enumMap.put((EnumMap) c9[i9], (zziq.zza) zziq.c(split[i8].charAt(0)));
            i9++;
            i8++;
        }
        return new v(enumMap, parseInt, (Boolean) null, (String) null);
    }

    public static Boolean e(Bundle bundle) {
        zzip e8;
        if (bundle == null || (e8 = zziq.e(bundle.getString("ad_personalization"))) == null) {
            return null;
        }
        int i8 = u.f17014a[e8.ordinal()];
        if (i8 != 3) {
            if (i8 != 4) {
                return null;
            }
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    private final String l() {
        zziq.zza[] c9;
        StringBuilder sb = new StringBuilder();
        sb.append(this.f17036a);
        for (zziq.zza zzaVar : zzir.DMA.c()) {
            sb.append(":");
            sb.append(zziq.a(this.f17040e.get(zzaVar)));
        }
        return sb.toString();
    }

    public final int a() {
        return this.f17036a;
    }

    public final boolean equals(Object obj) {
        if (obj instanceof v) {
            v vVar = (v) obj;
            if (this.f17037b.equalsIgnoreCase(vVar.f17037b) && Objects.equals(this.f17038c, vVar.f17038c)) {
                return Objects.equals(this.f17039d, vVar.f17039d);
            }
            return false;
        }
        return false;
    }

    public final Bundle f() {
        Bundle bundle = new Bundle();
        for (Map.Entry<zziq.zza, zzip> entry : this.f17040e.entrySet()) {
            String r4 = zziq.r(entry.getValue());
            if (r4 != null) {
                bundle.putString(entry.getKey().f17280a, r4);
            }
        }
        Boolean bool = this.f17038c;
        if (bool != null) {
            bundle.putString("is_dma_region", bool.toString());
        }
        String str = this.f17039d;
        if (str != null) {
            bundle.putString("cps_display_str", str);
        }
        return bundle;
    }

    public final zzip g() {
        zzip zzipVar = this.f17040e.get(zziq.zza.AD_USER_DATA);
        return zzipVar == null ? zzip.UNINITIALIZED : zzipVar;
    }

    public final Boolean h() {
        return this.f17038c;
    }

    public final int hashCode() {
        Boolean bool = this.f17038c;
        int i8 = bool == null ? 3 : bool == Boolean.TRUE ? 7 : 13;
        String str = this.f17039d;
        return this.f17037b.hashCode() + (i8 * 29) + ((str == null ? 17 : str.hashCode()) * 137);
    }

    public final String i() {
        return this.f17039d;
    }

    public final String j() {
        return this.f17037b;
    }

    public final boolean k() {
        for (zzip zzipVar : this.f17040e.values()) {
            if (zzipVar != zzip.UNINITIALIZED) {
                return true;
            }
        }
        return false;
    }

    public final String toString() {
        zziq.zza[] c9;
        int i8;
        String str;
        StringBuilder sb = new StringBuilder("source=");
        sb.append(zziq.j(this.f17036a));
        for (zziq.zza zzaVar : zzir.DMA.c()) {
            sb.append(",");
            sb.append(zzaVar.f17280a);
            sb.append("=");
            zzip zzipVar = this.f17040e.get(zzaVar);
            if (zzipVar == null || (i8 = u.f17014a[zzipVar.ordinal()]) == 1) {
                sb.append("uninitialized");
            } else {
                if (i8 == 2) {
                    str = "default";
                } else if (i8 == 3) {
                    str = "denied";
                } else if (i8 == 4) {
                    str = "granted";
                }
                sb.append(str);
            }
        }
        if (this.f17038c != null) {
            sb.append(",isDmaRegion=");
            sb.append(this.f17038c);
        }
        if (this.f17039d != null) {
            sb.append(",cpsDisplayStr=");
            sb.append(this.f17039d);
        }
        return sb.toString();
    }
}
