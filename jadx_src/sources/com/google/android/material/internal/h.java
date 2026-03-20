package com.google.android.material.internal;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.StateSet;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h {

    /* renamed from: a  reason: collision with root package name */
    private final ArrayList<b> f18117a = new ArrayList<>();

    /* renamed from: b  reason: collision with root package name */
    private b f18118b = null;

    /* renamed from: c  reason: collision with root package name */
    ValueAnimator f18119c = null;

    /* renamed from: d  reason: collision with root package name */
    private final Animator.AnimatorListener f18120d = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends AnimatorListenerAdapter {
        a() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            h hVar = h.this;
            if (hVar.f18119c == animator) {
                hVar.f18119c = null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        final int[] f18122a;

        /* renamed from: b  reason: collision with root package name */
        final ValueAnimator f18123b;

        b(int[] iArr, ValueAnimator valueAnimator) {
            this.f18122a = iArr;
            this.f18123b = valueAnimator;
        }
    }

    private void b() {
        ValueAnimator valueAnimator = this.f18119c;
        if (valueAnimator != null) {
            valueAnimator.cancel();
            this.f18119c = null;
        }
    }

    private void e(b bVar) {
        ValueAnimator valueAnimator = bVar.f18123b;
        this.f18119c = valueAnimator;
        valueAnimator.start();
    }

    public void a(int[] iArr, ValueAnimator valueAnimator) {
        b bVar = new b(iArr, valueAnimator);
        valueAnimator.addListener(this.f18120d);
        this.f18117a.add(bVar);
    }

    public void c() {
        ValueAnimator valueAnimator = this.f18119c;
        if (valueAnimator != null) {
            valueAnimator.end();
            this.f18119c = null;
        }
    }

    public void d(int[] iArr) {
        b bVar;
        int size = this.f18117a.size();
        int i8 = 0;
        while (true) {
            if (i8 >= size) {
                bVar = null;
                break;
            }
            bVar = this.f18117a.get(i8);
            if (StateSet.stateSetMatches(bVar.f18122a, iArr)) {
                break;
            }
            i8++;
        }
        b bVar2 = this.f18118b;
        if (bVar == bVar2) {
            return;
        }
        if (bVar2 != null) {
            b();
        }
        this.f18118b = bVar;
        if (bVar != null) {
            e(bVar);
        }
    }
}
