package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class s9 extends n9 {
    private s9() {
        super();
    }

    private static <E> g9<E> e(Object obj, long j8) {
        return (g9) yb.B(obj, j8);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [java.util.List] */
    @Override // com.google.android.gms.internal.measurement.n9
    public final <E> void b(Object obj, Object obj2, long j8) {
        g9<E> e8 = e(obj, j8);
        g9<E> e9 = e(obj2, j8);
        int size = e8.size();
        int size2 = e9.size();
        g9<E> g9Var = e8;
        g9Var = e8;
        if (size > 0 && size2 > 0) {
            boolean a9 = e8.a();
            g9<E> g9Var2 = e8;
            if (!a9) {
                g9Var2 = e8.c(size2 + size);
            }
            g9Var2.addAll(e9);
            g9Var = g9Var2;
        }
        if (size > 0) {
            e9 = g9Var;
        }
        yb.j(obj, j8, e9);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.measurement.n9
    public final void d(Object obj, long j8) {
        e(obj, j8).zzb();
    }
}
