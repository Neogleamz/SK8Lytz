package androidx.transition;

import android.view.ViewGroup;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class p {

    /* renamed from: a  reason: collision with root package name */
    private ViewGroup f7593a;

    /* renamed from: b  reason: collision with root package name */
    private Runnable f7594b;

    public static p b(ViewGroup viewGroup) {
        return (p) viewGroup.getTag(x1.b.f23765f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(ViewGroup viewGroup, p pVar) {
        viewGroup.setTag(x1.b.f23765f, pVar);
    }

    public void a() {
        Runnable runnable;
        if (b(this.f7593a) != this || (runnable = this.f7594b) == null) {
            return;
        }
        runnable.run();
    }
}
