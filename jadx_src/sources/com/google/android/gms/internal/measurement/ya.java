package com.google.android.gms.internal.measurement;

import java.lang.Comparable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ya<K extends Comparable<K>, V> extends AbstractMap<K, V> {

    /* renamed from: a  reason: collision with root package name */
    private final int f12687a;

    /* renamed from: b  reason: collision with root package name */
    private List<kb> f12688b;

    /* renamed from: c  reason: collision with root package name */
    private Map<K, V> f12689c;

    /* renamed from: d  reason: collision with root package name */
    private boolean f12690d;

    /* renamed from: e  reason: collision with root package name */
    private volatile lb f12691e;

    /* renamed from: f  reason: collision with root package name */
    private Map<K, V> f12692f;

    /* renamed from: g  reason: collision with root package name */
    private volatile db f12693g;

    private ya(int i8) {
        this.f12687a = i8;
        this.f12688b = Collections.emptyList();
        this.f12689c = Collections.emptyMap();
        this.f12692f = Collections.emptyMap();
    }

    private final int b(K k8) {
        int size = this.f12688b.size() - 1;
        if (size >= 0) {
            int compareTo = k8.compareTo((Comparable) this.f12688b.get(size).getKey());
            if (compareTo > 0) {
                return -(size + 2);
            }
            if (compareTo == 0) {
                return size;
            }
        }
        int i8 = 0;
        while (i8 <= size) {
            int i9 = (i8 + size) / 2;
            int compareTo2 = k8.compareTo((Comparable) this.f12688b.get(i9).getKey());
            if (compareTo2 < 0) {
                size = i9 - 1;
            } else if (compareTo2 <= 0) {
                return i9;
            } else {
                i8 = i9 + 1;
            }
        }
        return -(i8 + 1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <FieldDescriptorType extends q8<FieldDescriptorType>> ya<FieldDescriptorType, Object> c(int i8) {
        return new cb(i8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final V l(int i8) {
        s();
        V v8 = (V) this.f12688b.remove(i8).getValue();
        if (!this.f12689c.isEmpty()) {
            Iterator<Map.Entry<K, V>> it = r().entrySet().iterator();
            this.f12688b.add(new kb(this, it.next()));
            it.remove();
        }
        return v8;
    }

    private final SortedMap<K, V> r() {
        s();
        if (this.f12689c.isEmpty() && !(this.f12689c instanceof TreeMap)) {
            TreeMap treeMap = new TreeMap();
            this.f12689c = treeMap;
            this.f12692f = treeMap.descendingMap();
        }
        return (SortedMap) this.f12689c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void s() {
        if (this.f12690d) {
            throw new UnsupportedOperationException();
        }
    }

    public final int a() {
        return this.f12688b.size();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public void clear() {
        s();
        if (!this.f12688b.isEmpty()) {
            this.f12688b.clear();
        }
        if (this.f12689c.isEmpty()) {
            return;
        }
        this.f12689c.clear();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object obj) {
        Comparable comparable = (Comparable) obj;
        return b(comparable) >= 0 || this.f12689c.containsKey(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        if (this.f12691e == null) {
            this.f12691e = new lb(this);
        }
        return this.f12691e;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object obj) {
        Object obj2;
        Object obj3;
        if (this == obj) {
            return true;
        }
        if (obj instanceof ya) {
            ya yaVar = (ya) obj;
            int size = size();
            if (size != yaVar.size()) {
                return false;
            }
            int a9 = a();
            if (a9 != yaVar.a()) {
                obj2 = entrySet();
                obj3 = yaVar.entrySet();
            } else {
                for (int i8 = 0; i8 < a9; i8++) {
                    if (!j(i8).equals(yaVar.j(i8))) {
                        return false;
                    }
                }
                if (a9 == size) {
                    return true;
                }
                obj2 = this.f12689c;
                obj3 = yaVar.f12689c;
            }
            return obj2.equals(obj3);
        }
        return super.equals(obj);
    }

    @Override // java.util.AbstractMap, java.util.Map
    /* renamed from: f */
    public final V put(K k8, V v8) {
        s();
        int b9 = b(k8);
        if (b9 >= 0) {
            return (V) this.f12688b.get(b9).setValue(v8);
        }
        s();
        if (this.f12688b.isEmpty() && !(this.f12688b instanceof ArrayList)) {
            this.f12688b = new ArrayList(this.f12687a);
        }
        int i8 = -(b9 + 1);
        if (i8 >= this.f12687a) {
            return r().put(k8, v8);
        }
        int size = this.f12688b.size();
        int i9 = this.f12687a;
        if (size == i9) {
            kb remove = this.f12688b.remove(i9 - 1);
            r().put((K) remove.getKey(), (V) remove.getValue());
        }
        this.f12688b.add(i8, new kb(this, k8, v8));
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Comparable comparable = (Comparable) obj;
        int b9 = b(comparable);
        return b9 >= 0 ? (V) this.f12688b.get(b9).getValue() : this.f12689c.get(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        int a9 = a();
        int i8 = 0;
        for (int i9 = 0; i9 < a9; i9++) {
            i8 += this.f12688b.get(i9).hashCode();
        }
        return this.f12689c.size() > 0 ? i8 + this.f12689c.hashCode() : i8;
    }

    public final Iterable<Map.Entry<K, V>> i() {
        return this.f12689c.isEmpty() ? fb.a() : this.f12689c.entrySet();
    }

    public final Map.Entry<K, V> j(int i8) {
        return this.f12688b.get(i8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Set<Map.Entry<K, V>> n() {
        if (this.f12693g == null) {
            this.f12693g = new db(this);
        }
        return this.f12693g;
    }

    public void o() {
        if (this.f12690d) {
            return;
        }
        this.f12689c = this.f12689c.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.f12689c);
        this.f12692f = this.f12692f.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.f12692f);
        this.f12690d = true;
    }

    public final boolean q() {
        return this.f12690d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public V remove(Object obj) {
        s();
        Comparable comparable = (Comparable) obj;
        int b9 = b(comparable);
        if (b9 >= 0) {
            return (V) l(b9);
        }
        if (this.f12689c.isEmpty()) {
            return null;
        }
        return this.f12689c.remove(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return this.f12688b.size() + this.f12689c.size();
    }
}
