package x7;

import android.graphics.RectF;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class k implements c {

    /* renamed from: a  reason: collision with root package name */
    private final float f24209a;

    public k(float f5) {
        this.f24209a = f5;
    }

    @Override // x7.c
    public float a(RectF rectF) {
        return this.f24209a * rectF.height();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof k) && this.f24209a == ((k) obj).f24209a;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Float.valueOf(this.f24209a)});
    }
}
