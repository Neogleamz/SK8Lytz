package androidx.media2.session;

import android.app.PendingIntent;
import androidx.media2.common.MediaItem;
import androidx.media2.common.MediaMetadata;
import androidx.media2.common.ParcelImplListSlice;
import androidx.media2.common.SessionPlayer;
import androidx.media2.common.VideoSize;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ConnectionResultParcelizer {
    public static ConnectionResult read(VersionedParcel versionedParcel) {
        ConnectionResult connectionResult = new ConnectionResult();
        connectionResult.f6144a = versionedParcel.v(connectionResult.f6144a, 0);
        connectionResult.f6146c = versionedParcel.G(connectionResult.f6146c, 1);
        connectionResult.f6156m = versionedParcel.v(connectionResult.f6156m, 10);
        connectionResult.f6157n = versionedParcel.v(connectionResult.f6157n, 11);
        connectionResult.f6158o = (ParcelImplListSlice) versionedParcel.A(connectionResult.f6158o, 12);
        connectionResult.f6159p = (SessionCommandGroup) versionedParcel.I(connectionResult.f6159p, 13);
        connectionResult.q = versionedParcel.v(connectionResult.q, 14);
        connectionResult.f6160r = versionedParcel.v(connectionResult.f6160r, 15);
        connectionResult.f6161s = versionedParcel.v(connectionResult.f6161s, 16);
        connectionResult.f6162t = versionedParcel.k(connectionResult.f6162t, 17);
        connectionResult.f6163u = (VideoSize) versionedParcel.I(connectionResult.f6163u, 18);
        connectionResult.f6164v = versionedParcel.w(connectionResult.f6164v, 19);
        connectionResult.f6147d = (PendingIntent) versionedParcel.A(connectionResult.f6147d, 2);
        connectionResult.f6165w = (SessionPlayer.TrackInfo) versionedParcel.I(connectionResult.f6165w, 20);
        connectionResult.f6166x = (SessionPlayer.TrackInfo) versionedParcel.I(connectionResult.f6166x, 21);
        connectionResult.f6167y = (SessionPlayer.TrackInfo) versionedParcel.I(connectionResult.f6167y, 23);
        connectionResult.f6168z = (SessionPlayer.TrackInfo) versionedParcel.I(connectionResult.f6168z, 24);
        connectionResult.A = (MediaMetadata) versionedParcel.I(connectionResult.A, 25);
        connectionResult.B = versionedParcel.v(connectionResult.B, 26);
        connectionResult.f6148e = versionedParcel.v(connectionResult.f6148e, 3);
        connectionResult.f6150g = (MediaItem) versionedParcel.I(connectionResult.f6150g, 4);
        connectionResult.f6151h = versionedParcel.y(connectionResult.f6151h, 5);
        connectionResult.f6152i = versionedParcel.y(connectionResult.f6152i, 6);
        connectionResult.f6153j = versionedParcel.s(connectionResult.f6153j, 7);
        connectionResult.f6154k = versionedParcel.y(connectionResult.f6154k, 8);
        connectionResult.f6155l = (MediaController$PlaybackInfo) versionedParcel.I(connectionResult.f6155l, 9);
        connectionResult.c();
        return connectionResult;
    }

    public static void write(ConnectionResult connectionResult, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        connectionResult.d(versionedParcel.g());
        versionedParcel.Y(connectionResult.f6144a, 0);
        versionedParcel.j0(connectionResult.f6146c, 1);
        versionedParcel.Y(connectionResult.f6156m, 10);
        versionedParcel.Y(connectionResult.f6157n, 11);
        versionedParcel.d0(connectionResult.f6158o, 12);
        versionedParcel.m0(connectionResult.f6159p, 13);
        versionedParcel.Y(connectionResult.q, 14);
        versionedParcel.Y(connectionResult.f6160r, 15);
        versionedParcel.Y(connectionResult.f6161s, 16);
        versionedParcel.O(connectionResult.f6162t, 17);
        versionedParcel.m0(connectionResult.f6163u, 18);
        versionedParcel.Z(connectionResult.f6164v, 19);
        versionedParcel.d0(connectionResult.f6147d, 2);
        versionedParcel.m0(connectionResult.f6165w, 20);
        versionedParcel.m0(connectionResult.f6166x, 21);
        versionedParcel.m0(connectionResult.f6167y, 23);
        versionedParcel.m0(connectionResult.f6168z, 24);
        versionedParcel.m0(connectionResult.A, 25);
        versionedParcel.Y(connectionResult.B, 26);
        versionedParcel.Y(connectionResult.f6148e, 3);
        versionedParcel.m0(connectionResult.f6150g, 4);
        versionedParcel.b0(connectionResult.f6151h, 5);
        versionedParcel.b0(connectionResult.f6152i, 6);
        versionedParcel.W(connectionResult.f6153j, 7);
        versionedParcel.b0(connectionResult.f6154k, 8);
        versionedParcel.m0(connectionResult.f6155l, 9);
    }
}
