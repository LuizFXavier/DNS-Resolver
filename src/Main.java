import codec.MessageDecoder;
import codec.MessageEncoder;
import model.DnsMessage;
import model.messageStructure.DnsHeader;
import model.messageStructure.Question;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {

        DnsHeader header = new DnsHeader();
        header.flags().set_RD(true);
        header.setQuestions((short)1);
        header.setId((short) 40);

        DnsMessage message = new DnsMessage(header);
        ArrayList<Question> qs = new ArrayList<>();
        qs.add(new Question("www.ufms.br", (short) 1, (short) 1));

        message.setQuestions(qs);

        System.out.println(message);

        DatagramSocket socket = null;
        try {
            int port = 53;
            InetAddress address = InetAddress.getByName("192.5.5.241");

            byte[] coded = MessageEncoder.encode(message);

            socket = new DatagramSocket();

            System.out.println("Criando pacote...");
            DatagramPacket packet = new DatagramPacket(coded, coded.length, address, port);

            System.out.println("Enviando PACOTE...");
            socket.send(packet);

            System.out.println("Pacote Enviado com sucesso!");

            byte[] buffer = new byte[4096];
            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);

            socket.setSoTimeout(5000);
            System.out.println("Pacote aguardando recebimento...");

            socket.receive(receivedPacket);

            System.out.println("Pacote recebido!");

            System.out.println("Tamanho: " + receivedPacket.getLength());
            int responseLength = receivedPacket.getLength();

            byte[] responseData = Arrays.copyOf(receivedPacket.getData(), responseLength);


            DnsMessage response = MessageDecoder.decode(responseData);

            System.out.println(response);

        } catch (SocketTimeoutException e) {
            System.err.println("Erro: Timeout! O servidor não respondeu a tempo.");
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // 10. Fechar o Socket.
            if (socket != null && !socket.isClosed()) {
                System.out.println("Fechando o socket.");
                socket.close(); // close
            }
        }

    }
}