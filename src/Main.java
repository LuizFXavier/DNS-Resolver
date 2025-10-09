import codec.MessageDecoder;
import codec.MessageEncoder;
import model.DnsMessage;
import model.dnsStructure.DnsHeader;
import model.dnsStructure.Question;
import model.dnsStructure.RR;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        DnsHeader header = new DnsHeader();
        header.setFlags((short)(1 << 10));
        header.setQuestions((short)2);
        header.setAnswers((short)1);
        header.setId((short) 40);

        DnsMessage message = new DnsMessage(header);
        ArrayList<Question> qs = new ArrayList<>();
        qs.add(new Question("www.pocotom.br", (short) 1, (short) 2));
        qs.add(new Question("hvcomp.io", (short)15, (short)4));

        ArrayList<RR>  rrs = new ArrayList<>();
        byte[] b = {1, 2};
        rrs.add(new RR("www2.sus.com", (short)22, (short)3, 800, (short)2, b));

        message.setQuestions(qs);
        message.setAnswers(rrs);

        System.out.println(message);

        byte[] code = MessageEncoder.encode(message);

        DnsMessage decode = MessageDecoder.decode(code);

        System.out.println(decode);
    }
}