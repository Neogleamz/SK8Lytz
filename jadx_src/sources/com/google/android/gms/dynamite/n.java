package com.google.android.gms.dynamite;

import android.os.IBinder;
import android.os.Parcel;
import x6.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n extends com.google.android.gms.internal.common.a {
    /* JADX INFO: Access modifiers changed from: package-private */
    public n(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.dynamite.IDynamiteLoaderV2");
    }

    public final x6.a f(x6.a aVar, String str, int i8, x6.a aVar2) {
        Parcel e8 = e();
        com.google.android.gms.internal.common.h.d(e8, aVar);
        e8.writeString(str);
        e8.writeInt(i8);
        com.google.android.gms.internal.common.h.d(e8, aVar2);
        Parcel d8 = d(2, e8);
        x6.a e9 = a.AbstractBinderC0227a.e(d8.readStrongBinder());
        d8.recycle();
        return e9;
    }

    public final x6.a g(x6.a aVar, String str, int i8, x6.a aVar2) {
        Parcel e8 = e();
        com.google.android.gms.internal.common.h.d(e8, aVar);
        e8.writeString(str);
        e8.writeInt(i8);
        com.google.android.gms.internal.common.h.d(e8, aVar2);
        Parcel d8 = d(3, e8);
        x6.a e9 = a.AbstractBinderC0227a.e(d8.readStrongBinder());
        d8.recycle();
        return e9;
    }
}
