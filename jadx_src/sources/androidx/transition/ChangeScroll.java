package androidx.transition;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ChangeScroll extends Transition {
    private static final String[] Y = {"android:changeScroll:x", "android:changeScroll:y"};

    public ChangeScroll() {
    }

    public ChangeScroll(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    private void o0(u uVar) {
        uVar.f7619a.put("android:changeScroll:x", Integer.valueOf(uVar.f7620b.getScrollX()));
        uVar.f7619a.put("android:changeScroll:y", Integer.valueOf(uVar.f7620b.getScrollY()));
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
        ObjectAnimator objectAnimator;
        ObjectAnimator objectAnimator2 = null;
        if (uVar == null || uVar2 == null) {
            return null;
        }
        View view = uVar2.f7620b;
        int intValue = ((Integer) uVar.f7619a.get("android:changeScroll:x")).intValue();
        int intValue2 = ((Integer) uVar2.f7619a.get("android:changeScroll:x")).intValue();
        int intValue3 = ((Integer) uVar.f7619a.get("android:changeScroll:y")).intValue();
        int intValue4 = ((Integer) uVar2.f7619a.get("android:changeScroll:y")).intValue();
        if (intValue != intValue2) {
            view.setScrollX(intValue);
            objectAnimator = ObjectAnimator.ofInt(view, "scrollX", intValue, intValue2);
        } else {
            objectAnimator = null;
        }
        if (intValue3 != intValue4) {
            view.setScrollY(intValue3);
            objectAnimator2 = ObjectAnimator.ofInt(view, "scrollY", intValue3, intValue4);
        }
        return t.c(objectAnimator, objectAnimator2);
    }
}
