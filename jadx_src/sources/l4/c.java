package l4;

import android.media.MediaCodec;
import b6.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {

    /* renamed from: a  reason: collision with root package name */
    public byte[] f21573a;

    /* renamed from: b  reason: collision with root package name */
    public byte[] f21574b;

    /* renamed from: c  reason: collision with root package name */
    public int f21575c;

    /* renamed from: d  reason: collision with root package name */
    public int[] f21576d;

    /* renamed from: e  reason: collision with root package name */
    public int[] f21577e;

    /* renamed from: f  reason: collision with root package name */
    public int f21578f;

    /* renamed from: g  reason: collision with root package name */
    public int f21579g;

    /* renamed from: h  reason: collision with root package name */
    public int f21580h;

    /* renamed from: i  reason: collision with root package name */
    private final MediaCodec.CryptoInfo f21581i;

    /* renamed from: j  reason: collision with root package name */
    private final b f21582j;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b {

        /* renamed from: a  reason: collision with root package name */
        private final MediaCodec.CryptoInfo f21583a;

        /* renamed from: b  reason: collision with root package name */
        private final MediaCodec.CryptoInfo.Pattern f21584b;

        private b(MediaCodec.CryptoInfo cryptoInfo) {
            this.f21583a = cryptoInfo;
            this.f21584b = new MediaCodec.CryptoInfo.Pattern(0, 0);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void b(int i8, int i9) {
            this.f21584b.set(i8, i9);
            this.f21583a.setPattern(this.f21584b);
        }
    }

    public c() {
        MediaCodec.CryptoInfo cryptoInfo = new MediaCodec.CryptoInfo();
        this.f21581i = cryptoInfo;
        this.f21582j = l0.f8063a >= 24 ? new b(cryptoInfo) : null;
    }

    public MediaCodec.CryptoInfo a() {
        return this.f21581i;
    }

    public void b(int i8) {
        if (i8 == 0) {
            return;
        }
        if (this.f21576d == null) {
            int[] iArr = new int[1];
            this.f21576d = iArr;
            this.f21581i.numBytesOfClearData = iArr;
        }
        int[] iArr2 = this.f21576d;
        iArr2[0] = iArr2[0] + i8;
    }

    public void c(int i8, int[] iArr, int[] iArr2, byte[] bArr, byte[] bArr2, int i9, int i10, int i11) {
        this.f21578f = i8;
        this.f21576d = iArr;
        this.f21577e = iArr2;
        this.f21574b = bArr;
        this.f21573a = bArr2;
        this.f21575c = i9;
        this.f21579g = i10;
        this.f21580h = i11;
        MediaCodec.CryptoInfo cryptoInfo = this.f21581i;
        cryptoInfo.numSubSamples = i8;
        cryptoInfo.numBytesOfClearData = iArr;
        cryptoInfo.numBytesOfEncryptedData = iArr2;
        cryptoInfo.key = bArr;
        cryptoInfo.iv = bArr2;
        cryptoInfo.mode = i9;
        if (l0.f8063a >= 24) {
            ((b) b6.a.e(this.f21582j)).b(i10, i11);
        }
    }
}
