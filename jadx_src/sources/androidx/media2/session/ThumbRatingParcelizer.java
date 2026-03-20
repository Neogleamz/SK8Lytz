package androidx.media2.session;

import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ThumbRatingParcelizer {
    public static ThumbRating read(VersionedParcel versionedParcel) {
        ThumbRating thumbRating = new ThumbRating();
        thumbRating.f6234a = versionedParcel.i(thumbRating.f6234a, 1);
        thumbRating.f6235b = versionedParcel.i(thumbRating.f6235b, 2);
        return thumbRating;
    }

    public static void write(ThumbRating thumbRating, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.M(thumbRating.f6234a, 1);
        versionedParcel.M(thumbRating.f6235b, 2);
    }
}
