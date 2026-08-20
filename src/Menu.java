import java.io.File;
import java.util.Scanner;

public class Menu {

    public void exibir() throws Exception {

        Scanner scanner = new Scanner(System.in);

        boolean programaAtivo = true;

        while (programaAtivo) {
            System.out.print("Digite o nome do arquivo (Enter para sair): ");
            String nomeArquivo = scanner.nextLine().trim();

            if (nomeArquivo.isEmpty()) {
                System.out.println("Programa encerrado.");
                programaAtivo = false;
            } else {
                File arquivo = new File("arquivo/" + nomeArquivo);

                if (!arquivo.exists()) {
                    System.out.println("Arquivo não encontrado.");
                } else {
                    System.out.println("Arquivo encontrado!");

                    System.out.println("==============================");
                    System.out.println("      REPRESENTAÇÃO DE GRAFOS");
                    System.out.println("==============================");
                    System.out.println("1 - Matriz de Adjacência (MA)");
                    System.out.println("2 - Matriz de Incidência (MI)");
                    System.out.println("3 - Lista de Adjacência (LA)");
                    System.out.println("0 - Sair");
                    System.out.println("==============================");

                    System.out.print("Escolha uma opção: ");
                    String entrada = scanner.nextLine().trim();
                    int opcao;

                    try {
                        opcao = Integer.parseInt(entrada);
                    } catch (NumberFormatException e) {
                        opcao = -1;
                    }

                    switch (opcao) {

                        case 1:
                            new MatrizA(arquivo);
                            break;

                        case 2:
                            new MatrizI(arquivo);
                            break;

                        case 3:
                            new ListaA(arquivo);
                            break;

                        case 0:
                            System.out.println("Programa encerrado.");
                            programaAtivo = false;
                            break;

                        default:
                            System.out.println("Opção inválida.");
                            break;
                    }
                }
            }
        }
    }
}
