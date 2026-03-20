package com.google.android.material.internal;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.transition.Transition;
import androidx.transition.u;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class k extends Transition {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements ValueAnimator.AnimatorUpdateListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ TextView f18147a;

        a(TextView textView) {
            this.f18147a = textView;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
            this.f18147a.setScaleX(floatValue);
            this.f18147a.setScaleY(floatValue);
        }
    }

    private void o0(u uVar) {
        View view = uVar.f7620b;
        if (view instanceof TextView) {
            uVar.f7619a.put("android:textscale:scale", Float.valueOf(((TextView) view).getScaleX()));
        }
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
        if (uVar == null || uVar2 == null || !(uVar.f7620b instanceof TextView)) {
            return null;
        }
        View view = uVar2.f7620b;
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            Map<String, Object> map = uVar.f7619a;
            Map<String, Object> map2 = uVar2.f7619a;
            float floatValue = map.get("android:textscale:scale") != null ? ((Float) map.get("android:textscale:scale")).floatValue() : 1.0f;
            float floatValue2 = map2.get("android:textscale:scale") != null ? ((Float) map2.get("android:textscale:scale")).floatValue() : 1.0f;
            if (floatValue == floatValue2) {
                return null;
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(floatValue, floatValue2);
            ofFloat.addUpdateListener(new a(textView));
            return ofFloat;
        }
        return null;
    }
}
