public class Salao {
    private static char[][] tab = null;
    private static int quantidade = 0;
    private static int[][] direcoes = {{0,1}, {0,-1}, {1,0}, {-1,0}, {1,1}, {-1,-1}, {1,-1}, {-1,1}};
    
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Insira 3 argumentos.");
            return;
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

        posiciona(N, b, c, 0);

        long fim = System.nanoTime();
        double tempoSegundos = (fim - inicio) / 1e9;
   
        System.out.printf("Total de solucoes %d %d %d: %d (%.2f s)", N, b, c, quantidade, tempoSegundos);
    }
    
    // Posiciona os forasteiros recursivamente e caso ainda não tenha aparecido, adiciona no set
    public static void posiciona(int N, int b, int c, int pos) {
        if (b == 0 && c == 0 && validaTabela(N)) {
            quantidade++;
            return;
        }
        
        // Verifica se a posição está fora do tabuleiro
        if (pos >= N * N) return;

        // EX: pos = 11 & N = 4
        // x = 2 -> passou pela linha 0 e 1 e agora está na linha 2
        // y = 3 -> avançou 10 casas, passando por 2 linhas (4 + 4) e agora está na coluna 3 da linha 2
        int x = pos / N;
        int y = pos % N;

        // Utilizamos essa lógica para evitar utilizar dois for's
        if (tab[x][y] == '.') {
            if (b > 0 && validaRedores(N, x, y, 'b')) {
                tab[x][y] = 'b';
                posiciona(N, b - 1, c, pos + 1);
                tab[x][y] = '.';
            }

            if (c > 0 && validaRedores(N, x, y, 'c')) {
                tab[x][y] = 'c';
                posiciona(N, b, c - 1, pos + 1);
                tab[x][y] = '.';
            }
        }

        posiciona(N, b, c, pos + 1);
    }

    // Verifica se não há um forasteiro da mesma gangue nos 8 espaços ao redor do atual
    public static boolean validaRedores(int N, int x, int y, char tipo) {
        for (int[] direcao : direcoes) {
            int nx = x + direcao[0], ny = y + direcao[1];
    
            if (nx >= 0 && nx < N && ny >= 0 && ny < N && tab[nx][ny] == tipo) 
                return false;
        }   

        return validaInimigos(N, x, y, tipo);
    }
    
    // Verifica se não há nenhum inimigo no campo de visão
    public static boolean validaInimigos(int N, int x, int y, char tipo) {
        char oponente = (tipo == 'b') ? 'c' : 'b';

        for (int[] direcao : direcoes) {
            int nx = x + direcao[0], ny = y + direcao[1];
            
            while (nx >= 0 && nx < N && ny >= 0 && ny < N) {
                if (tab[nx][ny] == tipo) return false;
                if (tab[nx][ny] == oponente) break;

                nx += direcao[0];
                ny += direcao[1];
            }
        }

        return true;
    }

    // Função que verifica se a tabela é válida
    public static boolean validaTabela(int N) {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tab[i][j] != '.' && !validaVisao(N, i, j, tab[i][j]))
                    return false;

        return true;
    }
    
    // Verifica se para cada forasteiro do tabuleiro, ele possuí pelo menos outros dois na visão e não enxerga nenhum aliado
    public static boolean validaVisao(int N, int x, int y, char tipo) {
        int totalInimigos = 0;
        char oponente = (tipo == 'b') ? 'c' : 'b';

        for (int[] direcao : direcoes) {
            int nx = x + direcao[0], ny = y + direcao[1];
            
            while (nx >= 0 && nx < N && ny >= 0 && ny < N) {
                if (tab[nx][ny] == tipo) return false;
                if (tab[nx][ny] == oponente) {
                    totalInimigos++;
                    break;
                }
                nx += direcao[0];
                ny += direcao[1];
            }
        }
    
        return totalInimigos >= 2;
    }

    // --- MÉTODOS AUXILIARES ---

    // Printa a matriz
    public static void printaMatriz() {
        System.out.println("-----");
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab[i].length; j++) {
                System.out.print(tab[i][j] + " ");
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