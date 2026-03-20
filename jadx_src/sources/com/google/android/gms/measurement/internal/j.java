package com.google.android.gms.measurement.internal;

import com.google.android.gms.measurement.internal.zziq;
import java.util.EnumMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class j {

    /* renamed from: a  reason: collision with root package name */
    private final EnumMap<zziq.zza, i> f16690a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j() {
        this.f16690a = new EnumMap<>(zziq.zza.class);
    }

    private j(EnumMap<zziq.zza, i> enumMap) {
        EnumMap<zziq.zza, i> enumMap2 = new EnumMap<>(zziq.zza.class);
        this.f16690a = enumMap2;
        enumMap2.putAll(enumMap);
    }

    public static j b(String str) {
        EnumMap enumMap = new EnumMap(zziq.zza.class);
        if (str.length() >= zziq.zza.values().length) {
            int i8 = 0;
            if (str.charAt(0) == '1') {
                zziq.zza[] values = zziq.zza.values();
                int length = values.length;
                int i9 = 1;
                while (i8 < length) {
                    enumMap.put((EnumMap) values[i8], (zziq.zza) i.f(str.charAt(i9)));
                    i8++;
                    i9++;
                }
                return new j(enumMap);
            }
        }
        return new j();
    }

    public final i a(zziq.zza zzaVar) {
        i iVar = this.f16690a.get(zzaVar);
        return iVar == null ? i.UNSET : iVar;
    }

    public final void c(zziq.zza zzaVar, int i8) {
        i iVar = i.UNSET;
        if (i8 != -30) {
            if (i8 != -20) {
                if (i8 == -10) {
                    iVar = i.MANIFEST;
                } else if (i8 != 0) {
                    if (i8 == 30) {
                        iVar = i.INITIALIZATION;
                    }
                }
            }
            iVar = i.API;
        } else {
            iVar = i.TCF;
        }
        this.f16690a.put((EnumMap<zziq.zza, i>) zzaVar, (zziq.zza) iVar);
    }

    public final void d(zziq.zza zzaVar, i iVar) {
        this.f16690a.put((EnumMap<zziq.zza, i>) zzaVar, (zziq.zza) iVar);
    }

    public final String toString() {
        char c9;
        StringBuilder sb = new StringBuilder("1");
        for (zziq.zza zzaVar : zziq.zza.values()) {
            i iVar = this.f16690a.get(zzaVar);
            if (iVar == null) {
                iVar = i.UNSET;
            }
            c9 = iVar.f16668a;
            sb.append(c9);
        }
        return sb.toString();
    }
}
