package t4;

import b6.z;
import com.google.android.libraries.barhopper.RecognitionOptions;
import n4.l;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class f {

    /* renamed from: a  reason: collision with root package name */
    private final z f22918a = new z(8);

    /* renamed from: b  reason: collision with root package name */
    private int f22919b;

    private long a(l lVar) {
        int i8 = 0;
        lVar.k(this.f22918a.e(), 0, 1);
        int i9 = this.f22918a.e()[0] & 255;
        if (i9 == 0) {
            return Long.MIN_VALUE;
        }
        int i10 = RecognitionOptions.ITF;
        int i11 = 0;
        while ((i9 & i10) == 0) {
            i10 >>= 1;
            i11++;
        }
        int i12 = i9 & (~i10);
        lVar.k(this.f22918a.e(), 1, i11);
        while (i8 < i11) {
            i8++;
            i12 = (this.f22918a.e()[i8] & 255) + (i12 << 8);
        }
        this.f22919b += i11 + 1;
        return i12;
    }

    public boolean b(l lVar) {
        long a9;
        int i8;
        long b9 = lVar.b();
        int i9 = (b9 > (-1L) ? 1 : (b9 == (-1L) ? 0 : -1));
        long j8 = 1024;
        if (i9 != 0 && b9 <= 1024) {
            j8 = b9;
        }
        int i10 = (int) j8;
        lVar.k(this.f22918a.e(), 0, 4);
        long J = this.f22918a.J();
        this.f22919b = 4;
        while (J != 440786851) {
            int i11 = this.f22919b + 1;
            this.f22919b = i11;
            if (i11 == i10) {
                return false;
            }
            lVar.k(this.f22918a.e(), 0, 1);
            J = ((J << 8) & (-256)) | (this.f22918a.e()[0] & 255);
        }
        long a10 = a(lVar);
        long j9 = this.f22919b;
        if (a10 == Long.MIN_VALUE) {
            return false;
        }
        if (i9 != 0 && j9 + a10 >= b9) {
            return false;
        }
        while (true) {
            int i12 = this.f22919b;
            long j10 = j9 + a10;
            if (i12 >= j10) {
                return ((long) i12) == j10;
            } else if (a(lVar) != Long.MIN_VALUE && (a(lVar)) >= 0 && a9 <= 2147483647L) {
                if (i8 != 0) {
                    int i13 = (int) a9;
                    lVar.f(i13);
                    this.f22919b += i13;
                }
            }
        }
    }
}
