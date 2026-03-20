package j2;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b implements i2.b {
    @Override // i2.b
    public Class<?> a(String str) {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                return contextClassLoader.loadClass(str);
            }
        } catch (ClassNotFoundException | SecurityException unused) {
        }
        try {
            ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
            if (systemClassLoader != null) {
                return systemClassLoader.loadClass(str);
            }
        } catch (ClassNotFoundException | Error | IllegalStateException | SecurityException unused2) {
        }
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException | LinkageError unused3) {
            return null;
        }
    }
}
