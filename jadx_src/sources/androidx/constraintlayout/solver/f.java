package androidx.constraintlayout.solver;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class f<T> implements e<T> {

    /* renamed from: a  reason: collision with root package name */
    private final Object[] f3581a;

    /* renamed from: b  reason: collision with root package name */
    private int f3582b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public f(int i8) {
        if (i8 <= 0) {
            throw new IllegalArgumentException("The max pool size must be > 0");
        }
        this.f3581a = new Object[i8];
    }

    @Override // androidx.constraintlayout.solver.e
    public boolean a(T t8) {
        int i8 = this.f3582b;
        Object[] objArr = this.f3581a;
        if (i8 < objArr.length) {
            objArr[i8] = t8;
            this.f3582b = i8 + 1;
            return true;
        }
        return false;
    }

    @Override // androidx.constraintlayout.solver.e
    public T b() {
        int i8 = this.f3582b;
        if (i8 > 0) {
            int i9 = i8 - 1;
            Object[] objArr = this.f3581a;
            T t8 = (T) objArr[i9];
            objArr[i9] = null;
            this.f3582b = i8 - 1;
            return t8;
        }
        return null;
    }

    @Override // androidx.constraintlayout.solver.e
    public void c(T[] tArr, int i8) {
        if (i8 > tArr.length) {
            i8 = tArr.length;
        }
        for (int i9 = 0; i9 < i8; i9++) {
            T t8 = tArr[i9];
            int i10 = this.f3582b;
            Object[] objArr = this.f3581a;
            if (i10 < objArr.length) {
                objArr[i10] = t8;
                this.f3582b = i10 + 1;
            }
        }
    }
}
