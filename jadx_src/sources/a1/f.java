package a1;

import android.os.Build;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.SparseArray;
import android.widget.TextView;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f {

    /* renamed from: a  reason: collision with root package name */
    private final b f54a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a extends b {

        /* renamed from: a  reason: collision with root package name */
        private final TextView f55a;

        /* renamed from: b  reason: collision with root package name */
        private final d f56b;

        /* renamed from: c  reason: collision with root package name */
        private boolean f57c = true;

        a(TextView textView) {
            this.f55a = textView;
            this.f56b = new d(textView);
        }

        private InputFilter[] f(InputFilter[] inputFilterArr) {
            int length = inputFilterArr.length;
            for (InputFilter inputFilter : inputFilterArr) {
                if (inputFilter == this.f56b) {
                    return inputFilterArr;
                }
            }
            InputFilter[] inputFilterArr2 = new InputFilter[inputFilterArr.length + 1];
            System.arraycopy(inputFilterArr, 0, inputFilterArr2, 0, length);
            inputFilterArr2[length] = this.f56b;
            return inputFilterArr2;
        }

        private SparseArray<InputFilter> g(InputFilter[] inputFilterArr) {
            SparseArray<InputFilter> sparseArray = new SparseArray<>(1);
            for (int i8 = 0; i8 < inputFilterArr.length; i8++) {
                if (inputFilterArr[i8] instanceof d) {
                    sparseArray.put(i8, inputFilterArr[i8]);
                }
            }
            return sparseArray;
        }

        private InputFilter[] h(InputFilter[] inputFilterArr) {
            SparseArray<InputFilter> g8 = g(inputFilterArr);
            if (g8.size() == 0) {
                return inputFilterArr;
            }
            int length = inputFilterArr.length;
            InputFilter[] inputFilterArr2 = new InputFilter[inputFilterArr.length - g8.size()];
            int i8 = 0;
            for (int i9 = 0; i9 < length; i9++) {
                if (g8.indexOfKey(i9) < 0) {
                    inputFilterArr2[i8] = inputFilterArr[i9];
                    i8++;
                }
            }
            return inputFilterArr2;
        }

        private TransformationMethod j(TransformationMethod transformationMethod) {
            return transformationMethod instanceof h ? ((h) transformationMethod).a() : transformationMethod;
        }

        private void k() {
            this.f55a.setFilters(a(this.f55a.getFilters()));
        }

        private TransformationMethod m(TransformationMethod transformationMethod) {
            return ((transformationMethod instanceof h) || (transformationMethod instanceof PasswordTransformationMethod)) ? transformationMethod : new h(transformationMethod);
        }

        @Override // a1.f.b
        InputFilter[] a(InputFilter[] inputFilterArr) {
            return !this.f57c ? h(inputFilterArr) : f(inputFilterArr);
        }

        @Override // a1.f.b
        public boolean b() {
            return this.f57c;
        }

        @Override // a1.f.b
        void c(boolean z4) {
            if (z4) {
                l();
            }
        }

        @Override // a1.f.b
        void d(boolean z4) {
            this.f57c = z4;
            l();
            k();
        }

        @Override // a1.f.b
        TransformationMethod e(TransformationMethod transformationMethod) {
            return this.f57c ? m(transformationMethod) : j(transformationMethod);
        }

        void i(boolean z4) {
            this.f57c = z4;
        }

        void l() {
            this.f55a.setTransformationMethod(e(this.f55a.getTransformationMethod()));
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b {
        b() {
        }

        InputFilter[] a(InputFilter[] inputFilterArr) {
            return inputFilterArr;
        }

        public boolean b() {
            return false;
        }

        void c(boolean z4) {
        }

        void d(boolean z4) {
        }

        TransformationMethod e(TransformationMethod transformationMethod) {
            return transformationMethod;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c extends b {

        /* renamed from: a  reason: collision with root package name */
        private final a f58a;

        c(TextView textView) {
            this.f58a = new a(textView);
        }

        private boolean f() {
            return !androidx.emoji2.text.e.h();
        }

        @Override // a1.f.b
        InputFilter[] a(InputFilter[] inputFilterArr) {
            return f() ? inputFilterArr : this.f58a.a(inputFilterArr);
        }

        @Override // a1.f.b
        public boolean b() {
            return this.f58a.b();
        }

        @Override // a1.f.b
        void c(boolean z4) {
            if (f()) {
                return;
            }
            this.f58a.c(z4);
        }

        @Override // a1.f.b
        void d(boolean z4) {
            if (f()) {
                this.f58a.i(z4);
            } else {
                this.f58a.d(z4);
            }
        }

        @Override // a1.f.b
        TransformationMethod e(TransformationMethod transformationMethod) {
            return f() ? transformationMethod : this.f58a.e(transformationMethod);
        }
    }

    public f(TextView textView, boolean z4) {
        androidx.core.util.h.i(textView, "textView cannot be null");
        if (Build.VERSION.SDK_INT < 19) {
            this.f54a = new b();
        } else {
            this.f54a = !z4 ? new c(textView) : new a(textView);
        }
    }

    public InputFilter[] a(InputFilter[] inputFilterArr) {
        return this.f54a.a(inputFilterArr);
    }

    public boolean b() {
        return this.f54a.b();
    }

    public void c(boolean z4) {
        this.f54a.c(z4);
    }

    public void d(boolean z4) {
        this.f54a.d(z4);
    }

    public TransformationMethod e(TransformationMethod transformationMethod) {
        return this.f54a.e(transformationMethod);
    }
}
