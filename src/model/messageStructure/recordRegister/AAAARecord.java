package model.messageStructure.recordRegister;

/**
 * Representação dos registros que contém IPv6
 */
public class AAAARecord extends RR{
    String address;

    public AAAARecord(String NAME, short TYPE, short CLASS, int TTL, short RDLENGHT, byte[] RDATA, String address) {
        super(NAME, TYPE, CLASS, TTL, RDLENGHT, RDATA);
        this.address = address;
    }

    @Override
    public String decodedRdata()
    {
        return address;
    }
}
