package androidx.media2.session;

import androidx.media2.session.MediaSession;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class CommandButtonParcelizer {
    public static MediaSession.CommandButton read(VersionedParcel versionedParcel) {
        MediaSession.CommandButton commandButton = new MediaSession.CommandButton();
        commandButton.f6191a = (SessionCommand) versionedParcel.I(commandButton.f6191a, 1);
        commandButton.f6192b = versionedParcel.v(commandButton.f6192b, 2);
        commandButton.f6193c = versionedParcel.o(commandButton.f6193c, 3);
        commandButton.f6194d = versionedParcel.k(commandButton.f6194d, 4);
        commandButton.f6195e = versionedParcel.i(commandButton.f6195e, 5);
        return commandButton;
    }

    public static void write(MediaSession.CommandButton commandButton, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.m0(commandButton.f6191a, 1);
        versionedParcel.Y(commandButton.f6192b, 2);
        versionedParcel.S(commandButton.f6193c, 3);
        versionedParcel.O(commandButton.f6194d, 4);
        versionedParcel.M(commandButton.f6195e, 5);
    }
}
