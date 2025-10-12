package network;

import codec.MessageDecoder;
import codec.MessageEncoder;
import model.DnsMessage;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

public interface DnsCommunicator {

    DnsMessage sendMessage(DnsMessage query, String address) throws IOException;
}
