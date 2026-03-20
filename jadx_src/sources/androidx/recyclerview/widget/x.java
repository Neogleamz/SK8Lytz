package androidx.recyclerview.widget;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class x {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(RecyclerView.y yVar, u uVar, View view, View view2, RecyclerView.o oVar, boolean z4) {
        if (oVar.K() == 0 || yVar.b() == 0 || view == null || view2 == null) {
            return 0;
        }
        if (z4) {
            return Math.min(uVar.n(), uVar.d(view2) - uVar.g(view));
        }
        return Math.abs(oVar.i0(view) - oVar.i0(view2)) + 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int b(RecyclerView.y yVar, u uVar, View view, View view2, RecyclerView.o oVar, boolean z4, boolean z8) {
        if (oVar.K() == 0 || yVar.b() == 0 || view == null || view2 == null) {
            return 0;
        }
        int max = z8 ? Math.max(0, (yVar.b() - Math.max(oVar.i0(view), oVar.i0(view2))) - 1) : Math.max(0, Math.min(oVar.i0(view), oVar.i0(view2)));
        if (z4) {
            return Math.round((max * (Math.abs(uVar.d(view2) - uVar.g(view)) / (Math.abs(oVar.i0(view) - oVar.i0(view2)) + 1))) + (uVar.m() - uVar.g(view)));
        }
        return max;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(RecyclerView.y yVar, u uVar, View view, View view2, RecyclerView.o oVar, boolean z4) {
        if (oVar.K() == 0 || yVar.b() == 0 || view == null || view2 == null) {
            return 0;
        }
        if (z4) {
            return (int) (((uVar.d(view2) - uVar.g(view)) / (Math.abs(oVar.i0(view) - oVar.i0(view2)) + 1)) * yVar.b());
        }
        return yVar.b();
    }
}
