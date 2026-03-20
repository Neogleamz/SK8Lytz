package com.google.android.material.appbar;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import com.google.android.material.internal.m;
import k7.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b {

    /* renamed from: a  reason: collision with root package name */
    private static final int[] f17421a = {16843848};

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(View view) {
        view.setOutlineProvider(ViewOutlineProvider.BOUNDS);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(View view, float f5) {
        int integer = view.getResources().getInteger(g.f21177a);
        StateListAnimator stateListAnimator = new StateListAnimator();
        long j8 = integer;
        stateListAnimator.addState(new int[]{16842766, k7.b.U, -k7.b.V}, ObjectAnimator.ofFloat(view, "elevation", 0.0f).setDuration(j8));
        stateListAnimator.addState(new int[]{16842766}, ObjectAnimator.ofFloat(view, "elevation", f5).setDuration(j8));
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(view, "elevation", 0.0f).setDuration(0L));
        view.setStateListAnimator(stateListAnimator);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(View view, AttributeSet attributeSet, int i8, int i9) {
        Context context = view.getContext();
        TypedArray h8 = m.h(context, attributeSet, f17421a, i8, i9, new int[0]);
        try {
            if (h8.hasValue(0)) {
                view.setStateListAnimator(AnimatorInflater.loadStateListAnimator(context, h8.getResourceId(0, 0)));
            }
        } finally {
            h8.recycle();
        }
    }
}
