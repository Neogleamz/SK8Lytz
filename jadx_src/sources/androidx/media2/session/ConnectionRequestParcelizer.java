package androidx.media2.session;

import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ConnectionRequestParcelizer {
    public static ConnectionRequest read(VersionedParcel versionedParcel) {
        ConnectionRequest connectionRequest = new ConnectionRequest();
        connectionRequest.f6140a = versionedParcel.v(connectionRequest.f6140a, 0);
        connectionRequest.f6141b = versionedParcel.E(connectionRequest.f6141b, 1);
        connectionRequest.f6142c = versionedParcel.v(connectionRequest.f6142c, 2);
        connectionRequest.f6143d = versionedParcel.k(connectionRequest.f6143d, 3);
        return connectionRequest;
    }

    public static void write(ConnectionRequest connectionRequest, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.Y(connectionRequest.f6140a, 0);
        versionedParcel.h0(connectionRequest.f6141b, 1);
        versionedParcel.Y(connectionRequest.f6142c, 2);
        versionedParcel.O(connectionRequest.f6143d, 3);
    }
}
