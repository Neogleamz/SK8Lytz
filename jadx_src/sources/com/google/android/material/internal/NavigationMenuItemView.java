package com.google.android.material.internal;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.appcompat.view.menu.n;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.o0;
import androidx.core.view.c0;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class NavigationMenuItemView extends ForegroundLinearLayout implements n.a {
    private static final int[] Q = {16842912};
    private int B;
    private boolean C;
    boolean E;
    private final CheckedTextView F;
    private FrameLayout G;
    private androidx.appcompat.view.menu.i H;
    private ColorStateList K;
    private boolean L;
    private Drawable O;
    private final androidx.core.view.a P;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends androidx.core.view.a {
        a() {
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            super.g(view, cVar);
            cVar.a0(NavigationMenuItemView.this.E);
        }
    }

    public NavigationMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public NavigationMenuItemView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        a aVar = new a();
        this.P = aVar;
        setOrientation(0);
        LayoutInflater.from(context).inflate(k7.h.f21190l, (ViewGroup) this, true);
        setIconSize(context.getResources().getDimensionPixelSize(k7.d.f21112l));
        CheckedTextView checkedTextView = (CheckedTextView) findViewById(k7.f.f21158g);
        this.F = checkedTextView;
        checkedTextView.setDuplicateParentStateEnabled(true);
        c0.t0(checkedTextView, aVar);
    }

    private void B() {
        LinearLayoutCompat.LayoutParams layoutParams;
        int i8;
        if (E()) {
            this.F.setVisibility(8);
            FrameLayout frameLayout = this.G;
            if (frameLayout == null) {
                return;
            }
            layoutParams = (LinearLayoutCompat.LayoutParams) frameLayout.getLayoutParams();
            i8 = -1;
        } else {
            this.F.setVisibility(0);
            FrameLayout frameLayout2 = this.G;
            if (frameLayout2 == null) {
                return;
            }
            layoutParams = (LinearLayoutCompat.LayoutParams) frameLayout2.getLayoutParams();
            i8 = -2;
        }
        ((LinearLayout.LayoutParams) layoutParams).width = i8;
        this.G.setLayoutParams(layoutParams);
    }

    private StateListDrawable C() {
        TypedValue typedValue = new TypedValue();
        if (getContext().getTheme().resolveAttribute(g.a.f19883w, typedValue, true)) {
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(Q, new ColorDrawable(typedValue.data));
            stateListDrawable.addState(ViewGroup.EMPTY_STATE_SET, new ColorDrawable(0));
            return stateListDrawable;
        }
        return null;
    }

    private boolean E() {
        return this.H.getTitle() == null && this.H.getIcon() == null && this.H.getActionView() != null;
    }

    private void setActionView(View view) {
        if (view != null) {
            if (this.G == null) {
                this.G = (FrameLayout) ((ViewStub) findViewById(k7.f.f21157f)).inflate();
            }
            this.G.removeAllViews();
            this.G.addView(view);
        }
    }

    public void D() {
        FrameLayout frameLayout = this.G;
        if (frameLayout != null) {
            frameLayout.removeAllViews();
        }
        this.F.setCompoundDrawables(null, null, null, null);
    }

    @Override // androidx.appcompat.view.menu.n.a
    public boolean d() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.n.a
    public void e(androidx.appcompat.view.menu.i iVar, int i8) {
        this.H = iVar;
        if (iVar.getItemId() > 0) {
            setId(iVar.getItemId());
        }
        setVisibility(iVar.isVisible() ? 0 : 8);
        if (getBackground() == null) {
            c0.x0(this, C());
        }
        setCheckable(iVar.isCheckable());
        setChecked(iVar.isChecked());
        setEnabled(iVar.isEnabled());
        setTitle(iVar.getTitle());
        setIcon(iVar.getIcon());
        setActionView(iVar.getActionView());
        setContentDescription(iVar.getContentDescription());
        o0.a(this, iVar.getTooltipText());
        B();
    }

    @Override // androidx.appcompat.view.menu.n.a
    public androidx.appcompat.view.menu.i getItemData() {
        return this.H;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected int[] onCreateDrawableState(int i8) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i8 + 1);
        androidx.appcompat.view.menu.i iVar = this.H;
        if (iVar != null && iVar.isCheckable() && this.H.isChecked()) {
            ViewGroup.mergeDrawableStates(onCreateDrawableState, Q);
        }
        return onCreateDrawableState;
    }

    public void setCheckable(boolean z4) {
        refreshDrawableState();
        if (this.E != z4) {
            this.E = z4;
            this.P.l(this.F, RecognitionOptions.PDF417);
        }
    }

    public void setChecked(boolean z4) {
        refreshDrawableState();
        this.F.setChecked(z4);
    }

    public void setHorizontalPadding(int i8) {
        setPadding(i8, 0, i8, 0);
    }

    public void setIcon(Drawable drawable) {
        if (drawable != null) {
            if (this.L) {
                Drawable.ConstantState constantState = drawable.getConstantState();
                if (constantState != null) {
                    drawable = constantState.newDrawable();
                }
                drawable = androidx.core.graphics.drawable.a.r(drawable).mutate();
                androidx.core.graphics.drawable.a.o(drawable, this.K);
            }
            int i8 = this.B;
            drawable.setBounds(0, 0, i8, i8);
        } else if (this.C) {
            if (this.O == null) {
                Drawable e8 = androidx.core.content.res.h.e(getResources(), k7.e.f21148h, getContext().getTheme());
                this.O = e8;
                if (e8 != null) {
                    int i9 = this.B;
                    e8.setBounds(0, 0, i9, i9);
                }
            }
            drawable = this.O;
        }
        androidx.core.widget.k.l(this.F, drawable, null, null, null);
    }

    public void setIconPadding(int i8) {
        this.F.setCompoundDrawablePadding(i8);
    }

    public void setIconSize(int i8) {
        this.B = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIconTintList(ColorStateList colorStateList) {
        this.K = colorStateList;
        this.L = colorStateList != null;
        androidx.appcompat.view.menu.i iVar = this.H;
        if (iVar != null) {
            setIcon(iVar.getIcon());
        }
    }

    public void setMaxLines(int i8) {
        this.F.setMaxLines(i8);
    }

    public void setNeedsEmptyIcon(boolean z4) {
        this.C = z4;
    }

    public void setTextAppearance(int i8) {
        androidx.core.widget.k.q(this.F, i8);
    }

    public void setTextColor(ColorStateList colorStateList) {
        this.F.setTextColor(colorStateList);
    }

    public void setTitle(CharSequence charSequence) {
        this.F.setText(charSequence);
    }
}
