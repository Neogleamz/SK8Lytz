package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzap extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzap> CREATOR = new s();

    /* renamed from: a  reason: collision with root package name */
    private final int f14898a;

    /* renamed from: b  reason: collision with root package name */
    private final int f14899b;

    /* renamed from: c  reason: collision with root package name */
    private final int f14900c;

    /* renamed from: d  reason: collision with root package name */
    private final int f14901d;

    /* renamed from: e  reason: collision with root package name */
    private final int f14902e;

    /* renamed from: f  reason: collision with root package name */
    private final int f14903f;

    /* renamed from: g  reason: collision with root package name */
    private final boolean f14904g;

    /* renamed from: h  reason: collision with root package name */
    private final String f14905h;

    public zzap(int i8, int i9, int i10, int i11, int i12, int i13, boolean z4, String str) {
        this.f14898a = i8;
        this.f14899b = i9;
        this.f14900c = i10;
        this.f14901d = i11;
        this.f14902e = i12;
        this.f14903f = i13;
        this.f14904g = z4;
        this.f14905h = str;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f14898a);
        o6.a.l(parcel, 2, this.f14899b);
        o6.a.l(parcel, 3, this.f14900c);
        o6.a.l(parcel, 4, this.f14901d);
        o6.a.l(parcel, 5, this.f14902e);
        o6.a.l(parcel, 6, this.f14903f);
        o6.a.c(parcel, 7, this.f14904g);
        o6.a.r(parcel, 8, this.f14905h, false);
        o6.a.b(parcel, a9);
    }
}
