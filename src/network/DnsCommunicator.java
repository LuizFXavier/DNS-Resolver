package network;

import model.DnsMessage;

import java.io.IOException;

public interface DnsCommunicator {

    DnsMessage sendMessage(DnsMessage query, String address) throws IOException;
}
