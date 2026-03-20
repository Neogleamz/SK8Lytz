package androidx.appcompat.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.appcompat.view.menu.n;
import androidx.appcompat.widget.j0;
import androidx.core.view.c0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ListMenuItemView extends LinearLayout implements n.a, AbsListView.SelectionBoundsAdjuster {

    /* renamed from: a  reason: collision with root package name */
    private i f836a;

    /* renamed from: b  reason: collision with root package name */
    private ImageView f837b;

    /* renamed from: c  reason: collision with root package name */
    private RadioButton f838c;

    /* renamed from: d  reason: collision with root package name */
    private TextView f839d;

    /* renamed from: e  reason: collision with root package name */
    private CheckBox f840e;

    /* renamed from: f  reason: collision with root package name */
    private TextView f841f;

    /* renamed from: g  reason: collision with root package name */
    private ImageView f842g;

    /* renamed from: h  reason: collision with root package name */
    private ImageView f843h;

    /* renamed from: j  reason: collision with root package name */
    private LinearLayout f844j;

    /* renamed from: k  reason: collision with root package name */
    private Drawable f845k;

    /* renamed from: l  reason: collision with root package name */
    private int f846l;

    /* renamed from: m  reason: collision with root package name */
    private Context f847m;

    /* renamed from: n  reason: collision with root package name */
    private boolean f848n;

    /* renamed from: p  reason: collision with root package name */
    private Drawable f849p;
    private boolean q;

    /* renamed from: t  reason: collision with root package name */
    private LayoutInflater f850t;

    /* renamed from: w  reason: collision with root package name */
    private boolean f851w;

    public ListMenuItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.G);
    }

    public ListMenuItemView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet);
        j0 v8 = j0.v(getContext(), attributeSet, g.j.f20024d2, i8, 0);
        this.f845k = v8.g(g.j.f20035f2);
        this.f846l = v8.n(g.j.f20030e2, -1);
        this.f848n = v8.a(g.j.f20040g2, false);
        this.f847m = context;
        this.f849p = v8.g(g.j.f20045h2);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(null, new int[]{16843049}, g.a.C, 0);
        this.q = obtainStyledAttributes.hasValue(0);
        v8.w();
        obtainStyledAttributes.recycle();
    }

    private void a(View view) {
        b(view, -1);
    }

    private void b(View view, int i8) {
        LinearLayout linearLayout = this.f844j;
        if (linearLayout != null) {
            linearLayout.addView(view, i8);
        } else {
            addView(view, i8);
        }
    }

    private void c() {
        CheckBox checkBox = (CheckBox) getInflater().inflate(g.g.f19970j, (ViewGroup) this, false);
        this.f840e = checkBox;
        a(checkBox);
    }

    private void f() {
        ImageView imageView = (ImageView) getInflater().inflate(g.g.f19971k, (ViewGroup) this, false);
        this.f837b = imageView;
        b(imageView, 0);
    }

    private void g() {
        RadioButton radioButton = (RadioButton) getInflater().inflate(g.g.f19973m, (ViewGroup) this, false);
        this.f838c = radioButton;
        a(radioButton);
    }

    private LayoutInflater getInflater() {
        if (this.f850t == null) {
            this.f850t = LayoutInflater.from(getContext());
        }
        return this.f850t;
    }

    private void setSubMenuArrowVisible(boolean z4) {
        ImageView imageView = this.f842g;
        if (imageView != null) {
            imageView.setVisibility(z4 ? 0 : 8);
        }
    }

    @Override // android.widget.AbsListView.SelectionBoundsAdjuster
    public void adjustListItemSelectionBounds(Rect rect) {
        ImageView imageView = this.f843h;
        if (imageView == null || imageView.getVisibility() != 0) {
            return;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.f843h.getLayoutParams();
        rect.top += this.f843h.getHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
    }

    @Override // androidx.appcompat.view.menu.n.a
    public boolean d() {
        return false;
    }

    @Override // androidx.appcompat.view.menu.n.a
    public void e(i iVar, int i8) {
        this.f836a = iVar;
        setVisibility(iVar.isVisible() ? 0 : 8);
        setTitle(iVar.i(this));
        setCheckable(iVar.isCheckable());
        h(iVar.A(), iVar.g());
        setIcon(iVar.getIcon());
        setEnabled(iVar.isEnabled());
        setSubMenuArrowVisible(iVar.hasSubMenu());
        setContentDescription(iVar.getContentDescription());
    }

    @Override // androidx.appcompat.view.menu.n.a
    public i getItemData() {
        return this.f836a;
    }

    public void h(boolean z4, char c9) {
        int i8 = (z4 && this.f836a.A()) ? 0 : 8;
        if (i8 == 0) {
            this.f841f.setText(this.f836a.h());
        }
        if (this.f841f.getVisibility() != i8) {
            this.f841f.setVisibility(i8);
        }
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        c0.x0(this, this.f845k);
        TextView textView = (TextView) findViewById(g.f.S);
        this.f839d = textView;
        int i8 = this.f846l;
        if (i8 != -1) {
            textView.setTextAppearance(this.f847m, i8);
        }
        this.f841f = (TextView) findViewById(g.f.L);
        ImageView imageView = (ImageView) findViewById(g.f.O);
        this.f842g = imageView;
        if (imageView != null) {
            imageView.setImageDrawable(this.f849p);
        }
        this.f843h = (ImageView) findViewById(g.f.f19955u);
        this.f844j = (LinearLayout) findViewById(g.f.f19948m);
    }

    @Override // android.widget.LinearLayout, android.view.View
    protected void onMeasure(int i8, int i9) {
        if (this.f837b != null && this.f848n) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.f837b.getLayoutParams();
            int i10 = layoutParams.height;
            if (i10 > 0 && layoutParams2.width <= 0) {
                layoutParams2.width = i10;
            }
        }
        super.onMeasure(i8, i9);
    }

    public void setCheckable(boolean z4) {
        CompoundButton compoundButton;
        CompoundButton compoundButton2;
        if (!z4 && this.f838c == null && this.f840e == null) {
            return;
        }
        if (this.f836a.m()) {
            if (this.f838c == null) {
                g();
            }
            compoundButton = this.f838c;
            compoundButton2 = this.f840e;
        } else {
            if (this.f840e == null) {
                c();
            }
            compoundButton = this.f840e;
            compoundButton2 = this.f838c;
        }
        if (z4) {
            compoundButton.setChecked(this.f836a.isChecked());
            if (compoundButton.getVisibility() != 0) {
                compoundButton.setVisibility(0);
            }
            if (compoundButton2 == null || compoundButton2.getVisibility() == 8) {
                return;
            }
            compoundButton2.setVisibility(8);
            return;
        }
        CheckBox checkBox = this.f840e;
        if (checkBox != null) {
            checkBox.setVisibility(8);
        }
        RadioButton radioButton = this.f838c;
        if (radioButton != null) {
            radioButton.setVisibility(8);
        }
    }

    public void setChecked(boolean z4) {
        CompoundButton compoundButton;
        if (this.f836a.m()) {
            if (this.f838c == null) {
                g();
            }
            compoundButton = this.f838c;
        } else {
            if (this.f840e == null) {
                c();
            }
            compoundButton = this.f840e;
        }
        compoundButton.setChecked(z4);
    }

    public void setForceShowIcon(boolean z4) {
        this.f851w = z4;
        this.f848n = z4;
    }

    public void setGroupDividerEnabled(boolean z4) {
        ImageView imageView = this.f843h;
        if (imageView != null) {
            imageView.setVisibility((this.q || !z4) ? 8 : 0);
        }
    }

    public void setIcon(Drawable drawable) {
        boolean z4 = this.f836a.z() || this.f851w;
        if (z4 || this.f848n) {
            ImageView imageView = this.f837b;
            if (imageView == null && drawable == null && !this.f848n) {
                return;
            }
            if (imageView == null) {
                f();
            }
            if (drawable == null && !this.f848n) {
                this.f837b.setVisibility(8);
                return;
            }
            ImageView imageView2 = this.f837b;
            if (!z4) {
                drawable = null;
            }
            imageView2.setImageDrawable(drawable);
            if (this.f837b.getVisibility() != 0) {
                this.f837b.setVisibility(0);
            }
        }
    }

    public void setTitle(CharSequence charSequence) {
        int i8;
        TextView textView;
        if (charSequence != null) {
            this.f839d.setText(charSequence);
            if (this.f839d.getVisibility() == 0) {
                return;
            }
            textView = this.f839d;
            i8 = 0;
        } else {
            i8 = 8;
            if (this.f839d.getVisibility() == 8) {
                return;
            }
            textView = this.f839d;
        }
        textView.setVisibility(i8);
    }
}
