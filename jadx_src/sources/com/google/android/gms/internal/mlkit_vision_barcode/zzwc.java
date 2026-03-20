package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzwc extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzwc> CREATOR = new ai();

    /* renamed from: a  reason: collision with root package name */
    private final int f14696a;

    /* renamed from: b  reason: collision with root package name */
    private final int f14697b;

    /* renamed from: c  reason: collision with root package name */
    private final int f14698c;

    /* renamed from: d  reason: collision with root package name */
    private final int f14699d;

    /* renamed from: e  reason: collision with root package name */
    private final long f14700e;

    public zzwc(int i8, int i9, int i10, int i11, long j8) {
        this.f14696a = i8;
        this.f14697b = i9;
        this.f14698c = i10;
        this.f14699d = i11;
        this.f14700e = j8;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14696a);
        o6.a.l(parcel, 2, this.f14697b);
        o6.a.l(parcel, 3, this.f14698c);
        o6.a.l(parcel, 4, this.f14699d);
        o6.a.n(parcel, 5, this.f14700e);
        o6.a.b(parcel, a9);
    }
}
