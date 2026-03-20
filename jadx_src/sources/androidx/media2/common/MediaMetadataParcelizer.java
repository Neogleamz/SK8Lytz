package androidx.media2.common;

import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MediaMetadataParcelizer {
    public static MediaMetadata read(VersionedParcel versionedParcel) {
        MediaMetadata mediaMetadata = new MediaMetadata();
        mediaMetadata.f6121b = versionedParcel.k(mediaMetadata.f6121b, 1);
        mediaMetadata.f6122c = (ParcelImplListSlice) versionedParcel.A(mediaMetadata.f6122c, 2);
        mediaMetadata.c();
        return mediaMetadata;
    }

    public static void write(MediaMetadata mediaMetadata, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        mediaMetadata.d(versionedParcel.g());
        versionedParcel.O(mediaMetadata.f6121b, 1);
        versionedParcel.d0(mediaMetadata.f6122c, 2);
    }
}
