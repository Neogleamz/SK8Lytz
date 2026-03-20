package com.google.common.base;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class d implements m<Character> {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class a extends d {
        a() {
        }

        @Override // com.google.common.base.m
        @Deprecated
        public /* bridge */ /* synthetic */ boolean apply(Character ch2) {
            return super.b(ch2);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class b extends a {

        /* renamed from: a  reason: collision with root package name */
        private final char f18810a;

        /* renamed from: b  reason: collision with root package name */
        private final char f18811b;

        b(char c9, char c10) {
            l.d(c10 >= c9);
            this.f18810a = c9;
            this.f18811b = c10;
        }

        @Override // com.google.common.base.d
        public boolean f(char c9) {
            return this.f18810a <= c9 && c9 <= this.f18811b;
        }

        public String toString() {
            String h8 = d.h(this.f18810a);
            String h9 = d.h(this.f18811b);
            StringBuilder sb = new StringBuilder(String.valueOf(h8).length() + 27 + String.valueOf(h9).length());
            sb.append("CharMatcher.inRange('");
            sb.append(h8);
            sb.append("', '");
            sb.append(h9);
            sb.append("')");
            return sb.toString();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class c extends a {

        /* renamed from: a  reason: collision with root package name */
        private final char f18812a;

        c(char c9) {
            this.f18812a = c9;
        }

        @Override // com.google.common.base.d
        public boolean f(char c9) {
            return c9 == this.f18812a;
        }

        public String toString() {
            String h8 = d.h(this.f18812a);
            StringBuilder sb = new StringBuilder(String.valueOf(h8).length() + 18);
            sb.append("CharMatcher.is('");
            sb.append(h8);
            sb.append("')");
            return sb.toString();
        }
    }

    /* renamed from: com.google.common.base.d$d  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static abstract class AbstractC0146d extends a {

        /* renamed from: a  reason: collision with root package name */
        private final String f18813a;

        AbstractC0146d(String str) {
            this.f18813a = (String) l.n(str);
        }

        public final String toString() {
            return this.f18813a;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class e extends AbstractC0146d {

        /* renamed from: b  reason: collision with root package name */
        static final e f18814b = new e();

        private e() {
            super("CharMatcher.none()");
        }

        @Override // com.google.common.base.d
        public int d(CharSequence charSequence, int i8) {
            l.p(i8, charSequence.length());
            return -1;
        }

        @Override // com.google.common.base.d
        public boolean f(char c9) {
            return false;
        }
    }

    protected d() {
    }

    public static d c(char c9, char c10) {
        return new b(c9, c10);
    }

    public static d e(char c9) {
        return new c(c9);
    }

    public static d g() {
        return e.f18814b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String h(char c9) {
        char[] cArr = {'\\', 'u', 0, 0, 0, 0};
        for (int i8 = 0; i8 < 4; i8++) {
            cArr[5 - i8] = "0123456789ABCDEF".charAt(c9 & 15);
            c9 = (char) (c9 >> 4);
        }
        return String.copyValueOf(cArr);
    }

    @Deprecated
    public boolean b(Character ch2) {
        return f(ch2.charValue());
    }

    public int d(CharSequence charSequence, int i8) {
        int length = charSequence.length();
        l.p(i8, length);
        while (i8 < length) {
            if (f(charSequence.charAt(i8))) {
                return i8;
            }
            i8++;
        }
        return -1;
    }

    public abstract boolean f(char c9);
}
