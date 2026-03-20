package androidx.media2.session;

import android.app.PendingIntent;
import android.os.Bundle;
import android.os.IBinder;
import androidx.media2.common.MediaItem;
import androidx.media2.common.MediaMetadata;
import androidx.media2.common.ParcelImplListSlice;
import androidx.media2.common.SessionPlayer;
import androidx.media2.common.VideoSize;
import androidx.media2.session.b;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class ConnectionResult extends CustomVersionedParcelable {
    MediaMetadata A;
    int B;

    /* renamed from: a  reason: collision with root package name */
    int f6144a;

    /* renamed from: b  reason: collision with root package name */
    b f6145b;

    /* renamed from: c  reason: collision with root package name */
    IBinder f6146c;

    /* renamed from: d  reason: collision with root package name */
    PendingIntent f6147d;

    /* renamed from: e  reason: collision with root package name */
    int f6148e;

    /* renamed from: f  reason: collision with root package name */
    MediaItem f6149f;

    /* renamed from: g  reason: collision with root package name */
    MediaItem f6150g;

    /* renamed from: h  reason: collision with root package name */
    long f6151h;

    /* renamed from: i  reason: collision with root package name */
    long f6152i;

    /* renamed from: j  reason: collision with root package name */
    float f6153j;

    /* renamed from: k  reason: collision with root package name */
    long f6154k;

    /* renamed from: l  reason: collision with root package name */
    MediaController$PlaybackInfo f6155l;

    /* renamed from: m  reason: collision with root package name */
    int f6156m;

    /* renamed from: n  reason: collision with root package name */
    int f6157n;

    /* renamed from: o  reason: collision with root package name */
    ParcelImplListSlice f6158o;

    /* renamed from: p  reason: collision with root package name */
    SessionCommandGroup f6159p;
    int q;

    /* renamed from: r  reason: collision with root package name */
    int f6160r;

    /* renamed from: s  reason: collision with root package name */
    int f6161s;

    /* renamed from: t  reason: collision with root package name */
    Bundle f6162t;

    /* renamed from: u  reason: collision with root package name */
    VideoSize f6163u;

    /* renamed from: v  reason: collision with root package name */
    List<SessionPlayer.TrackInfo> f6164v;

    /* renamed from: w  reason: collision with root package name */
    SessionPlayer.TrackInfo f6165w;

    /* renamed from: x  reason: collision with root package name */
    SessionPlayer.TrackInfo f6166x;

    /* renamed from: y  reason: collision with root package name */
    SessionPlayer.TrackInfo f6167y;

    /* renamed from: z  reason: collision with root package name */
    SessionPlayer.TrackInfo f6168z;

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void c() {
        this.f6145b = b.a.d(this.f6146c);
        this.f6149f = this.f6150g;
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void d(boolean z4) {
        synchronized (this.f6145b) {
            if (this.f6146c == null) {
                this.f6146c = (IBinder) this.f6145b;
                this.f6150g = h.d(this.f6149f);
            }
        }
    }
}
