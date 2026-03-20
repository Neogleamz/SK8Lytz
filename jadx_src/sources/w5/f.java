package w5;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import b6.l0;
import b6.p;
import java.util.ArrayDeque;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {
    public static void a(Spannable spannable, int i8, int i9, g gVar, d dVar, Map<String, g> map, int i10) {
        d e8;
        Object bVar;
        g f5;
        Object absoluteSizeSpan;
        int i11;
        if (gVar.l() != -1) {
            spannable.setSpan(new StyleSpan(gVar.l()), i8, i9, 33);
        }
        if (gVar.s()) {
            spannable.setSpan(new StrikethroughSpan(), i8, i9, 33);
        }
        if (gVar.t()) {
            spannable.setSpan(new UnderlineSpan(), i8, i9, 33);
        }
        if (gVar.q()) {
            t5.c.a(spannable, new ForegroundColorSpan(gVar.c()), i8, i9, 33);
        }
        if (gVar.p()) {
            t5.c.a(spannable, new BackgroundColorSpan(gVar.b()), i8, i9, 33);
        }
        if (gVar.d() != null) {
            t5.c.a(spannable, new TypefaceSpan(gVar.d()), i8, i9, 33);
        }
        if (gVar.o() != null) {
            b bVar2 = (b) b6.a.e(gVar.o());
            int i12 = bVar2.f23604a;
            if (i12 == -1) {
                i12 = (i10 == 2 || i10 == 1) ? 3 : 1;
                i11 = 1;
            } else {
                i11 = bVar2.f23605b;
            }
            int i13 = bVar2.f23606c;
            if (i13 == -2) {
                i13 = 1;
            }
            t5.c.a(spannable, new t5.d(i12, i11, i13), i8, i9, 33);
        }
        int j8 = gVar.j();
        if (j8 == 2) {
            d d8 = d(dVar, map);
            if (d8 != null && (e8 = e(d8, map)) != null) {
                if (e8.g() != 1 || e8.f(0).f23624b == null) {
                    p.f("TtmlRenderUtil", "Skipping rubyText node without exactly one text child.");
                } else {
                    String str = (String) l0.j(e8.f(0).f23624b);
                    g f8 = f(e8.f23628f, e8.l(), map);
                    int i14 = f8 != null ? f8.i() : -1;
                    if (i14 == -1 && (f5 = f(d8.f23628f, d8.l(), map)) != null) {
                        i14 = f5.i();
                    }
                    bVar = new t5.b(str, i14);
                    spannable.setSpan(bVar, i8, i9, 33);
                }
            }
        } else if (j8 == 3 || j8 == 4) {
            bVar = new a();
            spannable.setSpan(bVar, i8, i9, 33);
        }
        if (gVar.n()) {
            t5.c.a(spannable, new t5.a(), i8, i9, 33);
        }
        int f9 = gVar.f();
        if (f9 == 1) {
            absoluteSizeSpan = new AbsoluteSizeSpan((int) gVar.e(), true);
        } else if (f9 == 2) {
            absoluteSizeSpan = new RelativeSizeSpan(gVar.e());
        } else if (f9 != 3) {
            return;
        } else {
            absoluteSizeSpan = new RelativeSizeSpan(gVar.e() / 100.0f);
        }
        t5.c.a(spannable, absoluteSizeSpan, i8, i9, 33);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String b(String str) {
        return str.replaceAll("\r\n", "\n").replaceAll(" *\n *", "\n").replaceAll("\n", " ").replaceAll("[ \t\\x0B\f\r]+", " ");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void c(SpannableStringBuilder spannableStringBuilder) {
        int length = spannableStringBuilder.length() - 1;
        while (length >= 0 && spannableStringBuilder.charAt(length) == ' ') {
            length--;
        }
        if (length < 0 || spannableStringBuilder.charAt(length) == '\n') {
            return;
        }
        spannableStringBuilder.append('\n');
    }

    private static d d(d dVar, Map<String, g> map) {
        while (dVar != null) {
            g f5 = f(dVar.f23628f, dVar.l(), map);
            if (f5 != null && f5.j() == 1) {
                return dVar;
            }
            dVar = dVar.f23632j;
        }
        return null;
    }

    private static d e(d dVar, Map<String, g> map) {
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.push(dVar);
        while (!arrayDeque.isEmpty()) {
            d dVar2 = (d) arrayDeque.pop();
            g f5 = f(dVar2.f23628f, dVar2.l(), map);
            if (f5 != null && f5.j() == 3) {
                return dVar2;
            }
            for (int g8 = dVar2.g() - 1; g8 >= 0; g8--) {
                arrayDeque.push(dVar2.f(g8));
            }
        }
        return null;
    }

    public static g f(g gVar, String[] strArr, Map<String, g> map) {
        int i8 = 0;
        if (gVar == null) {
            if (strArr == null) {
                return null;
            }
            if (strArr.length == 1) {
                return map.get(strArr[0]);
            }
            if (strArr.length > 1) {
                g gVar2 = new g();
                int length = strArr.length;
                while (i8 < length) {
                    gVar2.a(map.get(strArr[i8]));
                    i8++;
                }
                return gVar2;
            }
        } else if (strArr != null && strArr.length == 1) {
            return gVar.a(map.get(strArr[0]));
        } else {
            if (strArr != null && strArr.length > 1) {
                int length2 = strArr.length;
                while (i8 < length2) {
                    gVar.a(map.get(strArr[i8]));
                    i8++;
                }
            }
        }
        return gVar;
    }
}
