package p5;

import b6.a;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class l extends l4.f implements h {

    /* renamed from: d  reason: collision with root package name */
    private h f22424d;

    /* renamed from: e  reason: collision with root package name */
    private long f22425e;

    @Override // p5.h
    public int c(long j8) {
        return ((h) a.e(this.f22424d)).c(j8 - this.f22425e);
    }

    @Override // p5.h
    public long f(int i8) {
        return ((h) a.e(this.f22424d)).f(i8) + this.f22425e;
    }

    @Override // p5.h
    public List<b> h(long j8) {
        return ((h) a.e(this.f22424d)).h(j8 - this.f22425e);
    }

    @Override // p5.h
    public int i() {
        return ((h) a.e(this.f22424d)).i();
    }

    @Override // l4.a
    public void k() {
        super.k();
        this.f22424d = null;
    }

    public void z(long j8, h hVar, long j9) {
        this.f21597b = j8;
        this.f22424d = hVar;
        if (j9 != Long.MAX_VALUE) {
            j8 = j9;
        }
        this.f22425e = j8;
    }
}
