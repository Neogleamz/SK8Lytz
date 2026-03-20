package androidx.viewpager.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a {

    /* renamed from: a  reason: collision with root package name */
    private final DataSetObservable f7834a = new DataSetObservable();

    /* renamed from: b  reason: collision with root package name */
    private DataSetObserver f7835b;

    @Deprecated
    public void a(View view, int i8, Object obj) {
        throw new UnsupportedOperationException("Required method destroyItem was not overridden");
    }

    public void b(ViewGroup viewGroup, int i8, Object obj) {
        a(viewGroup, i8, obj);
    }

    @Deprecated
    public void c(View view) {
    }

    public void d(ViewGroup viewGroup) {
        c(viewGroup);
    }

    public abstract int e();

    public int f(Object obj) {
        return -1;
    }

    public CharSequence g(int i8) {
        return null;
    }

    public float h(int i8) {
        return 1.0f;
    }

    @Deprecated
    public Object i(View view, int i8) {
        throw new UnsupportedOperationException("Required method instantiateItem was not overridden");
    }

    public Object j(ViewGroup viewGroup, int i8) {
        return i(viewGroup, i8);
    }

    public abstract boolean k(View view, Object obj);

    public void l() {
        synchronized (this) {
            DataSetObserver dataSetObserver = this.f7835b;
            if (dataSetObserver != null) {
                dataSetObserver.onChanged();
            }
        }
        this.f7834a.notifyChanged();
    }

    public void m(DataSetObserver dataSetObserver) {
        this.f7834a.registerObserver(dataSetObserver);
    }

    public void n(Parcelable parcelable, ClassLoader classLoader) {
    }

    public Parcelable o() {
        return null;
    }

    @Deprecated
    public void p(View view, int i8, Object obj) {
    }

    public void q(ViewGroup viewGroup, int i8, Object obj) {
        p(viewGroup, i8, obj);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void r(DataSetObserver dataSetObserver) {
        synchronized (this) {
            this.f7835b = dataSetObserver;
        }
    }

    @Deprecated
    public void s(View view) {
    }

    public void t(ViewGroup viewGroup) {
        s(viewGroup);
    }

    public void u(DataSetObserver dataSetObserver) {
        this.f7834a.unregisterObserver(dataSetObserver);
    }
}
