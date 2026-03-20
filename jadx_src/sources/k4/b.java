package k4;

import b6.l0;
import b6.y;
import b6.z;
import com.example.seedpoint.R;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private static final int[] f20984a = {1, 2, 3, 6};

    /* renamed from: b  reason: collision with root package name */
    private static final int[] f20985b = {48000, 44100, 32000};

    /* renamed from: c  reason: collision with root package name */
    private static final int[] f20986c = {24000, 22050, 16000};

    /* renamed from: d  reason: collision with root package name */
    private static final int[] f20987d = {2, 1, 2, 3, 3, 4, 4, 5};

    /* renamed from: e  reason: collision with root package name */
    private static final int[] f20988e = {32, 40, 48, 56, 64, 80, 96, R.styleable.AppCompatTheme_toolbarNavigationButtonStyle, RecognitionOptions.ITF, 160, 192, 224, RecognitionOptions.QR_CODE, 320, 384, 448, RecognitionOptions.UPC_A, 576, 640};

    /* renamed from: f  reason: collision with root package name */
    private static final int[] f20989f = {69, 87, 104, 121, 139, 174, 208, 243, 278, 348, 417, 487, 557, 696, 835, 975, 1114, 1253, 1393};

    /* renamed from: k4.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0184b {

        /* renamed from: a  reason: collision with root package name */
        public final String f20990a;

        /* renamed from: b  reason: collision with root package name */
        public final int f20991b;

        /* renamed from: c  reason: collision with root package name */
        public final int f20992c;

        /* renamed from: d  reason: collision with root package name */
        public final int f20993d;

        /* renamed from: e  reason: collision with root package name */
        public final int f20994e;

        /* renamed from: f  reason: collision with root package name */
        public final int f20995f;

        /* renamed from: g  reason: collision with root package name */
        public final int f20996g;

        private C0184b(String str, int i8, int i9, int i10, int i11, int i12, int i13) {
            this.f20990a = str;
            this.f20991b = i8;
            this.f20993d = i9;
            this.f20992c = i10;
            this.f20994e = i11;
            this.f20995f = i12;
            this.f20996g = i13;
        }
    }

    private static int a(int i8, int i9, int i10) {
        return (i8 * i9) / (i10 * 32);
    }

    public static int b(ByteBuffer byteBuffer) {
        int position = byteBuffer.position();
        int limit = byteBuffer.limit() - 10;
        for (int i8 = position; i8 <= limit; i8++) {
            if ((l0.I(byteBuffer, i8 + 4) & (-2)) == -126718022) {
                return i8 - position;
            }
        }
        return -1;
    }

    private static int c(int i8, int i9) {
        int i10 = i9 / 2;
        if (i8 >= 0) {
            int[] iArr = f20985b;
            if (i8 >= iArr.length || i9 < 0) {
                return -1;
            }
            int[] iArr2 = f20989f;
            if (i10 >= iArr2.length) {
                return -1;
            }
            int i11 = iArr[i8];
            if (i11 == 44100) {
                return (iArr2[i10] + (i9 % 2)) * 2;
            }
            int i12 = f20988e[i10];
            return i11 == 32000 ? i12 * 6 : i12 * 4;
        }
        return -1;
    }

    public static w0 d(z zVar, String str, String str2, DrmInitData drmInitData) {
        y yVar = new y();
        yVar.m(zVar);
        int i8 = f20985b[yVar.h(2)];
        yVar.r(8);
        int i9 = f20987d[yVar.h(3)];
        if (yVar.h(1) != 0) {
            i9++;
        }
        int i10 = f20988e[yVar.h(5)] * 1000;
        yVar.c();
        zVar.U(yVar.d());
        return new w0.b().U(str).g0("audio/ac3").J(i9).h0(i8).O(drmInitData).X(str2).I(i10).b0(i10).G();
    }

    public static int e(ByteBuffer byteBuffer) {
        if (((byteBuffer.get(byteBuffer.position() + 5) & 248) >> 3) > 10) {
            return f20984a[((byteBuffer.get(byteBuffer.position() + 4) & 192) >> 6) != 3 ? (byteBuffer.get(byteBuffer.position() + 4) & 48) >> 4 : 3] * RecognitionOptions.QR_CODE;
        }
        return 1536;
    }

    public static C0184b f(y yVar) {
        int i8;
        int i9;
        int i10;
        String str;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        int i18;
        int i19;
        int e8 = yVar.e();
        yVar.r(40);
        boolean z4 = yVar.h(5) > 10;
        yVar.p(e8);
        int i20 = -1;
        if (z4) {
            yVar.r(16);
            int h8 = yVar.h(2);
            if (h8 == 0) {
                i20 = 0;
            } else if (h8 == 1) {
                i20 = 1;
            } else if (h8 == 2) {
                i20 = 2;
            }
            yVar.r(3);
            int h9 = (yVar.h(11) + 1) * 2;
            int h10 = yVar.h(2);
            if (h10 == 3) {
                i15 = f20986c[yVar.h(2)];
                i14 = 3;
                i16 = 6;
            } else {
                int h11 = yVar.h(2);
                int i21 = f20984a[h11];
                i14 = h11;
                i15 = f20985b[h10];
                i16 = i21;
            }
            int i22 = i16 * RecognitionOptions.QR_CODE;
            int a9 = a(h9, i15, i16);
            int h12 = yVar.h(3);
            boolean g8 = yVar.g();
            i8 = f20987d[h12] + (g8 ? 1 : 0);
            yVar.r(10);
            if (yVar.g()) {
                yVar.r(8);
            }
            if (h12 == 0) {
                yVar.r(5);
                if (yVar.g()) {
                    yVar.r(8);
                }
            }
            if (i20 == 1 && yVar.g()) {
                yVar.r(16);
            }
            if (yVar.g()) {
                if (h12 > 2) {
                    yVar.r(2);
                }
                if ((h12 & 1) == 0 || h12 <= 2) {
                    i18 = 6;
                } else {
                    i18 = 6;
                    yVar.r(6);
                }
                if ((h12 & 4) != 0) {
                    yVar.r(i18);
                }
                if (g8 && yVar.g()) {
                    yVar.r(5);
                }
                if (i20 == 0) {
                    if (yVar.g()) {
                        i19 = 6;
                        yVar.r(6);
                    } else {
                        i19 = 6;
                    }
                    if (h12 == 0 && yVar.g()) {
                        yVar.r(i19);
                    }
                    if (yVar.g()) {
                        yVar.r(i19);
                    }
                    int h13 = yVar.h(2);
                    if (h13 == 1) {
                        yVar.r(5);
                    } else if (h13 == 2) {
                        yVar.r(12);
                    } else if (h13 == 3) {
                        int h14 = yVar.h(5);
                        if (yVar.g()) {
                            yVar.r(5);
                            if (yVar.g()) {
                                yVar.r(4);
                            }
                            if (yVar.g()) {
                                yVar.r(4);
                            }
                            if (yVar.g()) {
                                yVar.r(4);
                            }
                            if (yVar.g()) {
                                yVar.r(4);
                            }
                            if (yVar.g()) {
                                yVar.r(4);
                            }
                            if (yVar.g()) {
                                yVar.r(4);
                            }
                            if (yVar.g()) {
                                yVar.r(4);
                            }
                            if (yVar.g()) {
                                if (yVar.g()) {
                                    yVar.r(4);
                                }
                                if (yVar.g()) {
                                    yVar.r(4);
                                }
                            }
                        }
                        if (yVar.g()) {
                            yVar.r(5);
                            if (yVar.g()) {
                                yVar.r(7);
                                if (yVar.g()) {
                                    yVar.r(8);
                                }
                            }
                        }
                        yVar.r((h14 + 2) * 8);
                        yVar.c();
                    }
                    if (h12 < 2) {
                        if (yVar.g()) {
                            yVar.r(14);
                        }
                        if (h12 == 0 && yVar.g()) {
                            yVar.r(14);
                        }
                    }
                    if (yVar.g()) {
                        if (i14 == 0) {
                            yVar.r(5);
                        } else {
                            for (int i23 = 0; i23 < i16; i23++) {
                                if (yVar.g()) {
                                    yVar.r(5);
                                }
                            }
                        }
                    }
                }
            }
            if (yVar.g()) {
                yVar.r(5);
                if (h12 == 2) {
                    yVar.r(4);
                }
                if (h12 >= 6) {
                    yVar.r(2);
                }
                if (yVar.g()) {
                    yVar.r(8);
                }
                if (h12 == 0 && yVar.g()) {
                    yVar.r(8);
                }
                if (h10 < 3) {
                    yVar.q();
                }
            }
            if (i20 == 0 && i14 != 3) {
                yVar.q();
            }
            if (i20 == 2 && (i14 == 3 || yVar.g())) {
                i17 = 6;
                yVar.r(6);
            } else {
                i17 = 6;
            }
            str = (yVar.g() && yVar.h(i17) == 1 && yVar.h(8) == 1) ? "audio/eac3-joc" : "audio/eac3";
            i9 = i20;
            i10 = i22;
            i12 = h9;
            i13 = i15;
            i11 = a9;
        } else {
            yVar.r(32);
            int h15 = yVar.h(2);
            String str2 = h15 == 3 ? null : "audio/ac3";
            int h16 = yVar.h(6);
            int i24 = f20988e[h16 / 2] * 1000;
            int c9 = c(h15, h16);
            yVar.r(8);
            int h17 = yVar.h(3);
            if ((h17 & 1) != 0 && h17 != 1) {
                yVar.r(2);
            }
            if ((h17 & 4) != 0) {
                yVar.r(2);
            }
            if (h17 == 2) {
                yVar.r(2);
            }
            int[] iArr = f20985b;
            int i25 = h15 < iArr.length ? iArr[h15] : -1;
            i8 = f20987d[h17] + (yVar.g() ? 1 : 0);
            i9 = -1;
            i10 = 1536;
            str = str2;
            i11 = i24;
            i12 = c9;
            i13 = i25;
        }
        return new C0184b(str, i9, i8, i13, i12, i10, i11);
    }

    public static int g(byte[] bArr) {
        if (bArr.length < 6) {
            return -1;
        }
        if (((bArr[5] & 248) >> 3) > 10) {
            return (((bArr[3] & 255) | ((bArr[2] & 7) << 8)) + 1) * 2;
        }
        return c((bArr[4] & 192) >> 6, bArr[4] & 63);
    }

    public static w0 h(z zVar, String str, String str2, DrmInitData drmInitData) {
        String str3;
        y yVar = new y();
        yVar.m(zVar);
        int h8 = yVar.h(13) * 1000;
        yVar.r(3);
        int i8 = f20985b[yVar.h(2)];
        yVar.r(10);
        int i9 = f20987d[yVar.h(3)];
        if (yVar.h(1) != 0) {
            i9++;
        }
        yVar.r(3);
        int h9 = yVar.h(4);
        yVar.r(1);
        if (h9 > 0) {
            yVar.s(6);
            if (yVar.h(1) != 0) {
                i9 += 2;
            }
            yVar.r(1);
        }
        if (yVar.b() > 7) {
            yVar.r(7);
            if (yVar.h(1) != 0) {
                str3 = "audio/eac3-joc";
                yVar.c();
                zVar.U(yVar.d());
                return new w0.b().U(str).g0(str3).J(i9).h0(i8).O(drmInitData).X(str2).b0(h8).G();
            }
        }
        str3 = "audio/eac3";
        yVar.c();
        zVar.U(yVar.d());
        return new w0.b().U(str).g0(str3).J(i9).h0(i8).O(drmInitData).X(str2).b0(h8).G();
    }

    public static int i(ByteBuffer byteBuffer, int i8) {
        return 40 << ((byteBuffer.get((byteBuffer.position() + i8) + ((byteBuffer.get((byteBuffer.position() + i8) + 7) & 255) == 187 ? 9 : 8)) >> 4) & 7);
    }

    public static int j(byte[] bArr) {
        if (bArr[4] == -8 && bArr[5] == 114 && bArr[6] == 111 && (bArr[7] & 254) == 186) {
            return 40 << ((bArr[(bArr[7] & 255) == 187 ? '\t' : '\b'] >> 4) & 7);
        }
        return 0;
    }
}
