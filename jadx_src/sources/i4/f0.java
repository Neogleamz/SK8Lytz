package i4;

import android.annotation.SuppressLint;
import com.google.android.exoplayer2.w0;
import com.google.android.libraries.barhopper.RecognitionOptions;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface f0 {
    @SuppressLint({"WrongConstant"})
    static int F(int i8) {
        return i8 & 7;
    }

    @SuppressLint({"WrongConstant"})
    static int j(int i8) {
        return i8 & 384;
    }

    @SuppressLint({"WrongConstant"})
    static int n(int i8, int i9, int i10, int i11, int i12) {
        return i8 | i9 | i10 | i11 | i12;
    }

    @SuppressLint({"WrongConstant"})
    static int o(int i8) {
        return i8 & 64;
    }

    @SuppressLint({"WrongConstant"})
    static int p(int i8) {
        return i8 & 32;
    }

    static int r(int i8, int i9, int i10) {
        return n(i8, i9, i10, 0, RecognitionOptions.ITF);
    }

    @SuppressLint({"WrongConstant"})
    static int t(int i8) {
        return i8 & 24;
    }

    static int u(int i8) {
        return r(i8, 0, 0);
    }

    int a(w0 w0Var);

    String getName();

    int h();

    int v();
}
