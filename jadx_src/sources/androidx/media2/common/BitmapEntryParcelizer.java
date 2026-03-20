package androidx.media2.common;

import android.graphics.Bitmap;
import androidx.media2.common.MediaMetadata;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class BitmapEntryParcelizer {
    public static MediaMetadata.BitmapEntry read(VersionedParcel versionedParcel) {
        MediaMetadata.BitmapEntry bitmapEntry = new MediaMetadata.BitmapEntry();
        bitmapEntry.f6123a = versionedParcel.E(bitmapEntry.f6123a, 1);
        bitmapEntry.f6124b = (Bitmap) versionedParcel.A(bitmapEntry.f6124b, 2);
        return bitmapEntry;
    }

    public static void write(MediaMetadata.BitmapEntry bitmapEntry, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.h0(bitmapEntry.f6123a, 1);
        versionedParcel.d0(bitmapEntry.f6124b, 2);
    }
}
