package androidx.savedstate;

import android.os.Bundle;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.h;
import androidx.lifecycle.j;
import androidx.savedstate.a;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
import s1.d;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class Recreator implements h {

    /* renamed from: b  reason: collision with root package name */
    public static final a f7250b = new a(null);

    /* renamed from: a  reason: collision with root package name */
    private final d f7251a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        private a() {
        }

        public /* synthetic */ a(i iVar) {
            this();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b implements a.c {

        /* renamed from: a  reason: collision with root package name */
        private final Set<String> f7252a;

        public b(androidx.savedstate.a aVar) {
            p.e(aVar, "registry");
            this.f7252a = new LinkedHashSet();
            aVar.h("androidx.savedstate.Restarter", this);
        }

        @Override // androidx.savedstate.a.c
        public Bundle a() {
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("classes_to_restore", new ArrayList<>(this.f7252a));
            return bundle;
        }

        public final void b(String str) {
            p.e(str, "className");
            this.f7252a.add(str);
        }
    }

    public Recreator(d dVar) {
        p.e(dVar, "owner");
        this.f7251a = dVar;
    }

    private final void a(String str) {
        try {
            Class<? extends U> asSubclass = Class.forName(str, false, Recreator.class.getClassLoader()).asSubclass(a.InterfaceC0079a.class);
            p.d(asSubclass, "{\n                Class.…class.java)\n            }");
            try {
                Constructor declaredConstructor = asSubclass.getDeclaredConstructor(new Class[0]);
                declaredConstructor.setAccessible(true);
                try {
                    Object newInstance = declaredConstructor.newInstance(new Object[0]);
                    p.d(newInstance, "{\n                constr…wInstance()\n            }");
                    ((a.InterfaceC0079a) newInstance).a(this.f7251a);
                } catch (Exception e8) {
                    throw new RuntimeException("Failed to instantiate " + str, e8);
                }
            } catch (NoSuchMethodException e9) {
                throw new IllegalStateException("Class " + asSubclass.getSimpleName() + " must have default constructor in order to be automatically recreated", e9);
            }
        } catch (ClassNotFoundException e10) {
            throw new RuntimeException("Class " + str + " wasn't found", e10);
        }
    }

    @Override // androidx.lifecycle.h
    public void c(j jVar, Lifecycle.Event event) {
        p.e(jVar, "source");
        p.e(event, "event");
        if (event != Lifecycle.Event.ON_CREATE) {
            throw new AssertionError("Next event must be ON_CREATE");
        }
        jVar.getLifecycle().c(this);
        Bundle b9 = this.f7251a.getSavedStateRegistry().b("androidx.savedstate.Restarter");
        if (b9 == null) {
            return;
        }
        ArrayList<String> stringArrayList = b9.getStringArrayList("classes_to_restore");
        if (stringArrayList == null) {
            throw new IllegalStateException("Bundle with restored state for the component \"androidx.savedstate.Restarter\" must contain list of strings by the key \"classes_to_restore\"");
        }
        for (String str : stringArrayList) {
            a(str);
        }
    }
}
