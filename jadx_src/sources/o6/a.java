package o6;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {
    public static int a(Parcel parcel) {
        return w(parcel, 20293);
    }

    public static void b(Parcel parcel, int i8) {
        x(parcel, i8);
    }

    public static void c(Parcel parcel, int i8, boolean z4) {
        y(parcel, i8, 4);
        parcel.writeInt(z4 ? 1 : 0);
    }

    public static void d(Parcel parcel, int i8, Boolean bool, boolean z4) {
        if (bool != null) {
            y(parcel, i8, 4);
            parcel.writeInt(bool.booleanValue() ? 1 : 0);
        } else if (z4) {
            y(parcel, i8, 0);
        }
    }

    public static void e(Parcel parcel, int i8, Bundle bundle, boolean z4) {
        if (bundle == null) {
            if (z4) {
                y(parcel, i8, 0);
                return;
            }
            return;
        }
        int w8 = w(parcel, i8);
        parcel.writeBundle(bundle);
        x(parcel, w8);
    }

    public static void f(Parcel parcel, int i8, byte[] bArr, boolean z4) {
        if (bArr == null) {
            if (z4) {
                y(parcel, i8, 0);
                return;
            }
            return;
        }
        int w8 = w(parcel, i8);
        parcel.writeByteArray(bArr);
        x(parcel, w8);
    }

    public static void g(Parcel parcel, int i8, double d8) {
        y(parcel, i8, 8);
        parcel.writeDouble(d8);
    }

    public static void h(Parcel parcel, int i8, Double d8, boolean z4) {
        if (d8 != null) {
            y(parcel, i8, 8);
            parcel.writeDouble(d8.doubleValue());
        } else if (z4) {
            y(parcel, i8, 0);
        }
    }

    public static void i(Parcel parcel, int i8, float f5) {
        y(parcel, i8, 4);
        parcel.writeFloat(f5);
    }

    public static void j(Parcel parcel, int i8, Float f5, boolean z4) {
        if (f5 != null) {
            y(parcel, i8, 4);
            parcel.writeFloat(f5.floatValue());
        } else if (z4) {
            y(parcel, i8, 0);
        }
    }

    public static void k(Parcel parcel, int i8, IBinder iBinder, boolean z4) {
        if (iBinder == null) {
            if (z4) {
                y(parcel, i8, 0);
                return;
            }
            return;
        }
        int w8 = w(parcel, i8);
        parcel.writeStrongBinder(iBinder);
        x(parcel, w8);
    }

    public static void l(Parcel parcel, int i8, int i9) {
        y(parcel, i8, 4);
        parcel.writeInt(i9);
    }

    public static void m(Parcel parcel, int i8, int[] iArr, boolean z4) {
        if (iArr == null) {
            if (z4) {
                y(parcel, i8, 0);
                return;
            }
            return;
        }
        int w8 = w(parcel, i8);
        parcel.writeIntArray(iArr);
        x(parcel, w8);
    }

    public static void n(Parcel parcel, int i8, long j8) {
        y(parcel, i8, 8);
        parcel.writeLong(j8);
    }

    public static void o(Parcel parcel, int i8, Long l8, boolean z4) {
        if (l8 != null) {
            y(parcel, i8, 8);
            parcel.writeLong(l8.longValue());
        } else if (z4) {
            y(parcel, i8, 0);
        }
    }

    public static void p(Parcel parcel, int i8, Parcel parcel2, boolean z4) {
        if (parcel2 == null) {
            if (z4) {
                y(parcel, i8, 0);
                return;
            }
            return;
        }
        int w8 = w(parcel, i8);
        parcel.appendFrom(parcel2, 0, parcel2.dataSize());
        x(parcel, w8);
    }

    public static void q(Parcel parcel, int i8, Parcelable parcelable, int i9, boolean z4) {
        if (parcelable == null) {
            if (z4) {
                y(parcel, i8, 0);
                return;
            }
            return;
        }
        int w8 = w(parcel, i8);
        parcelable.writeToParcel(parcel, i9);
        x(parcel, w8);
    }

    public static void r(Parcel parcel, int i8, String str, boolean z4) {
        if (str == null) {
            if (z4) {
                y(parcel, i8, 0);
                return;
            }
            return;
        }
        int w8 = w(parcel, i8);
        parcel.writeString(str);
        x(parcel, w8);
    }

    public static void s(Parcel parcel, int i8, String[] strArr, boolean z4) {
        if (strArr == null) {
            if (z4) {
                y(parcel, i8, 0);
                return;
            }
            return;
        }
        int w8 = w(parcel, i8);
        parcel.writeStringArray(strArr);
        x(parcel, w8);
    }

    public static void t(Parcel parcel, int i8, List<String> list, boolean z4) {
        if (list == null) {
            if (z4) {
                y(parcel, i8, 0);
                return;
            }
            return;
        }
        int w8 = w(parcel, i8);
        parcel.writeStringList(list);
        x(parcel, w8);
    }

    public static <T extends Parcelable> void u(Parcel parcel, int i8, T[] tArr, int i9, boolean z4) {
        if (tArr == null) {
            if (z4) {
                y(parcel, i8, 0);
                return;
            }
            return;
        }
        int w8 = w(parcel, i8);
        parcel.writeInt(tArr.length);
        for (T t8 : tArr) {
            if (t8 == null) {
                parcel.writeInt(0);
            } else {
                z(parcel, t8, i9);
            }
        }
        x(parcel, w8);
    }

    public static <T extends Parcelable> void v(Parcel parcel, int i8, List<T> list, boolean z4) {
        if (list == null) {
            if (z4) {
                y(parcel, i8, 0);
                return;
            }
            return;
        }
        int w8 = w(parcel, i8);
        int size = list.size();
        parcel.writeInt(size);
        for (int i9 = 0; i9 < size; i9++) {
            T t8 = list.get(i9);
            if (t8 == null) {
                parcel.writeInt(0);
            } else {
                z(parcel, t8, 0);
            }
        }
        x(parcel, w8);
    }

    private static int w(Parcel parcel, int i8) {
        parcel.writeInt(i8 | (-65536));
        parcel.writeInt(0);
        return parcel.dataPosition();
    }

    private static void x(Parcel parcel, int i8) {
        int dataPosition = parcel.dataPosition();
        parcel.setDataPosition(i8 - 4);
        parcel.writeInt(dataPosition - i8);
        parcel.setDataPosition(dataPosition);
    }

    private static void y(Parcel parcel, int i8, int i9) {
        parcel.writeInt(i8 | (i9 << 16));
    }

    private static void z(Parcel parcel, Parcelable parcelable, int i8) {
        int dataPosition = parcel.dataPosition();
        parcel.writeInt(1);
        int dataPosition2 = parcel.dataPosition();
        parcelable.writeToParcel(parcel, i8);
        int dataPosition3 = parcel.dataPosition();
        parcel.setDataPosition(dataPosition);
        parcel.writeInt(dataPosition3 - dataPosition2);
        parcel.setDataPosition(dataPosition3);
    }
}
