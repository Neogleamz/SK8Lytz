package com.google.common.collect;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ImmutableListMultimap<K, V> extends ImmutableMultimap<K, V> implements i1<K, V> {
    private static final long serialVersionUID = 0;

    /* JADX WARN: Multi-variable type inference failed */
    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        int readInt = objectInputStream.readInt();
        if (readInt < 0) {
            StringBuilder sb = new StringBuilder(29);
            sb.append("Invalid key count ");
            sb.append(readInt);
            throw new InvalidObjectException(sb.toString());
        }
        ImmutableMap.b a9 = ImmutableMap.a();
        int i8 = 0;
        for (int i9 = 0; i9 < readInt; i9++) {
            Object readObject = objectInputStream.readObject();
            int readInt2 = objectInputStream.readInt();
            if (readInt2 <= 0) {
                StringBuilder sb2 = new StringBuilder(31);
                sb2.append("Invalid value count ");
                sb2.append(readInt2);
                throw new InvalidObjectException(sb2.toString());
            }
            ImmutableList.a u8 = ImmutableList.u();
            for (int i10 = 0; i10 < readInt2; i10++) {
                u8.a(objectInputStream.readObject());
            }
            a9.g(readObject, u8.k());
            i8 += readInt2;
        }
        try {
            ImmutableMultimap.e.f18990a.b(this, a9.d());
            ImmutableMultimap.e.f18991b.a(this, i8);
        } catch (IllegalArgumentException e8) {
            throw ((InvalidObjectException) new InvalidObjectException(e8.getMessage()).initCause(e8));
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        m2.j(this, objectOutputStream);
    }

    @Override // com.google.common.collect.ImmutableMultimap
    /* renamed from: v */
    public ImmutableList<V> q(K k8) {
        ImmutableList<V> immutableList = (ImmutableList) this.f18977e.get(k8);
        return immutableList == null ? ImmutableList.E() : immutableList;
    }

    @Override // com.google.common.collect.ImmutableMultimap
    @Deprecated
    /* renamed from: w */
    public final ImmutableList<V> s(Object obj) {
        throw new UnsupportedOperationException();
    }
}
