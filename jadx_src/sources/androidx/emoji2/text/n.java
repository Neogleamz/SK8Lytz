package androidx.emoji2.text;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.Editable;
import android.text.SpanWatcher;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class n extends SpannableStringBuilder {

    /* renamed from: a  reason: collision with root package name */
    private final Class<?> f5300a;

    /* renamed from: b  reason: collision with root package name */
    private final List<a> f5301b;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements TextWatcher, SpanWatcher {

        /* renamed from: a  reason: collision with root package name */
        final Object f5302a;

        /* renamed from: b  reason: collision with root package name */
        private final AtomicInteger f5303b = new AtomicInteger(0);

        a(Object obj) {
            this.f5302a = obj;
        }

        private boolean b(Object obj) {
            return obj instanceof i;
        }

        final void a() {
            this.f5303b.incrementAndGet();
        }

        @Override // android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            ((TextWatcher) this.f5302a).afterTextChanged(editable);
        }

        @Override // android.text.TextWatcher
        public void beforeTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
            ((TextWatcher) this.f5302a).beforeTextChanged(charSequence, i8, i9, i10);
        }

        final void c() {
            this.f5303b.decrementAndGet();
        }

        @Override // android.text.SpanWatcher
        public void onSpanAdded(Spannable spannable, Object obj, int i8, int i9) {
            if (this.f5303b.get() <= 0 || !b(obj)) {
                ((SpanWatcher) this.f5302a).onSpanAdded(spannable, obj, i8, i9);
            }
        }

        @Override // android.text.SpanWatcher
        public void onSpanChanged(Spannable spannable, Object obj, int i8, int i9, int i10, int i11) {
            int i12;
            int i13;
            if (this.f5303b.get() <= 0 || !b(obj)) {
                if (Build.VERSION.SDK_INT < 28) {
                    int i14 = i8 > i9 ? 0 : i8;
                    if (i10 > i11) {
                        i12 = i14;
                        i13 = 0;
                    } else {
                        i13 = i10;
                        i12 = i14;
                    }
                } else {
                    i12 = i8;
                    i13 = i10;
                }
                ((SpanWatcher) this.f5302a).onSpanChanged(spannable, obj, i12, i9, i13, i11);
            }
        }

        @Override // android.text.SpanWatcher
        public void onSpanRemoved(Spannable spannable, Object obj, int i8, int i9) {
            if (this.f5303b.get() <= 0 || !b(obj)) {
                ((SpanWatcher) this.f5302a).onSpanRemoved(spannable, obj, i8, i9);
            }
        }

        @Override // android.text.TextWatcher
        public void onTextChanged(CharSequence charSequence, int i8, int i9, int i10) {
            ((TextWatcher) this.f5302a).onTextChanged(charSequence, i8, i9, i10);
        }
    }

    n(Class<?> cls, CharSequence charSequence) {
        super(charSequence);
        this.f5301b = new ArrayList();
        androidx.core.util.h.i(cls, "watcherClass cannot be null");
        this.f5300a = cls;
    }

    n(Class<?> cls, CharSequence charSequence, int i8, int i9) {
        super(charSequence, i8, i9);
        this.f5301b = new ArrayList();
        androidx.core.util.h.i(cls, "watcherClass cannot be null");
        this.f5300a = cls;
    }

    private void b() {
        for (int i8 = 0; i8 < this.f5301b.size(); i8++) {
            this.f5301b.get(i8).a();
        }
    }

    public static n c(Class<?> cls, CharSequence charSequence) {
        return new n(cls, charSequence);
    }

    private void e() {
        for (int i8 = 0; i8 < this.f5301b.size(); i8++) {
            this.f5301b.get(i8).onTextChanged(this, 0, length(), length());
        }
    }

    private a f(Object obj) {
        for (int i8 = 0; i8 < this.f5301b.size(); i8++) {
            a aVar = this.f5301b.get(i8);
            if (aVar.f5302a == obj) {
                return aVar;
            }
        }
        return null;
    }

    private boolean g(Class<?> cls) {
        return this.f5300a == cls;
    }

    private boolean h(Object obj) {
        return obj != null && g(obj.getClass());
    }

    private void i() {
        for (int i8 = 0; i8 < this.f5301b.size(); i8++) {
            this.f5301b.get(i8).c();
        }
    }

    public void a() {
        b();
    }

    @Override // android.text.SpannableStringBuilder, android.text.Editable, java.lang.Appendable
    public SpannableStringBuilder append(char c9) {
        super.append(c9);
        return this;
    }

    @Override // android.text.SpannableStringBuilder, android.text.Editable, java.lang.Appendable
    public SpannableStringBuilder append(@SuppressLint({"UnknownNullness"}) CharSequence charSequence) {
        super.append(charSequence);
        return this;
    }

    @Override // android.text.SpannableStringBuilder, android.text.Editable, java.lang.Appendable
    public SpannableStringBuilder append(@SuppressLint({"UnknownNullness"}) CharSequence charSequence, int i8, int i9) {
        super.append(charSequence, i8, i9);
        return this;
    }

    @Override // android.text.SpannableStringBuilder
    @SuppressLint({"UnknownNullness"})
    public SpannableStringBuilder append(CharSequence charSequence, Object obj, int i8) {
        super.append(charSequence, obj, i8);
        return this;
    }

    public void d() {
        i();
        e();
    }

    @Override // android.text.SpannableStringBuilder, android.text.Editable
    @SuppressLint({"UnknownNullness"})
    public SpannableStringBuilder delete(int i8, int i9) {
        super.delete(i8, i9);
        return this;
    }

    @Override // android.text.SpannableStringBuilder, android.text.Spanned
    public int getSpanEnd(Object obj) {
        a f5;
        if (h(obj) && (f5 = f(obj)) != null) {
            obj = f5;
        }
        return super.getSpanEnd(obj);
    }

    @Override // android.text.SpannableStringBuilder, android.text.Spanned
    public int getSpanFlags(Object obj) {
        a f5;
        if (h(obj) && (f5 = f(obj)) != null) {
            obj = f5;
        }
        return super.getSpanFlags(obj);
    }

    @Override // android.text.SpannableStringBuilder, android.text.Spanned
    public int getSpanStart(Object obj) {
        a f5;
        if (h(obj) && (f5 = f(obj)) != null) {
            obj = f5;
        }
        return super.getSpanStart(obj);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // android.text.SpannableStringBuilder, android.text.Spanned
    @SuppressLint({"UnknownNullness"})
    public <T> T[] getSpans(int i8, int i9, Class<T> cls) {
        if (g(cls)) {
            a[] aVarArr = (a[]) super.getSpans(i8, i9, a.class);
            T[] tArr = (T[]) ((Object[]) Array.newInstance((Class<?>) cls, aVarArr.length));
            for (int i10 = 0; i10 < aVarArr.length; i10++) {
                tArr[i10] = aVarArr[i10].f5302a;
            }
            return tArr;
        }
        return (T[]) super.getSpans(i8, i9, cls);
    }

    @Override // android.text.SpannableStringBuilder, android.text.Editable
    @SuppressLint({"UnknownNullness"})
    public SpannableStringBuilder insert(int i8, CharSequence charSequence) {
        super.insert(i8, charSequence);
        return this;
    }

    @Override // android.text.SpannableStringBuilder, android.text.Editable
    @SuppressLint({"UnknownNullness"})
    public SpannableStringBuilder insert(int i8, CharSequence charSequence, int i9, int i10) {
        super.insert(i8, charSequence, i9, i10);
        return this;
    }

    @Override // android.text.SpannableStringBuilder, android.text.Spanned
    public int nextSpanTransition(int i8, int i9, Class cls) {
        return super.nextSpanTransition(i8, i9, (cls == null || g(cls)) ? a.class : a.class);
    }

    @Override // android.text.SpannableStringBuilder, android.text.Spannable
    public void removeSpan(Object obj) {
        a aVar;
        if (h(obj)) {
            aVar = f(obj);
            if (aVar != null) {
                obj = aVar;
            }
        } else {
            aVar = null;
        }
        super.removeSpan(obj);
        if (aVar != null) {
            this.f5301b.remove(aVar);
        }
    }

    @Override // android.text.SpannableStringBuilder, android.text.Editable
    @SuppressLint({"UnknownNullness"})
    public SpannableStringBuilder replace(int i8, int i9, CharSequence charSequence) {
        b();
        super.replace(i8, i9, charSequence);
        i();
        return this;
    }

    @Override // android.text.SpannableStringBuilder, android.text.Editable
    @SuppressLint({"UnknownNullness"})
    public SpannableStringBuilder replace(int i8, int i9, CharSequence charSequence, int i10, int i11) {
        b();
        super.replace(i8, i9, charSequence, i10, i11);
        i();
        return this;
    }

    @Override // android.text.SpannableStringBuilder, android.text.Spannable
    public void setSpan(Object obj, int i8, int i9, int i10) {
        if (h(obj)) {
            a aVar = new a(obj);
            this.f5301b.add(aVar);
            obj = aVar;
        }
        super.setSpan(obj, i8, i9, i10);
    }

    @Override // android.text.SpannableStringBuilder, java.lang.CharSequence
    @SuppressLint({"UnknownNullness"})
    public CharSequence subSequence(int i8, int i9) {
        return new n(this.f5300a, this, i8, i9);
    }
}
