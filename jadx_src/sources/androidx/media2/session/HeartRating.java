package androidx.media2.session;

import androidx.media2.common.Rating;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class HeartRating implements Rating {

    /* renamed from: a  reason: collision with root package name */
    boolean f6169a = false;

    /* renamed from: b  reason: collision with root package name */
    boolean f6170b;

    public boolean equals(Object obj) {
        if (obj instanceof HeartRating) {
            HeartRating heartRating = (HeartRating) obj;
            return this.f6170b == heartRating.f6170b && this.f6169a == heartRating.f6169a;
        }
        return false;
    }

    public int hashCode() {
        return androidx.core.util.c.b(Boolean.valueOf(this.f6169a), Boolean.valueOf(this.f6170b));
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("HeartRating: ");
        if (this.f6169a) {
            str = "hasHeart=" + this.f6170b;
        } else {
            str = "unrated";
        }
        sb.append(str);
        return sb.toString();
    }
}
