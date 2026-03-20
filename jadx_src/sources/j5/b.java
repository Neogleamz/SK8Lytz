package j5;

import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b implements o {

    /* renamed from: b  reason: collision with root package name */
    private final long f20718b;

    /* renamed from: c  reason: collision with root package name */
    private final long f20719c;

    /* renamed from: d  reason: collision with root package name */
    private long f20720d;

    public b(long j8, long j9) {
        this.f20718b = j8;
        this.f20719c = j9;
        f();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void c() {
        long j8 = this.f20720d;
        if (j8 < this.f20718b || j8 > this.f20719c) {
            throw new NoSuchElementException();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final long d() {
        return this.f20720d;
    }

    public boolean e() {
        return this.f20720d > this.f20719c;
    }

    public void f() {
        this.f20720d = this.f20718b - 1;
    }

    @Override // j5.o
    public boolean next() {
        this.f20720d++;
        return !e();
    }
}
