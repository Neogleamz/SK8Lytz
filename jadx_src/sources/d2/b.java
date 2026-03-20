package d2;

import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class b implements View.OnClickListener {

    /* renamed from: a  reason: collision with root package name */
    static boolean f19698a = true;

    /* renamed from: b  reason: collision with root package name */
    private static final Runnable f19699b = a.a;

    public abstract void b(View view);

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        if (f19698a) {
            f19698a = false;
            view.post(f19699b);
            b(view);
        }
    }
}
