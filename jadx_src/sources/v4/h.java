package v4;

import b6.z;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.metadata.id3.ApicFrame;
import com.google.android.exoplayer2.metadata.id3.CommentFrame;
import com.google.android.exoplayer2.metadata.id3.Id3Frame;
import com.google.android.exoplayer2.metadata.id3.InternalFrame;
import com.google.android.exoplayer2.metadata.id3.TextInformationFrame;
import com.google.android.exoplayer2.metadata.mp4.MdtaMetadataEntry;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import n4.v;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class h {

    /* renamed from: a  reason: collision with root package name */
    static final String[] f23257a = {"Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz", "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial", "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal", "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel", "Noise", "AlternRock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock", "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native American", "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin", "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", "Slow Rock", "Big Band", "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson", "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", "Drum Solo", "A capella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie", "BritPop", "Afro-Punk", "Polsk Punk", "Beat", "Christian Gangsta Rap", "Heavy Metal", "Black Metal", "Crossover", "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "Jpop", "Synthpop", "Abstract", "Art Rock", "Baroque", "Bhangra", "Big beat", "Breakbeat", "Chillout", "Downtempo", "Dub", "EBM", "Eclectic", "Electro", "Electroclash", "Emo", "Experimental", "Garage", "Global", "IDM", "Illbient", "Industro-Goth", "Jam Band", "Krautrock", "Leftfield", "Lounge", "Math Rock", "New Romantic", "Nu-Breakz", "Post-Punk", "Post-Rock", "Psytrance", "Shoegaze", "Space Rock", "Trop Rock", "World Music", "Neoclassical", "Audiobook", "Audio theatre", "Neue Deutsche Welle", "Podcast", "Indie-Rock", "G-Funk", "Dubstep", "Garage Rock", "Psybient"};

    private static CommentFrame a(int i8, z zVar) {
        int q = zVar.q();
        if (zVar.q() == 1684108385) {
            zVar.V(8);
            String C = zVar.C(q - 16);
            return new CommentFrame("und", C, C);
        }
        b6.p.i("MetadataUtil", "Failed to parse comment attribute: " + a.a(i8));
        return null;
    }

    private static ApicFrame b(z zVar) {
        String str;
        int q = zVar.q();
        if (zVar.q() == 1684108385) {
            int b9 = a.b(zVar.q());
            String str2 = b9 == 13 ? "image/jpeg" : b9 == 14 ? "image/png" : null;
            if (str2 != null) {
                zVar.V(4);
                int i8 = q - 16;
                byte[] bArr = new byte[i8];
                zVar.l(bArr, 0, i8);
                return new ApicFrame(str2, null, 3, bArr);
            }
            str = "Unrecognized cover art flags: " + b9;
        } else {
            str = "Failed to parse cover art attribute";
        }
        b6.p.i("MetadataUtil", str);
        return null;
    }

    public static Metadata.Entry c(z zVar) {
        int f5 = zVar.f() + zVar.q();
        int q = zVar.q();
        int i8 = (q >> 24) & 255;
        try {
            if (i8 == 169 || i8 == 253) {
                int i9 = 16777215 & q;
                if (i9 == 6516084) {
                    return a(q, zVar);
                }
                if (i9 == 7233901 || i9 == 7631467) {
                    return h(q, "TIT2", zVar);
                }
                if (i9 == 6516589 || i9 == 7828084) {
                    return h(q, "TCOM", zVar);
                }
                if (i9 == 6578553) {
                    return h(q, "TDRC", zVar);
                }
                if (i9 == 4280916) {
                    return h(q, "TPE1", zVar);
                }
                if (i9 == 7630703) {
                    return h(q, "TSSE", zVar);
                }
                if (i9 == 6384738) {
                    return h(q, "TALB", zVar);
                }
                if (i9 == 7108978) {
                    return h(q, "USLT", zVar);
                }
                if (i9 == 6776174) {
                    return h(q, "TCON", zVar);
                }
                if (i9 == 6779504) {
                    return h(q, "TIT1", zVar);
                }
            } else if (q == 1735291493) {
                return g(zVar);
            } else {
                if (q == 1684632427) {
                    return d(q, "TPOS", zVar);
                }
                if (q == 1953655662) {
                    return d(q, "TRCK", zVar);
                }
                if (q == 1953329263) {
                    return i(q, "TBPM", zVar, true, false);
                }
                if (q == 1668311404) {
                    return i(q, "TCMP", zVar, true, true);
                }
                if (q == 1668249202) {
                    return b(zVar);
                }
                if (q == 1631670868) {
                    return h(q, "TPE2", zVar);
                }
                if (q == 1936682605) {
                    return h(q, "TSOT", zVar);
                }
                if (q == 1936679276) {
                    return h(q, "TSO2", zVar);
                }
                if (q == 1936679282) {
                    return h(q, "TSOA", zVar);
                }
                if (q == 1936679265) {
                    return h(q, "TSOP", zVar);
                }
                if (q == 1936679791) {
                    return h(q, "TSOC", zVar);
                }
                if (q == 1920233063) {
                    return i(q, "ITUNESADVISORY", zVar, false, false);
                }
                if (q == 1885823344) {
                    return i(q, "ITUNESGAPLESS", zVar, false, true);
                }
                if (q == 1936683886) {
                    return h(q, "TVSHOWSORT", zVar);
                }
                if (q == 1953919848) {
                    return h(q, "TVSHOW", zVar);
                }
                if (q == 757935405) {
                    return e(zVar, f5);
                }
            }
            b6.p.b("MetadataUtil", "Skipped unknown metadata entry: " + a.a(q));
            return null;
        } finally {
            zVar.U(f5);
        }
    }

    private static TextInformationFrame d(int i8, String str, z zVar) {
        int q = zVar.q();
        if (zVar.q() == 1684108385 && q >= 22) {
            zVar.V(10);
            int N = zVar.N();
            if (N > 0) {
                String str2 = BuildConfig.FLAVOR + N;
                int N2 = zVar.N();
                if (N2 > 0) {
                    str2 = str2 + "/" + N2;
                }
                return new TextInformationFrame(str, null, ImmutableList.F(str2));
            }
        }
        b6.p.i("MetadataUtil", "Failed to parse index/count attribute: " + a.a(i8));
        return null;
    }

    private static Id3Frame e(z zVar, int i8) {
        int i9 = -1;
        int i10 = -1;
        String str = null;
        String str2 = null;
        while (zVar.f() < i8) {
            int f5 = zVar.f();
            int q = zVar.q();
            int q8 = zVar.q();
            zVar.V(4);
            if (q8 == 1835360622) {
                str = zVar.C(q - 12);
            } else if (q8 == 1851878757) {
                str2 = zVar.C(q - 12);
            } else {
                if (q8 == 1684108385) {
                    i9 = f5;
                    i10 = q;
                }
                zVar.V(q - 12);
            }
        }
        if (str == null || str2 == null || i9 == -1) {
            return null;
        }
        zVar.U(i9);
        zVar.V(16);
        return new InternalFrame(str, str2, zVar.C(i10 - 16));
    }

    public static MdtaMetadataEntry f(z zVar, int i8, String str) {
        while (true) {
            int f5 = zVar.f();
            if (f5 >= i8) {
                return null;
            }
            int q = zVar.q();
            if (zVar.q() == 1684108385) {
                int q8 = zVar.q();
                int q9 = zVar.q();
                int i9 = q - 16;
                byte[] bArr = new byte[i9];
                zVar.l(bArr, 0, i9);
                return new MdtaMetadataEntry(str, bArr, q9, q8);
            }
            zVar.U(f5 + q);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:11:0x0020  */
    /* JADX WARN: Removed duplicated region for block: B:9:0x0014  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private static com.google.android.exoplayer2.metadata.id3.TextInformationFrame g(b6.z r3) {
        /*
            int r3 = j(r3)
            r0 = 0
            if (r3 <= 0) goto L11
            java.lang.String[] r1 = v4.h.f23257a
            int r2 = r1.length
            if (r3 > r2) goto L11
            int r3 = r3 + (-1)
            r3 = r1[r3]
            goto L12
        L11:
            r3 = r0
        L12:
            if (r3 == 0) goto L20
            com.google.android.exoplayer2.metadata.id3.TextInformationFrame r1 = new com.google.android.exoplayer2.metadata.id3.TextInformationFrame
            com.google.common.collect.ImmutableList r3 = com.google.common.collect.ImmutableList.F(r3)
            java.lang.String r2 = "TCON"
            r1.<init>(r2, r0, r3)
            return r1
        L20:
            java.lang.String r3 = "MetadataUtil"
            java.lang.String r1 = "Failed to parse standard genre code"
            b6.p.i(r3, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: v4.h.g(b6.z):com.google.android.exoplayer2.metadata.id3.TextInformationFrame");
    }

    private static TextInformationFrame h(int i8, String str, z zVar) {
        int q = zVar.q();
        if (zVar.q() == 1684108385) {
            zVar.V(8);
            return new TextInformationFrame(str, null, ImmutableList.F(zVar.C(q - 16)));
        }
        b6.p.i("MetadataUtil", "Failed to parse text attribute: " + a.a(i8));
        return null;
    }

    private static Id3Frame i(int i8, String str, z zVar, boolean z4, boolean z8) {
        int j8 = j(zVar);
        if (z8) {
            j8 = Math.min(1, j8);
        }
        if (j8 >= 0) {
            return z4 ? new TextInformationFrame(str, null, ImmutableList.F(Integer.toString(j8))) : new CommentFrame("und", str, Integer.toString(j8));
        }
        b6.p.i("MetadataUtil", "Failed to parse uint8 attribute: " + a.a(i8));
        return null;
    }

    private static int j(z zVar) {
        zVar.V(4);
        if (zVar.q() == 1684108385) {
            zVar.V(8);
            return zVar.H();
        }
        b6.p.i("MetadataUtil", "Failed to parse uint8 attribute value");
        return -1;
    }

    public static void k(int i8, v vVar, w0.b bVar) {
        if (i8 == 1 && vVar.a()) {
            bVar.P(vVar.f22145a).Q(vVar.f22146b);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:4:0x000b, code lost:
        if (r6 != null) goto L5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void l(int r5, com.google.android.exoplayer2.metadata.Metadata r6, com.google.android.exoplayer2.metadata.Metadata r7, com.google.android.exoplayer2.w0.b r8, com.google.android.exoplayer2.metadata.Metadata... r9) {
        /*
            com.google.android.exoplayer2.metadata.Metadata r0 = new com.google.android.exoplayer2.metadata.Metadata
            r1 = 0
            com.google.android.exoplayer2.metadata.Metadata$Entry[] r2 = new com.google.android.exoplayer2.metadata.Metadata.Entry[r1]
            r0.<init>(r2)
            r2 = 1
            if (r5 != r2) goto Le
            if (r6 == 0) goto L3c
            goto L3d
        Le:
            r6 = 2
            if (r5 != r6) goto L3c
            if (r7 == 0) goto L3c
            r5 = r1
        L14:
            int r6 = r7.e()
            if (r5 >= r6) goto L3c
            com.google.android.exoplayer2.metadata.Metadata$Entry r6 = r7.d(r5)
            boolean r3 = r6 instanceof com.google.android.exoplayer2.metadata.mp4.MdtaMetadataEntry
            if (r3 == 0) goto L39
            com.google.android.exoplayer2.metadata.mp4.MdtaMetadataEntry r6 = (com.google.android.exoplayer2.metadata.mp4.MdtaMetadataEntry) r6
            java.lang.String r3 = r6.f10125a
            java.lang.String r4 = "com.android.capture.fps"
            boolean r3 = r4.equals(r3)
            if (r3 == 0) goto L39
            com.google.android.exoplayer2.metadata.Metadata r5 = new com.google.android.exoplayer2.metadata.Metadata
            com.google.android.exoplayer2.metadata.Metadata$Entry[] r7 = new com.google.android.exoplayer2.metadata.Metadata.Entry[r2]
            r7[r1] = r6
            r5.<init>(r7)
            r6 = r5
            goto L3d
        L39:
            int r5 = r5 + 1
            goto L14
        L3c:
            r6 = r0
        L3d:
            int r5 = r9.length
        L3e:
            if (r1 >= r5) goto L49
            r7 = r9[r1]
            com.google.android.exoplayer2.metadata.Metadata r6 = r6.b(r7)
            int r1 = r1 + 1
            goto L3e
        L49:
            int r5 = r6.e()
            if (r5 <= 0) goto L52
            r8.Z(r6)
        L52:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: v4.h.l(int, com.google.android.exoplayer2.metadata.Metadata, com.google.android.exoplayer2.metadata.Metadata, com.google.android.exoplayer2.w0$b, com.google.android.exoplayer2.metadata.Metadata[]):void");
    }
}
