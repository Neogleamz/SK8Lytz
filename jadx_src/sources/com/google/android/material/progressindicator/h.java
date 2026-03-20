package com.google.android.material.progressindicator;

import android.animation.Animator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class h<T extends Animator> {

    /* renamed from: a  reason: collision with root package name */
    protected i f18311a;

    /* renamed from: b  reason: collision with root package name */
    protected final float[] f18312b;

    /* renamed from: c  reason: collision with root package name */
    protected final int[] f18313c;

    /* JADX INFO: Access modifiers changed from: protected */
    public h(int i8) {
        this.f18312b = new float[i8 * 2];
        this.f18313c = new int[i8];
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a();

    /* JADX INFO: Access modifiers changed from: protected */
    public float b(int i8, int i9, int i10) {
        return (i8 - i9) / i10;
    }

    public abstract void c();

    public abstract void d(androidx.vectordrawable.graphics.drawable.b bVar);

    /* JADX INFO: Access modifiers changed from: protected */
    public void e(i iVar) {
        this.f18311a = iVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void f();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void g();

    public abstract void h();
}
