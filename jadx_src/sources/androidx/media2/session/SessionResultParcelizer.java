package androidx.media2.session;

import androidx.media2.common.MediaItem;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SessionResultParcelizer {
    public static SessionResult read(VersionedParcel versionedParcel) {
        SessionResult sessionResult = new SessionResult();
        sessionResult.f6212a = versionedParcel.v(sessionResult.f6212a, 1);
        sessionResult.f6213b = versionedParcel.y(sessionResult.f6213b, 2);
        sessionResult.f6214c = versionedParcel.k(sessionResult.f6214c, 3);
        sessionResult.f6216e = (MediaItem) versionedParcel.I(sessionResult.f6216e, 4);
        sessionResult.c();
        return sessionResult;
    }

    public static void write(SessionResult sessionResult, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        sessionResult.d(versionedParcel.g());
        versionedParcel.Y(sessionResult.f6212a, 1);
        versionedParcel.b0(sessionResult.f6213b, 2);
        versionedParcel.O(sessionResult.f6214c, 3);
        versionedParcel.m0(sessionResult.f6216e, 4);
    }
}
