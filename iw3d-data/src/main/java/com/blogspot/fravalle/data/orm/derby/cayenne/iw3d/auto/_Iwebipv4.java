package com.blogspot.fravalle.data.orm.derby.cayenne.iw3d.auto;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.cayenne.BaseDataObject;
import org.apache.cayenne.exp.ExpressionFactory;
import org.apache.cayenne.exp.Property;

/**
 * Class _Iwebipv4 was generated by Cayenne.
 * It is probably a good idea to avoid changing this class manually,
 * since it may be overwritten next time code is regenerated.
 * If you need to make any customizations, please use subclass.
 */
public abstract class _Iwebipv4 extends BaseDataObject {

    private static final long serialVersionUID = 1L; 

    public static final Property<Integer> IWID4_PK_PROPERTY = Property.create(ExpressionFactory.dbPathExp("IWID4"), Integer.class);
    public static final String IWID4_PK_COLUMN = "IWID4";

    public static final Property<Integer> IWCATEGORYID = Property.create("iwcategoryid", Integer.class);
    public static final Property<String> IWCATEGORYNAME = Property.create("iwcategoryname", String.class);
    public static final Property<String> IWDOMAIN1STLEV = Property.create("iwdomain1stlev", String.class);
    public static final Property<String> IWDOMAIN2NDLEV = Property.create("iwdomain2ndlev", String.class);
    public static final Property<String> IWDOMAINNAME = Property.create("iwdomainname", String.class);
    public static final Property<String> IWDOMAINTLD = Property.create("iwdomaintld", String.class);
    public static final Property<Boolean> IWHASHTTPS = Property.create("iwhashttps", Boolean.class);
    public static final Property<String> IWHOOKURL = Property.create("iwhookurl", String.class);
    public static final Property<Short> IWHTTPCODE = Property.create("iwhttpcode", Short.class);
    public static final Property<String> IWIPADDRESS = Property.create("iwipaddress", String.class);
    public static final Property<Short> IWIPCLASSA = Property.create("iwipclassa", Short.class);
    public static final Property<Short> IWIPCLASSB = Property.create("iwipclassb", Short.class);
    public static final Property<Short> IWIPCLASSC = Property.create("iwipclassc", Short.class);
    public static final Property<Short> IWIPCLASSD = Property.create("iwipclassd", Short.class);
    public static final Property<String> IWTITLE = Property.create("iwtitle", String.class);
    public static final Property<String> IWURL = Property.create("iwurl", String.class);
    public static final Property<Integer> IWWEBDEPTHLEVEL = Property.create("iwwebdepthlevel", Integer.class);
    public static final Property<Integer> IWWEBSHOTID = Property.create("iwwebshotid", Integer.class);

    protected Integer iwcategoryid;
    protected String iwcategoryname;
    protected String iwdomain1stlev;
    protected String iwdomain2ndlev;
    protected String iwdomainname;
    protected String iwdomaintld;
    protected Boolean iwhashttps;
    protected String iwhookurl;
    protected short iwhttpcode;
    protected String iwipaddress;
    protected short iwipclassa;
    protected short iwipclassb;
    protected short iwipclassc;
    protected short iwipclassd;
    protected String iwtitle;
    protected String iwurl;
    protected int iwwebdepthlevel;
    protected int iwwebshotid;


    public void setIwcategoryid(int iwcategoryid) {
        beforePropertyWrite("iwcategoryid", this.iwcategoryid, iwcategoryid);
        this.iwcategoryid = iwcategoryid;
    }

    public int getIwcategoryid() {
        beforePropertyRead("iwcategoryid");
        if(this.iwcategoryid == null) {
            return 0;
        }
        return this.iwcategoryid;
    }

    public void setIwcategoryname(String iwcategoryname) {
        beforePropertyWrite("iwcategoryname", this.iwcategoryname, iwcategoryname);
        this.iwcategoryname = iwcategoryname;
    }

    public String getIwcategoryname() {
        beforePropertyRead("iwcategoryname");
        return this.iwcategoryname;
    }

    public void setIwdomain1stlev(String iwdomain1stlev) {
        beforePropertyWrite("iwdomain1stlev", this.iwdomain1stlev, iwdomain1stlev);
        this.iwdomain1stlev = iwdomain1stlev;
    }

    public String getIwdomain1stlev() {
        beforePropertyRead("iwdomain1stlev");
        return this.iwdomain1stlev;
    }

    public void setIwdomain2ndlev(String iwdomain2ndlev) {
        beforePropertyWrite("iwdomain2ndlev", this.iwdomain2ndlev, iwdomain2ndlev);
        this.iwdomain2ndlev = iwdomain2ndlev;
    }

    public String getIwdomain2ndlev() {
        beforePropertyRead("iwdomain2ndlev");
        return this.iwdomain2ndlev;
    }

