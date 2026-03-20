package w3;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
abstract class j {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static Executor a() {
        return new m(Executors.newSingleThreadExecutor());
    }
}
