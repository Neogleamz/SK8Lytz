package androidx.recyclerview.widget;

import android.view.View;
import com.example.seedpoint.R;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class a0 {

    /* renamed from: a  reason: collision with root package name */
    final b f6782a;

    /* renamed from: b  reason: collision with root package name */
    a f6783b = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a {

        /* renamed from: a  reason: collision with root package name */
        int f6784a = 0;

        /* renamed from: b  reason: collision with root package name */
        int f6785b;

        /* renamed from: c  reason: collision with root package name */
        int f6786c;

        /* renamed from: d  reason: collision with root package name */
        int f6787d;

        /* renamed from: e  reason: collision with root package name */
        int f6788e;

        a() {
        }

        void a(int i8) {
            this.f6784a = i8 | this.f6784a;
        }

        boolean b() {
            int i8 = this.f6784a;
            if ((i8 & 7) == 0 || (i8 & (c(this.f6787d, this.f6785b) << 0)) != 0) {
                int i9 = this.f6784a;
                if ((i9 & R.styleable.AppCompatTheme_toolbarNavigationButtonStyle) == 0 || (i9 & (c(this.f6787d, this.f6786c) << 4)) != 0) {
                    int i10 = this.f6784a;
                    if ((i10 & 1792) == 0 || (i10 & (c(this.f6788e, this.f6785b) << 8)) != 0) {
                        int i11 = this.f6784a;
                        return (i11 & 28672) == 0 || (i11 & (c(this.f6788e, this.f6786c) << 12)) != 0;
                    }
                    return false;
                }
                return false;
            }
            return false;
        }

        int c(int i8, int i9) {
            if (i8 > i9) {
                return 1;
            }
            return i8 == i9 ? 2 : 4;
        }

        void d() {
            this.f6784a = 0;
        }

        void e(int i8, int i9, int i10, int i11) {
            this.f6785b = i8;
            this.f6786c = i9;
            this.f6787d = i10;
            this.f6788e = i11;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface b {
        View a(int i8);

        int b(View view);

        int c();

        int d();

        int e(View view);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public a0(b bVar) {
        this.f6782a = bVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public View a(int i8, int i9, int i10, int i11) {
        int c9 = this.f6782a.c();
        int d8 = this.f6782a.d();
        int i12 = i9 > i8 ? 1 : -1;
        View view = null;
        while (i8 != i9) {
            View a9 = this.f6782a.a(i8);
            this.f6783b.e(c9, d8, this.f6782a.b(a9), this.f6782a.e(a9));
            if (i10 != 0) {
                this.f6783b.d();
                this.f6783b.a(i10);
                if (this.f6783b.b()) {
                    return a9;
                }
            }
            if (i11 != 0) {
                this.f6783b.d();
                this.f6783b.a(i11);
                if (this.f6783b.b()) {
                    view = a9;
                }
            }
            i8 += i12;
        }
        return view;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean b(View view, int i8) {
        this.f6783b.e(this.f6782a.c(), this.f6782a.d(), this.f6782a.b(view), this.f6782a.e(view));
        if (i8 != 0) {
            this.f6783b.d();
            this.f6783b.a(i8);
            return this.f6783b.b();
        }
        return false;
    }
}
