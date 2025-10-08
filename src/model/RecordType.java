package model;

public enum RecordType {
    A(1),
    NS(2),
    CNAME(5),
    SOA(6),
    MX(15),
    AAAA(28),
    DS(43),
    RRSIG(46),
    DNSKEY(48),
    OPT(41);

    private final int code;

    RecordType(int i) {
        code = i;
    }
    short getCode() {
        return (short)code;
    }
}
