package androidx.transition;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewOverlay;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class d0 implements e0 {

    /* renamed from: a  reason: collision with root package name */
    private final ViewOverlay f7549a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d0(View view) {
        this.f7549a = view.getOverlay();
    }

    @Override // androidx.transition.e0
    public void a(Drawable drawable) {
        this.f7549a.add(drawable);
    }

    @Override // androidx.transition.e0
    public void b(Drawable drawable) {
        this.f7549a.remove(drawable);
    }
}
