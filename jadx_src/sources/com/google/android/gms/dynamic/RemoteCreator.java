package com.google.android.gms.dynamic;

import android.content.Context;
import android.os.IBinder;
import com.google.android.gms.common.d;
import n6.j;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class RemoteCreator<T> {

    /* renamed from: a  reason: collision with root package name */
    private final String f12013a;

    /* renamed from: b  reason: collision with root package name */
    private Object f12014b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class RemoteCreatorException extends Exception {
        public RemoteCreatorException(String str) {
            super(str);
        }

        public RemoteCreatorException(String str, Throwable th) {
            super(str, th);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public RemoteCreator(String str) {
        this.f12013a = str;
    }

    protected abstract T a(IBinder iBinder);

    /* JADX INFO: Access modifiers changed from: protected */
    public final T b(Context context) {
        if (this.f12014b == null) {
            j.l(context);
            Context c9 = d.c(context);
            if (c9 == null) {
                throw new RemoteCreatorException("Could not get remote context.");
            }
            try {
                this.f12014b = a((IBinder) c9.getClassLoader().loadClass(this.f12013a).newInstance());
            } catch (ClassNotFoundException e8) {
                throw new RemoteCreatorException("Could not load creator class.", e8);
            } catch (IllegalAccessException e9) {
                throw new RemoteCreatorException("Could not access creator.", e9);
            } catch (InstantiationException e10) {
                throw new RemoteCreatorException("Could not instantiate creator.", e10);
            }
        }
        return (T) this.f12014b;
    }
}
