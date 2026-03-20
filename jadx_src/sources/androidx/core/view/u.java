package androidx.core.view;

import android.view.View;
import android.view.ViewGroup;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class u {

    /* renamed from: a  reason: collision with root package name */
    private int f5081a;

    /* renamed from: b  reason: collision with root package name */
    private int f5082b;

    public u(ViewGroup viewGroup) {
    }

    public int a() {
        return this.f5081a | this.f5082b;
    }

    public void b(View view, View view2, int i8) {
        c(view, view2, i8, 0);
    }

    public void c(View view, View view2, int i8, int i9) {
        if (i9 == 1) {
            this.f5082b = i8;
        } else {
            this.f5081a = i8;
        }
    }

    public void d(View view) {
        e(view, 0);
    }

    public void e(View view, int i8) {
        if (i8 == 1) {
            this.f5082b = 0;
        } else {
            this.f5081a = 0;
        }
    }
}
