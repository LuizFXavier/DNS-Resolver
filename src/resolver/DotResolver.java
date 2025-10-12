package resolver;

import model.DnsMessage;
import network.DnsCommunicator;

import java.io.IOException;

public class DotResolver implements DnsResolver{
    private DnsCommunicator dnsCommunicator;

    public DotResolver(DnsCommunicator dnsCommunicator) {
        this.dnsCommunicator = dnsCommunicator;
    }

    @Override
    public void resolve(String name) {
        DnsMessage dnsQuery = new DnsMessage(name);

        try {
            DnsMessage dnsResponse = dnsCommunicator.sendMessage(dnsQuery, "dns.google");
            System.out.println(dnsResponse);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
