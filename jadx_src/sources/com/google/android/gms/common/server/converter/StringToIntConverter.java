package com.google.android.gms.common.server.converter;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseArray;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import com.google.android.gms.common.server.response.FastJsonResponse;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class StringToIntConverter extends AbstractSafeParcelable implements FastJsonResponse.a<String, Integer> {
    public static final Parcelable.Creator<StringToIntConverter> CREATOR = new b();

    /* renamed from: a  reason: collision with root package name */
    final int f11944a;

    /* renamed from: b  reason: collision with root package name */
    private final HashMap f11945b;

    /* renamed from: c  reason: collision with root package name */
    private final SparseArray f11946c;

    public StringToIntConverter() {
        this.f11944a = 1;
        this.f11945b = new HashMap();
        this.f11946c = new SparseArray();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public StringToIntConverter(int i8, ArrayList arrayList) {
        this.f11944a = i8;
        this.f11945b = new HashMap();
        this.f11946c = new SparseArray();
        int size = arrayList.size();
        for (int i9 = 0; i9 < size; i9++) {
            zac zacVar = (zac) arrayList.get(i9);
            t(zacVar.f11950b, zacVar.f11951c);
        }
    }

    @Override // com.google.android.gms.common.server.response.FastJsonResponse.a
    public final /* bridge */ /* synthetic */ Object h(Object obj) {
        String str = (String) this.f11946c.get(((Integer) obj).intValue());
        return (str == null && this.f11945b.containsKey("gms_unknown")) ? "gms_unknown" : str;
    }

    public StringToIntConverter t(String str, int i8) {
        this.f11945b.put(str, Integer.valueOf(i8));
        this.f11946c.put(i8, str);
        return this;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11944a);
        ArrayList arrayList = new ArrayList();
        for (String str : this.f11945b.keySet()) {
            arrayList.add(new zac(str, ((Integer) this.f11945b.get(str)).intValue()));
        }
        o6.a.v(parcel, 2, arrayList, false);
        o6.a.b(parcel, a9);
    }
}
