package com.google.android.exoplayer2.source;

import java.util.Arrays;
import java.util.Random;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface x {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements x {

        /* renamed from: a  reason: collision with root package name */
        private final Random f10833a;

        /* renamed from: b  reason: collision with root package name */
        private final int[] f10834b;

        /* renamed from: c  reason: collision with root package name */
        private final int[] f10835c;

        public a(int i8) {
            this(i8, new Random());
        }

        private a(int i8, Random random) {
            this(a(i8, random), random);
        }

        public a(int[] iArr, long j8) {
            this(Arrays.copyOf(iArr, iArr.length), new Random(j8));
        }

        private a(int[] iArr, Random random) {
            this.f10834b = iArr;
            this.f10833a = random;
            this.f10835c = new int[iArr.length];
            for (int i8 = 0; i8 < iArr.length; i8++) {
                this.f10835c[iArr[i8]] = i8;
            }
        }

        private static int[] a(int i8, Random random) {
            int[] iArr = new int[i8];
            int i9 = 0;
            while (i9 < i8) {
                int i10 = i9 + 1;
                int nextInt = random.nextInt(i10);
                iArr[i9] = iArr[nextInt];
                iArr[nextInt] = i9;
                i9 = i10;
            }
            return iArr;
        }

        @Override // com.google.android.exoplayer2.source.x
        public int b() {
            return this.f10834b.length;
        }

        @Override // com.google.android.exoplayer2.source.x
        public x c(int i8, int i9) {
            int i10 = i9 - i8;
            int[] iArr = new int[this.f10834b.length - i10];
            int i11 = 0;
            int i12 = 0;
            while (true) {
                int[] iArr2 = this.f10834b;
                if (i11 >= iArr2.length) {
                    return new a(iArr, new Random(this.f10833a.nextLong()));
                }
                if (iArr2[i11] < i8 || iArr2[i11] >= i9) {
                    iArr[i11 - i12] = iArr2[i11] >= i8 ? iArr2[i11] - i10 : iArr2[i11];
                } else {
                    i12++;
                }
                i11++;
            }
        }

        @Override // com.google.android.exoplayer2.source.x
        public int d() {
            int[] iArr = this.f10834b;
            if (iArr.length > 0) {
                return iArr[0];
            }
            return -1;
        }

        @Override // com.google.android.exoplayer2.source.x
        public int e(int i8) {
            int i9 = this.f10835c[i8] - 1;
            if (i9 >= 0) {
                return this.f10834b[i9];
            }
            return -1;
        }

        @Override // com.google.android.exoplayer2.source.x
        public int f(int i8) {
            int i9 = this.f10835c[i8] + 1;
            int[] iArr = this.f10834b;
            if (i9 < iArr.length) {
                return iArr[i9];
            }
            return -1;
        }

        @Override // com.google.android.exoplayer2.source.x
        public x g(int i8, int i9) {
            int[] iArr = new int[i9];
            int[] iArr2 = new int[i9];
            int i10 = 0;
            int i11 = 0;
            while (i11 < i9) {
                iArr[i11] = this.f10833a.nextInt(this.f10834b.length + 1);
                int i12 = i11 + 1;
                int nextInt = this.f10833a.nextInt(i12);
                iArr2[i11] = iArr2[nextInt];
                iArr2[nextInt] = i11 + i8;
                i11 = i12;
            }
            Arrays.sort(iArr);
            int[] iArr3 = new int[this.f10834b.length + i9];
            int i13 = 0;
            int i14 = 0;
            while (true) {
                int[] iArr4 = this.f10834b;
                if (i10 >= iArr4.length + i9) {
                    return new a(iArr3, new Random(this.f10833a.nextLong()));
                }
                if (i13 >= i9 || i14 != iArr[i13]) {
                    int i15 = i14 + 1;
                    iArr3[i10] = iArr4[i14];
                    if (iArr3[i10] >= i8) {
                        iArr3[i10] = iArr3[i10] + i9;
                    }
                    i14 = i15;
                } else {
                    iArr3[i10] = iArr2[i13];
                    i13++;
                }
                i10++;
            }
        }

        @Override // com.google.android.exoplayer2.source.x
        public int h() {
            int[] iArr = this.f10834b;
            if (iArr.length > 0) {
                return iArr[iArr.length - 1];
            }
            return -1;
        }

        @Override // com.google.android.exoplayer2.source.x
        public x i() {
            return new a(0, new Random(this.f10833a.nextLong()));
        }
    }

    int b();

    x c(int i8, int i9);

    int d();

    int e(int i8);

    int f(int i8);

    x g(int i8, int i9);

    int h();

    x i();
}
