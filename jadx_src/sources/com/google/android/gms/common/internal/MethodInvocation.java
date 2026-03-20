package com.google.android.gms.common.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MethodInvocation extends AbstractSafeParcelable {
    public static final Parcelable.Creator<MethodInvocation> CREATOR = new n6.z();

    /* renamed from: a  reason: collision with root package name */
    private final int f11797a;

    /* renamed from: b  reason: collision with root package name */
    private final int f11798b;

    /* renamed from: c  reason: collision with root package name */
    private final int f11799c;

    /* renamed from: d  reason: collision with root package name */
    private final long f11800d;

    /* renamed from: e  reason: collision with root package name */
    private final long f11801e;

    /* renamed from: f  reason: collision with root package name */
    private final String f11802f;

    /* renamed from: g  reason: collision with root package name */
    private final String f11803g;

    /* renamed from: h  reason: collision with root package name */
    private final int f11804h;

    /* renamed from: j  reason: collision with root package name */
    private final int f11805j;

    @Deprecated
    public MethodInvocation(int i8, int i9, int i10, long j8, long j9, String str, String str2, int i11) {
        this(i8, i9, i10, j8, j9, str, str2, i11, -1);
    }

    public MethodInvocation(int i8, int i9, int i10, long j8, long j9, String str, String str2, int i11, int i12) {
        this.f11797a = i8;
        this.f11798b = i9;
        this.f11799c = i10;
        this.f11800d = j8;
        this.f11801e = j9;
        this.f11802f = str;
        this.f11803g = str2;
        this.f11804h = i11;
        this.f11805j = i12;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11797a);
        o6.a.l(parcel, 2, this.f11798b);
        o6.a.l(parcel, 3, this.f11799c);
        o6.a.n(parcel, 4, this.f11800d);
        o6.a.n(parcel, 5, this.f11801e);
        o6.a.r(parcel, 6, this.f11802f, false);
        o6.a.r(parcel, 7, this.f11803g, false);
        o6.a.l(parcel, 8, this.f11804h);
        o6.a.l(parcel, 9, this.f11805j);
        o6.a.b(parcel, a9);
    }
}
