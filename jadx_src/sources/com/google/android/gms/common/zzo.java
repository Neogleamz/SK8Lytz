package com.google.android.gms.common;

import android.content.Context;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import x6.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzo extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzo> CREATOR = new s();

    /* renamed from: a  reason: collision with root package name */
    private final String f11999a;

    /* renamed from: b  reason: collision with root package name */
    private final boolean f12000b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f12001c;

    /* renamed from: d  reason: collision with root package name */
    private final Context f12002d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f12003e;

    /* renamed from: f  reason: collision with root package name */
    private final boolean f12004f;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzo(String str, boolean z4, boolean z8, IBinder iBinder, boolean z9, boolean z10) {
        this.f11999a = str;
        this.f12000b = z4;
        this.f12001c = z8;
        this.f12002d = (Context) x6.b.f(a.AbstractBinderC0227a.e(iBinder));
        this.f12003e = z9;
        this.f12004f = z10;
    }

    /* JADX WARN: Type inference failed for: r5v5, types: [x6.a, android.os.IBinder] */
    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        String str = this.f11999a;
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, str, false);
        o6.a.c(parcel, 2, this.f12000b);
        o6.a.c(parcel, 3, this.f12001c);
        o6.a.k(parcel, 4, x6.b.g(this.f12002d), false);
        o6.a.c(parcel, 5, this.f12003e);
        o6.a.c(parcel, 6, this.f12004f);
        o6.a.b(parcel, a9);
    }
}
