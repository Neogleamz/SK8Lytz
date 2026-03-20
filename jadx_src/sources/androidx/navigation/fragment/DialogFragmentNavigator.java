package androidx.navigation.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.h;
import androidx.lifecycle.j;
import androidx.navigation.i;
import androidx.navigation.n;
import androidx.navigation.q;
import java.util.HashSet;
@q.b("dialog")
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class DialogFragmentNavigator extends q<a> {

    /* renamed from: a  reason: collision with root package name */
    private final Context f6328a;

    /* renamed from: b  reason: collision with root package name */
    private final FragmentManager f6329b;

    /* renamed from: c  reason: collision with root package name */
    private int f6330c = 0;

    /* renamed from: d  reason: collision with root package name */
    private final HashSet<String> f6331d = new HashSet<>();

    /* renamed from: e  reason: collision with root package name */
    private h f6332e = new h() { // from class: androidx.navigation.fragment.DialogFragmentNavigator.1
        @Override // androidx.lifecycle.h
        public void c(j jVar, Lifecycle.Event event) {
            if (event == Lifecycle.Event.ON_STOP) {
                androidx.fragment.app.c cVar = (androidx.fragment.app.c) jVar;
                if (cVar.V1().isShowing()) {
                    return;
                }
                b.K1(cVar).m();
            }
        }
    };

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends i implements androidx.navigation.b {

        /* renamed from: k  reason: collision with root package name */
        private String f6334k;

        public a(q<? extends a> qVar) {
            super(qVar);
        }

        public final String F() {
            String str = this.f6334k;
            if (str != null) {
                return str;
            }
            throw new IllegalStateException("DialogFragment class was not set");
        }

        public final a G(String str) {
            this.f6334k = str;
            return this;
        }

        @Override // androidx.navigation.i
        public void x(Context context, AttributeSet attributeSet) {
            super.x(context, attributeSet);
            TypedArray obtainAttributes = context.getResources().obtainAttributes(attributeSet, d.f6349c);
            String string = obtainAttributes.getString(d.f6350d);
            if (string != null) {
                G(string);
            }
            obtainAttributes.recycle();
        }
    }

    public DialogFragmentNavigator(Context context, FragmentManager fragmentManager) {
        this.f6328a = context;
        this.f6329b = fragmentManager;
    }

    @Override // androidx.navigation.q
    public void c(Bundle bundle) {
        if (bundle != null) {
            this.f6330c = bundle.getInt("androidx-nav-dialogfragment:navigator:count", 0);
            for (int i8 = 0; i8 < this.f6330c; i8++) {
                FragmentManager fragmentManager = this.f6329b;
                androidx.fragment.app.c cVar = (androidx.fragment.app.c) fragmentManager.i0("androidx-nav-fragment:navigator:dialog:" + i8);
                if (cVar != null) {
                    cVar.getLifecycle().a(this.f6332e);
                } else {
                    HashSet<String> hashSet = this.f6331d;
                    hashSet.add("androidx-nav-fragment:navigator:dialog:" + i8);
                }
            }
        }
    }

    @Override // androidx.navigation.q
    public Bundle d() {
        if (this.f6330c == 0) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putInt("androidx-nav-dialogfragment:navigator:count", this.f6330c);
        return bundle;
    }

    @Override // androidx.navigation.q
    public boolean e() {
        if (this.f6330c == 0) {
            return false;
        }
        if (this.f6329b.K0()) {
            Log.i("DialogFragmentNavigator", "Ignoring popBackStack() call: FragmentManager has already saved its state");
            return false;
        }
        FragmentManager fragmentManager = this.f6329b;
        StringBuilder sb = new StringBuilder();
        sb.append("androidx-nav-fragment:navigator:dialog:");
        int i8 = this.f6330c - 1;
        this.f6330c = i8;
        sb.append(i8);
        Fragment i02 = fragmentManager.i0(sb.toString());
        if (i02 != null) {
            i02.getLifecycle().c(this.f6332e);
            ((androidx.fragment.app.c) i02).M1();
        }
        return true;
    }

    @Override // androidx.navigation.q
    /* renamed from: f */
    public a a() {
        return new a(this);
    }

    @Override // androidx.navigation.q
    /* renamed from: g */
    public i b(a aVar, Bundle bundle, n nVar, q.a aVar2) {
        if (this.f6329b.K0()) {
            Log.i("DialogFragmentNavigator", "Ignoring navigate() call: FragmentManager has already saved its state");
            return null;
        }
        String F = aVar.F();
        if (F.charAt(0) == '.') {
            F = this.f6328a.getPackageName() + F;
        }
        Fragment a9 = this.f6329b.q0().a(this.f6328a.getClassLoader(), F);
        if (!androidx.fragment.app.c.class.isAssignableFrom(a9.getClass())) {
            throw new IllegalArgumentException("Dialog destination " + aVar.F() + " is not an instance of DialogFragment");
        }
        androidx.fragment.app.c cVar = (androidx.fragment.app.c) a9;
        cVar.t1(bundle);
        cVar.getLifecycle().a(this.f6332e);
        FragmentManager fragmentManager = this.f6329b;
        StringBuilder sb = new StringBuilder();
        sb.append("androidx-nav-fragment:navigator:dialog:");
        int i8 = this.f6330c;
        this.f6330c = i8 + 1;
        sb.append(i8);
        cVar.Y1(fragmentManager, sb.toString());
        return aVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void h(Fragment fragment) {
        if (this.f6331d.remove(fragment.R())) {
            fragment.getLifecycle().a(this.f6332e);
        }
    }
}
