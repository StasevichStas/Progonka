import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final double k0 = 0, kN = 0, nu0 = 1;
    public static double h, tau;

    private static double A() {
        return -tau / (4 * h);
    }


    private static double B() {
        return tau / (4 * h);
    }

    private static double C() {
        return 1;
    }

    private static double D(double[][] u, int j, int n) {
        return u[n][j] - (tau / (4 * h)) * u[n][j + 1] + (tau / (4 * h)) * u[n][j - 1];
    }

    private static double F() {
        return 0;
    }

    private static double getU0() {
        return 0;
    }

    private static double getMu0() {
        return 1;
    }

    private static double getMuN(int t) {
        return (t * tau) == 1 ? 1 : 0;
    }

//    private static double solution(double x, double t) {
//        return (1+Math.tanh((t-x)/0.1))*0.5;
//    }
    private static double realSolution(double x, double t) {return x <= t ? 1 : 0;
    }

    public static void main(String[] args) {
        double a = 0, b = 1;
        Scanner scanner = new Scanner(System.in);
        System.out.println("введите h и tau соответственно");
        h = scanner.nextDouble();
        tau = scanner.nextDouble();
        int N1 = 1 + (int) ((b - a) / h);
        int N2 = 1 + (int) ((b - a) / tau);
        double[][] u = solve(N1, N2);
        //printMatrix(u);
        error(u, N1, N2);
        double constT5 = 0.5,constT2 = 0.2,constT8 = 1.0;
        try (FileWriter writerProgonka2 = new FileWriter("progonka2.txt");
             FileWriter writerProgonka5 = new FileWriter("progonka5.txt");
             FileWriter writerProgonka8 = new FileWriter("progonka8.txt");
             FileWriter writerProgonkaTest = new FileWriter("progonkaTest.txt")) {
            for (int i = 0; i < N1; i++) {
                writerProgonka2.write(i * h + " " + u[(int) (constT2 / tau)][i] + "\n");
                writerProgonka5.write(i * h + " " + u[(int) (constT5 / tau)][i] + "\n");
                writerProgonka8.write(i * h + " " + u[(int) (constT8 / tau)][i] + "\n");
                writerProgonkaTest.write(i * h + " " + realSolution(i*h,0.5) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Ошибка записи в файл" + e.getMessage());
        }
    }

    private static double[][] solve(int N1, int N2) {
        double[][] u = new double[N2][N1];
        double[] k = new double[N1];
        double[] nu = new double[N1];
        Arrays.fill(u[0], getU0());
        u[0][0] = 1;
        k[0] = k0;
        k[N1 - 1] = kN;
        nu[0] = nu0;
        for (int n = 0; n < N2 - 1; n++) {
            nu[N1 - 1] = getMuN(n + 1);
            for (int j = 1; j < N1 - 1; j++) {
                if ((C() + A() * k[j - 1]) == 0) System.out.println("ERROR:деление на ноль на прогоночных коэф.");
                k[j] = -B() / (C() + A() * k[j - 1]);
                nu[j] = -(A() * nu[j - 1] - D(u, j, n)) / (C() + A() * k[j - 1]);
            }
            if ((1 - k[N1 - 1] * k[N1 - 2]) == 0) System.out.println("ERROR:деление на ноль для u[n + 1][N1 - 1]");
            u[n + 1][N1 - 1] = (nu[N1 - 1] + k[N1 - 1] * nu[N1 - 2]) / (1 - k[N1 - 1] * k[N1 - 2]);
            for (int j = N1 - 2; j >= 0; --j) {
                u[n + 1][j] = k[j] * u[n][j] + nu[j];
            }
        }
        return u;
    }

    public static void error(double[][] u, int N1, int N2) {
        double maxError = 0, error;
        for (int n = 0; n < N2; n++) {
            for (int j = 0; j < N1; j++) {
                error = Math.abs(realSolution(h * j, tau * n) - u[n][j]);
                if (maxError < error) maxError = error;
            }
        }
        System.out.println("maxError = " + maxError);
    }

    public static void printMatrix(double[][] matrix) {
        for (double[] doubles : matrix) {
            for (double v : doubles) {
                System.out.printf("%10.4f", v);
            }
            System.out.print(System.lineSeparator());
        }
    }

}