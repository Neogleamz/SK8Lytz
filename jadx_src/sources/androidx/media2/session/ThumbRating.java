package androidx.media2.session;

import androidx.media2.common.Rating;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ThumbRating implements Rating {

    /* renamed from: a  reason: collision with root package name */
    boolean f6234a = false;

    /* renamed from: b  reason: collision with root package name */
    boolean f6235b;

    public boolean equals(Object obj) {
        if (obj instanceof ThumbRating) {
            ThumbRating thumbRating = (ThumbRating) obj;
            return this.f6235b == thumbRating.f6235b && this.f6234a == thumbRating.f6234a;
        }
        return false;
    }

    public int hashCode() {
        return androidx.core.util.c.b(Boolean.valueOf(this.f6234a), Boolean.valueOf(this.f6235b));
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("ThumbRating: ");
        if (this.f6234a) {
            str = "isThumbUp=" + this.f6235b;
        } else {
            str = "unrated";
        }
        sb.append(str);
        return sb.toString();
    }
}
