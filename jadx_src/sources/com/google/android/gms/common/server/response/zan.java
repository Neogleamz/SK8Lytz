package com.google.android.gms.common.server.response;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.server.response.FastJsonResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import n6.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zan extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zan> CREATOR = new c();

    /* renamed from: a  reason: collision with root package name */
    final int f11976a;

    /* renamed from: b  reason: collision with root package name */
    private final HashMap f11977b;

    /* renamed from: c  reason: collision with root package name */
    private final String f11978c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zan(int i8, ArrayList arrayList, String str) {
        this.f11976a = i8;
        HashMap hashMap = new HashMap();
        int size = arrayList.size();
        for (int i9 = 0; i9 < size; i9++) {
            zal zalVar = (zal) arrayList.get(i9);
            String str2 = zalVar.f11971b;
            HashMap hashMap2 = new HashMap();
            int size2 = ((ArrayList) j.l(zalVar.f11972c)).size();
            for (int i10 = 0; i10 < size2; i10++) {
                zam zamVar = (zam) zalVar.f11972c.get(i10);
                hashMap2.put(zamVar.f11974b, zamVar.f11975c);
            }
            hashMap.put(str2, hashMap2);
        }
        this.f11977b = hashMap;
        this.f11978c = (String) j.l(str);
        Z();
    }

    public final void Z() {
        for (String str : this.f11977b.keySet()) {
            Map map = (Map) this.f11977b.get(str);
            for (String str2 : map.keySet()) {
                ((FastJsonResponse.Field) map.get(str2)).T0(this);
            }
        }
    }

    public final String t() {
        return this.f11978c;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        for (String str : this.f11977b.keySet()) {
            sb.append(str);
            sb.append(":\n");
            Map map = (Map) this.f11977b.get(str);
            for (String str2 : map.keySet()) {
                sb.append("  ");
                sb.append(str2);
                sb.append(": ");
                sb.append(map.get(str2));
            }
        }
        return sb.toString();
    }

    public final Map u(String str) {
        return (Map) this.f11977b.get(str);
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11976a);
        ArrayList arrayList = new ArrayList();
        for (String str : this.f11977b.keySet()) {
            arrayList.add(new zal(str, (Map) this.f11977b.get(str)));
        }
        o6.a.v(parcel, 2, arrayList, false);
        o6.a.r(parcel, 3, this.f11978c, false);
        o6.a.b(parcel, a9);
    }
}
