package com.google.android.gms.signin.internal;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zaa extends AbstractSafeParcelable implements k6.e {
    public static final Parcelable.Creator<zaa> CREATOR = new b();

    /* renamed from: a  reason: collision with root package name */
    final int f17317a;

    /* renamed from: b  reason: collision with root package name */
    private int f17318b;

    /* renamed from: c  reason: collision with root package name */
    private Intent f17319c;

    public zaa() {
        this(2, 0, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zaa(int i8, int i9, Intent intent) {
        this.f17317a = i8;
        this.f17318b = i9;
        this.f17319c = intent;
    }

    @Override // k6.e
    public final Status p() {
        return this.f17318b == 0 ? Status.f11547f : Status.f11551k;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f17317a);
        o6.a.l(parcel, 2, this.f17318b);
        o6.a.q(parcel, 3, this.f17319c, i8, false);
        o6.a.b(parcel, a9);
    }
}
