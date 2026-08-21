import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class ListaA {
    private File arquivo;
    private ArrayList<Vertice> vertices;

    public ListaA(File arquivo) throws Exception {
        // Já monta a lista pelo arquivo e depois faz todas as verificações do grafo
        this.arquivo = arquivo;
        vertices = new ArrayList<>();
        leArquivo(this.arquivo);
        exibirLista();

        boolean orientado = verificaOrientacao();

        if (orientado)
            System.out.println("Grafo é orientado");
        else
            System.out.println("Grafo não orientado");

        grafoSimples();
        grafoRegular(orientado);
        grafoCompleto();
    }
    public void exibirLista() {
        // Passa por todos os vértices para mostrar a lista de vizinhos de cada um
        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            System.out.print(v.getRotulo());
            Aresta a = v.getInicio();
            // Vai seguindo de aresta em aresta até chegar ao fim da lista encadeada
            while (a != null) {
                System.out.print(" -> " + a.getDestino());
                if (a.getPeso() != null)
                    System.out.print("," + a.getPeso());
                a = a.getProx();
            }
            System.out.println();
        }
    }
    public void leArquivo(File arq) throws Exception {
        Scanner leitor = new Scanner(arq);
        // Cada linha do arquivo representa um vértice e todas as arestas que saem dele
        while (leitor.hasNextLine()) {
            String linha = leitor.nextLine().trim();
            if (!linha.isEmpty()) {
                String[] valores = linha.split("\\s+");
                Vertice v = new Vertice(valores[0]);
                // Começa no índice 1 porque o índice 0 é o nome do próprio vértice
                for (int i = 1; i < valores.length; i++) {
                    String[] dados = valores[i].split(",");
                    String destino = dados[0];
                    Aresta nova;
                    if (dados.length == 2) {
                        // Quando tem vírgula, o segundo valor é o peso da aresta
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
        // Primeiro procura na lista o vértice que será a origem
        for (int i = 0; i < vertices.size(); i++) {
            if (vertices.get(i).getRotulo().equalsIgnoreCase(origem))
                v = vertices.get(i);
        }
        if (v != null) {
            // Depois percorre as arestas dele até encontrar o destino ou acabar a lista
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
        // Para cada ligação A -> B, procura a volta B -> A
        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            Aresta a = v.getInicio();
            while (a != null) {
                String origem = v.getRotulo();
                String destino = a.getDestino();
                if (!existeAresta(destino, origem))
                    // Se não existe a volta, essa ligação tem direção
                    orientado = true;
                a = a.getProx();
            }
        }
        return orientado;
    }

    public boolean ehSimples() {
        boolean simples = true;
        // Primeiro procura laços, ou seja, arestas que voltam para o mesmo vértice
        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            Aresta a = v.getInicio();
            while (a != null) {
                if (a.getDestino().equalsIgnoreCase(v.getRotulo()))
                    simples = false;
                a = a.getProx();
            }
        }
        // Agora compara as arestas de cada vértice para achar destinos repetidos
        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            Aresta a = v.getInicio();
            while (a != null) {
                Aresta aux = a.getProx();
                // A auxiliar começa na próxima para não comparar uma aresta com ela mesma
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
        // Mostra na tela o resultado encontrado pelo método acima
        if (ehSimples())
            System.out.println("Grafo é simples");
        else
            System.out.println("Grafo não é simples");
    }

    public void grafoRegular(boolean orientado) {
        boolean flag = true;
        if (!orientado) {
            // Conta as arestas do primeiro vértice e usa esse grau como referência
            int grauReferencia = 0;
            Aresta a = vertices.get(0).getInicio();
            while (a != null) {
                grauReferencia++;
                a = a.getProx();
            }
            System.out.println(vertices.get(0).getRotulo() + " - Grau: " + grauReferencia);
            // Faz a mesma contagem nos demais e compara com o primeiro
            for (int i = 1; i < vertices.size(); i++) {
                int grauAtual = 0;
                a = vertices.get(i).getInicio();
                while (a != null) {
                    grauAtual++;
                    a = a.getProx();
                }
                System.out.println(vertices.get(i).getRotulo() + " - Grau: " + grauAtual);
                if (grauAtual != grauReferencia)
                    flag = false;
            }
        }
        else {
            // Em grafo orientado precisamos saber quantas arestas saem e chegam
            int emissaoReferencia = 0;
            int recepcaoReferencia = 0;
            Aresta a = vertices.get(0).getInicio();
            while (a != null) {
                emissaoReferencia++;
                a = a.getProx();
            }
            String primeiroRotulo = vertices.get(0).getRotulo();
            // Procura o primeiro vértice como destino para descobrir seu grau de entrada
            for (int i = 0; i < vertices.size(); i++) {
                Aresta aux = vertices.get(i).getInicio();
                while (aux != null) {
                    if (aux.getDestino().equalsIgnoreCase(primeiroRotulo))
                        recepcaoReferencia++;
                    aux = aux.getProx();
                }
            }
            // Repete as duas contagens para cada vértice do grafo
            for (int i = 0; i < vertices.size(); i++) {
                Vertice v = vertices.get(i);
                int emissao = 0;
                int recepcao = 0;
                a = v.getInicio();
                while (a != null) {
                    emissao++;
                    a = a.getProx();
                }
                for (int j = 0; j < vertices.size(); j++) {
                    // Aqui percorre todas as listas procurando arestas que chegam no vértice atual
                    Aresta aux = vertices.get(j).getInicio();
                    while (aux != null) {
                        if (aux.getDestino().equalsIgnoreCase(v.getRotulo()))
                            recepcao++;
                        aux = aux.getProx();
                    }
                }
                System.out.println(v.getRotulo() + " - Emissão: " + emissao + " | Recepção: " + recepcao);
                if (emissao != emissaoReferencia || recepcao != recepcaoReferencia)
                    flag = false;
            }
        }
        if (flag)
            System.out.println("Grafo é regular");
        else
            System.out.println("Grafo não é regular");
    }

    public void grafoCompleto() {
        boolean completo = true;
        // Um grafo completo também precisa ser simples
        if (!ehSimples())
            completo = false;
        // Cada vértice deve estar ligado a todos os outros, menos a ele mesmo
        for (int i = 0; i < vertices.size(); i++) {
            int cont = 0;
            Aresta a = vertices.get(i).getInicio();
            while (a != null) {
                cont++;
                a = a.getProx();
            }
            if (cont != vertices.size() - 1)
                completo = false;
        }
        if (completo)
            System.out.println("Grafo completo K" + vertices.size());
        else
            System.out.println("Grafo incompleto");
    }
}
