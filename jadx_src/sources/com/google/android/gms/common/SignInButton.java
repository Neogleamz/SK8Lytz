package com.google.android.gms.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.zaaa;
import com.google.android.gms.dynamic.RemoteCreator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SignInButton extends FrameLayout implements View.OnClickListener {

    /* renamed from: a  reason: collision with root package name */
    private int f11533a;

    /* renamed from: b  reason: collision with root package name */
    private int f11534b;

    /* renamed from: c  reason: collision with root package name */
    private View f11535c;

    /* renamed from: d  reason: collision with root package name */
    private View.OnClickListener f11536d;

    public SignInButton(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public SignInButton(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f11536d = null;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, h6.d.f20344b, 0, 0);
        try {
            this.f11533a = obtainStyledAttributes.getInt(h6.d.f20345c, 0);
            this.f11534b = obtainStyledAttributes.getInt(h6.d.f20346d, 2);
            obtainStyledAttributes.recycle();
            a(this.f11533a, this.f11534b);
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
    }

    private final void b(Context context) {
        View view = this.f11535c;
        if (view != null) {
            removeView(view);
        }
        try {
            this.f11535c = com.google.android.gms.common.internal.l.c(context, this.f11533a, this.f11534b);
        } catch (RemoteCreator.RemoteCreatorException unused) {
            Log.w("SignInButton", "Sign in button not found, using placeholder instead");
            int i8 = this.f11533a;
            int i9 = this.f11534b;
            zaaa zaaaVar = new zaaa(context, null);
            zaaaVar.a(context.getResources(), i8, i9);
            this.f11535c = zaaaVar;
        }
        addView(this.f11535c);
        this.f11535c.setEnabled(isEnabled());
        this.f11535c.setOnClickListener(this);
    }

    public void a(int i8, int i9) {
        this.f11533a = i8;
        this.f11534b = i9;
        b(getContext());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        View.OnClickListener onClickListener = this.f11536d;
        if (onClickListener == null || view != this.f11535c) {
            return;
        }
        onClickListener.onClick(this);
    }

    public void setColorScheme(int i8) {
        a(this.f11533a, i8);
    }

    @Override // android.view.View
    public void setEnabled(boolean z4) {
        super.setEnabled(z4);
        this.f11535c.setEnabled(z4);
    }

    @Override // android.view.View
    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.f11536d = onClickListener;
        View view = this.f11535c;
        if (view != null) {
            view.setOnClickListener(this);
        }
    }

    @Deprecated
    public void setScopes(Scope[] scopeArr) {
        a(this.f11533a, this.f11534b);
    }

    public void setSize(int i8) {
        a(i8, this.f11534b);
    }
}
