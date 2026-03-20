package androidx.camera.view;

import android.graphics.Bitmap;
import android.util.Size;
import android.view.View;
import android.widget.FrameLayout;
import androidx.camera.core.z2;
import androidx.camera.view.PreviewView;
import java.util.concurrent.Executor;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class k {

    /* renamed from: a  reason: collision with root package name */
    Size f3020a;

    /* renamed from: b  reason: collision with root package name */
    FrameLayout f3021b;

    /* renamed from: c  reason: collision with root package name */
    private final f f3022c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f3023d = false;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface a {
        void a();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public k(FrameLayout frameLayout, f fVar) {
        this.f3021b = frameLayout;
        this.f3022c = fVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Bitmap a() {
        Bitmap c9 = c();
        if (c9 == null) {
            return null;
        }
        return this.f3022c.a(c9, new Size(this.f3021b.getWidth(), this.f3021b.getHeight()), this.f3021b.getLayoutDirection());
    }

    abstract View b();

    abstract Bitmap c();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void d();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void e();

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        this.f3023d = true;
        h();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void g(z2 z2Var, a aVar);

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h() {
        View b9 = b();
        if (b9 == null || !this.f3023d) {
            return;
        }
        this.f3022c.q(new Size(this.f3021b.getWidth(), this.f3021b.getHeight()), this.f3021b.getLayoutDirection(), b9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void i(Executor executor, PreviewView.d dVar);

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract com.google.common.util.concurrent.d<Void> j();
}
