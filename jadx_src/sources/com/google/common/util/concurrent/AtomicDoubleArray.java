package com.google.common.util.concurrent;

import com.google.common.primitives.ImmutableLongArray;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLongArray;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AtomicDoubleArray implements Serializable {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    private transient AtomicLongArray f19656a;

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        ImmutableLongArray.b b9 = ImmutableLongArray.b();
        for (int i8 = 0; i8 < readInt; i8++) {
            b9.a(Double.doubleToRawLongBits(objectInputStream.readDouble()));
        }
        this.f19656a = new AtomicLongArray(b9.b().g());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        int b9 = b();
        objectOutputStream.writeInt(b9);
        for (int i8 = 0; i8 < b9; i8++) {
            objectOutputStream.writeDouble(a(i8));
        }
    }

    public final double a(int i8) {
        return Double.longBitsToDouble(this.f19656a.get(i8));
    }

    public final int b() {
        return this.f19656a.length();
    }

    public String toString() {
        int b9 = b() - 1;
        if (b9 == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder((b9 + 1) * 19);
        sb.append('[');
        int i8 = 0;
        while (true) {
            sb.append(Double.longBitsToDouble(this.f19656a.get(i8)));
            if (i8 == b9) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(',');
            sb.append(' ');
            i8++;
        }
    }
}
