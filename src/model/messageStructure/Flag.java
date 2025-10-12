package model.messageStructure;

public class Flag{
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

    void setValues(short values){
        this.QR = (short)(values & (short)(1 << 15));
        this.OPCODE = (short)(values & (short)(0b1111 << 14));
        this.AA = (short)(values & (short)(1 << 10));
        this.TC = (short)(values & (short)(1 << 9));
        this.RD = (short)(values & (short)(1 << 8));
        this.RA = (short)(values & (short)(1 << 7));
        this.RCODE = (short)(values & (short)(0b1111));
    }

    @Override
    public String toString(){
        return String.format("(QR: %b, OPCODE: %d, AA: %b, TC: %b, RD: %b, RA: %b, RCODE: %d)", QR(), OPCODE(), AA(), TC(), RD(), RA(), RCODE());
    }

    boolean QR() { return QR != 0; }
    boolean AA(){
        return AA != 0;
    }
    public boolean TC(){
        return TC != 0;
    }
    boolean RD() { return RD != 0; }
    boolean RA() { return RA != 0; }
    short OPCODE(){return (short)(OPCODE >> 11); }
    public short RCODE(){ return (short)RCODE; }

    public void set_QR(boolean b){
        short r = (short)(b ? 1 : 0);
        QR = (short)(r << 15);
    }
    public void set_OPCODE(short s){
        OPCODE = (short)(s << 11);
    }

    public void set_AA(boolean b){
        short r = (short)(b ? 1 : 0);
        AA = (short)(r << 10);
    }
    public void set_TC(boolean b){
        short r = (short)(b ? 1 : 0);
        TC = (short)(r << 9);
    }
    public void set_RD(boolean b){
        short r = (short)(b ? 1 : 0);
        RD = (short)(r << 8);
    }
    public void set_RA(boolean b){
        short r = (short)(b ? 1 : 0);
        RA = (short)(r << 7);
    }
    public void set_RCODE(short s){
        RCODE = s;
    }
}
