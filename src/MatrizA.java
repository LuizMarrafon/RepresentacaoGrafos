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
        grafoCompleto(orientado);
    }

    public void lerArquivo(File arq) throws Exception {
        Scanner leitor = new Scanner(arq);
        String primeiraLinha = leitor.nextLine();
        rotulo = primeiraLinha.trim().split("[/\\s]+");
        int tamanho = rotulo.length;
        matriz = new int[tamanho][tamanho];
        for (int i = 0; i < tamanho; i++) {
            String linha = leitor.nextLine();
            // aceita um ou vários espaços entre os números
            String[] valores = linha.trim().split("\\s+");
            for (int j = 0; j < tamanho; j++) {
                matriz[i][j] = Integer.parseInt(valores[j]);
            }
        }
        leitor.close();
    }

    public void exibirMatriz() {
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
            int grauReferencia = 0;
            for (int j = 0; j < matriz.length; j++) {
                if (matriz[0][j] != 0)
                    grauReferencia++;
            }
            System.out.println(rotulo[0] + " - Grau: " + grauReferencia);
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
            int emissaoReferencia = 0;
            int recepcaoReferencia = 0;
            for (int j = 0; j < matriz.length; j++) {
                if (matriz[0][j] != 0)
                    emissaoReferencia++;
                if (matriz[j][0] != 0)
                    recepcaoReferencia++;
            }
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
                if (emissao != emissaoReferencia || recepcao != recepcaoReferencia)
                    flag = false;
            }
        }
        if (flag)
            System.out.println("Grafo é regular");
        else
            System.out.println("Grafo não é regular");
    }

    public void grafoCompleto(boolean orientado) {
        boolean completo = true;
        // K_n precisa ser simples
        if (!ehSimples())
            completo = false;
        // K_n é a classificação usada para o grafo não orientado
        if (orientado)
            completo = false;
        // todos os vértices precisam estar ligados a todos os outros
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