package androidx.navigation;

import androidx.lifecycle.e0;
import androidx.lifecycle.f0;
import androidx.lifecycle.i0;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class f extends e0 {

    /* renamed from: e  reason: collision with root package name */
    private static final f0.b f6326e = new a();

    /* renamed from: d  reason: collision with root package name */
    private final HashMap<UUID, i0> f6327d = new HashMap<>();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements f0.b {
        a() {
        }

        @Override // androidx.lifecycle.f0.b
        public <T extends e0> T a(Class<T> cls) {
            return new f();
        }
    }

    f() {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static f g(i0 i0Var) {
        return (f) new f0(i0Var, f6326e).a(f.class);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.lifecycle.e0
    public void d() {
        for (i0 i0Var : this.f6327d.values()) {
            i0Var.a();
        }
        this.f6327d.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f(UUID uuid) {
        i0 remove = this.f6327d.remove(uuid);
        if (remove != null) {
            remove.a();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public i0 h(UUID uuid) {
        i0 i0Var = this.f6327d.get(uuid);
        if (i0Var == null) {
            i0 i0Var2 = new i0();
            this.f6327d.put(uuid, i0Var2);
            return i0Var2;
        }
        return i0Var;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("NavControllerViewModel{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append("} ViewModelStores (");
        Iterator<UUID> it = this.f6327d.keySet().iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(')');
        return sb.toString();
    }
}
