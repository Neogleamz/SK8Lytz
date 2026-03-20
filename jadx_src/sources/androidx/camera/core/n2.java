package androidx.camera.core;

import android.graphics.Rect;
import android.util.Size;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n2 extends h0 {

    /* renamed from: d  reason: collision with root package name */
    private final Object f2735d;

    /* renamed from: e  reason: collision with root package name */
    private final i1 f2736e;

    /* renamed from: f  reason: collision with root package name */
    private Rect f2737f;

    /* renamed from: g  reason: collision with root package name */
    private final int f2738g;

    /* renamed from: h  reason: collision with root package name */
    private final int f2739h;

    public n2(l1 l1Var, Size size, i1 i1Var) {
        super(l1Var);
        int height;
        this.f2735d = new Object();
        if (size == null) {
            this.f2738g = super.getWidth();
            height = super.getHeight();
        } else {
            this.f2738g = size.getWidth();
            height = size.getHeight();
        }
        this.f2739h = height;
        this.f2736e = i1Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public n2(l1 l1Var, i1 i1Var) {
        this(l1Var, null, i1Var);
    }

    @Override // androidx.camera.core.h0, androidx.camera.core.l1
    public void Y0(Rect rect) {
        if (rect != null) {
            Rect rect2 = new Rect(rect);
            if (!rect2.intersect(0, 0, getWidth(), getHeight())) {
                rect2.setEmpty();
            }
            rect = rect2;
        }
        synchronized (this.f2735d) {
            this.f2737f = rect;
        }
    }

    @Override // androidx.camera.core.h0, androidx.camera.core.l1
    public i1 e1() {
        return this.f2736e;
    }

    @Override // androidx.camera.core.h0, androidx.camera.core.l1
    public int getHeight() {
        return this.f2739h;
    }

    @Override // androidx.camera.core.h0, androidx.camera.core.l1
    public int getWidth() {
        return this.f2738g;
    }
}
