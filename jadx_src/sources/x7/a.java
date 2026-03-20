package x7;

import android.graphics.RectF;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements c {

    /* renamed from: a  reason: collision with root package name */
    private final float f24159a;

    public a(float f5) {
        this.f24159a = f5;
    }

    @Override // x7.c
    public float a(RectF rectF) {
        return this.f24159a;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof a) && this.f24159a == ((a) obj).f24159a;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Float.valueOf(this.f24159a)});
    }
}
