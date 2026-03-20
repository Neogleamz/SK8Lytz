package androidx.media2.session;

import androidx.media2.common.MediaItem;
import androidx.media2.common.ParcelImplListSlice;
import androidx.media2.session.MediaLibraryService;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class LibraryResult extends CustomVersionedParcelable {

    /* renamed from: a  reason: collision with root package name */
    int f6171a;

    /* renamed from: b  reason: collision with root package name */
    long f6172b;

    /* renamed from: c  reason: collision with root package name */
    MediaItem f6173c;

    /* renamed from: d  reason: collision with root package name */
    MediaItem f6174d;

    /* renamed from: e  reason: collision with root package name */
    MediaLibraryService.LibraryParams f6175e;

    /* renamed from: f  reason: collision with root package name */
    List<MediaItem> f6176f;

    /* renamed from: g  reason: collision with root package name */
    ParcelImplListSlice f6177g;

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void c() {
        this.f6173c = this.f6174d;
        this.f6176f = h.b(this.f6177g);
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void d(boolean z4) {
        MediaItem mediaItem = this.f6173c;
        if (mediaItem != null) {
            synchronized (mediaItem) {
                if (this.f6174d == null) {
                    this.f6174d = h.d(this.f6173c);
                }
            }
        }
        List<MediaItem> list = this.f6176f;
        if (list != null) {
            synchronized (list) {
                if (this.f6177g == null) {
                    this.f6177g = h.a(this.f6176f);
                }
            }
        }
    }
}
