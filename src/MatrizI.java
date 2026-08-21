import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class MatrizI {

    private int[][] matriz;
    private String[] rotulo;
    private String[] rotuloAresta;
    private File arquivo;
    private boolean simplesVerificado = false;
    private boolean grafoSimples = false;

    public MatrizI(File arquivo) throws Exception {
        // Ao criar a matriz, já carrega o arquivo e roda todas as análises do grafo
        this.arquivo = arquivo;
        lerArquivo(this.arquivo);
        exibirMatriz();

        boolean orientado = verificaOrientacaoMI();

        if (orientado)
            System.out.println("Grafo é orientado");
        else
            System.out.println("Grafo não orientado");

        grafoSimplesMI();
        grafoRegularMI();
        grafoCompletoMI();
    }

    public void lerArquivo(File arq) throws Exception {
        Scanner leitor = new Scanner(arq);

        // A primeira linha guarda os nomes dos vértices
        String primeiraLinha = leitor.nextLine();
        rotulo = primeiraLinha.trim().split("[/\\s]+");
        int tamanho = rotulo.length;

        // A segunda linha guarda os nomes das arestas, que serão as colunas da matriz
        String linha = leitor.nextLine();
        rotuloAresta = linha.trim().split("[/\\s]+");

        // Lê uma linha para cada vértice e transforma os valores do arquivo em números
        for (int i = 0; i < tamanho; i++) {
            linha = leitor.nextLine();
            String[] valores = linha.trim().split("\\s+");

            if (i == 0) {
                // Só aqui descobrimos quantas colunas a matriz realmente vai ter
                matriz = new int[tamanho][valores.length];
            } else if (valores.length != matriz[0].length) {
                // Se uma linha vier com tamanho diferente, o arquivo está montado errado
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
            // Mostra primeiro o cabeçalho com o nome de cada aresta
            System.out.printf("%6s", "");

            for (int i = 0; i < rotuloAresta.length; i++) {
                System.out.printf("%8s", rotuloAresta[i]);
            }

            System.out.println();
        }

        // Depois imprime cada vértice junto com sua linha da matriz
        for (int i = 0; i < matriz.length; i++) {
            System.out.printf("%6s", rotulo[i]);

            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("%8d", matriz[i][j]);
            }

            System.out.println();
        }
    }

    public boolean verificaOrientacaoMI() {
        // Na matriz de incidência, valor negativo indica a origem de uma aresta orientada
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] < 0) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean grafoSimplesMI() { // Método que verifica se o grafo da matriz de incidência é simples.
        boolean orientado = verificaOrientacaoMI();
        boolean simples = true;
        ArrayList<String> arestas = new ArrayList<String>();
        int quantidadeArestas = matriz[0].length;

        // Analisa uma aresta por vez. Se achar algum problema, já pode parar o loop
        for (int coluna = 0; coluna < quantidadeArestas && simples; coluna++) {
            String aresta = "";

            if (orientado) {
                // Em uma aresta orientada esperamos exatamente uma origem e um destino
                int origem = -1;
                int destino = -1;
                int quantidadeOrigem = 0;
                int quantidadeDestino = 0;

                for (int linha = 0; linha < matriz.length; linha++) {
                    if (matriz[linha][coluna] < 0) {
                        origem = linha;
                        quantidadeOrigem++;
                    } else if (matriz[linha][coluna] > 0) {
                        destino = linha;
                        quantidadeDestino++;
                    }
                }

                if (quantidadeOrigem != 1 || quantidadeDestino != 1 || origem == destino) {
                    simples = false;
                } else {
                    aresta = origem + "->" + destino;
                }
            } else {
                // Sem orientação, a coluna deve ligar exatamente dois vértices
                int primeiroVertice = -1;
                int segundoVertice = -1;
                int quantidadeIncidencias = 0;

                for (int linha = 0; linha < matriz.length; linha++) {
                    if (matriz[linha][coluna] != 0) {
                        quantidadeIncidencias++;

                        if (primeiroVertice == -1)
                            primeiroVertice = linha;
                        else if (segundoVertice == -1)
                            segundoVertice = linha;
                    }
                }

                if (quantidadeIncidencias != 2) {
                    simples = false;
                } else {
                    int menor = Math.min(primeiroVertice, segundoVertice);
                    int maior = Math.max(primeiroVertice, segundoVertice);
                    aresta = menor + "-" + maior;
                }
            }

            if (arestas.contains(aresta)) {
                // Se essa mesma ligação já apareceu, existem arestas paralelas
                simples = false;
            } else {
                arestas.add(aresta);
            }
        }

        simplesVerificado = true;
        grafoSimples = simples;

        if (simples)
            System.out.println("Grafo é simples");
        else
            System.out.println("Grafo apresenta laço ou arestas paralelas, não é simples");

        return simples;
    }

    public boolean grafoRegularMI() {
        boolean orientado = verificaOrientacaoMI();
        boolean regular = true;

        if (orientado) {
            // Pega os graus de entrada e saída do primeiro vértice como referência
            int grauSaidaReferencia = 0;
            int grauEntradaReferencia = 0;

            for (int coluna = 0; coluna < matriz[0].length; coluna++) {
                if (matriz[0][coluna] < 0)
                    grauSaidaReferencia++;
                else if (matriz[0][coluna] > 0)
                    grauEntradaReferencia++;
            }

            // Compara os graus de todos os outros vértices com essa referência
            for (int linha = 1; linha < matriz.length; linha++) {
                int grauSaidaAtual = 0;
                int grauEntradaAtual = 0;

                for (int coluna = 0; coluna < matriz[linha].length; coluna++) {
                    if (matriz[linha][coluna] < 0)
                        grauSaidaAtual++;
                    else if (matriz[linha][coluna] > 0)
                        grauEntradaAtual++;
                }

                if (grauSaidaAtual != grauSaidaReferencia || grauEntradaAtual != grauEntradaReferencia)
                    regular = false;
            }
        } else {
            // No não orientado basta contar quantas arestas encostam em cada vértice
            int grauReferencia = 0;

            for (int coluna = 0; coluna < matriz[0].length; coluna++) {
                if (matriz[0][coluna] != 0) {
                    int quantidadeIncidencias = 0;

                    for (int linha = 0; linha < matriz.length; linha++) {
                        if (matriz[linha][coluna] != 0)
                            quantidadeIncidencias++;
                    }

                    if (quantidadeIncidencias == 1)
                        // Um laço conta duas vezes no grau do vértice
                        grauReferencia += 2;
                    else
                        grauReferencia++;
                }
            }

            // Repete a mesma contagem para os outros vértices e compara os resultados
            for (int linha = 1; linha < matriz.length; linha++) {
                int grauAtual = 0;

                for (int coluna = 0; coluna < matriz[linha].length; coluna++) {
                    if (matriz[linha][coluna] != 0) {
                        int quantidadeIncidencias = 0;

                        for (int outraLinha = 0; outraLinha < matriz.length; outraLinha++) {
                            if (matriz[outraLinha][coluna] != 0)
                                quantidadeIncidencias++;
                        }

                        if (quantidadeIncidencias == 1)
                            grauAtual += 2;
                        else
                            grauAtual++;
                    }
                }

                if (grauAtual != grauReferencia)
                    regular = false;
            }
        }

        if (regular)
            System.out.println("É regular");
        else
            System.out.println("Não regular");

        return regular;
    }

    public boolean grafoCompletoMI() {
        boolean orientado = verificaOrientacaoMI();
        boolean completo = true;

        if (!simplesVerificado)
            // Evita depender da ordem em que os métodos foram chamados
            grafoSimplesMI();

        if (!grafoSimples) {
            completo = false;
        } else if (orientado) {
            // Testa cada par possível, pois deve existir uma ligação em cada direção
            for (int origem = 0; origem < matriz.length; origem++) {
                for (int destino = 0; destino < matriz.length; destino++) {
                    if (origem != destino) {
                        int quantidadeLigacoes = 0;

                        // Procura, coluna por coluna, a aresta exata entre essa origem e destino
                        for (int coluna = 0; coluna < matriz[0].length; coluna++) {
                            int origemAresta = -1;
                            int destinoAresta = -1;
                            int quantidadeOrigem = 0;
                            int quantidadeDestino = 0;

                            for (int linha = 0; linha < matriz.length; linha++) {
                                if (matriz[linha][coluna] < 0) {
                                    origemAresta = linha;
                                    quantidadeOrigem++;
                                } else if (matriz[linha][coluna] > 0) {
                                    destinoAresta = linha;
                                    quantidadeDestino++;
                                }
                            }

                            if (quantidadeOrigem == 1 && quantidadeDestino == 1
                                    && origemAresta == origem && destinoAresta == destino) {
                                quantidadeLigacoes++;
                            }
                        }

                        if (quantidadeLigacoes != 1)
                            completo = false;
                    }
                }
            }
        } else {
            // No não orientado, i + 1 evita conferir o mesmo par duas vezes
            for (int i = 0; i < matriz.length; i++) {
                for (int j = i + 1; j < matriz.length; j++) {
                    int quantidadeLigacoes = 0;

                    // Conta quantas arestas ligam exatamente o par que está sendo testado
                    for (int coluna = 0; coluna < matriz[0].length; coluna++) {
                        int primeiroVertice = -1;
                        int segundoVertice = -1;
                        int quantidadeIncidencias = 0;

                        for (int linha = 0; linha < matriz.length; linha++) {
                            if (matriz[linha][coluna] != 0) {
                                quantidadeIncidencias++;

                                if (primeiroVertice == -1)
                                    primeiroVertice = linha;
                                else if (segundoVertice == -1)
                                    segundoVertice = linha;
                            }
                        }

                        if (quantidadeIncidencias == 2) {
                            int menor = Math.min(primeiroVertice, segundoVertice);
                            int maior = Math.max(primeiroVertice, segundoVertice);

                            if (menor == i && maior == j)
                                quantidadeLigacoes++;
                        }
                    }

                    if (quantidadeLigacoes != 1)
                        completo = false;
                }
            }
        }
        if (completo)
            System.out.println("Grafo completo K " + rotulo.length);
        else
            System.out.println("Grafo não é completo");

        return completo;
    }
}
