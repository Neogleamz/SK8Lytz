package androidx.recyclerview.widget;

import androidx.recyclerview.widget.a;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class t {

    /* renamed from: a  reason: collision with root package name */
    final a f7026a;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void a(a.b bVar);

        a.b b(int i8, int i9, int i10, Object obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public t(a aVar) {
        this.f7026a = aVar;
    }

    private int a(List<a.b> list) {
        boolean z4 = false;
        for (int size = list.size() - 1; size >= 0; size--) {
            if (list.get(size).f6778a != 8) {
                z4 = true;
            } else if (z4) {
                return size;
            }
        }
        return -1;
    }

    private void c(List<a.b> list, int i8, a.b bVar, int i9, a.b bVar2) {
        int i10 = bVar.f6781d;
        int i11 = bVar2.f6779b;
        int i12 = i10 < i11 ? -1 : 0;
        int i13 = bVar.f6779b;
        if (i13 < i11) {
            i12++;
        }
        if (i11 <= i13) {
            bVar.f6779b = i13 + bVar2.f6781d;
        }
        int i14 = bVar2.f6779b;
        if (i14 <= i10) {
            bVar.f6781d = i10 + bVar2.f6781d;
        }
        bVar2.f6779b = i14 + i12;
        list.set(i8, bVar2);
        list.set(i9, bVar);
    }

    private void d(List<a.b> list, int i8, int i9) {
        a.b bVar = list.get(i8);
        a.b bVar2 = list.get(i9);
        int i10 = bVar2.f6778a;
        if (i10 == 1) {
            c(list, i8, bVar, i9, bVar2);
        } else if (i10 == 2) {
            e(list, i8, bVar, i9, bVar2);
        } else if (i10 != 4) {
        } else {
            f(list, i8, bVar, i9, bVar2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void b(List<a.b> list) {
        while (true) {
            int a9 = a(list);
            if (a9 == -1) {
                return;
            }
            d(list, a9, a9 + 1);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x009e, code lost:
        if (r0 > r14.f6779b) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00c8, code lost:
        if (r0 >= r14.f6779b) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00ca, code lost:
        r12.f6781d = r0 - r14.f6781d;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void e(java.util.List<androidx.recyclerview.widget.a.b> r10, int r11, androidx.recyclerview.widget.a.b r12, int r13, androidx.recyclerview.widget.a.b r14) {
        /*
            Method dump skipped, instructions count: 229
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.t.e(java.util.List, int, androidx.recyclerview.widget.a$b, int, androidx.recyclerview.widget.a$b):void");
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0027  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x002b  */
    /* JADX WARN: Removed duplicated region for block: B:17:0x0048  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x004c  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0056  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x005b  */
    /* JADX WARN: Removed duplicated region for block: B:24:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void f(java.util.List<androidx.recyclerview.widget.a.b> r9, int r10, androidx.recyclerview.widget.a.b r11, int r12, androidx.recyclerview.widget.a.b r13) {
        /*
            r8 = this;
            int r0 = r11.f6781d
            int r1 = r13.f6779b
            r2 = 4
            r3 = 0
            r4 = 1
            if (r0 >= r1) goto Ld
            int r1 = r1 - r4
            r13.f6779b = r1
            goto L20
        Ld:
            int r5 = r13.f6781d
            int r1 = r1 + r5
            if (r0 >= r1) goto L20
            int r5 = r5 - r4
            r13.f6781d = r5
            androidx.recyclerview.widget.t$a r0 = r8.f7026a
            int r1 = r11.f6779b
            java.lang.Object r5 = r13.f6780c
            androidx.recyclerview.widget.a$b r0 = r0.b(r2, r1, r4, r5)
            goto L21
        L20:
            r0 = r3
        L21:
            int r1 = r11.f6779b
            int r5 = r13.f6779b
            if (r1 > r5) goto L2b
            int r5 = r5 + r4
            r13.f6779b = r5
            goto L41
        L2b:
            int r6 = r13.f6781d
            int r7 = r5 + r6
            if (r1 >= r7) goto L41
            int r5 = r5 + r6
            int r5 = r5 - r1
            androidx.recyclerview.widget.t$a r3 = r8.f7026a
            int r1 = r1 + r4
            java.lang.Object r4 = r13.f6780c
            androidx.recyclerview.widget.a$b r3 = r3.b(r2, r1, r5, r4)
            int r1 = r13.f6781d
            int r1 = r1 - r5
            r13.f6781d = r1
        L41:
            r9.set(r12, r11)
            int r11 = r13.f6781d
            if (r11 <= 0) goto L4c
            r9.set(r10, r13)
            goto L54
        L4c:
            r9.remove(r10)
            androidx.recyclerview.widget.t$a r11 = r8.f7026a
            r11.a(r13)
        L54:
            if (r0 == 0) goto L59
            r9.add(r10, r0)
        L59:
            if (r3 == 0) goto L5e
            r9.add(r10, r3)
        L5e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.t.f(java.util.List, int, androidx.recyclerview.widget.a$b, int, androidx.recyclerview.widget.a$b):void");
    }
}
