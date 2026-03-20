package com.google.android.exoplayer2.metadata.emsg;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.w0;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class EventMessage implements Metadata.Entry {

    /* renamed from: a  reason: collision with root package name */
    public final String f10061a;

    /* renamed from: b  reason: collision with root package name */
    public final String f10062b;

    /* renamed from: c  reason: collision with root package name */
    public final long f10063c;

    /* renamed from: d  reason: collision with root package name */
    public final long f10064d;

    /* renamed from: e  reason: collision with root package name */
    public final byte[] f10065e;

    /* renamed from: f  reason: collision with root package name */
    private int f10066f;

    /* renamed from: g  reason: collision with root package name */
    private static final w0 f10059g = new w0.b().g0("application/id3").G();

    /* renamed from: h  reason: collision with root package name */
    private static final w0 f10060h = new w0.b().g0("application/x-scte35").G();
    public static final Parcelable.Creator<EventMessage> CREATOR = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<EventMessage> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public EventMessage createFromParcel(Parcel parcel) {
            return new EventMessage(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public EventMessage[] newArray(int i8) {
            return new EventMessage[i8];
        }
    }

    EventMessage(Parcel parcel) {
        this.f10061a = (String) l0.j(parcel.readString());
        this.f10062b = (String) l0.j(parcel.readString());
        this.f10063c = parcel.readLong();
        this.f10064d = parcel.readLong();
        this.f10065e = (byte[]) l0.j(parcel.createByteArray());
    }

    public EventMessage(String str, String str2, long j8, long j9, byte[] bArr) {
        this.f10061a = str;
        this.f10062b = str2;
        this.f10063c = j8;
        this.f10064d = j9;
        this.f10065e = bArr;
    }

    @Override // com.google.android.exoplayer2.metadata.Metadata.Entry
    public byte[] M0() {
        if (V() != null) {
            return this.f10065e;
        }
        return null;
    }

    @Override // com.google.android.exoplayer2.metadata.Metadata.Entry
    public w0 V() {
        String str = this.f10061a;
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case -1468477611:
                if (str.equals("urn:scte:scte35:2014:bin")) {
                    c9 = 0;
                    break;
                }
                break;
            case -795945609:
                if (str.equals("https://aomedia.org/emsg/ID3")) {
                    c9 = 1;
                    break;
                }
                break;
            case 1303648457:
                if (str.equals("https://developer.apple.com/streaming/emsg-id3")) {
                    c9 = 2;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                return f10060h;
            case 1:
            case 2:
                return f10059g;
            default:
                return null;
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || EventMessage.class != obj.getClass()) {
            return false;
        }
        EventMessage eventMessage = (EventMessage) obj;
        return this.f10063c == eventMessage.f10063c && this.f10064d == eventMessage.f10064d && l0.c(this.f10061a, eventMessage.f10061a) && l0.c(this.f10062b, eventMessage.f10062b) && Arrays.equals(this.f10065e, eventMessage.f10065e);
    }

    public int hashCode() {
        if (this.f10066f == 0) {
            String str = this.f10061a;
            int hashCode = (527 + (str != null ? str.hashCode() : 0)) * 31;
            String str2 = this.f10062b;
            int hashCode2 = str2 != null ? str2.hashCode() : 0;
            long j8 = this.f10063c;
            long j9 = this.f10064d;
            this.f10066f = ((((((hashCode + hashCode2) * 31) + ((int) (j8 ^ (j8 >>> 32)))) * 31) + ((int) (j9 ^ (j9 >>> 32)))) * 31) + Arrays.hashCode(this.f10065e);
        }
        return this.f10066f;
    }

    public String toString() {
        return "EMSG: scheme=" + this.f10061a + ", id=" + this.f10064d + ", durationMs=" + this.f10063c + ", value=" + this.f10062b;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10061a);
        parcel.writeString(this.f10062b);
        parcel.writeLong(this.f10063c);
        parcel.writeLong(this.f10064d);
        parcel.writeByteArray(this.f10065e);
    }
}
