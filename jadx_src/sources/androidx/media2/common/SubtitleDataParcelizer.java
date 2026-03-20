package androidx.media2.common;

import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SubtitleDataParcelizer {
    public static SubtitleData read(VersionedParcel versionedParcel) {
        SubtitleData subtitleData = new SubtitleData();
        subtitleData.f6135a = versionedParcel.y(subtitleData.f6135a, 1);
        subtitleData.f6136b = versionedParcel.y(subtitleData.f6136b, 2);
        subtitleData.f6137c = versionedParcel.m(subtitleData.f6137c, 3);
        return subtitleData;
    }

    public static void write(SubtitleData subtitleData, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.b0(subtitleData.f6135a, 1);
        versionedParcel.b0(subtitleData.f6136b, 2);
        versionedParcel.Q(subtitleData.f6137c, 3);
    }
}
