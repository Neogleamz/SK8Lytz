package androidx.camera.camera2.internal;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class z1 {

    /* renamed from: a  reason: collision with root package name */
    private final Object f2189a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private final s.y f2190b;

    /* renamed from: c  reason: collision with root package name */
    private int f2191c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public z1(s.y yVar, int i8) {
        this.f2190b = yVar;
        this.f2191c = i8;
    }

    public int a() {
        int i8;
        synchronized (this.f2189a) {
            i8 = this.f2191c;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(int i8) {
        synchronized (this.f2189a) {
            this.f2191c = i8;
        }
    }
}
