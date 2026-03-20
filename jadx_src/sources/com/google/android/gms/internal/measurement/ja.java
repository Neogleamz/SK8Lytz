package com.google.android.gms.internal.measurement;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class ja {

    /* renamed from: a  reason: collision with root package name */
    private static final char[] f12262a;

    static {
        char[] cArr = new char[80];
        f12262a = cArr;
        Arrays.fill(cArr, ' ');
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String a(ia iaVar, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("# ");
        sb.append(str);
        c(iaVar, sb, 0);
        return sb.toString();
    }

    private static void b(int i8, StringBuilder sb) {
        while (i8 > 0) {
            char[] cArr = f12262a;
            int length = i8 > cArr.length ? cArr.length : i8;
            sb.append(cArr, 0, length);
            i8 -= length;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:55:0x0163, code lost:
        if (r5.containsKey("get" + r9.substring(0, r9.length() - 5)) == false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x0193, code lost:
        if (((java.lang.Boolean) r7).booleanValue() == false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x0195, code lost:
        r13 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:69:0x01a6, code lost:
        if (((java.lang.Integer) r7).intValue() == 0) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x01b8, code lost:
        if (java.lang.Float.floatToRawIntBits(((java.lang.Float) r7).floatValue()) == 0) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x01ce, code lost:
        if (java.lang.Double.doubleToRawLongBits(((java.lang.Double) r7).doubleValue()) == 0) goto L75;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static void c(com.google.android.gms.internal.measurement.ia r20, java.lang.StringBuilder r21, int r22) {
        /*
            Method dump skipped, instructions count: 584
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.internal.measurement.ja.c(com.google.android.gms.internal.measurement.ia, java.lang.StringBuilder, int):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void d(StringBuilder sb, int i8, String str, Object obj) {
        if (obj instanceof List) {
            for (Object obj2 : (List) obj) {
                d(sb, i8, str, obj2);
            }
        } else if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                d(sb, i8, str, entry);
            }
        } else {
            sb.append('\n');
            b(i8, sb);
            if (!str.isEmpty()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(Character.toLowerCase(str.charAt(0)));
                for (int i9 = 1; i9 < str.length(); i9++) {
                    char charAt = str.charAt(i9);
                    if (Character.isUpperCase(charAt)) {
                        sb2.append("_");
                    }
                    sb2.append(Character.toLowerCase(charAt));
                }
                str = sb2.toString();
            }
            sb.append(str);
            if (obj instanceof String) {
                sb.append(": \"");
                sb.append(pb.a(zzij.n((String) obj)));
                sb.append('\"');
            } else if (obj instanceof zzij) {
                sb.append(": \"");
                sb.append(pb.a((zzij) obj));
                sb.append('\"');
            } else if (obj instanceof x8) {
                sb.append(" {");
                c((x8) obj, sb, i8 + 2);
                sb.append("\n");
                b(i8, sb);
                sb.append("}");
            } else if (!(obj instanceof Map.Entry)) {
                sb.append(": ");
                sb.append(obj);
            } else {
                sb.append(" {");
                Map.Entry entry2 = (Map.Entry) obj;
                int i10 = i8 + 2;
                d(sb, i10, "key", entry2.getKey());
                d(sb, i10, "value", entry2.getValue());
                sb.append("\n");
                b(i8, sb);
                sb.append("}");
            }
        }
    }
}
