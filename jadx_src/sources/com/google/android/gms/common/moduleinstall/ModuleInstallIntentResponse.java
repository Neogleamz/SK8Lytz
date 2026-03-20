package com.google.android.gms.common.moduleinstall;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import o6.a;
import q6.f;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ModuleInstallIntentResponse extends AbstractSafeParcelable {
    public static final Parcelable.Creator<ModuleInstallIntentResponse> CREATOR = new f();

    /* renamed from: a  reason: collision with root package name */
    private final PendingIntent f11895a;

    public ModuleInstallIntentResponse(PendingIntent pendingIntent) {
        this.f11895a = pendingIntent;
    }

    public PendingIntent t() {
        return this.f11895a;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int a9 = a.a(parcel);
        a.q(parcel, 1, t(), i8, false);
        a.b(parcel, a9);
    }
}
