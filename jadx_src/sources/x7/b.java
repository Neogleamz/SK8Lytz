package x7;

import android.graphics.RectF;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements c {

    /* renamed from: a  reason: collision with root package name */
    private final c f24160a;

    /* renamed from: b  reason: collision with root package name */
    private final float f24161b;

    public b(float f5, c cVar) {
        while (cVar instanceof b) {
            cVar = ((b) cVar).f24160a;
            f5 += ((b) cVar).f24161b;
        }
        this.f24160a = cVar;
        this.f24161b = f5;
    }

    @Override // x7.c
    public float a(RectF rectF) {
        return Math.max(0.0f, this.f24160a.a(rectF) + this.f24161b);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof b) {
            b bVar = (b) obj;
            return this.f24160a.equals(bVar.f24160a) && this.f24161b == bVar.f24161b;
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this.f24160a, Float.valueOf(this.f24161b)});
    }
}
