package com.google.android.material.badge;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.core.view.c0;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.material.internal.j;
import com.google.android.material.internal.m;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import k7.b;
import k7.f;
import k7.i;
import k7.k;
import k7.l;
import u7.c;
import u7.d;
import x7.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class BadgeDrawable extends Drawable implements j.b {

    /* renamed from: w  reason: collision with root package name */
    private static final int f17422w = k.f21246r;

    /* renamed from: x  reason: collision with root package name */
    private static final int f17423x = b.f21050c;

    /* renamed from: a  reason: collision with root package name */
    private final WeakReference<Context> f17424a;

    /* renamed from: b  reason: collision with root package name */
    private final h f17425b;

    /* renamed from: c  reason: collision with root package name */
    private final j f17426c;

    /* renamed from: d  reason: collision with root package name */
    private final Rect f17427d;

    /* renamed from: e  reason: collision with root package name */
    private final float f17428e;

    /* renamed from: f  reason: collision with root package name */
    private final float f17429f;

    /* renamed from: g  reason: collision with root package name */
    private final float f17430g;

    /* renamed from: h  reason: collision with root package name */
    private final SavedState f17431h;

    /* renamed from: j  reason: collision with root package name */
    private float f17432j;

    /* renamed from: k  reason: collision with root package name */
    private float f17433k;

    /* renamed from: l  reason: collision with root package name */
    private int f17434l;

    /* renamed from: m  reason: collision with root package name */
    private float f17435m;

    /* renamed from: n  reason: collision with root package name */
    private float f17436n;

    /* renamed from: p  reason: collision with root package name */
    private float f17437p;
    private WeakReference<View> q;

    /* renamed from: t  reason: collision with root package name */
    private WeakReference<FrameLayout> f17438t;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        private int f17439a;

        /* renamed from: b  reason: collision with root package name */
        private int f17440b;

        /* renamed from: c  reason: collision with root package name */
        private int f17441c;

        /* renamed from: d  reason: collision with root package name */
        private int f17442d;

        /* renamed from: e  reason: collision with root package name */
        private int f17443e;

        /* renamed from: f  reason: collision with root package name */
        private CharSequence f17444f;

        /* renamed from: g  reason: collision with root package name */
        private int f17445g;

        /* renamed from: h  reason: collision with root package name */
        private int f17446h;

        /* renamed from: j  reason: collision with root package name */
        private int f17447j;

        /* renamed from: k  reason: collision with root package name */
        private boolean f17448k;

        /* renamed from: l  reason: collision with root package name */
        private int f17449l;

        /* renamed from: m  reason: collision with root package name */
        private int f17450m;

        /* renamed from: n  reason: collision with root package name */
        private int f17451n;

        /* renamed from: p  reason: collision with root package name */
        private int f17452p;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        public SavedState(Context context) {
            this.f17441c = 255;
            this.f17442d = -1;
            this.f17440b = new d(context, k.f21234e).f23093a.getDefaultColor();
            this.f17444f = context.getString(k7.j.f21215k);
            this.f17445g = i.f21204a;
            this.f17446h = k7.j.f21217m;
            this.f17448k = true;
        }

        protected SavedState(Parcel parcel) {
            this.f17441c = 255;
            this.f17442d = -1;
            this.f17439a = parcel.readInt();
            this.f17440b = parcel.readInt();
            this.f17441c = parcel.readInt();
            this.f17442d = parcel.readInt();
            this.f17443e = parcel.readInt();
            this.f17444f = parcel.readString();
            this.f17445g = parcel.readInt();
            this.f17447j = parcel.readInt();
            this.f17449l = parcel.readInt();
            this.f17450m = parcel.readInt();
            this.f17451n = parcel.readInt();
            this.f17452p = parcel.readInt();
            this.f17448k = parcel.readInt() != 0;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeInt(this.f17439a);
            parcel.writeInt(this.f17440b);
            parcel.writeInt(this.f17441c);
            parcel.writeInt(this.f17442d);
            parcel.writeInt(this.f17443e);
            parcel.writeString(this.f17444f.toString());
            parcel.writeInt(this.f17445g);
            parcel.writeInt(this.f17447j);
            parcel.writeInt(this.f17449l);
            parcel.writeInt(this.f17450m);
            parcel.writeInt(this.f17451n);
            parcel.writeInt(this.f17452p);
            parcel.writeInt(this.f17448k ? 1 : 0);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ View f17453a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ FrameLayout f17454b;

        a(View view, FrameLayout frameLayout) {
            this.f17453a = view;
            this.f17454b = frameLayout;
        }

        @Override // java.lang.Runnable
        public void run() {
            BadgeDrawable.this.F(this.f17453a, this.f17454b);
        }
    }

    private BadgeDrawable(Context context) {
        this.f17424a = new WeakReference<>(context);
        m.c(context);
        Resources resources = context.getResources();
        this.f17427d = new Rect();
        this.f17425b = new h();
        this.f17428e = resources.getDimensionPixelSize(k7.d.K);
        this.f17430g = resources.getDimensionPixelSize(k7.d.J);
        this.f17429f = resources.getDimensionPixelSize(k7.d.M);
        j jVar = new j(this);
        this.f17426c = jVar;
        jVar.e().setTextAlign(Paint.Align.CENTER);
        this.f17431h = new SavedState(context);
        A(k.f21234e);
    }

    private void A(int i8) {
        Context context = this.f17424a.get();
        if (context == null) {
            return;
        }
        z(new d(context, i8));
    }

    private void D(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        if (viewGroup == null || viewGroup.getId() != f.f21172v) {
            WeakReference<FrameLayout> weakReference = this.f17438t;
            if (weakReference == null || weakReference.get() != viewGroup) {
                E(view);
                FrameLayout frameLayout = new FrameLayout(view.getContext());
                frameLayout.setId(f.f21172v);
                frameLayout.setClipChildren(false);
                frameLayout.setClipToPadding(false);
                frameLayout.setLayoutParams(view.getLayoutParams());
                frameLayout.setMinimumWidth(view.getWidth());
                frameLayout.setMinimumHeight(view.getHeight());
                int indexOfChild = viewGroup.indexOfChild(view);
                viewGroup.removeViewAt(indexOfChild);
                view.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
                frameLayout.addView(view);
                viewGroup.addView(frameLayout, indexOfChild);
                this.f17438t = new WeakReference<>(frameLayout);
                frameLayout.post(new a(view, frameLayout));
            }
        }
    }

    private static void E(View view) {
        ViewGroup viewGroup = (ViewGroup) view.getParent();
        viewGroup.setClipChildren(false);
        viewGroup.setClipToPadding(false);
    }

    private void G() {
        Context context = this.f17424a.get();
        WeakReference<View> weakReference = this.q;
        View view = weakReference != null ? weakReference.get() : null;
        if (context == null || view == null) {
            return;
        }
        Rect rect = new Rect();
        rect.set(this.f17427d);
        Rect rect2 = new Rect();
        view.getDrawingRect(rect2);
        WeakReference<FrameLayout> weakReference2 = this.f17438t;
        FrameLayout frameLayout = weakReference2 != null ? weakReference2.get() : null;
        if (frameLayout != null || com.google.android.material.badge.a.f17456a) {
            if (frameLayout == null) {
                frameLayout = (ViewGroup) view.getParent();
            }
            frameLayout.offsetDescendantRectToMyCoords(view, rect2);
        }
        b(context, rect2, view);
        com.google.android.material.badge.a.f(this.f17427d, this.f17432j, this.f17433k, this.f17436n, this.f17437p);
        this.f17425b.X(this.f17435m);
        if (rect.equals(this.f17427d)) {
            return;
        }
        this.f17425b.setBounds(this.f17427d);
    }

    private void H() {
        this.f17434l = ((int) Math.pow(10.0d, k() - 1.0d)) - 1;
    }

    private void b(Context context, Rect rect, View view) {
        float f5;
        int i8 = this.f17431h.f17450m + this.f17431h.f17452p;
        int i9 = this.f17431h.f17447j;
        this.f17433k = (i9 == 8388691 || i9 == 8388693) ? rect.bottom - i8 : rect.top + i8;
        if (l() <= 9) {
            f5 = !n() ? this.f17428e : this.f17429f;
            this.f17435m = f5;
            this.f17437p = f5;
        } else {
            float f8 = this.f17429f;
            this.f17435m = f8;
            this.f17437p = f8;
            f5 = (this.f17426c.f(g()) / 2.0f) + this.f17430g;
        }
        this.f17436n = f5;
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(n() ? k7.d.L : k7.d.I);
        int i10 = this.f17431h.f17449l + this.f17431h.f17451n;
        int i11 = this.f17431h.f17447j;
        this.f17432j = (i11 == 8388659 || i11 == 8388691 ? c0.E(view) != 0 : c0.E(view) == 0) ? ((rect.right + this.f17436n) - dimensionPixelSize) - i10 : (rect.left - this.f17436n) + dimensionPixelSize + i10;
    }

    public static BadgeDrawable c(Context context) {
        return d(context, null, f17423x, f17422w);
    }

    private static BadgeDrawable d(Context context, AttributeSet attributeSet, int i8, int i9) {
        BadgeDrawable badgeDrawable = new BadgeDrawable(context);
        badgeDrawable.o(context, attributeSet, i8, i9);
        return badgeDrawable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static BadgeDrawable e(Context context, SavedState savedState) {
        BadgeDrawable badgeDrawable = new BadgeDrawable(context);
        badgeDrawable.q(savedState);
        return badgeDrawable;
    }

    private void f(Canvas canvas) {
        Rect rect = new Rect();
        String g8 = g();
        this.f17426c.e().getTextBounds(g8, 0, g8.length(), rect);
        canvas.drawText(g8, this.f17432j, this.f17433k + (rect.height() / 2), this.f17426c.e());
    }

    private String g() {
        if (l() <= this.f17434l) {
            return NumberFormat.getInstance().format(l());
        }
        Context context = this.f17424a.get();
        return context == null ? BuildConfig.FLAVOR : context.getString(k7.j.f21218n, Integer.valueOf(this.f17434l), "+");
    }

    private void o(Context context, AttributeSet attributeSet, int i8, int i9) {
        TypedArray h8 = m.h(context, attributeSet, l.C, i8, i9, new int[0]);
        x(h8.getInt(l.H, 4));
        int i10 = l.I;
        if (h8.hasValue(i10)) {
            y(h8.getInt(i10, 0));
        }
        t(p(context, h8, l.D));
        int i11 = l.F;
        if (h8.hasValue(i11)) {
            v(p(context, h8, i11));
        }
        u(h8.getInt(l.E, 8388661));
        w(h8.getDimensionPixelOffset(l.G, 0));
        B(h8.getDimensionPixelOffset(l.J, 0));
        h8.recycle();
    }

    private static int p(Context context, TypedArray typedArray, int i8) {
        return c.a(context, typedArray, i8).getDefaultColor();
    }

    private void q(SavedState savedState) {
        x(savedState.f17443e);
        if (savedState.f17442d != -1) {
            y(savedState.f17442d);
        }
        t(savedState.f17439a);
        v(savedState.f17440b);
        u(savedState.f17447j);
        w(savedState.f17449l);
        B(savedState.f17450m);
        r(savedState.f17451n);
        s(savedState.f17452p);
        C(savedState.f17448k);
    }

    private void z(d dVar) {
        Context context;
        if (this.f17426c.d() == dVar || (context = this.f17424a.get()) == null) {
            return;
        }
        this.f17426c.h(dVar, context);
        G();
    }

    public void B(int i8) {
        this.f17431h.f17450m = i8;
        G();
    }

    public void C(boolean z4) {
        setVisible(z4, false);
        this.f17431h.f17448k = z4;
        if (!com.google.android.material.badge.a.f17456a || i() == null || z4) {
            return;
        }
        ((ViewGroup) i().getParent()).invalidate();
    }

    public void F(View view, FrameLayout frameLayout) {
        this.q = new WeakReference<>(view);
        boolean z4 = com.google.android.material.badge.a.f17456a;
        if (z4 && frameLayout == null) {
            D(view);
        } else {
            this.f17438t = new WeakReference<>(frameLayout);
        }
        if (!z4) {
            E(view);
        }
        G();
        invalidateSelf();
    }

    @Override // com.google.android.material.internal.j.b
    public void a() {
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (getBounds().isEmpty() || getAlpha() == 0 || !isVisible()) {
            return;
        }
        this.f17425b.draw(canvas);
        if (n()) {
            f(canvas);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public int getAlpha() {
        return this.f17431h.f17441c;
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicHeight() {
        return this.f17427d.height();
    }

    @Override // android.graphics.drawable.Drawable
    public int getIntrinsicWidth() {
        return this.f17427d.width();
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    public CharSequence h() {
        Context context;
        if (isVisible()) {
            if (n()) {
                if (this.f17431h.f17445g <= 0 || (context = this.f17424a.get()) == null) {
                    return null;
                }
                return l() <= this.f17434l ? context.getResources().getQuantityString(this.f17431h.f17445g, l(), Integer.valueOf(l())) : context.getString(this.f17431h.f17446h, Integer.valueOf(this.f17434l));
            }
            return this.f17431h.f17444f;
        }
        return null;
    }

    public FrameLayout i() {
        WeakReference<FrameLayout> weakReference = this.f17438t;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return false;
    }

    public int j() {
        return this.f17431h.f17449l;
    }

    public int k() {
        return this.f17431h.f17443e;
    }

    public int l() {
        if (n()) {
            return this.f17431h.f17442d;
        }
        return 0;
    }

    public SavedState m() {
        return this.f17431h;
    }

    public boolean n() {
        return this.f17431h.f17442d != -1;
    }

    @Override // android.graphics.drawable.Drawable, com.google.android.material.internal.j.b
    public boolean onStateChange(int[] iArr) {
        return super.onStateChange(iArr);
    }

    void r(int i8) {
        this.f17431h.f17451n = i8;
        G();
    }

    void s(int i8) {
        this.f17431h.f17452p = i8;
        G();
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i8) {
        this.f17431h.f17441c = i8;
        this.f17426c.e().setAlpha(i8);
        invalidateSelf();
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void t(int i8) {
        this.f17431h.f17439a = i8;
        ColorStateList valueOf = ColorStateList.valueOf(i8);
        if (this.f17425b.x() != valueOf) {
            this.f17425b.a0(valueOf);
            invalidateSelf();
        }
    }

    public void u(int i8) {
        if (this.f17431h.f17447j != i8) {
            this.f17431h.f17447j = i8;
            WeakReference<View> weakReference = this.q;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            View view = this.q.get();
            WeakReference<FrameLayout> weakReference2 = this.f17438t;
            F(view, weakReference2 != null ? weakReference2.get() : null);
        }
    }

    public void v(int i8) {
        this.f17431h.f17440b = i8;
        if (this.f17426c.e().getColor() != i8) {
            this.f17426c.e().setColor(i8);
            invalidateSelf();
        }
    }

    public void w(int i8) {
        this.f17431h.f17449l = i8;
        G();
    }

    public void x(int i8) {
        if (this.f17431h.f17443e != i8) {
            this.f17431h.f17443e = i8;
            H();
            this.f17426c.i(true);
            G();
            invalidateSelf();
        }
    }

    public void y(int i8) {
        int max = Math.max(0, i8);
        if (this.f17431h.f17442d != max) {
            this.f17431h.f17442d = max;
            this.f17426c.i(true);
            G();
            invalidateSelf();
        }
    }
}
