package com.google.common.base;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c {
    public static boolean a(CharSequence charSequence, CharSequence charSequence2) {
        int b9;
        int length = charSequence.length();
        if (charSequence == charSequence2) {
            return true;
        }
        if (length != charSequence2.length()) {
            return false;
        }
        for (int i8 = 0; i8 < length; i8++) {
            char charAt = charSequence.charAt(i8);
            char charAt2 = charSequence2.charAt(i8);
            if (charAt != charAt2 && ((b9 = b(charAt)) >= 26 || b9 != b(charAt2))) {
                return false;
            }
        }
        return true;
    }

    private static int b(char c9) {
        return (char) ((c9 | ' ') - 97);
    }

    public static boolean c(char c9) {
        return c9 >= 'a' && c9 <= 'z';
    }

    public static boolean d(char c9) {
        return c9 >= 'A' && c9 <= 'Z';
    }

    public static String e(String str) {
        int length = str.length();
        int i8 = 0;
        while (i8 < length) {
            if (d(str.charAt(i8))) {
                char[] charArray = str.toCharArray();
                while (i8 < length) {
                    char c9 = charArray[i8];
                    if (d(c9)) {
                        charArray[i8] = (char) (c9 ^ ' ');
                    }
                    i8++;
                }
                return String.valueOf(charArray);
            }
            i8++;
        }
        return str;
    }

    public static String f(String str) {
        int length = str.length();
        int i8 = 0;
        while (i8 < length) {
            if (c(str.charAt(i8))) {
                char[] charArray = str.toCharArray();
                while (i8 < length) {
                    char c9 = charArray[i8];
                    if (c(c9)) {
                        charArray[i8] = (char) (c9 ^ ' ');
                    }
                    i8++;
                }
                return String.valueOf(charArray);
            }
            i8++;
        }
        return str;
    }
}
