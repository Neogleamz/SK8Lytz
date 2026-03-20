package com.google.android.gms.measurement.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzba extends AbstractSafeParcelable implements Iterable<String> {
    public static final Parcelable.Creator<zzba> CREATOR = new a0();

    /* renamed from: a  reason: collision with root package name */
    private final Bundle f17262a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzba(Bundle bundle) {
        this.f17262a = bundle;
    }

    public final Bundle D0() {
        return new Bundle(this.f17262a);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Long E0(String str) {
        return Long.valueOf(this.f17262a.getLong(str));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Object I0(String str) {
        return this.f17262a.get(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final String T0(String str) {
        return this.f17262a.getString(str);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Double Z(String str) {
        return Double.valueOf(this.f17262a.getDouble(str));
    }

    @Override // java.lang.Iterable
    public final Iterator<String> iterator() {
        return new b0(this);
    }

    public final int t() {
        return this.f17262a.size();
    }

    public final String toString() {
        return this.f17262a.toString();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.e(parcel, 2, D0(), false);
        o6.a.b(parcel, a9);
    }
}
