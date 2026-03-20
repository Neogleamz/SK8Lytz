package androidx.media2.session;

import android.content.ComponentName;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SessionTokenImplBaseParcelizer {
    public static SessionTokenImplBase read(VersionedParcel versionedParcel) {
        SessionTokenImplBase sessionTokenImplBase = new SessionTokenImplBase();
        sessionTokenImplBase.f6218a = versionedParcel.v(sessionTokenImplBase.f6218a, 1);
        sessionTokenImplBase.f6219b = versionedParcel.v(sessionTokenImplBase.f6219b, 2);
        sessionTokenImplBase.f6220c = versionedParcel.E(sessionTokenImplBase.f6220c, 3);
        sessionTokenImplBase.f6221d = versionedParcel.E(sessionTokenImplBase.f6221d, 4);
        sessionTokenImplBase.f6222e = versionedParcel.G(sessionTokenImplBase.f6222e, 5);
        sessionTokenImplBase.f6223f = (ComponentName) versionedParcel.A(sessionTokenImplBase.f6223f, 6);
        sessionTokenImplBase.f6224g = versionedParcel.k(sessionTokenImplBase.f6224g, 7);
        return sessionTokenImplBase;
    }

    public static void write(SessionTokenImplBase sessionTokenImplBase, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.Y(sessionTokenImplBase.f6218a, 1);
        versionedParcel.Y(sessionTokenImplBase.f6219b, 2);
        versionedParcel.h0(sessionTokenImplBase.f6220c, 3);
        versionedParcel.h0(sessionTokenImplBase.f6221d, 4);
        versionedParcel.j0(sessionTokenImplBase.f6222e, 5);
        versionedParcel.d0(sessionTokenImplBase.f6223f, 6);
        versionedParcel.O(sessionTokenImplBase.f6224g, 7);
    }
}
