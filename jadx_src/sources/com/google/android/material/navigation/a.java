package com.google.android.material.navigation;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.view.menu.i;
import androidx.appcompat.view.menu.n;
import androidx.appcompat.widget.o0;
import androidx.core.view.accessibility.c;
import androidx.core.view.c0;
import androidx.core.view.z;
import androidx.core.widget.k;
import com.google.android.material.badge.BadgeDrawable;
import k7.d;
import k7.e;
import k7.f;
import k7.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a extends FrameLayout implements n.a {

    /* renamed from: w  reason: collision with root package name */
    private static final int[] f18198w = {16842912};

    /* renamed from: a  reason: collision with root package name */
    private final int f18199a;

    /* renamed from: b  reason: collision with root package name */
    private float f18200b;

    /* renamed from: c  reason: collision with root package name */
    private float f18201c;

    /* renamed from: d  reason: collision with root package name */
    private float f18202d;

    /* renamed from: e  reason: collision with root package name */
    private int f18203e;

    /* renamed from: f  reason: collision with root package name */
    private boolean f18204f;

    /* renamed from: g  reason: collision with root package name */
    private ImageView f18205g;

    /* renamed from: h  reason: collision with root package name */
    private final ViewGroup f18206h;

    /* renamed from: j  reason: collision with root package name */
    private final TextView f18207j;

    /* renamed from: k  reason: collision with root package name */
    private final TextView f18208k;

    /* renamed from: l  reason: collision with root package name */
    private int f18209l;

    /* renamed from: m  reason: collision with root package name */
    private i f18210m;

    /* renamed from: n  reason: collision with root package name */
    private ColorStateList f18211n;

    /* renamed from: p  reason: collision with root package name */
    private Drawable f18212p;
    private Drawable q;

    /* renamed from: t  reason: collision with root package name */
    private BadgeDrawable f18213t;

    /* renamed from: com.google.android.material.navigation.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class View$OnLayoutChangeListenerC0137a implements View.OnLayoutChangeListener {
        View$OnLayoutChangeListenerC0137a() {
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i8, int i9, int i10, int i11, int i12, int i13, int i14, int i15) {
            if (a.this.f18205g.getVisibility() == 0) {
                a aVar = a.this;
                aVar.m(aVar.f18205g);
            }
        }
    }

    public a(Context context) {
        super(context);
        this.f18209l = -1;
        LayoutInflater.from(context).inflate(getItemLayoutResId(), (ViewGroup) this, true);
        this.f18205g = (ImageView) findViewById(f.M);
        ViewGroup viewGroup = (ViewGroup) findViewById(f.N);
        this.f18206h = viewGroup;
        TextView textView = (TextView) findViewById(f.P);
        this.f18207j = textView;
        TextView textView2 = (TextView) findViewById(f.O);
        this.f18208k = textView2;
        setBackgroundResource(getItemBackgroundResId());
        this.f18199a = getResources().getDimensionPixelSize(getItemDefaultMarginResId());
        viewGroup.setTag(f.L, Integer.valueOf(viewGroup.getPaddingBottom()));
        c0.E0(textView, 2);
        c0.E0(textView2, 2);
        setFocusable(true);
        c(textView.getTextSize(), textView2.getTextSize());
        ImageView imageView = this.f18205g;
        if (imageView != null) {
            imageView.addOnLayoutChangeListener(new View$OnLayoutChangeListenerC0137a());
        }
    }

    private void c(float f5, float f8) {
        this.f18200b = f5 - f8;
        this.f18201c = (f8 * 1.0f) / f5;
        this.f18202d = (f5 * 1.0f) / f8;
    }

    private FrameLayout f(View view) {
        ImageView imageView = this.f18205g;
        if (view == imageView && com.google.android.material.badge.a.f17456a) {
            return (FrameLayout) imageView.getParent();
        }
        return null;
    }

    private boolean g() {
        return this.f18213t != null;
    }

    private int getItemVisiblePosition() {
        ViewGroup viewGroup = (ViewGroup) getParent();
        int indexOfChild = viewGroup.indexOfChild(this);
        int i8 = 0;
        for (int i9 = 0; i9 < indexOfChild; i9++) {
            View childAt = viewGroup.getChildAt(i9);
            if ((childAt instanceof a) && childAt.getVisibility() == 0) {
                i8++;
            }
        }
        return i8;
    }

    private int getSuggestedIconHeight() {
        BadgeDrawable badgeDrawable = this.f18213t;
        int minimumHeight = badgeDrawable != null ? badgeDrawable.getMinimumHeight() / 2 : 0;
        return Math.max(minimumHeight, ((FrameLayout.LayoutParams) this.f18205g.getLayoutParams()).topMargin) + this.f18205g.getMeasuredWidth() + minimumHeight;
    }

    private int getSuggestedIconWidth() {
        BadgeDrawable badgeDrawable = this.f18213t;
        int minimumWidth = badgeDrawable == null ? 0 : badgeDrawable.getMinimumWidth() - this.f18213t.j();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.f18205g.getLayoutParams();
        return Math.max(minimumWidth, layoutParams.leftMargin) + this.f18205g.getMeasuredWidth() + Math.max(minimumWidth, layoutParams.rightMargin);
    }

    private static void i(View view, int i8, int i9) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.topMargin = i8;
        layoutParams.gravity = i9;
        view.setLayoutParams(layoutParams);
    }

    private static void j(View view, float f5, float f8, int i8) {
        view.setScaleX(f5);
        view.setScaleY(f8);
        view.setVisibility(i8);
    }

    private void k(View view) {
        if (g() && view != null) {
            setClipChildren(false);
            setClipToPadding(false);
            com.google.android.material.badge.a.a(this.f18213t, view, f(view));
        }
    }

    private void l(View view) {
        if (g()) {
            if (view != null) {
                setClipChildren(true);
                setClipToPadding(true);
                com.google.android.material.badge.a.d(this.f18213t, view);
            }
            this.f18213t = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void m(View view) {
        if (g()) {
            com.google.android.material.badge.a.e(this.f18213t, view, f(view));
        }
    }

    private static void n(View view, int i8) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), i8);
    }

    @Override // androidx.appcompat.view.menu.n.a
    public boolean d() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.n.a
    public void e(i iVar, int i8) {
        this.f18210m = iVar;
        setCheckable(iVar.isCheckable());
        setChecked(iVar.isChecked());
        setEnabled(iVar.isEnabled());
        setIcon(iVar.getIcon());
        setTitle(iVar.getTitle());
        setId(iVar.getItemId());
        if (!TextUtils.isEmpty(iVar.getContentDescription())) {
            setContentDescription(iVar.getContentDescription());
        }
        CharSequence tooltipText = !TextUtils.isEmpty(iVar.getTooltipText()) ? iVar.getTooltipText() : iVar.getTitle();
        int i9 = Build.VERSION.SDK_INT;
        if (i9 < 21 || i9 > 23) {
            o0.a(this, tooltipText);
        }
        setVisibility(iVar.isVisible() ? 0 : 8);
    }

    public BadgeDrawable getBadge() {
        return this.f18213t;
    }

    protected int getItemBackgroundResId() {
        return e.f21147g;
    }

    @Override // androidx.appcompat.view.menu.n.a
    public i getItemData() {
        return this.f18210m;
    }

    protected int getItemDefaultMarginResId() {
        return d.f21105h0;
    }

    protected abstract int getItemLayoutResId();

    public int getItemPosition() {
        return this.f18209l;
    }

    @Override // android.view.View
    protected int getSuggestedMinimumHeight() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.f18206h.getLayoutParams();
        return getSuggestedIconHeight() + layoutParams.topMargin + this.f18206h.getMeasuredHeight() + layoutParams.bottomMargin;
    }

    @Override // android.view.View
    protected int getSuggestedMinimumWidth() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.f18206h.getLayoutParams();
        return Math.max(getSuggestedIconWidth(), layoutParams.leftMargin + this.f18206h.getMeasuredWidth() + layoutParams.rightMargin);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h() {
        l(this.f18205g);
    }

    @Override // android.view.ViewGroup, android.view.View
    public int[] onCreateDrawableState(int i8) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i8 + 1);
        i iVar = this.f18210m;
        if (iVar != null && iVar.isCheckable() && this.f18210m.isChecked()) {
            FrameLayout.mergeDrawableStates(onCreateDrawableState, f18198w);
        }
        return onCreateDrawableState;
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        BadgeDrawable badgeDrawable = this.f18213t;
        if (badgeDrawable != null && badgeDrawable.isVisible()) {
            CharSequence title = this.f18210m.getTitle();
            if (!TextUtils.isEmpty(this.f18210m.getContentDescription())) {
                title = this.f18210m.getContentDescription();
            }
            accessibilityNodeInfo.setContentDescription(((Object) title) + ", " + ((Object) this.f18213t.h()));
        }
        androidx.core.view.accessibility.c I0 = androidx.core.view.accessibility.c.I0(accessibilityNodeInfo);
        I0.f0(c.C0043c.a(0, 1, getItemVisiblePosition(), 1, false, isSelected()));
        if (isSelected()) {
            I0.d0(false);
            I0.T(c.a.f4911i);
        }
        I0.w0(getResources().getString(j.f21212h));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBadge(BadgeDrawable badgeDrawable) {
        this.f18213t = badgeDrawable;
        ImageView imageView = this.f18205g;
        if (imageView != null) {
            k(imageView);
        }
    }

    public void setCheckable(boolean z4) {
        refreshDrawableState();
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0067, code lost:
        if (r9 != false) goto L13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x0069, code lost:
        i(r8.f18205g, (int) (r8.f18199a + r8.f18200b), 49);
        j(r8.f18208k, 1.0f, 1.0f, 0);
        r0 = r8.f18207j;
        r1 = r8.f18201c;
        j(r0, r1, r1, 4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:13:0x0082, code lost:
        i(r8.f18205g, r8.f18199a, 49);
        r0 = r8.f18208k;
        r1 = r8.f18202d;
        j(r0, r1, r1, 4);
        j(r8.f18207j, 1.0f, 1.0f, 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x009a, code lost:
        if (r9 != false) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x009c, code lost:
        i(r0, r1, 49);
        r0 = r8.f18206h;
        n(r0, ((java.lang.Integer) r0.getTag(k7.f.L)).intValue());
        r8.f18208k.setVisibility(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x00b6, code lost:
        i(r0, r1, 17);
        n(r8.f18206h, 0);
        r8.f18208k.setVisibility(4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x00c3, code lost:
        r8.f18207j.setVisibility(4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x00d1, code lost:
        if (r9 != false) goto L17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x00e5, code lost:
        if (r9 != false) goto L13;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void setChecked(boolean r9) {
        /*
            Method dump skipped, instructions count: 239
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.navigation.a.setChecked(boolean):void");
    }

    @Override // android.view.View
    public void setEnabled(boolean z4) {
        super.setEnabled(z4);
        this.f18207j.setEnabled(z4);
        this.f18208k.setEnabled(z4);
        this.f18205g.setEnabled(z4);
        c0.K0(this, z4 ? z.b(getContext(), 1002) : null);
    }

    public void setIcon(Drawable drawable) {
        if (drawable == this.f18212p) {
            return;
        }
        this.f18212p = drawable;
        if (drawable != null) {
            Drawable.ConstantState constantState = drawable.getConstantState();
            if (constantState != null) {
                drawable = constantState.newDrawable();
            }
            drawable = androidx.core.graphics.drawable.a.r(drawable).mutate();
            this.q = drawable;
            ColorStateList colorStateList = this.f18211n;
            if (colorStateList != null) {
                androidx.core.graphics.drawable.a.o(drawable, colorStateList);
            }
        }
        this.f18205g.setImageDrawable(drawable);
    }

    public void setIconSize(int i8) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.f18205g.getLayoutParams();
        layoutParams.width = i8;
        layoutParams.height = i8;
        this.f18205g.setLayoutParams(layoutParams);
    }

    public void setIconTintList(ColorStateList colorStateList) {
        Drawable drawable;
        this.f18211n = colorStateList;
        if (this.f18210m == null || (drawable = this.q) == null) {
            return;
        }
        androidx.core.graphics.drawable.a.o(drawable, colorStateList);
        this.q.invalidateSelf();
    }

    public void setItemBackground(int i8) {
        setItemBackground(i8 == 0 ? null : androidx.core.content.a.f(getContext(), i8));
    }

    public void setItemBackground(Drawable drawable) {
        if (drawable != null && drawable.getConstantState() != null) {
            drawable = drawable.getConstantState().newDrawable().mutate();
        }
        c0.x0(this, drawable);
    }

    public void setItemPosition(int i8) {
        this.f18209l = i8;
    }

    public void setLabelVisibilityMode(int i8) {
        if (this.f18203e != i8) {
            this.f18203e = i8;
            i iVar = this.f18210m;
            if (iVar != null) {
                setChecked(iVar.isChecked());
            }
        }
    }

    public void setShifting(boolean z4) {
        if (this.f18204f != z4) {
            this.f18204f = z4;
            i iVar = this.f18210m;
            if (iVar != null) {
                setChecked(iVar.isChecked());
            }
        }
    }

    public void setTextAppearanceActive(int i8) {
        k.q(this.f18208k, i8);
        c(this.f18207j.getTextSize(), this.f18208k.getTextSize());
    }

    public void setTextAppearanceInactive(int i8) {
        k.q(this.f18207j, i8);
        c(this.f18207j.getTextSize(), this.f18208k.getTextSize());
    }

    public void setTextColor(ColorStateList colorStateList) {
        if (colorStateList != null) {
            this.f18207j.setTextColor(colorStateList);
            this.f18208k.setTextColor(colorStateList);
        }
    }

    public void setTitle(CharSequence charSequence) {
        this.f18207j.setText(charSequence);
        this.f18208k.setText(charSequence);
        i iVar = this.f18210m;
        if (iVar == null || TextUtils.isEmpty(iVar.getContentDescription())) {
            setContentDescription(charSequence);
        }
        i iVar2 = this.f18210m;
        if (iVar2 != null && !TextUtils.isEmpty(iVar2.getTooltipText())) {
            charSequence = this.f18210m.getTooltipText();
        }
        int i8 = Build.VERSION.SDK_INT;
        if (i8 < 21 || i8 > 23) {
            o0.a(this, charSequence);
        }
    }
}
