import java.util.Scanner;

public class Master {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("=================================================");
        System.out.println("     SISTEMA GENÉRICO DE ANÁLISE DE RECORRÊNCIA");
        System.out.println("=================================================");

        System.out.print("Digite a equação T(n): ");
        String equacao = scanner.nextLine();

        if (!validarEntrada(equacao)) {
            System.out.println(
                "[ERRO] Equação inválida. Padrão esperado: T(n) = aT(n op b) + g(n)"
            );

            scanner.close();
            return;
        }

        System.out.print("Informe T(1): ");
        long casoBase = scanner.nextLong();

        System.out.print("Quantas vezes deseja iterar os passos 1 e 2? ");
        int iteracoes = scanner.nextInt();

        if (iteracoes <= 0) {
            System.out.println("[ERRO] O número de iterações deve ser maior que zero.");
            scanner.close();
            return;
        }

        processar(
                equacao,
                casoBase,
                iteracoes
        );

        scanner.close();
    }

    public static boolean validarEntrada(String eq) {

        if (eq == null || eq.trim().isEmpty()) {
            return false;
        }

        String limpa = eq
                .replace(" ", "")
                .replace("−", "-");

        if (!limpa.startsWith("T(n)=")) {
            return false;
        }

        if (!limpa.contains("T(")) {
            return false;
        }

        if (!limpa.contains("+")) {
            return false;
        }

        if (!limpa.contains("/") && !limpa.contains("-")) {
            return false;
        }

        return true;
    }

    public static void processar(
            String equacao,
            long casoBase,
            int iteracoes) {

        String s = equacao
                .replace(" ", "")
                .replace("−", "-");

        boolean ehDivisao = s.contains("/");

        int a = extrairA(s);

        int b = extrairB(
                s,
                ehDivisao
        );

        String trabalho = extrairTrabalho(s);

        imprimirEtapas(
                a,
                b,
                trabalho,
                ehDivisao,
                iteracoes
        );

        System.out.println();
        System.out.println("=================================================");
        System.out.println("CASO BASE");
        System.out.println("=================================================");
        System.out.println("T(1) = " + casoBase);
    }

    public static int extrairA(String s) {

        int inicio = s.indexOf("T(n)=") + 5;

        int inicioT = s.indexOf(
                "T(",
                inicio
        );

        if (inicioT == inicio) {
            return 1;
        }

        String numero = s.substring(
                inicio,
                inicioT
        );

        if (numero.isEmpty()) {
            return 1;
        }

        return Integer.parseInt(numero);
    }

    public static int extrairB(
            String s,
            boolean ehDivisao) {

        int inicioT = s.indexOf("T(");

        if (ehDivisao) {

            int inicio = s.indexOf(
                    "/",
                    inicioT
            );

            int fim = s.indexOf(
                    ")",
                    inicio
            );

            String numero = s.substring(
                    inicio + 1,
                    fim
            );

            return Integer.parseInt(numero);

        } else {

            int inicio = s.indexOf(
                    "-",
                    inicioT
            );

            int fim = s.indexOf(
                    ")",
                    inicio
            );

            String numero = s.substring(
                    inicio + 1,
                    fim
            );

            return Integer.parseInt(numero);
        }
    }

    public static String extrairTrabalho(String s) {

        int posMais = s.indexOf("+");

        if (posMais == -1) {
            return "c";
        }

        return s.substring(
                posMais + 1
        );
    }

    public static void imprimirEtapas(
        int a,
        int b,
        String t,
        boolean ehDivisao,
        int iteracoes) {

    String coefA = (a == 1)
            ? ""
            : a + " * ";

    System.out.println();
    System.out.println("=================================================");
    System.out.println("1 ETAPA - ENTENDER A FUNÇÃO");
    System.out.println("=================================================");

    System.out.println("Função original:");

    if (ehDivisao) {
        System.out.println(
                "T(n) = "
                + coefA
                + "T(n/" + b + ") + "
                + t
        );
    } else {
        System.out.println(
                "T(n) = "
                + coefA
                + "T(n - " + b + ") + "
                + t
        );
    }

    System.out.println();
    System.out.println("Iterações:");

    for (int i = 1; i <= iteracoes; i++) {

        if (ehDivisao) {

            long atual = potencia(b, i);
            long proximo = potencia(b, i + 1);

            System.out.println(
                    "T(n/" + atual + ") = "
                    + coefA
                    + "T(n/" + proximo + ") + "
                    + substituirN(
                            t,
                            "(n/" + atual + ")"
                    )
            );

        } else {

            long atual = (long) i * b;
            long proximo = (long) (i + 1) * b;

            System.out.println(
                    "T(n - " + atual + ") = "
                    + coefA
                    + "T(n - " + proximo + ") + "
                    + substituirN(
                            t,
                            "(n - " + atual + ")"
                    )
            );
        }
    }

    System.out.println();
    System.out.println("=================================================");
    System.out.println("2 ETAPA - SUBSTITUIR");
    System.out.println("=================================================");

    System.out.println("Função original:");

    if (ehDivisao) {
        System.out.println(
                "T(n) = "
                + coefA
                + "T(n/" + b + ") + "
                + t
        );
    } else {
        System.out.println(
                "T(n) = "
                + coefA
                + "T(n - " + b + ") + "
                + t
        );
    }

    System.out.println();
    System.out.println("Substituições:");

    for (int i = 1; i <= iteracoes; i++) {

        long coef = potencia(a, i);

        String strA = (coef == 1)
                ? ""
                : coef + " * ";

        if (ehDivisao) {

            long divN = potencia(b, i);

            System.out.println(
                    "T(n) = "
                    + strA
                    + "T(n/" + divN + ") + "
                    + formatarAcumulado(
                            a,
                            b,
                            i,
                            t,
                            true
                    )
            );

        } else {

            long subN = (long) i * b;

            System.out.println(
                    "T(n) = "
                    + strA
                    + "T(n - " + subN + ") + "
                    + formatarAcumulado(
                            a,
                            b,
                            i,
                            t,
                            false
                    )
            );
        }
    }

    System.out.println();
    System.out.println("=================================================");
    System.out.println("3 ETAPA - IDENTIFICAR PADRÃO");
    System.out.println("=================================================");

    System.out.println(
            "T(n) = "
            + construirPadraoK(
                    a,
                    b,
                    t,
                    ehDivisao
            )
    );
}

    private static String substituirN(
            String texto,
            String substituicao) {

        return texto.replace(
                "n",
                substituicao
        );
    }

    private static String formatarAcumulado(
            int a,
            int b,
            int step,
            String t,
            boolean ehDivisao) {

        if (a == 1) {

            if (step == 1) {
                return t;
            }

            return step + " * " + t;
        }

        if (ehDivisao && a == 4 && b == 2) {

            long mult = potencia(
                    2,
                    step
            ) - 1;

            return mult + " * " + t;
        }

        return "acumulado("
                + step
                + ") * "
                + t;
    }

    private static String construirPadraoK(
            int a,
            int b,
            String t,
            boolean ehDivisao) {

        if (ehDivisao) {

            if (a == 1) {

                return "T(n/"
                        + b
                        + "^k) + k * "
                        + t;
            }

            if (a == 4 && b == 2) {

                return "4^k * T(n/2^k) + "
                        + "(2^k - 1) * "
                        + t;
            }

            if (a == b) {

                return a
                        + "^k * T(n/"
                        + b
                        + "^k) + k * "
                        + t;
            }

            return a
                    + "^k * T(n/"
                    + b
                    + "^k) + termo_acumulado(k)";

        } else {

            if (a == 1) {

                return "T(n - k*"
                        + b
                        + ") + k * "
                        + t;
            }

            return a
                    + "^k * T(n - k*"
                    + b
                    + ") + termo_acumulado(k)";
        }
    }

    private static long potencia(
            long base,
            int expoente) {

        long resultado = 1;

        for (int i = 0; i < expoente; i++) {
            resultado *= base;
        }

        return resultado;
    }
}