package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.os.Parcelable;
import androidx.versionedparcelable.VersionedParcel;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class IconCompatParcelizer {
    public static IconCompat read(VersionedParcel versionedParcel) {
        IconCompat iconCompat = new IconCompat();
        iconCompat.f4714a = versionedParcel.v(iconCompat.f4714a, 1);
        iconCompat.f4716c = versionedParcel.m(iconCompat.f4716c, 2);
        iconCompat.f4717d = versionedParcel.A(iconCompat.f4717d, 3);
        iconCompat.f4718e = versionedParcel.v(iconCompat.f4718e, 4);
        iconCompat.f4719f = versionedParcel.v(iconCompat.f4719f, 5);
        iconCompat.f4720g = (ColorStateList) versionedParcel.A(iconCompat.f4720g, 6);
        iconCompat.f4722i = versionedParcel.E(iconCompat.f4722i, 7);
        iconCompat.f4723j = versionedParcel.E(iconCompat.f4723j, 8);
        iconCompat.c();
        return iconCompat;
    }

    public static void write(IconCompat iconCompat, VersionedParcel versionedParcel) {
        versionedParcel.K(true, true);
        iconCompat.d(versionedParcel.g());
        int i8 = iconCompat.f4714a;
        if (-1 != i8) {
            versionedParcel.Y(i8, 1);
        }
        byte[] bArr = iconCompat.f4716c;
        if (bArr != null) {
            versionedParcel.Q(bArr, 2);
        }
        Parcelable parcelable = iconCompat.f4717d;
        if (parcelable != null) {
            versionedParcel.d0(parcelable, 3);
        }
        int i9 = iconCompat.f4718e;
        if (i9 != 0) {
            versionedParcel.Y(i9, 4);
        }
        int i10 = iconCompat.f4719f;
        if (i10 != 0) {
            versionedParcel.Y(i10, 5);
        }
        ColorStateList colorStateList = iconCompat.f4720g;
        if (colorStateList != null) {
            versionedParcel.d0(colorStateList, 6);
        }
        String str = iconCompat.f4722i;
        if (str != null) {
            versionedParcel.h0(str, 7);
        }
        String str2 = iconCompat.f4723j;
        if (str2 != null) {
            versionedParcel.h0(str2, 8);
        }
    }
}
