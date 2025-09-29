import java.io.*;

public class Driver {
    public static void main(String[] args) {
        try {

            double[] c1 = {6, 0, 0, 5};    // 6 + 5x^3
            int[] e1 = {0, 1, 2, 3};
            Polynomial p1 = new Polynomial(c1, e1);

            double[] c2 = {0, -2, 0, 0, -9}; // -2x - 9x^4
            int[] e2 = {0, 1, 2, 3, 4};
            Polynomial p2 = new Polynomial(c2, e2);

            System.out.println("p1: " + p1);
            System.out.println("p2: " + p2);


            Polynomial sum = p1.add(p2);
            System.out.println("p1 + p2 = " + sum);
            System.out.println("sum(0.1) = " + sum.evaluate(0.1));


            if (sum.hasRoot(1)) {
                System.out.println("1 is a root of sum");
            } else {
                System.out.println("1 is not a root of sum");
            }


            Polynomial product = p1.multiply(p2);
            System.out.println("p1 * p2 = " + product);
            System.out.println("product(0.1) = " + product.evaluate(0.1));


            String filename = "poly.txt";
            sum.saveToFile(filename);
            System.out.println("Sum polynomial saved to " + filename);


            Polynomial fromFile = new Polynomial(new File(filename));
            System.out.println("Polynomial read from file: " + fromFile);

        } catch (IOException e) {
            System.out.println("IO Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal Argument: " + e.getMessage());
        }
    }
}
