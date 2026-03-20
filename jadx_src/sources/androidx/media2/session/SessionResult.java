package androidx.media2.session;

import android.os.Bundle;
import android.os.SystemClock;
import androidx.media2.common.MediaItem;
import androidx.versionedparcelable.CustomVersionedParcelable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SessionResult extends CustomVersionedParcelable {

    /* renamed from: a  reason: collision with root package name */
    int f6212a;

    /* renamed from: b  reason: collision with root package name */
    long f6213b;

    /* renamed from: c  reason: collision with root package name */
    Bundle f6214c;

    /* renamed from: d  reason: collision with root package name */
    MediaItem f6215d;

    /* renamed from: e  reason: collision with root package name */
    MediaItem f6216e;

    /* JADX INFO: Access modifiers changed from: package-private */
    public SessionResult() {
    }

    public SessionResult(int i8, Bundle bundle) {
        this(i8, bundle, null, SystemClock.elapsedRealtime());
    }

    SessionResult(int i8, Bundle bundle, MediaItem mediaItem, long j8) {
        this.f6212a = i8;
        this.f6214c = bundle;
        this.f6215d = mediaItem;
        this.f6213b = j8;
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void c() {
        this.f6215d = this.f6216e;
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void d(boolean z4) {
        MediaItem mediaItem = this.f6215d;
        if (mediaItem != null) {
            synchronized (mediaItem) {
                if (this.f6216e == null) {
                    this.f6216e = h.d(this.f6215d);
                }
            }
        }
    }
}
