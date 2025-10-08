package model.dnsStructure;

public class DnsHeader {
    short id;
    short flags;
    short questions = 0;
    short answers = 0;
    short authorities = 0;
    short additional = 0;

    public DnsHeader(short questions) {
        id = 0;
        this.questions = questions;
        Flag flag = new Flag();
        flag.set_RA(true);
        flags = flag.parse_short();
    }

    public DnsHeader(){}

    public static int BYTES(){
        return Short.BYTES * 6;
    }

    public short id() {
        return id;
    }

    public short flags() {
        return flags;
    }

    public short questions() {
        return questions;
    }

    public short answers() {
        return answers;
    }

    public short authorities() {
        return authorities;
    }

    public short additional() {
        return additional;
    }

    public void setId(short id) {
        this.id = id;
    }

    public void setFlags(short flags) {
        this.flags = flags;
    }

    public void setQuestions(short questions) {
        this.questions = questions;
    }

    public void setAnswers(short answers) {
        this.answers = answers;
    }

    public void setAuthorities(short authorities) {
        this.authorities = authorities;
    }

    public void setAdditional(short additional) {
        this.additional = additional;
    }
}

class Flag{
    private short QR = 0;
    private short OPCODE = 0; // 4 bits
    private short AA =  0;
    private short TC = 0;
    private short RD = 0;
    private short RA = 0;
    private short z = 0; // 3 bits
    private short RCODE = 0; // 4 bits

    short parse_short(){
        int r = QR;

        r = (r | OPCODE);
        r = (r | AA);
        r = (r | TC);
        r = (r | RD);
        r = (r | RA);
        r = (r | z);
        r = (r | RCODE);

        return (short)r;
    }
    short QR(){
        return QR;
    }
    short OPCODE(){
        return OPCODE;
    }
    short AA(){
        return AA;
    }
    short TC(){
        return TC;
    }
    short RD(){
        return RD;
    }
    short RA(){
        return RA;
    }
    short RCODE(){
        return RCODE;
    }

    void set_QR(boolean b){
        short r = (short)(b ? 1 : 0);
        QR = (short)(r << 15);
    }
    void set_OPCODE(short s){
        OPCODE = (short)(s << 11);
    }
    void set_AA(boolean b){
        short r = (short)(b ? 1 : 0);
        AA = (short)(r << 10);
    }
    void set_TC(boolean b){
        short r = (short)(b ? 1 : 0);
        TC = (short)(r << 9);
    }
    void set_RD(boolean b){
        short r = (short)(b ? 1 : 0);
        RD = (short)(r << 8);
    }
    void set_RA(boolean b){
        short r = (short)(b ? 1 : 0);
        RA = (short)(r << 7);
    }
    void set_RCODE(short s){
        RCODE = s;
    }
}