package model.messageStructure.recordRegister;

/**
 * Representação de um Record Register do DNS
 */
public class RR{
    String NAME;
    short TYPE;
    short CLASS;
    int TTL;
    short RDLENGHT;
    byte[] RDATA;

    public RR(String NAME, short TYPE, short CLASS, int TTL, short RDLENGHT, byte[] RDATA) {
        this.NAME = NAME;
        this.TYPE = TYPE;
        this.CLASS = CLASS;
        this.TTL = TTL;
        this.RDLENGHT = RDLENGHT;
        this.RDATA = RDATA;
    }

    @Override
    public String toString() {
        return String.format("(%s, %d, %d, %d, %d, %s)", NAME, TYPE, CLASS, TTL, RDLENGHT, decodedRdata());
    }

    public String decodedRdata(){
        return "[data...]";
    }

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
