package androidx.camera.view;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Size;
import androidx.camera.core.t1;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l extends t1 {

    /* renamed from: d  reason: collision with root package name */
    static final PointF f3024d = new PointF(2.0f, 2.0f);

    /* renamed from: b  reason: collision with root package name */
    private final f f3025b;

    /* renamed from: c  reason: collision with root package name */
    private Matrix f3026c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(f fVar) {
        this.f3025b = fVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(Size size, int i8) {
        androidx.camera.core.impl.utils.m.a();
        synchronized (this) {
            if (size.getWidth() != 0 && size.getHeight() != 0) {
                this.f3026c = this.f3025b.c(size, i8);
                return;
            }
            this.f3026c = null;
        }
    }
}
