package androidx.media2.common;

import androidx.core.util.c;
import java.util.Arrays;
import y1.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SubtitleData implements b {

    /* renamed from: a  reason: collision with root package name */
    long f6135a;

    /* renamed from: b  reason: collision with root package name */
    long f6136b;

    /* renamed from: c  reason: collision with root package name */
    byte[] f6137c;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || SubtitleData.class != obj.getClass()) {
            return false;
        }
        SubtitleData subtitleData = (SubtitleData) obj;
        return this.f6135a == subtitleData.f6135a && this.f6136b == subtitleData.f6136b && Arrays.equals(this.f6137c, subtitleData.f6137c);
    }

    public int hashCode() {
        return c.b(Long.valueOf(this.f6135a), Long.valueOf(this.f6136b), Integer.valueOf(Arrays.hashCode(this.f6137c)));
    }
}
