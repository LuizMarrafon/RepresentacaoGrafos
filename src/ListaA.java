import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ListaA {
    private File arquivo;
    private ArrayList<Vertice> vertices;

    public ListaA(File arquivo) throws Exception {
        this.arquivo = arquivo;
        vertices = new ArrayList<>();
        leArquivo(this.arquivo);

        boolean orientado = verificaOrientacao();

        if (orientado)
            System.out.println("Grafo é orientado");
        else
            System.out.println("Grafo não orientado");

        grafoSimples();
        grafoRegular(orientado);
        grafoCompleto(orientado);
    }

    public void leArquivo(File arq) throws Exception {
        Scanner leitor = new Scanner(arq);
        while (leitor.hasNextLine()) {
            String linha = leitor.nextLine().trim();
            if (!linha.isEmpty()) {
                String[] valores = linha.split("\\s+");
                Vertice v = new Vertice(valores[0]);
                for (int i = 1; i < valores.length; i++) {
                    String[] dados = valores[i].split(",");
                    String destino = dados[0];
                    Aresta nova;
                    if (dados.length == 2) {
                        int peso = Integer.parseInt(dados[1]);
                        nova = new Aresta(destino, peso);
                    } else {
                        nova = new Aresta(destino);
                    }
                    v.adicionarAresta(nova);
                }
                vertices.add(v);
            }
        }
        leitor.close();
    }

    public boolean existeAresta(String origem, String destino) {
        boolean existe = false;
        Vertice v = null;
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getRotulo().equalsIgnoreCase(origem))
                v = vertices.get(i);
        }
        if (v != null) {
            Aresta a = v.getInicio();
            while (a != null && !existe) {
                if (a.getDestino().equalsIgnoreCase(destino))
                    existe = true;
                a = a.getProx();
            }
        }
        return existe;
    }

    public boolean verificaOrientacao() {
        boolean orientado = false;
        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            Aresta a = v.getInicio();
            while (a != null) {
                String origem = v.getRotulo();
                String destino = a.getDestino();
                if (!existeAresta(destino, origem))
                    orientado = true;
                a = a.getProx();
            }
        }
        return orientado;
    }

    public boolean ehSimples() {
        boolean simples = true;
        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            Aresta a = v.getInicio();
            while (a != null) {
                if (a.getDestino().equalsIgnoreCase(v.getRotulo()))
                    simples = false;
                a = a.getProx();
            }
        }

        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            Aresta a = v.getInicio();
            while (a != null) {
                Aresta aux = a.getProx();
                while (aux != null) {
                    if (a.getDestino().equalsIgnoreCase(aux.getDestino()))
                        simples = false;
                    aux = aux.getProx();
                }
                a = a.getProx();
            }
        }
        return simples;
    }

    public void grafoSimples() {
        if (ehSimples())
            System.out.println("Grafo é simples");
        else
            System.out.println("Grafo não é simples");
    }

    public void grafoRegular(boolean orientado) {
        boolean flag = true;
        if (!orientado) {
            int grauReferencia = 0;
            Aresta a = vertices.get(0).getInicio();
            while(a != null) {
                grauReferencia++;
                a = a.getProx();
            }
            for (int i = 1; i < vertices.size(); i++) {
                int grauAtual = 0;
                a = vertices.get(i).getInicio();
                while (a != null) {
                    grauAtual++;
                    a = a.getProx();
                }
                if (grauAtual != grauReferencia)
                    flag = false;
            }
        }
        else{
            int emissaoReferencia = 0;
            int recepcaoReferencia = 0;
            Aresta a = vertices.get(0).getInicio();
            while(a != null) {
                emissaoReferencia++;
                a = a.getProx();
            }
            String primeiroRotulo = vertices.get(0).getRotulo();
            for(int i = 0; i < vertices.size(); i++) {
                Aresta aux = vertices.get(i).getInicio();
                while(aux != null){
                    if (aux.getDestino().equalsIgnoreCase(primeiroRotulo))
                        recepcaoReferencia++;
                    aux = aux.getProx();
                }
            }
            for(int i = 0; i < vertices.size(); i++) {
                Vertice v = vertices.get(i);
                int emissao = 0;
                int recepcao = 0;
                a = v.getInicio();
                while(a != null){
                    emissao++;
                    a = a.getProx();
                }
                for (int j = 0; j < vertices.size(); j++) {
                    Aresta aux = vertices.get(j).getInicio();
                    while (aux != null) {
                        if (aux.getDestino().equalsIgnoreCase(v.getRotulo()))
                            recepcao++;
                        aux = aux.getProx();
                    }
                }
                System.out.println(v.getRotulo() + " - Emissão: " + emissao + " | Recepção: " + recepcao);
                if(emissao != emissaoReferencia || recepcao != recepcaoReferencia)
                    flag = false;
            }
        }
        if(flag)
            System.out.println("Grafo é regular");
        else
            System.out.println("Grafo não é regular");
    }
    public void grafoCompleto(boolean orientado) {
        boolean completo = true;
        if(!ehSimples())
            completo = false;
        if(orientado)
            completo = false;
        for(int i = 0; i < vertices.size(); i++){
            int cont = 0;
            Aresta a = vertices.get(i).getInicio();
            while (a != null) {
                cont++;
                a = a.getProx();
            }
            if(cont != vertices.size() - 1)
                completo = false;
        }
        if(completo)
            System.out.println("Grafo completo K" + vertices.size());
        else
            System.out.println("Grafo incompleto");
    }
}