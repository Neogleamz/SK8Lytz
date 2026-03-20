package v0;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import v0.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class a extends BaseAdapter implements Filterable, b.a {

    /* renamed from: a  reason: collision with root package name */
    protected boolean f23144a;

    /* renamed from: b  reason: collision with root package name */
    protected boolean f23145b;

    /* renamed from: c  reason: collision with root package name */
    protected Cursor f23146c;

    /* renamed from: d  reason: collision with root package name */
    protected Context f23147d;

    /* renamed from: e  reason: collision with root package name */
    protected int f23148e;

    /* renamed from: f  reason: collision with root package name */
    protected C0214a f23149f;

    /* renamed from: g  reason: collision with root package name */
    protected DataSetObserver f23150g;

    /* renamed from: h  reason: collision with root package name */
    protected v0.b f23151h;

    /* JADX INFO: Access modifiers changed from: private */
    /* renamed from: v0.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0214a extends ContentObserver {
        C0214a() {
            super(new Handler());
        }

        @Override // android.database.ContentObserver
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z4) {
            a.this.i();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b extends DataSetObserver {
        b() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            a aVar = a.this;
            aVar.f23144a = true;
            aVar.notifyDataSetChanged();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            a aVar = a.this;
            aVar.f23144a = false;
            aVar.notifyDataSetInvalidated();
        }
    }

    public a(Context context, Cursor cursor, boolean z4) {
        f(context, cursor, z4 ? 1 : 2);
    }

    public void a(Cursor cursor) {
        Cursor j8 = j(cursor);
        if (j8 != null) {
            j8.close();
        }
    }

    @Override // v0.b.a
    public Cursor b() {
        return this.f23146c;
    }

    public abstract CharSequence c(Cursor cursor);

    public abstract void e(View view, Context context, Cursor cursor);

    void f(Context context, Cursor cursor, int i8) {
        b bVar;
        if ((i8 & 1) == 1) {
            i8 |= 2;
            this.f23145b = true;
        } else {
            this.f23145b = false;
        }
        boolean z4 = cursor != null;
        this.f23146c = cursor;
        this.f23144a = z4;
        this.f23147d = context;
        this.f23148e = z4 ? cursor.getColumnIndexOrThrow("_id") : -1;
        if ((i8 & 2) == 2) {
            this.f23149f = new C0214a();
            bVar = new b();
        } else {
            bVar = null;
            this.f23149f = null;
        }
        this.f23150g = bVar;
        if (z4) {
            C0214a c0214a = this.f23149f;
            if (c0214a != null) {
                cursor.registerContentObserver(c0214a);
            }
            DataSetObserver dataSetObserver = this.f23150g;
            if (dataSetObserver != null) {
                cursor.registerDataSetObserver(dataSetObserver);
            }
        }
    }

    public abstract View g(Context context, Cursor cursor, ViewGroup viewGroup);

    @Override // android.widget.Adapter
    public int getCount() {
        Cursor cursor;
        if (!this.f23144a || (cursor = this.f23146c) == null) {
            return 0;
        }
        return cursor.getCount();
    }

    @Override // android.widget.BaseAdapter, android.widget.SpinnerAdapter
    public View getDropDownView(int i8, View view, ViewGroup viewGroup) {
        if (this.f23144a) {
            this.f23146c.moveToPosition(i8);
            if (view == null) {
                view = g(this.f23147d, this.f23146c, viewGroup);
            }
            e(view, this.f23147d, this.f23146c);
            return view;
        }
        return null;
    }

    @Override // android.widget.Filterable
    public Filter getFilter() {
        if (this.f23151h == null) {
            this.f23151h = new v0.b(this);
        }
        return this.f23151h;
    }

    @Override // android.widget.Adapter
    public Object getItem(int i8) {
        Cursor cursor;
        if (!this.f23144a || (cursor = this.f23146c) == null) {
            return null;
        }
        cursor.moveToPosition(i8);
        return this.f23146c;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i8) {
        Cursor cursor;
        if (this.f23144a && (cursor = this.f23146c) != null && cursor.moveToPosition(i8)) {
            return this.f23146c.getLong(this.f23148e);
        }
        return 0L;
    }

    @Override // android.widget.Adapter
    public View getView(int i8, View view, ViewGroup viewGroup) {
        if (this.f23144a) {
            if (this.f23146c.moveToPosition(i8)) {
                if (view == null) {
                    view = h(this.f23147d, this.f23146c, viewGroup);
                }
                e(view, this.f23147d, this.f23146c);
                return view;
            }
            throw new IllegalStateException("couldn't move cursor to position " + i8);
        }
        throw new IllegalStateException("this should only be called when the cursor is valid");
    }

    public abstract View h(Context context, Cursor cursor, ViewGroup viewGroup);

    protected void i() {
        Cursor cursor;
        if (!this.f23145b || (cursor = this.f23146c) == null || cursor.isClosed()) {
            return;
        }
        this.f23144a = this.f23146c.requery();
    }

    public Cursor j(Cursor cursor) {
        Cursor cursor2 = this.f23146c;
        if (cursor == cursor2) {
            return null;
        }
        if (cursor2 != null) {
            C0214a c0214a = this.f23149f;
            if (c0214a != null) {
                cursor2.unregisterContentObserver(c0214a);
            }
            DataSetObserver dataSetObserver = this.f23150g;
            if (dataSetObserver != null) {
                cursor2.unregisterDataSetObserver(dataSetObserver);
            }
        }
        this.f23146c = cursor;
        if (cursor != null) {
            C0214a c0214a2 = this.f23149f;
            if (c0214a2 != null) {
                cursor.registerContentObserver(c0214a2);
            }
            DataSetObserver dataSetObserver2 = this.f23150g;
            if (dataSetObserver2 != null) {
                cursor.registerDataSetObserver(dataSetObserver2);
            }
            this.f23148e = cursor.getColumnIndexOrThrow("_id");
            this.f23144a = true;
            notifyDataSetChanged();
        } else {
            this.f23148e = -1;
            this.f23144a = false;
            notifyDataSetInvalidated();
        }
        return cursor2;
    }
}
