package v4;

import b6.z;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class n {

    /* renamed from: a  reason: collision with root package name */
    private static final int[] f23298a = {1769172845, 1769172786, 1769172787, 1769172788, 1769172789, 1769172790, 1769172793, 1635148593, 1752589105, 1751479857, 1635135537, 1836069937, 1836069938, 862401121, 862401122, 862417462, 862417718, 862414134, 862414646, 1295275552, 1295270176, 1714714144, 1801741417, 1295275600, 1903435808, 1297305174, 1684175153, 1769172332, 1885955686};

    private static boolean a(int i8, boolean z4) {
        if ((i8 >>> 8) == 3368816) {
            return true;
        }
        if (i8 == 1751476579 && z4) {
            return true;
        }
        for (int i9 : f23298a) {
            if (i9 == i8) {
                return true;
            }
        }
        return false;
    }

    public static boolean b(n4.l lVar) {
        return c(lVar, true, false);
    }

    private static boolean c(n4.l lVar, boolean z4, boolean z8) {
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12;
        boolean z13;
        long b9 = lVar.b();
        long j8 = -1;
        int i8 = (b9 > (-1L) ? 1 : (b9 == (-1L) ? 0 : -1));
        long j9 = 4096;
        if (i8 != 0 && b9 <= 4096) {
            j9 = b9;
        }
        int i9 = (int) j9;
        z zVar = new z(64);
        boolean z14 = false;
        int i10 = 0;
        boolean z15 = false;
        while (i10 < i9) {
            zVar.Q(8);
            if (!lVar.d(zVar.e(), z14 ? 1 : 0, 8, true)) {
                break;
            }
            long J = zVar.J();
            int q = zVar.q();
            int i11 = 16;
            if (J == 1) {
                lVar.k(zVar.e(), 8, 8);
                zVar.T(16);
                J = zVar.A();
            } else {
                if (J == 0) {
                    long b10 = lVar.b();
                    if (b10 != j8) {
                        J = (b10 - lVar.e()) + 8;
                    }
                }
                i11 = 8;
            }
            long j10 = i11;
            if (J < j10) {
                return z14;
            }
            i10 += i11;
            if (q == 1836019574) {
                i9 += (int) J;
                if (i8 != 0 && i9 > b9) {
                    i9 = (int) b9;
                }
            } else if (q == 1836019558 || q == 1836475768) {
                z9 = z14 ? 1 : 0;
                z10 = true;
                z11 = true;
                break;
            } else {
                long j11 = b9;
                if ((i10 + J) - j10 >= i9) {
                    z9 = false;
                    z10 = true;
                    break;
                }
                int i12 = (int) (J - j10);
                i10 += i12;
                if (q != 1718909296) {
                    z12 = false;
                    z15 = z15;
                    if (i12 != 0) {
                        lVar.f(i12);
                        z15 = z15;
                    }
                } else if (i12 < 8) {
                    return false;
                } else {
                    zVar.Q(i12);
                    lVar.k(zVar.e(), 0, i12);
                    int i13 = i12 / 4;
                    int i14 = 0;
                    while (true) {
                        if (i14 >= i13) {
                            z13 = z15;
                            break;
                        }
                        if (i14 == 1) {
                            zVar.V(4);
                        } else if (a(zVar.q(), z8)) {
                            z13 = true;
                            break;
                        }
                        i14++;
                    }
                    z12 = false;
                    z15 = z13;
                    if (!z13) {
                        return false;
                    }
                }
                z14 = z12;
                b9 = j11;
            }
            j8 = -1;
            z15 = z15;
        }
        z9 = z14 ? 1 : 0;
        z10 = true;
        z11 = z9;
        return (z15 && z4 == z11) ? z10 : z9;
    }

    public static boolean d(n4.l lVar, boolean z4) {
        return c(lVar, false, z4);
    }
}
