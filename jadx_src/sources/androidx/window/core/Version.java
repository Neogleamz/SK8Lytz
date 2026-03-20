package androidx.window.core;

import cj.j;
import cj.k;
import com.daimajia.numberprogressbar.BuildConfig;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
import mj.a;
import vj.e;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class Version implements Comparable<Version> {
    private static final Version CURRENT;
    public static final Companion Companion = new Companion(null);
    private static final Version UNKNOWN = new Version(0, 0, 0, BuildConfig.FLAVOR);
    private static final Version VERSION_0_1 = new Version(0, 1, 0, BuildConfig.FLAVOR);
    private static final Version VERSION_1_0;
    private static final String VERSION_PATTERN_STRING = "(\\d+)(?:\\.(\\d+))(?:\\.(\\d+))(?:-(.+))?";
    private final j bigInteger$delegate;
    private final String description;
    private final int major;
    private final int minor;
    private final int patch;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(i iVar) {
            this();
        }

        public final Version getCURRENT() {
            return Version.CURRENT;
        }

        public final Version getUNKNOWN() {
            return Version.UNKNOWN;
        }

        public final Version getVERSION_0_1() {
            return Version.VERSION_0_1;
        }

        public final Version getVERSION_1_0() {
            return Version.VERSION_1_0;
        }

        public final Version parse(String str) {
            if (str == null || e.n(str)) {
                return null;
            }
            Matcher matcher = Pattern.compile(Version.VERSION_PATTERN_STRING).matcher(str);
            if (matcher.matches()) {
                String group = matcher.group(1);
                Integer valueOf = group == null ? null : Integer.valueOf(Integer.parseInt(group));
                if (valueOf == null) {
                    return null;
                }
                int intValue = valueOf.intValue();
                String group2 = matcher.group(2);
                Integer valueOf2 = group2 == null ? null : Integer.valueOf(Integer.parseInt(group2));
                if (valueOf2 == null) {
                    return null;
                }
                int intValue2 = valueOf2.intValue();
                String group3 = matcher.group(3);
                Integer valueOf3 = group3 == null ? null : Integer.valueOf(Integer.parseInt(group3));
                if (valueOf3 == null) {
                    return null;
                }
                int intValue3 = valueOf3.intValue();
                String group4 = matcher.group(4) != null ? matcher.group(4) : BuildConfig.FLAVOR;
                p.d(group4, "description");
                return new Version(intValue, intValue2, intValue3, group4, null);
            }
            return null;
        }
    }

    static {
        Version version = new Version(1, 0, 0, BuildConfig.FLAVOR);
        VERSION_1_0 = version;
        CURRENT = version;
    }

    private Version(int i8, int i9, int i10, String str) {
        this.major = i8;
        this.minor = i9;
        this.patch = i10;
        this.description = str;
        this.bigInteger$delegate = k.b(new a<BigInteger>() { // from class: androidx.window.core.Version$bigInteger$2
            /* JADX INFO: Access modifiers changed from: package-private */
            {
                super(0);
            }

            public final BigInteger invoke() {
                return BigInteger.valueOf(Version.this.getMajor()).shiftLeft(32).or(BigInteger.valueOf(Version.this.getMinor())).shiftLeft(32).or(BigInteger.valueOf(Version.this.getPatch()));
            }
        });
    }

    public /* synthetic */ Version(int i8, int i9, int i10, String str, i iVar) {
        this(i8, i9, i10, str);
    }

    private final BigInteger getBigInteger() {
        Object value = this.bigInteger$delegate.getValue();
        p.d(value, "<get-bigInteger>(...)");
        return (BigInteger) value;
    }

    public static final Version parse(String str) {
        return Companion.parse(str);
    }

    @Override // java.lang.Comparable
    public int compareTo(Version version) {
        p.e(version, "other");
        return getBigInteger().compareTo(version.getBigInteger());
    }

    public boolean equals(Object obj) {
        if (obj instanceof Version) {
            Version version = (Version) obj;
            return this.major == version.major && this.minor == version.minor && this.patch == version.patch;
        }
        return false;
    }

    public final String getDescription() {
        return this.description;
    }

    public final int getMajor() {
        return this.major;
    }

    public final int getMinor() {
        return this.minor;
    }

    public final int getPatch() {
        return this.patch;
    }

    public int hashCode() {
        return ((((527 + this.major) * 31) + this.minor) * 31) + this.patch;
    }

    public String toString() {
        String m8 = e.n(this.description) ^ true ? p.m("-", this.description) : BuildConfig.FLAVOR;
        return this.major + '.' + this.minor + '.' + this.patch + m8;
    }
}
