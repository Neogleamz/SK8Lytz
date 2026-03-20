package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.dynamite.descriptors.com.google.mlkit.dynamite.barcode.ModuleDescriptor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class p extends RecyclerView.x {

    /* renamed from: k  reason: collision with root package name */
    protected PointF f7015k;

    /* renamed from: l  reason: collision with root package name */
    private final DisplayMetrics f7016l;

    /* renamed from: n  reason: collision with root package name */
    private float f7018n;

    /* renamed from: i  reason: collision with root package name */
    protected final LinearInterpolator f7013i = new LinearInterpolator();

    /* renamed from: j  reason: collision with root package name */
    protected final DecelerateInterpolator f7014j = new DecelerateInterpolator();

    /* renamed from: m  reason: collision with root package name */
    private boolean f7017m = false;

    /* renamed from: o  reason: collision with root package name */
    protected int f7019o = 0;

    /* renamed from: p  reason: collision with root package name */
    protected int f7020p = 0;

    public p(Context context) {
        this.f7016l = context.getResources().getDisplayMetrics();
    }

    private float A() {
        if (!this.f7017m) {
            this.f7018n = v(this.f7016l);
            this.f7017m = true;
        }
        return this.f7018n;
    }

    private int y(int i8, int i9) {
        int i10 = i8 - i9;
        if (i8 * i10 <= 0) {
            return 0;
        }
        return i10;
    }

    protected int B() {
        PointF pointF = this.f7015k;
        if (pointF != null) {
            float f5 = pointF.y;
            if (f5 != 0.0f) {
                return f5 > 0.0f ? 1 : -1;
            }
        }
        return 0;
    }

    protected void C(RecyclerView.x.a aVar) {
        PointF a9 = a(f());
        if (a9 == null || (a9.x == 0.0f && a9.y == 0.0f)) {
            aVar.b(f());
            r();
            return;
        }
        i(a9);
        this.f7015k = a9;
        this.f7019o = (int) (a9.x * 10000.0f);
        this.f7020p = (int) (a9.y * 10000.0f);
        aVar.d((int) (this.f7019o * 1.2f), (int) (this.f7020p * 1.2f), (int) (x(ModuleDescriptor.MODULE_VERSION) * 1.2f), this.f7013i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.x
    protected void l(int i8, int i9, RecyclerView.y yVar, RecyclerView.x.a aVar) {
        if (c() == 0) {
            r();
            return;
        }
        this.f7019o = y(this.f7019o, i8);
        int y8 = y(this.f7020p, i9);
        this.f7020p = y8;
        if (this.f7019o == 0 && y8 == 0) {
            C(aVar);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.x
    protected void m() {
    }

    @Override // androidx.recyclerview.widget.RecyclerView.x
    protected void n() {
        this.f7020p = 0;
        this.f7019o = 0;
        this.f7015k = null;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.x
    protected void o(View view, RecyclerView.y yVar, RecyclerView.x.a aVar) {
        int t8 = t(view, z());
        int u8 = u(view, B());
        int w8 = w((int) Math.sqrt((t8 * t8) + (u8 * u8)));
        if (w8 > 0) {
            aVar.d(-t8, -u8, w8, this.f7014j);
        }
    }

    public int s(int i8, int i9, int i10, int i11, int i12) {
        if (i12 != -1) {
            if (i12 != 0) {
                if (i12 == 1) {
                    return i11 - i9;
                }
                throw new IllegalArgumentException("snap preference should be one of the constants defined in SmoothScroller, starting with SNAP_");
            }
            int i13 = i10 - i8;
            if (i13 > 0) {
                return i13;
            }
            int i14 = i11 - i9;
            if (i14 < 0) {
                return i14;
            }
            return 0;
        }
        return i10 - i8;
    }

    public int t(View view, int i8) {
        RecyclerView.o e8 = e();
        if (e8 == null || !e8.l()) {
            return 0;
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        return s(e8.R(view) - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, e8.U(view) + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, e8.f0(), e8.p0() - e8.g0(), i8);
    }

    public int u(View view, int i8) {
        RecyclerView.o e8 = e();
        if (e8 == null || !e8.m()) {
            return 0;
        }
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        return s(e8.V(view) - ((ViewGroup.MarginLayoutParams) layoutParams).topMargin, e8.P(view) + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin, e8.h0(), e8.X() - e8.e0(), i8);
    }

    protected float v(DisplayMetrics displayMetrics) {
        return 25.0f / displayMetrics.densityDpi;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int w(int i8) {
        return (int) Math.ceil(x(i8) / 0.3356d);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int x(int i8) {
        return (int) Math.ceil(Math.abs(i8) * A());
    }

    protected int z() {
        PointF pointF = this.f7015k;
        if (pointF != null) {
            float f5 = pointF.x;
            if (f5 != 0.0f) {
                return f5 > 0.0f ? 1 : -1;
            }
        }
        return 0;
    }
}
