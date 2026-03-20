package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public enum zzs {
    DEBUG(3),
    ERROR(6),
    INFO(4),
    VERBOSE(2),
    WARN(5);
    

    /* renamed from: a  reason: collision with root package name */
    private final int f12961a;

    zzs(int i8) {
        this.f12961a = i8;
    }

    public static zzs c(int i8) {
        return i8 != 2 ? i8 != 3 ? i8 != 5 ? i8 != 6 ? INFO : ERROR : WARN : DEBUG : VERBOSE;
    }
}
