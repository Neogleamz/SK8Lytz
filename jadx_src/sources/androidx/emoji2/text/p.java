package androidx.emoji2.text;

import android.os.Build;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.SpannableString;
import java.util.stream.IntStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class p implements Spannable {

    /* renamed from: a  reason: collision with root package name */
    private boolean f5305a = false;

    /* renamed from: b  reason: collision with root package name */
    private Spannable f5306b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {
        static IntStream a(CharSequence charSequence) {
            return charSequence.chars();
        }

        static IntStream b(CharSequence charSequence) {
            return charSequence.codePoints();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        b() {
        }

        boolean a(CharSequence charSequence) {
            return charSequence instanceof androidx.core.text.c;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c extends b {
        c() {
        }

        @Override // androidx.emoji2.text.p.b
        boolean a(CharSequence charSequence) {
            return (charSequence instanceof PrecomputedText) || (charSequence instanceof androidx.core.text.c);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(Spannable spannable) {
        this.f5306b = spannable;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public p(CharSequence charSequence) {
        this.f5306b = new SpannableString(charSequence);
    }

    private void a() {
        Spannable spannable = this.f5306b;
        if (!this.f5305a && c().a(spannable)) {
            this.f5306b = new SpannableString(spannable);
        }
        this.f5305a = true;
    }

    static b c() {
        return Build.VERSION.SDK_INT < 28 ? new b() : new c();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Spannable b() {
        return this.f5306b;
    }

    @Override // java.lang.CharSequence
    public char charAt(int i8) {
        return this.f5306b.charAt(i8);
    }

    @Override // java.lang.CharSequence
    public IntStream chars() {
        return a.a(this.f5306b);
    }

    @Override // java.lang.CharSequence
    public IntStream codePoints() {
        return a.b(this.f5306b);
    }

    @Override // android.text.Spanned
    public int getSpanEnd(Object obj) {
        return this.f5306b.getSpanEnd(obj);
    }

    @Override // android.text.Spanned
    public int getSpanFlags(Object obj) {
        return this.f5306b.getSpanFlags(obj);
    }

    @Override // android.text.Spanned
    public int getSpanStart(Object obj) {
        return this.f5306b.getSpanStart(obj);
    }

    @Override // android.text.Spanned
    public <T> T[] getSpans(int i8, int i9, Class<T> cls) {
        return (T[]) this.f5306b.getSpans(i8, i9, cls);
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.f5306b.length();
    }

    @Override // android.text.Spanned
    public int nextSpanTransition(int i8, int i9, Class cls) {
        return this.f5306b.nextSpanTransition(i8, i9, cls);
    }

    @Override // android.text.Spannable
    public void removeSpan(Object obj) {
        a();
        this.f5306b.removeSpan(obj);
    }

    @Override // android.text.Spannable
    public void setSpan(Object obj, int i8, int i9, int i10) {
        a();
        this.f5306b.setSpan(obj, i8, i9, i10);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int i8, int i9) {
        return this.f5306b.subSequence(i8, i9);
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this.f5306b.toString();
    }
}
