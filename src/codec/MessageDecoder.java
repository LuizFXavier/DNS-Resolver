package codec;

import model.DnsMessage;
import model.dnsStructure.DnsHeader;

import java.nio.ByteBuffer;

public class MessageDecoder {

    DnsHeader decodeHeader(byte[] bytes) {
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

    public void decode(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);

        byte[] headerBytes = new byte[DnsHeader.BYTES()];
        buffer.get(headerBytes);

        DnsHeader header = decodeHeader(headerBytes);
        
    }
}
