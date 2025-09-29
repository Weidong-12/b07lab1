import java.io.*;
import java.util.*;

public class Polynomial {
    private double[] coef;
    private int[] exp;     


    public Polynomial() {
        coef = new double[]{0};
        exp = new int[]{0};
    }


    public Polynomial(double[] a, int[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Coefficient and exponent arrays must have the same length");
        }

        List<Double> aList = new ArrayList<>();
        List<Integer> bList = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            if (a[i] != 0) {
                aList.add(a[i]);
                bList.add(b[i]);
            }
        }

        coef = new double[aList.size()];
        exp = new int[bList.size()];
        for (int i = 0; i < aList.size(); i++) {
            coef[i] = aList.get(i);
            exp[i] = bList.get(i);
        }
    }


    public Polynomial(File file) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader(file));
        String line = f.readLine();
        f.close();
        getPolynomial(line);
    }


    private void getPolynomial(String p) {
        p = p.replace("-", "+-");
        if (p.startsWith("+-")) p = p.substring(1);
        else if (p.startsWith("+")) p = p.substring(1);

        String[] terms = p.split("\\+");
        List<Double> lc = new ArrayList<>();
        List<Integer> le = new ArrayList<>();

        for (String term : terms) {
            if (term.isEmpty()) continue;

            double c;
            int e;
            if (term.contains("x")) {
                String[] parts = term.split("x");
                if (parts[0].equals("") || parts[0].equals("+")) {
                    c = 1;
                } else if (parts[0].equals("-")) {
                    c = -1;
                } else {
                    c = Double.parseDouble(parts[0]);
                }
                if (parts.length == 1 || parts[1].isEmpty()) {
                    e = 1;
                } else {
                    e = Integer.parseInt(parts[1]);
                }
            } else {
                c = Double.parseDouble(term);
                e = 0;
            }
            lc.add(c);
            le.add(e);
        }

        coef = new double[lc.size()];
        exp = new int[le.size()];
        for (int i = 0; i < lc.size(); i++) {
            coef[i] = lc.get(i);
            exp[i] = le.get(i);
        }
    }


    public Polynomial add(Polynomial a) {
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < coef.length; i++) {
            map.put(exp[i], map.getOrDefault(exp[i], 0.0) + coef[i]);
        }
        for (int i = 0; i < a.coef.length; i++) {
            map.put(a.exp[i], map.getOrDefault(a.exp[i], 0.0) + a.coef[i]);
        }
        return fromMap(map);
    }


    public Polynomial multiply(Polynomial a) {
        Map<Integer, Double> map = new HashMap<>();
        for (int i = 0; i < coef.length; i++) {
            for (int j = 0; j < a.coef.length; j++) {
                int newExp = exp[i] + a.exp[j];
                double newCoef = coef[i] * a.coef[j];
                map.put(newExp, map.getOrDefault(newExp, 0.0) + newCoef);
            }
        }
        return fromMap(map);
    }


    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coef.length; i++) {
            result += coef[i] * Math.pow(x, exp[i]);
        }
        return result;
    }


    public boolean hasRoot(double x) {
        return Math.abs(evaluate(x)) < 1e-9;
    }


    public void saveToFile(String filename) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        bw.write(toString());
        bw.close();
    }


    private static Polynomial fromMap(Map<Integer, Double> map) {
        List<Integer> eList = new ArrayList<>(map.keySet());
        Collections.sort(eList);

        List<Double> cList = new ArrayList<>();
        List<Integer> exps = new ArrayList<>();
        for (int e : eList) {
            double c = map.get(e);
            if (c != 0) {
                cList.add(c);
                exps.add(e);
            }
        }

        double[] cArr = new double[cList.size()];
        int[] eArr = new int[exps.size()];
        for (int i = 0; i < cList.size(); i++) {
            cArr[i] = cList.get(i);
            eArr[i] = exps.get(i);
        }
        return new Polynomial(cArr, eArr);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coef.length; i++) {
            double c = coef[i];
            int e = exp[i];
            if (c == 0) continue;

            if (sb.length() > 0 && c > 0) sb.append("+");

            if (e == 0) sb.append(c);
            else if (e == 1) {
                if (c == 1) sb.append("x");
                else if (c == -1) sb.append("-x");
                else sb.append(c + "x");
            } else {
                if (c == 1) sb.append("x" + e);
                else if (c == -1) sb.append("-x" + e);
                else sb.append(c + "x" + e);
            }
        }
        return sb.toString();
    }
}
