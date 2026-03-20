package androidx.emoji2.text;

import android.os.Build;
import android.text.TextPaint;
import androidx.emoji2.text.e;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d implements e.d {

    /* renamed from: b  reason: collision with root package name */
    private static final ThreadLocal<StringBuilder> f5220b = new ThreadLocal<>();

    /* renamed from: a  reason: collision with root package name */
    private final TextPaint f5221a;

    /* JADX INFO: Access modifiers changed from: package-private */
    public d() {
        TextPaint textPaint = new TextPaint();
        this.f5221a = textPaint;
        textPaint.setTextSize(10.0f);
    }

    private static StringBuilder b() {
        ThreadLocal<StringBuilder> threadLocal = f5220b;
        if (threadLocal.get() == null) {
            threadLocal.set(new StringBuilder());
        }
        return threadLocal.get();
    }

    @Override // androidx.emoji2.text.e.d
    public boolean a(CharSequence charSequence, int i8, int i9, int i10) {
        int i11 = Build.VERSION.SDK_INT;
        if (i11 >= 23 || i10 <= i11) {
            StringBuilder b9 = b();
            b9.setLength(0);
            while (i8 < i9) {
                b9.append(charSequence.charAt(i8));
                i8++;
            }
            return androidx.core.graphics.d.a(this.f5221a, b9.toString());
        }
        return false;
    }
}
