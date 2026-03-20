package androidx.core.util;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f<T> implements e<T> {

    /* renamed from: a  reason: collision with root package name */
    private final Object[] f4891a;

    /* renamed from: b  reason: collision with root package name */
    private int f4892b;

    public f(int i8) {
        if (i8 <= 0) {
            throw new IllegalArgumentException("The max pool size must be > 0");
        }
        this.f4891a = new Object[i8];
    }

    private boolean c(T t8) {
        for (int i8 = 0; i8 < this.f4892b; i8++) {
            if (this.f4891a[i8] == t8) {
                return true;
            }
        }
        return false;
    }

    @Override // androidx.core.util.e
    public boolean a(T t8) {
        if (c(t8)) {
            throw new IllegalStateException("Already in the pool!");
        }
        int i8 = this.f4892b;
        Object[] objArr = this.f4891a;
        if (i8 < objArr.length) {
            objArr[i8] = t8;
            this.f4892b = i8 + 1;
            return true;
        }
        return false;
    }

    @Override // androidx.core.util.e
    public T b() {
        int i8 = this.f4892b;
        if (i8 > 0) {
            int i9 = i8 - 1;
            Object[] objArr = this.f4891a;
            T t8 = (T) objArr[i9];
            objArr[i9] = null;
            this.f4892b = i8 - 1;
            return t8;
        }
        return null;
    }
}
