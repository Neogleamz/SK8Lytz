package b6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i<E> implements Iterable<E> {

    /* renamed from: a  reason: collision with root package name */
    private final Object f8055a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private final Map<E, Integer> f8056b = new HashMap();

    /* renamed from: c  reason: collision with root package name */
    private Set<E> f8057c = Collections.emptySet();

    /* renamed from: d  reason: collision with root package name */
    private List<E> f8058d = Collections.emptyList();

    public void e(E e8) {
        synchronized (this.f8055a) {
            ArrayList arrayList = new ArrayList(this.f8058d);
            arrayList.add(e8);
            this.f8058d = Collections.unmodifiableList(arrayList);
            Integer num = this.f8056b.get(e8);
            if (num == null) {
                HashSet hashSet = new HashSet(this.f8057c);
                hashSet.add(e8);
                this.f8057c = Collections.unmodifiableSet(hashSet);
            }
            this.f8056b.put(e8, Integer.valueOf(num != null ? 1 + num.intValue() : 1));
        }
    }

    public void g(E e8) {
        synchronized (this.f8055a) {
            Integer num = this.f8056b.get(e8);
            if (num == null) {
                return;
            }
            ArrayList arrayList = new ArrayList(this.f8058d);
            arrayList.remove(e8);
            this.f8058d = Collections.unmodifiableList(arrayList);
            if (num.intValue() == 1) {
                this.f8056b.remove(e8);
                HashSet hashSet = new HashSet(this.f8057c);
                hashSet.remove(e8);
                this.f8057c = Collections.unmodifiableSet(hashSet);
            } else {
                this.f8056b.put(e8, Integer.valueOf(num.intValue() - 1));
            }
        }
    }

    @Override // java.lang.Iterable
    public Iterator<E> iterator() {
        Iterator<E> it;
        synchronized (this.f8055a) {
            it = this.f8058d.iterator();
        }
        return it;
    }

    public Set<E> l() {
        Set<E> set;
        synchronized (this.f8055a) {
            set = this.f8057c;
        }
        return set;
    }

    public int m0(E e8) {
        int intValue;
        synchronized (this.f8055a) {
            intValue = this.f8056b.containsKey(e8) ? this.f8056b.get(e8).intValue() : 0;
        }
        return intValue;
    }
}
