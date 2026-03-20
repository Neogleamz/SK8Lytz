package androidx.window.layout;

import android.graphics.Rect;
import androidx.window.core.Bounds;
import androidx.window.layout.FoldingFeature;
import java.util.Objects;
import kotlin.jvm.internal.i;
import kotlin.jvm.internal.p;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class HardwareFoldingFeature implements FoldingFeature {
    public static final Companion Companion = new Companion(null);
    private final Bounds featureBounds;
    private final FoldingFeature.State state;
    private final Type type;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(i iVar) {
            this();
        }

        public final void validateFeatureBounds$window_release(Bounds bounds) {
            p.e(bounds, "bounds");
            boolean z4 = false;
            if (!((bounds.getWidth() == 0 && bounds.getHeight() == 0) ? false : true)) {
                throw new IllegalArgumentException("Bounds must be non zero".toString());
            }
            if (bounds.getLeft() == 0 || bounds.getTop() == 0) {
                z4 = true;
            }
            if (!z4) {
                throw new IllegalArgumentException("Bounding rectangle must start at the top or left window edge for folding features".toString());
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class Type {
        public static final Companion Companion = new Companion(null);
        private static final Type FOLD = new Type("FOLD");
        private static final Type HINGE = new Type("HINGE");
        private final String description;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(i iVar) {
                this();
            }

            public final Type getFOLD() {
                return Type.FOLD;
            }

            public final Type getHINGE() {
                return Type.HINGE;
            }
        }

        private Type(String str) {
            this.description = str;
        }

        public String toString() {
            return this.description;
        }
    }

    public HardwareFoldingFeature(Bounds bounds, Type type, FoldingFeature.State state) {
        p.e(bounds, "featureBounds");
        p.e(type, "type");
        p.e(state, "state");
        this.featureBounds = bounds;
        this.type = type;
        this.state = state;
        Companion.validateFeatureBounds$window_release(bounds);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (p.a(HardwareFoldingFeature.class, obj == null ? null : obj.getClass())) {
            Objects.requireNonNull(obj, "null cannot be cast to non-null type androidx.window.layout.HardwareFoldingFeature");
            HardwareFoldingFeature hardwareFoldingFeature = (HardwareFoldingFeature) obj;
            return p.a(this.featureBounds, hardwareFoldingFeature.featureBounds) && p.a(this.type, hardwareFoldingFeature.type) && p.a(getState(), hardwareFoldingFeature.getState());
        }
        return false;
    }

    @Override // androidx.window.layout.DisplayFeature
    public Rect getBounds() {
        return this.featureBounds.toRect();
    }

    @Override // androidx.window.layout.FoldingFeature
    public FoldingFeature.OcclusionType getOcclusionType() {
        return (this.featureBounds.getWidth() == 0 || this.featureBounds.getHeight() == 0) ? FoldingFeature.OcclusionType.NONE : FoldingFeature.OcclusionType.FULL;
    }

    @Override // androidx.window.layout.FoldingFeature
    public FoldingFeature.Orientation getOrientation() {
        return this.featureBounds.getWidth() > this.featureBounds.getHeight() ? FoldingFeature.Orientation.HORIZONTAL : FoldingFeature.Orientation.VERTICAL;
    }

    @Override // androidx.window.layout.FoldingFeature
    public FoldingFeature.State getState() {
        return this.state;
    }

    public final Type getType$window_release() {
        return this.type;
    }

    public int hashCode() {
        return (((this.featureBounds.hashCode() * 31) + this.type.hashCode()) * 31) + getState().hashCode();
    }

    @Override // androidx.window.layout.FoldingFeature
    public boolean isSeparating() {
        Type type = this.type;
        Type.Companion companion = Type.Companion;
        if (p.a(type, companion.getHINGE())) {
            return true;
        }
        return p.a(this.type, companion.getFOLD()) && p.a(getState(), FoldingFeature.State.HALF_OPENED);
    }

    public String toString() {
        return ((Object) HardwareFoldingFeature.class.getSimpleName()) + " { " + this.featureBounds + ", type=" + this.type + ", state=" + getState() + " }";
    }
}
