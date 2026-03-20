package t;

import android.hardware.camera2.params.OutputConfiguration;
import android.view.Surface;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e extends d {
    /* JADX INFO: Access modifiers changed from: package-private */
    public e(int i8, Surface surface) {
        this(new OutputConfiguration(i8, surface));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(Object obj) {
        super(obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static e j(OutputConfiguration outputConfiguration) {
        return new e(outputConfiguration);
    }

    @Override // t.d, t.c, t.g, t.b.a
    public String c() {
        return null;
    }

    @Override // t.d, t.c, t.g, t.b.a
    public void e(String str) {
        ((OutputConfiguration) f()).setPhysicalCameraId(str);
    }

    @Override // t.d, t.c, t.g, t.b.a
    public Object f() {
        androidx.core.util.h.a(this.f22818a instanceof OutputConfiguration);
        return this.f22818a;
    }
}
