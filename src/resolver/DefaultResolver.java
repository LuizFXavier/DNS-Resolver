package resolver;

import model.DnsMessage;
import model.messageStructure.recordRegister.RR;
import network.DnsCommunicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultResolver implements DnsResolver {
    private List<String> rootServersIPs;
    private DnsCommunicator dnsCommunicator;

    public DefaultResolver(DnsCommunicator dnsCommunicator) {
        this.dnsCommunicator = dnsCommunicator;

        rootServersIPs = new ArrayList<>(List.of(
                "198.41.0.4",      // a.root-servers.net
                "199.9.14.201",    // b.root-servers.net
                "192.33.4.12",     // c.root-servers.net
                "199.7.91.13",     // d.root-servers.net
                "192.203.230.10",  // e.root-servers.net
                "192.5.5.241",     // f.root-servers.net
                "192.112.36.4",    // g.root-servers.net
                "198.97.190.53",   // h.root-servers.net
                "192.36.148.17",   // i.root-servers.net
                "192.58.128.30",   // j.root-servers.net
                "193.0.14.129",    // k.root-servers.net
                "199.7.83.42",     // l.root-servers.net
                "202.12.27.33"     // m.root-servers.net
        ));
    }

    private List<String> extractAddresses(DnsMessage dnsMessage){
        List<String> list = new ArrayList<>();
        for (RR record : dnsMessage.additional()){
            list.add(record.decodedRdata());
        }
        return list;
    }

    public void resolve(String name){

        List<String> targetServers = rootServersIPs;
        DnsMessage dnsQuery, dnsResponse;

        dnsQuery = new DnsMessage(name);

        boolean resolved = false;

        while(!resolved){

            for (String serverIP : targetServers) {
                System.out.println("Connecting to " + serverIP);
                try {
                    dnsResponse = dnsCommunicator.sendMessage(dnsQuery, serverIP);

                    if (dnsResponse.hasFinalAnswer()){

                        System.out.println(dnsResponse);
                        resolved = true;
                        break;
                    }
                    else{
                        System.out.println(dnsResponse);
                        targetServers = extractAddresses(dnsResponse);
                        break;
                    }

                }catch (IOException e) {
                    System.err.println("Fail on connecting to " + serverIP + ". Trying next server...");
                }
            }
            if (targetServers.isEmpty()){
                System.err.println("No util server found.");
                break;
            }
        }
    }
}
