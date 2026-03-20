package androidx.media2.session;

import androidx.media.AudioAttributesCompat;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class PlaybackInfoParcelizer {
    public static MediaController$PlaybackInfo read(VersionedParcel versionedParcel) {
        MediaController$PlaybackInfo mediaController$PlaybackInfo = new MediaController$PlaybackInfo();
        mediaController$PlaybackInfo.f6178a = versionedParcel.v(mediaController$PlaybackInfo.f6178a, 1);
        mediaController$PlaybackInfo.f6179b = versionedParcel.v(mediaController$PlaybackInfo.f6179b, 2);
        mediaController$PlaybackInfo.f6180c = versionedParcel.v(mediaController$PlaybackInfo.f6180c, 3);
        mediaController$PlaybackInfo.f6181d = versionedParcel.v(mediaController$PlaybackInfo.f6181d, 4);
        mediaController$PlaybackInfo.f6182e = (AudioAttributesCompat) versionedParcel.I(mediaController$PlaybackInfo.f6182e, 5);
        return mediaController$PlaybackInfo;
    }

    public static void write(MediaController$PlaybackInfo mediaController$PlaybackInfo, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.Y(mediaController$PlaybackInfo.f6178a, 1);
        versionedParcel.Y(mediaController$PlaybackInfo.f6179b, 2);
        versionedParcel.Y(mediaController$PlaybackInfo.f6180c, 3);
        versionedParcel.Y(mediaController$PlaybackInfo.f6181d, 4);
        versionedParcel.m0(mediaController$PlaybackInfo.f6182e, 5);
    }
}
