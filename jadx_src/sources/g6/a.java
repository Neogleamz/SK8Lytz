package g6;

import com.google.android.gms.common.util.VisibleForTesting;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {
    @VisibleForTesting

    /* renamed from: b  reason: collision with root package name */
    static int f20198b = 31;

    /* renamed from: a  reason: collision with root package name */
    private int f20199a = 1;

    public a a(Object obj) {
        this.f20199a = (f20198b * this.f20199a) + (obj == null ? 0 : obj.hashCode());
        return this;
    }

    public int b() {
        return this.f20199a;
    }

    public final a c(boolean z4) {
        this.f20199a = (f20198b * this.f20199a) + (z4 ? 1 : 0);
        return this;
    }
}
