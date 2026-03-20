package androidx.appcompat.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.util.Property;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CompoundButton;
import androidx.emoji2.text.e;
import com.example.seedpoint.R;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SwitchCompat extends CompoundButton {

    /* renamed from: j0  reason: collision with root package name */
    private static final Property<SwitchCompat, Float> f1325j0 = new a(Float.class, "thumbPos");

    /* renamed from: k0  reason: collision with root package name */
    private static final int[] f1326k0 = {16842912};
    private int A;
    private float B;
    private float C;
    private VelocityTracker E;
    private int F;
    float G;
    private int H;
    private int K;
    private int L;
    private int O;
    private int P;
    private int Q;
    private int R;
    private boolean T;
    private final TextPaint W;

    /* renamed from: a  reason: collision with root package name */
    private Drawable f1327a;

    /* renamed from: a0  reason: collision with root package name */
    private ColorStateList f1328a0;

    /* renamed from: b  reason: collision with root package name */
    private ColorStateList f1329b;

    /* renamed from: b0  reason: collision with root package name */
    private Layout f1330b0;

    /* renamed from: c  reason: collision with root package name */
    private PorterDuff.Mode f1331c;

    /* renamed from: c0  reason: collision with root package name */
    private Layout f1332c0;

    /* renamed from: d  reason: collision with root package name */
    private boolean f1333d;

    /* renamed from: d0  reason: collision with root package name */
    private TransformationMethod f1334d0;

    /* renamed from: e  reason: collision with root package name */
    private boolean f1335e;

    /* renamed from: e0  reason: collision with root package name */
    ObjectAnimator f1336e0;

    /* renamed from: f  reason: collision with root package name */
    private Drawable f1337f;

    /* renamed from: f0  reason: collision with root package name */
    private final p f1338f0;

    /* renamed from: g  reason: collision with root package name */
    private ColorStateList f1339g;

    /* renamed from: g0  reason: collision with root package name */
    private i f1340g0;

    /* renamed from: h  reason: collision with root package name */
    private PorterDuff.Mode f1341h;

    /* renamed from: h0  reason: collision with root package name */
    private c f1342h0;

    /* renamed from: i0  reason: collision with root package name */
    private final Rect f1343i0;

    /* renamed from: j  reason: collision with root package name */
    private boolean f1344j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f1345k;

    /* renamed from: l  reason: collision with root package name */
    private int f1346l;

    /* renamed from: m  reason: collision with root package name */
    private int f1347m;

    /* renamed from: n  reason: collision with root package name */
    private int f1348n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f1349p;
    private CharSequence q;

    /* renamed from: t  reason: collision with root package name */
    private CharSequence f1350t;

    /* renamed from: w  reason: collision with root package name */
    private CharSequence f1351w;

    /* renamed from: x  reason: collision with root package name */
    private CharSequence f1352x;

    /* renamed from: y  reason: collision with root package name */
    private boolean f1353y;

    /* renamed from: z  reason: collision with root package name */
    private int f1354z;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends Property<SwitchCompat, Float> {
        a(Class cls, String str) {
            super(cls, str);
        }

        @Override // android.util.Property
        /* renamed from: a */
        public Float get(SwitchCompat switchCompat) {
            return Float.valueOf(switchCompat.G);
        }

        @Override // android.util.Property
        /* renamed from: b */
        public void set(SwitchCompat switchCompat, Float f5) {
            switchCompat.setThumbPosition(f5.floatValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        static void a(ObjectAnimator objectAnimator, boolean z4) {
            objectAnimator.setAutoCancel(z4);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c extends e.AbstractC0051e {

        /* renamed from: a  reason: collision with root package name */
        private final Reference<SwitchCompat> f1355a;

        c(SwitchCompat switchCompat) {
            this.f1355a = new WeakReference(switchCompat);
        }

        @Override // androidx.emoji2.text.e.AbstractC0051e
        public void a(Throwable th) {
            SwitchCompat switchCompat = this.f1355a.get();
            if (switchCompat != null) {
                switchCompat.j();
            }
        }

        @Override // androidx.emoji2.text.e.AbstractC0051e
        public void b() {
            SwitchCompat switchCompat = this.f1355a.get();
            if (switchCompat != null) {
                switchCompat.j();
            }
        }
    }

    public SwitchCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, g.a.O);
    }

    public SwitchCompat(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f1329b = null;
        this.f1331c = null;
        this.f1333d = false;
        this.f1335e = false;
        this.f1339g = null;
        this.f1341h = null;
        this.f1344j = false;
        this.f1345k = false;
        this.E = VelocityTracker.obtain();
        this.T = true;
        this.f1343i0 = new Rect();
        e0.a(this, getContext());
        TextPaint textPaint = new TextPaint(1);
        this.W = textPaint;
        textPaint.density = getResources().getDisplayMetrics().density;
        int[] iArr = g.j.N2;
        j0 v8 = j0.v(context, attributeSet, iArr, i8, 0);
        androidx.core.view.c0.r0(this, context, iArr, attributeSet, v8.r(), i8, 0);
        Drawable g8 = v8.g(g.j.Q2);
        this.f1327a = g8;
        if (g8 != null) {
            g8.setCallback(this);
        }
        Drawable g9 = v8.g(g.j.Z2);
        this.f1337f = g9;
        if (g9 != null) {
            g9.setCallback(this);
        }
        setTextOnInternal(v8.p(g.j.O2));
        setTextOffInternal(v8.p(g.j.P2));
        this.f1353y = v8.a(g.j.R2, true);
        this.f1346l = v8.f(g.j.W2, 0);
        this.f1347m = v8.f(g.j.T2, 0);
        this.f1348n = v8.f(g.j.U2, 0);
        this.f1349p = v8.a(g.j.S2, false);
        ColorStateList c9 = v8.c(g.j.X2);
        if (c9 != null) {
            this.f1329b = c9;
            this.f1333d = true;
        }
        PorterDuff.Mode e8 = t.e(v8.k(g.j.Y2, -1), null);
        if (this.f1331c != e8) {
            this.f1331c = e8;
            this.f1335e = true;
        }
        if (this.f1333d || this.f1335e) {
            b();
        }
        ColorStateList c10 = v8.c(g.j.f20007a3);
        if (c10 != null) {
            this.f1339g = c10;
            this.f1344j = true;
        }
        PorterDuff.Mode e9 = t.e(v8.k(g.j.f20013b3, -1), null);
        if (this.f1341h != e9) {
            this.f1341h = e9;
            this.f1345k = true;
        }
        if (this.f1344j || this.f1345k) {
            c();
        }
        int n8 = v8.n(g.j.V2, 0);
        if (n8 != 0) {
            m(context, n8);
        }
        p pVar = new p(this);
        this.f1338f0 = pVar;
        pVar.m(attributeSet, i8);
        v8.w();
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        this.A = viewConfiguration.getScaledTouchSlop();
        this.F = viewConfiguration.getScaledMinimumFlingVelocity();
        getEmojiTextViewHelper().c(attributeSet, i8);
        refreshDrawableState();
        setChecked(isChecked());
    }

    private void a(boolean z4) {
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, f1325j0, z4 ? 1.0f : 0.0f);
        this.f1336e0 = ofFloat;
        ofFloat.setDuration(250L);
        if (Build.VERSION.SDK_INT >= 18) {
            b.a(this.f1336e0, true);
        }
        this.f1336e0.start();
    }

    private void b() {
        Drawable drawable = this.f1327a;
        if (drawable != null) {
            if (this.f1333d || this.f1335e) {
                Drawable mutate = androidx.core.graphics.drawable.a.r(drawable).mutate();
                this.f1327a = mutate;
                if (this.f1333d) {
                    androidx.core.graphics.drawable.a.o(mutate, this.f1329b);
                }
                if (this.f1335e) {
                    androidx.core.graphics.drawable.a.p(this.f1327a, this.f1331c);
                }
                if (this.f1327a.isStateful()) {
                    this.f1327a.setState(getDrawableState());
                }
            }
        }
    }

    private void c() {
        Drawable drawable = this.f1337f;
        if (drawable != null) {
            if (this.f1344j || this.f1345k) {
                Drawable mutate = androidx.core.graphics.drawable.a.r(drawable).mutate();
                this.f1337f = mutate;
                if (this.f1344j) {
                    androidx.core.graphics.drawable.a.o(mutate, this.f1339g);
                }
                if (this.f1345k) {
                    androidx.core.graphics.drawable.a.p(this.f1337f, this.f1341h);
                }
                if (this.f1337f.isStateful()) {
                    this.f1337f.setState(getDrawableState());
                }
            }
        }
    }

    private void d() {
        ObjectAnimator objectAnimator = this.f1336e0;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    private void e(MotionEvent motionEvent) {
        MotionEvent obtain = MotionEvent.obtain(motionEvent);
        obtain.setAction(3);
        super.onTouchEvent(obtain);
        obtain.recycle();
    }

    private static float f(float f5, float f8, float f9) {
        return f5 < f8 ? f8 : f5 > f9 ? f9 : f5;
    }

    private CharSequence g(CharSequence charSequence) {
        TransformationMethod f5 = getEmojiTextViewHelper().f(this.f1334d0);
        return f5 != null ? f5.getTransformation(charSequence, this) : charSequence;
    }

    private i getEmojiTextViewHelper() {
        if (this.f1340g0 == null) {
            this.f1340g0 = new i(this);
        }
        return this.f1340g0;
    }

    private boolean getTargetCheckedState() {
        return this.G > 0.5f;
    }

    private int getThumbOffset() {
        return (int) (((u0.b(this) ? 1.0f - this.G : this.G) * getThumbScrollRange()) + 0.5f);
    }

    private int getThumbScrollRange() {
        Drawable drawable = this.f1337f;
        if (drawable != null) {
            Rect rect = this.f1343i0;
            drawable.getPadding(rect);
            Drawable drawable2 = this.f1327a;
            Rect d8 = drawable2 != null ? t.d(drawable2) : t.f1606c;
            return ((((this.H - this.L) - rect.left) - rect.right) - d8.left) - d8.right;
        }
        return 0;
    }

    private boolean h(float f5, float f8) {
        if (this.f1327a == null) {
            return false;
        }
        int thumbOffset = getThumbOffset();
        this.f1327a.getPadding(this.f1343i0);
        int i8 = this.P;
        int i9 = this.A;
        int i10 = i8 - i9;
        int i11 = (this.O + thumbOffset) - i9;
        Rect rect = this.f1343i0;
        return f5 > ((float) i11) && f5 < ((float) ((((this.L + i11) + rect.left) + rect.right) + i9)) && f8 > ((float) i10) && f8 < ((float) (this.R + i9));
    }

    private Layout i(CharSequence charSequence) {
        TextPaint textPaint = this.W;
        return new StaticLayout(charSequence, textPaint, charSequence != null ? (int) Math.ceil(Layout.getDesiredWidth(charSequence, textPaint)) : 0, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
    }

    private void k() {
        if (Build.VERSION.SDK_INT >= 30) {
            CharSequence charSequence = this.f1351w;
            if (charSequence == null) {
                charSequence = getResources().getString(g.h.f19984c);
            }
            androidx.core.view.c0.N0(this, charSequence);
        }
    }

    private void l() {
        if (Build.VERSION.SDK_INT >= 30) {
            CharSequence charSequence = this.q;
            if (charSequence == null) {
                charSequence = getResources().getString(g.h.f19985d);
            }
            androidx.core.view.c0.N0(this, charSequence);
        }
    }

    private void o(int i8, int i9) {
        n(i8 != 1 ? i8 != 2 ? i8 != 3 ? null : Typeface.MONOSPACE : Typeface.SERIF : Typeface.SANS_SERIF, i9);
    }

    private void p() {
        if (this.f1342h0 == null && this.f1340g0.b() && androidx.emoji2.text.e.h()) {
            androidx.emoji2.text.e b9 = androidx.emoji2.text.e.b();
            int d8 = b9.d();
            if (d8 == 3 || d8 == 0) {
                c cVar = new c(this);
                this.f1342h0 = cVar;
                b9.s(cVar);
            }
        }
    }

    private void q(MotionEvent motionEvent) {
        this.f1354z = 0;
        boolean z4 = true;
        boolean z8 = motionEvent.getAction() == 1 && isEnabled();
        boolean isChecked = isChecked();
        if (z8) {
            this.E.computeCurrentVelocity(1000);
            float xVelocity = this.E.getXVelocity();
            if (Math.abs(xVelocity) <= this.F) {
                z4 = getTargetCheckedState();
            } else if (!u0.b(this) ? xVelocity <= 0.0f : xVelocity >= 0.0f) {
                z4 = false;
            }
        } else {
            z4 = isChecked;
        }
        if (z4 != isChecked) {
            playSoundEffect(0);
        }
        setChecked(z4);
        e(motionEvent);
    }

    private void setTextOffInternal(CharSequence charSequence) {
        this.f1351w = charSequence;
        this.f1352x = g(charSequence);
        this.f1332c0 = null;
        if (this.f1353y) {
            p();
        }
    }

    private void setTextOnInternal(CharSequence charSequence) {
        this.q = charSequence;
        this.f1350t = g(charSequence);
        this.f1330b0 = null;
        if (this.f1353y) {
            p();
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        int i8;
        int i9;
        Rect rect = this.f1343i0;
        int i10 = this.O;
        int i11 = this.P;
        int i12 = this.Q;
        int i13 = this.R;
        int thumbOffset = getThumbOffset() + i10;
        Drawable drawable = this.f1327a;
        Rect d8 = drawable != null ? t.d(drawable) : t.f1606c;
        Drawable drawable2 = this.f1337f;
        if (drawable2 != null) {
            drawable2.getPadding(rect);
            int i14 = rect.left;
            thumbOffset += i14;
            if (d8 != null) {
                int i15 = d8.left;
                if (i15 > i14) {
                    i10 += i15 - i14;
                }
                int i16 = d8.top;
                int i17 = rect.top;
                i8 = i16 > i17 ? (i16 - i17) + i11 : i11;
                int i18 = d8.right;
                int i19 = rect.right;
                if (i18 > i19) {
                    i12 -= i18 - i19;
                }
                int i20 = d8.bottom;
                int i21 = rect.bottom;
                if (i20 > i21) {
                    i9 = i13 - (i20 - i21);
                    this.f1337f.setBounds(i10, i8, i12, i9);
                }
            } else {
                i8 = i11;
            }
            i9 = i13;
            this.f1337f.setBounds(i10, i8, i12, i9);
        }
        Drawable drawable3 = this.f1327a;
        if (drawable3 != null) {
            drawable3.getPadding(rect);
            int i22 = thumbOffset - rect.left;
            int i23 = thumbOffset + this.L + rect.right;
            this.f1327a.setBounds(i22, i11, i23, i13);
            Drawable background = getBackground();
            if (background != null) {
                androidx.core.graphics.drawable.a.l(background, i22, i11, i23, i13);
            }
        }
        super.draw(canvas);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void drawableHotspotChanged(float f5, float f8) {
        if (Build.VERSION.SDK_INT >= 21) {
            super.drawableHotspotChanged(f5, f8);
        }
        Drawable drawable = this.f1327a;
        if (drawable != null) {
            androidx.core.graphics.drawable.a.k(drawable, f5, f8);
        }
        Drawable drawable2 = this.f1337f;
        if (drawable2 != null) {
            androidx.core.graphics.drawable.a.k(drawable2, f5, f8);
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        int[] drawableState = getDrawableState();
        Drawable drawable = this.f1327a;
        boolean z4 = false;
        if (drawable != null && drawable.isStateful()) {
            z4 = false | drawable.setState(drawableState);
        }
        Drawable drawable2 = this.f1337f;
        if (drawable2 != null && drawable2.isStateful()) {
            z4 |= drawable2.setState(drawableState);
        }
        if (z4) {
            invalidate();
        }
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingLeft() {
        if (u0.b(this)) {
            int compoundPaddingLeft = super.getCompoundPaddingLeft() + this.H;
            return !TextUtils.isEmpty(getText()) ? compoundPaddingLeft + this.f1348n : compoundPaddingLeft;
        }
        return super.getCompoundPaddingLeft();
    }

    @Override // android.widget.CompoundButton, android.widget.TextView
    public int getCompoundPaddingRight() {
        if (u0.b(this)) {
            return super.getCompoundPaddingRight();
        }
        int compoundPaddingRight = super.getCompoundPaddingRight() + this.H;
        return !TextUtils.isEmpty(getText()) ? compoundPaddingRight + this.f1348n : compoundPaddingRight;
    }

    @Override // android.widget.TextView
    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        return androidx.core.widget.k.s(super.getCustomSelectionActionModeCallback());
    }

    public boolean getShowText() {
        return this.f1353y;
    }

    public boolean getSplitTrack() {
        return this.f1349p;
    }

    public int getSwitchMinWidth() {
        return this.f1347m;
    }

    public int getSwitchPadding() {
        return this.f1348n;
    }

    public CharSequence getTextOff() {
        return this.f1351w;
    }

    public CharSequence getTextOn() {
        return this.q;
    }

    public Drawable getThumbDrawable() {
        return this.f1327a;
    }

    protected final float getThumbPosition() {
        return this.G;
    }

    public int getThumbTextPadding() {
        return this.f1346l;
    }

    public ColorStateList getThumbTintList() {
        return this.f1329b;
    }

    public PorterDuff.Mode getThumbTintMode() {
        return this.f1331c;
    }

    public Drawable getTrackDrawable() {
        return this.f1337f;
    }

    public ColorStateList getTrackTintList() {
        return this.f1339g;
    }

    public PorterDuff.Mode getTrackTintMode() {
        return this.f1341h;
    }

    void j() {
        setTextOnInternal(this.q);
        setTextOffInternal(this.f1351w);
        requestLayout();
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.f1327a;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        Drawable drawable2 = this.f1337f;
        if (drawable2 != null) {
            drawable2.jumpToCurrentState();
        }
        ObjectAnimator objectAnimator = this.f1336e0;
        if (objectAnimator == null || !objectAnimator.isStarted()) {
            return;
        }
        this.f1336e0.end();
        this.f1336e0 = null;
    }

    public void m(Context context, int i8) {
        j0 t8 = j0.t(context, i8, g.j.f20019c3);
        ColorStateList c9 = t8.c(g.j.f20041g3);
        if (c9 == null) {
            c9 = getTextColors();
        }
        this.f1328a0 = c9;
        int f5 = t8.f(g.j.f20025d3, 0);
        if (f5 != 0) {
            float f8 = f5;
            if (f8 != this.W.getTextSize()) {
                this.W.setTextSize(f8);
                requestLayout();
            }
        }
        o(t8.k(g.j.f20031e3, -1), t8.k(g.j.f20036f3, -1));
        this.f1334d0 = t8.a(g.j.f20076n3, false) ? new k.a(getContext()) : null;
        setTextOnInternal(this.q);
        setTextOffInternal(this.f1351w);
        t8.w();
    }

    public void n(Typeface typeface, int i8) {
        if (i8 <= 0) {
            this.W.setFakeBoldText(false);
            this.W.setTextSkewX(0.0f);
            setSwitchTypeface(typeface);
            return;
        }
        Typeface defaultFromStyle = typeface == null ? Typeface.defaultFromStyle(i8) : Typeface.create(typeface, i8);
        setSwitchTypeface(defaultFromStyle);
        int i9 = (~(defaultFromStyle != null ? defaultFromStyle.getStyle() : 0)) & i8;
        this.W.setFakeBoldText((i9 & 1) != 0);
        this.W.setTextSkewX((i9 & 2) != 0 ? -0.25f : 0.0f);
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected int[] onCreateDrawableState(int i8) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i8 + 1);
        if (isChecked()) {
            CompoundButton.mergeDrawableStates(onCreateDrawableState, f1326k0);
        }
        return onCreateDrawableState;
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected void onDraw(Canvas canvas) {
        int width;
        super.onDraw(canvas);
        Rect rect = this.f1343i0;
        Drawable drawable = this.f1337f;
        if (drawable != null) {
            drawable.getPadding(rect);
        } else {
            rect.setEmpty();
        }
        int i8 = this.P;
        int i9 = this.R;
        int i10 = i8 + rect.top;
        int i11 = i9 - rect.bottom;
        Drawable drawable2 = this.f1327a;
        if (drawable != null) {
            if (!this.f1349p || drawable2 == null) {
                drawable.draw(canvas);
            } else {
                Rect d8 = t.d(drawable2);
                drawable2.copyBounds(rect);
                rect.left += d8.left;
                rect.right -= d8.right;
                int save = canvas.save();
                canvas.clipRect(rect, Region.Op.DIFFERENCE);
                drawable.draw(canvas);
                canvas.restoreToCount(save);
            }
        }
        int save2 = canvas.save();
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
        Layout layout = getTargetCheckedState() ? this.f1330b0 : this.f1332c0;
        if (layout != null) {
            int[] drawableState = getDrawableState();
            ColorStateList colorStateList = this.f1328a0;
            if (colorStateList != null) {
                this.W.setColor(colorStateList.getColorForState(drawableState, 0));
            }
            this.W.drawableState = drawableState;
            if (drawable2 != null) {
                Rect bounds = drawable2.getBounds();
                width = bounds.left + bounds.right;
            } else {
                width = getWidth();
            }
            canvas.translate((width / 2) - (layout.getWidth() / 2), ((i10 + i11) / 2) - (layout.getHeight() / 2));
            layout.draw(canvas);
        }
        canvas.restoreToCount(save2);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName("android.widget.Switch");
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName("android.widget.Switch");
        if (Build.VERSION.SDK_INT < 30) {
            CharSequence charSequence = isChecked() ? this.q : this.f1351w;
            if (TextUtils.isEmpty(charSequence)) {
                return;
            }
            CharSequence text = accessibilityNodeInfo.getText();
            if (TextUtils.isEmpty(text)) {
                accessibilityNodeInfo.setText(charSequence);
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(text);
            sb.append(' ');
            sb.append(charSequence);
            accessibilityNodeInfo.setText(sb);
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        int i12;
        int width;
        int i13;
        int i14;
        int paddingTop;
        int i15;
        super.onLayout(z4, i8, i9, i10, i11);
        int i16 = 0;
        if (this.f1327a != null) {
            Rect rect = this.f1343i0;
            Drawable drawable = this.f1337f;
            if (drawable != null) {
                drawable.getPadding(rect);
            } else {
                rect.setEmpty();
            }
            Rect d8 = t.d(this.f1327a);
            i12 = Math.max(0, d8.left - rect.left);
            i16 = Math.max(0, d8.right - rect.right);
        } else {
            i12 = 0;
        }
        if (u0.b(this)) {
            i13 = getPaddingLeft() + i12;
            width = ((this.H + i13) - i12) - i16;
        } else {
            width = (getWidth() - getPaddingRight()) - i16;
            i13 = (width - this.H) + i12 + i16;
        }
        int gravity = getGravity() & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle;
        if (gravity == 16) {
            i14 = this.K;
            paddingTop = (((getPaddingTop() + getHeight()) - getPaddingBottom()) / 2) - (i14 / 2);
        } else if (gravity == 80) {
            i15 = getHeight() - getPaddingBottom();
            paddingTop = i15 - this.K;
            this.O = i13;
            this.P = paddingTop;
            this.R = i15;
            this.Q = width;
        } else {
            paddingTop = getPaddingTop();
            i14 = this.K;
        }
        i15 = i14 + paddingTop;
        this.O = i13;
        this.P = paddingTop;
        this.R = i15;
        this.Q = width;
    }

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i8, int i9) {
        int i10;
        int i11;
        if (this.f1353y) {
            if (this.f1330b0 == null) {
                this.f1330b0 = i(this.f1350t);
            }
            if (this.f1332c0 == null) {
                this.f1332c0 = i(this.f1352x);
            }
        }
        Rect rect = this.f1343i0;
        Drawable drawable = this.f1327a;
        int i12 = 0;
        if (drawable != null) {
            drawable.getPadding(rect);
            i10 = (this.f1327a.getIntrinsicWidth() - rect.left) - rect.right;
            i11 = this.f1327a.getIntrinsicHeight();
        } else {
            i10 = 0;
            i11 = 0;
        }
        this.L = Math.max(this.f1353y ? Math.max(this.f1330b0.getWidth(), this.f1332c0.getWidth()) + (this.f1346l * 2) : 0, i10);
        Drawable drawable2 = this.f1337f;
        if (drawable2 != null) {
            drawable2.getPadding(rect);
            i12 = this.f1337f.getIntrinsicHeight();
        } else {
            rect.setEmpty();
        }
        int i13 = rect.left;
        int i14 = rect.right;
        Drawable drawable3 = this.f1327a;
        if (drawable3 != null) {
            Rect d8 = t.d(drawable3);
            i13 = Math.max(i13, d8.left);
            i14 = Math.max(i14, d8.right);
        }
        int max = this.T ? Math.max(this.f1347m, (this.L * 2) + i13 + i14) : this.f1347m;
        int max2 = Math.max(i12, i11);
        this.H = max;
        this.K = max2;
        super.onMeasure(i8, i9);
        if (getMeasuredHeight() < max2) {
            setMeasuredDimension(getMeasuredWidthAndState(), max2);
        }
    }

    @Override // android.view.View
    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        CharSequence charSequence = isChecked() ? this.q : this.f1351w;
        if (charSequence != null) {
            accessibilityEvent.getText().add(charSequence);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0012, code lost:
        if (r0 != 3) goto L8;
     */
    @Override // android.widget.TextView, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean onTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            android.view.VelocityTracker r0 = r6.E
            r0.addMovement(r7)
            int r0 = r7.getActionMasked()
            r1 = 1
            if (r0 == 0) goto L9d
            r2 = 2
            if (r0 == r1) goto L89
            if (r0 == r2) goto L16
            r3 = 3
            if (r0 == r3) goto L89
            goto Lb7
        L16:
            int r0 = r6.f1354z
            if (r0 == r1) goto L55
            if (r0 == r2) goto L1e
            goto Lb7
        L1e:
            float r7 = r7.getX()
            int r0 = r6.getThumbScrollRange()
            float r2 = r6.B
            float r2 = r7 - r2
            r3 = 1065353216(0x3f800000, float:1.0)
            r4 = 0
            if (r0 == 0) goto L32
            float r0 = (float) r0
            float r2 = r2 / r0
            goto L3b
        L32:
            int r0 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r0 <= 0) goto L38
            r2 = r3
            goto L3b
        L38:
            r0 = -1082130432(0xffffffffbf800000, float:-1.0)
            r2 = r0
        L3b:
            boolean r0 = androidx.appcompat.widget.u0.b(r6)
            if (r0 == 0) goto L42
            float r2 = -r2
        L42:
            float r0 = r6.G
            float r0 = r0 + r2
            float r0 = f(r0, r4, r3)
            float r2 = r6.G
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 == 0) goto L54
            r6.B = r7
            r6.setThumbPosition(r0)
        L54:
            return r1
        L55:
            float r0 = r7.getX()
            float r3 = r7.getY()
            float r4 = r6.B
            float r4 = r0 - r4
            float r4 = java.lang.Math.abs(r4)
            int r5 = r6.A
            float r5 = (float) r5
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 > 0) goto L7b
            float r4 = r6.C
            float r4 = r3 - r4
            float r4 = java.lang.Math.abs(r4)
            int r5 = r6.A
            float r5 = (float) r5
            int r4 = (r4 > r5 ? 1 : (r4 == r5 ? 0 : -1))
            if (r4 <= 0) goto Lb7
        L7b:
            r6.f1354z = r2
            android.view.ViewParent r7 = r6.getParent()
            r7.requestDisallowInterceptTouchEvent(r1)
            r6.B = r0
            r6.C = r3
            return r1
        L89:
            int r0 = r6.f1354z
            if (r0 != r2) goto L94
            r6.q(r7)
            super.onTouchEvent(r7)
            return r1
        L94:
            r0 = 0
            r6.f1354z = r0
            android.view.VelocityTracker r0 = r6.E
            r0.clear()
            goto Lb7
        L9d:
            float r0 = r7.getX()
            float r2 = r7.getY()
            boolean r3 = r6.isEnabled()
            if (r3 == 0) goto Lb7
            boolean r3 = r6.h(r0, r2)
            if (r3 == 0) goto Lb7
            r6.f1354z = r1
            r6.B = r0
            r6.C = r2
        Lb7:
            boolean r7 = super.onTouchEvent(r7)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.SwitchCompat.onTouchEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean z4) {
        super.setAllCaps(z4);
        getEmojiTextViewHelper().d(z4);
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z4) {
        super.setChecked(z4);
        boolean isChecked = isChecked();
        if (isChecked) {
            l();
        } else {
            k();
        }
        if (getWindowToken() != null && androidx.core.view.c0.W(this)) {
            a(isChecked);
            return;
        }
        d();
        setThumbPosition(isChecked ? 1.0f : 0.0f);
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(androidx.core.widget.k.t(this, callback));
    }

    public void setEmojiCompatEnabled(boolean z4) {
        getEmojiTextViewHelper().e(z4);
        setTextOnInternal(this.q);
        setTextOffInternal(this.f1351w);
        requestLayout();
    }

    protected final void setEnforceSwitchWidth(boolean z4) {
        this.T = z4;
        invalidate();
    }

    @Override // android.widget.TextView
    public void setFilters(InputFilter[] inputFilterArr) {
        super.setFilters(getEmojiTextViewHelper().a(inputFilterArr));
    }

    public void setShowText(boolean z4) {
        if (this.f1353y != z4) {
            this.f1353y = z4;
            requestLayout();
            if (z4) {
                p();
            }
        }
    }

    public void setSplitTrack(boolean z4) {
        this.f1349p = z4;
        invalidate();
    }

    public void setSwitchMinWidth(int i8) {
        this.f1347m = i8;
        requestLayout();
    }

    public void setSwitchPadding(int i8) {
        this.f1348n = i8;
        requestLayout();
    }

    public void setSwitchTypeface(Typeface typeface) {
        if ((this.W.getTypeface() == null || this.W.getTypeface().equals(typeface)) && (this.W.getTypeface() != null || typeface == null)) {
            return;
        }
        this.W.setTypeface(typeface);
        requestLayout();
        invalidate();
    }

    public void setTextOff(CharSequence charSequence) {
        setTextOffInternal(charSequence);
        requestLayout();
        if (isChecked()) {
            return;
        }
        k();
    }

    public void setTextOn(CharSequence charSequence) {
        setTextOnInternal(charSequence);
        requestLayout();
        if (isChecked()) {
            l();
        }
    }

    public void setThumbDrawable(Drawable drawable) {
        Drawable drawable2 = this.f1327a;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.f1327a = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        requestLayout();
    }

    void setThumbPosition(float f5) {
        this.G = f5;
        invalidate();
    }

    public void setThumbResource(int i8) {
        setThumbDrawable(h.a.b(getContext(), i8));
    }

    public void setThumbTextPadding(int i8) {
        this.f1346l = i8;
        requestLayout();
    }

    public void setThumbTintList(ColorStateList colorStateList) {
        this.f1329b = colorStateList;
        this.f1333d = true;
        b();
    }

    public void setThumbTintMode(PorterDuff.Mode mode) {
        this.f1331c = mode;
        this.f1335e = true;
        b();
    }

    public void setTrackDrawable(Drawable drawable) {
        Drawable drawable2 = this.f1337f;
        if (drawable2 != null) {
            drawable2.setCallback(null);
        }
        this.f1337f = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
        requestLayout();
    }

    public void setTrackResource(int i8) {
        setTrackDrawable(h.a.b(getContext(), i8));
    }

    public void setTrackTintList(ColorStateList colorStateList) {
        this.f1339g = colorStateList;
        this.f1344j = true;
        c();
    }

    public void setTrackTintMode(PorterDuff.Mode mode) {
        this.f1341h = mode;
        this.f1345k = true;
        c();
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void toggle() {
        setChecked(!isChecked());
    }

    @Override // android.widget.CompoundButton, android.widget.TextView, android.view.View
    protected boolean verifyDrawable(Drawable drawable) {
        return super.verifyDrawable(drawable) || drawable == this.f1327a || drawable == this.f1337f;
    }
}
