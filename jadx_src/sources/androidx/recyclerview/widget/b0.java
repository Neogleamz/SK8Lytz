package androidx.recyclerview.widget;

import androidx.recyclerview.widget.RecyclerView;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b0 {

    /* renamed from: a  reason: collision with root package name */
    final k0.g<RecyclerView.b0, a> f6790a = new k0.g<>();

    /* renamed from: b  reason: collision with root package name */
    final k0.d<RecyclerView.b0> f6791b = new k0.d<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: d  reason: collision with root package name */
        static androidx.core.util.e<a> f6792d = new androidx.core.util.f(20);

        /* renamed from: a  reason: collision with root package name */
        int f6793a;

        /* renamed from: b  reason: collision with root package name */
        RecyclerView.l.c f6794b;

        /* renamed from: c  reason: collision with root package name */
        RecyclerView.l.c f6795c;

        private a() {
        }

        static void a() {
            do {
            } while (f6792d.b() != null);
        }

        static a b() {
            a b9 = f6792d.b();
            return b9 == null ? new a() : b9;
        }

        static void c(a aVar) {
            aVar.f6793a = 0;
            aVar.f6794b = null;
            aVar.f6795c = null;
            f6792d.a(aVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a(RecyclerView.b0 b0Var);

        void b(RecyclerView.b0 b0Var, RecyclerView.l.c cVar, RecyclerView.l.c cVar2);

        void c(RecyclerView.b0 b0Var, RecyclerView.l.c cVar, RecyclerView.l.c cVar2);

        void d(RecyclerView.b0 b0Var, RecyclerView.l.c cVar, RecyclerView.l.c cVar2);
    }

    private RecyclerView.l.c l(RecyclerView.b0 b0Var, int i8) {
        a o5;
        RecyclerView.l.c cVar;
        int h8 = this.f6790a.h(b0Var);
        if (h8 >= 0 && (o5 = this.f6790a.o(h8)) != null) {
            int i9 = o5.f6793a;
            if ((i9 & i8) != 0) {
                int i10 = (~i8) & i9;
                o5.f6793a = i10;
                if (i8 == 4) {
                    cVar = o5.f6794b;
                } else if (i8 != 8) {
                    throw new IllegalArgumentException("Must provide flag PRE or POST");
                } else {
                    cVar = o5.f6795c;
                }
                if ((i10 & 12) == 0) {
                    this.f6790a.m(h8);
                    a.c(o5);
                }
                return cVar;
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void a(RecyclerView.b0 b0Var, RecyclerView.l.c cVar) {
        a aVar = this.f6790a.get(b0Var);
        if (aVar == null) {
            aVar = a.b();
            this.f6790a.put(b0Var, aVar);
        }
        aVar.f6793a |= 2;
        aVar.f6794b = cVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(RecyclerView.b0 b0Var) {
        a aVar = this.f6790a.get(b0Var);
        if (aVar == null) {
            aVar = a.b();
            this.f6790a.put(b0Var, aVar);
        }
        aVar.f6793a |= 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c(long j8, RecyclerView.b0 b0Var) {
        this.f6791b.l(j8, b0Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d(RecyclerView.b0 b0Var, RecyclerView.l.c cVar) {
        a aVar = this.f6790a.get(b0Var);
        if (aVar == null) {
            aVar = a.b();
            this.f6790a.put(b0Var, aVar);
        }
        aVar.f6795c = cVar;
        aVar.f6793a |= 8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void e(RecyclerView.b0 b0Var, RecyclerView.l.c cVar) {
        a aVar = this.f6790a.get(b0Var);
        if (aVar == null) {
            aVar = a.b();
            this.f6790a.put(b0Var, aVar);
        }
        aVar.f6794b = cVar;
        aVar.f6793a |= 4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void f() {
        this.f6790a.clear();
        this.f6791b.c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecyclerView.b0 g(long j8) {
        return this.f6791b.f(j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean h(RecyclerView.b0 b0Var) {
        a aVar = this.f6790a.get(b0Var);
        return (aVar == null || (aVar.f6793a & 1) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean i(RecyclerView.b0 b0Var) {
        a aVar = this.f6790a.get(b0Var);
        return (aVar == null || (aVar.f6793a & 4) == 0) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void j() {
        a.a();
    }

    public void k(RecyclerView.b0 b0Var) {
        p(b0Var);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecyclerView.l.c m(RecyclerView.b0 b0Var) {
        return l(b0Var, 8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public RecyclerView.l.c n(RecyclerView.b0 b0Var) {
        return l(b0Var, 4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void o(b bVar) {
        RecyclerView.l.c cVar;
        RecyclerView.l.c cVar2;
        for (int size = this.f6790a.size() - 1; size >= 0; size--) {
            RecyclerView.b0 k8 = this.f6790a.k(size);
            a m8 = this.f6790a.m(size);
            int i8 = m8.f6793a;
            if ((i8 & 3) != 3) {
                if ((i8 & 1) != 0) {
                    cVar = m8.f6794b;
                    cVar2 = cVar != null ? m8.f6795c : null;
                } else {
                    if ((i8 & 14) != 14) {
                        if ((i8 & 12) == 12) {
                            bVar.d(k8, m8.f6794b, m8.f6795c);
                        } else if ((i8 & 4) != 0) {
                            cVar = m8.f6794b;
                        } else if ((i8 & 8) == 0) {
                        }
                        a.c(m8);
                    }
                    bVar.b(k8, m8.f6794b, m8.f6795c);
                    a.c(m8);
                }
                bVar.c(k8, cVar, cVar2);
                a.c(m8);
            }
            bVar.a(k8);
            a.c(m8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void p(RecyclerView.b0 b0Var) {
        a aVar = this.f6790a.get(b0Var);
        if (aVar == null) {
            return;
        }
        aVar.f6793a &= -2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void q(RecyclerView.b0 b0Var) {
        int q = this.f6791b.q() - 1;
        while (true) {
            if (q < 0) {
                break;
            } else if (b0Var == this.f6791b.r(q)) {
                this.f6791b.o(q);
                break;
            } else {
                q--;
            }
        }
        a remove = this.f6790a.remove(b0Var);
        if (remove != null) {
            a.c(remove);
        }
    }
}
