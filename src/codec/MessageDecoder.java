package codec;

import model.DnsMessage;
import model.messageStructure.DnsHeader;
import model.messageStructure.Question;
import model.messageStructure.recordRegister.AAAARecord;
import model.messageStructure.recordRegister.ARecord;
import model.messageStructure.recordRegister.NsRecord;
import model.messageStructure.recordRegister.RR;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static codec.RDataDecoder.*;

public class MessageDecoder {


    /**
     * Decodifica os primeiros bytes de uma mensagem DNS para
     * gerar um objeto DnsHeader
     * @param buffer Buffer de bytes que contém os bytes de cabeçalho
     * @return Cabeçalho decodificado.
     */
    private static DnsHeader decodeHeader(ByteBuffer buffer) {
        DnsHeader header = new DnsHeader();

        header.setId(buffer.getShort());
        header.setFlags(buffer.getShort());
        header.setQuestions(buffer.getShort());
        header.setAnswers(buffer.getShort());
        header.setAuthorities(buffer.getShort());
        header.setAdditional(buffer.getShort());

        return header;
    }

    private static Question decodeQuestion(ByteBuffer buffer) {

        String name = decodeName(buffer);

        short QTYPE = buffer.getShort();
        short QCLASS = buffer.getShort();

        return new Question(name, QTYPE, QCLASS);
    }

    private static List<Question> decodeQuestions(short n, ByteBuffer buffer) {

        ArrayList<Question> questions = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            questions.add(decodeQuestion(buffer));
        }
        return questions;
    }

    private static RR decodeRR(ByteBuffer buffer) {

        String NAME = decodeName(buffer);
        short TYPE = buffer.getShort();
        short CLASS = buffer.getShort();
        int TTL = buffer.getInt();
        short RDLENGHT = buffer.getShort();

        buffer.mark();
        byte[] RDATA = new byte[RDLENGHT];
        buffer.get(RDATA);
        buffer.reset();

        return switch (TYPE) {
            case 1 -> new ARecord(NAME, TYPE, CLASS, TTL, RDLENGHT, RDATA, decodeIPv4(buffer));
            case 2 -> new NsRecord(NAME, TYPE, CLASS, TTL, RDLENGHT, RDATA, decodeName(buffer));
            case 28 -> new AAAARecord(NAME, TYPE, CLASS, TTL, RDLENGHT, RDATA, decodeIPv6(buffer));
            default -> new RR(NAME, TYPE, CLASS, TTL, RDLENGHT, RDATA);
        };

    }

    private static List<RR> decodeRRs(int n, ByteBuffer buffer) {
        ArrayList<RR> RRs = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            RRs.add(decodeRR(buffer));
        }
        return RRs;
    }

    /**
     * Decodifica os bytes recebidos pelo servidor DNS, convertendo-os
     * para um objeto DnsMessage.
     * @param bytes Array de bytes que contém a mensagem DNS recebida.
     * @return Mensagem DNS decodificada.
     */
    public static DnsMessage decode(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        DnsHeader header = decodeHeader(buffer);
        DnsMessage message = new DnsMessage(header);

        message.setQuestions(decodeQuestions(header.questions(), buffer));
        message.setAnswers(decodeRRs(header.answers(), buffer));
        message.setAuthorities(decodeRRs(header.authorities(), buffer));
        message.setAdditional(decodeRRs(header.additional(), buffer));

        return message;
    }
}
