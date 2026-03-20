package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.server.response.FastJsonResponse;
import java.util.ArrayList;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zal extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zal> CREATOR = new d();

    /* renamed from: a  reason: collision with root package name */
    final int f11970a;

    /* renamed from: b  reason: collision with root package name */
    final String f11971b;

    /* renamed from: c  reason: collision with root package name */
    final ArrayList f11972c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zal(int i8, String str, ArrayList arrayList) {
        this.f11970a = i8;
        this.f11971b = str;
        this.f11972c = arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zal(String str, Map map) {
        ArrayList arrayList;
        this.f11970a = 1;
        this.f11971b = str;
        if (map == null) {
            arrayList = null;
        } else {
            arrayList = new ArrayList();
            for (String str2 : map.keySet()) {
                arrayList.add(new zam(str2, (FastJsonResponse.Field) map.get(str2)));
            }
        }
        this.f11972c = arrayList;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11970a);
        o6.a.r(parcel, 2, this.f11971b, false);
        o6.a.v(parcel, 3, this.f11972c, false);
        o6.a.b(parcel, a9);
    }
}
