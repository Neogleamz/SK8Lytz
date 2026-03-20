package androidx.core.text;

import android.text.SpannableStringBuilder;
import com.daimajia.numberprogressbar.BuildConfig;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: d  reason: collision with root package name */
    static final d f4836d;

    /* renamed from: e  reason: collision with root package name */
    private static final String f4837e;

    /* renamed from: f  reason: collision with root package name */
    private static final String f4838f;

    /* renamed from: g  reason: collision with root package name */
    static final a f4839g;

    /* renamed from: h  reason: collision with root package name */
    static final a f4840h;

    /* renamed from: a  reason: collision with root package name */
    private final boolean f4841a;

    /* renamed from: b  reason: collision with root package name */
    private final int f4842b;

    /* renamed from: c  reason: collision with root package name */
    private final d f4843c;

    /* renamed from: androidx.core.text.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class C0038a {

        /* renamed from: a  reason: collision with root package name */
        private boolean f4844a;

        /* renamed from: b  reason: collision with root package name */
        private int f4845b;

        /* renamed from: c  reason: collision with root package name */
        private d f4846c;

        public C0038a() {
            c(a.e(Locale.getDefault()));
        }

        private static a b(boolean z4) {
            return z4 ? a.f4840h : a.f4839g;
        }

        private void c(boolean z4) {
            this.f4844a = z4;
            this.f4846c = a.f4836d;
            this.f4845b = 2;
        }

        public a a() {
            return (this.f4845b == 2 && this.f4846c == a.f4836d) ? b(this.f4844a) : new a(this.f4844a, this.f4845b, this.f4846c);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {

        /* renamed from: f  reason: collision with root package name */
        private static final byte[] f4847f = new byte[1792];

        /* renamed from: a  reason: collision with root package name */
        private final CharSequence f4848a;

        /* renamed from: b  reason: collision with root package name */
        private final boolean f4849b;

        /* renamed from: c  reason: collision with root package name */
        private final int f4850c;

        /* renamed from: d  reason: collision with root package name */
        private int f4851d;

        /* renamed from: e  reason: collision with root package name */
        private char f4852e;

        static {
            for (int i8 = 0; i8 < 1792; i8++) {
                f4847f[i8] = Character.getDirectionality(i8);
            }
        }

        b(CharSequence charSequence, boolean z4) {
            this.f4848a = charSequence;
            this.f4849b = z4;
            this.f4850c = charSequence.length();
        }

        private static byte c(char c9) {
            return c9 < 1792 ? f4847f[c9] : Character.getDirectionality(c9);
        }

        private byte f() {
            char charAt;
            int i8 = this.f4851d;
            do {
                int i9 = this.f4851d;
                if (i9 <= 0) {
                    break;
                }
                CharSequence charSequence = this.f4848a;
                int i10 = i9 - 1;
                this.f4851d = i10;
                charAt = charSequence.charAt(i10);
                this.f4852e = charAt;
                if (charAt == '&') {
                    return (byte) 12;
                }
            } while (charAt != ';');
            this.f4851d = i8;
            this.f4852e = ';';
            return (byte) 13;
        }

        private byte g() {
            char charAt;
            do {
                int i8 = this.f4851d;
                if (i8 >= this.f4850c) {
                    return (byte) 12;
                }
                CharSequence charSequence = this.f4848a;
                this.f4851d = i8 + 1;
                charAt = charSequence.charAt(i8);
                this.f4852e = charAt;
            } while (charAt != ';');
            return (byte) 12;
        }

        private byte h() {
            char charAt;
            int i8 = this.f4851d;
            while (true) {
                int i9 = this.f4851d;
                if (i9 <= 0) {
                    break;
                }
                CharSequence charSequence = this.f4848a;
                int i10 = i9 - 1;
                this.f4851d = i10;
                char charAt2 = charSequence.charAt(i10);
                this.f4852e = charAt2;
                if (charAt2 == '<') {
                    return (byte) 12;
                }
                if (charAt2 == '>') {
                    break;
                } else if (charAt2 == '\"' || charAt2 == '\'') {
                    do {
                        int i11 = this.f4851d;
                        if (i11 > 0) {
                            CharSequence charSequence2 = this.f4848a;
                            int i12 = i11 - 1;
                            this.f4851d = i12;
                            charAt = charSequence2.charAt(i12);
                            this.f4852e = charAt;
                        }
                    } while (charAt != charAt2);
                }
            }
            this.f4851d = i8;
            this.f4852e = '>';
            return (byte) 13;
        }

        private byte i() {
            char charAt;
            int i8 = this.f4851d;
            while (true) {
                int i9 = this.f4851d;
                if (i9 >= this.f4850c) {
                    this.f4851d = i8;
                    this.f4852e = '<';
                    return (byte) 13;
                }
                CharSequence charSequence = this.f4848a;
                this.f4851d = i9 + 1;
                char charAt2 = charSequence.charAt(i9);
                this.f4852e = charAt2;
                if (charAt2 == '>') {
                    return (byte) 12;
                }
                if (charAt2 == '\"' || charAt2 == '\'') {
                    do {
                        int i10 = this.f4851d;
                        if (i10 < this.f4850c) {
                            CharSequence charSequence2 = this.f4848a;
                            this.f4851d = i10 + 1;
                            charAt = charSequence2.charAt(i10);
                            this.f4852e = charAt;
                        }
                    } while (charAt != charAt2);
                }
            }
        }

        byte a() {
            char charAt = this.f4848a.charAt(this.f4851d - 1);
            this.f4852e = charAt;
            if (Character.isLowSurrogate(charAt)) {
                int codePointBefore = Character.codePointBefore(this.f4848a, this.f4851d);
                this.f4851d -= Character.charCount(codePointBefore);
                return Character.getDirectionality(codePointBefore);
            }
            this.f4851d--;
            byte c9 = c(this.f4852e);
            if (this.f4849b) {
                char c10 = this.f4852e;
                return c10 == '>' ? h() : c10 == ';' ? f() : c9;
            }
            return c9;
        }

        byte b() {
            char charAt = this.f4848a.charAt(this.f4851d);
            this.f4852e = charAt;
            if (Character.isHighSurrogate(charAt)) {
                int codePointAt = Character.codePointAt(this.f4848a, this.f4851d);
                this.f4851d += Character.charCount(codePointAt);
                return Character.getDirectionality(codePointAt);
            }
            this.f4851d++;
            byte c9 = c(this.f4852e);
            if (this.f4849b) {
                char c10 = this.f4852e;
                return c10 == '<' ? i() : c10 == '&' ? g() : c9;
            }
            return c9;
        }

        int d() {
            this.f4851d = 0;
            int i8 = 0;
            int i9 = 0;
            int i10 = 0;
            while (this.f4851d < this.f4850c && i8 == 0) {
                byte b9 = b();
                if (b9 != 0) {
                    if (b9 == 1 || b9 == 2) {
                        if (i10 == 0) {
                            return 1;
                        }
                    } else if (b9 != 9) {
                        switch (b9) {
                            case 14:
                            case 15:
                                i10++;
                                i9 = -1;
                                break;
                            case 16:
                            case 17:
                                i10++;
                                i9 = 1;
                                break;
                            case 18:
                                i10--;
                                i9 = 0;
                                break;
                        }
                    }
                } else if (i10 == 0) {
                    return -1;
                }
                i8 = i10;
            }
            if (i8 == 0) {
                return 0;
            }
            if (i9 != 0) {
                return i9;
            }
            while (this.f4851d > 0) {
                switch (a()) {
                    case 14:
                    case 15:
                        if (i8 == i10) {
                            return -1;
                        }
                        break;
                    case 16:
                    case 17:
                        if (i8 == i10) {
                            return 1;
                        }
                        break;
                    case 18:
                        i10++;
                        continue;
                }
                i10--;
            }
            return 0;
        }

        int e() {
            this.f4851d = this.f4850c;
            int i8 = 0;
            while (true) {
                int i9 = i8;
                while (this.f4851d > 0) {
                    byte a9 = a();
                    if (a9 != 0) {
                        if (a9 == 1 || a9 == 2) {
                            if (i8 == 0) {
                                return 1;
                            }
                            if (i9 == 0) {
                                break;
                            }
                        } else if (a9 != 9) {
                            switch (a9) {
                                case 14:
                                case 15:
                                    if (i9 == i8) {
                                        return -1;
                                    }
                                    i8--;
                                    break;
                                case 16:
                                case 17:
                                    if (i9 == i8) {
                                        return 1;
                                    }
                                    i8--;
                                    break;
                                case 18:
                                    i8++;
                                    break;
                                default:
                                    if (i9 != 0) {
                                        break;
                                    } else {
                                        break;
                                    }
                            }
                        } else {
                            continue;
                        }
                    } else if (i8 == 0) {
                        return -1;
                    } else {
                        if (i9 == 0) {
                            break;
                        }
                    }
                }
                return 0;
            }
        }
    }

    static {
        d dVar = e.f4870c;
        f4836d = dVar;
        f4837e = Character.toString((char) 8206);
        f4838f = Character.toString((char) 8207);
        f4839g = new a(false, 2, dVar);
        f4840h = new a(true, 2, dVar);
    }

    a(boolean z4, int i8, d dVar) {
        this.f4841a = z4;
        this.f4842b = i8;
        this.f4843c = dVar;
    }

    private static int a(CharSequence charSequence) {
        return new b(charSequence, false).d();
    }

    private static int b(CharSequence charSequence) {
        return new b(charSequence, false).e();
    }

    public static a c() {
        return new C0038a().a();
    }

    static boolean e(Locale locale) {
        return f.b(locale) == 1;
    }

    private String f(CharSequence charSequence, d dVar) {
        boolean a9 = dVar.a(charSequence, 0, charSequence.length());
        return (this.f4841a || !(a9 || b(charSequence) == 1)) ? this.f4841a ? (!a9 || b(charSequence) == -1) ? f4838f : BuildConfig.FLAVOR : BuildConfig.FLAVOR : f4837e;
    }

    private String g(CharSequence charSequence, d dVar) {
        boolean a9 = dVar.a(charSequence, 0, charSequence.length());
        return (this.f4841a || !(a9 || a(charSequence) == 1)) ? this.f4841a ? (!a9 || a(charSequence) == -1) ? f4838f : BuildConfig.FLAVOR : BuildConfig.FLAVOR : f4837e;
    }

    public boolean d() {
        return (this.f4842b & 2) != 0;
    }

    public CharSequence h(CharSequence charSequence) {
        return i(charSequence, this.f4843c, true);
    }

    public CharSequence i(CharSequence charSequence, d dVar, boolean z4) {
        if (charSequence == null) {
            return null;
        }
        boolean a9 = dVar.a(charSequence, 0, charSequence.length());
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if (d() && z4) {
            spannableStringBuilder.append((CharSequence) g(charSequence, a9 ? e.f4869b : e.f4868a));
        }
        if (a9 != this.f4841a) {
            spannableStringBuilder.append(a9 ? (char) 8235 : (char) 8234);
            spannableStringBuilder.append(charSequence);
            spannableStringBuilder.append((char) 8236);
        } else {
            spannableStringBuilder.append(charSequence);
        }
        if (z4) {
            spannableStringBuilder.append((CharSequence) f(charSequence, a9 ? e.f4869b : e.f4868a));
        }
        return spannableStringBuilder;
    }

    public String j(String str) {
        return k(str, this.f4843c, true);
    }

    public String k(String str, d dVar, boolean z4) {
        if (str == null) {
            return null;
        }
        return i(str, dVar, z4).toString();
    }
}
