package androidx.activity.result;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.h;
import androidx.lifecycle.j;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class ActivityResultRegistry {

    /* renamed from: a  reason: collision with root package name */
    private Random f415a = new Random();

    /* renamed from: b  reason: collision with root package name */
    private final Map<Integer, String> f416b = new HashMap();

    /* renamed from: c  reason: collision with root package name */
    final Map<String, Integer> f417c = new HashMap();

    /* renamed from: d  reason: collision with root package name */
    private final Map<String, d> f418d = new HashMap();

    /* renamed from: e  reason: collision with root package name */
    ArrayList<String> f419e = new ArrayList<>();

    /* renamed from: f  reason: collision with root package name */
    final transient Map<String, c<?>> f420f = new HashMap();

    /* renamed from: g  reason: collision with root package name */
    final Map<String, Object> f421g = new HashMap();

    /* renamed from: h  reason: collision with root package name */
    final Bundle f422h = new Bundle();

    /* JADX INFO: Add missing generic type declarations: [I] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a<I> extends androidx.activity.result.b<I> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ String f427a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ f.a f428b;

        a(String str, f.a aVar) {
            this.f427a = str;
            this.f428b = aVar;
        }

        @Override // androidx.activity.result.b
        public void b(I i8, androidx.core.app.c cVar) {
            Integer num = ActivityResultRegistry.this.f417c.get(this.f427a);
            if (num != null) {
                ActivityResultRegistry.this.f419e.add(this.f427a);
                try {
                    ActivityResultRegistry.this.f(num.intValue(), this.f428b, i8, cVar);
                    return;
                } catch (Exception e8) {
                    ActivityResultRegistry.this.f419e.remove(this.f427a);
                    throw e8;
                }
            }
            throw new IllegalStateException("Attempting to launch an unregistered ActivityResultLauncher with contract " + this.f428b + " and input " + i8 + ". You must ensure the ActivityResultLauncher is registered before calling launch().");
        }

        @Override // androidx.activity.result.b
        public void c() {
            ActivityResultRegistry.this.l(this.f427a);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX INFO: Add missing generic type declarations: [I] */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b<I> extends androidx.activity.result.b<I> {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ String f430a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ f.a f431b;

        b(String str, f.a aVar) {
            this.f430a = str;
            this.f431b = aVar;
        }

        @Override // androidx.activity.result.b
        public void b(I i8, androidx.core.app.c cVar) {
            Integer num = ActivityResultRegistry.this.f417c.get(this.f430a);
            if (num != null) {
                ActivityResultRegistry.this.f419e.add(this.f430a);
                try {
                    ActivityResultRegistry.this.f(num.intValue(), this.f431b, i8, cVar);
                    return;
                } catch (Exception e8) {
                    ActivityResultRegistry.this.f419e.remove(this.f430a);
                    throw e8;
                }
            }
            throw new IllegalStateException("Attempting to launch an unregistered ActivityResultLauncher with contract " + this.f431b + " and input " + i8 + ". You must ensure the ActivityResultLauncher is registered before calling launch().");
        }

        @Override // androidx.activity.result.b
        public void c() {
            ActivityResultRegistry.this.l(this.f430a);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c<O> {

        /* renamed from: a  reason: collision with root package name */
        final androidx.activity.result.a<O> f433a;

        /* renamed from: b  reason: collision with root package name */
        final f.a<?, O> f434b;

        c(androidx.activity.result.a<O> aVar, f.a<?, O> aVar2) {
            this.f433a = aVar;
            this.f434b = aVar2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {

        /* renamed from: a  reason: collision with root package name */
        final Lifecycle f435a;

        /* renamed from: b  reason: collision with root package name */
        private final ArrayList<h> f436b = new ArrayList<>();

        d(Lifecycle lifecycle) {
            this.f435a = lifecycle;
        }

        void a(h hVar) {
            this.f435a.a(hVar);
            this.f436b.add(hVar);
        }

        void b() {
            Iterator<h> it = this.f436b.iterator();
            while (it.hasNext()) {
                this.f435a.c(it.next());
            }
            this.f436b.clear();
        }
    }

    private void a(int i8, String str) {
        this.f416b.put(Integer.valueOf(i8), str);
        this.f417c.put(str, Integer.valueOf(i8));
    }

    private <O> void d(String str, int i8, Intent intent, c<O> cVar) {
        if (cVar == null || cVar.f433a == null || !this.f419e.contains(str)) {
            this.f421g.remove(str);
            this.f422h.putParcelable(str, new ActivityResult(i8, intent));
            return;
        }
        cVar.f433a.a(cVar.f434b.c(i8, intent));
        this.f419e.remove(str);
    }

    private int e() {
        int nextInt = this.f415a.nextInt(2147418112);
        while (true) {
            int i8 = nextInt + 65536;
            if (!this.f416b.containsKey(Integer.valueOf(i8))) {
                return i8;
            }
            nextInt = this.f415a.nextInt(2147418112);
        }
    }

    private void k(String str) {
        if (this.f417c.get(str) != null) {
            return;
        }
        a(e(), str);
    }

    public final boolean b(int i8, int i9, Intent intent) {
        String str = this.f416b.get(Integer.valueOf(i8));
        if (str == null) {
            return false;
        }
        d(str, i9, intent, this.f420f.get(str));
        return true;
    }

    public final <O> boolean c(int i8, @SuppressLint({"UnknownNullness"}) O o5) {
        androidx.activity.result.a<?> aVar;
        String str = this.f416b.get(Integer.valueOf(i8));
        if (str == null) {
            return false;
        }
        c<?> cVar = this.f420f.get(str);
        if (cVar == null || (aVar = cVar.f433a) == null) {
            this.f422h.remove(str);
            this.f421g.put(str, o5);
            return true;
        } else if (this.f419e.remove(str)) {
            aVar.a(o5);
            return true;
        } else {
            return true;
        }
    }

    public abstract <I, O> void f(int i8, f.a<I, O> aVar, @SuppressLint({"UnknownNullness"}) I i9, androidx.core.app.c cVar);

    public final void g(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        ArrayList<Integer> integerArrayList = bundle.getIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS");
        ArrayList<String> stringArrayList = bundle.getStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS");
        if (stringArrayList == null || integerArrayList == null) {
            return;
        }
        this.f419e = bundle.getStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS");
        this.f415a = (Random) bundle.getSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT");
        this.f422h.putAll(bundle.getBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT"));
        for (int i8 = 0; i8 < stringArrayList.size(); i8++) {
            String str = stringArrayList.get(i8);
            if (this.f417c.containsKey(str)) {
                Integer remove = this.f417c.remove(str);
                if (!this.f422h.containsKey(str)) {
                    this.f416b.remove(remove);
                }
            }
            a(integerArrayList.get(i8).intValue(), stringArrayList.get(i8));
        }
    }

    public final void h(Bundle bundle) {
        bundle.putIntegerArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_RCS", new ArrayList<>(this.f417c.values()));
        bundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_REGISTERED_KEYS", new ArrayList<>(this.f417c.keySet()));
        bundle.putStringArrayList("KEY_COMPONENT_ACTIVITY_LAUNCHED_KEYS", new ArrayList<>(this.f419e));
        bundle.putBundle("KEY_COMPONENT_ACTIVITY_PENDING_RESULT", (Bundle) this.f422h.clone());
        bundle.putSerializable("KEY_COMPONENT_ACTIVITY_RANDOM_OBJECT", this.f415a);
    }

    public final <I, O> androidx.activity.result.b<I> i(final String str, j jVar, final f.a<I, O> aVar, final androidx.activity.result.a<O> aVar2) {
        Lifecycle lifecycle = jVar.getLifecycle();
        if (lifecycle.b().f(Lifecycle.State.STARTED)) {
            throw new IllegalStateException("LifecycleOwner " + jVar + " is attempting to register while current state is " + lifecycle.b() + ". LifecycleOwners must call register before they are STARTED.");
        }
        k(str);
        d dVar = this.f418d.get(str);
        if (dVar == null) {
            dVar = new d(lifecycle);
        }
        dVar.a(new h() { // from class: androidx.activity.result.ActivityResultRegistry.1
            @Override // androidx.lifecycle.h
            public void c(j jVar2, Lifecycle.Event event) {
                if (!Lifecycle.Event.ON_START.equals(event)) {
                    if (Lifecycle.Event.ON_STOP.equals(event)) {
                        ActivityResultRegistry.this.f420f.remove(str);
                        return;
                    } else if (Lifecycle.Event.ON_DESTROY.equals(event)) {
                        ActivityResultRegistry.this.l(str);
                        return;
                    } else {
                        return;
                    }
                }
                ActivityResultRegistry.this.f420f.put(str, new c<>(aVar2, aVar));
                if (ActivityResultRegistry.this.f421g.containsKey(str)) {
                    Object obj = ActivityResultRegistry.this.f421g.get(str);
                    ActivityResultRegistry.this.f421g.remove(str);
                    aVar2.a(obj);
                }
                ActivityResult activityResult = (ActivityResult) ActivityResultRegistry.this.f422h.getParcelable(str);
                if (activityResult != null) {
                    ActivityResultRegistry.this.f422h.remove(str);
                    aVar2.a(aVar.c(activityResult.b(), activityResult.a()));
                }
            }
        });
        this.f418d.put(str, dVar);
        return new a(str, aVar);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final <I, O> androidx.activity.result.b<I> j(String str, f.a<I, O> aVar, androidx.activity.result.a<O> aVar2) {
        k(str);
        this.f420f.put(str, new c<>(aVar2, aVar));
        if (this.f421g.containsKey(str)) {
            Object obj = this.f421g.get(str);
            this.f421g.remove(str);
            aVar2.a(obj);
        }
        ActivityResult activityResult = (ActivityResult) this.f422h.getParcelable(str);
        if (activityResult != null) {
            this.f422h.remove(str);
            aVar2.a(aVar.c(activityResult.b(), activityResult.a()));
        }
        return new b(str, aVar);
    }

    final void l(String str) {
        Integer remove;
        if (!this.f419e.contains(str) && (remove = this.f417c.remove(str)) != null) {
            this.f416b.remove(remove);
        }
        this.f420f.remove(str);
        if (this.f421g.containsKey(str)) {
            Log.w("ActivityResultRegistry", "Dropping pending result for request " + str + ": " + this.f421g.get(str));
            this.f421g.remove(str);
        }
        if (this.f422h.containsKey(str)) {
            Log.w("ActivityResultRegistry", "Dropping pending result for request " + str + ": " + this.f422h.getParcelable(str));
            this.f422h.remove(str);
        }
        d dVar = this.f418d.get(str);
        if (dVar != null) {
            dVar.b();
            this.f418d.remove(str);
        }
    }
}
