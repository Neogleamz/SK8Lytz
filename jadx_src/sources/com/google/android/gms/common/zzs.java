package com.google.android.gms.common;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import n6.o0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zzs extends AbstractSafeParcelable {
    public static final Parcelable.Creator<zzs> CREATOR = new u();

    /* renamed from: a  reason: collision with root package name */
    private final String f12009a;

    /* renamed from: b  reason: collision with root package name */
    private final n f12010b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f12011c;

    /* renamed from: d  reason: collision with root package name */
    private final boolean f12012d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzs(String str, IBinder iBinder, boolean z4, boolean z8) {
        this.f12009a = str;
        o oVar = null;
        if (iBinder != null) {
            try {
                x6.a b9 = o0.e(iBinder).b();
                byte[] bArr = b9 == null ? null : (byte[]) x6.b.f(b9);
                if (bArr != null) {
                    oVar = new o(bArr);
                } else {
                    Log.e("GoogleCertificatesQuery", "Could not unwrap certificate");
                }
            } catch (RemoteException e8) {
                Log.e("GoogleCertificatesQuery", "Could not unwrap certificate", e8);
            }
        }
        this.f12010b = oVar;
        this.f12011c = z4;
        this.f12012d = z8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzs(String str, n nVar, boolean z4, boolean z8) {
        this.f12009a = str;
        this.f12010b = nVar;
        this.f12011c = z4;
        this.f12012d = z8;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        String str = this.f12009a;
        int a9 = o6.a.a(parcel);
        o6.a.r(parcel, 1, str, false);
        n nVar = this.f12010b;
        if (nVar == null) {
            Log.w("GoogleCertificatesQuery", "certificate binder is null");
            nVar = null;
        }
        o6.a.k(parcel, 2, nVar, false);
        o6.a.c(parcel, 3, this.f12011c);
        o6.a.c(parcel, 4, this.f12012d);
        o6.a.b(parcel, a9);
    }
}
