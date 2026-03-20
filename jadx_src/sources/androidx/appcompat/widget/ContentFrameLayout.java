package androidx.appcompat.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ContentFrameLayout extends FrameLayout {

    /* renamed from: a  reason: collision with root package name */
    private TypedValue f1219a;

    /* renamed from: b  reason: collision with root package name */
    private TypedValue f1220b;

    /* renamed from: c  reason: collision with root package name */
    private TypedValue f1221c;

    /* renamed from: d  reason: collision with root package name */
    private TypedValue f1222d;

    /* renamed from: e  reason: collision with root package name */
    private TypedValue f1223e;

    /* renamed from: f  reason: collision with root package name */
    private TypedValue f1224f;

    /* renamed from: g  reason: collision with root package name */
    private final Rect f1225g;

    /* renamed from: h  reason: collision with root package name */
    private a f1226h;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a();

        void onDetachedFromWindow();
    }

    public ContentFrameLayout(Context context) {
        this(context, null);
    }

    public ContentFrameLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ContentFrameLayout(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.f1225g = new Rect();
    }

    public void a(Rect rect) {
        fitSystemWindows(rect);
    }

    public void b(int i8, int i9, int i10, int i11) {
        this.f1225g.set(i8, i9, i10, i11);
        if (androidx.core.view.c0.W(this)) {
            requestLayout();
        }
    }

    public TypedValue getFixedHeightMajor() {
        if (this.f1223e == null) {
            this.f1223e = new TypedValue();
        }
        return this.f1223e;
    }

    public TypedValue getFixedHeightMinor() {
        if (this.f1224f == null) {
            this.f1224f = new TypedValue();
        }
        return this.f1224f;
    }

    public TypedValue getFixedWidthMajor() {
        if (this.f1221c == null) {
            this.f1221c = new TypedValue();
        }
        return this.f1221c;
    }

    public TypedValue getFixedWidthMinor() {
        if (this.f1222d == null) {
            this.f1222d = new TypedValue();
        }
        return this.f1222d;
    }

    public TypedValue getMinWidthMajor() {
        if (this.f1219a == null) {
            this.f1219a = new TypedValue();
        }
        return this.f1219a;
    }

    public TypedValue getMinWidthMinor() {
        if (this.f1220b == null) {
            this.f1220b = new TypedValue();
        }
        return this.f1220b;
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        a aVar = this.f1226h;
        if (aVar != null) {
            aVar.a();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        a aVar = this.f1226h;
        if (aVar != null) {
            aVar.onDetachedFromWindow();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00ab  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00b8  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00be  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x00cc  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00d6  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00de  */
    /* JADX WARN: Removed duplicated region for block: B:61:? A[RETURN, SYNTHETIC] */
    @Override // android.widget.FrameLayout, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected void onMeasure(int r14, int r15) {
        /*
            Method dump skipped, instructions count: 226
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.ContentFrameLayout.onMeasure(int, int):void");
    }

    public void setAttachListener(a aVar) {
        this.f1226h = aVar;
    }
}
