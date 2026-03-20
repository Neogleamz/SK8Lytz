package c3;

import android.util.Log;
import gi.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e<T> implements h<T> {

    /* renamed from: a  reason: collision with root package name */
    private static final String f8299a = "c3.e";

    public void onComplete() {
        Log.i(f8299a, "onComplete");
    }

    public void onError(Throwable th) {
        String str = f8299a;
        Log.i(str, "onError : " + th.getMessage());
    }

    public void onNext(T t8) {
        Log.i(f8299a, "onNext");
    }

    public void onSubscribe(hi.b bVar) {
        Log.i(f8299a, "onSubscribe");
    }
}
