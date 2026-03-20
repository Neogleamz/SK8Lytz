package androidx.appcompat.view.menu;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import androidx.appcompat.view.menu.m;
import androidx.core.view.c0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class l {

    /* renamed from: a  reason: collision with root package name */
    private final Context f997a;

    /* renamed from: b  reason: collision with root package name */
    private final g f998b;

    /* renamed from: c  reason: collision with root package name */
    private final boolean f999c;

    /* renamed from: d  reason: collision with root package name */
    private final int f1000d;

    /* renamed from: e  reason: collision with root package name */
    private final int f1001e;

    /* renamed from: f  reason: collision with root package name */
    private View f1002f;

    /* renamed from: g  reason: collision with root package name */
    private int f1003g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f1004h;

    /* renamed from: i  reason: collision with root package name */
    private m.a f1005i;

    /* renamed from: j  reason: collision with root package name */
    private k f1006j;

    /* renamed from: k  reason: collision with root package name */
    private PopupWindow.OnDismissListener f1007k;

    /* renamed from: l  reason: collision with root package name */
    private final PopupWindow.OnDismissListener f1008l;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements PopupWindow.OnDismissListener {
        a() {
        }

        @Override // android.widget.PopupWindow.OnDismissListener
        public void onDismiss() {
            l.this.e();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        static void a(Display display, Point point) {
            display.getRealSize(point);
        }
    }

    public l(Context context, g gVar, View view, boolean z4, int i8) {
        this(context, gVar, view, z4, i8, 0);
    }

    public l(Context context, g gVar, View view, boolean z4, int i8, int i9) {
        this.f1003g = 8388611;
        this.f1008l = new a();
        this.f997a = context;
        this.f998b = gVar;
        this.f1002f = view;
        this.f999c = z4;
        this.f1000d = i8;
        this.f1001e = i9;
    }

    private k a() {
        Display defaultDisplay = ((WindowManager) this.f997a.getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= 17) {
            b.a(defaultDisplay, point);
        } else {
            defaultDisplay.getSize(point);
        }
        k dVar = Math.min(point.x, point.y) >= this.f997a.getResources().getDimensionPixelSize(g.d.f19898c) ? new d(this.f997a, this.f1002f, this.f1000d, this.f1001e, this.f999c) : new q(this.f997a, this.f998b, this.f1002f, this.f1000d, this.f1001e, this.f999c);
        dVar.d(this.f998b);
        dVar.x(this.f1008l);
        dVar.s(this.f1002f);
        dVar.j(this.f1005i);
        dVar.u(this.f1004h);
        dVar.v(this.f1003g);
        return dVar;
    }

    private void l(int i8, int i9, boolean z4, boolean z8) {
        k c9 = c();
        c9.y(z8);
        if (z4) {
            if ((androidx.core.view.f.b(this.f1003g, c0.E(this.f1002f)) & 7) == 5) {
                i8 -= this.f1002f.getWidth();
            }
            c9.w(i8);
            c9.z(i9);
            int i10 = (int) ((this.f997a.getResources().getDisplayMetrics().density * 48.0f) / 2.0f);
            c9.t(new Rect(i8 - i10, i9 - i10, i8 + i10, i9 + i10));
        }
        c9.a();
    }

    public void b() {
        if (d()) {
            this.f1006j.dismiss();
        }
    }

    public k c() {
        if (this.f1006j == null) {
            this.f1006j = a();
        }
        return this.f1006j;
    }

    public boolean d() {
        k kVar = this.f1006j;
        return kVar != null && kVar.b();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void e() {
        this.f1006j = null;
        PopupWindow.OnDismissListener onDismissListener = this.f1007k;
        if (onDismissListener != null) {
            onDismissListener.onDismiss();
        }
    }

    public void f(View view) {
        this.f1002f = view;
    }

    public void g(boolean z4) {
        this.f1004h = z4;
        k kVar = this.f1006j;
        if (kVar != null) {
            kVar.u(z4);
        }
    }

    public void h(int i8) {
        this.f1003g = i8;
    }

    public void i(PopupWindow.OnDismissListener onDismissListener) {
        this.f1007k = onDismissListener;
    }

    public void j(m.a aVar) {
        this.f1005i = aVar;
        k kVar = this.f1006j;
        if (kVar != null) {
            kVar.j(aVar);
        }
    }

    public void k() {
        if (!m()) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }

    public boolean m() {
        if (d()) {
            return true;
        }
        if (this.f1002f == null) {
            return false;
        }
        l(0, 0, false, false);
        return true;
    }

    public boolean n(int i8, int i9) {
        if (d()) {
            return true;
        }
        if (this.f1002f == null) {
            return false;
        }
        l(i8, i9, true, true);
        return true;
    }
}
