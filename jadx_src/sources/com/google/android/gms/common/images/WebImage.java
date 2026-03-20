package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import java.util.Locale;
import n6.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class WebImage extends AbstractSafeParcelable {
    public static final Parcelable.Creator<WebImage> CREATOR = new d();

    /* renamed from: a  reason: collision with root package name */
    final int f11759a;

    /* renamed from: b  reason: collision with root package name */
    private final Uri f11760b;

    /* renamed from: c  reason: collision with root package name */
    private final int f11761c;

    /* renamed from: d  reason: collision with root package name */
    private final int f11762d;

    /* JADX INFO: Access modifiers changed from: package-private */
    public WebImage(int i8, Uri uri, int i9, int i10) {
        this.f11759a = i8;
        this.f11760b = uri;
        this.f11761c = i9;
        this.f11762d = i10;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof WebImage)) {
            WebImage webImage = (WebImage) obj;
            if (i.a(this.f11760b, webImage.f11760b) && this.f11761c == webImage.f11761c && this.f11762d == webImage.f11762d) {
                return true;
            }
        }
        return false;
    }

    public int getHeight() {
        return this.f11762d;
    }

    public int getWidth() {
        return this.f11761c;
    }

    public int hashCode() {
        return i.b(this.f11760b, Integer.valueOf(this.f11761c), Integer.valueOf(this.f11762d));
    }

    public Uri t() {
        return this.f11760b;
    }

    public String toString() {
        return String.format(Locale.US, "Image %dx%d %s", Integer.valueOf(this.f11761c), Integer.valueOf(this.f11762d), this.f11760b.toString());
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.l(parcel, 1, this.f11759a);
        o6.a.q(parcel, 2, t(), i8, false);
        o6.a.l(parcel, 3, getWidth());
        o6.a.l(parcel, 4, getHeight());
        o6.a.b(parcel, a9);
    }
}
