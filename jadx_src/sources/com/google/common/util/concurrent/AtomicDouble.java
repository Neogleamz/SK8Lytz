package com.google.common.util.concurrent;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.atomic.AtomicLong;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AtomicDouble extends Number {
    private static final long serialVersionUID = 0;

    /* renamed from: a  reason: collision with root package name */
    private transient AtomicLong f19655a;

    public AtomicDouble() {
        this(0.0d);
    }

    public AtomicDouble(double d8) {
        this.f19655a = new AtomicLong(Double.doubleToRawLongBits(d8));
    }

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.f19655a = new AtomicLong();
        b(objectInputStream.readDouble());
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeDouble(a());
    }

    public final double a() {
        return Double.longBitsToDouble(this.f19655a.get());
    }

    public final void b(double d8) {
        this.f19655a.set(Double.doubleToRawLongBits(d8));
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return a();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) a();
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) a();
    }

    @Override // java.lang.Number
    public long longValue() {
        return (long) a();
    }

    public String toString() {
        return Double.toString(a());
    }
}
