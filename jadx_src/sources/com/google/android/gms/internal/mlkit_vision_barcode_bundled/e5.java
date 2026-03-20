package com.google.android.gms.internal.mlkit_vision_barcode_bundled;

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
public class e5 extends AbstractMap {

    /* renamed from: a  reason: collision with root package name */
    private final int f14756a;

    /* renamed from: d  reason: collision with root package name */
    private boolean f14759d;

    /* renamed from: e  reason: collision with root package name */
    private volatile c5 f14760e;

    /* renamed from: b  reason: collision with root package name */
    private List f14757b = Collections.emptyList();

    /* renamed from: c  reason: collision with root package name */
    private Map f14758c = Collections.emptyMap();

    /* renamed from: f  reason: collision with root package name */
    private Map f14761f = Collections.emptyMap();

    private final int m(Comparable comparable) {
        int size = this.f14757b.size() - 1;
        int i8 = 0;
        if (size >= 0) {
            int compareTo = comparable.compareTo(((y4) this.f14757b.get(size)).c());
            if (compareTo > 0) {
                return -(size + 2);
            }
            if (compareTo == 0) {
                return size;
            }
        }
        while (i8 <= size) {
            int i9 = (i8 + size) / 2;
            int compareTo2 = comparable.compareTo(((y4) this.f14757b.get(i9)).c());
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

    /* JADX INFO: Access modifiers changed from: private */
    public final Object n(int i8) {
        p();
        Object value = ((y4) this.f14757b.remove(i8)).getValue();
        if (!this.f14758c.isEmpty()) {
            Iterator it = o().entrySet().iterator();
            List list = this.f14757b;
            Map.Entry entry = (Map.Entry) it.next();
            list.add(new y4(this, (Comparable) entry.getKey(), entry.getValue()));
            it.remove();
        }
        return value;
    }

    private final SortedMap o() {
        p();
        if (this.f14758c.isEmpty() && !(this.f14758c instanceof TreeMap)) {
            TreeMap treeMap = new TreeMap();
            this.f14758c = treeMap;
            this.f14761f = treeMap.descendingMap();
        }
        return (SortedMap) this.f14758c;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void p() {
        if (this.f14759d) {
            throw new UnsupportedOperationException();
        }
    }

    public void a() {
        if (this.f14759d) {
            return;
        }
        this.f14758c = this.f14758c.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.f14758c);
        this.f14761f = this.f14761f.isEmpty() ? Collections.emptyMap() : Collections.unmodifiableMap(this.f14761f);
        this.f14759d = true;
    }

    public final int b() {
        return this.f14757b.size();
    }

    public final Iterable c() {
        return this.f14758c.isEmpty() ? x4.a() : this.f14758c.entrySet();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final void clear() {
        p();
        if (!this.f14757b.isEmpty()) {
            this.f14757b.clear();
        }
        if (this.f14758c.isEmpty()) {
            return;
        }
        this.f14758c.clear();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean containsKey(Object obj) {
        Comparable comparable = (Comparable) obj;
        return m(comparable) >= 0 || this.f14758c.containsKey(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Set entrySet() {
        if (this.f14760e == null) {
            this.f14760e = new c5(this, null);
        }
        return this.f14760e;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final boolean equals(Object obj) {
        Object entrySet;
        Object entrySet2;
        if (this == obj) {
            return true;
        }
        if (obj instanceof e5) {
            e5 e5Var = (e5) obj;
            int size = size();
            if (size != e5Var.size()) {
                return false;
            }
            int b9 = b();
            if (b9 == e5Var.b()) {
                for (int i8 = 0; i8 < b9; i8++) {
                    if (!i(i8).equals(e5Var.i(i8))) {
                        return false;
                    }
                }
                if (b9 == size) {
                    return true;
                }
                entrySet = this.f14758c;
                entrySet2 = e5Var.f14758c;
            } else {
                entrySet = entrySet();
                entrySet2 = e5Var.entrySet();
            }
            return entrySet.equals(entrySet2);
        }
        return super.equals(obj);
    }

    @Override // java.util.AbstractMap, java.util.Map
    /* renamed from: f */
    public final Object put(Comparable comparable, Object obj) {
        p();
        int m8 = m(comparable);
        if (m8 >= 0) {
            return ((y4) this.f14757b.get(m8)).setValue(obj);
        }
        p();
        if (this.f14757b.isEmpty() && !(this.f14757b instanceof ArrayList)) {
            this.f14757b = new ArrayList(this.f14756a);
        }
        int i8 = -(m8 + 1);
        if (i8 >= this.f14756a) {
            return o().put(comparable, obj);
        }
        int size = this.f14757b.size();
        int i9 = this.f14756a;
        if (size == i9) {
            y4 y4Var = (y4) this.f14757b.remove(i9 - 1);
            o().put(y4Var.c(), y4Var.getValue());
        }
        this.f14757b.add(i8, new y4(this, comparable, obj));
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Object get(Object obj) {
        Comparable comparable = (Comparable) obj;
        int m8 = m(comparable);
        return m8 >= 0 ? ((y4) this.f14757b.get(m8)).getValue() : this.f14758c.get(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int hashCode() {
        int b9 = b();
        int i8 = 0;
        for (int i9 = 0; i9 < b9; i9++) {
            i8 += ((y4) this.f14757b.get(i9)).hashCode();
        }
        return this.f14758c.size() > 0 ? i8 + this.f14758c.hashCode() : i8;
    }

    public final Map.Entry i(int i8) {
        return (Map.Entry) this.f14757b.get(i8);
    }

    public final boolean l() {
        return this.f14759d;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final Object remove(Object obj) {
        p();
        Comparable comparable = (Comparable) obj;
        int m8 = m(comparable);
        if (m8 >= 0) {
            return n(m8);
        }
        if (this.f14758c.isEmpty()) {
            return null;
        }
        return this.f14758c.remove(comparable);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public final int size() {
        return this.f14757b.size() + this.f14758c.size();
    }
}
