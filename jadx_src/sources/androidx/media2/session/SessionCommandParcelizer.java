package androidx.media2.session;

import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SessionCommandParcelizer {
    public static SessionCommand read(VersionedParcel versionedParcel) {
        SessionCommand sessionCommand = new SessionCommand();
        sessionCommand.f6208a = versionedParcel.v(sessionCommand.f6208a, 1);
        sessionCommand.f6209b = versionedParcel.E(sessionCommand.f6209b, 2);
        sessionCommand.f6210c = versionedParcel.k(sessionCommand.f6210c, 3);
        return sessionCommand;
    }

    public static void write(SessionCommand sessionCommand, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.Y(sessionCommand.f6208a, 1);
        versionedParcel.h0(sessionCommand.f6209b, 2);
        versionedParcel.O(sessionCommand.f6210c, 3);
    }
}
