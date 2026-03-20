package i;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.StateSet;
import i.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e extends b {

    /* renamed from: n  reason: collision with root package name */
    private a f20412n;

    /* renamed from: p  reason: collision with root package name */
    private boolean f20413p;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a extends b.d {
        int[][] J;

        /* JADX INFO: Access modifiers changed from: package-private */
        public a(a aVar, e eVar, Resources resources) {
            super(aVar, eVar, resources);
            if (aVar != null) {
                this.J = aVar.J;
            } else {
                this.J = new int[f()];
            }
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int A(int[] iArr) {
            int[][] iArr2 = this.J;
            int h8 = h();
            for (int i8 = 0; i8 < h8; i8++) {
                if (StateSet.stateSetMatches(iArr2[i8], iArr)) {
                    return i8;
                }
            }
            return -1;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable() {
            return new e(this, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public Drawable newDrawable(Resources resources) {
            return new e(this, resources);
        }

        @Override // i.b.d
        public void o(int i8, int i9) {
            super.o(i8, i9);
            int[][] iArr = new int[i9];
            System.arraycopy(this.J, 0, iArr, 0, i8);
            this.J = iArr;
        }

        @Override // i.b.d
        void r() {
            int[][] iArr = this.J;
            int[][] iArr2 = new int[iArr.length];
            for (int length = iArr.length - 1; length >= 0; length--) {
                int[][] iArr3 = this.J;
                iArr2[length] = iArr3[length] != null ? (int[]) iArr3[length].clone() : null;
            }
            this.J = iArr2;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public int z(int[] iArr, Drawable drawable) {
            int a9 = a(drawable);
            this.J[a9] = iArr;
            return a9;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public e(a aVar) {
        if (aVar != null) {
            h(aVar);
        }
    }

    e(a aVar, Resources resources) {
        h(new a(aVar, this, resources));
        onStateChange(getState());
    }

    @Override // i.b, android.graphics.drawable.Drawable
    public void applyTheme(Resources.Theme theme) {
        super.applyTheme(theme);
        onStateChange(getState());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // i.b
    public void h(b.d dVar) {
        super.h(dVar);
        if (dVar instanceof a) {
            this.f20412n = (a) dVar;
        }
    }

    @Override // android.graphics.drawable.Drawable
    public boolean isStateful() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // i.b
    /* renamed from: j */
    public a b() {
        return new a(this.f20412n, this, null);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int[] k(AttributeSet attributeSet) {
        int attributeCount = attributeSet.getAttributeCount();
        int[] iArr = new int[attributeCount];
        int i8 = 0;
        for (int i9 = 0; i9 < attributeCount; i9++) {
            int attributeNameResource = attributeSet.getAttributeNameResource(i9);
            if (attributeNameResource != 0 && attributeNameResource != 16842960 && attributeNameResource != 16843161) {
                int i10 = i8 + 1;
                if (!attributeSet.getAttributeBooleanValue(i9, false)) {
                    attributeNameResource = -attributeNameResource;
                }
                iArr[i8] = attributeNameResource;
                i8 = i10;
            }
        }
        return StateSet.trimStateSet(iArr, i8);
    }

    @Override // i.b, android.graphics.drawable.Drawable
    public Drawable mutate() {
        if (!this.f20413p && super.mutate() == this) {
            this.f20412n.r();
            this.f20413p = true;
        }
        return this;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // i.b, android.graphics.drawable.Drawable
    public boolean onStateChange(int[] iArr) {
        boolean onStateChange = super.onStateChange(iArr);
        int A = this.f20412n.A(iArr);
        if (A < 0) {
            A = this.f20412n.A(StateSet.WILD_CARD);
        }
        return g(A) || onStateChange;
    }
}
