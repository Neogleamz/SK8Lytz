package com.google.android.gms.common.stats;

import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class StatsEvent extends AbstractSafeParcelable implements ReflectedParcelable {
    public abstract String Z();

    public abstract int t();

    public final String toString() {
        long u8 = u();
        int t8 = t();
        String Z = Z();
        return u8 + "\t" + t8 + "\t-1" + Z;
    }

    public abstract long u();
}
