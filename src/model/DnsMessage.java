package model;

import model.dnsStructure.DnsHeader;
import model.dnsStructure.Question;
import model.dnsStructure.RR;

import java.util.ArrayList;
import java.util.List;

public abstract class DnsMessage {
    DnsHeader header;
    List<Question> questions = new ArrayList<Question>();
    List<RR> answers = new ArrayList<RR>();
    List<RR> authorities = new ArrayList<RR>();
    List<RR> additional = new ArrayList<RR>();

    public DnsHeader header() {
        return header;
    }

    public List<Question> questions() {
        return questions;
    }

    public List<RR> answers() {
        return answers;
    }

    public List<RR> authorities() {
        return authorities;
    }

    public List<RR> additional() {
        return additional;
    }
}

