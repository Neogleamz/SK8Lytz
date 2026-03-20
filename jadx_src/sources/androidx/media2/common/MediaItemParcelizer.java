package androidx.media2.common;

import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MediaItemParcelizer {
    public static MediaItem read(VersionedParcel versionedParcel) {
        MediaItem mediaItem = new MediaItem();
        mediaItem.f6112b = (MediaMetadata) versionedParcel.I(mediaItem.f6112b, 1);
        mediaItem.f6113c = versionedParcel.y(mediaItem.f6113c, 2);
        mediaItem.f6114d = versionedParcel.y(mediaItem.f6114d, 3);
        mediaItem.c();
        return mediaItem;
    }

    public static void write(MediaItem mediaItem, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        mediaItem.d(versionedParcel.g());
        versionedParcel.m0(mediaItem.f6112b, 1);
        versionedParcel.b0(mediaItem.f6113c, 2);
        versionedParcel.b0(mediaItem.f6114d, 3);
    }
}
