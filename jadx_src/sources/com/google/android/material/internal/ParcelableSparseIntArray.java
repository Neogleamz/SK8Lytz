package com.google.android.material.internal;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.SparseIntArray;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class ParcelableSparseIntArray extends SparseIntArray implements Parcelable {
    public static final Parcelable.Creator<ParcelableSparseIntArray> CREATOR = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class a implements Parcelable.Creator<ParcelableSparseIntArray> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public ParcelableSparseIntArray createFromParcel(Parcel parcel) {
            int readInt = parcel.readInt();
            ParcelableSparseIntArray parcelableSparseIntArray = new ParcelableSparseIntArray(readInt);
            int[] iArr = new int[readInt];
            int[] iArr2 = new int[readInt];
            parcel.readIntArray(iArr);
            parcel.readIntArray(iArr2);
            for (int i8 = 0; i8 < readInt; i8++) {
                parcelableSparseIntArray.put(iArr[i8], iArr2[i8]);
            }
            return parcelableSparseIntArray;
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public ParcelableSparseIntArray[] newArray(int i8) {
            return new ParcelableSparseIntArray[i8];
        }
    }

    public ParcelableSparseIntArray() {
    }

    public ParcelableSparseIntArray(int i8) {
        super(i8);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        int[] iArr = new int[size()];
        int[] iArr2 = new int[size()];
        for (int i9 = 0; i9 < size(); i9++) {
            iArr[i9] = keyAt(i9);
            iArr2[i9] = valueAt(i9);
        }
        parcel.writeInt(size());
        parcel.writeIntArray(iArr);
        parcel.writeIntArray(iArr2);
    }
}
