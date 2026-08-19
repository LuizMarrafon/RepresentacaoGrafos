import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class MatrizI {

    private int[][] matriz;
    private String[] rotulo;
    private String[] rotuloAresta;
    private File arquivo;

    public MatrizI(File arquivo) throws Exception {
        this.arquivo = arquivo;
        lerArquivo(this.arquivo);
        exibirMatriz();

        boolean orientado = verificaOrientacaoMI();

        if (orientado)
            System.out.println("Grafo é orientado");
        else
            System.out.println("Grafo não orientado");

        if (orientado) {
            grafoSimplesMI();
        }

        grafoRegularMI();
        grafoCompletoMI();
    }

    public void lerArquivo(File arq) throws Exception {
        Scanner leitor = new Scanner(arq);

        String primeiraLinha = leitor.nextLine();
        rotulo = primeiraLinha.trim().split("[/\\s]+");
        int tamanho = rotulo.length;

        String linha = leitor.nextLine();
        rotuloAresta = linha.trim().split("[/\\s]+");

        for (int i = 0; i < tamanho; i++) {
            linha = leitor.nextLine();
            String[] valores = linha.trim().split("\\s+");

            if (i == 0) {
                matriz = new int[tamanho][valores.length];
            } else if (valores.length != matriz[0].length) {
                leitor.close();
                throw new Exception("Todas as linhas da matriz devem ter a mesma quantidade de colunas.");
            }

            for (int j = 0; j < valores.length; j++) {
                matriz[i][j] = Integer.parseInt(valores[j]);
            }
        }

        leitor.close();
    }

    public void exibirMatriz() {
        if (rotuloAresta != null) {
            System.out.printf("%6s", "");

            for (int i = 0; i < rotuloAresta.length; i++) {
                System.out.printf("%8s", rotuloAresta[i]);
            }

            System.out.println();
        }

        for (int i = 0; i < matriz.length; i++) {
            System.out.printf("%6s", rotulo[i]);

            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("%8d", matriz[i][j]);
            }

            System.out.println();
        }
    }

    public boolean verificaOrientacaoMI() {
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] < 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public void grafoSimplesMI() { // Método que verifica se o grafo da matriz de incidência é simples.
        ArrayList<String> arestas = new ArrayList<String>(); // Lista usada para guardar as arestas que já foram encontradas.
        int quantidadeArestas = matriz[0].length; // A quantidade de colunas representa a quantidade de arestas.

        for (int coluna = 0; coluna < quantidadeArestas; coluna++) { // Percorre cada coluna da matriz, ou seja, cada aresta.
            int origem = -1; // Guarda a posição do vértice de origem da aresta.
            int destino = -1; // Guarda a posição do vértice de destino da aresta.

            for (int linha = 0; linha < matriz.length; linha++) { // Percorre cada linha da coluna atual, ou seja, cada vértice.
                if (matriz[linha][coluna] < 0) { // Se encontrou valor negativo, esse vértice é a origem da aresta.
                    origem = linha; // Salva o índice da linha como origem.
                }

                if (matriz[linha][coluna] > 0) { // Se encontrou valor positivo, esse vértice é o destino da aresta.
                    destino = linha; // Salva o índice da linha como destino.
                }
            }

            String aresta = origem + "->" + destino; // Monta a aresta em formato de texto, por exemplo: 0->2.

            if (arestas.contains(aresta)) { // Verifica se essa mesma aresta já apareceu antes.
                System.out.println("Grafo apresenta arestas paralelas, não é simples"); // Se apareceu, existem arestas paralelas.
                return; // Encerra o método porque o grafo já não é simples.
            } else { // Se a aresta ainda não apareceu.
                arestas.add(aresta); // Adiciona a aresta na lista de arestas já encontradas.
            }
        }

        System.out.println("Grafo é simples"); // Se terminou sem achar laço ou repetição, o grafo é simples.
    }

    public void grafoRegularMI() {
        // Verifica se a matriz de incidência representa um grafo orientado.
        boolean orientado = verificaOrientacaoMI();

        boolean flag = true; // Começa assumindo que o grafo é regular.
        int grauReferencia = 0; // Guarda o grau do primeiro vértice para comparar com os outros.

        // Calcula o grau do primeiro vértice, que está na primeira linha da matriz.
        for (int j = 0; j < matriz[0].length; j++) {
            if (orientado) {
                // Em grafo orientado, -1 e 1 contam como incidência, então usamos valor absoluto.
                grauReferencia += Math.abs(matriz[0][j]);
            } else {
                // Em grafo não orientado, a matriz normalmente possui apenas 0 e 1.
                grauReferencia += matriz[0][j];
            }
        }

        // Calcula o grau dos demais vértices e compara com o grau do primeiro.
        for (int i = 1; i < matriz.length; i++) {
            int grauAtual = 0; // Guarda o grau do vértice da linha atual.

            for (int j = 0; j < matriz[i].length; j++) {
                if (orientado) {
                    // Math.abs evita que o -1 diminua o grau.
                    grauAtual += Math.abs(matriz[i][j]);
                } else {
                    grauAtual += matriz[i][j];
                }
            }

            // Se algum vértice tiver grau diferente, o grafo não é regular.
            if (grauAtual != grauReferencia)
                flag = false;
        }

        if(flag)
            System.out.println("É regular");
        else
            System.out.println("Não regular");
    }

    public void grafoCompletoMI() {
        boolean flag = true; // Começa assumindo que o grafo é completo.

        // Percorre todos os pares de vértices diferentes.
        for (int i = 0; i < matriz.length; i++) {
            for (int j = i + 1; j < matriz.length; j++) {
                boolean verticesConectados = false; // Indica se existe aresta entre i e j.

                // Em matriz de incidência, cada coluna representa uma aresta.
                for (int coluna = 0; coluna < matriz[i].length; coluna++) {
                    // Math.abs permite considerar tanto MI orientada (-1 e 1) quanto não orientada (1 e 1).
                    if (Math.abs(matriz[i][coluna]) != 0 && Math.abs(matriz[j][coluna]) != 0) {
                        verticesConectados = true;
                    }
                }

                // Se algum par de vértices não estiver conectado, o grafo não é completo.
                if (!verticesConectados)
                    flag = false;
            }
        }

        if(flag)
            System.out.println("Grafo completo de K"+rotulo.length);
        else
            System.out.println("Grafo incompleto");
    }
}