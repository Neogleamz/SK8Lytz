package com.google.android.gms.cloudmessaging;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class CloudMessage extends AbstractSafeParcelable {
    public static final Parcelable.Creator<CloudMessage> CREATOR = new i6.c();

    /* renamed from: a  reason: collision with root package name */
    final Intent f11470a;

    public CloudMessage(Intent intent) {
        this.f11470a = intent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Integer Z() {
        if (this.f11470a.hasExtra("google.product_id")) {
            return Integer.valueOf(this.f11470a.getIntExtra("google.product_id", 0));
        }
        return null;
    }

    public Intent t() {
        return this.f11470a;
    }

    public String u() {
        String stringExtra = this.f11470a.getStringExtra("google.message_id");
        return stringExtra == null ? this.f11470a.getStringExtra("message_id") : stringExtra;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.q(parcel, 1, this.f11470a, i8, false);
        o6.a.b(parcel, a9);
    }
}
