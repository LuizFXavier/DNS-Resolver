package model;

import model.dnsStructure.DnsHeader;
import model.dnsStructure.Question;

public class DnsQuery extends DnsMessage {

    DnsQuery(String name, String type) {
        super();
        RecordType recordType = RecordType.valueOf(type);
        questions.add(new Question(name, recordType.getCode(), 1));
        this.header = new DnsHeader((short)questions.size());
    }
}
