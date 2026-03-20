package androidx.fragment.app;

import android.util.Log;
import androidx.lifecycle.e0;
import androidx.lifecycle.f0;
import androidx.lifecycle.i0;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class l extends e0 {

    /* renamed from: k  reason: collision with root package name */
    private static final f0.b f5639k = new a();

    /* renamed from: g  reason: collision with root package name */
    private final boolean f5643g;

    /* renamed from: d  reason: collision with root package name */
    private final HashMap<String, Fragment> f5640d = new HashMap<>();

    /* renamed from: e  reason: collision with root package name */
    private final HashMap<String, l> f5641e = new HashMap<>();

    /* renamed from: f  reason: collision with root package name */
    private final HashMap<String, i0> f5642f = new HashMap<>();

    /* renamed from: h  reason: collision with root package name */
    private boolean f5644h = false;

    /* renamed from: i  reason: collision with root package name */
    private boolean f5645i = false;

    /* renamed from: j  reason: collision with root package name */
    private boolean f5646j = false;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements f0.b {
        a() {
        }

        @Override // androidx.lifecycle.f0.b
        public <T extends e0> T a(Class<T> cls) {
            return new l(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public l(boolean z4) {
        this.f5643g = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static l j(i0 i0Var) {
        return (l) new f0(i0Var, f5639k).a(l.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.lifecycle.e0
    public void d() {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "onCleared called for " + this);
        }
        this.f5644h = true;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || l.class != obj.getClass()) {
            return false;
        }
        l lVar = (l) obj;
        return this.f5640d.equals(lVar.f5640d) && this.f5641e.equals(lVar.f5641e) && this.f5642f.equals(lVar.f5642f);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(Fragment fragment) {
        if (this.f5646j) {
            if (FragmentManager.F0(2)) {
                Log.v("FragmentManager", "Ignoring addRetainedFragment as the state is already saved");
            }
        } else if (this.f5640d.containsKey(fragment.f5399f)) {
        } else {
            this.f5640d.put(fragment.f5399f, fragment);
            if (FragmentManager.F0(2)) {
                Log.v("FragmentManager", "Updating retained Fragments: Added " + fragment);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void g(Fragment fragment) {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "Clearing non-config state for " + fragment);
        }
        l lVar = this.f5641e.get(fragment.f5399f);
        if (lVar != null) {
            lVar.d();
            this.f5641e.remove(fragment.f5399f);
        }
        i0 i0Var = this.f5642f.get(fragment.f5399f);
        if (i0Var != null) {
            i0Var.a();
            this.f5642f.remove(fragment.f5399f);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Fragment h(String str) {
        return this.f5640d.get(str);
    }

    public int hashCode() {
        return (((this.f5640d.hashCode() * 31) + this.f5641e.hashCode()) * 31) + this.f5642f.hashCode();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public l i(Fragment fragment) {
        l lVar = this.f5641e.get(fragment.f5399f);
        if (lVar == null) {
            l lVar2 = new l(this.f5643g);
            this.f5641e.put(fragment.f5399f, lVar2);
            return lVar2;
        }
        return lVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Collection<Fragment> k() {
        return new ArrayList(this.f5640d.values());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i0 l(Fragment fragment) {
        i0 i0Var = this.f5642f.get(fragment.f5399f);
        if (i0Var == null) {
            i0 i0Var2 = new i0();
            this.f5642f.put(fragment.f5399f, i0Var2);
            return i0Var2;
        }
        return i0Var;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean m() {
        return this.f5644h;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void n(Fragment fragment) {
        if (this.f5646j) {
            if (FragmentManager.F0(2)) {
                Log.v("FragmentManager", "Ignoring removeRetainedFragment as the state is already saved");
                return;
            }
            return;
        }
        if ((this.f5640d.remove(fragment.f5399f) != null) && FragmentManager.F0(2)) {
            Log.v("FragmentManager", "Updating retained Fragments: Removed " + fragment);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(boolean z4) {
        this.f5646j = z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean p(Fragment fragment) {
        if (this.f5640d.containsKey(fragment.f5399f)) {
            return this.f5643g ? this.f5644h : !this.f5645i;
        }
        return true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("FragmentManagerViewModel{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append("} Fragments (");
        Iterator<Fragment> it = this.f5640d.values().iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(") Child Non Config (");
        Iterator<String> it2 = this.f5641e.keySet().iterator();
        while (it2.hasNext()) {
            sb.append(it2.next());
            if (it2.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(") ViewModelStores (");
        Iterator<String> it3 = this.f5642f.keySet().iterator();
        while (it3.hasNext()) {
            sb.append(it3.next());
            if (it3.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
