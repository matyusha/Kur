package sample;

public class UtilFunctions {

    public static double eps;

    public static double analyticValue(double x, double t, double k, double c, double R, boolean f) {
        return Math.exp((k/c) * (t / Math.pow(R, 2))) * (f ? Math.cos(x / R) : Math.sin(x / R));
    }

    public static double[] calculatePointsForAnalytic(int I, double hi, double t, double k, double c, double R, boolean f) {
        double[] u = new double[I];
        for(int i=0; i<I; i++) {
            double x = i * hi;
            u[i] = Math.exp((k/c) * t / Math.pow(R, 2)) * (f ? Math.cos(x / R) : Math.sin(x / R));
        }
        return u;
    }

    public static double[] calculatePointsMyakScheme(int I, int N, double hi, double ht, double c, double k, double R, int layer) {
        double[][] u = new double[N+1][I+1];
        for (int i = 0; i < I; i++) {
            u[0][i] = Math.cos(i * hi / R);
        }
        u[0][I]=u[0][0];
        double koaff = (k / c) * (ht / Math.pow(hi, 2));
        for (int j = 1; j <= N; j++) {
            for (int i = 1; i < I; i++) {
                u[j][i] = u[j - 1][i] + koaff*(u[j-1][i+1]-2*u[j-1][i]+u[j-1][i-1]);
            }
            u[j][0]=(u[j][1]+u[j][I-1])/2;
            u[j][I]=u[j][0];
        }
        double max=0;

        for(int i=0; i<I; i++){
            for(int j=0; j<N; j++) {
                double t = Math.abs(u[j][i]-analyticValue(i*hi, j*ht, k,c,R,true));
                if(max<t) max=t;
            }
        }
        System.out.println("Мякушко погрешность: " + max);
        return u[layer];
    }

    public static double[] calculatePointsMatScheme(int I, int N, double hi, double ht, double c, double k, double R, int layer) {
        double[][] u = new double[N+1][I+1];
        for (int i = 0; i < I; i++) {
            u[0][i] = Math.cos((i * hi) / R);
        }
        u[0][I]=u[0][0];
        double koaff = (k / c) * (ht / Math.pow(hi, 2));
        for (int j = 1; j < N+1; j++) {
            for (int i = 1; i < I; i++) {
                u[j][i] = u[j - 1][i] + koaff*(u[j-1][i+1]-2*u[j-1][i]+u[j-1][i-1]);
            }
            u[j][0]=u[j - 1][0] + koaff*(u[j-1][1]-2*u[j-1][0]+u[j-1][I-1]);
            u[j][I]=u[j][0];
        }

        double max=0;
        for(int i=0; i<I; i++){
            for(int j=0; j<N+1; j++) {
                double t = Math.abs(u[j][i]-analyticValue(i*hi, j*ht, k, c, R,true));
                if(max<t) max=t;
            }
        }

        System.out.println((koaff<0.5) +"  Матюшкина погрешность: " + max + " "+ eps/max);
        eps = max;
        return u[layer];
    }

    public static double[] calculatePointsVolScheme(int I, int N, double hi, double ht, double c, double k, double R, int layer) {
        double[][] u = new double[N+1][I+1];
        for (int i = 0; i < I; i++) {
            u[0][i] = Math.sin(i * hi / R);
        }
        u[0][I]=u[0][0];
        double koaff = (k*ht) / (c*hi*hi);
        for (int j = 1; j < N+1; j++) {
            for (int i = 1; i < I; i++) {
                u[j][i] = u[j - 1][i] + koaff*(u[j-1][i+1]-2*u[j-1][i]+u[j-1][i-1]);
            }
            u[j][0]=u[j - 1][0] + koaff*(u[j-1][1]-2*u[j-1][0]+u[j-1][I-1]);
            u[j][I]=u[j][0];
        }

        double max=0;

        for(int i=0; i<I+1; i++){
            for(int j=0; j<N+1; j++) {
                double t = Math.abs(u[j][i]-analyticValue(i*hi, j*ht, k, c, R,false));
                if(max<t) max=t;
            }
        }
        System.out.println("Волошин погрешность: " + max);

        return u[layer];
    }
}