    public void setIwdomainname(String iwdomainname) {
        beforePropertyWrite("iwdomainname", this.iwdomainname, iwdomainname);
        this.iwdomainname = iwdomainname;
    }

    public String getIwdomainname() {
        beforePropertyRead("iwdomainname");
        return this.iwdomainname;
    }

    public void setIwdomaintld(String iwdomaintld) {
        beforePropertyWrite("iwdomaintld", this.iwdomaintld, iwdomaintld);
        this.iwdomaintld = iwdomaintld;
    }

    public String getIwdomaintld() {
        beforePropertyRead("iwdomaintld");
        return this.iwdomaintld;
    }

    public void setIwhashttps(boolean iwhashttps) {
        beforePropertyWrite("iwhashttps", this.iwhashttps, iwhashttps);
        this.iwhashttps = iwhashttps;
    }

	public boolean isIwhashttps() {
        beforePropertyRead("iwhashttps");
        if(this.iwhashttps == null) {
            return false;
        }
        return this.iwhashttps;
    }

    public void setIwhookurl(String iwhookurl) {
        beforePropertyWrite("iwhookurl", this.iwhookurl, iwhookurl);
        this.iwhookurl = iwhookurl;
    }

    public String getIwhookurl() {
        beforePropertyRead("iwhookurl");
        return this.iwhookurl;
    }

    public void setIwhttpcode(short iwhttpcode) {
        beforePropertyWrite("iwhttpcode", this.iwhttpcode, iwhttpcode);
        this.iwhttpcode = iwhttpcode;
    }

    public short getIwhttpcode() {
        beforePropertyRead("iwhttpcode");
        return this.iwhttpcode;
    }

    public void setIwipaddress(String iwipaddress) {
        beforePropertyWrite("iwipaddress", this.iwipaddress, iwipaddress);
        this.iwipaddress = iwipaddress;
    }

    public String getIwipaddress() {
        beforePropertyRead("iwipaddress");
        return this.iwipaddress;
    }

    public void setIwipclassa(short iwipclassa) {
        beforePropertyWrite("iwipclassa", this.iwipclassa, iwipclassa);
        this.iwipclassa = iwipclassa;
    }

    public short getIwipclassa() {
        beforePropertyRead("iwipclassa");
        return this.iwipclassa;
    }

    public void setIwipclassb(short iwipclassb) {
        beforePropertyWrite("iwipclassb", this.iwipclassb, iwipclassb);
        this.iwipclassb = iwipclassb;
    }

    public short getIwipclassb() {
        beforePropertyRead("iwipclassb");
        return this.iwipclassb;
    }

    public void setIwipclassc(short iwipclassc) {
        beforePropertyWrite("iwipclassc", this.iwipclassc, iwipclassc);
        this.iwipclassc = iwipclassc;
    }

    public short getIwipclassc() {
        beforePropertyRead("iwipclassc");
        return this.iwipclassc;
    }

    public void setIwipclassd(short iwipclassd) {
        beforePropertyWrite("iwipclassd", this.iwipclassd, iwipclassd);
        this.iwipclassd = iwipclassd;
    }

    public short getIwipclassd() {
        beforePropertyRead("iwipclassd");
        return this.iwipclassd;
    }

    public void setIwtitle(String iwtitle) {
        beforePropertyWrite("iwtitle", this.iwtitle, iwtitle);
        this.iwtitle = iwtitle;
    }

    public String getIwtitle() {
        beforePropertyRead("iwtitle");
        return this.iwtitle;
    }

    public void setIwurl(String iwurl) {
        beforePropertyWrite("iwurl", this.iwurl, iwurl);
        this.iwurl = iwurl;
    }

    public String getIwurl() {
        beforePropertyRead("iwurl");
        return this.iwurl;
    }

    public void setIwwebdepthlevel(int iwwebdepthlevel) {
        beforePropertyWrite("iwwebdepthlevel", this.iwwebdepthlevel, iwwebdepthlevel);
        this.iwwebdepthlevel = iwwebdepthlevel;
    }

    public int getIwwebdepthlevel() {
        beforePropertyRead("iwwebdepthlevel");
        return this.iwwebdepthlevel;
    }

    public void setIwwebshotid(int iwwebshotid) {
        beforePropertyWrite("iwwebshotid", this.iwwebshotid, iwwebshotid);
        this.iwwebshotid = iwwebshotid;
    }

    public int getIwwebshotid() {
        beforePropertyRead("iwwebshotid");
        return this.iwwebshotid;
    }

