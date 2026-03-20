package t;

import android.hardware.camera2.params.OutputConfiguration;
import android.view.Surface;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class f extends e {
    /* JADX INFO: Access modifiers changed from: package-private */
    public f(int i8, Surface surface) {
        this(new OutputConfiguration(i8, surface));
    }

    f(Object obj) {
        super(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static f k(OutputConfiguration outputConfiguration) {
        return new f(outputConfiguration);
    }

    @Override // t.g, t.b.a
    public void a(long j8) {
        if (j8 == -1) {
            return;
        }
        ((OutputConfiguration) f()).setStreamUseCase(j8);
    }

    @Override // t.d, t.g, t.b.a
    public /* bridge */ /* synthetic */ void b(Surface surface) {
        super.b(surface);
    }

    @Override // t.e, t.d, t.c, t.g, t.b.a
    public /* bridge */ /* synthetic */ String c() {
        return super.c();
    }

    @Override // t.d, t.c, t.g, t.b.a
    public /* bridge */ /* synthetic */ void d() {
        super.d();
    }

    @Override // t.e, t.d, t.c, t.g, t.b.a
    public /* bridge */ /* synthetic */ void e(String str) {
        super.e(str);
    }

    @Override // t.g
    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override // t.e, t.d, t.c, t.g, t.b.a
    public /* bridge */ /* synthetic */ Object f() {
        return super.f();
    }

    @Override // t.c, t.g, t.b.a
    public /* bridge */ /* synthetic */ Surface getSurface() {
        return super.getSurface();
    }

    @Override // t.g
    public /* bridge */ /* synthetic */ int hashCode() {
        return super.hashCode();
    }
}
