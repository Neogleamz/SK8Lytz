package n4;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {
    public static void a(long j8, b6.z zVar, b0[] b0VarArr) {
        while (true) {
            if (zVar.a() <= 1) {
                return;
            }
            int c9 = c(zVar);
            int c10 = c(zVar);
            int f5 = zVar.f() + c10;
            if (c10 == -1 || c10 > zVar.a()) {
                b6.p.i("CeaUtil", "Skipping remainder of malformed SEI NAL unit.");
                f5 = zVar.g();
            } else if (c9 == 4 && c10 >= 8) {
                int H = zVar.H();
                int N = zVar.N();
                int q = N == 49 ? zVar.q() : 0;
                int H2 = zVar.H();
                if (N == 47) {
                    zVar.V(1);
                }
                boolean z4 = H == 181 && (N == 49 || N == 47) && H2 == 3;
                if (N == 49) {
                    z4 &= q == 1195456820;
                }
                if (z4) {
                    b(j8, zVar, b0VarArr);
                }
            }
            zVar.U(f5);
        }
    }

    public static void b(long j8, b6.z zVar, b0[] b0VarArr) {
        int H = zVar.H();
        if ((H & 64) != 0) {
            zVar.V(1);
            int i8 = (H & 31) * 3;
            int f5 = zVar.f();
            for (b0 b0Var : b0VarArr) {
                zVar.U(f5);
                b0Var.b(zVar, i8);
                if (j8 != -9223372036854775807L) {
                    b0Var.d(j8, 1, i8, 0, null);
                }
            }
        }
    }

    private static int c(b6.z zVar) {
        int i8 = 0;
        while (zVar.a() != 0) {
            int H = zVar.H();
            i8 += H;
            if (H != 255) {
                return i8;
            }
        }
        return -1;
    }
}
