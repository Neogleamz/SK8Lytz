package androidx.core.app;

import android.app.PendingIntent;
import androidx.core.graphics.drawable.IconCompat;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class RemoteActionCompatParcelizer {
    public static RemoteActionCompat read(VersionedParcel versionedParcel) {
        RemoteActionCompat remoteActionCompat = new RemoteActionCompat();
        remoteActionCompat.f4443a = (IconCompat) versionedParcel.I(remoteActionCompat.f4443a, 1);
        remoteActionCompat.f4444b = versionedParcel.o(remoteActionCompat.f4444b, 2);
        remoteActionCompat.f4445c = versionedParcel.o(remoteActionCompat.f4445c, 3);
        remoteActionCompat.f4446d = (PendingIntent) versionedParcel.A(remoteActionCompat.f4446d, 4);
        remoteActionCompat.f4447e = versionedParcel.i(remoteActionCompat.f4447e, 5);
        remoteActionCompat.f4448f = versionedParcel.i(remoteActionCompat.f4448f, 6);
        return remoteActionCompat;
    }

    public static void write(RemoteActionCompat remoteActionCompat, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.m0(remoteActionCompat.f4443a, 1);
        versionedParcel.S(remoteActionCompat.f4444b, 2);
        versionedParcel.S(remoteActionCompat.f4445c, 3);
        versionedParcel.d0(remoteActionCompat.f4446d, 4);
        versionedParcel.M(remoteActionCompat.f4447e, 5);
        versionedParcel.M(remoteActionCompat.f4448f, 6);
    }
}
