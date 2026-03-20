package q2;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements b {

    /* renamed from: a  reason: collision with root package name */
    private final float f22524a;

    public a() {
        this(0.0f);
    }

    public a(float f5) {
        this.f22524a = f5;
    }

    @Override // q2.b
    public Animator[] a(View view) {
        return new Animator[]{ObjectAnimator.ofFloat(view, "alpha", this.f22524a, 1.0f)};
    }
}
