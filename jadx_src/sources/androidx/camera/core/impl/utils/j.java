package androidx.camera.core.impl.utils;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class j {

    /* renamed from: a  reason: collision with root package name */
    private final long f2654a;

    /* renamed from: b  reason: collision with root package name */
    private final long f2655b;

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(double d8) {
        this((long) (d8 * 10000.0d), 10000L);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public j(long j8, long j9) {
        this.f2654a = j8;
        this.f2655b = j9;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long a() {
        return this.f2655b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public long b() {
        return this.f2654a;
    }

    public String toString() {
        return this.f2654a + "/" + this.f2655b;
    }
}
