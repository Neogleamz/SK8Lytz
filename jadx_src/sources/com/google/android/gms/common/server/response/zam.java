package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.server.response.FastJsonResponse;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zam extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zam> CREATOR = new b();

    /* renamed from: a  reason: collision with root package name */
    final int f11973a;

    /* renamed from: b  reason: collision with root package name */
    final String f11974b;

    /* renamed from: c  reason: collision with root package name */
    final FastJsonResponse.Field f11975c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zam(int i8, String str, FastJsonResponse.Field field) {
        this.f11973a = i8;
        this.f11974b = str;
        this.f11975c = field;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zam(String str, FastJsonResponse.Field field) {
        this.f11973a = 1;
        this.f11974b = str;
        this.f11975c = field;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11973a);
        o6.a.r(parcel, 2, this.f11974b, false);
        o6.a.q(parcel, 3, this.f11975c, i8, false);
        o6.a.b(parcel, a9);
    }
}
