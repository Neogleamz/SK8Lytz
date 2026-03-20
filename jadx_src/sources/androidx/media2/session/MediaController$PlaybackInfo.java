package androidx.media2.session;

import androidx.media.AudioAttributesCompat;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MediaController$PlaybackInfo implements y1.b {

    /* renamed from: a  reason: collision with root package name */
    int f6178a;

    /* renamed from: b  reason: collision with root package name */
    int f6179b;

    /* renamed from: c  reason: collision with root package name */
    int f6180c;

    /* renamed from: d  reason: collision with root package name */
    int f6181d;

    /* renamed from: e  reason: collision with root package name */
    AudioAttributesCompat f6182e;

    public boolean equals(Object obj) {
        if (obj instanceof MediaController$PlaybackInfo) {
            MediaController$PlaybackInfo mediaController$PlaybackInfo = (MediaController$PlaybackInfo) obj;
            return this.f6178a == mediaController$PlaybackInfo.f6178a && this.f6179b == mediaController$PlaybackInfo.f6179b && this.f6180c == mediaController$PlaybackInfo.f6180c && this.f6181d == mediaController$PlaybackInfo.f6181d && androidx.core.util.c.a(this.f6182e, mediaController$PlaybackInfo.f6182e);
        }
        return false;
    }

    public int hashCode() {
        return androidx.core.util.c.b(Integer.valueOf(this.f6178a), Integer.valueOf(this.f6179b), Integer.valueOf(this.f6180c), Integer.valueOf(this.f6181d), this.f6182e);
    }
}
