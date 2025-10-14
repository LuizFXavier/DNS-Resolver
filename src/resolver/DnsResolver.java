package resolver;

public interface DnsResolver {
    void resolve(String name, String qtype);
}
