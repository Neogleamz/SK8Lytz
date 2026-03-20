package s4;

import com.google.android.exoplayer2.metadata.mp4.MotionPhotoMetadata;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class b {

    /* renamed from: a  reason: collision with root package name */
    public final long f22783a;

    /* renamed from: b  reason: collision with root package name */
    public final List<a> f22784b;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final String f22785a;

        /* renamed from: b  reason: collision with root package name */
        public final String f22786b;

        /* renamed from: c  reason: collision with root package name */
        public final long f22787c;

        /* renamed from: d  reason: collision with root package name */
        public final long f22788d;

        public a(String str, String str2, long j8, long j9) {
            this.f22785a = str;
            this.f22786b = str2;
            this.f22787c = j8;
            this.f22788d = j9;
        }
    }

    public b(long j8, List<a> list) {
        this.f22783a = j8;
        this.f22784b = list;
    }

    public MotionPhotoMetadata a(long j8) {
        long j9;
        if (this.f22784b.size() < 2) {
            return null;
        }
        long j10 = j8;
        long j11 = -1;
        long j12 = -1;
        long j13 = -1;
        long j14 = -1;
        boolean z4 = false;
        for (int size = this.f22784b.size() - 1; size >= 0; size--) {
            a aVar = this.f22784b.get(size);
            boolean equals = "video/mp4".equals(aVar.f22785a) | z4;
            if (size == 0) {
                j9 = j10 - aVar.f22788d;
                j10 = 0;
            } else {
                long j15 = j10;
                j10 -= aVar.f22787c;
                j9 = j15;
            }
            if (!equals || j10 == j9) {
                z4 = equals;
            } else {
                j14 = j9 - j10;
                j13 = j10;
                z4 = false;
            }
            if (size == 0) {
                j11 = j10;
                j12 = j9;
            }
        }
        if (j13 == -1 || j14 == -1 || j11 == -1 || j12 == -1) {
            return null;
        }
        return new MotionPhotoMetadata(j11, j12, this.f22783a, j13, j14);
    }
}
