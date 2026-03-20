package v4;

import n4.b0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class p {

    /* renamed from: a  reason: collision with root package name */
    public final boolean f23310a;

    /* renamed from: b  reason: collision with root package name */
    public final String f23311b;

    /* renamed from: c  reason: collision with root package name */
    public final b0.a f23312c;

    /* renamed from: d  reason: collision with root package name */
    public final int f23313d;

    /* renamed from: e  reason: collision with root package name */
    public final byte[] f23314e;

    public p(boolean z4, String str, int i8, byte[] bArr, int i9, int i10, byte[] bArr2) {
        b6.a.a((bArr2 == null) ^ (i8 == 0));
        this.f23310a = z4;
        this.f23311b = str;
        this.f23313d = i8;
        this.f23314e = bArr2;
        this.f23312c = new b0.a(a(str), bArr, i9, i10);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static int a(String str) {
        if (str == null) {
            return 1;
        }
        char c9 = 65535;
        switch (str.hashCode()) {
            case 3046605:
                if (str.equals("cbc1")) {
                    c9 = 0;
                    break;
                }
                break;
            case 3046671:
                if (str.equals("cbcs")) {
                    c9 = 1;
                    break;
                }
                break;
            case 3049879:
                if (str.equals("cenc")) {
                    c9 = 2;
                    break;
                }
                break;
            case 3049895:
                if (str.equals("cens")) {
                    c9 = 3;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
            case 1:
                return 2;
            case 2:
            case 3:
                break;
            default:
                b6.p.i("TrackEncryptionBox", "Unsupported protection scheme type '" + str + "'. Assuming AES-CTR crypto mode.");
                break;
        }
        return 1;
    }
}
