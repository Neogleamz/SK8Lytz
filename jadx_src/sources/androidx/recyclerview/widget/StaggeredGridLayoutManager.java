package androidx.recyclerview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import androidx.core.view.accessibility.c;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class StaggeredGridLayoutManager extends RecyclerView.o implements RecyclerView.x.b {
    private BitSet B;
    private boolean G;
    private boolean H;
    private SavedState I;
    private int J;
    private int[] O;

    /* renamed from: t  reason: collision with root package name */
    c[] f6731t;

    /* renamed from: u  reason: collision with root package name */
    u f6732u;

    /* renamed from: v  reason: collision with root package name */
    u f6733v;

    /* renamed from: w  reason: collision with root package name */
    private int f6734w;

    /* renamed from: x  reason: collision with root package name */
    private int f6735x;

    /* renamed from: y  reason: collision with root package name */
    private final o f6736y;

    /* renamed from: s  reason: collision with root package name */
    private int f6730s = -1;

    /* renamed from: z  reason: collision with root package name */
    boolean f6737z = false;
    boolean A = false;
    int C = -1;
    int D = Integer.MIN_VALUE;
    LazySpanLookup E = new LazySpanLookup();
    private int F = 2;
    private final Rect K = new Rect();
    private final b L = new b();
    private boolean M = false;
    private boolean N = true;
    private final Runnable P = new a();

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LayoutParams extends RecyclerView.LayoutParams {

        /* renamed from: e  reason: collision with root package name */
        c f6738e;

        /* renamed from: f  reason: collision with root package name */
        boolean f6739f;

        public LayoutParams(int i8, int i9) {
            super(i8, i9);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
        }

        public final int e() {
            c cVar = this.f6738e;
            if (cVar == null) {
                return -1;
            }
            return cVar.f6768e;
        }

        public boolean f() {
            return this.f6739f;
        }

        public void g(boolean z4) {
            this.f6739f = z4;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class LazySpanLookup {

        /* renamed from: a  reason: collision with root package name */
        int[] f6740a;

        /* renamed from: b  reason: collision with root package name */
        List<FullSpanItem> f6741b;

        /* JADX INFO: Access modifiers changed from: package-private */
        @SuppressLint({"BanParcelableUsage"})
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class FullSpanItem implements Parcelable {
            public static final Parcelable.Creator<FullSpanItem> CREATOR = new a();

            /* renamed from: a  reason: collision with root package name */
            int f6742a;

            /* renamed from: b  reason: collision with root package name */
            int f6743b;

            /* renamed from: c  reason: collision with root package name */
            int[] f6744c;

            /* renamed from: d  reason: collision with root package name */
            boolean f6745d;

            /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
            static class a implements Parcelable.Creator<FullSpanItem> {
                a() {
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: a */
                public FullSpanItem createFromParcel(Parcel parcel) {
                    return new FullSpanItem(parcel);
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: b */
                public FullSpanItem[] newArray(int i8) {
                    return new FullSpanItem[i8];
                }
            }

            FullSpanItem() {
            }

            FullSpanItem(Parcel parcel) {
                this.f6742a = parcel.readInt();
                this.f6743b = parcel.readInt();
                this.f6745d = parcel.readInt() == 1;
                int readInt = parcel.readInt();
                if (readInt > 0) {
                    int[] iArr = new int[readInt];
                    this.f6744c = iArr;
                    parcel.readIntArray(iArr);
                }
            }

            int a(int i8) {
                int[] iArr = this.f6744c;
                if (iArr == null) {
                    return 0;
                }
                return iArr[i8];
            }

            @Override // android.os.Parcelable
            public int describeContents() {
                return 0;
            }

            public String toString() {
                return "FullSpanItem{mPosition=" + this.f6742a + ", mGapDir=" + this.f6743b + ", mHasUnwantedGapAfter=" + this.f6745d + ", mGapPerSpan=" + Arrays.toString(this.f6744c) + '}';
            }

            @Override // android.os.Parcelable
            public void writeToParcel(Parcel parcel, int i8) {
                parcel.writeInt(this.f6742a);
                parcel.writeInt(this.f6743b);
                parcel.writeInt(this.f6745d ? 1 : 0);
                int[] iArr = this.f6744c;
                if (iArr == null || iArr.length <= 0) {
                    parcel.writeInt(0);
                    return;
                }
                parcel.writeInt(iArr.length);
                parcel.writeIntArray(this.f6744c);
            }
        }

        LazySpanLookup() {
        }

        private int i(int i8) {
            if (this.f6741b == null) {
                return -1;
            }
            FullSpanItem f5 = f(i8);
            if (f5 != null) {
                this.f6741b.remove(f5);
            }
            int size = this.f6741b.size();
            int i9 = 0;
            while (true) {
                if (i9 >= size) {
                    i9 = -1;
                    break;
                } else if (this.f6741b.get(i9).f6742a >= i8) {
                    break;
                } else {
                    i9++;
                }
            }
            if (i9 != -1) {
                this.f6741b.remove(i9);
                return this.f6741b.get(i9).f6742a;
            }
            return -1;
        }

        private void l(int i8, int i9) {
            List<FullSpanItem> list = this.f6741b;
            if (list == null) {
                return;
            }
            for (int size = list.size() - 1; size >= 0; size--) {
                FullSpanItem fullSpanItem = this.f6741b.get(size);
                int i10 = fullSpanItem.f6742a;
                if (i10 >= i8) {
                    fullSpanItem.f6742a = i10 + i9;
                }
            }
        }

        private void m(int i8, int i9) {
            List<FullSpanItem> list = this.f6741b;
            if (list == null) {
                return;
            }
            int i10 = i8 + i9;
            for (int size = list.size() - 1; size >= 0; size--) {
                FullSpanItem fullSpanItem = this.f6741b.get(size);
                int i11 = fullSpanItem.f6742a;
                if (i11 >= i8) {
                    if (i11 < i10) {
                        this.f6741b.remove(size);
                    } else {
                        fullSpanItem.f6742a = i11 - i9;
                    }
                }
            }
        }

        public void a(FullSpanItem fullSpanItem) {
            if (this.f6741b == null) {
                this.f6741b = new ArrayList();
            }
            int size = this.f6741b.size();
            for (int i8 = 0; i8 < size; i8++) {
                FullSpanItem fullSpanItem2 = this.f6741b.get(i8);
                if (fullSpanItem2.f6742a == fullSpanItem.f6742a) {
                    this.f6741b.remove(i8);
                }
                if (fullSpanItem2.f6742a >= fullSpanItem.f6742a) {
                    this.f6741b.add(i8, fullSpanItem);
                    return;
                }
            }
            this.f6741b.add(fullSpanItem);
        }

        void b() {
            int[] iArr = this.f6740a;
            if (iArr != null) {
                Arrays.fill(iArr, -1);
            }
            this.f6741b = null;
        }

        void c(int i8) {
            int[] iArr = this.f6740a;
            if (iArr == null) {
                int[] iArr2 = new int[Math.max(i8, 10) + 1];
                this.f6740a = iArr2;
                Arrays.fill(iArr2, -1);
            } else if (i8 >= iArr.length) {
                int[] iArr3 = new int[o(i8)];
                this.f6740a = iArr3;
                System.arraycopy(iArr, 0, iArr3, 0, iArr.length);
                int[] iArr4 = this.f6740a;
                Arrays.fill(iArr4, iArr.length, iArr4.length, -1);
            }
        }

        int d(int i8) {
            List<FullSpanItem> list = this.f6741b;
            if (list != null) {
                for (int size = list.size() - 1; size >= 0; size--) {
                    if (this.f6741b.get(size).f6742a >= i8) {
                        this.f6741b.remove(size);
                    }
                }
            }
            return h(i8);
        }

        public FullSpanItem e(int i8, int i9, int i10, boolean z4) {
            List<FullSpanItem> list = this.f6741b;
            if (list == null) {
                return null;
            }
            int size = list.size();
            for (int i11 = 0; i11 < size; i11++) {
                FullSpanItem fullSpanItem = this.f6741b.get(i11);
                int i12 = fullSpanItem.f6742a;
                if (i12 >= i9) {
                    return null;
                }
                if (i12 >= i8 && (i10 == 0 || fullSpanItem.f6743b == i10 || (z4 && fullSpanItem.f6745d))) {
                    return fullSpanItem;
                }
            }
            return null;
        }

        public FullSpanItem f(int i8) {
            List<FullSpanItem> list = this.f6741b;
            if (list == null) {
                return null;
            }
            for (int size = list.size() - 1; size >= 0; size--) {
                FullSpanItem fullSpanItem = this.f6741b.get(size);
                if (fullSpanItem.f6742a == i8) {
                    return fullSpanItem;
                }
            }
            return null;
        }

        int g(int i8) {
            int[] iArr = this.f6740a;
            if (iArr == null || i8 >= iArr.length) {
                return -1;
            }
            return iArr[i8];
        }

        int h(int i8) {
            int[] iArr = this.f6740a;
            if (iArr != null && i8 < iArr.length) {
                int i9 = i(i8);
                if (i9 == -1) {
                    int[] iArr2 = this.f6740a;
                    Arrays.fill(iArr2, i8, iArr2.length, -1);
                    return this.f6740a.length;
                }
                int i10 = i9 + 1;
                Arrays.fill(this.f6740a, i8, i10, -1);
                return i10;
            }
            return -1;
        }

        void j(int i8, int i9) {
            int[] iArr = this.f6740a;
            if (iArr == null || i8 >= iArr.length) {
                return;
            }
            int i10 = i8 + i9;
            c(i10);
            int[] iArr2 = this.f6740a;
            System.arraycopy(iArr2, i8, iArr2, i10, (iArr2.length - i8) - i9);
            Arrays.fill(this.f6740a, i8, i10, -1);
            l(i8, i9);
        }

        void k(int i8, int i9) {
            int[] iArr = this.f6740a;
            if (iArr == null || i8 >= iArr.length) {
                return;
            }
            int i10 = i8 + i9;
            c(i10);
            int[] iArr2 = this.f6740a;
            System.arraycopy(iArr2, i10, iArr2, i8, (iArr2.length - i8) - i9);
            int[] iArr3 = this.f6740a;
            Arrays.fill(iArr3, iArr3.length - i9, iArr3.length, -1);
            m(i8, i9);
        }

        void n(int i8, c cVar) {
            c(i8);
            this.f6740a[i8] = cVar.f6768e;
        }

        int o(int i8) {
            int length = this.f6740a.length;
            while (length <= i8) {
                length *= 2;
            }
            return length;
        }
    }

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class SavedState implements Parcelable {
        public static final Parcelable.Creator<SavedState> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        int f6746a;

        /* renamed from: b  reason: collision with root package name */
        int f6747b;

        /* renamed from: c  reason: collision with root package name */
        int f6748c;

        /* renamed from: d  reason: collision with root package name */
        int[] f6749d;

        /* renamed from: e  reason: collision with root package name */
        int f6750e;

        /* renamed from: f  reason: collision with root package name */
        int[] f6751f;

        /* renamed from: g  reason: collision with root package name */
        List<LazySpanLookup.FullSpanItem> f6752g;

        /* renamed from: h  reason: collision with root package name */
        boolean f6753h;

        /* renamed from: j  reason: collision with root package name */
        boolean f6754j;

        /* renamed from: k  reason: collision with root package name */
        boolean f6755k;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        static class a implements Parcelable.Creator<SavedState> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public SavedState[] newArray(int i8) {
                return new SavedState[i8];
            }
        }

        public SavedState() {
        }

        SavedState(Parcel parcel) {
            this.f6746a = parcel.readInt();
            this.f6747b = parcel.readInt();
            int readInt = parcel.readInt();
            this.f6748c = readInt;
            if (readInt > 0) {
                int[] iArr = new int[readInt];
                this.f6749d = iArr;
                parcel.readIntArray(iArr);
            }
            int readInt2 = parcel.readInt();
            this.f6750e = readInt2;
            if (readInt2 > 0) {
                int[] iArr2 = new int[readInt2];
                this.f6751f = iArr2;
                parcel.readIntArray(iArr2);
            }
            this.f6753h = parcel.readInt() == 1;
            this.f6754j = parcel.readInt() == 1;
            this.f6755k = parcel.readInt() == 1;
            this.f6752g = parcel.readArrayList(LazySpanLookup.FullSpanItem.class.getClassLoader());
        }

        public SavedState(SavedState savedState) {
            this.f6748c = savedState.f6748c;
            this.f6746a = savedState.f6746a;
            this.f6747b = savedState.f6747b;
            this.f6749d = savedState.f6749d;
            this.f6750e = savedState.f6750e;
            this.f6751f = savedState.f6751f;
            this.f6753h = savedState.f6753h;
            this.f6754j = savedState.f6754j;
            this.f6755k = savedState.f6755k;
            this.f6752g = savedState.f6752g;
        }

        void a() {
            this.f6749d = null;
            this.f6748c = 0;
            this.f6746a = -1;
            this.f6747b = -1;
        }

        void b() {
            this.f6749d = null;
            this.f6748c = 0;
            this.f6750e = 0;
            this.f6751f = null;
            this.f6752g = null;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeInt(this.f6746a);
            parcel.writeInt(this.f6747b);
            parcel.writeInt(this.f6748c);
            if (this.f6748c > 0) {
                parcel.writeIntArray(this.f6749d);
            }
            parcel.writeInt(this.f6750e);
            if (this.f6750e > 0) {
                parcel.writeIntArray(this.f6751f);
            }
            parcel.writeInt(this.f6753h ? 1 : 0);
            parcel.writeInt(this.f6754j ? 1 : 0);
            parcel.writeInt(this.f6755k ? 1 : 0);
            parcel.writeList(this.f6752g);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        public void run() {
            StaggeredGridLayoutManager.this.T1();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class b {

        /* renamed from: a  reason: collision with root package name */
        int f6757a;

        /* renamed from: b  reason: collision with root package name */
        int f6758b;

        /* renamed from: c  reason: collision with root package name */
        boolean f6759c;

        /* renamed from: d  reason: collision with root package name */
        boolean f6760d;

        /* renamed from: e  reason: collision with root package name */
        boolean f6761e;

        /* renamed from: f  reason: collision with root package name */
        int[] f6762f;

        b() {
            c();
        }

        void a() {
            this.f6758b = this.f6759c ? StaggeredGridLayoutManager.this.f6732u.i() : StaggeredGridLayoutManager.this.f6732u.m();
        }

        void b(int i8) {
            this.f6758b = this.f6759c ? StaggeredGridLayoutManager.this.f6732u.i() - i8 : StaggeredGridLayoutManager.this.f6732u.m() + i8;
        }

        void c() {
            this.f6757a = -1;
            this.f6758b = Integer.MIN_VALUE;
            this.f6759c = false;
            this.f6760d = false;
            this.f6761e = false;
            int[] iArr = this.f6762f;
            if (iArr != null) {
                Arrays.fill(iArr, -1);
            }
        }

        void d(c[] cVarArr) {
            int length = cVarArr.length;
            int[] iArr = this.f6762f;
            if (iArr == null || iArr.length < length) {
                this.f6762f = new int[StaggeredGridLayoutManager.this.f6731t.length];
            }
            for (int i8 = 0; i8 < length; i8++) {
                this.f6762f[i8] = cVarArr[i8].p(Integer.MIN_VALUE);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c {

        /* renamed from: a  reason: collision with root package name */
        ArrayList<View> f6764a = new ArrayList<>();

        /* renamed from: b  reason: collision with root package name */
        int f6765b = Integer.MIN_VALUE;

        /* renamed from: c  reason: collision with root package name */
        int f6766c = Integer.MIN_VALUE;

        /* renamed from: d  reason: collision with root package name */
        int f6767d = 0;

        /* renamed from: e  reason: collision with root package name */
        final int f6768e;

        c(int i8) {
            this.f6768e = i8;
        }

        void a(View view) {
            LayoutParams n8 = n(view);
            n8.f6738e = this;
            this.f6764a.add(view);
            this.f6766c = Integer.MIN_VALUE;
            if (this.f6764a.size() == 1) {
                this.f6765b = Integer.MIN_VALUE;
            }
            if (n8.c() || n8.b()) {
                this.f6767d += StaggeredGridLayoutManager.this.f6732u.e(view);
            }
        }

        void b(boolean z4, int i8) {
            int l8 = z4 ? l(Integer.MIN_VALUE) : p(Integer.MIN_VALUE);
            e();
            if (l8 == Integer.MIN_VALUE) {
                return;
            }
            if (!z4 || l8 >= StaggeredGridLayoutManager.this.f6732u.i()) {
                if (z4 || l8 <= StaggeredGridLayoutManager.this.f6732u.m()) {
                    if (i8 != Integer.MIN_VALUE) {
                        l8 += i8;
                    }
                    this.f6766c = l8;
                    this.f6765b = l8;
                }
            }
        }

        void c() {
            LazySpanLookup.FullSpanItem f5;
            ArrayList<View> arrayList = this.f6764a;
            View view = arrayList.get(arrayList.size() - 1);
            LayoutParams n8 = n(view);
            this.f6766c = StaggeredGridLayoutManager.this.f6732u.d(view);
            if (n8.f6739f && (f5 = StaggeredGridLayoutManager.this.E.f(n8.a())) != null && f5.f6743b == 1) {
                this.f6766c += f5.a(this.f6768e);
            }
        }

        void d() {
            LazySpanLookup.FullSpanItem f5;
            View view = this.f6764a.get(0);
            LayoutParams n8 = n(view);
            this.f6765b = StaggeredGridLayoutManager.this.f6732u.g(view);
            if (n8.f6739f && (f5 = StaggeredGridLayoutManager.this.E.f(n8.a())) != null && f5.f6743b == -1) {
                this.f6765b -= f5.a(this.f6768e);
            }
        }

        void e() {
            this.f6764a.clear();
            q();
            this.f6767d = 0;
        }

        public int f() {
            int i8;
            int size;
            if (StaggeredGridLayoutManager.this.f6737z) {
                i8 = this.f6764a.size() - 1;
                size = -1;
            } else {
                i8 = 0;
                size = this.f6764a.size();
            }
            return i(i8, size, true);
        }

        public int g() {
            int size;
            int i8;
            if (StaggeredGridLayoutManager.this.f6737z) {
                size = 0;
                i8 = this.f6764a.size();
            } else {
                size = this.f6764a.size() - 1;
                i8 = -1;
            }
            return i(size, i8, true);
        }

        int h(int i8, int i9, boolean z4, boolean z8, boolean z9) {
            int m8 = StaggeredGridLayoutManager.this.f6732u.m();
            int i10 = StaggeredGridLayoutManager.this.f6732u.i();
            int i11 = i9 > i8 ? 1 : -1;
            while (i8 != i9) {
                View view = this.f6764a.get(i8);
                int g8 = StaggeredGridLayoutManager.this.f6732u.g(view);
                int d8 = StaggeredGridLayoutManager.this.f6732u.d(view);
                boolean z10 = false;
                boolean z11 = !z9 ? g8 >= i10 : g8 > i10;
                if (!z9 ? d8 > m8 : d8 >= m8) {
                    z10 = true;
                }
                if (z11 && z10) {
                    if (!z4 || !z8) {
                        if (!z8 && g8 >= m8 && d8 <= i10) {
                        }
                        return StaggeredGridLayoutManager.this.i0(view);
                    } else if (g8 >= m8 && d8 <= i10) {
                        return StaggeredGridLayoutManager.this.i0(view);
                    }
                }
                i8 += i11;
            }
            return -1;
        }

        int i(int i8, int i9, boolean z4) {
            return h(i8, i9, false, false, z4);
        }

        public int j() {
            return this.f6767d;
        }

        int k() {
            int i8 = this.f6766c;
            if (i8 != Integer.MIN_VALUE) {
                return i8;
            }
            c();
            return this.f6766c;
        }

        int l(int i8) {
            int i9 = this.f6766c;
            if (i9 != Integer.MIN_VALUE) {
                return i9;
            }
            if (this.f6764a.size() == 0) {
                return i8;
            }
            c();
            return this.f6766c;
        }

        public View m(int i8, int i9) {
            View view = null;
            if (i9 != -1) {
                int size = this.f6764a.size() - 1;
                while (size >= 0) {
                    View view2 = this.f6764a.get(size);
                    StaggeredGridLayoutManager staggeredGridLayoutManager = StaggeredGridLayoutManager.this;
                    if (staggeredGridLayoutManager.f6737z && staggeredGridLayoutManager.i0(view2) >= i8) {
                        break;
                    }
                    StaggeredGridLayoutManager staggeredGridLayoutManager2 = StaggeredGridLayoutManager.this;
                    if ((!staggeredGridLayoutManager2.f6737z && staggeredGridLayoutManager2.i0(view2) <= i8) || !view2.hasFocusable()) {
                        break;
                    }
                    size--;
                    view = view2;
                }
            } else {
                int size2 = this.f6764a.size();
                int i10 = 0;
                while (i10 < size2) {
                    View view3 = this.f6764a.get(i10);
                    StaggeredGridLayoutManager staggeredGridLayoutManager3 = StaggeredGridLayoutManager.this;
                    if (staggeredGridLayoutManager3.f6737z && staggeredGridLayoutManager3.i0(view3) <= i8) {
                        break;
                    }
                    StaggeredGridLayoutManager staggeredGridLayoutManager4 = StaggeredGridLayoutManager.this;
                    if ((!staggeredGridLayoutManager4.f6737z && staggeredGridLayoutManager4.i0(view3) >= i8) || !view3.hasFocusable()) {
                        break;
                    }
                    i10++;
                    view = view3;
                }
            }
            return view;
        }

        LayoutParams n(View view) {
            return (LayoutParams) view.getLayoutParams();
        }

        int o() {
            int i8 = this.f6765b;
            if (i8 != Integer.MIN_VALUE) {
                return i8;
            }
            d();
            return this.f6765b;
        }

        int p(int i8) {
            int i9 = this.f6765b;
            if (i9 != Integer.MIN_VALUE) {
                return i9;
            }
            if (this.f6764a.size() == 0) {
                return i8;
            }
            d();
            return this.f6765b;
        }

        void q() {
            this.f6765b = Integer.MIN_VALUE;
            this.f6766c = Integer.MIN_VALUE;
        }

        void r(int i8) {
            int i9 = this.f6765b;
            if (i9 != Integer.MIN_VALUE) {
                this.f6765b = i9 + i8;
            }
            int i10 = this.f6766c;
            if (i10 != Integer.MIN_VALUE) {
                this.f6766c = i10 + i8;
            }
        }

        void s() {
            int size = this.f6764a.size();
            View remove = this.f6764a.remove(size - 1);
            LayoutParams n8 = n(remove);
            n8.f6738e = null;
            if (n8.c() || n8.b()) {
                this.f6767d -= StaggeredGridLayoutManager.this.f6732u.e(remove);
            }
            if (size == 1) {
                this.f6765b = Integer.MIN_VALUE;
            }
            this.f6766c = Integer.MIN_VALUE;
        }

        void t() {
            View remove = this.f6764a.remove(0);
            LayoutParams n8 = n(remove);
            n8.f6738e = null;
            if (this.f6764a.size() == 0) {
                this.f6766c = Integer.MIN_VALUE;
            }
            if (n8.c() || n8.b()) {
                this.f6767d -= StaggeredGridLayoutManager.this.f6732u.e(remove);
            }
            this.f6765b = Integer.MIN_VALUE;
        }

        void u(View view) {
            LayoutParams n8 = n(view);
            n8.f6738e = this;
            this.f6764a.add(0, view);
            this.f6765b = Integer.MIN_VALUE;
            if (this.f6764a.size() == 1) {
                this.f6766c = Integer.MIN_VALUE;
            }
            if (n8.c() || n8.b()) {
                this.f6767d += StaggeredGridLayoutManager.this.f6732u.e(view);
            }
        }

        void v(int i8) {
            this.f6765b = i8;
            this.f6766c = i8;
        }
    }

    public StaggeredGridLayoutManager(Context context, AttributeSet attributeSet, int i8, int i9) {
        RecyclerView.o.d j02 = RecyclerView.o.j0(context, attributeSet, i8, i9);
        I2(j02.f6680a);
        K2(j02.f6681b);
        J2(j02.f6682c);
        this.f6736y = new o();
        b2();
    }

    private void A2(View view) {
        for (int i8 = this.f6730s - 1; i8 >= 0; i8--) {
            this.f6731t[i8].u(view);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0010, code lost:
        if (r4.f7008e == (-1)) goto L9;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void B2(androidx.recyclerview.widget.RecyclerView.u r3, androidx.recyclerview.widget.o r4) {
        /*
            r2 = this;
            boolean r0 = r4.f7004a
            if (r0 == 0) goto L4d
            boolean r0 = r4.f7012i
            if (r0 == 0) goto L9
            goto L4d
        L9:
            int r0 = r4.f7005b
            r1 = -1
            if (r0 != 0) goto L1e
            int r0 = r4.f7008e
            if (r0 != r1) goto L18
        L12:
            int r4 = r4.f7010g
        L14:
            r2.C2(r3, r4)
            goto L4d
        L18:
            int r4 = r4.f7009f
        L1a:
            r2.D2(r3, r4)
            goto L4d
        L1e:
            int r0 = r4.f7008e
            if (r0 != r1) goto L37
            int r0 = r4.f7009f
            int r1 = r2.n2(r0)
            int r0 = r0 - r1
            if (r0 >= 0) goto L2c
            goto L12
        L2c:
            int r1 = r4.f7010g
            int r4 = r4.f7005b
            int r4 = java.lang.Math.min(r0, r4)
            int r4 = r1 - r4
            goto L14
        L37:
            int r0 = r4.f7010g
            int r0 = r2.o2(r0)
            int r1 = r4.f7010g
            int r0 = r0 - r1
            if (r0 >= 0) goto L43
            goto L18
        L43:
            int r1 = r4.f7009f
            int r4 = r4.f7005b
            int r4 = java.lang.Math.min(r0, r4)
            int r4 = r4 + r1
            goto L1a
        L4d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.StaggeredGridLayoutManager.B2(androidx.recyclerview.widget.RecyclerView$u, androidx.recyclerview.widget.o):void");
    }

    private void C2(RecyclerView.u uVar, int i8) {
        for (int K = K() - 1; K >= 0; K--) {
            View J = J(K);
            if (this.f6732u.g(J) < i8 || this.f6732u.q(J) < i8) {
                return;
            }
            LayoutParams layoutParams = (LayoutParams) J.getLayoutParams();
            if (layoutParams.f6739f) {
                for (int i9 = 0; i9 < this.f6730s; i9++) {
                    if (this.f6731t[i9].f6764a.size() == 1) {
                        return;
                    }
                }
                for (int i10 = 0; i10 < this.f6730s; i10++) {
                    this.f6731t[i10].s();
                }
            } else if (layoutParams.f6738e.f6764a.size() == 1) {
                return;
            } else {
                layoutParams.f6738e.s();
            }
            n1(J, uVar);
        }
    }

    private void D2(RecyclerView.u uVar, int i8) {
        while (K() > 0) {
            View J = J(0);
            if (this.f6732u.d(J) > i8 || this.f6732u.p(J) > i8) {
                return;
            }
            LayoutParams layoutParams = (LayoutParams) J.getLayoutParams();
            if (layoutParams.f6739f) {
                for (int i9 = 0; i9 < this.f6730s; i9++) {
                    if (this.f6731t[i9].f6764a.size() == 1) {
                        return;
                    }
                }
                for (int i10 = 0; i10 < this.f6730s; i10++) {
                    this.f6731t[i10].t();
                }
            } else if (layoutParams.f6738e.f6764a.size() == 1) {
                return;
            } else {
                layoutParams.f6738e.t();
            }
            n1(J, uVar);
        }
    }

    private void E2() {
        if (this.f6733v.k() == 1073741824) {
            return;
        }
        float f5 = 0.0f;
        int K = K();
        for (int i8 = 0; i8 < K; i8++) {
            View J = J(i8);
            float e8 = this.f6733v.e(J);
            if (e8 >= f5) {
                if (((LayoutParams) J.getLayoutParams()).f()) {
                    e8 = (e8 * 1.0f) / this.f6730s;
                }
                f5 = Math.max(f5, e8);
            }
        }
        int i9 = this.f6735x;
        int round = Math.round(f5 * this.f6730s);
        if (this.f6733v.k() == Integer.MIN_VALUE) {
            round = Math.min(round, this.f6733v.n());
        }
        Q2(round);
        if (this.f6735x == i9) {
            return;
        }
        for (int i10 = 0; i10 < K; i10++) {
            View J2 = J(i10);
            LayoutParams layoutParams = (LayoutParams) J2.getLayoutParams();
            if (!layoutParams.f6739f) {
                if (u2() && this.f6734w == 1) {
                    int i11 = this.f6730s;
                    int i12 = layoutParams.f6738e.f6768e;
                    J2.offsetLeftAndRight(((-((i11 - 1) - i12)) * this.f6735x) - ((-((i11 - 1) - i12)) * i9));
                } else {
                    int i13 = layoutParams.f6738e.f6768e;
                    int i14 = this.f6734w;
                    int i15 = (this.f6735x * i13) - (i13 * i9);
                    if (i14 == 1) {
                        J2.offsetLeftAndRight(i15);
                    } else {
                        J2.offsetTopAndBottom(i15);
                    }
                }
            }
        }
    }

    private void F2() {
        this.A = (this.f6734w == 1 || !u2()) ? this.f6737z : !this.f6737z;
    }

    private void H2(int i8) {
        o oVar = this.f6736y;
        oVar.f7008e = i8;
        oVar.f7007d = this.A != (i8 == -1) ? -1 : 1;
    }

    private void L2(int i8, int i9) {
        for (int i10 = 0; i10 < this.f6730s; i10++) {
            if (!this.f6731t[i10].f6764a.isEmpty()) {
                R2(this.f6731t[i10], i8, i9);
            }
        }
    }

    private boolean M2(RecyclerView.y yVar, b bVar) {
        boolean z4 = this.G;
        int b9 = yVar.b();
        bVar.f6757a = z4 ? h2(b9) : d2(b9);
        bVar.f6758b = Integer.MIN_VALUE;
        return true;
    }

    private void N1(View view) {
        for (int i8 = this.f6730s - 1; i8 >= 0; i8--) {
            this.f6731t[i8].a(view);
        }
    }

    private void O1(b bVar) {
        boolean z4;
        SavedState savedState = this.I;
        int i8 = savedState.f6748c;
        if (i8 > 0) {
            if (i8 == this.f6730s) {
                for (int i9 = 0; i9 < this.f6730s; i9++) {
                    this.f6731t[i9].e();
                    SavedState savedState2 = this.I;
                    int i10 = savedState2.f6749d[i9];
                    if (i10 != Integer.MIN_VALUE) {
                        i10 += savedState2.f6754j ? this.f6732u.i() : this.f6732u.m();
                    }
                    this.f6731t[i9].v(i10);
                }
            } else {
                savedState.b();
                SavedState savedState3 = this.I;
                savedState3.f6746a = savedState3.f6747b;
            }
        }
        SavedState savedState4 = this.I;
        this.H = savedState4.f6755k;
        J2(savedState4.f6753h);
        F2();
        SavedState savedState5 = this.I;
        int i11 = savedState5.f6746a;
        if (i11 != -1) {
            this.C = i11;
            z4 = savedState5.f6754j;
        } else {
            z4 = this.A;
        }
        bVar.f6759c = z4;
        if (savedState5.f6750e > 1) {
            LazySpanLookup lazySpanLookup = this.E;
            lazySpanLookup.f6740a = savedState5.f6751f;
            lazySpanLookup.f6741b = savedState5.f6752g;
        }
    }

    private void P2(int i8, RecyclerView.y yVar) {
        int i9;
        int i10;
        int c9;
        o oVar = this.f6736y;
        boolean z4 = false;
        oVar.f7005b = 0;
        oVar.f7006c = i8;
        if (!y0() || (c9 = yVar.c()) == -1) {
            i9 = 0;
            i10 = 0;
        } else {
            if (this.A == (c9 < i8)) {
                i9 = this.f6732u.n();
                i10 = 0;
            } else {
                i10 = this.f6732u.n();
                i9 = 0;
            }
        }
        if (N()) {
            this.f6736y.f7009f = this.f6732u.m() - i10;
            this.f6736y.f7010g = this.f6732u.i() + i9;
        } else {
            this.f6736y.f7010g = this.f6732u.h() + i9;
            this.f6736y.f7009f = -i10;
        }
        o oVar2 = this.f6736y;
        oVar2.f7011h = false;
        oVar2.f7004a = true;
        if (this.f6732u.k() == 0 && this.f6732u.h() == 0) {
            z4 = true;
        }
        oVar2.f7012i = z4;
    }

    private void R1(View view, LayoutParams layoutParams, o oVar) {
        if (oVar.f7008e == 1) {
            if (layoutParams.f6739f) {
                N1(view);
            } else {
                layoutParams.f6738e.a(view);
            }
        } else if (layoutParams.f6739f) {
            A2(view);
        } else {
            layoutParams.f6738e.u(view);
        }
    }

    private void R2(c cVar, int i8, int i9) {
        int j8 = cVar.j();
        if (i8 == -1) {
            if (cVar.o() + j8 > i9) {
                return;
            }
        } else if (cVar.k() - j8 < i9) {
            return;
        }
        this.B.set(cVar.f6768e, false);
    }

    private int S1(int i8) {
        if (K() == 0) {
            return this.A ? 1 : -1;
        }
        return (i8 < k2()) != this.A ? -1 : 1;
    }

    private int S2(int i8, int i9, int i10) {
        if (i9 == 0 && i10 == 0) {
            return i8;
        }
        int mode = View.MeasureSpec.getMode(i8);
        return (mode == Integer.MIN_VALUE || mode == 1073741824) ? View.MeasureSpec.makeMeasureSpec(Math.max(0, (View.MeasureSpec.getSize(i8) - i9) - i10), mode) : i8;
    }

    private boolean U1(c cVar) {
        if (this.A) {
            if (cVar.k() < this.f6732u.i()) {
                ArrayList<View> arrayList = cVar.f6764a;
                return !cVar.n(arrayList.get(arrayList.size() - 1)).f6739f;
            }
        } else if (cVar.o() > this.f6732u.m()) {
            return !cVar.n(cVar.f6764a.get(0)).f6739f;
        }
        return false;
    }

    private int V1(RecyclerView.y yVar) {
        if (K() == 0) {
            return 0;
        }
        return x.a(yVar, this.f6732u, f2(!this.N), e2(!this.N), this, this.N);
    }

    private int W1(RecyclerView.y yVar) {
        if (K() == 0) {
            return 0;
        }
        return x.b(yVar, this.f6732u, f2(!this.N), e2(!this.N), this, this.N, this.A);
    }

    private int X1(RecyclerView.y yVar) {
        if (K() == 0) {
            return 0;
        }
        return x.c(yVar, this.f6732u, f2(!this.N), e2(!this.N), this, this.N);
    }

    private int Y1(int i8) {
        return i8 != 1 ? i8 != 2 ? i8 != 17 ? i8 != 33 ? i8 != 66 ? (i8 == 130 && this.f6734w == 1) ? 1 : Integer.MIN_VALUE : this.f6734w == 0 ? 1 : Integer.MIN_VALUE : this.f6734w == 1 ? -1 : Integer.MIN_VALUE : this.f6734w == 0 ? -1 : Integer.MIN_VALUE : (this.f6734w != 1 && u2()) ? -1 : 1 : (this.f6734w != 1 && u2()) ? 1 : -1;
    }

    private LazySpanLookup.FullSpanItem Z1(int i8) {
        LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
        fullSpanItem.f6744c = new int[this.f6730s];
        for (int i9 = 0; i9 < this.f6730s; i9++) {
            fullSpanItem.f6744c[i9] = i8 - this.f6731t[i9].l(i8);
        }
        return fullSpanItem;
    }

    private LazySpanLookup.FullSpanItem a2(int i8) {
        LazySpanLookup.FullSpanItem fullSpanItem = new LazySpanLookup.FullSpanItem();
        fullSpanItem.f6744c = new int[this.f6730s];
        for (int i9 = 0; i9 < this.f6730s; i9++) {
            fullSpanItem.f6744c[i9] = this.f6731t[i9].p(i8) - i8;
        }
        return fullSpanItem;
    }

    private void b2() {
        this.f6732u = u.b(this, this.f6734w);
        this.f6733v = u.b(this, 1 - this.f6734w);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r9v0 */
    /* JADX WARN: Type inference failed for: r9v1, types: [int, boolean] */
    /* JADX WARN: Type inference failed for: r9v7 */
    private int c2(RecyclerView.u uVar, o oVar, RecyclerView.y yVar) {
        c cVar;
        int e8;
        int i8;
        int i9;
        int e9;
        RecyclerView.o oVar2;
        View view;
        int i10;
        int i11;
        boolean z4;
        ?? r9 = 0;
        this.B.set(0, this.f6730s, true);
        int i12 = this.f6736y.f7012i ? oVar.f7008e == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE : oVar.f7008e == 1 ? oVar.f7010g + oVar.f7005b : oVar.f7009f - oVar.f7005b;
        L2(oVar.f7008e, i12);
        int i13 = this.A ? this.f6732u.i() : this.f6732u.m();
        boolean z8 = false;
        while (oVar.a(yVar) && (this.f6736y.f7012i || !this.B.isEmpty())) {
            View b9 = oVar.b(uVar);
            LayoutParams layoutParams = (LayoutParams) b9.getLayoutParams();
            int a9 = layoutParams.a();
            int g8 = this.E.g(a9);
            boolean z9 = g8 == -1 ? true : r9;
            if (z9) {
                cVar = layoutParams.f6739f ? this.f6731t[r9] : q2(oVar);
                this.E.n(a9, cVar);
            } else {
                cVar = this.f6731t[g8];
            }
            c cVar2 = cVar;
            layoutParams.f6738e = cVar2;
            if (oVar.f7008e == 1) {
                e(b9);
            } else {
                f(b9, r9);
            }
            w2(b9, layoutParams, r9);
            if (oVar.f7008e == 1) {
                int m22 = layoutParams.f6739f ? m2(i13) : cVar2.l(i13);
                int e10 = this.f6732u.e(b9) + m22;
                if (z9 && layoutParams.f6739f) {
                    LazySpanLookup.FullSpanItem Z1 = Z1(m22);
                    Z1.f6743b = -1;
                    Z1.f6742a = a9;
                    this.E.a(Z1);
                }
                i8 = e10;
                e8 = m22;
            } else {
                int p22 = layoutParams.f6739f ? p2(i13) : cVar2.p(i13);
                e8 = p22 - this.f6732u.e(b9);
                if (z9 && layoutParams.f6739f) {
                    LazySpanLookup.FullSpanItem a22 = a2(p22);
                    a22.f6743b = 1;
                    a22.f6742a = a9;
                    this.E.a(a22);
                }
                i8 = p22;
            }
            if (layoutParams.f6739f && oVar.f7007d == -1) {
                if (!z9) {
                    if (!(oVar.f7008e == 1 ? P1() : Q1())) {
                        LazySpanLookup.FullSpanItem f5 = this.E.f(a9);
                        if (f5 != null) {
                            f5.f6745d = true;
                        }
                    }
                }
                this.M = true;
            }
            R1(b9, layoutParams, oVar);
            if (u2() && this.f6734w == 1) {
                int i14 = layoutParams.f6739f ? this.f6733v.i() : this.f6733v.i() - (((this.f6730s - 1) - cVar2.f6768e) * this.f6735x);
                e9 = i14;
                i9 = i14 - this.f6733v.e(b9);
            } else {
                int m8 = layoutParams.f6739f ? this.f6733v.m() : (cVar2.f6768e * this.f6735x) + this.f6733v.m();
                i9 = m8;
                e9 = this.f6733v.e(b9) + m8;
            }
            if (this.f6734w == 1) {
                oVar2 = this;
                view = b9;
                i10 = i9;
                i9 = e8;
                i11 = e9;
            } else {
                oVar2 = this;
                view = b9;
                i10 = e8;
                i11 = i8;
                i8 = e9;
            }
            oVar2.A0(view, i10, i9, i11, i8);
            if (layoutParams.f6739f) {
                L2(this.f6736y.f7008e, i12);
            } else {
                R2(cVar2, this.f6736y.f7008e, i12);
            }
            B2(uVar, this.f6736y);
            if (this.f6736y.f7011h && b9.hasFocusable()) {
                if (layoutParams.f6739f) {
                    this.B.clear();
                } else {
                    z4 = false;
                    this.B.set(cVar2.f6768e, false);
                    r9 = z4;
                    z8 = true;
                }
            }
            z4 = false;
            r9 = z4;
            z8 = true;
        }
        int i15 = r9;
        if (!z8) {
            B2(uVar, this.f6736y);
        }
        int m9 = this.f6736y.f7008e == -1 ? this.f6732u.m() - p2(this.f6732u.m()) : m2(this.f6732u.i()) - this.f6732u.i();
        return m9 > 0 ? Math.min(oVar.f7005b, m9) : i15;
    }

    private int d2(int i8) {
        int K = K();
        for (int i9 = 0; i9 < K; i9++) {
            int i02 = i0(J(i9));
            if (i02 >= 0 && i02 < i8) {
                return i02;
            }
        }
        return 0;
    }

    private int h2(int i8) {
        for (int K = K() - 1; K >= 0; K--) {
            int i02 = i0(J(K));
            if (i02 >= 0 && i02 < i8) {
                return i02;
            }
        }
        return 0;
    }

    private void i2(RecyclerView.u uVar, RecyclerView.y yVar, boolean z4) {
        int i8;
        int m22 = m2(Integer.MIN_VALUE);
        if (m22 != Integer.MIN_VALUE && (i8 = this.f6732u.i() - m22) > 0) {
            int i9 = i8 - (-G2(-i8, uVar, yVar));
            if (!z4 || i9 <= 0) {
                return;
            }
            this.f6732u.r(i9);
        }
    }

    private void j2(RecyclerView.u uVar, RecyclerView.y yVar, boolean z4) {
        int m8;
        int p22 = p2(Integer.MAX_VALUE);
        if (p22 != Integer.MAX_VALUE && (m8 = p22 - this.f6732u.m()) > 0) {
            int G2 = m8 - G2(m8, uVar, yVar);
            if (!z4 || G2 <= 0) {
                return;
            }
            this.f6732u.r(-G2);
        }
    }

    private int m2(int i8) {
        int l8 = this.f6731t[0].l(i8);
        for (int i9 = 1; i9 < this.f6730s; i9++) {
            int l9 = this.f6731t[i9].l(i8);
            if (l9 > l8) {
                l8 = l9;
            }
        }
        return l8;
    }

    private int n2(int i8) {
        int p8 = this.f6731t[0].p(i8);
        for (int i9 = 1; i9 < this.f6730s; i9++) {
            int p9 = this.f6731t[i9].p(i8);
            if (p9 > p8) {
                p8 = p9;
            }
        }
        return p8;
    }

    private int o2(int i8) {
        int l8 = this.f6731t[0].l(i8);
        for (int i9 = 1; i9 < this.f6730s; i9++) {
            int l9 = this.f6731t[i9].l(i8);
            if (l9 < l8) {
                l8 = l9;
            }
        }
        return l8;
    }

    private int p2(int i8) {
        int p8 = this.f6731t[0].p(i8);
        for (int i9 = 1; i9 < this.f6730s; i9++) {
            int p9 = this.f6731t[i9].p(i8);
            if (p9 < p8) {
                p8 = p9;
            }
        }
        return p8;
    }

    private c q2(o oVar) {
        int i8;
        int i9;
        int i10 = -1;
        if (y2(oVar.f7008e)) {
            i8 = this.f6730s - 1;
            i9 = -1;
        } else {
            i8 = 0;
            i10 = this.f6730s;
            i9 = 1;
        }
        c cVar = null;
        if (oVar.f7008e == 1) {
            int i11 = Integer.MAX_VALUE;
            int m8 = this.f6732u.m();
            while (i8 != i10) {
                c cVar2 = this.f6731t[i8];
                int l8 = cVar2.l(m8);
                if (l8 < i11) {
                    cVar = cVar2;
                    i11 = l8;
                }
                i8 += i9;
            }
            return cVar;
        }
        int i12 = Integer.MIN_VALUE;
        int i13 = this.f6732u.i();
        while (i8 != i10) {
            c cVar3 = this.f6731t[i8];
            int p8 = cVar3.p(i13);
            if (p8 > i12) {
                cVar = cVar3;
                i12 = p8;
            }
            i8 += i9;
        }
        return cVar;
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x003c  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0043 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0044  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void r2(int r7, int r8, int r9) {
        /*
            r6 = this;
            boolean r0 = r6.A
            if (r0 == 0) goto L9
            int r0 = r6.l2()
            goto Ld
        L9:
            int r0 = r6.k2()
        Ld:
            r1 = 8
            if (r9 != r1) goto L1a
            if (r7 >= r8) goto L16
            int r2 = r8 + 1
            goto L1c
        L16:
            int r2 = r7 + 1
            r3 = r8
            goto L1d
        L1a:
            int r2 = r7 + r8
        L1c:
            r3 = r7
        L1d:
            androidx.recyclerview.widget.StaggeredGridLayoutManager$LazySpanLookup r4 = r6.E
            r4.h(r3)
            r4 = 1
            if (r9 == r4) goto L3c
            r5 = 2
            if (r9 == r5) goto L36
            if (r9 == r1) goto L2b
            goto L41
        L2b:
            androidx.recyclerview.widget.StaggeredGridLayoutManager$LazySpanLookup r9 = r6.E
            r9.k(r7, r4)
            androidx.recyclerview.widget.StaggeredGridLayoutManager$LazySpanLookup r7 = r6.E
            r7.j(r8, r4)
            goto L41
        L36:
            androidx.recyclerview.widget.StaggeredGridLayoutManager$LazySpanLookup r9 = r6.E
            r9.k(r7, r8)
            goto L41
        L3c:
            androidx.recyclerview.widget.StaggeredGridLayoutManager$LazySpanLookup r9 = r6.E
            r9.j(r7, r8)
        L41:
            if (r2 > r0) goto L44
            return
        L44:
            boolean r7 = r6.A
            if (r7 == 0) goto L4d
            int r7 = r6.k2()
            goto L51
        L4d:
            int r7 = r6.l2()
        L51:
            if (r3 > r7) goto L56
            r6.u1()
        L56:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.StaggeredGridLayoutManager.r2(int, int, int):void");
    }

    private void v2(View view, int i8, int i9, boolean z4) {
        k(view, this.K);
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i10 = ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
        Rect rect = this.K;
        int S2 = S2(i8, i10 + rect.left, ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + rect.right);
        int i11 = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
        Rect rect2 = this.K;
        int S22 = S2(i9, i11 + rect2.top, ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + rect2.bottom);
        if (z4 ? I1(view, S2, S22, layoutParams) : G1(view, S2, S22, layoutParams)) {
            view.measure(S2, S22);
        }
    }

    private void w2(View view, LayoutParams layoutParams, boolean z4) {
        int L;
        int L2;
        if (layoutParams.f6739f) {
            if (this.f6734w != 1) {
                v2(view, RecyclerView.o.L(p0(), q0(), f0() + g0(), ((ViewGroup.MarginLayoutParams) layoutParams).width, true), this.J, z4);
                return;
            }
            L = this.J;
        } else if (this.f6734w != 1) {
            L = RecyclerView.o.L(p0(), q0(), f0() + g0(), ((ViewGroup.MarginLayoutParams) layoutParams).width, true);
            L2 = RecyclerView.o.L(this.f6735x, Y(), 0, ((ViewGroup.MarginLayoutParams) layoutParams).height, false);
            v2(view, L, L2, z4);
        } else {
            L = RecyclerView.o.L(this.f6735x, q0(), 0, ((ViewGroup.MarginLayoutParams) layoutParams).width, false);
        }
        L2 = RecyclerView.o.L(X(), Y(), h0() + e0(), ((ViewGroup.MarginLayoutParams) layoutParams).height, true);
        v2(view, L, L2, z4);
    }

    /* JADX WARN: Code restructure failed: missing block: B:87:0x014b, code lost:
        if (T1() != false) goto L83;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void x2(androidx.recyclerview.widget.RecyclerView.u r9, androidx.recyclerview.widget.RecyclerView.y r10, boolean r11) {
        /*
            Method dump skipped, instructions count: 367
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.StaggeredGridLayoutManager.x2(androidx.recyclerview.widget.RecyclerView$u, androidx.recyclerview.widget.RecyclerView$y, boolean):void");
    }

    private boolean y2(int i8) {
        if (this.f6734w == 0) {
            return (i8 == -1) != this.A;
        }
        return ((i8 == -1) == this.A) == u2();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void D0(int i8) {
        super.D0(i8);
        for (int i9 = 0; i9 < this.f6730s; i9++) {
            this.f6731t[i9].r(i8);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void D1(Rect rect, int i8, int i9) {
        int o5;
        int o8;
        int f02 = f0() + g0();
        int h02 = h0() + e0();
        if (this.f6734w == 1) {
            o8 = RecyclerView.o.o(i9, rect.height() + h02, c0());
            o5 = RecyclerView.o.o(i8, (this.f6735x * this.f6730s) + f02, d0());
        } else {
            o5 = RecyclerView.o.o(i8, rect.width() + f02, d0());
            o8 = RecyclerView.o.o(i9, (this.f6735x * this.f6730s) + h02, c0());
        }
        C1(o5, o8);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public RecyclerView.LayoutParams E() {
        return this.f6734w == 0 ? new LayoutParams(-2, -1) : new LayoutParams(-1, -2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void E0(int i8) {
        super.E0(i8);
        for (int i9 = 0; i9 < this.f6730s; i9++) {
            this.f6731t[i9].r(i8);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public RecyclerView.LayoutParams F(Context context, AttributeSet attributeSet) {
        return new LayoutParams(context, attributeSet);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public RecyclerView.LayoutParams G(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof ViewGroup.MarginLayoutParams ? new LayoutParams((ViewGroup.MarginLayoutParams) layoutParams) : new LayoutParams(layoutParams);
    }

    int G2(int i8, RecyclerView.u uVar, RecyclerView.y yVar) {
        if (K() == 0 || i8 == 0) {
            return 0;
        }
        z2(i8, yVar);
        int c22 = c2(uVar, this.f6736y, yVar);
        if (this.f6736y.f7005b >= c22) {
            i8 = i8 < 0 ? -c22 : c22;
        }
        this.f6732u.r(-i8);
        this.G = this.A;
        o oVar = this.f6736y;
        oVar.f7005b = 0;
        B2(uVar, oVar);
        return i8;
    }

    public void I2(int i8) {
        if (i8 != 0 && i8 != 1) {
            throw new IllegalArgumentException("invalid orientation.");
        }
        h(null);
        if (i8 == this.f6734w) {
            return;
        }
        this.f6734w = i8;
        u uVar = this.f6732u;
        this.f6732u = this.f6733v;
        this.f6733v = uVar;
        u1();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void J0(RecyclerView recyclerView, RecyclerView.u uVar) {
        super.J0(recyclerView, uVar);
        p1(this.P);
        for (int i8 = 0; i8 < this.f6730s; i8++) {
            this.f6731t[i8].e();
        }
        recyclerView.requestLayout();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void J1(RecyclerView recyclerView, RecyclerView.y yVar, int i8) {
        p pVar = new p(recyclerView.getContext());
        pVar.p(i8);
        K1(pVar);
    }

    public void J2(boolean z4) {
        h(null);
        SavedState savedState = this.I;
        if (savedState != null && savedState.f6753h != z4) {
            savedState.f6753h = z4;
        }
        this.f6737z = z4;
        u1();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public View K0(View view, int i8, RecyclerView.u uVar, RecyclerView.y yVar) {
        View C;
        View m8;
        if (K() == 0 || (C = C(view)) == null) {
            return null;
        }
        F2();
        int Y1 = Y1(i8);
        if (Y1 == Integer.MIN_VALUE) {
            return null;
        }
        LayoutParams layoutParams = (LayoutParams) C.getLayoutParams();
        boolean z4 = layoutParams.f6739f;
        c cVar = layoutParams.f6738e;
        int l22 = Y1 == 1 ? l2() : k2();
        P2(l22, yVar);
        H2(Y1);
        o oVar = this.f6736y;
        oVar.f7006c = oVar.f7007d + l22;
        oVar.f7005b = (int) (this.f6732u.n() * 0.33333334f);
        o oVar2 = this.f6736y;
        oVar2.f7011h = true;
        oVar2.f7004a = false;
        c2(uVar, oVar2, yVar);
        this.G = this.A;
        if (z4 || (m8 = cVar.m(l22, Y1)) == null || m8 == C) {
            if (y2(Y1)) {
                for (int i9 = this.f6730s - 1; i9 >= 0; i9--) {
                    View m9 = this.f6731t[i9].m(l22, Y1);
                    if (m9 != null && m9 != C) {
                        return m9;
                    }
                }
            } else {
                for (int i10 = 0; i10 < this.f6730s; i10++) {
                    View m10 = this.f6731t[i10].m(l22, Y1);
                    if (m10 != null && m10 != C) {
                        return m10;
                    }
                }
            }
            boolean z8 = (this.f6737z ^ true) == (Y1 == -1);
            if (!z4) {
                View D = D(z8 ? cVar.f() : cVar.g());
                if (D != null && D != C) {
                    return D;
                }
            }
            if (y2(Y1)) {
                for (int i11 = this.f6730s - 1; i11 >= 0; i11--) {
                    if (i11 != cVar.f6768e) {
                        c[] cVarArr = this.f6731t;
                        View D2 = D(z8 ? cVarArr[i11].f() : cVarArr[i11].g());
                        if (D2 != null && D2 != C) {
                            return D2;
                        }
                    }
                }
            } else {
                for (int i12 = 0; i12 < this.f6730s; i12++) {
                    c[] cVarArr2 = this.f6731t;
                    View D3 = D(z8 ? cVarArr2[i12].f() : cVarArr2[i12].g());
                    if (D3 != null && D3 != C) {
                        return D3;
                    }
                }
            }
            return null;
        }
        return m8;
    }

    public void K2(int i8) {
        h(null);
        if (i8 != this.f6730s) {
            t2();
            this.f6730s = i8;
            this.B = new BitSet(this.f6730s);
            this.f6731t = new c[this.f6730s];
            for (int i9 = 0; i9 < this.f6730s; i9++) {
                this.f6731t[i9] = new c(i9);
            }
            u1();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void L0(AccessibilityEvent accessibilityEvent) {
        super.L0(accessibilityEvent);
        if (K() > 0) {
            View f22 = f2(false);
            View e22 = e2(false);
            if (f22 == null || e22 == null) {
                return;
            }
            int i02 = i0(f22);
            int i03 = i0(e22);
            if (i02 < i03) {
                accessibilityEvent.setFromIndex(i02);
                accessibilityEvent.setToIndex(i03);
                return;
            }
            accessibilityEvent.setFromIndex(i03);
            accessibilityEvent.setToIndex(i02);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public boolean M1() {
        return this.I == null;
    }

    boolean N2(RecyclerView.y yVar, b bVar) {
        int i8;
        int m8;
        int g8;
        if (!yVar.e() && (i8 = this.C) != -1) {
            if (i8 >= 0 && i8 < yVar.b()) {
                SavedState savedState = this.I;
                if (savedState == null || savedState.f6746a == -1 || savedState.f6748c < 1) {
                    View D = D(this.C);
                    if (D != null) {
                        bVar.f6757a = this.A ? l2() : k2();
                        if (this.D != Integer.MIN_VALUE) {
                            if (bVar.f6759c) {
                                m8 = this.f6732u.i() - this.D;
                                g8 = this.f6732u.d(D);
                            } else {
                                m8 = this.f6732u.m() + this.D;
                                g8 = this.f6732u.g(D);
                            }
                            bVar.f6758b = m8 - g8;
                            return true;
                        } else if (this.f6732u.e(D) > this.f6732u.n()) {
                            bVar.f6758b = bVar.f6759c ? this.f6732u.i() : this.f6732u.m();
                            return true;
                        } else {
                            int g9 = this.f6732u.g(D) - this.f6732u.m();
                            if (g9 < 0) {
                                bVar.f6758b = -g9;
                                return true;
                            }
                            int i9 = this.f6732u.i() - this.f6732u.d(D);
                            if (i9 < 0) {
                                bVar.f6758b = i9;
                                return true;
                            }
                            bVar.f6758b = Integer.MIN_VALUE;
                        }
                    } else {
                        int i10 = this.C;
                        bVar.f6757a = i10;
                        int i11 = this.D;
                        if (i11 == Integer.MIN_VALUE) {
                            bVar.f6759c = S1(i10) == 1;
                            bVar.a();
                        } else {
                            bVar.b(i11);
                        }
                        bVar.f6760d = true;
                    }
                } else {
                    bVar.f6758b = Integer.MIN_VALUE;
                    bVar.f6757a = this.C;
                }
                return true;
            }
            this.C = -1;
            this.D = Integer.MIN_VALUE;
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int O(RecyclerView.u uVar, RecyclerView.y yVar) {
        return this.f6734w == 1 ? this.f6730s : super.O(uVar, yVar);
    }

    void O2(RecyclerView.y yVar, b bVar) {
        if (N2(yVar, bVar) || M2(yVar, bVar)) {
            return;
        }
        bVar.a();
        bVar.f6757a = 0;
    }

    boolean P1() {
        int l8 = this.f6731t[0].l(Integer.MIN_VALUE);
        for (int i8 = 1; i8 < this.f6730s; i8++) {
            if (this.f6731t[i8].l(Integer.MIN_VALUE) != l8) {
                return false;
            }
        }
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void Q0(RecyclerView.u uVar, RecyclerView.y yVar, View view, androidx.core.view.accessibility.c cVar) {
        int i8;
        int i9;
        int e8;
        int i10;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (!(layoutParams instanceof LayoutParams)) {
            super.P0(view, cVar);
            return;
        }
        LayoutParams layoutParams2 = (LayoutParams) layoutParams;
        if (this.f6734w == 0) {
            i8 = layoutParams2.e();
            i9 = layoutParams2.f6739f ? this.f6730s : 1;
            e8 = -1;
            i10 = -1;
        } else {
            i8 = -1;
            i9 = -1;
            e8 = layoutParams2.e();
            i10 = layoutParams2.f6739f ? this.f6730s : 1;
        }
        cVar.f0(c.C0043c.a(i8, i9, e8, i10, false, false));
    }

    boolean Q1() {
        int p8 = this.f6731t[0].p(Integer.MIN_VALUE);
        for (int i8 = 1; i8 < this.f6730s; i8++) {
            if (this.f6731t[i8].p(Integer.MIN_VALUE) != p8) {
                return false;
            }
        }
        return true;
    }

    void Q2(int i8) {
        this.f6735x = i8 / this.f6730s;
        this.J = View.MeasureSpec.makeMeasureSpec(i8, this.f6733v.k());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void S0(RecyclerView recyclerView, int i8, int i9) {
        r2(i8, i9, 1);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void T0(RecyclerView recyclerView) {
        this.E.b();
        u1();
    }

    boolean T1() {
        int k22;
        int l22;
        if (K() == 0 || this.F == 0 || !s0()) {
            return false;
        }
        if (this.A) {
            k22 = l2();
            l22 = k2();
        } else {
            k22 = k2();
            l22 = l2();
        }
        if (k22 == 0 && s2() != null) {
            this.E.b();
        } else if (!this.M) {
            return false;
        } else {
            int i8 = this.A ? -1 : 1;
            int i9 = l22 + 1;
            LazySpanLookup.FullSpanItem e8 = this.E.e(k22, i9, i8, true);
            if (e8 == null) {
                this.M = false;
                this.E.d(i9);
                return false;
            }
            LazySpanLookup.FullSpanItem e9 = this.E.e(k22, e8.f6742a, i8 * (-1), true);
            if (e9 == null) {
                this.E.d(e8.f6742a);
            } else {
                this.E.d(e9.f6742a + 1);
            }
        }
        v1();
        u1();
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void U0(RecyclerView recyclerView, int i8, int i9, int i10) {
        r2(i8, i9, 8);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void V0(RecyclerView recyclerView, int i8, int i9) {
        r2(i8, i9, 2);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void X0(RecyclerView recyclerView, int i8, int i9, Object obj) {
        r2(i8, i9, 4);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void Y0(RecyclerView.u uVar, RecyclerView.y yVar) {
        x2(uVar, yVar, true);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void Z0(RecyclerView.y yVar) {
        super.Z0(yVar);
        this.C = -1;
        this.D = Integer.MIN_VALUE;
        this.I = null;
        this.L.c();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.x.b
    public PointF a(int i8) {
        int S1 = S1(i8);
        PointF pointF = new PointF();
        if (S1 == 0) {
            return null;
        }
        if (this.f6734w == 0) {
            pointF.x = S1;
            pointF.y = 0.0f;
        } else {
            pointF.x = 0.0f;
            pointF.y = S1;
        }
        return pointF;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void d1(Parcelable parcelable) {
        if (parcelable instanceof SavedState) {
            this.I = (SavedState) parcelable;
            u1();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public Parcelable e1() {
        int p8;
        int m8;
        int[] iArr;
        if (this.I != null) {
            return new SavedState(this.I);
        }
        SavedState savedState = new SavedState();
        savedState.f6753h = this.f6737z;
        savedState.f6754j = this.G;
        savedState.f6755k = this.H;
        LazySpanLookup lazySpanLookup = this.E;
        if (lazySpanLookup == null || (iArr = lazySpanLookup.f6740a) == null) {
            savedState.f6750e = 0;
        } else {
            savedState.f6751f = iArr;
            savedState.f6750e = iArr.length;
            savedState.f6752g = lazySpanLookup.f6741b;
        }
        if (K() > 0) {
            savedState.f6746a = this.G ? l2() : k2();
            savedState.f6747b = g2();
            int i8 = this.f6730s;
            savedState.f6748c = i8;
            savedState.f6749d = new int[i8];
            for (int i9 = 0; i9 < this.f6730s; i9++) {
                if (this.G) {
                    p8 = this.f6731t[i9].l(Integer.MIN_VALUE);
                    if (p8 != Integer.MIN_VALUE) {
                        m8 = this.f6732u.i();
                        p8 -= m8;
                        savedState.f6749d[i9] = p8;
                    } else {
                        savedState.f6749d[i9] = p8;
                    }
                } else {
                    p8 = this.f6731t[i9].p(Integer.MIN_VALUE);
                    if (p8 != Integer.MIN_VALUE) {
                        m8 = this.f6732u.m();
                        p8 -= m8;
                        savedState.f6749d[i9] = p8;
                    } else {
                        savedState.f6749d[i9] = p8;
                    }
                }
            }
        } else {
            savedState.f6746a = -1;
            savedState.f6747b = -1;
            savedState.f6748c = 0;
        }
        return savedState;
    }

    View e2(boolean z4) {
        int m8 = this.f6732u.m();
        int i8 = this.f6732u.i();
        View view = null;
        for (int K = K() - 1; K >= 0; K--) {
            View J = J(K);
            int g8 = this.f6732u.g(J);
            int d8 = this.f6732u.d(J);
            if (d8 > m8 && g8 < i8) {
                if (d8 <= i8 || !z4) {
                    return J;
                }
                if (view == null) {
                    view = J;
                }
            }
        }
        return view;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void f1(int i8) {
        if (i8 == 0) {
            T1();
        }
    }

    View f2(boolean z4) {
        int m8 = this.f6732u.m();
        int i8 = this.f6732u.i();
        int K = K();
        View view = null;
        for (int i9 = 0; i9 < K; i9++) {
            View J = J(i9);
            int g8 = this.f6732u.g(J);
            if (this.f6732u.d(J) > m8 && g8 < i8) {
                if (g8 >= m8 || !z4) {
                    return J;
                }
                if (view == null) {
                    view = J;
                }
            }
        }
        return view;
    }

    int g2() {
        View e22 = this.A ? e2(true) : f2(true);
        if (e22 == null) {
            return -1;
        }
        return i0(e22);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void h(String str) {
        if (this.I == null) {
            super.h(str);
        }
    }

    int k2() {
        if (K() == 0) {
            return 0;
        }
        return i0(J(0));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public boolean l() {
        return this.f6734w == 0;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int l0(RecyclerView.u uVar, RecyclerView.y yVar) {
        return this.f6734w == 0 ? this.f6730s : super.l0(uVar, yVar);
    }

    int l2() {
        int K = K();
        if (K == 0) {
            return 0;
        }
        return i0(J(K - 1));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public boolean m() {
        return this.f6734w == 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public boolean n(RecyclerView.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void p(int i8, int i9, RecyclerView.y yVar, RecyclerView.o.c cVar) {
        int l8;
        int i10;
        if (this.f6734w != 0) {
            i8 = i9;
        }
        if (K() == 0 || i8 == 0) {
            return;
        }
        z2(i8, yVar);
        int[] iArr = this.O;
        if (iArr == null || iArr.length < this.f6730s) {
            this.O = new int[this.f6730s];
        }
        int i11 = 0;
        for (int i12 = 0; i12 < this.f6730s; i12++) {
            o oVar = this.f6736y;
            if (oVar.f7007d == -1) {
                l8 = oVar.f7009f;
                i10 = this.f6731t[i12].p(l8);
            } else {
                l8 = this.f6731t[i12].l(oVar.f7010g);
                i10 = this.f6736y.f7010g;
            }
            int i13 = l8 - i10;
            if (i13 >= 0) {
                this.O[i11] = i13;
                i11++;
            }
        }
        Arrays.sort(this.O, 0, i11);
        for (int i14 = 0; i14 < i11 && this.f6736y.a(yVar); i14++) {
            cVar.a(this.f6736y.f7006c, this.O[i14]);
            o oVar2 = this.f6736y;
            oVar2.f7006c += oVar2.f7007d;
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int r(RecyclerView.y yVar) {
        return V1(yVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int s(RecyclerView.y yVar) {
        return W1(yVar);
    }

    /* JADX WARN: Code restructure failed: missing block: B:31:0x0074, code lost:
        if (r10 == r11) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0086, code lost:
        if (r10 == r11) goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x0088, code lost:
        r10 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008a, code lost:
        r10 = false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    android.view.View s2() {
        /*
            r12 = this;
            int r0 = r12.K()
            r1 = 1
            int r0 = r0 - r1
            java.util.BitSet r2 = new java.util.BitSet
            int r3 = r12.f6730s
            r2.<init>(r3)
            int r3 = r12.f6730s
            r4 = 0
            r2.set(r4, r3, r1)
            int r3 = r12.f6734w
            r5 = -1
            if (r3 != r1) goto L20
            boolean r3 = r12.u2()
            if (r3 == 0) goto L20
            r3 = r1
            goto L21
        L20:
            r3 = r5
        L21:
            boolean r6 = r12.A
            if (r6 == 0) goto L27
            r6 = r5
            goto L2b
        L27:
            int r0 = r0 + 1
            r6 = r0
            r0 = r4
        L2b:
            if (r0 >= r6) goto L2e
            r5 = r1
        L2e:
            if (r0 == r6) goto Lab
            android.view.View r7 = r12.J(r0)
            android.view.ViewGroup$LayoutParams r8 = r7.getLayoutParams()
            androidx.recyclerview.widget.StaggeredGridLayoutManager$LayoutParams r8 = (androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams) r8
            androidx.recyclerview.widget.StaggeredGridLayoutManager$c r9 = r8.f6738e
            int r9 = r9.f6768e
            boolean r9 = r2.get(r9)
            if (r9 == 0) goto L54
            androidx.recyclerview.widget.StaggeredGridLayoutManager$c r9 = r8.f6738e
            boolean r9 = r12.U1(r9)
            if (r9 == 0) goto L4d
            return r7
        L4d:
            androidx.recyclerview.widget.StaggeredGridLayoutManager$c r9 = r8.f6738e
            int r9 = r9.f6768e
            r2.clear(r9)
        L54:
            boolean r9 = r8.f6739f
            if (r9 == 0) goto L59
            goto La9
        L59:
            int r9 = r0 + r5
            if (r9 == r6) goto La9
            android.view.View r9 = r12.J(r9)
            boolean r10 = r12.A
            if (r10 == 0) goto L77
            androidx.recyclerview.widget.u r10 = r12.f6732u
            int r10 = r10.d(r7)
            androidx.recyclerview.widget.u r11 = r12.f6732u
            int r11 = r11.d(r9)
            if (r10 >= r11) goto L74
            return r7
        L74:
            if (r10 != r11) goto L8a
            goto L88
        L77:
            androidx.recyclerview.widget.u r10 = r12.f6732u
            int r10 = r10.g(r7)
            androidx.recyclerview.widget.u r11 = r12.f6732u
            int r11 = r11.g(r9)
            if (r10 <= r11) goto L86
            return r7
        L86:
            if (r10 != r11) goto L8a
        L88:
            r10 = r1
            goto L8b
        L8a:
            r10 = r4
        L8b:
            if (r10 == 0) goto La9
            android.view.ViewGroup$LayoutParams r9 = r9.getLayoutParams()
            androidx.recyclerview.widget.StaggeredGridLayoutManager$LayoutParams r9 = (androidx.recyclerview.widget.StaggeredGridLayoutManager.LayoutParams) r9
            androidx.recyclerview.widget.StaggeredGridLayoutManager$c r8 = r8.f6738e
            int r8 = r8.f6768e
            androidx.recyclerview.widget.StaggeredGridLayoutManager$c r9 = r9.f6738e
            int r9 = r9.f6768e
            int r8 = r8 - r9
            if (r8 >= 0) goto La0
            r8 = r1
            goto La1
        La0:
            r8 = r4
        La1:
            if (r3 >= 0) goto La5
            r9 = r1
            goto La6
        La5:
            r9 = r4
        La6:
            if (r8 == r9) goto La9
            return r7
        La9:
            int r0 = r0 + r5
            goto L2e
        Lab:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.StaggeredGridLayoutManager.s2():android.view.View");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int t(RecyclerView.y yVar) {
        return X1(yVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public boolean t0() {
        return this.F != 0;
    }

    public void t2() {
        this.E.b();
        u1();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int u(RecyclerView.y yVar) {
        return V1(yVar);
    }

    boolean u2() {
        return a0() == 1;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int v(RecyclerView.y yVar) {
        return W1(yVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int w(RecyclerView.y yVar) {
        return X1(yVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int x1(int i8, RecyclerView.u uVar, RecyclerView.y yVar) {
        return G2(i8, uVar, yVar);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public void y1(int i8) {
        SavedState savedState = this.I;
        if (savedState != null && savedState.f6746a != i8) {
            savedState.a();
        }
        this.C = i8;
        this.D = Integer.MIN_VALUE;
        u1();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.o
    public int z1(int i8, RecyclerView.u uVar, RecyclerView.y yVar) {
        return G2(i8, uVar, yVar);
    }

    void z2(int i8, RecyclerView.y yVar) {
        int i9;
        int k22;
        if (i8 > 0) {
            k22 = l2();
            i9 = 1;
        } else {
            i9 = -1;
            k22 = k2();
        }
        this.f6736y.f7004a = true;
        P2(k22, yVar);
        H2(i9);
        o oVar = this.f6736y;
        oVar.f7006c = k22 + oVar.f7007d;
        oVar.f7005b = Math.abs(i8);
    }
}
