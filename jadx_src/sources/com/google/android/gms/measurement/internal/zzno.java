package com.google.android.gms.measurement.internal;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzno extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzno> CREATOR = new qb();

    /* renamed from: a  reason: collision with root package name */
    private final int f17307a;

    /* renamed from: b  reason: collision with root package name */
    public final String f17308b;

    /* renamed from: c  reason: collision with root package name */
    public final long f17309c;

    /* renamed from: d  reason: collision with root package name */
    public final Long f17310d;

    /* renamed from: e  reason: collision with root package name */
    private final Float f17311e;

    /* renamed from: f  reason: collision with root package name */
    public final String f17312f;

    /* renamed from: g  reason: collision with root package name */
    public final String f17313g;

    /* renamed from: h  reason: collision with root package name */
    public final Double f17314h;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzno(int i8, String str, long j8, Long l8, Float f5, String str2, String str3, Double d8) {
        this.f17307a = i8;
        this.f17308b = str;
        this.f17309c = j8;
        this.f17310d = l8;
        this.f17311e = null;
        if (i8 == 1) {
            this.f17314h = f5 != null ? Double.valueOf(f5.doubleValue()) : null;
        } else {
            this.f17314h = d8;
        }
        this.f17312f = str2;
        this.f17313g = str3;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzno(pb pbVar) {
        this(pbVar.f16887c, pbVar.f16888d, pbVar.f16889e, pbVar.f16886b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzno(String str, long j8, Object obj, String str2) {
        n6.j.f(str);
        this.f17307a = 2;
        this.f17308b = str;
        this.f17309c = j8;
        this.f17313g = str2;
        if (obj == null) {
            this.f17310d = null;
            this.f17311e = null;
            this.f17314h = null;
            this.f17312f = null;
        } else if (obj instanceof Long) {
            this.f17310d = (Long) obj;
            this.f17311e = null;
            this.f17314h = null;
            this.f17312f = null;
        } else if (obj instanceof String) {
            this.f17310d = null;
            this.f17311e = null;
            this.f17314h = null;
            this.f17312f = (String) obj;
        } else if (!(obj instanceof Double)) {
            throw new IllegalArgumentException("User attribute given of un-supported type");
        } else {
            this.f17310d = null;
            this.f17311e = null;
            this.f17314h = (Double) obj;
            this.f17312f = null;
        }
    }

    public final Object t() {
        Long l8 = this.f17310d;
        if (l8 != null) {
            return l8;
        }
        Double d8 = this.f17314h;
        if (d8 != null) {
            return d8;
        }
        String str = this.f17312f;
        if (str != null) {
            return str;
        }
        return null;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f17307a);
        o6.a.r(parcel, 2, this.f17308b, false);
        o6.a.n(parcel, 3, this.f17309c);
        o6.a.o(parcel, 4, this.f17310d, false);
        o6.a.j(parcel, 5, null, false);
        o6.a.r(parcel, 6, this.f17312f, false);
        o6.a.r(parcel, 7, this.f17313g, false);
        o6.a.h(parcel, 8, this.f17314h, false);
        o6.a.b(parcel, a9);
    }
}
