package l6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.common.api.internal.LifecycleCallback;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i0 extends Fragment implements f {

    /* renamed from: s0  reason: collision with root package name */
    private static final WeakHashMap f21766s0 = new WeakHashMap();

    /* renamed from: p0  reason: collision with root package name */
    private final Map f21767p0 = Collections.synchronizedMap(new k0.a());

    /* renamed from: q0  reason: collision with root package name */
    private int f21768q0 = 0;

    /* renamed from: r0  reason: collision with root package name */
    private Bundle f21769r0;

    public static i0 L1(FragmentActivity fragmentActivity) {
        i0 i0Var;
        WeakHashMap weakHashMap = f21766s0;
        WeakReference weakReference = (WeakReference) weakHashMap.get(fragmentActivity);
        if (weakReference == null || (i0Var = (i0) weakReference.get()) == null) {
            try {
                i0 i0Var2 = (i0) fragmentActivity.getSupportFragmentManager().i0("SupportLifecycleFragmentImpl");
                if (i0Var2 == null || i0Var2.g0()) {
                    i0Var2 = new i0();
                    fragmentActivity.getSupportFragmentManager().l().d(i0Var2, "SupportLifecycleFragmentImpl").j();
                }
                weakHashMap.put(fragmentActivity, new WeakReference(i0Var2));
                return i0Var2;
            } catch (ClassCastException e8) {
                throw new IllegalStateException("Fragment with tag SupportLifecycleFragmentImpl is not a SupportLifecycleFragmentImpl", e8);
            }
        }
        return i0Var;
    }

    @Override // androidx.fragment.app.Fragment
    public final void I0(Bundle bundle) {
        super.I0(bundle);
        if (bundle == null) {
            return;
        }
        for (Map.Entry entry : this.f21767p0.entrySet()) {
            Bundle bundle2 = new Bundle();
            ((LifecycleCallback) entry.getValue()).i(bundle2);
            bundle.putBundle((String) entry.getKey(), bundle2);
        }
    }

    @Override // l6.f
    public final void a(String str, LifecycleCallback lifecycleCallback) {
        if (this.f21767p0.containsKey(str)) {
            throw new IllegalArgumentException("LifecycleCallback with tag " + str + " already added to this fragment.");
        }
        this.f21767p0.put(str, lifecycleCallback);
        if (this.f21768q0 > 0) {
            new com.google.android.gms.internal.common.j(Looper.getMainLooper()).post(new h0(this, lifecycleCallback, str));
        }
    }

    @Override // l6.f
    public final <T extends LifecycleCallback> T b(String str, Class<T> cls) {
        return cls.cast(this.f21767p0.get(str));
    }

    @Override // l6.f
    public final /* synthetic */ Activity c() {
        return k();
    }

    @Override // androidx.fragment.app.Fragment
    public final void g(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.g(str, fileDescriptor, printWriter, strArr);
        for (LifecycleCallback lifecycleCallback : this.f21767p0.values()) {
            lifecycleCallback.a(str, fileDescriptor, printWriter, strArr);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public final void l0(int i8, int i9, Intent intent) {
        super.l0(i8, i9, intent);
        for (LifecycleCallback lifecycleCallback : this.f21767p0.values()) {
            lifecycleCallback.e(i8, i9, intent);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f21768q0 = 1;
        this.f21769r0 = bundle;
        for (Map.Entry entry : this.f21767p0.entrySet()) {
            ((LifecycleCallback) entry.getValue()).f(bundle != null ? bundle.getBundle((String) entry.getKey()) : null);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public final void onDestroy() {
        super.onDestroy();
        this.f21768q0 = 5;
        for (LifecycleCallback lifecycleCallback : this.f21767p0.values()) {
            lifecycleCallback.g();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public final void onResume() {
        super.onResume();
        this.f21768q0 = 3;
        for (LifecycleCallback lifecycleCallback : this.f21767p0.values()) {
            lifecycleCallback.h();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public final void onStart() {
        super.onStart();
        this.f21768q0 = 2;
        for (LifecycleCallback lifecycleCallback : this.f21767p0.values()) {
            lifecycleCallback.j();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public final void onStop() {
        super.onStop();
        this.f21768q0 = 4;
        for (LifecycleCallback lifecycleCallback : this.f21767p0.values()) {
            lifecycleCallback.k();
        }
    }
}
