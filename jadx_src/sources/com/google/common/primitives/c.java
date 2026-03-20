package com.google.common.primitives;

import java.util.regex.Pattern;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c extends d {

    /* renamed from: a  reason: collision with root package name */
    static final Pattern f19605a = a();

    private static Pattern a() {
        String concat = "(?:\\d+#(?:\\.\\d*#)?|\\.\\d+#)".concat("(?:[eE][+-]?\\d+#)?[fFdD]?");
        StringBuilder sb = new StringBuilder("(?:[0-9a-fA-F]+#(?:\\.[0-9a-fA-F]*#)?|\\.[0-9a-fA-F]+#)".length() + 25);
        sb.append("0[xX]");
        sb.append("(?:[0-9a-fA-F]+#(?:\\.[0-9a-fA-F]*#)?|\\.[0-9a-fA-F]+#)");
        sb.append("[pP][+-]?\\d+#[fFdD]?");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder(String.valueOf(concat).length() + 23 + String.valueOf(sb2).length());
        sb3.append("[+-]?(?:NaN|Infinity|");
        sb3.append(concat);
        sb3.append("|");
        sb3.append(sb2);
        sb3.append(")");
        return Pattern.compile(sb3.toString().replace("#", "+"));
    }

    public static int b(double d8) {
        return Double.valueOf(d8).hashCode();
    }
}
