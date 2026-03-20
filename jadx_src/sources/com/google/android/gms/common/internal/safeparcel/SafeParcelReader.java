package com.google.android.gms.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class SafeParcelReader {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class ParseException extends RuntimeException {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public ParseException(java.lang.String r3, android.os.Parcel r4) {
            /*
                r2 = this;
                int r0 = r4.dataPosition()
                int r4 = r4.dataSize()
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                r1.append(r3)
                java.lang.String r3 = " Parcel: pos="
                r1.append(r3)
                r1.append(r0)
                java.lang.String r3 = " size="
                r1.append(r3)
                r1.append(r4)
                java.lang.String r3 = r1.toString()
                r2.<init>(r3)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.safeparcel.SafeParcelReader.ParseException.<init>(java.lang.String, android.os.Parcel):void");
        }
    }

    public static Float A(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        if (G == 0) {
            return null;
        }
        J(parcel, i8, G, 4);
        return Float.valueOf(parcel.readFloat());
    }

    public static int B(Parcel parcel) {
        return parcel.readInt();
    }

    public static IBinder C(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        IBinder readStrongBinder = parcel.readStrongBinder();
        parcel.setDataPosition(dataPosition + G);
        return readStrongBinder;
    }

    public static int D(Parcel parcel, int i8) {
        K(parcel, i8, 4);
        return parcel.readInt();
    }

    public static long E(Parcel parcel, int i8) {
        K(parcel, i8, 8);
        return parcel.readLong();
    }

    public static Long F(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        if (G == 0) {
            return null;
        }
        J(parcel, i8, G, 8);
        return Long.valueOf(parcel.readLong());
    }

    public static int G(Parcel parcel, int i8) {
        return (i8 & (-65536)) != -65536 ? (char) (i8 >> 16) : parcel.readInt();
    }

    public static void H(Parcel parcel, int i8) {
        parcel.setDataPosition(parcel.dataPosition() + G(parcel, i8));
    }

    public static int I(Parcel parcel) {
        int B = B(parcel);
        int G = G(parcel, B);
        int u8 = u(B);
        int dataPosition = parcel.dataPosition();
        if (u8 == 20293) {
            int i8 = G + dataPosition;
            if (i8 < dataPosition || i8 > parcel.dataSize()) {
                throw new ParseException("Size read is invalid start=" + dataPosition + " end=" + i8, parcel);
            }
            return i8;
        }
        throw new ParseException("Expected object header. Got 0x".concat(String.valueOf(Integer.toHexString(B))), parcel);
    }

    private static void J(Parcel parcel, int i8, int i9, int i10) {
        if (i9 == i10) {
            return;
        }
        String hexString = Integer.toHexString(i9);
        throw new ParseException("Expected size " + i10 + " got " + i9 + " (0x" + hexString + ")", parcel);
    }

    private static void K(Parcel parcel, int i8, int i9) {
        int G = G(parcel, i8);
        if (G == i9) {
            return;
        }
        String hexString = Integer.toHexString(G);
        throw new ParseException("Expected size " + i9 + " got " + G + " (0x" + hexString + ")", parcel);
    }

    public static BigDecimal a(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        byte[] createByteArray = parcel.createByteArray();
        int readInt = parcel.readInt();
        parcel.setDataPosition(dataPosition + G);
        return new BigDecimal(new BigInteger(createByteArray), readInt);
    }

    public static BigDecimal[] b(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        int readInt = parcel.readInt();
        BigDecimal[] bigDecimalArr = new BigDecimal[readInt];
        for (int i9 = 0; i9 < readInt; i9++) {
            byte[] createByteArray = parcel.createByteArray();
            bigDecimalArr[i9] = new BigDecimal(new BigInteger(createByteArray), parcel.readInt());
        }
        parcel.setDataPosition(dataPosition + G);
        return bigDecimalArr;
    }

    public static BigInteger c(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        byte[] createByteArray = parcel.createByteArray();
        parcel.setDataPosition(dataPosition + G);
        return new BigInteger(createByteArray);
    }

    public static BigInteger[] d(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        int readInt = parcel.readInt();
        BigInteger[] bigIntegerArr = new BigInteger[readInt];
        for (int i9 = 0; i9 < readInt; i9++) {
            bigIntegerArr[i9] = new BigInteger(parcel.createByteArray());
        }
        parcel.setDataPosition(dataPosition + G);
        return bigIntegerArr;
    }

    public static boolean[] e(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        boolean[] createBooleanArray = parcel.createBooleanArray();
        parcel.setDataPosition(dataPosition + G);
        return createBooleanArray;
    }

    public static Bundle f(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        Bundle readBundle = parcel.readBundle();
        parcel.setDataPosition(dataPosition + G);
        return readBundle;
    }

    public static byte[] g(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        byte[] createByteArray = parcel.createByteArray();
        parcel.setDataPosition(dataPosition + G);
        return createByteArray;
    }

    public static double[] h(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        double[] createDoubleArray = parcel.createDoubleArray();
        parcel.setDataPosition(dataPosition + G);
        return createDoubleArray;
    }

    public static float[] i(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        float[] createFloatArray = parcel.createFloatArray();
        parcel.setDataPosition(dataPosition + G);
        return createFloatArray;
    }

    public static int[] j(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        int[] createIntArray = parcel.createIntArray();
        parcel.setDataPosition(dataPosition + G);
        return createIntArray;
    }

    public static long[] k(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        long[] createLongArray = parcel.createLongArray();
        parcel.setDataPosition(dataPosition + G);
        return createLongArray;
    }

    public static Parcel l(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        Parcel obtain = Parcel.obtain();
        obtain.appendFrom(parcel, dataPosition, G);
        parcel.setDataPosition(dataPosition + G);
        return obtain;
    }

    public static Parcel[] m(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        int readInt = parcel.readInt();
        Parcel[] parcelArr = new Parcel[readInt];
        for (int i9 = 0; i9 < readInt; i9++) {
            int readInt2 = parcel.readInt();
            if (readInt2 != 0) {
                int dataPosition2 = parcel.dataPosition();
                Parcel obtain = Parcel.obtain();
                obtain.appendFrom(parcel, dataPosition2, readInt2);
                parcelArr[i9] = obtain;
                parcel.setDataPosition(dataPosition2 + readInt2);
            } else {
                parcelArr[i9] = null;
            }
        }
        parcel.setDataPosition(dataPosition + G);
        return parcelArr;
    }

    public static <T extends Parcelable> T n(Parcel parcel, int i8, Parcelable.Creator<T> creator) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        T createFromParcel = creator.createFromParcel(parcel);
        parcel.setDataPosition(dataPosition + G);
        return createFromParcel;
    }

    public static String o(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        String readString = parcel.readString();
        parcel.setDataPosition(dataPosition + G);
        return readString;
    }

    public static String[] p(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        String[] createStringArray = parcel.createStringArray();
        parcel.setDataPosition(dataPosition + G);
        return createStringArray;
    }

    public static ArrayList<String> q(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        ArrayList<String> createStringArrayList = parcel.createStringArrayList();
        parcel.setDataPosition(dataPosition + G);
        return createStringArrayList;
    }

    public static <T> T[] r(Parcel parcel, int i8, Parcelable.Creator<T> creator) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        T[] tArr = (T[]) parcel.createTypedArray(creator);
        parcel.setDataPosition(dataPosition + G);
        return tArr;
    }

    public static <T> ArrayList<T> s(Parcel parcel, int i8, Parcelable.Creator<T> creator) {
        int G = G(parcel, i8);
        int dataPosition = parcel.dataPosition();
        if (G == 0) {
            return null;
        }
        ArrayList<T> createTypedArrayList = parcel.createTypedArrayList(creator);
        parcel.setDataPosition(dataPosition + G);
        return createTypedArrayList;
    }

    public static void t(Parcel parcel, int i8) {
        if (parcel.dataPosition() == i8) {
            return;
        }
        throw new ParseException("Overread allowed size end=" + i8, parcel);
    }

    public static int u(int i8) {
        return (char) i8;
    }

    public static boolean v(Parcel parcel, int i8) {
        K(parcel, i8, 4);
        return parcel.readInt() != 0;
    }

    public static Boolean w(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        if (G == 0) {
            return null;
        }
        J(parcel, i8, G, 4);
        return Boolean.valueOf(parcel.readInt() != 0);
    }

    public static double x(Parcel parcel, int i8) {
        K(parcel, i8, 8);
        return parcel.readDouble();
    }

    public static Double y(Parcel parcel, int i8) {
        int G = G(parcel, i8);
        if (G == 0) {
            return null;
        }
        J(parcel, i8, G, 8);
        return Double.valueOf(parcel.readDouble());
    }

    public static float z(Parcel parcel, int i8) {
        K(parcel, i8, 4);
        return parcel.readFloat();
    }
}
