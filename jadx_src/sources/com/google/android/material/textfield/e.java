package com.google.android.material.textfield;

import android.content.Context;
import com.google.android.material.internal.CheckableImageButton;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class e {

    /* renamed from: a  reason: collision with root package name */
    TextInputLayout f18656a;

    /* renamed from: b  reason: collision with root package name */
    Context f18657b;

    /* renamed from: c  reason: collision with root package name */
    CheckableImageButton f18658c;

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(TextInputLayout textInputLayout) {
        this.f18656a = textInputLayout;
        this.f18657b = textInputLayout.getContext();
        this.f18658c = textInputLayout.getEndIconView();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void a();

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(int i8) {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(boolean z4) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean d() {
        return false;
    }
}
