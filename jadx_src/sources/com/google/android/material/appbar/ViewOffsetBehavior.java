package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class ViewOffsetBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

    /* renamed from: a  reason: collision with root package name */
    private a f17411a;

    /* renamed from: b  reason: collision with root package name */
    private int f17412b;

    /* renamed from: c  reason: collision with root package name */
    private int f17413c;

    public ViewOffsetBehavior() {
        this.f17412b = 0;
        this.f17413c = 0;
    }

    public ViewOffsetBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.f17412b = 0;
        this.f17413c = 0;
    }

    public int E() {
        a aVar = this.f17411a;
        if (aVar != null) {
            return aVar.c();
        }
        return 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void F(CoordinatorLayout coordinatorLayout, V v8, int i8) {
        coordinatorLayout.M(v8, i8);
    }

    public boolean G(int i8) {
        a aVar = this.f17411a;
        if (aVar != null) {
            return aVar.f(i8);
        }
        this.f17412b = i8;
        return false;
    }

    @Override // androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior
    public boolean l(CoordinatorLayout coordinatorLayout, V v8, int i8) {
        F(coordinatorLayout, v8, i8);
        if (this.f17411a == null) {
            this.f17411a = new a(v8);
        }
        this.f17411a.d();
        this.f17411a.a();
        int i9 = this.f17412b;
        if (i9 != 0) {
            this.f17411a.f(i9);
            this.f17412b = 0;
        }
        int i10 = this.f17413c;
        if (i10 != 0) {
            this.f17411a.e(i10);
            this.f17413c = 0;
            return true;
        }
        return true;
    }
}
