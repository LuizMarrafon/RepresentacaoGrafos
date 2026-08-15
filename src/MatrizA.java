import java.io.File;
import java.util.Scanner;

public class MatrizA {

    private int matriz[][];
    private String rotulo[];
    private File arquivo;

    public MatrizA(File arquivo) throws Exception {
        this.arquivo = arquivo;
        lerArquivo(this.arquivo);
        exibirMatriz();
        boolean orientado = verificaOrientacao();
        if (orientado) {
            exibirGraus();
        }
        grafoSimples();
        grafoRegular();
        grafoCompleto();
    }

    public void lerArquivo(File arq) throws Exception {
        Scanner leitor = new Scanner(arq);
        String primeiraLinha = leitor.nextLine();
        rotulo = primeiraLinha.split(" ");
        int tamanho = rotulo.length;
        matriz = new int[tamanho][tamanho];
        for (int i = 0; i < tamanho; i++) {
            String linha = leitor.nextLine();
            String[] valores = linha.split(" ");
            for (int j = 0; j < tamanho; j++) {
                matriz[i][j] = Integer.parseInt(valores[j]);
            }
        }
        leitor.close();
    }

    public void exibirMatriz() {
        for (int i = 0; i < rotulo.length; i++) {
            System.out.printf(rotulo[i]+" ");
        }
        System.out.println("\n");
        for (int i = 0; i < matriz.length; i++) {

            for (int j = 0; j < matriz[i].length; j++) {

                System.out.print(matriz[i][j] + " ");
            }

            System.out.println();
        }
    }

    public boolean verificaOrientacao(){
        Boolean flag = false;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                if(matriz[i][j] != matriz[j][i])
                    flag=true;
            }
        }
        if(flag)
            System.out.println("Grafo é orientado");
        else
            System.out.println("Grafo não orientado");
        return flag;
    }

    public void grafoSimples(){
        boolean flag = false;
        for (int i = 0; i < matriz.length; i++) {
            if (matriz[i][i] != 0)
                flag = true;
        }
        if(flag)
            System.out.println("Grafo apresenta laço, não é simples");
        else
            System.out.println("Grafo é simples");
    }

    public void grafoRegular(){
        boolean flag = true;
        int cont = 0, aux = 0;
        for (int i = 0; i < matriz.length; i++) {
            if(matriz[0][i] != 0)
                cont++;
        }
        for (int i = 0; i < matriz.length; i++) {
            aux = 0;
            for (int j = 0; j < matriz.length; j++) {
                if(matriz[i][j] != 0)
                    aux++;
            }
            if(aux != cont)
                flag = false;
        }
        if(flag)
            System.out.println("É regular");
        else
            System.out.println("Não regular");
    }

    public void grafoCompleto(){
        boolean flag = true;
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz.length; j++) {
                if(i != j && matriz[i][j] == 0)
                    flag = false;
            }
        }
        if(flag)
            System.out.println("Grafo completo de K"+rotulo.length);
        else
            System.out.println("Grafo incompleto");
    }

    public void exibirGraus() {
        for (int i = 0; i < matriz.length; i++) {
            int emissao = 0;
            int recepcao = 0;
            for (int j = 0; j < matriz.length; j++) {
                if (matriz[i][j] != 0)
                    emissao++;
                if (matriz[j][i] != 0)
                    recepcao++;
            }
            System.out.println(rotulo[i] +" - Emissão: " + emissao +" | Recepção: " + recepcao);
        }
    }

}