import java.io.*;
import java.util.*;

public class Polynomial {
    private double[] coef;
    private int[] expo;     


    public Polynomial() {
        coef = new double[]{0};
        expo = new int[]{0};
    }


    public Polynomial(double[] a, int[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("zero coefficient");
        }

        List<Double> aList = new ArrayList<>();
        List<Integer> bList = new ArrayList<>();
        for (int i = 0; i < a.length; i++){
            if (a[i] != 0) {
                aList.add(a[i]);
                bList.add(b[i]);
            }
        }

        coef = new double[aList.size()];
        expo = new int[bList.size()];
        for (int i = 0; i < aList.size(); i++){
            coef[i] = aList.get(i);
            expo[i] = bList.get(i);
        }
    }


    public Polynomial(File file) throws IOException {
        BufferedReader f = new BufferedReader(new FileReader(file));
        String line = f.readLine();
        f.close();
        getPolynomial(line);
    }


    private void getPolynomial(String p){
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
            if (term.contains("x")){
                String[] parts = term.split("x");
                if (parts[0].equals("") || parts[0].equals("+")){
                    c = 1;
                } else if (parts[0].equals("-")) {
                    c = -1;
                } else {
                    c = Double.parseDouble(parts[0]);
                }
                if (parts.length == 1 || parts[1].isEmpty()){
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
        expo = new int[le.size()];
        for (int i = 0; i < lc.size(); i++){
            coef[i] = lc.get(i);
            expo[i] = le.get(i);
        }
    }


    public Polynomial add(Polynomial a){
        int maxexpo = 0;
        for(int e : this.expo){
            if(e > maxexpo) maxexpo = e;
        }
        for(int e : a.expo){
            if(e > maxexpo) maxexpo = e;
        }

        double[] newcoef = new double[maxexpo + 1];
        int[] newexpo = new int[maxexpo + 1];
        for(int i = 0; i <= maxexpo; i++){
            newexpo[i] = i;
            newcoef[i] = 0;
        }

        for (int i = 0; i < coef.length; i++){
            newcoef[expo[i]] += coef[i];
        }

        for (int i = 0; i < a.coef.length; i++) {
            newcoef[a.expo[i]] += a.coef[i];
        }
    
        return new Polynomial(newcoef, newexpo);
    }


    public Polynomial multiply(Polynomial a){		
        int maxexpo = 0;
        for (int e1 : this.expo){
            for (int e2 : a.expo){
                if (e1 + e2 > maxexpo){
                    maxexpo = e1+e2;
                }
            }
        }
    
        double[] tempcoef = new double[maxexpo + 1];
        int[] tempexpo = new int[maxexpo + 1];
    
        for (int i = 0; i <= maxexpo; i++){
            tempexpo[i] = i;
            tempcoef[i] = 0;
        }
    
        for (int i = 0; i < coef.length; i++){
            for (int j = 0; j < a.coef.length; j++) {
                int expSum = expo[i] + a.expo[j];
                tempcoef[expSum] += coef[i] * a.coef[j];
            }
        }

        int zero = 0;
        for(double c : tempcoef){
            if(c != 0) zero++;
        }
    
        double[] newCoef = new double[zero];
        int[] newExpo = new int[zero];
        int index = 0;
        for(int i = 0; i < tempcoef.length; i++){
            if(tempcoef[i] != 0){
                newCoef[index] = tempcoef[i];
                newExpo[index] = tempexpo[i];
                index++;
            }
        }
    
        return new Polynomial(newCoef, newExpo);
    }


    public double evaluate(double x) {
        double result = 0;
        for (int i = 0; i < coef.length; i++) {
            result += coef[i] * Math.pow(x, expo[i]);
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




    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < coef.length; i++) {
            double c = coef[i];
            int e = expo[i];
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
