package com.google.android.gms.measurement.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
enum i {
    UNSET('0'),
    REMOTE_DEFAULT('1'),
    REMOTE_DELEGATION('2'),
    MANIFEST('3'),
    INITIALIZATION('4'),
    API('5'),
    CHILD_ACCOUNT('6'),
    TCF('7'),
    REMOTE_ENFORCED_DEFAULT('8'),
    FAILSAFE('9');
    

    /* renamed from: a  reason: collision with root package name */
    private final char f16668a;

    i(char c9) {
        this.f16668a = c9;
    }

    public static i f(char c9) {
        i[] values;
        for (i iVar : values()) {
            if (iVar.f16668a == c9) {
                return iVar;
            }
        }
        return UNSET;
    }
}
