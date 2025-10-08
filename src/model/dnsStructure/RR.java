package model.dnsStructure;

public class RR{
    String NAME;
    short TYPE;
    short CLASS;
    int TTL;
    short RDLENGHT;
    byte[] RDATA;

    public String NAME() {
        return NAME;
    }

    public short TYPE() {
        return TYPE;
    }

    public short CLASS() {
        return CLASS;
    }

    public int TTL() {
        return TTL;
    }

    public short RDLENGHT() {
        return RDLENGHT;
    }

    public byte[] RDATA() {
        return RDATA;
    }
}
