import java.util.Scanner;

public class Master {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================================");
        System.out.println("     SISTEMA GENÉRICO DE ANÁLISE DE RECORRÊNCIA");
        System.out.println("=================================================");
        System.out.println();
        System.out.println("A recorrência deve ter o formato:");
        System.out.println("   T(n) = a * T(n op b) + g(n)");
        System.out.println();

        System.out.print("Digite o valor de 'a' (quantidade de chamadas recursivas): ");
        int a = scanner.nextInt();

        System.out.println();
        System.out.println("Escolha a operação dentro de T( ):");
        System.out.println("  1 - Divisão   -> T(n/b)");
        System.out.println("  2 - Subtração -> T(n-b)");
        System.out.print("Opção: ");
        int operacao = scanner.nextInt();
        boolean ehDivisao = (operacao == 1);

        System.out.print("Digite o valor de 'b': ");
        int b = scanner.nextInt();

        System.out.println();
        System.out.println("Escolha o grau de g(n) (custo fora da recursão):");
        System.out.println("  0 - Constante  (g(n) = c)");
        System.out.println("  1 - Linear     (g(n) = c*n)");
        System.out.println("  2 - Quadrática (g(n) = c*n^2)");
        System.out.print("Opção: ");
        int d = scanner.nextInt();

        System.out.print("Digite o coeficiente 'c' de g(n): ");
        long c = scanner.nextLong();

        System.out.println();
        System.out.print("Informe o valor de 'n' no caso base (ex: 1, 2, 3...): ");
        long n0 = scanner.nextLong();

        System.out.print("Informe o valor de T(" + n0 + ") (o custo desse caso base): ");
        long casoBase = scanner.nextLong();

        System.out.println();
        System.out.print("Quantas vezes deseja iterar os passos 1 e 2? ");
        int iteracoes = scanner.nextInt();

        if (iteracoes <= 0) {
            System.out.println("[ERRO] O número de iterações deve ser maior que zero.");
            scanner.close();
            return;
        }

        processar(a, b, ehDivisao, d, c, n0, casoBase, iteracoes);

