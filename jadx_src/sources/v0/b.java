package v0;

import android.database.Cursor;
import android.widget.Filter;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class b extends Filter {

    /* renamed from: a  reason: collision with root package name */
    a f23154a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface a {
        void a(Cursor cursor);

        Cursor b();

        CharSequence c(Cursor cursor);

        Cursor d(CharSequence charSequence);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public b(a aVar) {
        this.f23154a = aVar;
    }

    @Override // android.widget.Filter
    public CharSequence convertResultToString(Object obj) {
        return this.f23154a.c((Cursor) obj);
    }

    @Override // android.widget.Filter
    protected Filter.FilterResults performFiltering(CharSequence charSequence) {
        Cursor d8 = this.f23154a.d(charSequence);
        Filter.FilterResults filterResults = new Filter.FilterResults();
        if (d8 != null) {
            filterResults.count = d8.getCount();
        } else {
            filterResults.count = 0;
            d8 = null;
        }
        filterResults.values = d8;
        return filterResults;
    }

    @Override // android.widget.Filter
    protected void publishResults(CharSequence charSequence, Filter.FilterResults filterResults) {
        Cursor b9 = this.f23154a.b();
        Object obj = filterResults.values;
        if (obj == null || obj == b9) {
            return;
        }
        this.f23154a.a((Cursor) obj);
    }
}
