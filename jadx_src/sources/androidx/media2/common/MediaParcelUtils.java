package androidx.media2.common;

import android.annotation.SuppressLint;
import androidx.versionedparcelable.ParcelImpl;
import y1.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MediaParcelUtils {

    /* JADX INFO: Access modifiers changed from: private */
    @SuppressLint({"RestrictedApi"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class MediaItemParcelImpl extends ParcelImpl {

        /* renamed from: b  reason: collision with root package name */
        private final MediaItem f6125b;

        MediaItemParcelImpl(MediaItem mediaItem) {
            super(new MediaItem(mediaItem));
            this.f6125b = mediaItem;
        }

        @Override // androidx.versionedparcelable.ParcelImpl
        /* renamed from: b */
        public MediaItem a() {
            return this.f6125b;
        }
    }

    public static <T extends b> T a(ParcelImpl parcelImpl) {
        return (T) y1.a.a(parcelImpl);
    }

    public static ParcelImpl b(b bVar) {
        return bVar instanceof MediaItem ? new MediaItemParcelImpl((MediaItem) bVar) : (ParcelImpl) y1.a.d(bVar);
    }
}
