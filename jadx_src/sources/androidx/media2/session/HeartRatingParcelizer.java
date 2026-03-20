package androidx.media2.session;

import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class HeartRatingParcelizer {
    public static HeartRating read(VersionedParcel versionedParcel) {
        HeartRating heartRating = new HeartRating();
        heartRating.f6169a = versionedParcel.i(heartRating.f6169a, 1);
        heartRating.f6170b = versionedParcel.i(heartRating.f6170b, 2);
        return heartRating;
    }

    public static void write(HeartRating heartRating, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.M(heartRating.f6169a, 1);
        versionedParcel.M(heartRating.f6170b, 2);
    }
}
