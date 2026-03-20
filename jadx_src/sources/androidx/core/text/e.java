package androidx.core.text;

import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: a  reason: collision with root package name */
    public static final androidx.core.text.d f4868a = new C0041e(null, false);

    /* renamed from: b  reason: collision with root package name */
    public static final androidx.core.text.d f4869b = new C0041e(null, true);

    /* renamed from: c  reason: collision with root package name */
    public static final androidx.core.text.d f4870c;

    /* renamed from: d  reason: collision with root package name */
    public static final androidx.core.text.d f4871d;

    /* renamed from: e  reason: collision with root package name */
    public static final androidx.core.text.d f4872e;

    /* renamed from: f  reason: collision with root package name */
    public static final androidx.core.text.d f4873f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a implements c {

        /* renamed from: b  reason: collision with root package name */
        static final a f4874b = new a(true);

        /* renamed from: a  reason: collision with root package name */
        private final boolean f4875a;

        private a(boolean z4) {
            this.f4875a = z4;
        }

        @Override // androidx.core.text.e.c
        public int a(CharSequence charSequence, int i8, int i9) {
            int i10 = i9 + i8;
            boolean z4 = false;
            while (i8 < i10) {
                int a9 = e.a(Character.getDirectionality(charSequence.charAt(i8)));
                if (a9 != 0) {
                    if (a9 != 1) {
                        continue;
                        i8++;
                        z4 = z4;
                    } else if (!this.f4875a) {
                        return 1;
                    }
                } else if (this.f4875a) {
                    return 0;
                }
                z4 = true;
                i8++;
                z4 = z4;
            }
            if (z4) {
                return this.f4875a ? 1 : 0;
            }
            return 2;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class b implements c {

        /* renamed from: a  reason: collision with root package name */
        static final b f4876a = new b();

        private b() {
        }

        @Override // androidx.core.text.e.c
        public int a(CharSequence charSequence, int i8, int i9) {
            int i10 = i9 + i8;
            int i11 = 2;
            while (i8 < i10 && i11 == 2) {
                i11 = e.b(Character.getDirectionality(charSequence.charAt(i8)));
                i8++;
            }
            return i11;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface c {
        int a(CharSequence charSequence, int i8, int i9);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static abstract class d implements androidx.core.text.d {

        /* renamed from: a  reason: collision with root package name */
        private final c f4877a;

        d(c cVar) {
            this.f4877a = cVar;
        }

        private boolean c(CharSequence charSequence, int i8, int i9) {
            int a9 = this.f4877a.a(charSequence, i8, i9);
            if (a9 != 0) {
                if (a9 != 1) {
                    return b();
                }
                return false;
            }
            return true;
        }

        @Override // androidx.core.text.d
        public boolean a(CharSequence charSequence, int i8, int i9) {
            if (charSequence == null || i8 < 0 || i9 < 0 || charSequence.length() - i9 < i8) {
                throw new IllegalArgumentException();
            }
            return this.f4877a == null ? b() : c(charSequence, i8, i9);
        }

        protected abstract boolean b();
    }

    /* renamed from: androidx.core.text.e$e  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class C0041e extends d {

        /* renamed from: b  reason: collision with root package name */
        private final boolean f4878b;

        C0041e(c cVar, boolean z4) {
            super(cVar);
            this.f4878b = z4;
        }

        @Override // androidx.core.text.e.d
        protected boolean b() {
            return this.f4878b;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class f extends d {

        /* renamed from: b  reason: collision with root package name */
        static final f f4879b = new f();

        f() {
            super(null);
        }

        @Override // androidx.core.text.e.d
        protected boolean b() {
            return androidx.core.text.f.b(Locale.getDefault()) == 1;
        }
    }

    static {
        b bVar = b.f4876a;
        f4870c = new C0041e(bVar, false);
        f4871d = new C0041e(bVar, true);
        f4872e = new C0041e(a.f4874b, false);
        f4873f = f.f4879b;
    }

    static int a(int i8) {
        if (i8 != 0) {
            return (i8 == 1 || i8 == 2) ? 0 : 2;
        }
        return 1;
    }

    static int b(int i8) {
        if (i8 != 0) {
            if (i8 == 1 || i8 == 2) {
                return 0;
            }
            switch (i8) {
                case 14:
                case 15:
                    break;
                case 16:
                case 17:
                    return 0;
                default:
                    return 2;
            }
        }
        return 1;
    }
}
