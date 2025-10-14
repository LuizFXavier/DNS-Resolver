package resolver;

import model.DnsMessage;
import model.messageStructure.recordRegister.RR;
import network.DefaultCommunicator;
import network.DnsCommunicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DefaultResolver implements DnsResolver {
    private final List<String> rootServersIPs;
    private final DefaultCommunicator dnsCommunicator = new DefaultCommunicator();
    private final boolean recursive;

    /**
     * @param recursive Caso verdadeiro, as resoluções a serem executadas
     *                  deverão ser feitas de forma recursiva. Serão de
     *                  forma iterativa caso o contrário (ou se o servidor
     *                  alvo não suportar recursivade).
     * @param firstServer Indica qual o primeiro servidor que deverá ser
     *                    perguntado em uma resolução.
     */
    public DefaultResolver(boolean recursive, String firstServer) {
        this.recursive = recursive;

        this.rootServersIPs = new ArrayList<>(List.of(firstServer));
    }

    /**
     * @param recursive Caso verdadeiro, as resoluções a serem executadas
     *                  deverão ser feitas de forma recursiva. Serão de
     *                  forma iterativa caso o contrário (ou se o servidor
     *                  alvo não suportar recursivade).
     */
    public DefaultResolver(boolean recursive) {
        this.recursive = recursive;

        // Lista de servidores DNS raízes
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

    /**
     * Resolução DNS de modo especificado no construtor.
     * @param name Nome a ser incluído na query.
     * @param qType Tipo de query, podendo ser A ou AAAA
     */
    public void resolve(String name, String qType){

        List<String> targetServers = rootServersIPs;
        DnsMessage dnsQuery, dnsResponse;

        dnsQuery = new DnsMessage(name, qType, recursive);

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
