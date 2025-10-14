package network;

import codec.MessageDecoder;
import codec.MessageEncoder;
import model.DnsMessage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class DefaultCommunicator implements DnsCommunicator {

    private static final int PORT = 53;

    // Envio de mensagem DNS, primeiro por UDP e com fallback para TCP
    @Override
    public DnsMessage sendMessage(DnsMessage query, String address) throws IOException {

        byte[] codedQuery = MessageEncoder.encode(query);

        DnsMessage firstResponse = sendMessageUDP(codedQuery, address);

        if (!firstResponse.trucated())
            return firstResponse;

        return sendMessageTCP(codedQuery, address);
    }

    // Envio da mensagem DNS pelo protocolo TCP
    private DnsMessage sendMessageTCP(byte[] codedQuery, String address) throws IOException{

        try(Socket socket = new Socket(address, PORT)) {
            socket.setSoTimeout(5000);
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

    // Envio da mensagem DNS pelo protocolo UDP
    private DnsMessage sendMessageUDP(byte[] codedQuery, String address) throws IOException {

        // Criação do socket UDP
        try(DatagramSocket socket = new DatagramSocket()){

            // Preparação e envio do pacote
            InetAddress inetAddress = InetAddress.getByName(address);
            DatagramPacket packet = new DatagramPacket(codedQuery, codedQuery.length, inetAddress, PORT);

            socket.send(packet);

            // Recebimento da resposta
            byte[] buffer = new byte[4096];
            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);

            socket.setSoTimeout(5000);

            socket.receive(receivedPacket);

            int responseLength = receivedPacket.getLength();
            byte[] responseData = Arrays.copyOf(receivedPacket.getData(), responseLength);

            // Retorno da mensagem decodificada para alto nível
            return MessageDecoder.decode(responseData);

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }catch (SocketException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }
}
