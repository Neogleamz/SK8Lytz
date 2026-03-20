package c6;

import b6.u;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* renamed from: a  reason: collision with root package name */
    public final List<byte[]> f8360a;

    /* renamed from: b  reason: collision with root package name */
    public final int f8361b;

    /* renamed from: c  reason: collision with root package name */
    public final int f8362c;

    /* renamed from: d  reason: collision with root package name */
    public final int f8363d;

    /* renamed from: e  reason: collision with root package name */
    public final float f8364e;

    /* renamed from: f  reason: collision with root package name */
    public final int f8365f;

    /* renamed from: g  reason: collision with root package name */
    public final int f8366g;

    /* renamed from: h  reason: collision with root package name */
    public final int f8367h;

    /* renamed from: i  reason: collision with root package name */
    public final String f8368i;

    private f(List<byte[]> list, int i8, int i9, int i10, float f5, String str, int i11, int i12, int i13) {
        this.f8360a = list;
        this.f8361b = i8;
        this.f8362c = i9;
        this.f8363d = i10;
        this.f8364e = f5;
        this.f8368i = str;
        this.f8365f = i11;
        this.f8366g = i12;
        this.f8367h = i13;
    }

    public static f a(z zVar) {
        int i8;
        int i9;
        try {
            zVar.V(21);
            int H = zVar.H() & 3;
            int H2 = zVar.H();
            int f5 = zVar.f();
            int i10 = 0;
            int i11 = 0;
            for (int i12 = 0; i12 < H2; i12++) {
                zVar.V(1);
                int N = zVar.N();
                for (int i13 = 0; i13 < N; i13++) {
                    int N2 = zVar.N();
                    i11 += N2 + 4;
                    zVar.V(N2);
                }
            }
            zVar.U(f5);
            byte[] bArr = new byte[i11];
            float f8 = 1.0f;
            String str = null;
            int i14 = -1;
            int i15 = -1;
            int i16 = -1;
            int i17 = -1;
            int i18 = -1;
            int i19 = 0;
            int i20 = 0;
            while (i19 < H2) {
                int H3 = zVar.H() & 63;
                int N3 = zVar.N();
                int i21 = i10;
                while (i21 < N3) {
                    int N4 = zVar.N();
                    byte[] bArr2 = b6.u.f8109a;
                    int i22 = H2;
                    System.arraycopy(bArr2, i10, bArr, i20, bArr2.length);
                    int length = i20 + bArr2.length;
                    System.arraycopy(zVar.e(), zVar.f(), bArr, length, N4);
                    if (H3 == 33 && i21 == 0) {
                        u.a h8 = b6.u.h(bArr, length, length + N4);
                        int i23 = h8.f8120h;
                        i15 = h8.f8121i;
                        int i24 = h8.f8123k;
                        int i25 = h8.f8124l;
                        int i26 = h8.f8125m;
                        float f9 = h8.f8122j;
                        i8 = H3;
                        i9 = N3;
                        i14 = i23;
                        i18 = i26;
                        str = b6.e.c(h8.f8113a, h8.f8114b, h8.f8115c, h8.f8116d, h8.f8117e, h8.f8118f);
                        i17 = i25;
                        f8 = f9;
                        i16 = i24;
                    } else {
                        i8 = H3;
                        i9 = N3;
                    }
                    i20 = length + N4;
                    zVar.V(N4);
                    i21++;
                    H2 = i22;
                    H3 = i8;
                    N3 = i9;
                    i10 = 0;
                }
                i19++;
                i10 = 0;
            }
            return new f(i11 == 0 ? Collections.emptyList() : Collections.singletonList(bArr), H + 1, i14, i15, f8, str, i16, i17, i18);
        } catch (ArrayIndexOutOfBoundsException e8) {
            throw ParserException.a("Error parsing HEVC config", e8);
        }
    }
}
