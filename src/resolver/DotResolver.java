package resolver;

import model.DnsMessage;
import network.DotCommunicator;

import java.io.IOException;

public class DotResolver implements DnsResolver{
    private final DotCommunicator dnsCommunicator = new DotCommunicator();
    String SNI;

    public DotResolver(String SNI) {
        this.SNI = SNI;
    }

    @Override
    public void resolve(String name, String qType) {
        DnsMessage dnsQuery = new DnsMessage(name, qType, true);

        try {
            DnsMessage dnsResponse = dnsCommunicator.sendMessage(dnsQuery, SNI);
            System.out.println(dnsResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
