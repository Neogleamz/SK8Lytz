package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.server.response.FastJsonResponse;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zaa extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zaa> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    final int f11947a;

    /* renamed from: b  reason: collision with root package name */
    private final StringToIntConverter f11948b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zaa(int i8, StringToIntConverter stringToIntConverter) {
        this.f11947a = i8;
        this.f11948b = stringToIntConverter;
    }

    private zaa(StringToIntConverter stringToIntConverter) {
        this.f11947a = 1;
        this.f11948b = stringToIntConverter;
    }

    public static zaa t(FastJsonResponse.a aVar) {
        if (aVar instanceof StringToIntConverter) {
            return new zaa((StringToIntConverter) aVar);
        }
        throw new IllegalArgumentException("Unsupported safe parcelable field converter class.");
    }

    public final FastJsonResponse.a u() {
        StringToIntConverter stringToIntConverter = this.f11948b;
        if (stringToIntConverter != null) {
            return stringToIntConverter;
        }
        throw new IllegalStateException("There was no converter wrapped in this ConverterWrapper.");
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11947a);
        o6.a.q(parcel, 2, this.f11948b, i8, false);
        o6.a.b(parcel, a9);
    }
}
