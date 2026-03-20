package androidx.media2.common;

import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class VideoSizeParcelizer {
    public static VideoSize read(VersionedParcel versionedParcel) {
        VideoSize videoSize = new VideoSize();
        videoSize.f6138a = versionedParcel.v(videoSize.f6138a, 1);
        videoSize.f6139b = versionedParcel.v(videoSize.f6139b, 2);
        return videoSize;
    }

    public static void write(VideoSize videoSize, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.Y(videoSize.f6138a, 1);
        versionedParcel.Y(videoSize.f6139b, 2);
    }
}
