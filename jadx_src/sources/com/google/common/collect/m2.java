package com.google.common.collect;

import com.google.common.collect.r1;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class m2 {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class b<T> {

        /* renamed from: a  reason: collision with root package name */
        private final Field f19411a;

        private b(Field field) {
            this.f19411a = field;
            field.setAccessible(true);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void a(T t8, int i8) {
            try {
                this.f19411a.set(t8, Integer.valueOf(i8));
            } catch (IllegalAccessException e8) {
                throw new AssertionError(e8);
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public void b(T t8, Object obj) {
            try {
                this.f19411a.set(t8, obj);
            } catch (IllegalAccessException e8) {
                throw new AssertionError(e8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> b<T> a(Class<T> cls, String str) {
        try {
            return new b<>(cls.getDeclaredField(str));
        } catch (NoSuchFieldException e8) {
            throw new AssertionError(e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> void b(Map<K, V> map, ObjectInputStream objectInputStream) {
        c(map, objectInputStream, objectInputStream.readInt());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static <K, V> void c(Map<K, V> map, ObjectInputStream objectInputStream, int i8) {
        for (int i9 = 0; i9 < i8; i9++) {
            map.put(objectInputStream.readObject(), objectInputStream.readObject());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> void d(n1<K, V> n1Var, ObjectInputStream objectInputStream) {
        e(n1Var, objectInputStream, objectInputStream.readInt());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static <K, V> void e(n1<K, V> n1Var, ObjectInputStream objectInputStream, int i8) {
        for (int i9 = 0; i9 < i8; i9++) {
            Collection collection = n1Var.get(objectInputStream.readObject());
            int readInt = objectInputStream.readInt();
            for (int i10 = 0; i10 < readInt; i10++) {
                collection.add(objectInputStream.readObject());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> void f(r1<E> r1Var, ObjectInputStream objectInputStream) {
        g(r1Var, objectInputStream, objectInputStream.readInt());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    public static <E> void g(r1<E> r1Var, ObjectInputStream objectInputStream, int i8) {
        for (int i9 = 0; i9 < i8; i9++) {
            r1Var.J(objectInputStream.readObject(), objectInputStream.readInt());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int h(ObjectInputStream objectInputStream) {
        return objectInputStream.readInt();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> void i(Map<K, V> map, ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeInt(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeObject(entry.getValue());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <K, V> void j(n1<K, V> n1Var, ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeInt(n1Var.b().size());
        for (Map.Entry<K, Collection<V>> entry : n1Var.b().entrySet()) {
            objectOutputStream.writeObject(entry.getKey());
            objectOutputStream.writeInt(entry.getValue().size());
            for (V v8 : entry.getValue()) {
                objectOutputStream.writeObject(v8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <E> void k(r1<E> r1Var, ObjectOutputStream objectOutputStream) {
        objectOutputStream.writeInt(r1Var.entrySet().size());
        for (r1.a<E> aVar : r1Var.entrySet()) {
            objectOutputStream.writeObject(aVar.a());
            objectOutputStream.writeInt(aVar.getCount());
        }
    }
}
