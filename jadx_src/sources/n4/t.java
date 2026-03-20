package n4;

import b6.l0;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.flac.PictureFrame;
import com.google.android.exoplayer2.w0;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class t {

    /* renamed from: a  reason: collision with root package name */
    public final int f22129a;

    /* renamed from: b  reason: collision with root package name */
    public final int f22130b;

    /* renamed from: c  reason: collision with root package name */
    public final int f22131c;

    /* renamed from: d  reason: collision with root package name */
    public final int f22132d;

    /* renamed from: e  reason: collision with root package name */
    public final int f22133e;

    /* renamed from: f  reason: collision with root package name */
    public final int f22134f;

    /* renamed from: g  reason: collision with root package name */
    public final int f22135g;

    /* renamed from: h  reason: collision with root package name */
    public final int f22136h;

    /* renamed from: i  reason: collision with root package name */
    public final int f22137i;

    /* renamed from: j  reason: collision with root package name */
    public final long f22138j;

    /* renamed from: k  reason: collision with root package name */
    public final a f22139k;

    /* renamed from: l  reason: collision with root package name */
    private final Metadata f22140l;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        public final long[] f22141a;

        /* renamed from: b  reason: collision with root package name */
        public final long[] f22142b;

        public a(long[] jArr, long[] jArr2) {
            this.f22141a = jArr;
            this.f22142b = jArr2;
        }
    }

    private t(int i8, int i9, int i10, int i11, int i12, int i13, int i14, long j8, a aVar, Metadata metadata) {
        this.f22129a = i8;
        this.f22130b = i9;
        this.f22131c = i10;
        this.f22132d = i11;
        this.f22133e = i12;
        this.f22134f = j(i12);
        this.f22135g = i13;
        this.f22136h = i14;
        this.f22137i = e(i14);
        this.f22138j = j8;
        this.f22139k = aVar;
        this.f22140l = metadata;
    }

    public t(byte[] bArr, int i8) {
        b6.y yVar = new b6.y(bArr);
        yVar.p(i8 * 8);
        this.f22129a = yVar.h(16);
        this.f22130b = yVar.h(16);
        this.f22131c = yVar.h(24);
        this.f22132d = yVar.h(24);
        int h8 = yVar.h(20);
        this.f22133e = h8;
        this.f22134f = j(h8);
        this.f22135g = yVar.h(3) + 1;
        int h9 = yVar.h(5) + 1;
        this.f22136h = h9;
        this.f22137i = e(h9);
        this.f22138j = yVar.j(36);
        this.f22139k = null;
        this.f22140l = null;
    }

    private static int e(int i8) {
        if (i8 != 8) {
            if (i8 != 12) {
                if (i8 != 16) {
                    if (i8 != 20) {
                        return i8 != 24 ? -1 : 6;
                    }
                    return 5;
                }
                return 4;
            }
            return 2;
        }
        return 1;
    }

    private static int j(int i8) {
        switch (i8) {
            case 8000:
                return 4;
            case 16000:
                return 5;
            case 22050:
                return 6;
            case 24000:
                return 7;
            case 32000:
                return 8;
            case 44100:
                return 9;
            case 48000:
                return 10;
            case 88200:
                return 1;
            case 96000:
                return 11;
            case 176400:
                return 2;
            case 192000:
                return 3;
            default:
                return -1;
        }
    }

    public t a(List<PictureFrame> list) {
        return new t(this.f22129a, this.f22130b, this.f22131c, this.f22132d, this.f22133e, this.f22135g, this.f22136h, this.f22138j, this.f22139k, h(new Metadata(list)));
    }

    public t b(a aVar) {
        return new t(this.f22129a, this.f22130b, this.f22131c, this.f22132d, this.f22133e, this.f22135g, this.f22136h, this.f22138j, aVar, this.f22140l);
    }

    public t c(List<String> list) {
        return new t(this.f22129a, this.f22130b, this.f22131c, this.f22132d, this.f22133e, this.f22135g, this.f22136h, this.f22138j, this.f22139k, h(e0.c(list)));
    }

    public long d() {
        long j8;
        long j9;
        int i8 = this.f22132d;
        if (i8 > 0) {
            j8 = (i8 + this.f22131c) / 2;
            j9 = 1;
        } else {
            int i9 = this.f22129a;
            j8 = ((((i9 != this.f22130b || i9 <= 0) ? 4096L : i9) * this.f22135g) * this.f22136h) / 8;
            j9 = 64;
        }
        return j8 + j9;
    }

    public long f() {
        long j8 = this.f22138j;
        if (j8 == 0) {
            return -9223372036854775807L;
        }
        return (j8 * 1000000) / this.f22133e;
    }

    public w0 g(byte[] bArr, Metadata metadata) {
        bArr[4] = Byte.MIN_VALUE;
        int i8 = this.f22132d;
        if (i8 <= 0) {
            i8 = -1;
        }
        return new w0.b().g0("audio/flac").Y(i8).J(this.f22135g).h0(this.f22133e).V(Collections.singletonList(bArr)).Z(h(metadata)).G();
    }

    public Metadata h(Metadata metadata) {
        Metadata metadata2 = this.f22140l;
        return metadata2 == null ? metadata : metadata2.b(metadata);
    }

    public long i(long j8) {
        return l0.r((j8 * this.f22133e) / 1000000, 0L, this.f22138j - 1);
    }
}
