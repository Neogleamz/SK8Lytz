package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ChangeClipBounds extends Transition {
    private static final String[] Y = {"android:clipBounds:clip"};

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends AnimatorListenerAdapter {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f7411a;

        a(View view) {
            this.f7411a = view;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            androidx.core.view.c0.A0(this.f7411a, null);
        }
    }

    public ChangeClipBounds() {
    }

    public ChangeClipBounds(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void o0(u uVar) {
        View view = uVar.f7620b;
        if (view.getVisibility() == 8) {
            return;
        }
        Rect w8 = androidx.core.view.c0.w(view);
        uVar.f7619a.put("android:clipBounds:clip", w8);
        if (w8 == null) {
            uVar.f7619a.put("android:clipBounds:bounds", new Rect(0, 0, view.getWidth(), view.getHeight()));
        }
    }

    @Override // androidx.transition.Transition
    public String[] L() {
        return Y;
    }

    @Override // androidx.transition.Transition
    public void j(u uVar) {
        o0(uVar);
    }

    @Override // androidx.transition.Transition
    public void m(u uVar) {
        o0(uVar);
    }

    @Override // androidx.transition.Transition
    public Animator s(ViewGroup viewGroup, u uVar, u uVar2) {
        ObjectAnimator objectAnimator = null;
        if (uVar != null && uVar2 != null && uVar.f7619a.containsKey("android:clipBounds:clip") && uVar2.f7619a.containsKey("android:clipBounds:clip")) {
            Rect rect = (Rect) uVar.f7619a.get("android:clipBounds:clip");
            Rect rect2 = (Rect) uVar2.f7619a.get("android:clipBounds:clip");
            boolean z4 = rect2 == null;
            if (rect == null && rect2 == null) {
                return null;
            }
            if (rect == null) {
                rect = (Rect) uVar.f7619a.get("android:clipBounds:bounds");
            } else if (rect2 == null) {
                rect2 = (Rect) uVar2.f7619a.get("android:clipBounds:bounds");
            }
            if (rect.equals(rect2)) {
                return null;
            }
            androidx.core.view.c0.A0(uVar2.f7620b, rect);
            objectAnimator = ObjectAnimator.ofObject(uVar2.f7620b, (Property<View, V>) f0.f7554c, (TypeEvaluator) new o(new Rect()), (Object[]) new Rect[]{rect, rect2});
            if (z4) {
                objectAnimator.addListener(new a(uVar2.f7620b));
            }
        }
        return objectAnimator;
    }
}
