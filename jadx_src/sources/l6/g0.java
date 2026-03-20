package l6;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g0 extends Fragment implements f {

    /* renamed from: d  reason: collision with root package name */
    private static final WeakHashMap f21759d = new WeakHashMap();

    /* renamed from: a  reason: collision with root package name */
    private final Map f21760a = Collections.synchronizedMap(new k0.a());

    /* renamed from: b  reason: collision with root package name */
    private int f21761b = 0;

    /* renamed from: c  reason: collision with root package name */
    private Bundle f21762c;

    public static g0 f(Activity activity) {
        g0 g0Var;
        WeakHashMap weakHashMap = f21759d;
        WeakReference weakReference = (WeakReference) weakHashMap.get(activity);
        if (weakReference == null || (g0Var = (g0) weakReference.get()) == null) {
            try {
                g0 g0Var2 = (g0) activity.getFragmentManager().findFragmentByTag("LifecycleFragmentImpl");
                if (g0Var2 == null || g0Var2.isRemoving()) {
                    g0Var2 = new g0();
                    activity.getFragmentManager().beginTransaction().add(g0Var2, "LifecycleFragmentImpl").commitAllowingStateLoss();
                }
                weakHashMap.put(activity, new WeakReference(g0Var2));
                return g0Var2;
            } catch (ClassCastException e8) {
                throw new IllegalStateException("Fragment with tag LifecycleFragmentImpl is not a LifecycleFragmentImpl", e8);
            }
        }
        return g0Var;
    }

    @Override // l6.f
    public final void a(String str, LifecycleCallback lifecycleCallback) {
        if (this.f21760a.containsKey(str)) {
            throw new IllegalArgumentException("LifecycleCallback with tag " + str + " already added to this fragment.");
        }
        this.f21760a.put(str, lifecycleCallback);
        if (this.f21761b > 0) {
            new com.google.android.gms.internal.common.j(Looper.getMainLooper()).post(new f0(this, lifecycleCallback, str));
        }
    }

    @Override // l6.f
    public final <T extends LifecycleCallback> T b(String str, Class<T> cls) {
        return cls.cast(this.f21760a.get(str));
    }

    @Override // l6.f
    public final Activity c() {
        return getActivity();
    }

    @Override // android.app.Fragment
    public final void dump(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dump(str, fileDescriptor, printWriter, strArr);
        for (LifecycleCallback lifecycleCallback : this.f21760a.values()) {
            lifecycleCallback.a(str, fileDescriptor, printWriter, strArr);
        }
    }

    @Override // android.app.Fragment
    public final void onActivityResult(int i8, int i9, Intent intent) {
        super.onActivityResult(i8, i9, intent);
        for (LifecycleCallback lifecycleCallback : this.f21760a.values()) {
            lifecycleCallback.e(i8, i9, intent);
        }
    }

    @Override // android.app.Fragment
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f21761b = 1;
        this.f21762c = bundle;
        for (Map.Entry entry : this.f21760a.entrySet()) {
            ((LifecycleCallback) entry.getValue()).f(bundle != null ? bundle.getBundle((String) entry.getKey()) : null);
        }
    }

    @Override // android.app.Fragment
    public final void onDestroy() {
        super.onDestroy();
        this.f21761b = 5;
        for (LifecycleCallback lifecycleCallback : this.f21760a.values()) {
            lifecycleCallback.g();
        }
    }

    @Override // android.app.Fragment
    public final void onResume() {
        super.onResume();
        this.f21761b = 3;
        for (LifecycleCallback lifecycleCallback : this.f21760a.values()) {
            lifecycleCallback.h();
        }
    }

    @Override // android.app.Fragment
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle == null) {
            return;
        }
        for (Map.Entry entry : this.f21760a.entrySet()) {
            Bundle bundle2 = new Bundle();
            ((LifecycleCallback) entry.getValue()).i(bundle2);
            bundle.putBundle((String) entry.getKey(), bundle2);
        }
    }

    @Override // android.app.Fragment
    public final void onStart() {
        super.onStart();
        this.f21761b = 2;
        for (LifecycleCallback lifecycleCallback : this.f21760a.values()) {
            lifecycleCallback.j();
        }
    }

    @Override // android.app.Fragment
    public final void onStop() {
        super.onStop();
        this.f21761b = 4;
        for (LifecycleCallback lifecycleCallback : this.f21760a.values()) {
            lifecycleCallback.k();
        }
    }
}
