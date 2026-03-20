package androidx.transition;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class x extends c0 implements z {
    /* JADX INFO: Access modifiers changed from: package-private */
    public x(Context context, ViewGroup viewGroup, View view) {
        super(context, viewGroup, view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static x g(ViewGroup viewGroup) {
        return (x) c0.e(viewGroup);
    }

    @Override // androidx.transition.z
    public void c(View view) {
        this.f7524a.b(view);
    }

    @Override // androidx.transition.z
    public void d(View view) {
        this.f7524a.g(view);
    }
}
