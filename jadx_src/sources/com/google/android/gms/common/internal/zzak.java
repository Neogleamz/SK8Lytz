package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzak extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzak> CREATOR = new o();

    /* renamed from: a  reason: collision with root package name */
    final int f11888a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzak(int i8) {
        this.f11888a = i8;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int i9 = this.f11888a;
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, i9);
        o6.a.b(parcel, a9);
    }
}
