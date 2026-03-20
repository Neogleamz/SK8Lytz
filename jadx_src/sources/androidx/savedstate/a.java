package androidx.savedstate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.j;
import androidx.savedstate.Recreator;
import java.util.Iterator;
import java.util.Map;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
import s1.d;
@SuppressLint({"RestrictedApi"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: g  reason: collision with root package name */
    private static final b f7253g = new b(null);

    /* renamed from: b  reason: collision with root package name */
    private boolean f7255b;

    /* renamed from: c  reason: collision with root package name */
    private Bundle f7256c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f7257d;

    /* renamed from: e  reason: collision with root package name */
    private Recreator.b f7258e;

    /* renamed from: a  reason: collision with root package name */
    private final m.b<String, c> f7254a = new m.b<>();

    /* renamed from: f  reason: collision with root package name */
    private boolean f7259f = true;

    /* renamed from: androidx.savedstate.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0079a {
        void a(d dVar);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b {
        private b() {
        }

        public /* synthetic */ b(i iVar) {
            this();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        Bundle a();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final void d(a aVar, j jVar, Lifecycle.Event event) {
        boolean z4;
        p.e(aVar, "this$0");
        p.e(jVar, "<anonymous parameter 0>");
        p.e(event, "event");
        if (event == Lifecycle.Event.ON_START) {
            z4 = true;
        } else if (event != Lifecycle.Event.ON_STOP) {
            return;
        } else {
            z4 = false;
        }
        aVar.f7259f = z4;
    }

    public final Bundle b(String str) {
        p.e(str, "key");
        if (this.f7257d) {
            Bundle bundle = this.f7256c;
            if (bundle != null) {
                Bundle bundle2 = bundle != null ? bundle.getBundle(str) : null;
                Bundle bundle3 = this.f7256c;
                if (bundle3 != null) {
                    bundle3.remove(str);
                }
                Bundle bundle4 = this.f7256c;
                boolean z4 = false;
                if (bundle4 != null && !bundle4.isEmpty()) {
                    z4 = true;
                }
                if (!z4) {
                    this.f7256c = null;
                }
                return bundle2;
            }
            return null;
        }
        throw new IllegalStateException("You can consumeRestoredStateForKey only after super.onCreate of corresponding component".toString());
    }

    public final c c(String str) {
        p.e(str, "key");
        Iterator<Map.Entry<String, c>> it = this.f7254a.iterator();
        while (it.hasNext()) {
            Map.Entry<String, c> next = it.next();
            p.d(next, "components");
            c value = next.getValue();
            if (p.a(next.getKey(), str)) {
                return value;
            }
        }
        return null;
    }

    public final void e(Lifecycle lifecycle) {
        p.e(lifecycle, "lifecycle");
        if (!(!this.f7255b)) {
            throw new IllegalStateException("SavedStateRegistry was already attached.".toString());
        }
        lifecycle.a(new s1.b(this));
        this.f7255b = true;
    }

    public final void f(Bundle bundle) {
        if (!this.f7255b) {
            throw new IllegalStateException("You must call performAttach() before calling performRestore(Bundle).".toString());
        }
        if (!(!this.f7257d)) {
            throw new IllegalStateException("SavedStateRegistry was already restored.".toString());
        }
        this.f7256c = bundle != null ? bundle.getBundle("androidx.lifecycle.BundlableSavedStateRegistry.key") : null;
        this.f7257d = true;
    }

    public final void g(Bundle bundle) {
        p.e(bundle, "outBundle");
        Bundle bundle2 = new Bundle();
        Bundle bundle3 = this.f7256c;
        if (bundle3 != null) {
            bundle2.putAll(bundle3);
        }
        m.b<String, c>.d h8 = this.f7254a.h();
        p.d(h8, "this.components.iteratorWithAdditions()");
        while (h8.hasNext()) {
            Map.Entry next = h8.next();
            bundle2.putBundle((String) next.getKey(), ((c) next.getValue()).a());
        }
        if (bundle2.isEmpty()) {
            return;
        }
        bundle.putBundle("androidx.lifecycle.BundlableSavedStateRegistry.key", bundle2);
    }

    public final void h(String str, c cVar) {
        p.e(str, "key");
        p.e(cVar, "provider");
        if (!(this.f7254a.n(str, cVar) == null)) {
            throw new IllegalArgumentException("SavedStateProvider with the given key is already registered".toString());
        }
    }

    public final void i(Class<? extends InterfaceC0079a> cls) {
        p.e(cls, "clazz");
        if (!this.f7259f) {
            throw new IllegalStateException("Can not perform this action after onSaveInstanceState".toString());
        }
        Recreator.b bVar = this.f7258e;
        if (bVar == null) {
            bVar = new Recreator.b(this);
        }
        this.f7258e = bVar;
        try {
            cls.getDeclaredConstructor(new Class[0]);
            Recreator.b bVar2 = this.f7258e;
            if (bVar2 != null) {
                String name = cls.getName();
                p.d(name, "clazz.name");
                bVar2.b(name);
            }
        } catch (NoSuchMethodException e8) {
            throw new IllegalArgumentException("Class " + cls.getSimpleName() + " must have default constructor in order to be automatically recreated", e8);
        }
    }
}
