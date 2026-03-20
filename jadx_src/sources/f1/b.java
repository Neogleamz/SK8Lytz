package f1;

import androidx.lifecycle.e0;
import androidx.lifecycle.f0;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b implements f0.b {

    /* renamed from: b  reason: collision with root package name */
    private final f<?>[] f19837b;

    public b(f<?>... fVarArr) {
        p.e(fVarArr, "initializers");
        this.f19837b = fVarArr;
    }

    @Override // androidx.lifecycle.f0.b
    public <T extends e0> T b(Class<T> cls, a aVar) {
        f<?>[] fVarArr;
        p.e(cls, "modelClass");
        p.e(aVar, "extras");
        T t8 = null;
        for (f<?> fVar : this.f19837b) {
            if (p.a(fVar.a(), cls)) {
                Object invoke = fVar.b().invoke(aVar);
                t8 = invoke instanceof e0 ? (T) invoke : null;
            }
        }
        if (t8 != null) {
            return t8;
        }
        throw new IllegalArgumentException("No initializer set for given class " + cls.getName());
    }
}
