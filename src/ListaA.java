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
    }

    public void leArquivo(File arq) throws Exception {
        Scanner leitor = new Scanner(arq);
        while (leitor.hasNextLine()) {

            String linha = leitor.nextLine();
            String[] valores = linha.split(" ");
            Vertice v = new Vertice(valores[0]);
            for (int i = 1; i < valores.length; i++) {
                String[] dados = valores[i].split(",");
                String destino = dados[0];
                Aresta nova;
                if (dados.length == 2) {
                    int peso = Integer.parseInt(dados[1]);
                    nova = new Aresta(destino, peso);
                }
                else {
                    nova = new Aresta(destino);
                }
                v.adicionarAresta(nova);
            }
            vertices.add(v);
        }
        leitor.close();
    }

    public boolean existeAresta(String destino, String origem){
        boolean flag = false;
        Vertice v = vertices.get(0);
        int i = 0;
        while(i < vertices.size() && !v.getRotulo().equalsIgnoreCase(destino)){
            v = vertices.get(i);
            i++;
        }
        Aresta aresta = v.getInicio();
        while(aresta != null && !aresta.getDestino().equalsIgnoreCase(origem))
            aresta = aresta.getProx();
        if(aresta != null)
            flag = true;

        return flag;
    }

    public void verificaOrientacao() {
        boolean orientado = false;
        for (int i = 0; i < vertices.size(); i++) {
            Vertice v = vertices.get(i);
            Aresta aux = v.getInicio();
            while (aux != null) {
                String origem = v.getRotulo();
                String destino = aux.getDestino();
                if (!existeAresta(destino, origem)) {
                    orientado = true;
                }
                aux = aux.getProx();
            }
        }
        if (orientado)
            System.out.println("Grafo é orientado");
        else
            System.out.println("Grafo não orientado");
    }

    public boolean grafoSimples(){
        boolean flagS = false;
        Vertice v;
        //verifica se tem laço
        for (int i = 0; i < vertices.size(); i++) {
            v = vertices.get(i);
            Aresta a = v.getInicio();
            while(a != null && !flagS){
                if(a.getDestino().equalsIgnoreCase(v.getRotulo()))
                    flagS = true;
                a = a.getProx();
            }
        }

        boolean flag = false;
        for (int i = 0; i < vertices.size(); i++) {
            v = vertices.get(i);
            Aresta a = v.getInicio();
            while(a != null && !flag){
                Aresta aux = a.getProx();
                while(aux != null && !flag){
                    if(a.getDestino().equalsIgnoreCase(aux.getDestino()))
                        flag = true;
                    aux = aux.getProx();
                }
                a = a.getProx();
            }
        }

        if (!flagS && !flag){
            System.out.println("Grafo é simples");
            return true;
        }
        else{
            System.out.println("Grafo não é simples");
            return false;
        }
    }

    public void grafoRegular() {
        boolean flag = true;
        int cont = 0, aux;
        Aresta a = vertices.get(0).getInicio();
        while (a != null) {
            cont++;
            a = a.getProx();
        }
        for (int i = 0; i < vertices.size(); i++) {
            aux = 0;
            a = vertices.get(i).getInicio();
            while (a != null) {
                aux++;
                a = a.getProx();
            }
            if (aux != cont)
                flag = false;
        }
        if (flag)
            System.out.println("É regular");
        else
            System.out.println("Não regular");
    }

    public void grafoCompleto() {
        boolean flag = true;
        if (!grafoSimples()) {
            flag = false;
        }
        for (int i = 0; i < vertices.size() && flag; i++) {
            int cont = 0;
            Aresta a = vertices.get(i).getInicio();
            while (a != null) {
                cont++;
                a = a.getProx();
            }
            if (cont != vertices.size() - 1)
                flag = false;
        }
        if (flag)
            System.out.println("Grafo completo K" + vertices.size());
        else
            System.out.println("Grafo incompleto");
    }

    public void exibirGraus() {
        for (int i = 0; i < vertices.size(); i++) {
            int emissao = 0;
            int recepcao = 0;
            Vertice v = vertices.get(i);
            Aresta a = v.getInicio();
            while (a != null) {
                emissao++;
                a = a.getProx();
            }
            for (int j = 0; j < vertices.size(); j++) {
                Aresta aux = vertices.get(j).getInicio();
                while (aux != null) {
                    if (aux.getDestino().equalsIgnoreCase(v.getRotulo()))
                        recepcao++;
                    aux = aux.getProx();
                }
            }
            System.out.println(v.getRotulo() +" - Emissão: " + emissao + " | Recepção: " + recepcao);
        }
    }

}
