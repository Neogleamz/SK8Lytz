package com.google.android.gms.internal.measurement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class q9 extends n9 {

    /* renamed from: c  reason: collision with root package name */
    private static final Class<?> f12457c = Collections.unmodifiableList(Collections.emptyList()).getClass();

    private q9() {
        super();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <L> List<L> e(Object obj, long j8, int i8) {
        List<L> c9;
        l9 l9Var;
        List<L> f5 = f(obj, j8);
        if (!f5.isEmpty()) {
            if (f12457c.isAssignableFrom(f5.getClass())) {
                ArrayList arrayList = new ArrayList(f5.size() + i8);
                arrayList.addAll(f5);
                l9Var = arrayList;
            } else if (f5 instanceof xb) {
                l9 l9Var2 = new l9(f5.size() + i8);
                l9Var2.addAll((xb) f5);
                l9Var = l9Var2;
            } else if (!(f5 instanceof sa) || !(f5 instanceof g9)) {
                return f5;
            } else {
                g9 g9Var = (g9) f5;
                if (g9Var.a()) {
                    return f5;
                }
                c9 = g9Var.c(f5.size() + i8);
            }
            yb.j(obj, j8, l9Var);
            return l9Var;
        }
        c9 = f5 instanceof o9 ? new l9(i8) : ((f5 instanceof sa) && (f5 instanceof g9)) ? ((g9) f5).c(i8) : new ArrayList<>(i8);
        yb.j(obj, j8, c9);
        return c9;
    }

    private static <E> List<E> f(Object obj, long j8) {
        return (List) yb.B(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.n9
    public final <E> void b(Object obj, Object obj2, long j8) {
        List f5 = f(obj2, j8);
        List e8 = e(obj, j8, f5.size());
        int size = e8.size();
        int size2 = f5.size();
        if (size > 0 && size2 > 0) {
            e8.addAll(f5);
        }
        if (size > 0) {
            f5 = e8;
        }
        yb.j(obj, j8, f5);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.n9
    public final void d(Object obj, long j8) {
        Object unmodifiableList;
        List list = (List) yb.B(obj, j8);
        if (list instanceof o9) {
            unmodifiableList = ((o9) list).b();
        } else if (f12457c.isAssignableFrom(list.getClass())) {
            return;
        } else {
            if ((list instanceof sa) && (list instanceof g9)) {
                g9 g9Var = (g9) list;
                if (g9Var.a()) {
                    g9Var.zzb();
                    return;
                }
                return;
            }
            unmodifiableList = Collections.unmodifiableList(list);
        }
        yb.j(obj, j8, unmodifiableList);
    }
}
