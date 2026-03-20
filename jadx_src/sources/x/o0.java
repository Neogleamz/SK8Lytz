package x;

import android.graphics.Matrix;
import android.graphics.Rect;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.e1;
import androidx.camera.core.l1;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class o0 {
    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void n(ImageCaptureException imageCaptureException) {
        boolean z4 = g() != null;
        boolean z8 = i() != null;
        if (z4 && !z8) {
            e1.k g8 = g();
            Objects.requireNonNull(g8);
            g8.b(imageCaptureException);
        } else if (!z8 || z4) {
            throw new IllegalStateException("One and only one callback is allowed.");
        } else {
            e1.l i8 = i();
            Objects.requireNonNull(i8);
            i8.b(imageCaptureException);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void o(e1.n nVar) {
        e1.l i8 = i();
        Objects.requireNonNull(i8);
        Objects.requireNonNull(nVar);
        i8.a(nVar);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void p(l1 l1Var) {
        e1.k g8 = g();
        Objects.requireNonNull(g8);
        Objects.requireNonNull(l1Var);
        g8.a(l1Var);
    }

    abstract Executor d();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int e();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Rect f();

    abstract e1.k g();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int h();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract e1.l i();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract e1.m j();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int k();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract Matrix l();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract List<y.h> m();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(ImageCaptureException imageCaptureException) {
        d().execute(new m0(this, imageCaptureException));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(e1.n nVar) {
        d().execute(new l0(this, nVar));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void s(l1 l1Var) {
        d().execute(new n0(this, l1Var));
    }
}
