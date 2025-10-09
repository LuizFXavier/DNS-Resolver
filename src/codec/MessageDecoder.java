package codec;

import model.DnsMessage;
import model.dnsStructure.DnsHeader;
import model.dnsStructure.Question;
import model.dnsStructure.RR;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MessageDecoder {

    private static DnsHeader decodeHeader(byte[] bytes) {
        DnsHeader header = new DnsHeader();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        header.setId(buffer.getShort());
        header.setFlags(buffer.getShort());
        header.setQuestions(buffer.getShort());
        header.setAnswers(buffer.getShort());
        header.setAuthorities(buffer.getShort());
        header.setAdditional(buffer.getShort());
        return header;
    }

    private static String decodeName(ByteBuffer buffer) {

        byte b = buffer.get();
        ArrayList<Character> chars = new ArrayList<>();

        while (b != (byte) 0x00) {
            for (byte i = 0; i < b; ++i) {
                chars.add((char) buffer.get());
            }
            chars.add('.');
            b = buffer.get();
        }
        chars.removeLast();

        StringBuilder sb = new StringBuilder(chars.size());

        for (Character c : chars) {
            sb.append(c);
        }

        return sb.toString();
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

        byte[] RDATA = new byte[RDLENGHT];
        buffer.get(RDATA);

        return new RR(NAME, TYPE, CLASS, TTL, RDLENGHT, RDATA);
    }

    private static List<RR> decodeRRs(int n, ByteBuffer buffer) {
        ArrayList<RR> RRs = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            RRs.add(decodeRR(buffer));
        }
        return RRs;
    }
    public static DnsMessage decode(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        byte[] headerBytes = new byte[DnsHeader.BYTES()];
        buffer.get(headerBytes);

        DnsHeader header = decodeHeader(headerBytes);
        DnsMessage message = new DnsMessage(header);

        message.setQuestions(decodeQuestions(header.questions(), buffer));
        message.setAnswers(decodeRRs(header.answers(), buffer));
        message.setAuthorities(decodeRRs(header.authorities(), buffer));
        message.setAdditional(decodeRRs(header.additional(), buffer));

        return message;
    }
}
