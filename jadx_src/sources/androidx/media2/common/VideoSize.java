package androidx.media2.common;

import y1.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class VideoSize implements b {

    /* renamed from: a  reason: collision with root package name */
    int f6138a;

    /* renamed from: b  reason: collision with root package name */
    int f6139b;

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (obj instanceof VideoSize) {
            VideoSize videoSize = (VideoSize) obj;
            return this.f6138a == videoSize.f6138a && this.f6139b == videoSize.f6139b;
        }
        return false;
    }

    public int hashCode() {
        int i8 = this.f6139b;
        int i9 = this.f6138a;
        return i8 ^ ((i9 >>> 16) | (i9 << 16));
    }

    public String toString() {
        return this.f6138a + "x" + this.f6139b;
    }
}
