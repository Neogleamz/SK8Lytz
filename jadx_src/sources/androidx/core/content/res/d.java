package androidx.core.content.res;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParserException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    private final Shader f4645a;

    /* renamed from: b  reason: collision with root package name */
    private final ColorStateList f4646b;

    /* renamed from: c  reason: collision with root package name */
    private int f4647c;

    private d(Shader shader, ColorStateList colorStateList, int i8) {
        this.f4645a = shader;
        this.f4646b = colorStateList;
        this.f4647c = i8;
    }

    private static d a(Resources resources, int i8, Resources.Theme theme) {
        int next;
        XmlResourceParser xml = resources.getXml(i8);
        AttributeSet asAttributeSet = Xml.asAttributeSet(xml);
        do {
            next = xml.next();
            if (next == 2) {
                break;
            }
        } while (next != 1);
        if (next == 2) {
            String name = xml.getName();
            name.hashCode();
            if (name.equals("gradient")) {
                return d(f.b(resources, xml, asAttributeSet, theme));
            }
            if (name.equals("selector")) {
                return c(c.b(resources, xml, asAttributeSet, theme));
            }
            throw new XmlPullParserException(xml.getPositionDescription() + ": unsupported complex color tag " + name);
        }
        throw new XmlPullParserException("No start tag found");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static d b(int i8) {
        return new d(null, null, i8);
    }

    static d c(ColorStateList colorStateList) {
        return new d(null, colorStateList, colorStateList.getDefaultColor());
    }

    static d d(Shader shader) {
        return new d(shader, null, 0);
    }

    public static d g(Resources resources, int i8, Resources.Theme theme) {
        try {
            return a(resources, i8, theme);
        } catch (Exception e8) {
            Log.e("ComplexColorCompat", "Failed to inflate ComplexColor.", e8);
            return null;
        }
    }

    public int e() {
        return this.f4647c;
    }

    public Shader f() {
        return this.f4645a;
    }

    public boolean h() {
        return this.f4645a != null;
    }

    public boolean i() {
        ColorStateList colorStateList;
        return this.f4645a == null && (colorStateList = this.f4646b) != null && colorStateList.isStateful();
    }

    public boolean j(int[] iArr) {
        if (i()) {
            ColorStateList colorStateList = this.f4646b;
            int colorForState = colorStateList.getColorForState(iArr, colorStateList.getDefaultColor());
            if (colorForState != this.f4647c) {
                this.f4647c = colorForState;
                return true;
            }
        }
        return false;
    }

    public void k(int i8) {
        this.f4647c = i8;
    }

    public boolean l() {
        return h() || this.f4647c != 0;
    }
}