    @Override
    public Object readPropertyDirectly(String propName) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch(propName) {
            case "iwcategoryid":
                return this.iwcategoryid;
            case "iwcategoryname":
                return this.iwcategoryname;
            case "iwdomain1stlev":
                return this.iwdomain1stlev;
            case "iwdomain2ndlev":
                return this.iwdomain2ndlev;
            case "iwdomainname":
                return this.iwdomainname;
            case "iwdomaintld":
                return this.iwdomaintld;
            case "iwhashttps":
                return this.iwhashttps;
            case "iwhookurl":
                return this.iwhookurl;
            case "iwhttpcode":
                return this.iwhttpcode;
            case "iwipaddress":
                return this.iwipaddress;
            case "iwipclassa":
                return this.iwipclassa;
            case "iwipclassb":
                return this.iwipclassb;
            case "iwipclassc":
                return this.iwipclassc;
            case "iwipclassd":
                return this.iwipclassd;
            case "iwtitle":
                return this.iwtitle;
            case "iwurl":
                return this.iwurl;
            case "iwwebdepthlevel":
                return this.iwwebdepthlevel;
            case "iwwebshotid":
                return this.iwwebshotid;
            default:
                return super.readPropertyDirectly(propName);
        }
    }

    @Override
    public void writePropertyDirectly(String propName, Object val) {
        if(propName == null) {
            throw new IllegalArgumentException();
        }

        switch (propName) {
            case "iwcategoryid":
                this.iwcategoryid = (Integer)val;
                break;
            case "iwcategoryname":
                this.iwcategoryname = (String)val;
                break;
            case "iwdomain1stlev":
                this.iwdomain1stlev = (String)val;
                break;
            case "iwdomain2ndlev":
                this.iwdomain2ndlev = (String)val;
                break;
            case "iwdomainname":
                this.iwdomainname = (String)val;
                break;
            case "iwdomaintld":
                this.iwdomaintld = (String)val;
                break;
            case "iwhashttps":
                this.iwhashttps = (Boolean)val;
                break;
            case "iwhookurl":
                this.iwhookurl = (String)val;
                break;
            case "iwhttpcode":
                this.iwhttpcode = val == null ? 0 : (short)val;
                break;
            case "iwipaddress":
                this.iwipaddress = (String)val;
                break;
            case "iwipclassa":
                this.iwipclassa = val == null ? 0 : (short)val;
                break;
            case "iwipclassb":
                this.iwipclassb = val == null ? 0 : (short)val;
                break;
            case "iwipclassc":
                this.iwipclassc = val == null ? 0 : (short)val;
                break;
            case "iwipclassd":
                this.iwipclassd = val == null ? 0 : (short)val;
                break;
            case "iwtitle":
                this.iwtitle = (String)val;
                break;
            case "iwurl":
                this.iwurl = (String)val;
                break;
            case "iwwebdepthlevel":
                this.iwwebdepthlevel = val == null ? 0 : (int)val;
                break;
            case "iwwebshotid":
                this.iwwebshotid = val == null ? 0 : (int)val;
                break;
            default:
                super.writePropertyDirectly(propName, val);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        writeSerialized(out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        readSerialized(in);
    }

    @Override
    protected void writeState(ObjectOutputStream out) throws IOException {
        super.writeState(out);
        out.writeObject(this.iwcategoryid);
        out.writeObject(this.iwcategoryname);
        out.writeObject(this.iwdomain1stlev);
        out.writeObject(this.iwdomain2ndlev);
        out.writeObject(this.iwdomainname);
        out.writeObject(this.iwdomaintld);
        out.writeObject(this.iwhashttps);
        out.writeObject(this.iwhookurl);
        out.writeShort(this.iwhttpcode);
        out.writeObject(this.iwipaddress);
        out.writeShort(this.iwipclassa);
        out.writeShort(this.iwipclassb);
        out.writeShort(this.iwipclassc);
        out.writeShort(this.iwipclassd);
        out.writeObject(this.iwtitle);
        out.writeObject(this.iwurl);
        out.writeInt(this.iwwebdepthlevel);
        out.writeInt(this.iwwebshotid);
    }

    @Override
    protected void readState(ObjectInputStream in) throws IOException, ClassNotFoundException {
        super.readState(in);
        this.iwcategoryid = (Integer)in.readObject();
        this.iwcategoryname = (String)in.readObject();
        this.iwdomain1stlev = (String)in.readObject();
        this.iwdomain2ndlev = (String)in.readObject();
        this.iwdomainname = (String)in.readObject();
        this.iwdomaintld = (String)in.readObject();
        this.iwhashttps = (Boolean)in.readObject();
        this.iwhookurl = (String)in.readObject();
        this.iwhttpcode = in.readShort();
        this.iwipaddress = (String)in.readObject();
        this.iwipclassa = in.readShort();
        this.iwipclassb = in.readShort();
        this.iwipclassc = in.readShort();
        this.iwipclassd = in.readShort();
        this.iwtitle = (String)in.readObject();
        this.iwurl = (String)in.readObject();
        this.iwwebdepthlevel = in.readInt();
        this.iwwebshotid = in.readInt();
    }

}
