package l6;

import android.app.Activity;
import androidx.fragment.app.FragmentActivity;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e {

    /* renamed from: a  reason: collision with root package name */
    private final Object f21743a;

    public e(Activity activity) {
        n6.j.m(activity, "Activity must not be null");
        this.f21743a = activity;
    }

    public final Activity a() {
        return (Activity) this.f21743a;
    }

    public final FragmentActivity b() {
        return (FragmentActivity) this.f21743a;
    }

    public final boolean c() {
        return this.f21743a instanceof Activity;
    }

    public final boolean d() {
        return this.f21743a instanceof FragmentActivity;
    }
}
