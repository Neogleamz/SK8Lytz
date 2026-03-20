package n6;

import android.os.IBinder;
import android.os.Parcel;
import com.google.android.gms.common.zzo;
import com.google.android.gms.common.zzq;
import com.google.android.gms.common.zzs;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c0 extends com.google.android.gms.internal.common.a implements e0 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public c0(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IGoogleCertificatesApi");
    }

    @Override // n6.e0
    public final zzq R0(zzo zzoVar) {
        Parcel e8 = e();
        com.google.android.gms.internal.common.h.c(e8, zzoVar);
        Parcel d8 = d(8, e8);
        zzq zzqVar = (zzq) com.google.android.gms.internal.common.h.a(d8, zzq.CREATOR);
        d8.recycle();
        return zzqVar;
    }

    @Override // n6.e0
    public final boolean T0(zzs zzsVar, x6.a aVar) {
        Parcel e8 = e();
        com.google.android.gms.internal.common.h.c(e8, zzsVar);
        com.google.android.gms.internal.common.h.d(e8, aVar);
        Parcel d8 = d(5, e8);
        boolean e9 = com.google.android.gms.internal.common.h.e(d8);
        d8.recycle();
        return e9;
    }

    @Override // n6.e0
    public final boolean j() {
        Parcel d8 = d(7, e());
        boolean e8 = com.google.android.gms.internal.common.h.e(d8);
        d8.recycle();
        return e8;
    }

    @Override // n6.e0
    public final zzq q0(zzo zzoVar) {
        Parcel e8 = e();
        com.google.android.gms.internal.common.h.c(e8, zzoVar);
        Parcel d8 = d(6, e8);
        zzq zzqVar = (zzq) com.google.android.gms.internal.common.h.a(d8, zzq.CREATOR);
        d8.recycle();
        return zzqVar;
    }
}
