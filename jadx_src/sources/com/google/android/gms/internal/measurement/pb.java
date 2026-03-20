package com.google.android.gms.internal.measurement;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class pb {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static String a(zzij zzijVar) {
        String str;
        sb sbVar = new sb(zzijVar);
        StringBuilder sb = new StringBuilder(sbVar.zza());
        for (int i8 = 0; i8 < sbVar.zza(); i8++) {
            int c9 = sbVar.c(i8);
            if (c9 == 34) {
                str = "\\\"";
            } else if (c9 == 39) {
                str = "\\'";
            } else if (c9 != 92) {
                switch (c9) {
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
                        if (c9 < 32 || c9 > 126) {
                            sb.append('\\');
                            sb.append((char) (((c9 >>> 6) & 3) + 48));
                            sb.append((char) (((c9 >>> 3) & 7) + 48));
                            c9 = (c9 & 7) + 48;
                        }
                        sb.append((char) c9);
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
