package model.messageStructure.recordRegister;

/**
 * Representação dos registros que contém nomes de servidores DNS.
 */
public class NsRecord extends RR{
    String recordName;

    public NsRecord(String NAME, short TYPE, short CLASS, int TTL, short RDLENGHT, byte[] RDATA, String recordName) {
        super(NAME, TYPE, CLASS, TTL, RDLENGHT, RDATA);
        this.recordName = recordName;
    }
    @Override
    public String decodedRdata() {

        return recordName;
    }
}
