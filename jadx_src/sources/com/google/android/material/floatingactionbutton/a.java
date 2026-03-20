package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class a {

    /* renamed from: a  reason: collision with root package name */
    private Animator f17937a;

    public void a() {
        Animator animator = this.f17937a;
        if (animator != null) {
            animator.cancel();
        }
    }

    public void b() {
        this.f17937a = null;
    }

    public void c(Animator animator) {
        a();
        this.f17937a = animator;
    }
}
