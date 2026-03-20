package androidx.coordinatorlayout.widget;

import androidx.core.util.e;
import androidx.core.util.f;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import k0.g;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a<T> {

    /* renamed from: a  reason: collision with root package name */
    private final e<ArrayList<T>> f4402a = new f(10);

    /* renamed from: b  reason: collision with root package name */
    private final g<T, ArrayList<T>> f4403b = new g<>();

    /* renamed from: c  reason: collision with root package name */
    private final ArrayList<T> f4404c = new ArrayList<>();

    /* renamed from: d  reason: collision with root package name */
    private final HashSet<T> f4405d = new HashSet<>();

    private void e(T t8, ArrayList<T> arrayList, HashSet<T> hashSet) {
        if (arrayList.contains(t8)) {
            return;
        }
        if (hashSet.contains(t8)) {
            throw new RuntimeException("This graph contains cyclic dependencies");
        }
        hashSet.add(t8);
        ArrayList<T> arrayList2 = this.f4403b.get(t8);
        if (arrayList2 != null) {
            int size = arrayList2.size();
            for (int i8 = 0; i8 < size; i8++) {
                e(arrayList2.get(i8), arrayList, hashSet);
            }
        }
        hashSet.remove(t8);
        arrayList.add(t8);
    }

    private ArrayList<T> f() {
        ArrayList<T> b9 = this.f4402a.b();
        return b9 == null ? new ArrayList<>() : b9;
    }

    private void k(ArrayList<T> arrayList) {
        arrayList.clear();
        this.f4402a.a(arrayList);
    }

    public void a(T t8, T t9) {
        if (!this.f4403b.containsKey(t8) || !this.f4403b.containsKey(t9)) {
            throw new IllegalArgumentException("All nodes must be present in the graph before being added as an edge");
        }
        ArrayList<T> arrayList = this.f4403b.get(t8);
        if (arrayList == null) {
            arrayList = f();
            this.f4403b.put(t8, arrayList);
        }
        arrayList.add(t9);
    }

    public void b(T t8) {
        if (this.f4403b.containsKey(t8)) {
            return;
        }
        this.f4403b.put(t8, null);
    }

    public void c() {
        int size = this.f4403b.size();
        for (int i8 = 0; i8 < size; i8++) {
            ArrayList<T> o5 = this.f4403b.o(i8);
            if (o5 != null) {
                k(o5);
            }
        }
        this.f4403b.clear();
    }

    public boolean d(T t8) {
        return this.f4403b.containsKey(t8);
    }

    public List g(T t8) {
        return this.f4403b.get(t8);
    }

    public List<T> h(T t8) {
        int size = this.f4403b.size();
        ArrayList arrayList = null;
        for (int i8 = 0; i8 < size; i8++) {
            ArrayList<T> o5 = this.f4403b.o(i8);
            if (o5 != null && o5.contains(t8)) {
                if (arrayList == null) {
                    arrayList = new ArrayList();
                }
                arrayList.add(this.f4403b.k(i8));
            }
        }
        return arrayList;
    }

    public ArrayList<T> i() {
        this.f4404c.clear();
        this.f4405d.clear();
        int size = this.f4403b.size();
        for (int i8 = 0; i8 < size; i8++) {
            e(this.f4403b.k(i8), this.f4404c, this.f4405d);
        }
        return this.f4404c;
    }

    public boolean j(T t8) {
        int size = this.f4403b.size();
        for (int i8 = 0; i8 < size; i8++) {
            ArrayList<T> o5 = this.f4403b.o(i8);
            if (o5 != null && o5.contains(t8)) {
                return true;
            }
        }
        return false;
    }
}
