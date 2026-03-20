package com.google.android.gms.dynamite;

import android.os.IBinder;
import android.os.Parcel;
import x6.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class m extends com.google.android.gms.internal.common.a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public m(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.dynamite.IDynamiteLoader");
    }

    public final int f() {
        Parcel d8 = d(6, e());
        int readInt = d8.readInt();
        d8.recycle();
        return readInt;
    }

    public final int g(x6.a aVar, String str, boolean z4) {
        Parcel e8 = e();
        com.google.android.gms.internal.common.h.d(e8, aVar);
        e8.writeString(str);
        e8.writeInt(z4 ? 1 : 0);
        Parcel d8 = d(3, e8);
        int readInt = d8.readInt();
        d8.recycle();
        return readInt;
    }

    public final int k(x6.a aVar, String str, boolean z4) {
        Parcel e8 = e();
        com.google.android.gms.internal.common.h.d(e8, aVar);
        e8.writeString(str);
        e8.writeInt(z4 ? 1 : 0);
        Parcel d8 = d(5, e8);
        int readInt = d8.readInt();
        d8.recycle();
        return readInt;
    }

    public final x6.a q(x6.a aVar, String str, int i8) {
        Parcel e8 = e();
        com.google.android.gms.internal.common.h.d(e8, aVar);
        e8.writeString(str);
        e8.writeInt(i8);
        Parcel d8 = d(2, e8);
        x6.a e9 = a.AbstractBinderC0227a.e(d8.readStrongBinder());
        d8.recycle();
        return e9;
    }

    public final x6.a v(x6.a aVar, String str, int i8, x6.a aVar2) {
        Parcel e8 = e();
        com.google.android.gms.internal.common.h.d(e8, aVar);
        e8.writeString(str);
        e8.writeInt(i8);
        com.google.android.gms.internal.common.h.d(e8, aVar2);
        Parcel d8 = d(8, e8);
        x6.a e9 = a.AbstractBinderC0227a.e(d8.readStrongBinder());
        d8.recycle();
        return e9;
    }

    public final x6.a x(x6.a aVar, String str, int i8) {
        Parcel e8 = e();
        com.google.android.gms.internal.common.h.d(e8, aVar);
        e8.writeString(str);
        e8.writeInt(i8);
        Parcel d8 = d(4, e8);
        x6.a e9 = a.AbstractBinderC0227a.e(d8.readStrongBinder());
        d8.recycle();
        return e9;
    }

    public final x6.a y(x6.a aVar, String str, boolean z4, long j8) {
        Parcel e8 = e();
        com.google.android.gms.internal.common.h.d(e8, aVar);
        e8.writeString(str);
        e8.writeInt(z4 ? 1 : 0);
        e8.writeLong(j8);
        Parcel d8 = d(7, e8);
        x6.a e9 = a.AbstractBinderC0227a.e(d8.readStrongBinder());
        d8.recycle();
        return e9;
    }
}
