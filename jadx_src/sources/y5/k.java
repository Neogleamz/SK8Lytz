package y5;

import b6.l0;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class k implements p5.h {

    /* renamed from: a  reason: collision with root package name */
    private final List<e> f24474a;

    /* renamed from: b  reason: collision with root package name */
    private final long[] f24475b;

    /* renamed from: c  reason: collision with root package name */
    private final long[] f24476c;

    public k(List<e> list) {
        this.f24474a = Collections.unmodifiableList(new ArrayList(list));
        this.f24475b = new long[list.size() * 2];
        for (int i8 = 0; i8 < list.size(); i8++) {
            e eVar = list.get(i8);
            int i9 = i8 * 2;
            long[] jArr = this.f24475b;
            jArr[i9] = eVar.f24445b;
            jArr[i9 + 1] = eVar.f24446c;
        }
        long[] jArr2 = this.f24475b;
        long[] copyOf = Arrays.copyOf(jArr2, jArr2.length);
        this.f24476c = copyOf;
        Arrays.sort(copyOf);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static /* synthetic */ int b(e eVar, e eVar2) {
        return Long.compare(eVar.f24445b, eVar2.f24445b);
    }

    @Override // p5.h
    public int c(long j8) {
        int e8 = l0.e(this.f24476c, j8, false, false);
        if (e8 < this.f24476c.length) {
            return e8;
        }
        return -1;
    }

    @Override // p5.h
    public long f(int i8) {
        b6.a.a(i8 >= 0);
        b6.a.a(i8 < this.f24476c.length);
        return this.f24476c[i8];
    }

    @Override // p5.h
    public List<p5.b> h(long j8) {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (int i8 = 0; i8 < this.f24474a.size(); i8++) {
            long[] jArr = this.f24475b;
            int i9 = i8 * 2;
            if (jArr[i9] <= j8 && j8 < jArr[i9 + 1]) {
                e eVar = this.f24474a.get(i8);
                p5.b bVar = eVar.f24444a;
                if (bVar.f22378e == -3.4028235E38f) {
                    arrayList2.add(eVar);
                } else {
                    arrayList.add(bVar);
                }
            }
        }
        Collections.sort(arrayList2, j.a);
        for (int i10 = 0; i10 < arrayList2.size(); i10++) {
            arrayList.add(((e) arrayList2.get(i10)).f24444a.b().h((-1) - i10, 1).a());
        }
        return arrayList;
    }

    @Override // p5.h
    public int i() {
        return this.f24476c.length;
    }
}
