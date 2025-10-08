package model.dnsStructure;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Question{
    String QNAME;
    short QTYPE;
    short QCLASS;

    public Question(String QNAME, short QTYPE, int QCLASS){
        this.QNAME = QNAME;
        this.QTYPE = QTYPE;
        this.QCLASS = (short)QCLASS;
    }

    public String QNAME() {
        return QNAME;
    }

    public short QTYPE() {
        return QTYPE;
    }

    public short QCLASS() {
        return QCLASS;
    }
}
