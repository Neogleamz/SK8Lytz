package androidx.media2.common;

import androidx.core.util.d;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MediaItem extends CustomVersionedParcelable {

    /* renamed from: a  reason: collision with root package name */
    private final Object f6111a;

    /* renamed from: b  reason: collision with root package name */
    MediaMetadata f6112b;

    /* renamed from: c  reason: collision with root package name */
    long f6113c;

    /* renamed from: d  reason: collision with root package name */
    long f6114d;

    /* renamed from: e  reason: collision with root package name */
    private final List<d<Object, Executor>> f6115e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        MediaMetadata f6116a;

        /* renamed from: b  reason: collision with root package name */
        long f6117b = 0;

        /* renamed from: c  reason: collision with root package name */
        long f6118c = 576460752303423487L;

        public MediaItem a() {
            return new MediaItem(this);
        }

        public a b(long j8) {
            if (j8 < 0) {
                j8 = 576460752303423487L;
            }
            this.f6118c = j8;
            return this;
        }

        public a c(MediaMetadata mediaMetadata) {
            this.f6116a = mediaMetadata;
            return this;
        }

        public a d(long j8) {
            if (j8 < 0) {
                j8 = 0;
            }
            this.f6117b = j8;
            return this;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MediaItem() {
        this.f6111a = new Object();
        this.f6113c = 0L;
        this.f6114d = 576460752303423487L;
        this.f6115e = new ArrayList();
    }

    MediaItem(a aVar) {
        this(aVar.f6116a, aVar.f6117b, aVar.f6118c);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public MediaItem(MediaItem mediaItem) {
        this(mediaItem.f6112b, mediaItem.f6113c, mediaItem.f6114d);
    }

    MediaItem(MediaMetadata mediaMetadata, long j8, long j9) {
        this.f6111a = new Object();
        this.f6113c = 0L;
        this.f6114d = 576460752303423487L;
        this.f6115e = new ArrayList();
        if (j8 > j9) {
            throw new IllegalStateException("Illegal start/end position: " + j8 + " : " + j9);
        }
        if (mediaMetadata != null && mediaMetadata.e("android.media.metadata.DURATION")) {
            long f5 = mediaMetadata.f("android.media.metadata.DURATION");
            if (f5 != Long.MIN_VALUE && j9 != 576460752303423487L && j9 > f5) {
                throw new IllegalStateException("endPositionMs shouldn't be greater than duration in the metdata, endPositionMs=" + j9 + ", durationMs=" + f5);
            }
        }
        this.f6112b = mediaMetadata;
        this.f6113c = j8;
        this.f6114d = j9;
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void d(boolean z4) {
        if (getClass() != MediaItem.class) {
            throw new RuntimeException("MediaItem's subclasses shouldn't be parcelized.");
        }
        super.d(z4);
    }

    public long e() {
        return this.f6114d;
    }

    public String f() {
        String g8;
        synchronized (this.f6111a) {
            MediaMetadata mediaMetadata = this.f6112b;
            g8 = mediaMetadata != null ? mediaMetadata.g("android.media.metadata.MEDIA_ID") : null;
        }
        return g8;
    }

    public MediaMetadata g() {
        MediaMetadata mediaMetadata;
        synchronized (this.f6111a) {
            mediaMetadata = this.f6112b;
        }
        return mediaMetadata;
    }

    public long h() {
        return this.f6113c;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        synchronized (this.f6111a) {
            sb.append("{Media Id=");
            sb.append(f());
            sb.append(", mMetadata=");
            sb.append(this.f6112b);
            sb.append(", mStartPositionMs=");
            sb.append(this.f6113c);
            sb.append(", mEndPositionMs=");
            sb.append(this.f6114d);
            sb.append('}');
        }
        return sb.toString();
    }
}
