package resolver;

import model.DnsMessage;
import model.messageStructure.recordRegister.RR;
import network.DnsCommunicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface DnsResolver {
    public void resolve(String name);
}
