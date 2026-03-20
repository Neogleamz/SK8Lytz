package androidx.media2.session;

import androidx.media2.common.Rating;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class StarRating implements Rating {

    /* renamed from: a  reason: collision with root package name */
    int f6232a;

    /* renamed from: b  reason: collision with root package name */
    float f6233b;

    public boolean c() {
        return this.f6233b >= 0.0f;
    }

    public boolean equals(Object obj) {
        if (obj instanceof StarRating) {
            StarRating starRating = (StarRating) obj;
            return this.f6232a == starRating.f6232a && this.f6233b == starRating.f6233b;
        }
        return false;
    }

    public int hashCode() {
        return androidx.core.util.c.b(Integer.valueOf(this.f6232a), Float.valueOf(this.f6233b));
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("StarRating: maxStars=");
        sb.append(this.f6232a);
        if (c()) {
            str = ", starRating=" + this.f6233b;
        } else {
            str = ", unrated";
        }
        sb.append(str);
        return sb.toString();
    }
}
