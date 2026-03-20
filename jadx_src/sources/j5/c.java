package j5;

import com.google.android.exoplayer2.source.v;
import j5.g;
import n4.b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c implements g.b {

    /* renamed from: a  reason: collision with root package name */
    private final int[] f20721a;

    /* renamed from: b  reason: collision with root package name */
    private final v[] f20722b;

    public c(int[] iArr, v[] vVarArr) {
        this.f20721a = iArr;
        this.f20722b = vVarArr;
    }

    public int[] a() {
        int[] iArr = new int[this.f20722b.length];
        int i8 = 0;
        while (true) {
            v[] vVarArr = this.f20722b;
            if (i8 >= vVarArr.length) {
                return iArr;
            }
            iArr[i8] = vVarArr[i8].G();
            i8++;
        }
    }

    public void b(long j8) {
        for (v vVar : this.f20722b) {
            vVar.a0(j8);
        }
    }

    @Override // j5.g.b
    public b0 e(int i8, int i9) {
        int i10 = 0;
        while (true) {
            int[] iArr = this.f20721a;
            if (i10 >= iArr.length) {
                b6.p.c("BaseMediaChunkOutput", "Unmatched track of type: " + i9);
                return new n4.j();
            } else if (i9 == iArr[i10]) {
                return this.f20722b[i10];
            } else {
                i10++;
            }
        }
    }
}
