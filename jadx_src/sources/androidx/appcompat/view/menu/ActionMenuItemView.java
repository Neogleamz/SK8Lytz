package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.view.menu.g;
import androidx.appcompat.view.menu.n;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.o0;
import androidx.appcompat.widget.w;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ActionMenuItemView extends AppCompatTextView implements n.a, View.OnClickListener, ActionMenuView.a {

    /* renamed from: h  reason: collision with root package name */
    i f822h;

    /* renamed from: j  reason: collision with root package name */
    private CharSequence f823j;

    /* renamed from: k  reason: collision with root package name */
    private Drawable f824k;

    /* renamed from: l  reason: collision with root package name */
    g.b f825l;

    /* renamed from: m  reason: collision with root package name */
    private w f826m;

    /* renamed from: n  reason: collision with root package name */
    b f827n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f828p;
    private boolean q;

    /* renamed from: t  reason: collision with root package name */
    private int f829t;

    /* renamed from: w  reason: collision with root package name */
    private int f830w;

    /* renamed from: x  reason: collision with root package name */
    private int f831x;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private class a extends w {
        public a() {
            super(ActionMenuItemView.this);
        }

        @Override // androidx.appcompat.widget.w
        public p b() {
            b bVar = ActionMenuItemView.this.f827n;
            if (bVar != null) {
                return bVar.a();
            }
            return null;
        }

        @Override // androidx.appcompat.widget.w
        protected boolean c() {
            p b9;
            ActionMenuItemView actionMenuItemView = ActionMenuItemView.this;
            g.b bVar = actionMenuItemView.f825l;
            return bVar != null && bVar.a(actionMenuItemView.f822h) && (b9 = b()) != null && b9.b();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b {
        public abstract p a();
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ActionMenuItemView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        Resources resources = context.getResources();
        this.f828p = s();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, g.j.f20111v, i8, 0);
        this.f829t = obtainStyledAttributes.getDimensionPixelSize(g.j.f20116w, 0);
        obtainStyledAttributes.recycle();
        this.f831x = (int) ((resources.getDisplayMetrics().density * 32.0f) + 0.5f);
        setOnClickListener(this);
        this.f830w = -1;
        setSaveEnabled(false);
    }

    private boolean s() {
        Configuration configuration = getContext().getResources().getConfiguration();
        int i8 = configuration.screenWidthDp;
        return i8 >= 480 || (i8 >= 640 && configuration.screenHeightDp >= 480) || configuration.orientation == 2;
    }

    private void t() {
        boolean z4 = true;
        boolean z8 = !TextUtils.isEmpty(this.f823j);
        if (this.f824k != null && (!this.f822h.B() || (!this.f828p && !this.q))) {
            z4 = false;
        }
        boolean z9 = z8 & z4;
        setText(z9 ? this.f823j : null);
        CharSequence contentDescription = this.f822h.getContentDescription();
        if (TextUtils.isEmpty(contentDescription)) {
            contentDescription = z9 ? null : this.f822h.getTitle();
        }
        setContentDescription(contentDescription);
        CharSequence tooltipText = this.f822h.getTooltipText();
        if (TextUtils.isEmpty(tooltipText)) {
            o0.a(this, z9 ? null : this.f822h.getTitle());
        } else {
            o0.a(this, tooltipText);
        }
    }

    @Override // androidx.appcompat.widget.ActionMenuView.a
    public boolean a() {
        return r();
    }

    @Override // androidx.appcompat.widget.ActionMenuView.a
    public boolean b() {
        return r() && this.f822h.getIcon() == null;
    }

    @Override // androidx.appcompat.view.menu.n.a
    public boolean d() {
        return true;
    }

    @Override // androidx.appcompat.view.menu.n.a
    public void e(i iVar, int i8) {
        this.f822h = iVar;
        setIcon(iVar.getIcon());
        setTitle(iVar.i(this));
        setId(iVar.getItemId());
        setVisibility(iVar.isVisible() ? 0 : 8);
        setEnabled(iVar.isEnabled());
        if (iVar.hasSubMenu() && this.f826m == null) {
            this.f826m = new a();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return Button.class.getName();
    }

    @Override // androidx.appcompat.view.menu.n.a
    public i getItemData() {
        return this.f822h;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        g.b bVar = this.f825l;
        if (bVar != null) {
            bVar.a(this.f822h);
        }
    }

    @Override // android.widget.TextView, android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.f828p = s();
        t();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.widget.AppCompatTextView, android.widget.TextView, android.view.View
    public void onMeasure(int i8, int i9) {
        int i10;
        boolean r4 = r();
        if (r4 && (i10 = this.f830w) >= 0) {
            super.setPadding(i10, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        super.onMeasure(i8, i9);
        int mode = View.MeasureSpec.getMode(i8);
        int size = View.MeasureSpec.getSize(i8);
        int measuredWidth = getMeasuredWidth();
        int min = mode == Integer.MIN_VALUE ? Math.min(size, this.f829t) : this.f829t;
        if (mode != 1073741824 && this.f829t > 0 && measuredWidth < min) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(min, 1073741824), i9);
        }
        if (r4 || this.f824k == null) {
            return;
        }
        super.setPadding((getMeasuredWidth() - this.f824k.getBounds().width()) / 2, getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    @Override // android.widget.TextView, android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        super.onRestoreInstanceState(null);
    }

    @Override // android.widget.TextView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        w wVar;
        if (this.f822h.hasSubMenu() && (wVar = this.f826m) != null && wVar.onTouch(this, motionEvent)) {
            return true;
        }
        return super.onTouchEvent(motionEvent);
    }

    public boolean r() {
        return !TextUtils.isEmpty(getText());
    }

    public void setCheckable(boolean z4) {
    }

    public void setChecked(boolean z4) {
    }

    public void setExpandedFormat(boolean z4) {
        if (this.q != z4) {
            this.q = z4;
            i iVar = this.f822h;
            if (iVar != null) {
                iVar.c();
            }
        }
    }

    public void setIcon(Drawable drawable) {
        this.f824k = drawable;
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            int intrinsicHeight = drawable.getIntrinsicHeight();
            int i8 = this.f831x;
            if (intrinsicWidth > i8) {
                intrinsicHeight = (int) (intrinsicHeight * (i8 / intrinsicWidth));
                intrinsicWidth = i8;
            }
            if (intrinsicHeight > i8) {
                intrinsicWidth = (int) (intrinsicWidth * (i8 / intrinsicHeight));
            } else {
                i8 = intrinsicHeight;
            }
            drawable.setBounds(0, 0, intrinsicWidth, i8);
        }
        setCompoundDrawables(drawable, null, null, null);
        t();
    }

    public void setItemInvoker(g.b bVar) {
        this.f825l = bVar;
    }

    @Override // android.widget.TextView, android.view.View
    public void setPadding(int i8, int i9, int i10, int i11) {
        this.f830w = i8;
        super.setPadding(i8, i9, i10, i11);
    }

    public void setPopupCallback(b bVar) {
        this.f827n = bVar;
    }

    public void setTitle(CharSequence charSequence) {
        this.f823j = charSequence;
        t();
    }
}
