import network.DefaultCommunicator;
import network.DotCommunicator;
import resolver.DefaultResolver;
import resolver.DnsResolver;
import resolver.DotResolver;

public class Main {
    public static void main(String[] args) {

        CmdReturn cmd = new CmdReturn(args);

        DnsResolver resolver = cmd.resolver;

        System.out.println("Mode: " + cmd.mode);
        
        // Resolução com base nos argumentos passados
        resolver.resolve(cmd.name, cmd.qtype);
    }
}

// Classe que lerá todos os argumentos de linha de comando passados
class CmdReturn{
    public DnsResolver resolver;
    public String name = "";
    public String mode = "";
    public String SNI = "";
    public String qtype = "";
    public String ns = "";

    CmdReturn(String[] args){

        for(int i = 0; i < args.length; ++i){
            if(args[i].equals("--mode") && i+1 < args.length){
                mode = args[++i];
            }
            else if (args[i].equals("--name") && i+1 < args.length) {
                name = args[++i];
            }
            else if (args[i].equals("--sni") && i+1 < args.length) {
                SNI = args[++i];
            }
            else if (args[i].equals("--qtype") && i+1 < args.length) {
                qtype = args[++i];
            }
            else if (args[i].equals("--ns") && i+1 < args.length) {
                ns = args[++i];
            }
        }

        try{
            if(name.isBlank()){
                throw new IllegalArgumentException("Campo de nome faltante!");
            }

            if(!qtype.equals("A") && !qtype.equals("AAAA")){
                throw new IllegalArgumentException("Campo qtype inválido ou não implementado!");
            }

            // Criação de Resolver com base no --mode recebido
            if(mode.equals("iterative")){
                if(!ns.isBlank())
                    resolver = new DefaultResolver(false, ns);
                else
                    resolver = new DefaultResolver(false);
            } else if (mode.equals("dot")) {
                if(SNI.isBlank()){
                    throw new IllegalArgumentException("Campo SNI requerido neste modo!");
                }
                resolver = new DotResolver(SNI);
            } else if (mode.equals("recursive")) {
                if(!ns.isBlank())
                    resolver = new DefaultResolver(true, ns);
                else
                    resolver = new DefaultResolver(true);
            } else if (mode.equals("forwarder")) {
                if(!ns.isBlank())
                    resolver = new DefaultResolver(true, ns);
                else
                    throw new IllegalArgumentException("Campo NS requerido neste modo!");
            } else{
                throw new IllegalArgumentException("Modo de execução inválido ou não implementado!");
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        }


    }
}