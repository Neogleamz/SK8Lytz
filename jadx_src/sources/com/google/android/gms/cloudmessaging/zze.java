package com.google.android.gms.cloudmessaging;

import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.cloudmessaging.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class zze implements Parcelable {
    public static final Parcelable.Creator<zze> CREATOR = new e();

    /* renamed from: a  reason: collision with root package name */
    Messenger f11521a;

    /* renamed from: b  reason: collision with root package name */
    a f11522b;

    public zze(IBinder iBinder) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.f11521a = new Messenger(iBinder);
        } else {
            this.f11522b = new a.C0120a(iBinder);
        }
    }

    public final IBinder a() {
        Messenger messenger = this.f11521a;
        return messenger != null ? messenger.getBinder() : this.f11522b.asBinder();
    }

    public final void b(Message message) {
        Messenger messenger = this.f11521a;
        if (messenger != null) {
            messenger.send(message);
        } else {
            this.f11522b.p(message);
        }
    }

    @Override // android.os.Parcelable
    public final int describeContents() {
        return 0;
    }

    public final boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        try {
            return a().equals(((zze) obj).a());
        } catch (ClassCastException unused) {
            return false;
        }
    }

    public final int hashCode() {
        return a().hashCode();
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        Messenger messenger = this.f11521a;
        parcel.writeStrongBinder(messenger != null ? messenger.getBinder() : this.f11522b.asBinder());
    }
}