        scanner.close();
    }


    public static void processar(
            int a,
            int b,
            boolean ehDivisao,
            int d,
            long c,
            long n0,
            long casoBase,
            int iteracoes) {

        imprimirEtapas1e2e3(a, b, ehDivisao, d, c, iteracoes);

        System.out.println();
        System.out.println("=================================================");
        System.out.println("CASO BASE");
        System.out.println("=================================================");
        System.out.println("T(" + n0 + ") = " + casoBase);

        imprimirEtapa4(a, b, ehDivisao, d, c, n0);

        imprimirEtapa5(a, b, ehDivisao, d);
    }

   
    public static void imprimirEtapas1e2e3(
            int a,
            int b,
            boolean ehDivisao,
            int d,
            long c,
            int iteracoes) {

        String coefA = (a == 1) ? "" : a + " * ";

        System.out.println();
        System.out.println("=================================================");
        System.out.println("1 ETAPA - ENTENDER A FUNÇÃO");
        System.out.println("=================================================");
        System.out.println("Função original:");
        System.out.println("T(n) = " + coefA + "T(n" + (ehDivisao ? "/" : " - ") + b + ") + " + termoG(c, d, "n"));

        System.out.println();
        System.out.println("Iterações:");

        for (int i = 1; i <= iteracoes; i++) {
            if (ehDivisao) {
                long atual = potencia(b, i);
                long proximo = potencia(b, i + 1);
                System.out.println(
                        "T(n/" + atual + ") = " + coefA + "T(n/" + proximo + ") + "
                        + termoG(c, d, "n/" + atual)
                );
            } else {
                long atual = (long) i * b;
                long proximo = (long) (i + 1) * b;
                System.out.println(
                        "T(n - " + atual + ") = " + coefA + "T(n - " + proximo + ") + "
                        + termoG(c, d, "(n-" + atual + ")")
                );
            }
        }

        System.out.println();
        System.out.println("=================================================");
        System.out.println("2 ETAPA - SUBSTITUIR");
        System.out.println("=================================================");
        System.out.println("Função original:");
        System.out.println("T(n) = " + coefA + "T(n" + (ehDivisao ? "/" : " - ") + b + ") + " + termoG(c, d, "n"));

        System.out.println();
        System.out.println("Substituições:");

        for (int i = 1; i <= iteracoes; i++) {
            long coef = potencia(a, i);
            String strA = (coef == 1) ? "" : coef + " * ";

            if (ehDivisao) {
                long divN = potencia(b, i);
                System.out.println(
                        "T(n) = " + strA + "T(n/" + divN + ") + "
                        + formatarAcumulado(a, b, i, c, d, true)
                );
            } else {
                long subN = (long) i * b;
                System.out.println(
                        "T(n) = " + strA + "T(n - " + subN + ") + "
                        + formatarAcumulado(a, b, i, c, d, false)
                );
            }
        }

        System.out.println();
        System.out.println("=================================================");
        System.out.println("3 ETAPA - IDENTIFICAR PADRÃO (em função de k)");
        System.out.println("=================================================");
        System.out.println("T(n) = " + construirPadraoK(a, b, ehDivisao));
        System.out.println("onde a soma (acumulado) representa:  " + somatorioSimbolico(a, b, c, d, ehDivisao));
    }

    private static String formatarAcumulado(
            int a,
            int b,
            int passos,
            long c,
            int d,
            boolean ehDivisao) {

        String soma = "";

        for (int j = 0; j < passos; j++) {
            long coefJ = potencia(a, j);
            String prefixo = (coefJ == 1) ? "" : coefJ + "*";

            String argumento;
            if (ehDivisao) {
                long divisor = potencia(b, j);
                argumento = (divisor == 1) ? "n" : "n/" + divisor;
            } else {
                long subtrai = (long) j * b;
                argumento = (subtrai == 0) ? "n" : "(n-" + subtrai + ")";
            }

            String termo = prefixo + termoG(c, d, argumento);

            if (j > 0) {
                soma = soma + " + ";
            }
            soma = soma + termo;
        }

        return soma;
    }

    private static String termoG(long c, int d, String argumento) {
        if (d == 0) {
            return c + "";
        }
        String coefStr = (c == 1) ? "" : c + "*";
        if (d == 1) {
            return coefStr + argumento;
        }
        return coefStr + argumento + "^2";
    }

    private static String construirPadraoK(int a, int b, boolean ehDivisao) {
        if (ehDivisao) {
            return a + "^k * T(n/" + b + "^k) + [acumulado, ver abaixo]";
        }
        return a + "^k * T(n - k*" + b + ") + [acumulado, ver abaixo]";
    }

    private static String somatorioSimbolico(int a, int b, long c, int d, boolean ehDivisao) {
        String coefA = (a == 1) ? "" : a + "^j * ";
        String arg = ehDivisao ? "n/" + b + "^j" : "(n - j*" + b + ")";
        return "somatorio de j=0 ate k-1 de [ " + coefA + termoG(c, d, arg) + " ]";
    }

  
    private static void imprimirEtapa4(
            int a,
            int b,
            boolean ehDivisao,
            int d,
            long c,
            long n0) {

        System.out.println();
        System.out.println("=================================================");
        System.out.println("4 ETAPA - CONDIÇÃO DE PARADA (achar k)");
        System.out.println("=================================================");

        if (ehDivisao) {
            System.out.println("A recursão para quando n/b^k = n0, ou seja:");
            System.out.println("k = log_" + b + "(n / " + n0 + ")");
            System.out.println();
            System.out.println("Substituindo k na fórmula da Etapa 3:");
            System.out.println(
                    "T(n) = " + a + "^(log_" + b + "(n/" + n0 + ")) * T(" + n0 + ") + "
                    + somatorioSimbolico(a, b, c, d, true)
                    + "   [com k = log_" + b + "(n/" + n0 + ")]"
            );
        } else {
            System.out.println("A recursão para quando n - k*b = n0, ou seja:");
            System.out.println("k = (n - " + n0 + ") / " + b);
            System.out.println();
            System.out.println("Substituindo k na fórmula da Etapa 3:");
            System.out.println(
                    "T(n) = " + a + "^((n-" + n0 + ")/" + b + ") * T(" + n0 + ") + "
                    + somatorioSimbolico(a, b, c, d, false)
                    + "   [com k = (n-" + n0 + ")/" + b + "]"
            );
        }
    }


    private static void imprimirEtapa5(
            int a,
            int b,
            boolean ehDivisao,
            int d) {

        System.out.println();
        System.out.println("=================================================");
        System.out.println("5 ETAPA - CLASSIFICAR A COMPLEXIDADE");
        System.out.println("=================================================");
        System.out.println("Grau de g(n): d = " + d + "  (g(n) = Theta(n^" + d + "))");
        System.out.println();

        if (ehDivisao) {
            long bd = potencia(b, d);
            System.out.println("Comparando a=" + a + " com b^d=" + b + "^" + d + "=" + bd + ":");

            if (a < bd) {
                System.out.println("Como a < b^d  =>  T(n) = Theta(n^" + d + ")");
            } else if (a == bd) {
                if (d == 0) {
                    System.out.println("Como a == b^d  =>  T(n) = Theta(log n)");
                } else {
                    System.out.println("Como a == b^d  =>  T(n) = Theta(n^" + d + " * log n)");
                }
            } else {
                int logExato = calcularLogInteiro(a, b);
                if (logExato != -1) {
                    System.out.println(
                            "Como a > b^d  =>  T(n) = Theta(n^log_" + b + "(" + a + "))"
                            + " = Theta(n^" + logExato + ")"
                    );
                } else {
                    System.out.println(
                            "Como a > b^d  =>  T(n) = Theta(n^log_" + b + "(" + a + "))"
                            + "  (log_" + b + "(" + a + ") não é inteiro exato)"
                    );
                }
            }
        } else {
            if (a == 1) {
                System.out.println("Como a=1 (uma chamada só), k cresce linear com n: k = Theta(n).");
                System.out.println("Somando k termos de grau " + d + "  =>  T(n) = Theta(n^" + (d + 1) + ")");
            } else {
                System.out.println(
                        "Como a=" + a + " > 1 (chamadas se multiplicam a cada passo)  =>  "
                        + "T(n) = Theta(" + a + "^(n/" + b + "))  -> crescimento EXPONENCIAL"
                );
            }
        }
    }

    private static int calcularLogInteiro(int a, int b) {
        if (b <= 1) {
            return -1;
        }
        long valor = 1;
        int expoente = 0;
        while (valor < a) {
            valor *= b;
            expoente++;
        }
        return (valor == a) ? expoente : -1;
    }

    private static long potencia(long base, int expoente) {
        long resultado = 1;
        for (int i = 0; i < expoente; i++) {
            resultado *= base;
        }
        return resultado;
    }
}