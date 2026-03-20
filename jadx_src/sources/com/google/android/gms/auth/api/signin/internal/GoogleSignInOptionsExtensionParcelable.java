package com.google.android.gms.auth.api.signin.internal;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class GoogleSignInOptionsExtensionParcelable extends AbstractSafeParcelable {
    public static final Parcelable.Creator<GoogleSignInOptionsExtensionParcelable> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    final int f11467a;

    /* renamed from: b  reason: collision with root package name */
    private int f11468b;

    /* renamed from: c  reason: collision with root package name */
    private Bundle f11469c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public GoogleSignInOptionsExtensionParcelable(int i8, int i9, Bundle bundle) {
        this.f11467a = i8;
        this.f11468b = i9;
        this.f11469c = bundle;
    }

    public int t() {
        return this.f11468b;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11467a);
        o6.a.l(parcel, 2, t());
        o6.a.e(parcel, 3, this.f11469c, false);
        o6.a.b(parcel, a9);
    }
}
