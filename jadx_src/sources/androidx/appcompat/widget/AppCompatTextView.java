package androidx.appcompat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.textclassifier.TextClassifier;
import android.widget.TextView;
import androidx.core.text.c;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppCompatTextView extends TextView implements androidx.core.view.a0, androidx.core.widget.o {

    /* renamed from: a  reason: collision with root package name */
    private final d f1204a;

    /* renamed from: b  reason: collision with root package name */
    private final p f1205b;

    /* renamed from: c  reason: collision with root package name */
    private final o f1206c;

    /* renamed from: d  reason: collision with root package name */
    private i f1207d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f1208e;

    /* renamed from: f  reason: collision with root package name */
    private a f1209f;

    /* renamed from: g  reason: collision with root package name */
    private Future<androidx.core.text.c> f1210g;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(int[] iArr, int i8);

        int[] b();

        TextClassifier c();

        int d();

        void e(TextClassifier textClassifier);

        void f(int i8);

        void g(int i8, int i9, int i10, int i11);

        int h();

        int i();

        void j(int i8);

        int k();

        void l(int i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b implements a {
        b() {
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public void a(int[] iArr, int i8) {
            AppCompatTextView.super.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i8);
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public int[] b() {
            return AppCompatTextView.super.getAutoSizeTextAvailableSizes();
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public TextClassifier c() {
            return AppCompatTextView.super.getTextClassifier();
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public int d() {
            return AppCompatTextView.super.getAutoSizeMaxTextSize();
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public void e(TextClassifier textClassifier) {
            AppCompatTextView.super.setTextClassifier(textClassifier);
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public void f(int i8) {
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public void g(int i8, int i9, int i10, int i11) {
            AppCompatTextView.super.setAutoSizeTextTypeUniformWithConfiguration(i8, i9, i10, i11);
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public int h() {
            return AppCompatTextView.super.getAutoSizeTextType();
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public int i() {
            return AppCompatTextView.super.getAutoSizeMinTextSize();
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public void j(int i8) {
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public int k() {
            return AppCompatTextView.super.getAutoSizeStepGranularity();
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.a
        public void l(int i8) {
            AppCompatTextView.super.setAutoSizeTextTypeWithDefaults(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends b {
        c() {
            super();
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.b, androidx.appcompat.widget.AppCompatTextView.a
        public void f(int i8) {
            AppCompatTextView.super.setLastBaselineToBottomHeight(i8);
        }

        @Override // androidx.appcompat.widget.AppCompatTextView.b, androidx.appcompat.widget.AppCompatTextView.a
        public void j(int i8) {
            AppCompatTextView.super.setFirstBaselineToTopHeight(i8);
        }
    }

    public AppCompatTextView(Context context) {
        this(context, null);
    }

    public AppCompatTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842884);
    }

    public AppCompatTextView(Context context, AttributeSet attributeSet, int i8) {
        super(g0.b(context), attributeSet, i8);
        this.f1208e = false;
        this.f1209f = null;
        e0.a(this, getContext());
        d dVar = new d(this);
        this.f1204a = dVar;
        dVar.e(attributeSet, i8);
        p pVar = new p(this);
        this.f1205b = pVar;
        pVar.m(attributeSet, i8);
        pVar.b();
        this.f1206c = new o(this);
        getEmojiTextViewHelper().c(attributeSet, i8);
    }

    private i getEmojiTextViewHelper() {
        if (this.f1207d == null) {
            this.f1207d = new i(this);
        }
        return this.f1207d;
    }

    private void q() {
        Future<androidx.core.text.c> future = this.f1210g;
        if (future != null) {
            try {
                this.f1210g = null;
                androidx.core.widget.k.p(this, future.get());
            } catch (InterruptedException | ExecutionException unused) {
            }
        }
    }

    @Override // android.widget.TextView, android.view.View
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        d dVar = this.f1204a;
        if (dVar != null) {
            dVar.b();
        }
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.b();
        }
    }

    @Override // android.widget.TextView
    public int getAutoSizeMaxTextSize() {
        if (u0.f1636b) {
            return getSuperCaller().d();
        }
        p pVar = this.f1205b;
        if (pVar != null) {
            return pVar.e();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int getAutoSizeMinTextSize() {
        if (u0.f1636b) {
            return getSuperCaller().i();
        }
        p pVar = this.f1205b;
        if (pVar != null) {
            return pVar.f();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int getAutoSizeStepGranularity() {
        if (u0.f1636b) {
            return getSuperCaller().k();
        }
        p pVar = this.f1205b;
        if (pVar != null) {
            return pVar.g();
        }
        return -1;
    }

    @Override // android.widget.TextView
    public int[] getAutoSizeTextAvailableSizes() {
        if (u0.f1636b) {
            return getSuperCaller().b();
        }
        p pVar = this.f1205b;
        return pVar != null ? pVar.h() : new int[0];
    }

    @Override // android.widget.TextView
    @SuppressLint({"WrongConstant"})
    public int getAutoSizeTextType() {
        if (u0.f1636b) {
            return getSuperCaller().h() == 1 ? 1 : 0;
        }
        p pVar = this.f1205b;
        if (pVar != null) {
            return pVar.i();
        }
        return 0;
    }

    @Override // android.widget.TextView
    public ActionMode.Callback getCustomSelectionActionModeCallback() {
        return androidx.core.widget.k.s(super.getCustomSelectionActionModeCallback());
    }

    @Override // android.widget.TextView
    public int getFirstBaselineToTopHeight() {
        return androidx.core.widget.k.b(this);
    }

    @Override // android.widget.TextView
    public int getLastBaselineToBottomHeight() {
        return androidx.core.widget.k.c(this);
    }

    a getSuperCaller() {
        a bVar;
        if (this.f1209f == null) {
            int i8 = Build.VERSION.SDK_INT;
            if (i8 >= 28) {
                bVar = new c();
            } else if (i8 >= 26) {
                bVar = new b();
            }
            this.f1209f = bVar;
        }
        return this.f1209f;
    }

    @Override // androidx.core.view.a0
    public ColorStateList getSupportBackgroundTintList() {
        d dVar = this.f1204a;
        if (dVar != null) {
            return dVar.c();
        }
        return null;
    }

    @Override // androidx.core.view.a0
    public PorterDuff.Mode getSupportBackgroundTintMode() {
        d dVar = this.f1204a;
        if (dVar != null) {
            return dVar.d();
        }
        return null;
    }

    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.f1205b.j();
    }

    public PorterDuff.Mode getSupportCompoundDrawablesTintMode() {
        return this.f1205b.k();
    }

    @Override // android.widget.TextView
    public CharSequence getText() {
        q();
        return super.getText();
    }

    @Override // android.widget.TextView
    public TextClassifier getTextClassifier() {
        o oVar;
        return (Build.VERSION.SDK_INT >= 28 || (oVar = this.f1206c) == null) ? getSuperCaller().c() : oVar.a();
    }

    public c.a getTextMetricsParamsCompat() {
        return androidx.core.widget.k.g(this);
    }

    @Override // android.widget.TextView, android.view.View
    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
        this.f1205b.r(this, onCreateInputConnection, editorInfo);
        return j.a(onCreateInputConnection, editorInfo, this);
    }

    @Override // android.widget.TextView, android.view.View
    protected void onLayout(boolean z4, int i8, int i9, int i10, int i11) {
        super.onLayout(z4, i8, i9, i10, i11);
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.o(z4, i8, i9, i10, i11);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i8, int i9) {
        q();
        super.onMeasure(i8, i9);
    }

    @Override // android.widget.TextView
    protected void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
        super.onTextChanged(charSequence, i8, i9, i10);
        p pVar = this.f1205b;
        if ((pVar == null || u0.f1636b || !pVar.l()) ? false : true) {
            this.f1205b.c();
        }
    }

    @Override // android.widget.TextView
    public void setAllCaps(boolean z4) {
        super.setAllCaps(z4);
        getEmojiTextViewHelper().d(z4);
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeUniformWithConfiguration(int i8, int i9, int i10, int i11) {
        if (u0.f1636b) {
            getSuperCaller().g(i8, i9, i10, i11);
            return;
        }
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.t(i8, i9, i10, i11);
        }
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeUniformWithPresetSizes(int[] iArr, int i8) {
        if (u0.f1636b) {
            getSuperCaller().a(iArr, i8);
            return;
        }
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.u(iArr, i8);
        }
    }

    @Override // android.widget.TextView
    public void setAutoSizeTextTypeWithDefaults(int i8) {
        if (u0.f1636b) {
            getSuperCaller().l(i8);
            return;
        }
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.v(i8);
        }
    }

    @Override // android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        d dVar = this.f1204a;
        if (dVar != null) {
            dVar.f(drawable);
        }
    }

    @Override // android.view.View
    public void setBackgroundResource(int i8) {
        super.setBackgroundResource(i8);
        d dVar = this.f1204a;
        if (dVar != null) {
            dVar.g(i8);
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int i8, int i9, int i10, int i11) {
        Context context = getContext();
        setCompoundDrawablesRelativeWithIntrinsicBounds(i8 != 0 ? h.a.b(context, i8) : null, i9 != 0 ? h.a.b(context, i9) : null, i10 != 0 ? h.a.b(context, i10) : null, i11 != 0 ? h.a.b(context, i11) : null);
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(int i8, int i9, int i10, int i11) {
        Context context = getContext();
        setCompoundDrawablesWithIntrinsicBounds(i8 != 0 ? h.a.b(context, i8) : null, i9 != 0 ? h.a.b(context, i9) : null, i10 != 0 ? h.a.b(context, i10) : null, i11 != 0 ? h.a.b(context, i11) : null);
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.p();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        super.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.p();
        }
    }

    @Override // android.widget.TextView
    public void setCustomSelectionActionModeCallback(ActionMode.Callback callback) {
        super.setCustomSelectionActionModeCallback(androidx.core.widget.k.t(this, callback));
    }

    public void setEmojiCompatEnabled(boolean z4) {
        getEmojiTextViewHelper().e(z4);
    }

    @Override // android.widget.TextView
    public void setFilters(InputFilter[] inputFilterArr) {
        super.setFilters(getEmojiTextViewHelper().a(inputFilterArr));
    }

    @Override // android.widget.TextView
    public void setFirstBaselineToTopHeight(int i8) {
        if (Build.VERSION.SDK_INT >= 28) {
            getSuperCaller().j(i8);
        } else {
            androidx.core.widget.k.m(this, i8);
        }
    }

    @Override // android.widget.TextView
    public void setLastBaselineToBottomHeight(int i8) {
        if (Build.VERSION.SDK_INT >= 28) {
            getSuperCaller().f(i8);
        } else {
            androidx.core.widget.k.n(this, i8);
        }
    }

    @Override // android.widget.TextView
    public void setLineHeight(int i8) {
        androidx.core.widget.k.o(this, i8);
    }

    public void setPrecomputedText(androidx.core.text.c cVar) {
        androidx.core.widget.k.p(this, cVar);
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintList(ColorStateList colorStateList) {
        d dVar = this.f1204a;
        if (dVar != null) {
            dVar.i(colorStateList);
        }
    }

    @Override // androidx.core.view.a0
    public void setSupportBackgroundTintMode(PorterDuff.Mode mode) {
        d dVar = this.f1204a;
        if (dVar != null) {
            dVar.j(mode);
        }
    }

    @Override // androidx.core.widget.o
    public void setSupportCompoundDrawablesTintList(ColorStateList colorStateList) {
        this.f1205b.w(colorStateList);
        this.f1205b.b();
    }

    @Override // androidx.core.widget.o
    public void setSupportCompoundDrawablesTintMode(PorterDuff.Mode mode) {
        this.f1205b.x(mode);
        this.f1205b.b();
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i8) {
        super.setTextAppearance(context, i8);
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.q(context, i8);
        }
    }

    @Override // android.widget.TextView
    public void setTextClassifier(TextClassifier textClassifier) {
        o oVar;
        if (Build.VERSION.SDK_INT >= 28 || (oVar = this.f1206c) == null) {
            getSuperCaller().e(textClassifier);
        } else {
            oVar.b(textClassifier);
        }
    }

    public void setTextFuture(Future<androidx.core.text.c> future) {
        this.f1210g = future;
        if (future != null) {
            requestLayout();
        }
    }

    public void setTextMetricsParamsCompat(c.a aVar) {
        androidx.core.widget.k.r(this, aVar);
    }

    @Override // android.widget.TextView
    public void setTextSize(int i8, float f5) {
        if (u0.f1636b) {
            super.setTextSize(i8, f5);
            return;
        }
        p pVar = this.f1205b;
        if (pVar != null) {
            pVar.A(i8, f5);
        }
    }

    @Override // android.widget.TextView
    public void setTypeface(Typeface typeface, int i8) {
        if (this.f1208e) {
            return;
        }
        Typeface typeface2 = null;
        if (typeface != null && i8 > 0) {
            typeface2 = androidx.core.graphics.f.a(getContext(), typeface, i8);
        }
        this.f1208e = true;
        if (typeface2 != null) {
            typeface = typeface2;
        }
        try {
            super.setTypeface(typeface, i8);
        } finally {
            this.f1208e = false;
        }
    }
}
