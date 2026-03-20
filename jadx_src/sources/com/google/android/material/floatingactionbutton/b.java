package com.google.android.material.floatingactionbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Property;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import l7.h;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b implements f {

    /* renamed from: a  reason: collision with root package name */
    private final Context f17938a;

    /* renamed from: b  reason: collision with root package name */
    private final ExtendedFloatingActionButton f17939b;

    /* renamed from: c  reason: collision with root package name */
    private final ArrayList<Animator.AnimatorListener> f17940c = new ArrayList<>();

    /* renamed from: d  reason: collision with root package name */
    private final com.google.android.material.floatingactionbutton.a f17941d;

    /* renamed from: e  reason: collision with root package name */
    private h f17942e;

    /* renamed from: f  reason: collision with root package name */
    private h f17943f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a extends Property<ExtendedFloatingActionButton, Float> {
        a(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(ExtendedFloatingActionButton extendedFloatingActionButton) {
            return Float.valueOf(l7.a.a(0.0f, 1.0f, (Color.alpha(extendedFloatingActionButton.getCurrentTextColor()) / 255.0f) / Color.alpha(extendedFloatingActionButton.R.getColorForState(extendedFloatingActionButton.getDrawableState(), b.this.f17939b.R.getDefaultColor()))));
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(ExtendedFloatingActionButton extendedFloatingActionButton, Float f5) {
            int colorForState = extendedFloatingActionButton.R.getColorForState(extendedFloatingActionButton.getDrawableState(), b.this.f17939b.R.getDefaultColor());
            ColorStateList valueOf = ColorStateList.valueOf(Color.argb((int) (l7.a.a(0.0f, Color.alpha(colorForState) / 255.0f, f5.floatValue()) * 255.0f), Color.red(colorForState), Color.green(colorForState), Color.blue(colorForState)));
            if (f5.floatValue() == 1.0f) {
                extendedFloatingActionButton.D(extendedFloatingActionButton.R);
            } else {
                extendedFloatingActionButton.D(valueOf);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(ExtendedFloatingActionButton extendedFloatingActionButton, com.google.android.material.floatingactionbutton.a aVar) {
        this.f17939b = extendedFloatingActionButton;
        this.f17938a = extendedFloatingActionButton.getContext();
        this.f17941d = aVar;
    }

    @Override // com.google.android.material.floatingactionbutton.f
    public final void a(h hVar) {
        this.f17943f = hVar;
    }

    @Override // com.google.android.material.floatingactionbutton.f
    public void b() {
        this.f17941d.b();
    }

    @Override // com.google.android.material.floatingactionbutton.f
    public h e() {
        return this.f17943f;
    }

    @Override // com.google.android.material.floatingactionbutton.f
    public void g() {
        this.f17941d.b();
    }

    @Override // com.google.android.material.floatingactionbutton.f
    public AnimatorSet h() {
        return l(m());
    }

    @Override // com.google.android.material.floatingactionbutton.f
    public final List<Animator.AnimatorListener> i() {
        return this.f17940c;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AnimatorSet l(h hVar) {
        ArrayList arrayList = new ArrayList();
        if (hVar.j("opacity")) {
            arrayList.add(hVar.f("opacity", this.f17939b, View.ALPHA));
        }
        if (hVar.j("scale")) {
            arrayList.add(hVar.f("scale", this.f17939b, View.SCALE_Y));
            arrayList.add(hVar.f("scale", this.f17939b, View.SCALE_X));
        }
        if (hVar.j("width")) {
            arrayList.add(hVar.f("width", this.f17939b, ExtendedFloatingActionButton.W));
        }
        if (hVar.j("height")) {
            arrayList.add(hVar.f("height", this.f17939b, ExtendedFloatingActionButton.f17892a0));
        }
        if (hVar.j("paddingStart")) {
            arrayList.add(hVar.f("paddingStart", this.f17939b, ExtendedFloatingActionButton.f17893b0));
        }
        if (hVar.j("paddingEnd")) {
            arrayList.add(hVar.f("paddingEnd", this.f17939b, ExtendedFloatingActionButton.f17894c0));
        }
        if (hVar.j("labelOpacity")) {
            arrayList.add(hVar.f("labelOpacity", this.f17939b, new a(Float.class, "LABEL_OPACITY_PROPERTY")));
        }
        AnimatorSet animatorSet = new AnimatorSet();
        l7.b.a(animatorSet, arrayList);
        return animatorSet;
    }

    public final h m() {
        h hVar = this.f17943f;
        if (hVar != null) {
            return hVar;
        }
        if (this.f17942e == null) {
            this.f17942e = h.d(this.f17938a, c());
        }
        return (h) androidx.core.util.h.h(this.f17942e);
    }

    @Override // com.google.android.material.floatingactionbutton.f
    public void onAnimationStart(Animator animator) {
        this.f17941d.c(animator);
    }
}
