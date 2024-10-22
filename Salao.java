import java.util.HashSet;

public class Salao {
    private static char[][] tab = null;
    private static HashSet<String> possibilidades = new HashSet<>();
    private static boolean primeira = true;
    
    public static void main(String[] args) {
        if (args.length <= 2) {
            System.out.println("Insira 3 argumentos.");
        }

        int N = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        int c = Integer.parseInt(args[2]);

        if(N <= 2 || b <= 1 || c <= 1) {
            System.out.println(0);
            return;
        }

        tab = criaTabela(N);

        long inicio = System.nanoTime();

        posiciona(N, b, c, 0, 0);

        long fim = System.nanoTime();

        double tempoSegundos = (fim - inicio) / 1e9;
   
        System.out.printf("Total de solucoes %d %d %d: %d (%.2f s)", N, b, c, possibilidades.size(), tempoSegundos);
    }
    
    // Posiciona os forasteiros recursivamente e caso ainda não tenha aparecido, adiciona no set
    public static void posiciona(int N, int b, int c, int x, int y) {
        if(b == 0 && c == 0 && validaTabela(N)) {
            String tabStr = matrizParaString(tab);
            possibilidades.add(tabStr);
            return;
        }

        if(b == 0 && primeira == true) {
            x = 0;
            y = 0;
            primeira = false;
        }

        for (int i = x; i < N; i++) {
            for (int j = (i == x ? y : 0); j < N; j++) {
                if (tab[i][j] == '.' && b > 0 && validaRedores(N, i, j, 'b')) {
                    primeira = true;
                    tab[i][j] = 'b';

                    posiciona(N, b-1, c, i, j+1);
         
                    tab[i][j] = '.';
                }

                if (tab[i][j] == '.' && b == 0 && c > 0 && validaVisao(N, i, j, 'c')) {
                    tab[i][j] = 'c';
        
                    posiciona(N, b, c-1, i, j+1);
         
                    tab[i][j] = '.';
                }
            }
        }
    }

    // Verifica se não há um forasteiro da mesma gangue nos 8 espaços ao redor do atual
    public static boolean validaRedores(int N, int x, int y, char tipo) {
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
    
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
    
            if (nx >= 0 && nx < N && ny >= 0 && ny < N)
                if (tab[nx][ny] == tipo) return false;
        }   
        
        return true;
    }

    // Função que verifica se a tabela é válida
    public static boolean validaTabela(int N) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tab[i][j] == 'b' && !validaVisao(N, i, j, (tab[i][j] == 'b') ? 'b' : 'c'))
                    return false;

        return true;
    }
    
    // Verifica se para cada forasteiro do tabuleiro, ele possuí pelo menos outros dois na visão e não enxerga nenhum aliado
    public static boolean validaVisao(int N, int x, int y, char tipo) {
        int totalInimigos = 0;
        char oponente = (tipo == 'b') ? 'c' : 'b';

        int[][] direcoes = {{0,1}, {0,-1}, {1,0}, {-1,0}, {1,1}, {-1,-1}, {1,-1}, {-1,1}};

        for (int[] direcao : direcoes) {
            int nx = x + direcao[0], ny = y + direcao[1];
            boolean encontrouInimigo = false;
            
            while (nx >= 0 && nx < N && ny >= 0 && ny < N) {
                if (tab[nx][ny] == tipo) return false;
                if (tab[nx][ny] == oponente) {
                    encontrouInimigo = true;
                    break;
                }
                nx += direcao[0];
                ny += direcao[1];
            }
            
            if (encontrouInimigo) totalInimigos++;
        }
    
        return totalInimigos >= 2;
    }

    // --- MÉTODOS AUXILIARES ---

    // Copia a matriz para armazenar os valores e não a referência
    public static String matrizParaString(char[][] tab) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                sb.append(tab[i][j]);
            }
            sb.append("\n");  // Adiciona quebra de linha para separar as linhas
        }
        return sb.toString();
    }

    // Printa a matriz
    public static void printaMatriz(char matrix[][]) {
        System.out.println("-----");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
    
    // Cria a inicializa a tabela com valores '.'
    public static char[][] criaTabela(int N) {
        tab = new char[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                tab[i][j] = '.';
        }
        
        return tab;
    }
}