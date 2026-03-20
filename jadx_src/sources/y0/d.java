package y0;

import y0.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d extends b<d> {
    private e A;
    private float B;
    private boolean C;

    public <K> d(K k8, c<K> cVar) {
        super(k8, cVar);
        this.A = null;
        this.B = Float.MAX_VALUE;
        this.C = false;
    }

    private void o() {
        e eVar = this.A;
        if (eVar == null) {
            throw new UnsupportedOperationException("Incomplete SpringAnimation: Either final position or a spring force needs to be set.");
        }
        double a9 = eVar.a();
        if (a9 > this.f24354g) {
            throw new UnsupportedOperationException("Final position of the spring cannot be greater than the max value.");
        }
        if (a9 < this.f24355h) {
            throw new UnsupportedOperationException("Final position of the spring cannot be less than the min value.");
        }
    }

    @Override // y0.b
    public void j() {
        o();
        this.A.g(e());
        super.j();
    }

    @Override // y0.b
    boolean l(long j8) {
        e eVar;
        double d8;
        double d9;
        long j9;
        if (this.C) {
            float f5 = this.B;
            if (f5 != Float.MAX_VALUE) {
                this.A.e(f5);
                this.B = Float.MAX_VALUE;
            }
            this.f24349b = this.A.a();
            this.f24348a = 0.0f;
            this.C = false;
            return true;
        }
        if (this.B != Float.MAX_VALUE) {
            this.A.a();
            j9 = j8 / 2;
            b.o h8 = this.A.h(this.f24349b, this.f24348a, j9);
            this.A.e(this.B);
            this.B = Float.MAX_VALUE;
            eVar = this.A;
            d8 = h8.f24360a;
            d9 = h8.f24361b;
        } else {
            eVar = this.A;
            d8 = this.f24349b;
            d9 = this.f24348a;
            j9 = j8;
        }
        b.o h9 = eVar.h(d8, d9, j9);
        this.f24349b = h9.f24360a;
        this.f24348a = h9.f24361b;
        float max = Math.max(this.f24349b, this.f24355h);
        this.f24349b = max;
        float min = Math.min(max, this.f24354g);
        this.f24349b = min;
        if (n(min, this.f24348a)) {
            this.f24349b = this.A.a();
            this.f24348a = 0.0f;
            return true;
        }
        return false;
    }

    public void m(float f5) {
        if (f()) {
            this.B = f5;
            return;
        }
        if (this.A == null) {
            this.A = new e(f5);
        }
        this.A.e(f5);
        j();
    }

    boolean n(float f5, float f8) {
        return this.A.c(f5, f8);
    }

    public d p(e eVar) {
        this.A = eVar;
        return this;
    }
}
