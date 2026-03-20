package androidx.camera.core;

import androidx.camera.core.impl.CameraInternal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t {

    /* renamed from: b  reason: collision with root package name */
    public static final t f2810b = new a().d(0).b();

    /* renamed from: c  reason: collision with root package name */
    public static final t f2811c = new a().d(1).b();

    /* renamed from: a  reason: collision with root package name */
    private LinkedHashSet<r> f2812a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final LinkedHashSet<r> f2813a;

        public a() {
            this.f2813a = new LinkedHashSet<>();
        }

        private a(LinkedHashSet<r> linkedHashSet) {
            this.f2813a = new LinkedHashSet<>(linkedHashSet);
        }

        public static a c(t tVar) {
            return new a(tVar.c());
        }

        public a a(r rVar) {
            this.f2813a.add(rVar);
            return this;
        }

        public t b() {
            return new t(this.f2813a);
        }

        public a d(int i8) {
            this.f2813a.add(new y.i0(i8));
            return this;
        }
    }

    t(LinkedHashSet<r> linkedHashSet) {
        this.f2812a = linkedHashSet;
    }

    public LinkedHashSet<CameraInternal> a(LinkedHashSet<CameraInternal> linkedHashSet) {
        ArrayList arrayList = new ArrayList();
        Iterator<CameraInternal> it = linkedHashSet.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().b());
        }
        List<s> b9 = b(arrayList);
        LinkedHashSet<CameraInternal> linkedHashSet2 = new LinkedHashSet<>();
        Iterator<CameraInternal> it2 = linkedHashSet.iterator();
        while (it2.hasNext()) {
            CameraInternal next = it2.next();
            if (b9.contains(next.b())) {
                linkedHashSet2.add(next);
            }
        }
        return linkedHashSet2;
    }

    public List<s> b(List<s> list) {
        List<s> arrayList = new ArrayList<>(list);
        Iterator<r> it = this.f2812a.iterator();
        while (it.hasNext()) {
            arrayList = it.next().b(Collections.unmodifiableList(arrayList));
        }
        arrayList.retainAll(list);
        return arrayList;
    }

    public LinkedHashSet<r> c() {
        return this.f2812a;
    }

    public Integer d() {
        Iterator<r> it = this.f2812a.iterator();
        Integer num = null;
        while (it.hasNext()) {
            r next = it.next();
            if (next instanceof y.i0) {
                Integer valueOf = Integer.valueOf(((y.i0) next).c());
                if (num == null) {
                    num = valueOf;
                } else if (!num.equals(valueOf)) {
                    throw new IllegalStateException("Multiple conflicting lens facing requirements exist.");
                }
            }
        }
        return num;
    }

    public CameraInternal e(LinkedHashSet<CameraInternal> linkedHashSet) {
        Iterator<CameraInternal> it = a(linkedHashSet).iterator();
        if (it.hasNext()) {
            return it.next();
        }
        throw new IllegalArgumentException("No available camera can be found");
    }
}
