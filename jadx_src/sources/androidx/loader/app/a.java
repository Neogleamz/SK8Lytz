package androidx.loader.app;

import androidx.lifecycle.j;
import androidx.lifecycle.j0;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a {
    public static <T extends j & j0> a b(T t8) {
        return new b(t8, t8.getViewModelStore());
    }

    @Deprecated
    public abstract void a(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr);

    public abstract void c();
}
