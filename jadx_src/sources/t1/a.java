package t1;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a implements e {

    /* renamed from: a  reason: collision with root package name */
    private final String f22834a;

    /* renamed from: b  reason: collision with root package name */
    private final Object[] f22835b;

    public a(String str) {
        this(str, null);
    }

    public a(String str, Object[] objArr) {
        this.f22834a = str;
        this.f22835b = objArr;
    }

    private static void c(d dVar, int i8, Object obj) {
        long j8;
        int byteValue;
        double doubleValue;
        if (obj == null) {
            dVar.o1(i8);
        } else if (obj instanceof byte[]) {
            dVar.o0(i8, (byte[]) obj);
        } else {
            if (obj instanceof Float) {
                doubleValue = ((Float) obj).floatValue();
            } else if (!(obj instanceof Double)) {
                if (obj instanceof Long) {
                    j8 = ((Long) obj).longValue();
                } else {
                    if (obj instanceof Integer) {
                        byteValue = ((Integer) obj).intValue();
                    } else if (obj instanceof Short) {
                        byteValue = ((Short) obj).shortValue();
                    } else if (obj instanceof Byte) {
                        byteValue = ((Byte) obj).byteValue();
                    } else if (obj instanceof String) {
                        dVar.I(i8, (String) obj);
                        return;
                    } else if (!(obj instanceof Boolean)) {
                        throw new IllegalArgumentException("Cannot bind " + obj + " at index " + i8 + " Supported types: null, byte[], float, double, long, int, short, byte, string");
                    } else {
                        j8 = ((Boolean) obj).booleanValue() ? 1L : 0L;
                    }
                    j8 = byteValue;
                }
                dVar.h0(i8, j8);
                return;
            } else {
                doubleValue = ((Double) obj).doubleValue();
            }
            dVar.Q(i8, doubleValue);
        }
    }

    public static void d(d dVar, Object[] objArr) {
        if (objArr == null) {
            return;
        }
        int length = objArr.length;
        int i8 = 0;
        while (i8 < length) {
            Object obj = objArr[i8];
            i8++;
            c(dVar, i8, obj);
        }
    }

    @Override // t1.e
    public void a(d dVar) {
        d(dVar, this.f22835b);
    }

    @Override // t1.e
    public String b() {
        return this.f22834a;
    }
}
