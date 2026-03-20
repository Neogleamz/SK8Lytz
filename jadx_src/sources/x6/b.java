package x6;

import android.os.IBinder;
import com.google.errorprone.annotations.ResultIgnorabilityUnspecified;
import java.lang.reflect.Field;
import n6.j;
import x6.a;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b<T> extends a.AbstractBinderC0227a {

    /* renamed from: a  reason: collision with root package name */
    private final Object f24158a;

    private b(Object obj) {
        this.f24158a = obj;
    }

    @ResultIgnorabilityUnspecified
    public static <T> T f(a aVar) {
        if (aVar instanceof b) {
            return (T) ((b) aVar).f24158a;
        }
        IBinder asBinder = aVar.asBinder();
        Field[] declaredFields = asBinder.getClass().getDeclaredFields();
        Field field = null;
        int i8 = 0;
        for (Field field2 : declaredFields) {
            if (!field2.isSynthetic()) {
                i8++;
                field = field2;
            }
        }
        if (i8 != 1) {
            throw new IllegalArgumentException("Unexpected number of IObjectWrapper declared fields: " + declaredFields.length);
        }
        j.l(field);
        if (field.isAccessible()) {
            throw new IllegalArgumentException("IObjectWrapper declared field not private!");
        }
        field.setAccessible(true);
        try {
            return (T) field.get(asBinder);
        } catch (IllegalAccessException e8) {
            throw new IllegalArgumentException("Could not access the field in remoteBinder.", e8);
        } catch (NullPointerException e9) {
            throw new IllegalArgumentException("Binder object is null.", e9);
        }
    }

    public static <T> a g(T t8) {
        return new b(t8);
    }
}
