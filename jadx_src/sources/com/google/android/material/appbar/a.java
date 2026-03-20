package com.google.android.material.appbar;

import android.view.View;
import androidx.core.view.c0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class a {

    /* renamed from: a  reason: collision with root package name */
    private final View f17414a;

    /* renamed from: b  reason: collision with root package name */
    private int f17415b;

    /* renamed from: c  reason: collision with root package name */
    private int f17416c;

    /* renamed from: d  reason: collision with root package name */
    private int f17417d;

    /* renamed from: e  reason: collision with root package name */
    private int f17418e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f17419f = true;

    /* renamed from: g  reason: collision with root package name */
    private boolean f17420g = true;

    public a(View view) {
        this.f17414a = view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a() {
        View view = this.f17414a;
        c0.d0(view, this.f17417d - (view.getTop() - this.f17415b));
        View view2 = this.f17414a;
        c0.c0(view2, this.f17418e - (view2.getLeft() - this.f17416c));
    }

    public int b() {
        return this.f17415b;
    }

    public int c() {
        return this.f17417d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d() {
        this.f17415b = this.f17414a.getTop();
        this.f17416c = this.f17414a.getLeft();
    }

    public boolean e(int i8) {
        if (!this.f17420g || this.f17418e == i8) {
            return false;
        }
        this.f17418e = i8;
        a();
        return true;
    }

    public boolean f(int i8) {
        if (!this.f17419f || this.f17417d == i8) {
            return false;
        }
        this.f17417d = i8;
        a();
        return true;
    }
}
