package androidx.core.view;

import android.view.View;
import android.view.ViewTreeObserver;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class y implements ViewTreeObserver.OnPreDrawListener, View.OnAttachStateChangeListener {

    /* renamed from: a  reason: collision with root package name */
    private final View f5083a;

    /* renamed from: b  reason: collision with root package name */
    private ViewTreeObserver f5084b;

    /* renamed from: c  reason: collision with root package name */
    private final Runnable f5085c;

    private y(View view, Runnable runnable) {
        this.f5083a = view;
        this.f5084b = view.getViewTreeObserver();
        this.f5085c = runnable;
    }

    public static y a(View view, Runnable runnable) {
        Objects.requireNonNull(view, "view == null");
        Objects.requireNonNull(runnable, "runnable == null");
        y yVar = new y(view, runnable);
        view.getViewTreeObserver().addOnPreDrawListener(yVar);
        view.addOnAttachStateChangeListener(yVar);
        return yVar;
    }

    public void b() {
        (this.f5084b.isAlive() ? this.f5084b : this.f5083a.getViewTreeObserver()).removeOnPreDrawListener(this);
        this.f5083a.removeOnAttachStateChangeListener(this);
    }

    @Override // android.view.ViewTreeObserver.OnPreDrawListener
    public boolean onPreDraw() {
        b();
        this.f5085c.run();
        return true;
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewAttachedToWindow(View view) {
        this.f5084b = view.getViewTreeObserver();
    }

    @Override // android.view.View.OnAttachStateChangeListener
    public void onViewDetachedFromWindow(View view) {
        b();
    }
}
