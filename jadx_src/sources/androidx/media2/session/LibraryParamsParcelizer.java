package androidx.media2.session;

import androidx.media2.session.MediaLibraryService;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class LibraryParamsParcelizer {
    public static MediaLibraryService.LibraryParams read(VersionedParcel versionedParcel) {
        MediaLibraryService.LibraryParams libraryParams = new MediaLibraryService.LibraryParams();
        libraryParams.f6184a = versionedParcel.k(libraryParams.f6184a, 1);
        libraryParams.f6185b = versionedParcel.v(libraryParams.f6185b, 2);
        libraryParams.f6186c = versionedParcel.v(libraryParams.f6186c, 3);
        libraryParams.f6187d = versionedParcel.v(libraryParams.f6187d, 4);
        return libraryParams;
    }

    public static void write(MediaLibraryService.LibraryParams libraryParams, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        versionedParcel.O(libraryParams.f6184a, 1);
        versionedParcel.Y(libraryParams.f6185b, 2);
        versionedParcel.Y(libraryParams.f6186c, 3);
        versionedParcel.Y(libraryParams.f6187d, 4);
    }
}
