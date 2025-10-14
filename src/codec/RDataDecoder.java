package codec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Classe para a decodificação específica dos tipos de registros do DNS
 */
public class RDataDecoder {
    /**
     * @param buffer Buffer de bytes contendo o endereço IPv4.
     * @return Endereço IPv4 em formato de texto.
     */
    static String decodeIPv4(ByteBuffer buffer) {

        return (buffer.get() & 0xFF) + "." +
                (buffer.get() & 0xFF) + "." +
                (buffer.get() & 0xFF) + "." +
                (buffer.get() & 0xFF);
    }
    /**
     * @param buffer Buffer de bytes contendo o endereço IPv6.
     * @return Endereço IPv6 em formato de texto.
     */
    static String decodeIPv6(ByteBuffer buffer) {
        return IntStream.range(0, 8)
                .mapToObj(i -> Integer.toHexString(buffer.getShort() & 0xFFFF))
                .collect(Collectors.joining(":"));
    }

    /**
     * Decodificação dos nomes do DNS de bytes para string,
     * seguindo a formatação específica e a lógica de ponteiros.
     * @param buffer Buffer de bytes que contém o nome.
     * @return Nome contido no buffer, agora decodificado.
     */
    static String decodeName(ByteBuffer buffer) {
        StringBuilder name = new StringBuilder();
        int finalPosition = -1;
        boolean jumped = false;

        while (true) {
            byte b = buffer.get();
            if ((b & 0xC0) == 0xC0) { // Significa que esse byte é um ponteiro
                if (!jumped) {
                    finalPosition = buffer.position() + 1; // Posição após o ponteiro de 2 bytes
                    jumped = true;
                }
                byte b2 = buffer.get();
                int offset = ((b & 0x3F) << 8) | (b2 & 0xFF);
                buffer.position(offset); // Pula para o local correto
            } else if (b == 0x00) { // Fim do nome
                break;
            } else { // Esse byte é um label
                if (name.length() > 0) {
                    name.append('.');
                }
                byte[] labelBytes = new byte[b];
                buffer.get(labelBytes);
                name.append(new String(labelBytes, StandardCharsets.UTF_8));
            }
        }

        if (jumped) {
            buffer.position(finalPosition);
        }

        return name.toString();
    }
}
