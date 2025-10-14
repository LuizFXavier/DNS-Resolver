package model;

import model.messageStructure.DnsHeader;
import model.messageStructure.Question;
import model.messageStructure.recordRegister.RR;

import java.util.ArrayList;
import java.util.List;

public class DnsMessage {
    DnsHeader header;
    List<Question> questions = new ArrayList<Question>(); // Sessão de perguntas
    List<RR> answers = new ArrayList<RR>(); // Sessão de respostas
    List<RR> authorities = new ArrayList<RR>(); // Sessão de autoridades
    List<RR> additional = new ArrayList<RR>(); // Sessão de registros adicionais

    public DnsMessage(DnsHeader header){
        this.header = header;
    }

    // Constrói uma pergunta de um nome dns.
    public DnsMessage(String qName, String qType, boolean recursive){
        header = new DnsHeader();
        header.setQuestions((short)1);
        header.flags().set_RD(recursive);

        short t = switch (qType) {
            case "A" -> 1;
            case "AAAA" -> 28;
            case "NS" -> 2;
            default -> 0;
        };

        questions.add(new Question(qName, t, 1));
    }

    @Override
    public String toString(){
        String h = header.toString();
        String quest = "";
        String answ = "";
        String auth = "";
        String addi = "";

        if(header.questions() > 0){
            quest = "\nQuest:\n";
            for(int i = 0; i < header.questions(); i++){
                quest = quest.concat("       " + questions.get(i).toString()) + "\n";
            }
        }
        if(header.answers() > 0){
            answ = "\nAnswer:\n";
            for(int i = 0; i < header.answers(); i++){
                answ = answ.concat("       " + answers.get(i).toString()) + "\n";
            }
        }
        if(header.authorities() > 0){
            auth = "\nAuth:\n";
            for(int i = 0; i < header.authorities(); i++){
                auth = auth.concat("      " + authorities.get(i).toString()) + "\n";
            }
        }
        if(header.additional() > 0){
            addi = "\nAdd:\n";
            for(int i = 0; i < header.additional(); i++){
                addi = addi.concat("     " + additional.get(i).toString()) + "\n";
            }
        }
        return h + quest + answ + auth + addi;
    }

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

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public void setAnswers(List<RR> answers) {
        this.answers = answers;
    }

    public void setAuthorities(List<RR> authorities) {
        this.authorities = authorities;
    }

    public void setAdditional(List<RR> additional) {
        this.additional = additional;
    }

    public boolean hasFinalAnswer(){
        return header.answers() > 0 || header().flags().RCODE() == 3;
    }

    public boolean trucated(){
        return header.flags().TC();
    }
}

