package androidx.appcompat.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.PopupWindow;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class AppCompatPopupWindow extends PopupWindow {

    /* renamed from: b  reason: collision with root package name */
    private static final boolean f1170b;

    /* renamed from: a  reason: collision with root package name */
    private boolean f1171a;

    static {
        f1170b = Build.VERSION.SDK_INT < 21;
    }

    public AppCompatPopupWindow(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        a(context, attributeSet, i8, 0);
    }

    public AppCompatPopupWindow(Context context, AttributeSet attributeSet, int i8, int i9) {
        super(context, attributeSet, i8, i9);
        a(context, attributeSet, i8, i9);
    }

    private void a(Context context, AttributeSet attributeSet, int i8, int i9) {
        j0 v8 = j0.v(context, attributeSet, g.j.f20050i2, i8, i9);
        int i10 = g.j.f20060k2;
        if (v8.s(i10)) {
            b(v8.a(i10, false));
        }
        setBackgroundDrawable(v8.g(g.j.f20055j2));
        v8.w();
    }

    private void b(boolean z4) {
        if (f1170b) {
            this.f1171a = z4;
        } else {
            androidx.core.widget.j.a(this, z4);
        }
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i8, int i9) {
        if (f1170b && this.f1171a) {
            i9 -= view.getHeight();
        }
        super.showAsDropDown(view, i8, i9);
    }

    @Override // android.widget.PopupWindow
    public void showAsDropDown(View view, int i8, int i9, int i10) {
        if (f1170b && this.f1171a) {
            i9 -= view.getHeight();
        }
        super.showAsDropDown(view, i8, i9, i10);
    }

    @Override // android.widget.PopupWindow
    public void update(View view, int i8, int i9, int i10, int i11) {
        if (f1170b && this.f1171a) {
            i9 -= view.getHeight();
        }
        super.update(view, i8, i9, i10, i11);
    }
}
