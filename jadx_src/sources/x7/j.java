package x7;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class j extends f {

    /* renamed from: a  reason: collision with root package name */
    private final f f24207a;

    /* renamed from: b  reason: collision with root package name */
    private final float f24208b;

    public j(f fVar, float f5) {
        this.f24207a = fVar;
        this.f24208b = f5;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // x7.f
    public boolean b() {
        return this.f24207a.b();
    }

    @Override // x7.f
    public void c(float f5, float f8, float f9, o oVar) {
        this.f24207a.c(f5, f8 - this.f24208b, f9, oVar);
    }
}
