package v0;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class c extends a {

    /* renamed from: j  reason: collision with root package name */
    private int f23155j;

    /* renamed from: k  reason: collision with root package name */
    private int f23156k;

    /* renamed from: l  reason: collision with root package name */
    private LayoutInflater f23157l;

    @Deprecated
    public c(Context context, int i8, Cursor cursor, boolean z4) {
        super(context, cursor, z4);
        this.f23156k = i8;
        this.f23155j = i8;
        this.f23157l = (LayoutInflater) context.getSystemService("layout_inflater");
    }

    @Override // v0.a
    public View g(Context context, Cursor cursor, ViewGroup viewGroup) {
        return this.f23157l.inflate(this.f23156k, viewGroup, false);
    }

    @Override // v0.a
    public View h(Context context, Cursor cursor, ViewGroup viewGroup) {
        return this.f23157l.inflate(this.f23155j, viewGroup, false);
    }
}
