package androidx.fragment.app;

import android.util.Log;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.Writer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class w extends Writer {

    /* renamed from: a  reason: collision with root package name */
    private final String f5765a;

    /* renamed from: b  reason: collision with root package name */
    private StringBuilder f5766b = new StringBuilder((int) RecognitionOptions.ITF);

    /* JADX INFO: Access modifiers changed from: package-private */
    public w(String str) {
        this.f5765a = str;
    }

    private void a() {
        if (this.f5766b.length() > 0) {
            Log.d(this.f5765a, this.f5766b.toString());
            StringBuilder sb = this.f5766b;
            sb.delete(0, sb.length());
        }
    }

    @Override // java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        a();
    }

    @Override // java.io.Writer, java.io.Flushable
    public void flush() {
        a();
    }

    @Override // java.io.Writer
    public void write(char[] cArr, int i8, int i9) {
        for (int i10 = 0; i10 < i9; i10++) {
            char c9 = cArr[i8 + i10];
            if (c9 == '\n') {
                a();
            } else {
                this.f5766b.append(c9);
            }
        }
    }
}
