package com.google.android.exoplayer2.offline;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class DownloadRequest implements Parcelable {
    public static final Parcelable.Creator<DownloadRequest> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final String f10190a;

    /* renamed from: b  reason: collision with root package name */
    public final Uri f10191b;

    /* renamed from: c  reason: collision with root package name */
    public final String f10192c;

    /* renamed from: d  reason: collision with root package name */
    public final List<StreamKey> f10193d;

    /* renamed from: e  reason: collision with root package name */
    public final byte[] f10194e;

    /* renamed from: f  reason: collision with root package name */
    public final String f10195f;

    /* renamed from: g  reason: collision with root package name */
    public final byte[] f10196g;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class UnsupportedRequestException extends IOException {
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<DownloadRequest> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public DownloadRequest createFromParcel(Parcel parcel) {
            return new DownloadRequest(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public DownloadRequest[] newArray(int i8) {
            return new DownloadRequest[i8];
        }
    }

    DownloadRequest(Parcel parcel) {
        this.f10190a = (String) l0.j(parcel.readString());
        this.f10191b = Uri.parse((String) l0.j(parcel.readString()));
        this.f10192c = parcel.readString();
        int readInt = parcel.readInt();
        ArrayList arrayList = new ArrayList(readInt);
        for (int i8 = 0; i8 < readInt; i8++) {
            arrayList.add((StreamKey) parcel.readParcelable(StreamKey.class.getClassLoader()));
        }
        this.f10193d = Collections.unmodifiableList(arrayList);
        this.f10194e = parcel.createByteArray();
        this.f10195f = parcel.readString();
        this.f10196g = (byte[]) l0.j(parcel.createByteArray());
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (obj instanceof DownloadRequest) {
            DownloadRequest downloadRequest = (DownloadRequest) obj;
            return this.f10190a.equals(downloadRequest.f10190a) && this.f10191b.equals(downloadRequest.f10191b) && l0.c(this.f10192c, downloadRequest.f10192c) && this.f10193d.equals(downloadRequest.f10193d) && Arrays.equals(this.f10194e, downloadRequest.f10194e) && l0.c(this.f10195f, downloadRequest.f10195f) && Arrays.equals(this.f10196g, downloadRequest.f10196g);
        }
        return false;
    }

    public final int hashCode() {
        int hashCode = ((this.f10190a.hashCode() * 31 * 31) + this.f10191b.hashCode()) * 31;
        String str = this.f10192c;
        int hashCode2 = (((((hashCode + (str != null ? str.hashCode() : 0)) * 31) + this.f10193d.hashCode()) * 31) + Arrays.hashCode(this.f10194e)) * 31;
        String str2 = this.f10195f;
        return ((hashCode2 + (str2 != null ? str2.hashCode() : 0)) * 31) + Arrays.hashCode(this.f10196g);
    }

    public String toString() {
        return this.f10192c + ":" + this.f10190a;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10190a);
        parcel.writeString(this.f10191b.toString());
        parcel.writeString(this.f10192c);
        parcel.writeInt(this.f10193d.size());
        for (int i9 = 0; i9 < this.f10193d.size(); i9++) {
            parcel.writeParcelable(this.f10193d.get(i9), 0);
        }
        parcel.writeByteArray(this.f10194e);
        parcel.writeString(this.f10195f);
        parcel.writeByteArray(this.f10196g);
    }
}
