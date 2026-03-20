package androidx.media2.session;

import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class StarRatingParcelizer {
    public static StarRating read(VersionedParcel versionedParcel) {
        StarRating starRating = new StarRating();
        starRating.f6232a = versionedParcel.v(starRating.f6232a, 1);
        starRating.f6233b = versionedParcel.s(starRating.f6233b, 2);
        return starRating;
    }

    public static void write(StarRating starRating, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.Y(starRating.f6232a, 1);
        versionedParcel.W(starRating.f6233b, 2);
    }
}
