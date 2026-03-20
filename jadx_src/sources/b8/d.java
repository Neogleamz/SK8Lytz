package b8;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static void a(boolean z4, String str, int i8, int i9) {
        if (z4) {
            return;
        }
        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 36);
        sb.append("overflow: ");
        sb.append(str);
        sb.append("(");
        sb.append(i8);
        sb.append(", ");
        sb.append(i9);
        sb.append(")");
        throw new ArithmeticException(sb.toString());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b(boolean z4) {
        if (!z4) {
            throw new ArithmeticException("mode was UNNECESSARY, but rounding was necessary");
        }
    }
}
