package z5;

import android.os.SystemClock;
import com.google.android.exoplayer2.i2;
import com.google.android.exoplayer2.upstream.c;
import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.List;
import z5.t;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z {
    public static i2 a(t.a aVar, List<? extends u>[] listArr) {
        boolean z4;
        ImmutableList.a aVar2 = new ImmutableList.a();
        for (int i8 = 0; i8 < aVar.d(); i8++) {
            h5.w f5 = aVar.f(i8);
            List<? extends u> list = listArr[i8];
            for (int i9 = 0; i9 < f5.f20316a; i9++) {
                h5.u b9 = f5.b(i9);
                boolean z8 = aVar.a(i8, i9, false) != 0;
                int i10 = b9.f20308a;
                int[] iArr = new int[i10];
                boolean[] zArr = new boolean[i10];
                for (int i11 = 0; i11 < b9.f20308a; i11++) {
                    iArr[i11] = aVar.g(i8, i9, i11);
                    int i12 = 0;
                    while (true) {
                        if (i12 >= list.size()) {
                            z4 = false;
                            break;
                        }
                        u uVar = list.get(i12);
                        if (uVar.b().equals(b9) && uVar.u(i11) != -1) {
                            z4 = true;
                            break;
                        }
                        i12++;
                    }
                    zArr[i11] = z4;
                }
                aVar2.a(new i2.a(b9, z8, iArr, zArr));
            }
        }
        h5.w h8 = aVar.h();
        for (int i13 = 0; i13 < h8.f20316a; i13++) {
            h5.u b10 = h8.b(i13);
            int[] iArr2 = new int[b10.f20308a];
            Arrays.fill(iArr2, 0);
            aVar2.a(new i2.a(b10, false, iArr2, new boolean[b10.f20308a]));
        }
        return new i2(aVar2.k());
    }

    public static i2 b(t.a aVar, u[] uVarArr) {
        List[] listArr = new List[uVarArr.length];
        for (int i8 = 0; i8 < uVarArr.length; i8++) {
            u uVar = uVarArr[i8];
            listArr[i8] = uVar != null ? ImmutableList.F(uVar) : ImmutableList.E();
        }
        return a(aVar, listArr);
    }

    public static c.a c(r rVar) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        int length = rVar.length();
        int i8 = 0;
        for (int i9 = 0; i9 < length; i9++) {
            if (rVar.e(i9, elapsedRealtime)) {
                i8++;
            }
        }
        return new c.a(1, 0, length, i8);
    }
}
