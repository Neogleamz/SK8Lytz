package x7;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g extends f {

    /* renamed from: a  reason: collision with root package name */
    private final float f24163a;

    public g(float f5) {
        this.f24163a = f5 - 0.001f;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // x7.f
    public boolean b() {
        return true;
    }

    @Override // x7.f
    public void c(float f5, float f8, float f9, o oVar) {
        float sqrt = (float) ((this.f24163a * Math.sqrt(2.0d)) / 2.0d);
        float sqrt2 = (float) Math.sqrt(Math.pow(this.f24163a, 2.0d) - Math.pow(sqrt, 2.0d));
        oVar.n(f8 - sqrt, ((float) (-((this.f24163a * Math.sqrt(2.0d)) - this.f24163a))) + sqrt2);
        oVar.m(f8, (float) (-((this.f24163a * Math.sqrt(2.0d)) - this.f24163a)));
        oVar.m(f8 + sqrt, ((float) (-((this.f24163a * Math.sqrt(2.0d)) - this.f24163a))) + sqrt2);
    }
}
