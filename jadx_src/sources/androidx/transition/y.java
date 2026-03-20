package androidx.transition;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class y implements z {

    /* renamed from: a  reason: collision with root package name */
    private final ViewGroupOverlay f7635a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public y(ViewGroup viewGroup) {
        this.f7635a = viewGroup.getOverlay();
    }

    @Override // androidx.transition.e0
    public void a(Drawable drawable) {
        this.f7635a.add(drawable);
    }

    @Override // androidx.transition.e0
    public void b(Drawable drawable) {
        this.f7635a.remove(drawable);
    }

    @Override // androidx.transition.z
    public void c(View view) {
        this.f7635a.add(view);
    }

    @Override // androidx.transition.z
    public void d(View view) {
        this.f7635a.remove(view);
    }
}
