package codec;

import model.DnsMessage;
import model.dnsStructure.DnsHeader;
import model.dnsStructure.Question;
import model.dnsStructure.RR;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class MessageEncoder {

    byte[] encodeHeader(final DnsHeader header) {
        ByteBuffer buffer = ByteBuffer.allocate(Short.BYTES * 6);

        buffer.putShort(header.id());
        buffer.putShort(header.flags());
        buffer.putShort(header.questions());
        buffer.putShort(header.answers());
        buffer.putShort(header.authorities());
        buffer.putShort(header.additional());

        return buffer.array();
    }
    byte[] encodeName(String NAME){

        String[] labels = NAME.split("\\.");

        ArrayList<Byte> name = new ArrayList<Byte>();

        for (String label : labels) {
            name.add((byte) label.length());
            for (byte b : label.getBytes()) {
                name.add(b);
            }
        }
        name.add((byte) 0);

        byte[] bytes = new byte[name.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = name.get(i);
        }

        return bytes;
    }

    byte[] encodeQuestion(final Question question){

        byte[] name = encodeName(question.QNAME());

        ByteBuffer buffer = ByteBuffer.allocate(name.length + Short.BYTES * 2);

        buffer.put(name);
        buffer.putShort(question.QTYPE());
        buffer.putShort(question.QCLASS());

        return buffer.array();
    }

    byte[] encodeRR(final RR rr){
        byte[] name = encodeName(rr.NAME());
        ByteBuffer buffer = ByteBuffer.allocate(name.length +
                Short.BYTES * 2 + Integer.BYTES + Short.BYTES + rr.RDLENGHT());

        buffer.put(name);
        buffer.putShort(rr.TYPE());
        buffer.putShort(rr.CLASS());
        buffer.putInt(rr.TTL());
        buffer.putShort(rr.RDLENGHT());
        buffer.put(rr.RDATA());

        return buffer.array();
    }

    public byte[] encode(final DnsMessage message){
        byte[] header = encodeHeader(message.header());

        ArrayList<byte[]> questions = new ArrayList<byte[]>();
        for (Question q : message.questions()){
            questions.add(encodeQuestion(q));
        }
        int qSize = questions.stream().mapToInt(a -> a.length).sum();

        ArrayList<byte[]> answers = new ArrayList<byte[]>();
        for (RR ans : message.answers()){
            answers.add(encodeRR(ans));
        }
        int ansSize = answers.stream().mapToInt(a -> a.length).sum();

        ArrayList<byte[]> authorities = new ArrayList<byte[]>();
        for (RR rr : message.authorities()){
            authorities.add(encodeRR(rr));
        }
        int authSize = authorities.stream().mapToInt(a -> a.length).sum();

        ArrayList<byte[]> additional = new ArrayList<byte[]>();
        for (RR rr : message.additional()){
            additional.add(encodeRR(rr));
        }
        int addSize = additional.stream().mapToInt(a -> a.length).sum();

        ByteBuffer buffer = ByteBuffer.allocate(DnsHeader.BYTES() +
                qSize + ansSize + authSize);

        buffer.put(header);

        for (byte[] q : questions) {
            buffer.put(q);
        }
        for (byte[] ans : answers) {
            buffer.put(ans);
        }
        for (byte[] auth : authorities) {
            buffer.put(auth);
        }
        for (byte[] add : additional) {
            buffer.put(add);
        }

        return buffer.array();
    }
}
