package u6;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class i {

    /* renamed from: a  reason: collision with root package name */
    private static final char[] f23073a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /* renamed from: b  reason: collision with root package name */
    private static final char[] f23074b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    public static String a(byte[] bArr) {
        int length = bArr.length;
        char[] cArr = new char[length + length];
        int i8 = 0;
        for (byte b9 : bArr) {
            int i9 = b9 & 255;
            int i10 = i8 + 1;
            char[] cArr2 = f23074b;
            cArr[i8] = cArr2[i9 >>> 4];
            cArr[i10] = cArr2[i9 & 15];
            i8 = i10 + 1;
        }
        return new String(cArr);
    }

    public static String b(byte[] bArr, boolean z4) {
        int length = bArr.length;
        StringBuilder sb = new StringBuilder(length + length);
        for (int i8 = 0; i8 < length && (!z4 || i8 != length - 1 || (bArr[i8] & 255) != 0); i8++) {
            char[] cArr = f23073a;
            sb.append(cArr[(bArr[i8] & 240) >>> 4]);
            sb.append(cArr[bArr[i8] & 15]);
        }
        return sb.toString();
    }
}
