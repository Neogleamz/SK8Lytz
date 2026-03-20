package com.google.android.gms.common;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import n6.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class Feature extends AbstractSafeParcelable {
    public static final Parcelable.Creator<Feature> CREATOR = new j6.e();

    /* renamed from: a  reason: collision with root package name */
    private final String f11528a;
    @Deprecated

    /* renamed from: b  reason: collision with root package name */
    private final int f11529b;

    /* renamed from: c  reason: collision with root package name */
    private final long f11530c;

    public Feature(String str, int i8, long j8) {
        this.f11528a = str;
        this.f11529b = i8;
        this.f11530c = j8;
    }

    public Feature(String str, long j8) {
        this.f11528a = str;
        this.f11530c = j8;
        this.f11529b = -1;
    }

    public final boolean equals(Object obj) {
        if (obj instanceof Feature) {
            Feature feature = (Feature) obj;
            if (((t() != null && t().equals(feature.t())) || (t() == null && feature.t() == null)) && u() == feature.u()) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return n6.i.b(t(), Long.valueOf(u()));
    }

    public String t() {
        return this.f11528a;
    }

    public final String toString() {
        i.a c9 = n6.i.c(this);
        c9.a("name", t());
        c9.a("version", Long.valueOf(u()));
        return c9.toString();
    }

    public long u() {
        long j8 = this.f11530c;
        return j8 == -1 ? this.f11529b : j8;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, t(), false);
        o6.a.l(parcel, 2, this.f11529b);
        o6.a.n(parcel, 3, u());
        o6.a.b(parcel, a9);
    }
}
