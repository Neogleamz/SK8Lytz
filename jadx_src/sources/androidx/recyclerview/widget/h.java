package androidx.recyclerview.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h {

    /* renamed from: a  reason: collision with root package name */
    private static final Comparator<g> f6881a = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements Comparator<g> {
        a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(g gVar, g gVar2) {
            int i8 = gVar.f6896a - gVar2.f6896a;
            return i8 == 0 ? gVar.f6897b - gVar2.f6897b : i8;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class b {
        public abstract boolean a(int i8, int i9);

        public abstract boolean b(int i8, int i9);

        public abstract Object c(int i8, int i9);

        public abstract int d();

        public abstract int e();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c {

        /* renamed from: a  reason: collision with root package name */
        private final List<g> f6882a;

        /* renamed from: b  reason: collision with root package name */
        private final int[] f6883b;

        /* renamed from: c  reason: collision with root package name */
        private final int[] f6884c;

        /* renamed from: d  reason: collision with root package name */
        private final b f6885d;

        /* renamed from: e  reason: collision with root package name */
        private final int f6886e;

        /* renamed from: f  reason: collision with root package name */
        private final int f6887f;

        /* renamed from: g  reason: collision with root package name */
        private final boolean f6888g;

        c(b bVar, List<g> list, int[] iArr, int[] iArr2, boolean z4) {
            this.f6882a = list;
            this.f6883b = iArr;
            this.f6884c = iArr2;
            Arrays.fill(iArr, 0);
            Arrays.fill(iArr2, 0);
            this.f6885d = bVar;
            this.f6886e = bVar.e();
            this.f6887f = bVar.d();
            this.f6888g = z4;
            a();
            g();
        }

        private void a() {
            g gVar = this.f6882a.isEmpty() ? null : this.f6882a.get(0);
            if (gVar != null && gVar.f6896a == 0 && gVar.f6897b == 0) {
                return;
            }
            g gVar2 = new g();
            gVar2.f6896a = 0;
            gVar2.f6897b = 0;
            gVar2.f6899d = false;
            gVar2.f6898c = 0;
            gVar2.f6900e = false;
            this.f6882a.add(0, gVar2);
        }

        private void b(List<e> list, s sVar, int i8, int i9, int i10) {
            if (!this.f6888g) {
                sVar.b(i8, i9);
                return;
            }
            for (int i11 = i9 - 1; i11 >= 0; i11--) {
                int[] iArr = this.f6884c;
                int i12 = i10 + i11;
                int i13 = iArr[i12] & 31;
                if (i13 == 0) {
                    sVar.b(i8, 1);
                    for (e eVar : list) {
                        eVar.f6890b++;
                    }
                } else if (i13 == 4 || i13 == 8) {
                    int i14 = iArr[i12] >> 5;
                    sVar.a(i(list, i14, true).f6890b, i8);
                    if (i13 == 4) {
                        sVar.d(i8, 1, this.f6885d.c(i14, i12));
                    }
                } else if (i13 != 16) {
                    throw new IllegalStateException("unknown flag for pos " + i12 + " " + Long.toBinaryString(i13));
                } else {
                    list.add(new e(i12, i8, false));
                }
            }
        }

        private void c(List<e> list, s sVar, int i8, int i9, int i10) {
            if (!this.f6888g) {
                sVar.c(i8, i9);
                return;
            }
            for (int i11 = i9 - 1; i11 >= 0; i11--) {
                int[] iArr = this.f6883b;
                int i12 = i10 + i11;
                int i13 = iArr[i12] & 31;
                if (i13 == 0) {
                    sVar.c(i8 + i11, 1);
                    for (e eVar : list) {
                        eVar.f6890b--;
                    }
                } else if (i13 == 4 || i13 == 8) {
                    int i14 = iArr[i12] >> 5;
                    e i15 = i(list, i14, false);
                    sVar.a(i8 + i11, i15.f6890b - 1);
                    if (i13 == 4) {
                        sVar.d(i15.f6890b - 1, 1, this.f6885d.c(i12, i14));
                    }
                } else if (i13 != 16) {
                    throw new IllegalStateException("unknown flag for pos " + i12 + " " + Long.toBinaryString(i13));
                } else {
                    list.add(new e(i12, i8 + i11, true));
                }
            }
        }

        private void e(int i8, int i9, int i10) {
            if (this.f6883b[i8 - 1] != 0) {
                return;
            }
            f(i8, i9, i10, false);
        }

        private boolean f(int i8, int i9, int i10, boolean z4) {
            int i11;
            int i12;
            int i13;
            if (z4) {
                i9--;
                i12 = i8;
                i11 = i9;
            } else {
                i11 = i8 - 1;
                i12 = i11;
            }
            while (i10 >= 0) {
                g gVar = this.f6882a.get(i10);
                int i14 = gVar.f6896a;
                int i15 = gVar.f6898c;
                int i16 = i14 + i15;
                int i17 = gVar.f6897b + i15;
                if (z4) {
                    for (int i18 = i12 - 1; i18 >= i16; i18--) {
                        if (this.f6885d.b(i18, i11)) {
                            i13 = this.f6885d.a(i18, i11) ? 8 : 4;
                            this.f6884c[i11] = (i18 << 5) | 16;
                            this.f6883b[i18] = (i11 << 5) | i13;
                            return true;
                        }
                    }
                    continue;
                } else {
                    for (int i19 = i9 - 1; i19 >= i17; i19--) {
                        if (this.f6885d.b(i11, i19)) {
                            i13 = this.f6885d.a(i11, i19) ? 8 : 4;
                            int i20 = i8 - 1;
                            this.f6883b[i20] = (i19 << 5) | 16;
                            this.f6884c[i19] = (i20 << 5) | i13;
                            return true;
                        }
                    }
                    continue;
                }
                i12 = gVar.f6896a;
                i9 = gVar.f6897b;
                i10--;
            }
            return false;
        }

        private void g() {
            int i8 = this.f6886e;
            int i9 = this.f6887f;
            for (int size = this.f6882a.size() - 1; size >= 0; size--) {
                g gVar = this.f6882a.get(size);
                int i10 = gVar.f6896a;
                int i11 = gVar.f6898c;
                int i12 = i10 + i11;
                int i13 = gVar.f6897b + i11;
                if (this.f6888g) {
                    while (i8 > i12) {
                        e(i8, i9, size);
                        i8--;
                    }
                    while (i9 > i13) {
                        h(i8, i9, size);
                        i9--;
                    }
                }
                for (int i14 = 0; i14 < gVar.f6898c; i14++) {
                    int i15 = gVar.f6896a + i14;
                    int i16 = gVar.f6897b + i14;
                    int i17 = this.f6885d.a(i15, i16) ? 1 : 2;
                    this.f6883b[i15] = (i16 << 5) | i17;
                    this.f6884c[i16] = (i15 << 5) | i17;
                }
                i8 = gVar.f6896a;
                i9 = gVar.f6897b;
            }
        }

        private void h(int i8, int i9, int i10) {
            if (this.f6884c[i9 - 1] != 0) {
                return;
            }
            f(i8, i9, i10, true);
        }

        private static e i(List<e> list, int i8, boolean z4) {
            int size = list.size() - 1;
            while (size >= 0) {
                e eVar = list.get(size);
                if (eVar.f6889a == i8 && eVar.f6891c == z4) {
                    list.remove(size);
                    while (size < list.size()) {
                        list.get(size).f6890b += z4 ? 1 : -1;
                        size++;
                    }
                    return eVar;
                }
                size--;
            }
            return null;
        }

        public void d(s sVar) {
            androidx.recyclerview.widget.e eVar = sVar instanceof androidx.recyclerview.widget.e ? (androidx.recyclerview.widget.e) sVar : new androidx.recyclerview.widget.e(sVar);
            ArrayList arrayList = new ArrayList();
            int i8 = this.f6886e;
            int i9 = this.f6887f;
            for (int size = this.f6882a.size() - 1; size >= 0; size--) {
                g gVar = this.f6882a.get(size);
                int i10 = gVar.f6898c;
                int i11 = gVar.f6896a + i10;
                int i12 = gVar.f6897b + i10;
                if (i11 < i8) {
                    c(arrayList, eVar, i11, i8 - i11, i11);
                }
                if (i12 < i9) {
                    b(arrayList, eVar, i11, i9 - i12, i12);
                }
                for (int i13 = i10 - 1; i13 >= 0; i13--) {
                    int[] iArr = this.f6883b;
                    int i14 = gVar.f6896a;
                    if ((iArr[i14 + i13] & 31) == 2) {
                        eVar.d(i14 + i13, 1, this.f6885d.c(i14 + i13, gVar.f6897b + i13));
                    }
                }
                i8 = gVar.f6896a;
                i9 = gVar.f6897b;
            }
            eVar.e();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class d<T> {
        public abstract boolean a(T t8, T t9);

        public abstract boolean b(T t8, T t9);

        public Object c(T t8, T t9) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e {

        /* renamed from: a  reason: collision with root package name */
        int f6889a;

        /* renamed from: b  reason: collision with root package name */
        int f6890b;

        /* renamed from: c  reason: collision with root package name */
        boolean f6891c;

        public e(int i8, int i9, boolean z4) {
            this.f6889a = i8;
            this.f6890b = i9;
            this.f6891c = z4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f {

        /* renamed from: a  reason: collision with root package name */
        int f6892a;

        /* renamed from: b  reason: collision with root package name */
        int f6893b;

        /* renamed from: c  reason: collision with root package name */
        int f6894c;

        /* renamed from: d  reason: collision with root package name */
        int f6895d;

        public f() {
        }

        public f(int i8, int i9, int i10, int i11) {
            this.f6892a = i8;
            this.f6893b = i9;
            this.f6894c = i10;
            this.f6895d = i11;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class g {

        /* renamed from: a  reason: collision with root package name */
        int f6896a;

        /* renamed from: b  reason: collision with root package name */
        int f6897b;

        /* renamed from: c  reason: collision with root package name */
        int f6898c;

        /* renamed from: d  reason: collision with root package name */
        boolean f6899d;

        /* renamed from: e  reason: collision with root package name */
        boolean f6900e;

        g() {
        }
    }

    public static c a(b bVar) {
        return b(bVar, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x00c7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static androidx.recyclerview.widget.h.c b(androidx.recyclerview.widget.h.b r15, boolean r16) {
        /*
            Method dump skipped, instructions count: 238
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.h.b(androidx.recyclerview.widget.h$b, boolean):androidx.recyclerview.widget.h$c");
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0042, code lost:
        if (r24[r13 - 1] < r24[r13 + r5]) goto L42;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00b8, code lost:
        if (r25[r12 - 1] < r25[r12 + 1]) goto L77;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00e1 A[LOOP:4: B:52:0x00cd->B:56:0x00e1, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:89:0x00ec A[EDGE_INSN: B:89:0x00ec->B:58:0x00ec ?: BREAK  , SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static androidx.recyclerview.widget.h.g c(androidx.recyclerview.widget.h.b r19, int r20, int r21, int r22, int r23, int[] r24, int[] r25, int r26) {
        /*
            Method dump skipped, instructions count: 305
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.h.c(androidx.recyclerview.widget.h$b, int, int, int, int, int[], int[], int):androidx.recyclerview.widget.h$g");
    }
}
