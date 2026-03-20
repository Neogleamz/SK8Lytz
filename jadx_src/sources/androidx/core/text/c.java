package androidx.core.text;

import android.os.Build;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c implements Spannable {

    /* renamed from: d  reason: collision with root package name */
    private static final Object f4855d = new Object();

    /* renamed from: a  reason: collision with root package name */
    private final Spannable f4856a;

    /* renamed from: b  reason: collision with root package name */
    private final a f4857b;

    /* renamed from: c  reason: collision with root package name */
    private final PrecomputedText f4858c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private final TextPaint f4859a;

        /* renamed from: b  reason: collision with root package name */
        private final TextDirectionHeuristic f4860b;

        /* renamed from: c  reason: collision with root package name */
        private final int f4861c;

        /* renamed from: d  reason: collision with root package name */
        private final int f4862d;

        /* renamed from: e  reason: collision with root package name */
        final PrecomputedText.Params f4863e;

        /* renamed from: androidx.core.text.c$a$a  reason: collision with other inner class name */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class C0040a {

            /* renamed from: a  reason: collision with root package name */
            private final TextPaint f4864a;

            /* renamed from: b  reason: collision with root package name */
            private TextDirectionHeuristic f4865b;

            /* renamed from: c  reason: collision with root package name */
            private int f4866c;

            /* renamed from: d  reason: collision with root package name */
            private int f4867d;

            public C0040a(TextPaint textPaint) {
                this.f4864a = textPaint;
                int i8 = Build.VERSION.SDK_INT;
                if (i8 >= 23) {
                    this.f4866c = 1;
                    this.f4867d = 1;
                } else {
                    this.f4867d = 0;
                    this.f4866c = 0;
                }
                this.f4865b = i8 >= 18 ? TextDirectionHeuristics.FIRSTSTRONG_LTR : null;
            }

            public a a() {
                return new a(this.f4864a, this.f4865b, this.f4866c, this.f4867d);
            }

            public C0040a b(int i8) {
                this.f4866c = i8;
                return this;
            }

            public C0040a c(int i8) {
                this.f4867d = i8;
                return this;
            }

            public C0040a d(TextDirectionHeuristic textDirectionHeuristic) {
                this.f4865b = textDirectionHeuristic;
                return this;
            }
        }

        public a(PrecomputedText.Params params) {
            this.f4859a = params.getTextPaint();
            this.f4860b = params.getTextDirection();
            this.f4861c = params.getBreakStrategy();
            this.f4862d = params.getHyphenationFrequency();
            this.f4863e = Build.VERSION.SDK_INT < 29 ? null : params;
        }

        a(TextPaint textPaint, TextDirectionHeuristic textDirectionHeuristic, int i8, int i9) {
            this.f4863e = Build.VERSION.SDK_INT >= 29 ? new PrecomputedText.Params.Builder(textPaint).setBreakStrategy(i8).setHyphenationFrequency(i9).setTextDirection(textDirectionHeuristic).build() : null;
            this.f4859a = textPaint;
            this.f4860b = textDirectionHeuristic;
            this.f4861c = i8;
            this.f4862d = i9;
        }

        public boolean a(a aVar) {
            int i8 = Build.VERSION.SDK_INT;
            if ((i8 < 23 || (this.f4861c == aVar.b() && this.f4862d == aVar.c())) && this.f4859a.getTextSize() == aVar.e().getTextSize() && this.f4859a.getTextScaleX() == aVar.e().getTextScaleX() && this.f4859a.getTextSkewX() == aVar.e().getTextSkewX()) {
                if ((i8 < 21 || (this.f4859a.getLetterSpacing() == aVar.e().getLetterSpacing() && TextUtils.equals(this.f4859a.getFontFeatureSettings(), aVar.e().getFontFeatureSettings()))) && this.f4859a.getFlags() == aVar.e().getFlags()) {
                    if (i8 >= 24) {
                        if (!this.f4859a.getTextLocales().equals(aVar.e().getTextLocales())) {
                            return false;
                        }
                    } else if (i8 >= 17 && !this.f4859a.getTextLocale().equals(aVar.e().getTextLocale())) {
                        return false;
                    }
                    return this.f4859a.getTypeface() == null ? aVar.e().getTypeface() == null : this.f4859a.getTypeface().equals(aVar.e().getTypeface());
                }
                return false;
            }
            return false;
        }

        public int b() {
            return this.f4861c;
        }

        public int c() {
            return this.f4862d;
        }

        public TextDirectionHeuristic d() {
            return this.f4860b;
        }

        public TextPaint e() {
            return this.f4859a;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof a) {
                a aVar = (a) obj;
                if (a(aVar)) {
                    return Build.VERSION.SDK_INT < 18 || this.f4860b == aVar.d();
                }
                return false;
            }
            return false;
        }

        public int hashCode() {
            int i8 = Build.VERSION.SDK_INT;
            if (i8 >= 24) {
                return androidx.core.util.c.b(Float.valueOf(this.f4859a.getTextSize()), Float.valueOf(this.f4859a.getTextScaleX()), Float.valueOf(this.f4859a.getTextSkewX()), Float.valueOf(this.f4859a.getLetterSpacing()), Integer.valueOf(this.f4859a.getFlags()), this.f4859a.getTextLocales(), this.f4859a.getTypeface(), Boolean.valueOf(this.f4859a.isElegantTextHeight()), this.f4860b, Integer.valueOf(this.f4861c), Integer.valueOf(this.f4862d));
            }
            if (i8 >= 21) {
                return androidx.core.util.c.b(Float.valueOf(this.f4859a.getTextSize()), Float.valueOf(this.f4859a.getTextScaleX()), Float.valueOf(this.f4859a.getTextSkewX()), Float.valueOf(this.f4859a.getLetterSpacing()), Integer.valueOf(this.f4859a.getFlags()), this.f4859a.getTextLocale(), this.f4859a.getTypeface(), Boolean.valueOf(this.f4859a.isElegantTextHeight()), this.f4860b, Integer.valueOf(this.f4861c), Integer.valueOf(this.f4862d));
            }
            if (i8 < 18 && i8 < 17) {
                return androidx.core.util.c.b(Float.valueOf(this.f4859a.getTextSize()), Float.valueOf(this.f4859a.getTextScaleX()), Float.valueOf(this.f4859a.getTextSkewX()), Integer.valueOf(this.f4859a.getFlags()), this.f4859a.getTypeface(), this.f4860b, Integer.valueOf(this.f4861c), Integer.valueOf(this.f4862d));
            }
            return androidx.core.util.c.b(Float.valueOf(this.f4859a.getTextSize()), Float.valueOf(this.f4859a.getTextScaleX()), Float.valueOf(this.f4859a.getTextSkewX()), Integer.valueOf(this.f4859a.getFlags()), this.f4859a.getTextLocale(), this.f4859a.getTypeface(), this.f4860b, Integer.valueOf(this.f4861c), Integer.valueOf(this.f4862d));
        }

        /* JADX WARN: Removed duplicated region for block: B:14:0x00df  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.lang.String toString() {
            /*
                Method dump skipped, instructions count: 325
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.text.c.a.toString():java.lang.String");
        }
    }

    public a a() {
        return this.f4857b;
    }

    public PrecomputedText b() {
        Spannable spannable = this.f4856a;
        if (spannable instanceof PrecomputedText) {
            return (PrecomputedText) spannable;
        }
        return null;
    }

    @Override // java.lang.CharSequence
    public char charAt(int i8) {
        return this.f4856a.charAt(i8);
    }

    @Override // android.text.Spanned
    public int getSpanEnd(Object obj) {
        return this.f4856a.getSpanEnd(obj);
    }

    @Override // android.text.Spanned
    public int getSpanFlags(Object obj) {
        return this.f4856a.getSpanFlags(obj);
    }

    @Override // android.text.Spanned
    public int getSpanStart(Object obj) {
        return this.f4856a.getSpanStart(obj);
    }

    @Override // android.text.Spanned
    public <T> T[] getSpans(int i8, int i9, Class<T> cls) {
        return Build.VERSION.SDK_INT >= 29 ? (T[]) this.f4858c.getSpans(i8, i9, cls) : (T[]) this.f4856a.getSpans(i8, i9, cls);
    }

    @Override // java.lang.CharSequence
    public int length() {
        return this.f4856a.length();
    }

    @Override // android.text.Spanned
    public int nextSpanTransition(int i8, int i9, Class cls) {
        return this.f4856a.nextSpanTransition(i8, i9, cls);
    }

    @Override // android.text.Spannable
    public void removeSpan(Object obj) {
        if (obj instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
        }
        if (Build.VERSION.SDK_INT >= 29) {
            this.f4858c.removeSpan(obj);
        } else {
            this.f4856a.removeSpan(obj);
        }
    }

    @Override // android.text.Spannable
    public void setSpan(Object obj, int i8, int i9, int i10) {
        if (obj instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
        }
        if (Build.VERSION.SDK_INT >= 29) {
            this.f4858c.setSpan(obj, i8, i9, i10);
        } else {
            this.f4856a.setSpan(obj, i8, i9, i10);
        }
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int i8, int i9) {
        return this.f4856a.subSequence(i8, i9);
    }

    @Override // java.lang.CharSequence
    public String toString() {
        return this.f4856a.toString();
    }
}
