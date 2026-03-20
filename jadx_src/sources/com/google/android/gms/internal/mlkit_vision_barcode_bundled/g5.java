package com.google.android.gms.internal.mlkit_vision_barcode_bundled;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class g5 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static String a(zzdb zzdbVar) {
        String str;
        StringBuilder sb = new StringBuilder(zzdbVar.i());
        for (int i8 = 0; i8 < zzdbVar.i(); i8++) {
            int e8 = zzdbVar.e(i8);
            if (e8 == 34) {
                str = "\\\"";
            } else if (e8 == 39) {
                str = "\\'";
            } else if (e8 != 92) {
                switch (e8) {
                    case 7:
                        str = "\\a";
                        break;
                    case 8:
                        str = "\\b";
                        break;
                    case 9:
                        str = "\\t";
                        break;
                    case 10:
                        str = "\\n";
                        break;
                    case 11:
                        str = "\\v";
                        break;
                    case 12:
                        str = "\\f";
                        break;
                    case 13:
                        str = "\\r";
                        break;
                    default:
                        if (e8 < 32 || e8 > 126) {
                            sb.append('\\');
                            sb.append((char) (((e8 >>> 6) & 3) + 48));
                            sb.append((char) (((e8 >>> 3) & 7) + 48));
                            e8 = (e8 & 7) + 48;
                        }
                        sb.append((char) e8);
                        continue;
                        break;
                }
            } else {
                str = "\\\\";
            }
            sb.append(str);
        }
        return sb.toString();
    }
}
