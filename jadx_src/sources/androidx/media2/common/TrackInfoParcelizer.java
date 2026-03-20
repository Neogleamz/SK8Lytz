package androidx.media2.common;

import androidx.media2.common.SessionPlayer;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class TrackInfoParcelizer {
    public static SessionPlayer.TrackInfo read(VersionedParcel versionedParcel) {
        SessionPlayer.TrackInfo trackInfo = new SessionPlayer.TrackInfo();
        trackInfo.f6129a = versionedParcel.v(trackInfo.f6129a, 1);
        trackInfo.f6130b = versionedParcel.v(trackInfo.f6130b, 3);
        trackInfo.f6133e = versionedParcel.k(trackInfo.f6133e, 4);
        trackInfo.c();
        return trackInfo;
    }

    public static void write(SessionPlayer.TrackInfo trackInfo, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        trackInfo.d(versionedParcel.g());
        versionedParcel.Y(trackInfo.f6129a, 1);
        versionedParcel.Y(trackInfo.f6130b, 3);
        versionedParcel.O(trackInfo.f6133e, 4);
    }
}
