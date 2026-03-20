package com.google.android.gms.signin.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zag extends AbstractSafeParcelable implements k6.e {
    public static final Parcelable.Creator<zag> CREATOR = new h7.d();

    /* renamed from: a  reason: collision with root package name */
    private final List f17320a;

    /* renamed from: b  reason: collision with root package name */
    private final String f17321b;

    public zag(List list, String str) {
        this.f17320a = list;
        this.f17321b = str;
    }

    @Override // k6.e
    public final Status p() {
        return this.f17321b != null ? Status.f11547f : Status.f11551k;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.t(parcel, 1, this.f17320a, false);
        o6.a.r(parcel, 2, this.f17321b, false);
        o6.a.b(parcel, a9);
    }
}
