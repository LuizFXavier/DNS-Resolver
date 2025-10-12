import network.DefaultCommunicator;
import network.DotCommunicator;
import resolver.DefaultResolver;
import resolver.DnsResolver;
import resolver.DotResolver;

public class Main {
    public static void main(String[] args) {

        DnsResolver resolver = new DefaultResolver(new DefaultCommunicator());
//        DnsResolver resolver2 = new DotResolver(new DotCommunicator());
        resolver.resolve("dns.google");

    }
}