package model.messageStructure;

/**
 * Representação do cabeçalho das mensagens DNS
 */
public class DnsHeader {
    short id = 0; // Identificador da mensagem
    Flag flags = new Flag();
    short questions = 0; // Número de questões presentes na mensagem
    short answers = 0;
    short authorities = 0;
    short additional = 0;

    public DnsHeader(){}

    @Override
    public String toString() {
        return String.format("""
                id: %d  flags: %s
                Qust: %d  Answ: %d
                Auth: %d  Addi: %d
                -------------------""", id, flags.toString(), questions, answers, authorities, additional);
    }

    public static int BYTES(){
        return Short.BYTES * 6;
    }

    public short id() {
        return id;
    }

    public Flag flags() {
        return flags;
    }

    public short shortFlag() {return flags.parse_short();}

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
        this.flags.setValues(flags);
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

