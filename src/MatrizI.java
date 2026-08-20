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

    public boolean grafoSimplesMI() { // Método que verifica se o grafo da matriz de incidência é simples.
        boolean orientado = verificaOrientacaoMI();
        boolean simples = true;
        ArrayList<String> arestas = new ArrayList<String>();
        int quantidadeArestas = matriz[0].length;

        for (int coluna = 0; coluna < quantidadeArestas && simples; coluna++) {
            String aresta = "";

            if (orientado) {
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
            int grauSaidaReferencia = -1;
            int grauEntradaReferencia = -1;

            for (int linha = 0; linha < matriz.length; linha++) {
                int grauSaidaAtual = 0;
                int grauEntradaAtual = 0;

                for (int coluna = 0; coluna < matriz[linha].length; coluna++) {
                    if (matriz[linha][coluna] < 0)
                        grauSaidaAtual++;
                    else if (matriz[linha][coluna] > 0)
                        grauEntradaAtual++;
                }

                System.out.println(rotulo[linha] + " - Emissão: " + grauSaidaAtual + " | Recepção: " + grauEntradaAtual);

                if (linha == 0) {
                    grauSaidaReferencia = grauSaidaAtual;
                    grauEntradaReferencia = grauEntradaAtual;
                } else if (grauSaidaAtual != grauSaidaReferencia || grauEntradaAtual != grauEntradaReferencia) {
                    regular = false;
                }
            }
        } else {
            int grauReferencia = -1;

            for (int linha = 0; linha < matriz.length; linha++) {
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

                System.out.println(rotulo[linha] + " - Grau: " + grauAtual);

                if (linha == 0) {
                    grauReferencia = grauAtual;
                } else if (grauAtual != grauReferencia) {
                    regular = false;
                }
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
            grafoSimplesMI();

        if (!grafoSimples) {
            completo = false;
        } else if (orientado) {
            for (int origem = 0; origem < matriz.length; origem++) {
                for (int destino = 0; destino < matriz.length; destino++) {
                    if (origem != destino) {
                        int quantidadeLigacoes = 0;

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
            for (int i = 0; i < matriz.length; i++) {
                for (int j = i + 1; j < matriz.length; j++) {
                    int quantidadeLigacoes = 0;

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

        if (completo) {
            if (orientado)
                System.out.println("Grafo completo");
            else
                System.out.println("Grafo completo de K" + rotulo.length);
        } else {
            System.out.println("Grafo não é completo");
        }

        return completo;
    }
}
