package k4;

import b6.y;
import b6.z;
import com.google.android.exoplayer2.drm.DrmInitData;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.nio.ByteBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: a  reason: collision with root package name */
    private static final int[] f20997a = {2002, 2000, 1920, 1601, 1600, 1001, 1000, 960, 800, 800, 480, 400, 400, RecognitionOptions.PDF417};

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        public final int f20998a;

        /* renamed from: b  reason: collision with root package name */
        public final int f20999b;

        /* renamed from: c  reason: collision with root package name */
        public final int f21000c;

        /* renamed from: d  reason: collision with root package name */
        public final int f21001d;

        /* renamed from: e  reason: collision with root package name */
        public final int f21002e;

        private b(int i8, int i9, int i10, int i11, int i12) {
            this.f20998a = i8;
            this.f21000c = i9;
            this.f20999b = i10;
            this.f21001d = i11;
            this.f21002e = i12;
        }
    }

    public static void a(int i8, z zVar) {
        zVar.Q(7);
        byte[] e8 = zVar.e();
        e8[0] = -84;
        e8[1] = 64;
        e8[2] = -1;
        e8[3] = -1;
        e8[4] = (byte) ((i8 >> 16) & 255);
        e8[5] = (byte) ((i8 >> 8) & 255);
        e8[6] = (byte) (i8 & 255);
    }

    public static w0 b(z zVar, String str, String str2, DrmInitData drmInitData) {
        zVar.V(1);
        return new w0.b().U(str).g0("audio/ac4").J(2).h0(((zVar.H() & 32) >> 5) == 1 ? 48000 : 44100).O(drmInitData).X(str2).G();
    }

    public static int c(ByteBuffer byteBuffer) {
        byte[] bArr = new byte[16];
        int position = byteBuffer.position();
        byteBuffer.get(bArr);
        byteBuffer.position(position);
        return d(new y(bArr)).f21002e;
    }

    /* JADX WARN: Code restructure failed: missing block: B:38:0x0082, code lost:
        if (r10 != 11) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0087, code lost:
        if (r10 != 11) goto L43;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x008c, code lost:
        if (r10 != 8) goto L43;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static k4.c.b d(b6.y r10) {
        /*
            r0 = 16
            int r1 = r10.h(r0)
            int r0 = r10.h(r0)
            r2 = 4
            r3 = 65535(0xffff, float:9.1834E-41)
            if (r0 != r3) goto L18
            r0 = 24
            int r0 = r10.h(r0)
            r3 = 7
            goto L19
        L18:
            r3 = r2
        L19:
            int r0 = r0 + r3
            r3 = 44097(0xac41, float:6.1793E-41)
            if (r1 != r3) goto L21
            int r0 = r0 + 2
        L21:
            r7 = r0
            r0 = 2
            int r1 = r10.h(r0)
            r3 = 3
            if (r1 != r3) goto L2f
            int r4 = f(r10, r0)
            int r1 = r1 + r4
        L2f:
            r4 = r1
            r1 = 10
            int r1 = r10.h(r1)
            boolean r5 = r10.g()
            if (r5 == 0) goto L45
            int r5 = r10.h(r3)
            if (r5 <= 0) goto L45
            r10.r(r0)
        L45:
            boolean r5 = r10.g()
            r6 = 48000(0xbb80, float:6.7262E-41)
            r8 = 44100(0xac44, float:6.1797E-41)
            if (r5 == 0) goto L53
            r9 = r6
            goto L54
        L53:
            r9 = r8
        L54:
            int r10 = r10.h(r2)
            r5 = 0
            if (r9 != r8) goto L65
            r8 = 13
            if (r10 != r8) goto L65
            int[] r0 = k4.c.f20997a
            r10 = r0[r10]
            r8 = r10
            goto L91
        L65:
            if (r9 != r6) goto L90
            int[] r6 = k4.c.f20997a
            int r8 = r6.length
            if (r10 >= r8) goto L90
            r5 = r6[r10]
            int r1 = r1 % 5
            r6 = 8
            r8 = 1
            if (r1 == r8) goto L8a
            r8 = 11
            if (r1 == r0) goto L85
            if (r1 == r3) goto L8a
            if (r1 == r2) goto L7e
            goto L90
        L7e:
            if (r10 == r3) goto L8e
            if (r10 == r6) goto L8e
            if (r10 != r8) goto L90
            goto L89
        L85:
            if (r10 == r6) goto L8e
            if (r10 != r8) goto L90
        L89:
            goto L8e
        L8a:
            if (r10 == r3) goto L8e
            if (r10 != r6) goto L90
        L8e:
            int r5 = r5 + 1
        L90:
            r8 = r5
        L91:
            k4.c$b r10 = new k4.c$b
            r5 = 2
            r0 = 0
            r3 = r10
            r6 = r9
            r9 = r0
            r3.<init>(r4, r5, r6, r7, r8)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: k4.c.d(b6.y):k4.c$b");
    }

    public static int e(byte[] bArr, int i8) {
        int i9 = 7;
        if (bArr.length < 7) {
            return -1;
        }
        int i10 = ((bArr[2] & 255) << 8) | (bArr[3] & 255);
        if (i10 == 65535) {
            i10 = ((bArr[4] & 255) << 16) | ((bArr[5] & 255) << 8) | (bArr[6] & 255);
        } else {
            i9 = 4;
        }
        if (i8 == 44097) {
            i9 += 2;
        }
        return i10 + i9;
    }

    private static int f(y yVar, int i8) {
        int i9 = 0;
        while (true) {
            int h8 = i9 + yVar.h(i8);
            if (!yVar.g()) {
                return h8;
            }
            i9 = (h8 + 1) << i8;
        }
    }
}
