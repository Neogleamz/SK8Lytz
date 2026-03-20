package androidx.media2.session;

import android.content.ComponentName;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SessionTokenImplLegacyParcelizer {
    public static SessionTokenImplLegacy read(VersionedParcel versionedParcel) {
        SessionTokenImplLegacy sessionTokenImplLegacy = new SessionTokenImplLegacy();
        sessionTokenImplLegacy.f6226b = versionedParcel.k(sessionTokenImplLegacy.f6226b, 1);
        sessionTokenImplLegacy.f6227c = versionedParcel.v(sessionTokenImplLegacy.f6227c, 2);
        sessionTokenImplLegacy.f6228d = versionedParcel.v(sessionTokenImplLegacy.f6228d, 3);
        sessionTokenImplLegacy.f6229e = (ComponentName) versionedParcel.A(sessionTokenImplLegacy.f6229e, 4);
        sessionTokenImplLegacy.f6230f = versionedParcel.E(sessionTokenImplLegacy.f6230f, 5);
        sessionTokenImplLegacy.f6231g = versionedParcel.k(sessionTokenImplLegacy.f6231g, 6);
        sessionTokenImplLegacy.c();
        return sessionTokenImplLegacy;
    }

    public static void write(SessionTokenImplLegacy sessionTokenImplLegacy, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        sessionTokenImplLegacy.d(versionedParcel.g());
        versionedParcel.O(sessionTokenImplLegacy.f6226b, 1);
        versionedParcel.Y(sessionTokenImplLegacy.f6227c, 2);
        versionedParcel.Y(sessionTokenImplLegacy.f6228d, 3);
        versionedParcel.d0(sessionTokenImplLegacy.f6229e, 4);
        versionedParcel.h0(sessionTokenImplLegacy.f6230f, 5);
        versionedParcel.O(sessionTokenImplLegacy.f6231g, 6);
    }
}
