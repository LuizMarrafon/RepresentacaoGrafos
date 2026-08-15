import java.io.File;
import java.util.Scanner;

public class Menu {

    public void exibir() throws Exception {

        Scanner scanner = new Scanner(System.in);

        int opcao;
        String tipoRepresentacao = "";

        System.out.println("==============================");
        System.out.println("      REPRESENTAÇÃO DE GRAFOS");
        System.out.println("==============================");
        System.out.println("1 - Matriz de Adjacência (MA)");
        System.out.println("2 - Matriz de Incidência (MI)");
        System.out.println("3 - Lista de Adjacência (LA)");
        System.out.println("0 - Sair");
        System.out.println("==============================");

        System.out.print("Escolha uma opção: ");
        opcao = scanner.nextInt();

        switch (opcao) {

            case 1:
                tipoRepresentacao = "MA";
                break;

            case 2:
                tipoRepresentacao = "MI";
                break;

            case 3:
                tipoRepresentacao = "LA";
                break;

            case 0:
                System.out.println("Programa encerrado.");
                return;

            default:
                System.out.println("Opção inválida.");
                return;
        }

        System.out.print("Digite o nome do arquivo: ");
        String nomeArquivo = scanner.next();

        File arquivo = new File("arquivo/"+nomeArquivo);

        if (arquivo.exists()) {
            System.out.println("Arquivo encontrado!");
            System.out.println("Representação escolhida: " + tipoRepresentacao);
            System.out.println("Arquivo: " + nomeArquivo);
            MatrizA matrizA = new MatrizA(arquivo);
        } else {
            System.out.println("Arquivo não encontrado.");
        }
    }
}