package com.google.android.exoplayer2.metadata.id3;

import android.os.Parcel;
import android.os.Parcelable;
import b6.l0;
import com.google.android.exoplayer2.a1;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class TextInformationFrame extends Id3Frame {
    public static final Parcelable.Creator<TextInformationFrame> CREATOR = new a();

    /* renamed from: b  reason: collision with root package name */
    public final String f10120b;
    @Deprecated

    /* renamed from: c  reason: collision with root package name */
    public final String f10121c;

    /* renamed from: d  reason: collision with root package name */
    public final ImmutableList<String> f10122d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<TextInformationFrame> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public TextInformationFrame createFromParcel(Parcel parcel) {
            return new TextInformationFrame(parcel, null);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public TextInformationFrame[] newArray(int i8) {
            return new TextInformationFrame[i8];
        }
    }

    private TextInformationFrame(Parcel parcel) {
        this((String) b6.a.e(parcel.readString()), parcel.readString(), ImmutableList.y((String[]) b6.a.e(parcel.createStringArray())));
    }

    /* synthetic */ TextInformationFrame(Parcel parcel, a aVar) {
        this(parcel);
    }

    public TextInformationFrame(String str, String str2, List<String> list) {
        super(str);
        b6.a.a(!list.isEmpty());
        this.f10120b = str2;
        ImmutableList<String> x8 = ImmutableList.x(list);
        this.f10122d = x8;
        this.f10121c = x8.get(0);
    }

    private static List<Integer> a(String str) {
        int parseInt;
        ArrayList arrayList = new ArrayList();
        try {
            if (str.length() >= 10) {
                arrayList.add(Integer.valueOf(Integer.parseInt(str.substring(0, 4))));
                arrayList.add(Integer.valueOf(Integer.parseInt(str.substring(5, 7))));
                parseInt = Integer.parseInt(str.substring(8, 10));
            } else if (str.length() < 7) {
                if (str.length() >= 4) {
                    parseInt = Integer.parseInt(str.substring(0, 4));
                }
                return arrayList;
            } else {
                arrayList.add(Integer.valueOf(Integer.parseInt(str.substring(0, 4))));
                parseInt = Integer.parseInt(str.substring(5, 7));
            }
            arrayList.add(Integer.valueOf(parseInt));
            return arrayList;
        } catch (NumberFormatException unused) {
            return new ArrayList();
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.google.android.exoplayer2.metadata.Metadata.Entry
    public void C(a1.b bVar) {
        String str = this.f10109a;
        str.hashCode();
        char c9 = 65535;
        switch (str.hashCode()) {
            case 82815:
                if (str.equals("TAL")) {
                    c9 = 0;
                    break;
                }
                break;
            case 82878:
                if (str.equals("TCM")) {
                    c9 = 1;
                    break;
                }
                break;
            case 82897:
                if (str.equals("TDA")) {
                    c9 = 2;
                    break;
                }
                break;
            case 83253:
                if (str.equals("TP1")) {
                    c9 = 3;
                    break;
                }
                break;
            case 83254:
                if (str.equals("TP2")) {
                    c9 = 4;
                    break;
                }
                break;
            case 83255:
                if (str.equals("TP3")) {
                    c9 = 5;
                    break;
                }
                break;
            case 83341:
                if (str.equals("TRK")) {
                    c9 = 6;
                    break;
                }
                break;
            case 83378:
                if (str.equals("TT2")) {
                    c9 = 7;
                    break;
                }
                break;
            case 83536:
                if (str.equals("TXT")) {
                    c9 = '\b';
                    break;
                }
                break;
            case 83552:
                if (str.equals("TYE")) {
                    c9 = '\t';
                    break;
                }
                break;
            case 2567331:
                if (str.equals("TALB")) {
                    c9 = '\n';
                    break;
                }
                break;
            case 2569357:
                if (str.equals("TCOM")) {
                    c9 = 11;
                    break;
                }
                break;
            case 2569891:
                if (str.equals("TDAT")) {
                    c9 = '\f';
                    break;
                }
                break;
            case 2570401:
                if (str.equals("TDRC")) {
                    c9 = '\r';
                    break;
                }
                break;
            case 2570410:
                if (str.equals("TDRL")) {
                    c9 = 14;
                    break;
                }
                break;
            case 2571565:
                if (str.equals("TEXT")) {
                    c9 = 15;
                    break;
                }
                break;
            case 2575251:
                if (str.equals("TIT2")) {
                    c9 = 16;
                    break;
                }
                break;
            case 2581512:
                if (str.equals("TPE1")) {
                    c9 = 17;
                    break;
                }
                break;
            case 2581513:
                if (str.equals("TPE2")) {
                    c9 = 18;
                    break;
                }
                break;
            case 2581514:
                if (str.equals("TPE3")) {
                    c9 = 19;
                    break;
                }
                break;
            case 2583398:
                if (str.equals("TRCK")) {
                    c9 = 20;
                    break;
                }
                break;
            case 2590194:
                if (str.equals("TYER")) {
                    c9 = 21;
                    break;
                }
                break;
        }
        try {
            switch (c9) {
                case 0:
                case '\n':
                    bVar.N(this.f10122d.get(0));
                    return;
                case 1:
                case 11:
                    bVar.S(this.f10122d.get(0));
                    return;
                case 2:
                case '\f':
                    String str2 = this.f10122d.get(0);
                    bVar.f0(Integer.valueOf(Integer.parseInt(str2.substring(2, 4)))).e0(Integer.valueOf(Integer.parseInt(str2.substring(0, 2))));
                    return;
                case 3:
                case 17:
                    bVar.O(this.f10122d.get(0));
                    return;
                case 4:
                case 18:
                    bVar.M(this.f10122d.get(0));
                    return;
                case 5:
                case 19:
                    bVar.T(this.f10122d.get(0));
                    return;
                case 6:
                case 20:
                    String[] R0 = l0.R0(this.f10122d.get(0), "/");
                    bVar.p0(Integer.valueOf(Integer.parseInt(R0[0]))).o0(R0.length > 1 ? Integer.valueOf(Integer.parseInt(R0[1])) : null);
                    return;
                case 7:
                case 16:
                    bVar.m0(this.f10122d.get(0));
                    return;
                case '\b':
                case 15:
                    bVar.r0(this.f10122d.get(0));
                    return;
                case '\t':
                case 21:
                    bVar.g0(Integer.valueOf(Integer.parseInt(this.f10122d.get(0))));
                    return;
                case '\r':
                    List<Integer> a9 = a(this.f10122d.get(0));
                    int size = a9.size();
                    if (size != 1) {
                        if (size != 2) {
                            if (size != 3) {
                                return;
                            }
                            bVar.e0(a9.get(2));
                        }
                        bVar.f0(a9.get(1));
                    }
                    bVar.g0(a9.get(0));
                    return;
                case 14:
                    List<Integer> a10 = a(this.f10122d.get(0));
                    int size2 = a10.size();
                    if (size2 != 1) {
                        if (size2 != 2) {
                            if (size2 != 3) {
                                return;
                            }
                            bVar.h0(a10.get(2));
                        }
                        bVar.i0(a10.get(1));
                    }
                    bVar.j0(a10.get(0));
                    return;
                default:
                    return;
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException unused) {
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || TextInformationFrame.class != obj.getClass()) {
            return false;
        }
        TextInformationFrame textInformationFrame = (TextInformationFrame) obj;
        return l0.c(this.f10109a, textInformationFrame.f10109a) && l0.c(this.f10120b, textInformationFrame.f10120b) && this.f10122d.equals(textInformationFrame.f10122d);
    }

    public int hashCode() {
        int hashCode = (527 + this.f10109a.hashCode()) * 31;
        String str = this.f10120b;
        return ((hashCode + (str != null ? str.hashCode() : 0)) * 31) + this.f10122d.hashCode();
    }

    @Override // com.google.android.exoplayer2.metadata.id3.Id3Frame
    public String toString() {
        return this.f10109a + ": description=" + this.f10120b + ": values=" + this.f10122d;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeString(this.f10109a);
        parcel.writeString(this.f10120b);
        parcel.writeStringArray((String[]) this.f10122d.toArray(new String[0]));
    }
}
