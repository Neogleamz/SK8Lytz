package k4;

import b6.y;
import com.example.seedpoint.R;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.nio.ByteBuffer;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t {

    /* renamed from: a  reason: collision with root package name */
    private static final int[] f21011a = {1, 2, 2, 2, 2, 3, 3, 4, 4, 5, 6, 6, 6, 7, 8, 8};

    /* renamed from: b  reason: collision with root package name */
    private static final int[] f21012b = {-1, 8000, 16000, 32000, -1, -1, 11025, 22050, 44100, -1, -1, 12000, 24000, 48000, -1, -1};

    /* renamed from: c  reason: collision with root package name */
    private static final int[] f21013c = {64, R.styleable.AppCompatTheme_toolbarNavigationButtonStyle, RecognitionOptions.ITF, 192, 224, RecognitionOptions.QR_CODE, 384, 448, RecognitionOptions.UPC_A, 640, 768, 896, RecognitionOptions.UPC_E, 1152, 1280, 1536, 1920, RecognitionOptions.PDF417, 2304, 2560, 2688, 2816, 2823, 2944, 3072, 3840, RecognitionOptions.AZTEC, 6144, 7680};

    /* JADX WARN: Removed duplicated region for block: B:15:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int a(byte[] r7) {
        /*
            r0 = 0
            r1 = r7[r0]
            r2 = -2
            r3 = 6
            r4 = 7
            r5 = 1
            r6 = 4
            if (r1 == r2) goto L4a
            r2 = -1
            if (r1 == r2) goto L32
            r2 = 31
            if (r1 == r2) goto L21
            r1 = 5
            r1 = r7[r1]
            r1 = r1 & 3
            int r1 = r1 << 12
            r2 = r7[r3]
            r2 = r2 & 255(0xff, float:3.57E-43)
            int r2 = r2 << r6
            r1 = r1 | r2
            r7 = r7[r4]
            goto L58
        L21:
            r0 = r7[r3]
            r0 = r0 & 3
            int r0 = r0 << 12
            r1 = r7[r4]
            r1 = r1 & 255(0xff, float:3.57E-43)
            int r1 = r1 << r6
            r0 = r0 | r1
            r1 = 8
            r7 = r7[r1]
            goto L42
        L32:
            r0 = r7[r4]
            r0 = r0 & 3
            int r0 = r0 << 12
            r1 = r7[r3]
            r1 = r1 & 255(0xff, float:3.57E-43)
            int r1 = r1 << r6
            r0 = r0 | r1
            r1 = 9
            r7 = r7[r1]
        L42:
            r7 = r7 & 60
            int r7 = r7 >> 2
            r7 = r7 | r0
            int r7 = r7 + r5
            r0 = r5
            goto L5d
        L4a:
            r1 = r7[r6]
            r1 = r1 & 3
            int r1 = r1 << 12
            r2 = r7[r4]
            r2 = r2 & 255(0xff, float:3.57E-43)
            int r2 = r2 << r6
            r1 = r1 | r2
            r7 = r7[r3]
        L58:
            r7 = r7 & 240(0xf0, float:3.36E-43)
            int r7 = r7 >> r6
            r7 = r7 | r1
            int r7 = r7 + r5
        L5d:
            if (r0 == 0) goto L63
            int r7 = r7 * 16
            int r7 = r7 / 14
        L63:
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: k4.t.a(byte[]):int");
    }

    private static y b(byte[] bArr) {
        if (bArr[0] == Byte.MAX_VALUE) {
            return new y(bArr);
        }
        byte[] copyOf = Arrays.copyOf(bArr, bArr.length);
        if (c(copyOf)) {
            for (int i8 = 0; i8 < copyOf.length - 1; i8 += 2) {
                byte b9 = copyOf[i8];
                int i9 = i8 + 1;
                copyOf[i8] = copyOf[i9];
                copyOf[i9] = b9;
            }
        }
        y yVar = new y(copyOf);
        if (copyOf[0] == 31) {
            y yVar2 = new y(copyOf);
            while (yVar2.b() >= 16) {
                yVar2.r(2);
                yVar.f(yVar2.h(14), 14);
            }
        }
        yVar.n(copyOf);
        return yVar;
    }

    private static boolean c(byte[] bArr) {
        return bArr[0] == -2 || bArr[0] == -1;
    }

    public static boolean d(int i8) {
        return i8 == 2147385345 || i8 == -25230976 || i8 == 536864768 || i8 == -14745368;
    }

    public static int e(ByteBuffer byteBuffer) {
        int i8;
        int i9;
        int i10;
        int i11;
        int position = byteBuffer.position();
        byte b9 = byteBuffer.get(position);
        if (b9 != -2) {
            if (b9 == -1) {
                i8 = (byteBuffer.get(position + 4) & 7) << 4;
                i11 = position + 7;
            } else if (b9 != 31) {
                i8 = (byteBuffer.get(position + 4) & 1) << 6;
                i9 = position + 5;
            } else {
                i8 = (byteBuffer.get(position + 5) & 7) << 4;
                i11 = position + 6;
            }
            i10 = byteBuffer.get(i11) & 60;
            return (((i10 >> 2) | i8) + 1) * 32;
        }
        i8 = (byteBuffer.get(position + 5) & 1) << 6;
        i9 = position + 4;
        i10 = byteBuffer.get(i9) & 252;
        return (((i10 >> 2) | i8) + 1) * 32;
    }

    public static int f(byte[] bArr) {
        int i8;
        byte b9;
        int i9;
        byte b10;
        byte b11 = bArr[0];
        if (b11 != -2) {
            if (b11 == -1) {
                i8 = (bArr[4] & 7) << 4;
                b10 = bArr[7];
            } else if (b11 != 31) {
                i8 = (bArr[4] & 1) << 6;
                b9 = bArr[5];
            } else {
                i8 = (bArr[5] & 7) << 4;
                b10 = bArr[6];
            }
            i9 = b10 & 60;
            return (((i9 >> 2) | i8) + 1) * 32;
        }
        i8 = (bArr[5] & 1) << 6;
        b9 = bArr[4];
        i9 = b9 & 252;
        return (((i9 >> 2) | i8) + 1) * 32;
    }

    public static w0 g(byte[] bArr, String str, String str2, DrmInitData drmInitData) {
        y b9 = b(bArr);
        b9.r(60);
        int i8 = f21011a[b9.h(6)];
        int i9 = f21012b[b9.h(4)];
        int h8 = b9.h(5);
        int[] iArr = f21013c;
        int i10 = h8 >= iArr.length ? -1 : (iArr[h8] * 1000) / 2;
        b9.r(10);
        return new w0.b().U(str).g0("audio/vnd.dts").I(i10).J(i8 + (b9.h(2) > 0 ? 1 : 0)).h0(i9).O(drmInitData).X(str2).G();
    }
}
