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
        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            System.out.print(v.getRotulo());
            Aresta a = v.getInicio();
            //exibir simples como uma lista ligada
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
        while (leitor.hasNextLine()) {
            String linha = leitor.nextLine().trim();
            if (!linha.isEmpty()) {
                String[] valores = linha.split("\\s+");
                //cria uma classe vertice, onde ele tem o vertice inicial tipo A, de A -> B
                Vertice v = new Vertice(valores[0]);
                for (int i = 1; i < valores.length; i++) {
                    //agora pera a String valores ja splitada e splita dnv, deixando só as ligações do vertice.
                    //ai se a String tiver tamanho 2, significa q é valorado
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
        //sempre pega uma ligação e inverte ela, ai chama a função no if pra verificar e retornar um booleano
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
        //procura os laços
        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            Aresta a = v.getInicio();
            while (a != null) {
                if (a.getDestino().equalsIgnoreCase(v.getRotulo()))
                    simples = false;
                a = a.getProx();
            }
        }
        //verifica se é um multigrafo
        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            Aresta a = v.getInicio();
            while (a != null) {
                Aresta aux = a.getProx();
                //a variavel aux começa na próxima para não comparar uma aresta com ela mesma
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
        //só chaam a função pra printar
        if (ehSimples())
            System.out.println("Grafo é simples");
        else
            System.out.println("Grafo não é simples");
    }

    public void grafoRegular(boolean orientado) {
        boolean flag = true;
        if (!orientado) {
            //ta contando as arestas do primeiro vértice e usa esse grau como referência
            int grauReferencia = 0;
            Aresta a = vertices.get(0).getInicio();
            while (a != null) {
                grauReferencia++;
                a = a.getProx();
            }
            System.out.println(vertices.get(0).getRotulo() + " - Grau: " + grauReferencia);
            //faz a mesma contagem nos outros e compara com o primeiro
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
            //como é orientado precisa ver a emissao e recepção
            boolean regularEmissao = true;
            boolean regularRecepcao = true;
            int emissaoReferencia = 0;
            int recepcaoReferencia = 0;
            Aresta a = vertices.get(0).getInicio();
            while (a != null) {
                emissaoReferencia++;
                a = a.getProx();
            }
            String primeiroRotulo = vertices.get(0).getRotulo();
            //procura o primeiro vértice como destino para descobrir seu grau de entrada
            for (int i = 0; i < vertices.size(); i++) {
                Aresta aux = vertices.get(i).getInicio();
                while (aux != null) {
                    if (aux.getDestino().equalsIgnoreCase(primeiroRotulo))
                        recepcaoReferencia++;
                    aux = aux.getProx();
                }
            }
            //repete as duas contagens para cada vértice do grafo
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
                    //aqui ta percorrendo todas as listas procurando arestas que chegam no vértice atual
                    Aresta aux = vertices.get(j).getInicio();
                    while (aux != null) {
                        if (aux.getDestino().equalsIgnoreCase(v.getRotulo()))
                            recepcao++;
                        aux = aux.getProx();
                    }
                }
                System.out.println(v.getRotulo() + " - Emissão: " + emissao + " | Recepção: " + recepcao);
                if (emissao != emissaoReferencia)
                    regularEmissao = false;
                if (recepcao != recepcaoReferencia)
                    regularRecepcao = false;
            }

            if (regularEmissao)
                System.out.println("Grafo é regular de emissão");
            else
                System.out.println("Grafo não é regular de emissão");

            if (regularRecepcao)
                System.out.println("Grafo é regular de recepção");
            else
                System.out.println("Grafo não é regular de recepção");

            flag = regularEmissao && regularRecepcao;
        }
        if (!orientado) {
            if (flag)
                System.out.println("Grafo é regular");
            else
                System.out.println("Grafo não é regular");
        }
    }

    public void grafoCompleto() {
        boolean completo = true;
        if (!ehSimples())
            completo = false;
        //cada vértice deve estar ligado a todos os outros, menos a ele mesmo
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
