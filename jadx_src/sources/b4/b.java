package b4;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {
    public static <TInput, TResult, TException extends Throwable> TResult a(int i8, TInput tinput, a<TInput, TResult, TException> aVar, c<TInput, TResult> cVar) {
        TResult apply;
        if (i8 < 1) {
            return aVar.apply(tinput);
        }
        do {
            apply = aVar.apply(tinput);
            tinput = cVar.a(tinput, apply);
            if (tinput == null) {
                break;
            }
            i8--;
        } while (i8 >= 1);
        return apply;
    }
}
