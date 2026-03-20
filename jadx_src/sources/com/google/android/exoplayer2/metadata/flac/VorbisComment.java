package com.google.android.exoplayer2.metadata.flac;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import com.google.android.exoplayer2.a1;
import com.google.android.exoplayer2.metadata.Metadata;
@Deprecated
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class VorbisComment implements Metadata.Entry {
    public static final Parcelable.Creator<VorbisComment> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    public final String f10075a;

    /* renamed from: b  reason: collision with root package name */
    public final String f10076b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<VorbisComment> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public VorbisComment createFromParcel(Parcel parcel) {
            return new VorbisComment(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public VorbisComment[] newArray(int i8) {
            return new VorbisComment[i8];
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public VorbisComment(Parcel parcel) {
        this.f10075a = (String) l0.j(parcel.readString());
        this.f10076b = (String) l0.j(parcel.readString());
    }

    public VorbisComment(String str, String str2) {
        this.f10075a = str;
        this.f10076b = str2;
    }

    @Override // com.google.android.exoplayer2.metadata.Metadata.Entry
    public void C(a1.b bVar) {
        String str = this.f10075a;
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case 62359119:
                if (str.equals("ALBUM")) {
                    c9 = 0;
                    break;
                }
                break;
            case 79833656:
                if (str.equals("TITLE")) {
                    c9 = 1;
                    break;
                }
                break;
            case 428414940:
                if (str.equals("DESCRIPTION")) {
                    c9 = 2;
                    break;
                }
                break;
            case 1746739798:
                if (str.equals("ALBUMARTIST")) {
                    c9 = 3;
                    break;
                }
                break;
            case 1939198791:
                if (str.equals("ARTIST")) {
                    c9 = 4;
                    break;
                }
                break;
        }
        switch (c9) {
            case 0:
                bVar.N(this.f10076b);
                return;
            case 1:
                bVar.m0(this.f10076b);
                return;
            case 2:
                bVar.U(this.f10076b);
                return;
            case 3:
                bVar.M(this.f10076b);
                return;
            case 4:
                bVar.O(this.f10076b);
                return;
            default:
                return;
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
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        VorbisComment vorbisComment = (VorbisComment) obj;
        return this.f10075a.equals(vorbisComment.f10075a) && this.f10076b.equals(vorbisComment.f10076b);
    }

    public int hashCode() {
        return ((527 + this.f10075a.hashCode()) * 31) + this.f10076b.hashCode();
    }

    public String toString() {
        return "VC: " + this.f10075a + "=" + this.f10076b;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10075a);
        parcel.writeString(this.f10076b);
    }
}
