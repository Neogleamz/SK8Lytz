package androidx.camera.core;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class CameraState {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum ErrorType {
        RECOVERABLE,
        CRITICAL
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum Type {
        PENDING_OPEN,
        OPENING,
        OPEN,
        CLOSING,
        CLOSED
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static abstract class a {
        public static a a(int i8) {
            return b(i8, null);
        }

        public static a b(int i8, Throwable th) {
            return new g(i8, th);
        }

        public abstract Throwable c();

        public abstract int d();
    }

    public static CameraState a(Type type) {
        return b(type, null);
    }

    public static CameraState b(Type type, a aVar) {
        return new f(type, aVar);
    }

    public abstract a c();

    public abstract Type d();
}
