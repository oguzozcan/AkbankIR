package com.mallardduckapps.akbankir.objects;

/**
 * Created by oguzemreozcan on 25/01/16.
 */

import java.io.Serializable;

public class BasicNameValuePair implements Cloneable, Serializable {

    private static final long serialVersionUID = -6437800749411518984L;
    private final String name;
    private final String value;

    /**
     * Default Constructor taking a name and a value. The value may be null.
     *
     * @param name The name.
     * @param value The value.
     */
    public BasicNameValuePair(String name, final String value) {
        super();
        if (name == null) {
            //throw new IllegalArgumentException("Name may not be null");
            name = "";
        }
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        // don't call complex default formatting for a simple toString

        if (this.value == null) {
            return name;
        } else {
            //int len = this.name.length() + 1 + this.value.length();
            return this.name + "=" + this.value;
        }
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) return true;
        if (object instanceof BasicNameValuePair) {
            BasicNameValuePair that = (BasicNameValuePair) object;
            return this.name.equals(that.name)
                    && this.value.equals(that.value); // LangUtils.equals
        } else {
            return false;
        }
    }

 /*   @Override
    public int hashCode() {
        int hash = LangUtils.HASH_SEED;
        hash = LangUtils.hashCode(hash, this.name);
        hash = LangUtils.hashCode(hash, this.value);
        return hash;
    }*/

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}

