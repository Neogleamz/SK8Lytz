package androidx.recyclerview.widget;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.t;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a implements t.a {

    /* renamed from: a  reason: collision with root package name */
    private androidx.core.util.e<b> f6770a;

    /* renamed from: b  reason: collision with root package name */
    final ArrayList<b> f6771b;

    /* renamed from: c  reason: collision with root package name */
    final ArrayList<b> f6772c;

    /* renamed from: d  reason: collision with root package name */
    final InterfaceC0072a f6773d;

    /* renamed from: e  reason: collision with root package name */
    Runnable f6774e;

    /* renamed from: f  reason: collision with root package name */
    final boolean f6775f;

    /* renamed from: g  reason: collision with root package name */
    final t f6776g;

    /* renamed from: h  reason: collision with root package name */
    private int f6777h;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.recyclerview.widget.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface InterfaceC0072a {
        void a(int i8, int i9);

        void b(b bVar);

        void c(int i8, int i9, Object obj);

        void d(b bVar);

        RecyclerView.b0 e(int i8);

        void f(int i8, int i9);

        void g(int i8, int i9);

        void h(int i8, int i9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: a  reason: collision with root package name */
        int f6778a;

        /* renamed from: b  reason: collision with root package name */
        int f6779b;

        /* renamed from: c  reason: collision with root package name */
        Object f6780c;

        /* renamed from: d  reason: collision with root package name */
        int f6781d;

        b(int i8, int i9, int i10, Object obj) {
            this.f6778a = i8;
            this.f6779b = i9;
            this.f6781d = i10;
            this.f6780c = obj;
        }

        String a() {
            int i8 = this.f6778a;
            return i8 != 1 ? i8 != 2 ? i8 != 4 ? i8 != 8 ? "??" : "mv" : "up" : "rm" : "add";
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            b bVar = (b) obj;
            int i8 = this.f6778a;
            if (i8 != bVar.f6778a) {
                return false;
            }
            if (i8 == 8 && Math.abs(this.f6781d - this.f6779b) == 1 && this.f6781d == bVar.f6779b && this.f6779b == bVar.f6781d) {
                return true;
            }
            if (this.f6781d == bVar.f6781d && this.f6779b == bVar.f6779b) {
                Object obj2 = this.f6780c;
                Object obj3 = bVar.f6780c;
                if (obj2 != null) {
                    if (!obj2.equals(obj3)) {
                        return false;
                    }
                } else if (obj3 != null) {
                    return false;
                }
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (((this.f6778a * 31) + this.f6779b) * 31) + this.f6781d;
        }

        public String toString() {
            return Integer.toHexString(System.identityHashCode(this)) + "[" + a() + ",s:" + this.f6779b + "c:" + this.f6781d + ",p:" + this.f6780c + "]";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a(InterfaceC0072a interfaceC0072a) {
        this(interfaceC0072a, false);
    }

    a(InterfaceC0072a interfaceC0072a, boolean z4) {
        this.f6770a = new androidx.core.util.f(30);
        this.f6771b = new ArrayList<>();
        this.f6772c = new ArrayList<>();
        this.f6777h = 0;
        this.f6773d = interfaceC0072a;
        this.f6775f = z4;
        this.f6776g = new t(this);
    }

    private void c(b bVar) {
        v(bVar);
    }

    private void d(b bVar) {
        v(bVar);
    }

    private void f(b bVar) {
        boolean z4;
        char c9;
        int i8 = bVar.f6779b;
        int i9 = bVar.f6781d + i8;
        char c10 = 65535;
        int i10 = i8;
        int i11 = 0;
        while (i10 < i9) {
            if (this.f6773d.e(i10) != null || h(i10)) {
                if (c10 == 0) {
                    k(b(2, i8, i11, null));
                    z4 = true;
                } else {
                    z4 = false;
                }
                c9 = 1;
            } else {
                if (c10 == 1) {
                    v(b(2, i8, i11, null));
                    z4 = true;
                } else {
                    z4 = false;
                }
                c9 = 0;
            }
            if (z4) {
                i10 -= i11;
                i9 -= i11;
                i11 = 1;
            } else {
                i11++;
            }
            i10++;
            c10 = c9;
        }
        if (i11 != bVar.f6781d) {
            a(bVar);
            bVar = b(2, i8, i11, null);
        }
        if (c10 == 0) {
            k(bVar);
        } else {
            v(bVar);
        }
    }

    private void g(b bVar) {
        int i8 = bVar.f6779b;
        int i9 = bVar.f6781d + i8;
        int i10 = 0;
        boolean z4 = true;
        int i11 = i8;
        while (i8 < i9) {
            if (this.f6773d.e(i8) != null || h(i8)) {
                if (!z4) {
                    k(b(4, i11, i10, bVar.f6780c));
                    i11 = i8;
                    i10 = 0;
                }
                z4 = true;
            } else {
                if (z4) {
                    v(b(4, i11, i10, bVar.f6780c));
                    i11 = i8;
                    i10 = 0;
                }
                z4 = false;
            }
            i10++;
            i8++;
        }
        if (i10 != bVar.f6781d) {
            Object obj = bVar.f6780c;
            a(bVar);
            bVar = b(4, i11, i10, obj);
        }
        if (z4) {
            v(bVar);
        } else {
            k(bVar);
        }
    }

    private boolean h(int i8) {
        int size = this.f6772c.size();
        for (int i9 = 0; i9 < size; i9++) {
            b bVar = this.f6772c.get(i9);
            int i10 = bVar.f6778a;
            if (i10 == 8) {
                if (n(bVar.f6781d, i9 + 1) == i8) {
                    return true;
                }
            } else if (i10 == 1) {
                int i11 = bVar.f6779b;
                int i12 = bVar.f6781d + i11;
                while (i11 < i12) {
                    if (n(i11, i9 + 1) == i8) {
                        return true;
                    }
                    i11++;
                }
                continue;
            } else {
                continue;
            }
        }
        return false;
    }

    private void k(b bVar) {
        int i8;
        int i9 = bVar.f6778a;
        if (i9 == 1 || i9 == 8) {
            throw new IllegalArgumentException("should not dispatch add or move for pre layout");
        }
        int z4 = z(bVar.f6779b, i9);
        int i10 = bVar.f6779b;
        int i11 = bVar.f6778a;
        if (i11 == 2) {
            i8 = 0;
        } else if (i11 != 4) {
            throw new IllegalArgumentException("op should be remove or update." + bVar);
        } else {
            i8 = 1;
        }
        int i12 = 1;
        for (int i13 = 1; i13 < bVar.f6781d; i13++) {
            int z8 = z(bVar.f6779b + (i8 * i13), bVar.f6778a);
            int i14 = bVar.f6778a;
            if (i14 == 2 ? z8 == z4 : i14 == 4 && z8 == z4 + 1) {
                i12++;
            } else {
                b b9 = b(i14, z4, i12, bVar.f6780c);
                l(b9, i10);
                a(b9);
                if (bVar.f6778a == 4) {
                    i10 += i12;
                }
                i12 = 1;
                z4 = z8;
            }
        }
        Object obj = bVar.f6780c;
        a(bVar);
        if (i12 > 0) {
            b b10 = b(bVar.f6778a, z4, i12, obj);
            l(b10, i10);
            a(b10);
        }
    }

    private void v(b bVar) {
        this.f6772c.add(bVar);
        int i8 = bVar.f6778a;
        if (i8 == 1) {
            this.f6773d.g(bVar.f6779b, bVar.f6781d);
        } else if (i8 == 2) {
            this.f6773d.f(bVar.f6779b, bVar.f6781d);
        } else if (i8 == 4) {
            this.f6773d.c(bVar.f6779b, bVar.f6781d, bVar.f6780c);
        } else if (i8 == 8) {
            this.f6773d.a(bVar.f6779b, bVar.f6781d);
        } else {
            throw new IllegalArgumentException("Unknown update op type for " + bVar);
        }
    }

    private int z(int i8, int i9) {
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        for (int size = this.f6772c.size() - 1; size >= 0; size--) {
            b bVar = this.f6772c.get(size);
            int i16 = bVar.f6778a;
            if (i16 == 8) {
                int i17 = bVar.f6779b;
                int i18 = bVar.f6781d;
                if (i17 < i18) {
                    i12 = i17;
                    i11 = i18;
                } else {
                    i11 = i17;
                    i12 = i18;
                }
                if (i8 < i12 || i8 > i11) {
                    if (i8 < i17) {
                        if (i9 == 1) {
                            bVar.f6779b = i17 + 1;
                            i13 = i18 + 1;
                        } else if (i9 == 2) {
                            bVar.f6779b = i17 - 1;
                            i13 = i18 - 1;
                        }
                        bVar.f6781d = i13;
                    }
                } else if (i12 == i17) {
                    if (i9 == 1) {
                        i15 = i18 + 1;
                    } else {
                        if (i9 == 2) {
                            i15 = i18 - 1;
                        }
                        i8++;
                    }
                    bVar.f6781d = i15;
                    i8++;
                } else {
                    if (i9 == 1) {
                        i14 = i17 + 1;
                    } else {
                        if (i9 == 2) {
                            i14 = i17 - 1;
                        }
                        i8--;
                    }
                    bVar.f6779b = i14;
                    i8--;
                }
            } else {
                int i19 = bVar.f6779b;
                if (i19 > i8) {
                    if (i9 == 1) {
                        i10 = i19 + 1;
                    } else if (i9 == 2) {
                        i10 = i19 - 1;
                    }
                    bVar.f6779b = i10;
                } else if (i16 == 1) {
                    i8 -= bVar.f6781d;
                } else if (i16 == 2) {
                    i8 += bVar.f6781d;
                }
            }
        }
        for (int size2 = this.f6772c.size() - 1; size2 >= 0; size2--) {
            b bVar2 = this.f6772c.get(size2);
            if (bVar2.f6778a == 8) {
                int i20 = bVar2.f6781d;
                if (i20 != bVar2.f6779b && i20 >= 0) {
                }
                this.f6772c.remove(size2);
                a(bVar2);
            } else {
                if (bVar2.f6781d > 0) {
                }
                this.f6772c.remove(size2);
                a(bVar2);
            }
        }
        return i8;
    }

    @Override // androidx.recyclerview.widget.t.a
    public void a(b bVar) {
        if (this.f6775f) {
            return;
        }
        bVar.f6780c = null;
        this.f6770a.a(bVar);
    }

    @Override // androidx.recyclerview.widget.t.a
    public b b(int i8, int i9, int i10, Object obj) {
        b b9 = this.f6770a.b();
        if (b9 == null) {
            return new b(i8, i9, i10, obj);
        }
        b9.f6778a = i8;
        b9.f6779b = i9;
        b9.f6781d = i10;
        b9.f6780c = obj;
        return b9;
    }

    public int e(int i8) {
        int size = this.f6771b.size();
        for (int i9 = 0; i9 < size; i9++) {
            b bVar = this.f6771b.get(i9);
            int i10 = bVar.f6778a;
            if (i10 != 1) {
                if (i10 == 2) {
                    int i11 = bVar.f6779b;
                    if (i11 <= i8) {
                        int i12 = bVar.f6781d;
                        if (i11 + i12 > i8) {
                            return -1;
                        }
                        i8 -= i12;
                    } else {
                        continue;
                    }
                } else if (i10 == 8) {
                    int i13 = bVar.f6779b;
                    if (i13 == i8) {
                        i8 = bVar.f6781d;
                    } else {
                        if (i13 < i8) {
                            i8--;
                        }
                        if (bVar.f6781d <= i8) {
                            i8++;
                        }
                    }
                }
            } else if (bVar.f6779b <= i8) {
                i8 += bVar.f6781d;
            }
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void i() {
        int size = this.f6772c.size();
        for (int i8 = 0; i8 < size; i8++) {
            this.f6773d.d(this.f6772c.get(i8));
        }
        x(this.f6772c);
        this.f6777h = 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        i();
        int size = this.f6771b.size();
        for (int i8 = 0; i8 < size; i8++) {
            b bVar = this.f6771b.get(i8);
            int i9 = bVar.f6778a;
            if (i9 == 1) {
                this.f6773d.d(bVar);
                this.f6773d.g(bVar.f6779b, bVar.f6781d);
            } else if (i9 == 2) {
                this.f6773d.d(bVar);
                this.f6773d.h(bVar.f6779b, bVar.f6781d);
            } else if (i9 == 4) {
                this.f6773d.d(bVar);
                this.f6773d.c(bVar.f6779b, bVar.f6781d, bVar.f6780c);
            } else if (i9 == 8) {
                this.f6773d.d(bVar);
                this.f6773d.a(bVar.f6779b, bVar.f6781d);
            }
            Runnable runnable = this.f6774e;
            if (runnable != null) {
                runnable.run();
            }
        }
        x(this.f6771b);
        this.f6777h = 0;
    }

    void l(b bVar, int i8) {
        this.f6773d.b(bVar);
        int i9 = bVar.f6778a;
        if (i9 == 2) {
            this.f6773d.h(i8, bVar.f6781d);
        } else if (i9 != 4) {
            throw new IllegalArgumentException("only remove and update ops can be dispatched in first pass");
        } else {
            this.f6773d.c(i8, bVar.f6781d, bVar.f6780c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int m(int i8) {
        return n(i8, 0);
    }

    int n(int i8, int i9) {
        int size = this.f6772c.size();
        while (i9 < size) {
            b bVar = this.f6772c.get(i9);
            int i10 = bVar.f6778a;
            if (i10 == 8) {
                int i11 = bVar.f6779b;
                if (i11 == i8) {
                    i8 = bVar.f6781d;
                } else {
                    if (i11 < i8) {
                        i8--;
                    }
                    if (bVar.f6781d <= i8) {
                        i8++;
                    }
                }
            } else {
                int i12 = bVar.f6779b;
                if (i12 > i8) {
                    continue;
                } else if (i10 == 2) {
                    int i13 = bVar.f6781d;
                    if (i8 < i12 + i13) {
                        return -1;
                    }
                    i8 -= i13;
                } else if (i10 == 1) {
                    i8 += bVar.f6781d;
                }
            }
            i9++;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean o(int i8) {
        return (i8 & this.f6777h) != 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean p() {
        return this.f6771b.size() > 0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean q() {
        return (this.f6772c.isEmpty() || this.f6771b.isEmpty()) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean r(int i8, int i9, Object obj) {
        if (i9 < 1) {
            return false;
        }
        this.f6771b.add(b(4, i8, i9, obj));
        this.f6777h |= 4;
        return this.f6771b.size() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean s(int i8, int i9) {
        if (i9 < 1) {
            return false;
        }
        this.f6771b.add(b(1, i8, i9, null));
        this.f6777h |= 1;
        return this.f6771b.size() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean t(int i8, int i9, int i10) {
        if (i8 == i9) {
            return false;
        }
        if (i10 == 1) {
            this.f6771b.add(b(8, i8, i9, null));
            this.f6777h |= 8;
            return this.f6771b.size() == 1;
        }
        throw new IllegalArgumentException("Moving more than 1 item is not supported yet");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean u(int i8, int i9) {
        if (i9 < 1) {
            return false;
        }
        this.f6771b.add(b(2, i8, i9, null));
        this.f6777h |= 2;
        return this.f6771b.size() == 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void w() {
        this.f6776g.b(this.f6771b);
        int size = this.f6771b.size();
        for (int i8 = 0; i8 < size; i8++) {
            b bVar = this.f6771b.get(i8);
            int i9 = bVar.f6778a;
            if (i9 == 1) {
                c(bVar);
            } else if (i9 == 2) {
                f(bVar);
            } else if (i9 == 4) {
                g(bVar);
            } else if (i9 == 8) {
                d(bVar);
            }
            Runnable runnable = this.f6774e;
            if (runnable != null) {
                runnable.run();
            }
        }
        this.f6771b.clear();
    }

    void x(List<b> list) {
        int size = list.size();
        for (int i8 = 0; i8 < size; i8++) {
            a(list.get(i8));
        }
        list.clear();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void y() {
        x(this.f6771b);
        x(this.f6772c);
        this.f6777h = 0;
    }
}
