package androidx.media2.session;

import androidx.media2.common.Rating;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class PercentageRating implements Rating {

    /* renamed from: a  reason: collision with root package name */
    float f6202a = -1.0f;

    public boolean c() {
        return this.f6202a != -1.0f;
    }

    public boolean equals(Object obj) {
        return (obj instanceof PercentageRating) && this.f6202a == ((PercentageRating) obj).f6202a;
    }

    public int hashCode() {
        return androidx.core.util.c.b(Float.valueOf(this.f6202a));
    }

    public String toString() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("PercentageRating: ");
        if (c()) {
            str = "percentage=" + this.f6202a;
        } else {
            str = "unrated";
        }
        sb.append(str);
        return sb.toString();
    }
}
