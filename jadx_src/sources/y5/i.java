package y5;

import b6.l0;
import b6.z;
import com.google.android.exoplayer2.ParserException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i {

    /* renamed from: a  reason: collision with root package name */
    private static final Pattern f24473a = Pattern.compile("^NOTE([ \t].*)?$");

    public static Matcher a(z zVar) {
        String s8;
        while (true) {
            String s9 = zVar.s();
            if (s9 == null) {
                return null;
            }
            if (f24473a.matcher(s9).matches()) {
                do {
                    s8 = zVar.s();
                    if (s8 != null) {
                    }
                } while (!s8.isEmpty());
            } else {
                Matcher matcher = f.f24447a.matcher(s9);
                if (matcher.matches()) {
                    return matcher;
                }
            }
        }
    }

    public static boolean b(z zVar) {
        String s8 = zVar.s();
        return s8 != null && s8.startsWith("WEBVTT");
    }

    public static float c(String str) {
        if (str.endsWith("%")) {
            return Float.parseFloat(str.substring(0, str.length() - 1)) / 100.0f;
        }
        throw new NumberFormatException("Percentages must end with %");
    }

    public static long d(String str) {
        String[] S0 = l0.S0(str, "\\.");
        long j8 = 0;
        for (String str2 : l0.R0(S0[0], ":")) {
            j8 = (j8 * 60) + Long.parseLong(str2);
        }
        long j9 = j8 * 1000;
        if (S0.length == 2) {
            j9 += Long.parseLong(S0[1]);
        }
        return j9 * 1000;
    }

    public static void e(z zVar) {
        int f5 = zVar.f();
        if (b(zVar)) {
            return;
        }
        zVar.U(f5);
        throw ParserException.a("Expected WEBVTT. Got " + zVar.s(), null);
    }
}
