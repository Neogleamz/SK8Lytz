package androidx.media2.session;

import androidx.media2.common.MediaItem;
import androidx.media2.common.ParcelImplListSlice;
import androidx.media2.session.MediaLibraryService;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class LibraryResultParcelizer {
    public static LibraryResult read(VersionedParcel versionedParcel) {
        LibraryResult libraryResult = new LibraryResult();
        libraryResult.f6171a = versionedParcel.v(libraryResult.f6171a, 1);
        libraryResult.f6172b = versionedParcel.y(libraryResult.f6172b, 2);
        libraryResult.f6174d = (MediaItem) versionedParcel.I(libraryResult.f6174d, 3);
        libraryResult.f6175e = (MediaLibraryService.LibraryParams) versionedParcel.I(libraryResult.f6175e, 4);
        libraryResult.f6177g = (ParcelImplListSlice) versionedParcel.A(libraryResult.f6177g, 5);
        libraryResult.c();
        return libraryResult;
    }

    public static void write(LibraryResult libraryResult, VersionedParcel versionedParcel) {
        versionedParcel.K(false, false);
        libraryResult.d(versionedParcel.g());
        versionedParcel.Y(libraryResult.f6171a, 1);
        versionedParcel.b0(libraryResult.f6172b, 2);
        versionedParcel.m0(libraryResult.f6174d, 3);
        versionedParcel.m0(libraryResult.f6175e, 4);
        versionedParcel.d0(libraryResult.f6177g, 5);
    }
}
