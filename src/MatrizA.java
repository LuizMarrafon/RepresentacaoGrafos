import java.io.File;
import java.util.Scanner;

public class MatrizA {

    private int[][] matriz;
    private String[] rotulo;
    private File arquivo;

    public MatrizA(File arquivo) throws Exception {
        this.arquivo = arquivo;
        lerArquivo(this.arquivo);
        exibirMatriz();
        boolean orientado = verificaOrientacao();
        if (orientado)
            System.out.println("Grafo é orientado");
        else
            System.out.println("Grafo não orientado");
        grafoSimples();
        grafoRegular(orientado);
        grafoCompleto();
    }

    public void lerArquivo(File arq) throws Exception {
        //a primeira linha tem os nomes dos vertices, entao separamos e guardamos no vetor
        Scanner leitor = new Scanner(arq);
        String primeiraLinha = leitor.nextLine();
        //separei o rotulo em uma String separada, seria os vertices
        rotulo = primeiraLinha.trim().split("[/\\s]+");
        int tamanho = rotulo.length;
        matriz = new int[tamanho][tamanho];
        for (int i = 0; i < tamanho; i++) {
            String linha = leitor.nextLine();
            //aceita um ou varios espacos entre os numeros
            String[] valores = linha.trim().split("\\s+");
            //agora percorre os valores da linha e coloca cada um na sua coluna
            for (int j = 0; j < tamanho; j++) {
                matriz[i][j] = Integer.parseInt(valores[j]);
            }
        }
        leitor.close();
    }

    public void exibirMatriz() {
        //exibe o rotulo
        System.out.printf("%6s", "");
        for (int i = 0; i < rotulo.length; i++) {
            System.out.printf("%6s", rotulo[i]);
        }
        System.out.println();
        for (int i = 0; i < matriz.length; i++) {
            System.out.printf("%6s", rotulo[i]);
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("%6d", matriz[i][j]);
            }
            System.out.println();
        }
    }

    public boolean verificaOrientacao() {
        boolean flag = false;
        //compara a matriz com o seu "espelho". Se algum valor for diferente, tem direcao
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                if (matriz[i][j] != matriz[j][i])
                    flag = true;
            }
        }
        return flag;
    }

    public boolean ehSimples() {
        boolean simples = true;
        //verifica se tem laço, ma nao tem multigrafo então nao verifico
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][i] != 0)
                simples = false;
        }
        return simples;
    }

    public void grafoSimples() {
        if (ehSimples())
            System.out.println("Grafo é simples");
        else
            System.out.println("Grafo apresenta laço, não é simples");
    }

    public void grafoRegular(boolean orientado) {
        boolean flag = true;
        if (!orientado) {
            //usa o primeiro vertice como referencia para comparar o grau dos demais
            int grauReferencia = 0;
            for (int j = 0; j < matriz.length; j++) {
                if (matriz[0][j] != 0)
                    grauReferencia++;
            }
            System.out.println(rotulo[0] + " - Grau: " + grauReferencia);
            //conta o grau de cada vertice e ve se todos sao iguais ao primeiro
            for (int i = 1; i < matriz.length; i++) {
                int grauAtual = 0;
                for (int j = 0; j < matriz.length; j++) {
                    if (matriz[i][j] != 0)
                        grauAtual++;
                }
                System.out.println(rotulo[i] + " - Grau: " + grauAtual);
                if (grauAtual != grauReferencia)
                    flag = false;
            }
        } else {
            //compara os graus de emissão e recepção, por conta de ser orientado
            boolean regularEmissao = true;
            boolean regularRecepcao = true;
            int emissaoReferencia = 0;
            int recepcaoReferencia = 0;
            for (int j = 0; j < matriz.length; j++) {
                if (matriz[0][j] != 0)
                    emissaoReferencia++;
                if (matriz[j][0] != 0)
                    recepcaoReferencia++;
            }
            //passa por cada vertice contando quantas arestas saem e quantas chegam nele
            for (int i = 0; i < matriz.length; i++) {
                int emissao = 0;
                int recepcao = 0;
                for (int j = 0; j < matriz.length; j++) {
                    if (matriz[i][j] != 0)
                        emissao++;
                    if (matriz[j][i] != 0)
                        recepcao++;
                }
                System.out.println(rotulo[i] + " - Emissão: " + emissao + " | Recepção: " + recepcao);
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
        //k_n precisa ser simples
        if (!ehSimples())
            completo = false;
        //todos os vertices precisam estar ligados a todos os outros
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                if (i != j && matriz[i][j] == 0)
                    completo = false;
            }
        }
        if (completo)
            System.out.println("Grafo completo K" + rotulo.length);
        else
            System.out.println("Grafo incompleto");
    }
}
