package androidx.core.os;

import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Size;
import android.util.SizeF;
import java.io.Serializable;
import kotlin.Pair;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {
    public static final Bundle a(Pair<String, ? extends Object>... pairArr) {
        kotlin.jvm.internal.p.e(pairArr, "pairs");
        Bundle bundle = new Bundle(pairArr.length);
        for (Pair<String, ? extends Object> pair : pairArr) {
            String str = (String) pair.a();
            Object b9 = pair.b();
            if (b9 == null) {
                bundle.putString(str, null);
            } else if (b9 instanceof Boolean) {
                bundle.putBoolean(str, ((Boolean) b9).booleanValue());
            } else if (b9 instanceof Byte) {
                bundle.putByte(str, ((Number) b9).byteValue());
            } else if (b9 instanceof Character) {
                bundle.putChar(str, ((Character) b9).charValue());
            } else if (b9 instanceof Double) {
                bundle.putDouble(str, ((Number) b9).doubleValue());
            } else if (b9 instanceof Float) {
                bundle.putFloat(str, ((Number) b9).floatValue());
            } else if (b9 instanceof Integer) {
                bundle.putInt(str, ((Number) b9).intValue());
            } else if (b9 instanceof Long) {
                bundle.putLong(str, ((Number) b9).longValue());
            } else if (b9 instanceof Short) {
                bundle.putShort(str, ((Number) b9).shortValue());
            } else if (b9 instanceof Bundle) {
                bundle.putBundle(str, (Bundle) b9);
            } else if (b9 instanceof CharSequence) {
                bundle.putCharSequence(str, (CharSequence) b9);
            } else if (b9 instanceof Parcelable) {
                bundle.putParcelable(str, (Parcelable) b9);
            } else if (b9 instanceof boolean[]) {
                bundle.putBooleanArray(str, (boolean[]) b9);
            } else if (b9 instanceof byte[]) {
                bundle.putByteArray(str, (byte[]) b9);
            } else if (b9 instanceof char[]) {
                bundle.putCharArray(str, (char[]) b9);
            } else if (b9 instanceof double[]) {
                bundle.putDoubleArray(str, (double[]) b9);
            } else if (b9 instanceof float[]) {
                bundle.putFloatArray(str, (float[]) b9);
            } else if (b9 instanceof int[]) {
                bundle.putIntArray(str, (int[]) b9);
            } else if (b9 instanceof long[]) {
                bundle.putLongArray(str, (long[]) b9);
            } else if (b9 instanceof short[]) {
                bundle.putShortArray(str, (short[]) b9);
            } else if (b9 instanceof Object[]) {
                Class<?> componentType = b9.getClass().getComponentType();
                kotlin.jvm.internal.p.b(componentType);
                if (Parcelable.class.isAssignableFrom(componentType)) {
                    bundle.putParcelableArray(str, (Parcelable[]) b9);
                } else if (String.class.isAssignableFrom(componentType)) {
                    bundle.putStringArray(str, (String[]) b9);
                } else if (CharSequence.class.isAssignableFrom(componentType)) {
                    bundle.putCharSequenceArray(str, (CharSequence[]) b9);
                } else {
                    if (!Serializable.class.isAssignableFrom(componentType)) {
                        throw new IllegalArgumentException("Illegal value array type " + componentType.getCanonicalName() + " for key \"" + str + '\"');
                    }
                    bundle.putSerializable(str, (Serializable) b9);
                }
            } else {
                if (!(b9 instanceof Serializable)) {
                    int i8 = Build.VERSION.SDK_INT;
                    if (i8 >= 18 && (b9 instanceof IBinder)) {
                        b.a(bundle, str, (IBinder) b9);
                    } else if (i8 >= 21 && (b9 instanceof Size)) {
                        c.a(bundle, str, (Size) b9);
                    } else if (i8 < 21 || !(b9 instanceof SizeF)) {
                        throw new IllegalArgumentException("Illegal value type " + b9.getClass().getCanonicalName() + " for key \"" + str + '\"');
                    } else {
                        c.b(bundle, str, (SizeF) b9);
                    }
                }
                bundle.putSerializable(str, (Serializable) b9);
            }
        }
        return bundle;
    }
}
