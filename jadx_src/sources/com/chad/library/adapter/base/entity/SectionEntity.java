package com.chad.library.adapter.base.entity;

import java.io.Serializable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class SectionEntity<T> implements Serializable {

    /* renamed from: a  reason: collision with root package name */
    public boolean f8834a;

    /* renamed from: b  reason: collision with root package name */
    public T f8835b;

    /* renamed from: c  reason: collision with root package name */
    public String f8836c;

    public SectionEntity(T t8) {
        this.f8834a = false;
        this.f8836c = null;
        this.f8835b = t8;
    }

    public SectionEntity(boolean z4, String str) {
        this.f8834a = z4;
        this.f8836c = str;
        this.f8835b = null;
    }
}
