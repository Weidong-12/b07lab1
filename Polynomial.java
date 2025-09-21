public class Polynomial{
	private double[] coef;
	
	public Polynomial(){
		coef = new double[]{0};
	}
	
	public Polynomial(double[] array){
		coef = new double[array.length];	
		for(int i = 0; i<array.length; i++ ){
			coef[i] = array[i];
		}
	}

	public Polynomial add(Polynomial a){
		int len;
		double b , c;
		if(coef.length > a.coef.length){
			len = coef.length;
		}else{
			len = a.coef.length;
		}
		double[] result = new double[len];
		
		for (int i=0; i<len; i++){
            if (i < coef.length) {
                b = coef[i];
            } else {
            b = 0;
            }
			if (i < a.coef.length) {
                c = a.coef[i];
            } else {
            c = 0;
            }
			result[i] = b+c;
		}
		return new Polynomial(result);
	}
	
	public double evaluate(double x){
		int len = coef.length;
		double result = 0;
		for(int i = 0; i < len; i++){
			result = result + coef[i] * Math.pow(x , i);
		}
		return result; 
	}
	
	public boolean hasRoot(double x){
		return evaluate(x)==0;
	}
}