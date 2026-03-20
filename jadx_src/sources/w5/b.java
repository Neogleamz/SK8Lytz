package w5;

import android.text.TextUtils;
import com.google.common.collect.ImmutableSet;
import java.util.regex.Pattern;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b {

    /* renamed from: d  reason: collision with root package name */
    private static final Pattern f23599d = Pattern.compile("\\s+");

    /* renamed from: e  reason: collision with root package name */
    private static final ImmutableSet<String> f23600e = ImmutableSet.K("auto", "none");

    /* renamed from: f  reason: collision with root package name */
    private static final ImmutableSet<String> f23601f = ImmutableSet.L("dot", "sesame", "circle");

    /* renamed from: g  reason: collision with root package name */
    private static final ImmutableSet<String> f23602g = ImmutableSet.K("filled", "open");

    /* renamed from: h  reason: collision with root package name */
    private static final ImmutableSet<String> f23603h = ImmutableSet.L("after", "before", "outside");

    /* renamed from: a  reason: collision with root package name */
    public final int f23604a;

    /* renamed from: b  reason: collision with root package name */
    public final int f23605b;

    /* renamed from: c  reason: collision with root package name */
    public final int f23606c;

    private b(int i8, int i9, int i10) {
        this.f23604a = i8;
        this.f23605b = i9;
        this.f23606c = i10;
    }

    public static b a(String str) {
        if (str == null) {
            return null;
        }
        String e8 = com.google.common.base.c.e(str.trim());
        if (e8.isEmpty()) {
            return null;
        }
        return b(ImmutableSet.E(TextUtils.split(e8, f23599d)));
    }

    /* JADX WARN: Code restructure failed: missing block: B:35:0x0081, code lost:
        if (r9.equals("auto") != false) goto L22;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static w5.b b(com.google.common.collect.ImmutableSet<java.lang.String> r9) {
        /*
            Method dump skipped, instructions count: 288
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: w5.b.b(com.google.common.collect.ImmutableSet):w5.b");
    }
}
