package network;

import codec.MessageDecoder;
import codec.MessageEncoder;
import model.DnsMessage;

import javax.net.ssl.SNIHostName;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class DotCommunicator implements DnsCommunicator{
    private final int PORT = 853;

    @Override
    public DnsMessage sendMessage(DnsMessage query, String serverName) throws IOException {

        byte[] codedQuery = MessageEncoder.encode(query);
        try(SSLSocket socket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(serverName, PORT)) {

            SSLParameters parameters = socket.getSSLParameters();
            parameters.setServerNames(List.of(new SNIHostName(serverName)));
            socket.setSSLParameters(parameters);

            socket.startHandshake();

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            int msgLen = codedQuery.length;
            ByteBuffer byteBuffer = ByteBuffer.allocate(2);
            byteBuffer.putShort((short) msgLen);
            out.write(byteBuffer.array());

            // Escrita da query e envio dos dados de imediato
            out.write(codedQuery);
            out.flush();

            int responseLength = in.read() << 8 | in.read();

            byte[] responseData = new byte[responseLength];

            int bytesReceived = in.readNBytes(responseData, 0, responseLength);

            if (bytesReceived < responseLength) {
                throw new IOException("Incomplete response received!");
            }

            return MessageDecoder.decode(responseData);
        }

    }
}
