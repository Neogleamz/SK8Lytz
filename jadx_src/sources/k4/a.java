package k4;

import b6.p;
import b6.y;
import com.google.android.exoplayer2.ParserException;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    private static final int[] f20979a = {96000, 88200, 64000, 48000, 44100, 32000, 24000, 22050, 16000, 12000, 11025, 8000, 7350};

    /* renamed from: b  reason: collision with root package name */
    private static final int[] f20980b = {0, 1, 2, 3, 4, 5, 6, 8, -1, -1, -1, 7, 8, -1, 8, -1};

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final int f20981a;

        /* renamed from: b  reason: collision with root package name */
        public final int f20982b;

        /* renamed from: c  reason: collision with root package name */
        public final String f20983c;

        private b(int i8, int i9, String str) {
            this.f20981a = i8;
            this.f20982b = i9;
            this.f20983c = str;
        }
    }

    public static byte[] a(int i8, int i9) {
        int i10 = 0;
        int i11 = 0;
        int i12 = -1;
        while (true) {
            int[] iArr = f20979a;
            if (i11 >= iArr.length) {
                break;
            }
            if (i8 == iArr[i11]) {
                i12 = i11;
            }
            i11++;
        }
        int i13 = -1;
        while (true) {
            int[] iArr2 = f20980b;
            if (i10 >= iArr2.length) {
                break;
            }
            if (i9 == iArr2[i10]) {
                i13 = i10;
            }
            i10++;
        }
        if (i8 == -1 || i13 == -1) {
            throw new IllegalArgumentException("Invalid sample rate or number of channels: " + i8 + ", " + i9);
        }
        return b(2, i12, i13);
    }

    public static byte[] b(int i8, int i9, int i10) {
        return new byte[]{(byte) (((i8 << 3) & 248) | ((i9 >> 1) & 7)), (byte) (((i9 << 7) & RecognitionOptions.ITF) | ((i10 << 3) & 120))};
    }

    private static int c(y yVar) {
        int h8 = yVar.h(5);
        return h8 == 31 ? yVar.h(6) + 32 : h8;
    }

    private static int d(y yVar) {
        int h8 = yVar.h(4);
        if (h8 == 15) {
            if (yVar.b() >= 24) {
                return yVar.h(24);
            }
            throw ParserException.a("AAC header insufficient data", null);
        } else if (h8 < 13) {
            return f20979a[h8];
        } else {
            throw ParserException.a("AAC header wrong Sampling Frequency Index", null);
        }
    }

    public static b e(y yVar, boolean z4) {
        int c9 = c(yVar);
        int d8 = d(yVar);
        int h8 = yVar.h(4);
        String str = "mp4a.40." + c9;
        if (c9 == 5 || c9 == 29) {
            d8 = d(yVar);
            c9 = c(yVar);
            if (c9 == 22) {
                h8 = yVar.h(4);
            }
        }
        if (z4) {
            if (c9 != 1 && c9 != 2 && c9 != 3 && c9 != 4 && c9 != 6 && c9 != 7 && c9 != 17) {
                switch (c9) {
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 23:
                        break;
                    default:
                        throw ParserException.d("Unsupported audio object type: " + c9);
                }
            }
            g(yVar, c9, h8);
            switch (c9) {
                case 17:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                    int h9 = yVar.h(2);
                    if (h9 == 2 || h9 == 3) {
                        throw ParserException.d("Unsupported epConfig: " + h9);
                    }
            }
        }
        int i8 = f20980b[h8];
        if (i8 != -1) {
            return new b(d8, i8, str);
        }
        throw ParserException.a(null, null);
    }

    public static b f(byte[] bArr) {
        return e(new y(bArr), false);
    }

    private static void g(y yVar, int i8, int i9) {
        if (yVar.g()) {
            p.i("AacUtil", "Unexpected frameLengthFlag = 1");
        }
        if (yVar.g()) {
            yVar.r(14);
        }
        boolean g8 = yVar.g();
        if (i9 == 0) {
            throw new UnsupportedOperationException();
        }
        if (i8 == 6 || i8 == 20) {
            yVar.r(3);
        }
        if (g8) {
            if (i8 == 22) {
                yVar.r(16);
            }
            if (i8 == 17 || i8 == 19 || i8 == 20 || i8 == 23) {
                yVar.r(3);
            }
            yVar.r(1);
        }
    }
}
