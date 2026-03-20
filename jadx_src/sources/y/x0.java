package y;

import androidx.camera.core.i1;
import androidx.camera.core.l1;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class x0 implements f0 {

    /* renamed from: a  reason: collision with root package name */
    private final int f24316a;

    /* renamed from: b  reason: collision with root package name */
    private final l1 f24317b;

    public x0(l1 l1Var, String str) {
        i1 e12 = l1Var.e1();
        if (e12 == null) {
            throw new IllegalArgumentException("ImageProxy has no associated ImageInfo");
        }
        Integer num = (Integer) e12.a().c(str);
        if (num == null) {
            throw new IllegalArgumentException("ImageProxy has no associated tag");
        }
        this.f24316a = num.intValue();
        this.f24317b = l1Var;
    }

    @Override // y.f0
    public com.google.common.util.concurrent.d<l1> a(int i8) {
        return i8 != this.f24316a ? a0.f.f(new IllegalArgumentException("Capture id does not exist in the bundle")) : a0.f.h(this.f24317b);
    }

    @Override // y.f0
    public List<Integer> b() {
        return Collections.singletonList(Integer.valueOf(this.f24316a));
    }

    public void c() {
        this.f24317b.close();
    }
}
