package com.google.android.gms.internal.mlkit_vision_barcode;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzj extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzj> CREATOR = new xh();

    /* renamed from: a  reason: collision with root package name */
    public int f14322a;

    /* renamed from: b  reason: collision with root package name */
    public int f14323b;

    /* renamed from: c  reason: collision with root package name */
    public int f14324c;

    /* renamed from: d  reason: collision with root package name */
    public int f14325d;

    /* renamed from: e  reason: collision with root package name */
    public int f14326e;

    /* renamed from: f  reason: collision with root package name */
    public int f14327f;

    /* renamed from: g  reason: collision with root package name */
    public boolean f14328g;

    /* renamed from: h  reason: collision with root package name */
    public String f14329h;

    public zzj() {
    }

    public zzj(int i8, int i9, int i10, int i11, int i12, int i13, boolean z4, String str) {
        this.f14322a = i8;
        this.f14323b = i9;
        this.f14324c = i10;
        this.f14325d = i11;
        this.f14326e = i12;
        this.f14327f = i13;
        this.f14328g = z4;
        this.f14329h = str;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 2, this.f14322a);
        o6.a.l(parcel, 3, this.f14323b);
        o6.a.l(parcel, 4, this.f14324c);
        o6.a.l(parcel, 5, this.f14325d);
        o6.a.l(parcel, 6, this.f14326e);
        o6.a.l(parcel, 7, this.f14327f);
        o6.a.c(parcel, 8, this.f14328g);
        o6.a.r(parcel, 9, this.f14329h, false);
        o6.a.b(parcel, a9);
    }
}
