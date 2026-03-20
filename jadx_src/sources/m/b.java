package m;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b<K, V> implements Iterable<Map.Entry<K, V>> {

    /* renamed from: a  reason: collision with root package name */
    c<K, V> f21810a;

    /* renamed from: b  reason: collision with root package name */
    private c<K, V> f21811b;

    /* renamed from: c  reason: collision with root package name */
    private final WeakHashMap<f<K, V>, Boolean> f21812c = new WeakHashMap<>();

    /* renamed from: d  reason: collision with root package name */
    private int f21813d = 0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a<K, V> extends e<K, V> {
        a(c<K, V> cVar, c<K, V> cVar2) {
            super(cVar, cVar2);
        }

        @Override // m.b.e
        c<K, V> b(c<K, V> cVar) {
            return cVar.f21817d;
        }

        @Override // m.b.e
        c<K, V> c(c<K, V> cVar) {
            return cVar.f21816c;
        }
    }

    /* renamed from: m.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class C0187b<K, V> extends e<K, V> {
        C0187b(c<K, V> cVar, c<K, V> cVar2) {
            super(cVar, cVar2);
        }

        @Override // m.b.e
        c<K, V> b(c<K, V> cVar) {
            return cVar.f21816c;
        }

        @Override // m.b.e
        c<K, V> c(c<K, V> cVar) {
            return cVar.f21817d;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c<K, V> implements Map.Entry<K, V> {

        /* renamed from: a  reason: collision with root package name */
        final K f21814a;

        /* renamed from: b  reason: collision with root package name */
        final V f21815b;

        /* renamed from: c  reason: collision with root package name */
        c<K, V> f21816c;

        /* renamed from: d  reason: collision with root package name */
        c<K, V> f21817d;

        c(K k8, V v8) {
            this.f21814a = k8;
            this.f21815b = v8;
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof c) {
                c cVar = (c) obj;
                return this.f21814a.equals(cVar.f21814a) && this.f21815b.equals(cVar.f21815b);
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public K getKey() {
            return this.f21814a;
        }

        @Override // java.util.Map.Entry
        public V getValue() {
            return this.f21815b;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return this.f21814a.hashCode() ^ this.f21815b.hashCode();
        }

        @Override // java.util.Map.Entry
        public V setValue(V v8) {
            throw new UnsupportedOperationException("An entry modification is not supported");
        }

        public String toString() {
            return this.f21814a + "=" + this.f21815b;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d extends f<K, V> implements Iterator<Map.Entry<K, V>> {

        /* renamed from: a  reason: collision with root package name */
        private c<K, V> f21818a;

        /* renamed from: b  reason: collision with root package name */
        private boolean f21819b = true;

        d() {
        }

        @Override // m.b.f
        void a(c<K, V> cVar) {
            c<K, V> cVar2 = this.f21818a;
            if (cVar == cVar2) {
                c<K, V> cVar3 = cVar2.f21817d;
                this.f21818a = cVar3;
                this.f21819b = cVar3 == null;
            }
        }

        @Override // java.util.Iterator
        /* renamed from: b */
        public Map.Entry<K, V> next() {
            c<K, V> cVar;
            if (this.f21819b) {
                this.f21819b = false;
                cVar = b.this.f21810a;
            } else {
                c<K, V> cVar2 = this.f21818a;
                cVar = cVar2 != null ? cVar2.f21816c : null;
            }
            this.f21818a = cVar;
            return this.f21818a;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            if (this.f21819b) {
                return b.this.f21810a != null;
            }
            c<K, V> cVar = this.f21818a;
            return (cVar == null || cVar.f21816c == null) ? false : true;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static abstract class e<K, V> extends f<K, V> implements Iterator<Map.Entry<K, V>> {

        /* renamed from: a  reason: collision with root package name */
        c<K, V> f21821a;

        /* renamed from: b  reason: collision with root package name */
        c<K, V> f21822b;

        e(c<K, V> cVar, c<K, V> cVar2) {
            this.f21821a = cVar2;
            this.f21822b = cVar;
        }

        private c<K, V> e() {
            c<K, V> cVar = this.f21822b;
            c<K, V> cVar2 = this.f21821a;
            if (cVar == cVar2 || cVar2 == null) {
                return null;
            }
            return c(cVar);
        }

        @Override // m.b.f
        public void a(c<K, V> cVar) {
            if (this.f21821a == cVar && cVar == this.f21822b) {
                this.f21822b = null;
                this.f21821a = null;
            }
            c<K, V> cVar2 = this.f21821a;
            if (cVar2 == cVar) {
                this.f21821a = b(cVar2);
            }
            if (this.f21822b == cVar) {
                this.f21822b = e();
            }
        }

        abstract c<K, V> b(c<K, V> cVar);

        abstract c<K, V> c(c<K, V> cVar);

        @Override // java.util.Iterator
        /* renamed from: d */
        public Map.Entry<K, V> next() {
            c<K, V> cVar = this.f21822b;
            this.f21822b = e();
            return cVar;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.f21822b != null;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class f<K, V> {
        abstract void a(c<K, V> cVar);
    }

    public Iterator<Map.Entry<K, V>> descendingIterator() {
        C0187b c0187b = new C0187b(this.f21811b, this.f21810a);
        this.f21812c.put(c0187b, Boolean.FALSE);
        return c0187b;
    }

    public Map.Entry<K, V> e() {
        return this.f21810a;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof b) {
            b bVar = (b) obj;
            if (size() != bVar.size()) {
                return false;
            }
            Iterator<Map.Entry<K, V>> it = iterator();
            Iterator<Map.Entry<K, V>> it2 = bVar.iterator();
            while (it.hasNext() && it2.hasNext()) {
                Map.Entry<K, V> next = it.next();
                Map.Entry<K, V> next2 = it2.next();
                if ((next == null && next2 != null) || (next != null && !next.equals(next2))) {
                    return false;
                }
            }
            return (it.hasNext() || it2.hasNext()) ? false : true;
        }
        return false;
    }

    protected c<K, V> g(K k8) {
        c<K, V> cVar = this.f21810a;
        while (cVar != null && !cVar.f21814a.equals(k8)) {
            cVar = cVar.f21816c;
        }
        return cVar;
    }

    public b<K, V>.d h() {
        b<K, V>.d dVar = new d();
        this.f21812c.put(dVar, Boolean.FALSE);
        return dVar;
    }

    public int hashCode() {
        Iterator<Map.Entry<K, V>> it = iterator();
        int i8 = 0;
        while (it.hasNext()) {
            i8 += it.next().hashCode();
        }
        return i8;
    }

    public Map.Entry<K, V> i() {
        return this.f21811b;
    }

    @Override // java.lang.Iterable
    public Iterator<Map.Entry<K, V>> iterator() {
        a aVar = new a(this.f21810a, this.f21811b);
        this.f21812c.put(aVar, Boolean.FALSE);
        return aVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public c<K, V> k(K k8, V v8) {
        c<K, V> cVar = new c<>(k8, v8);
        this.f21813d++;
        c<K, V> cVar2 = this.f21811b;
        if (cVar2 == null) {
            this.f21810a = cVar;
        } else {
            cVar2.f21816c = cVar;
            cVar.f21817d = cVar2;
        }
        this.f21811b = cVar;
        return cVar;
    }

    public V n(K k8, V v8) {
        c<K, V> g8 = g(k8);
        if (g8 != null) {
            return g8.f21815b;
        }
        k(k8, v8);
        return null;
    }

    public V p(K k8) {
        c<K, V> g8 = g(k8);
        if (g8 == null) {
            return null;
        }
        this.f21813d--;
        if (!this.f21812c.isEmpty()) {
            for (f<K, V> fVar : this.f21812c.keySet()) {
                fVar.a(g8);
            }
        }
        c<K, V> cVar = g8.f21817d;
        c<K, V> cVar2 = g8.f21816c;
        if (cVar != null) {
            cVar.f21816c = cVar2;
        } else {
            this.f21810a = cVar2;
        }
        c<K, V> cVar3 = g8.f21816c;
        if (cVar3 != null) {
            cVar3.f21817d = cVar;
        } else {
            this.f21811b = cVar;
        }
        g8.f21816c = null;
        g8.f21817d = null;
        return g8.f21815b;
    }

    public int size() {
        return this.f21813d;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Iterator<Map.Entry<K, V>> it = iterator();
        while (it.hasNext()) {
            sb.append(it.next().toString());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
