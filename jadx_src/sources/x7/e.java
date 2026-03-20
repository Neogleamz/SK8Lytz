package x7;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e extends d {

    /* renamed from: a  reason: collision with root package name */
    float f24162a = -1.0f;

    @Override // x7.d
    public void a(o oVar, float f5, float f8, float f9) {
        oVar.o(0.0f, f9 * f8, 180.0f, 180.0f - f5);
        double d8 = f9;
        double d9 = f8;
        oVar.m((float) (Math.sin(Math.toRadians(f5)) * d8 * d9), (float) (Math.sin(Math.toRadians(90.0f - f5)) * d8 * d9));
    }
}
