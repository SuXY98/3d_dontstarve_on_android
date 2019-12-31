package com.example.a3d_dontstarve_on_android.Nurbs;

public class NurbsHelper {
    void nrbssurf(float []b,int k,int l,int npts,int mpts,int p1,int p2,float [] q){
        int i,j,j1,jbas;
        int icount;
        int uinc,winc;
        int nplusc,mplusc;
        int []x = new int[30];
        int [] y= new int[30];
        int temp;

        float[] nbasis = new float[30];
        float [] mbasis = new float [30];
        float pbasis;
        float h;
        float sum;
        float u,w;
        float stepu,stepw;

        nplusc = npts + k;
        mplusc = mpts + l;

        for (i = 1; i <= nplusc; i++){
            x[i] = 0;
        }
        for (i = 1; i <= mplusc; i++){
            y[i] = 0;
        }
        for (i = 1; i <= npts; i++){
            nbasis[i] = 0.f;
        }
        for (i = 1; i <= mpts; i++){
            mbasis[i] = 0.f;
        }
        temp = 3*(p1*p2);

        for (i = 1; i <= 3*p1*p2; i++){
            q[i] = 0.f;
        }


        knot(npts,k,x);       /*  calculate u knot vector */
        knot(mpts,l,y);       /*  calculate w knot vector */

        icount = 1;

        stepu = (float)x[nplusc]/(float)(p1-1);
        stepw = (float)y[mplusc]/(float)(p2-1);

        u = 0.f;
        for (uinc = 1; uinc <= p1; uinc++){
            if ((float)x[nplusc] - u < 5e-6){
                u = (float)x[nplusc];
            }
            basis(k,u,npts,x,nbasis);    /* basis function for this value of u */
            w = 0.f;
            for (winc = 1; winc <= p2; winc++){
                if ((float)y[mplusc] - w < 5e-6){
                    w = (float)y[mplusc];
                }
                basis(l,w,mpts,y,mbasis);    /* basis function for this value of w */
                sum = sumrbas(b,nbasis,mbasis,npts,mpts);
                // System.out.print("in rbspsurf u,w,sum = "+" "+u+" "+w+" "+sum);
                for (i = 1; i <= npts; i++){
                    if (nbasis[i] != 0.){
                        jbas = 4*mpts*(i-1);
                        for (j = 1; j <= mpts; j++){
                            if (mbasis[j] != 0.){
                                j1 = jbas +4*(j-1) + 1;
                                pbasis = b[j1+3]*nbasis[i]*mbasis[j]/sum;
                                q[icount] = q[icount]+b[j1]*pbasis;  /* calculate surface point */
                                q[icount+1] = q[icount+1]+b[j1+1]*pbasis;
                                q[icount+2] = q[icount+2]+b[j1+2]*pbasis;
                            }
                        }
                    }
                }
                icount = icount + 3;
                w = w + stepw;
            }
            u = u + stepu;
        }
    }

    float sumrbas(float[]b, float[]nbasis,float[]mbasis,int npts,int mpts){
        int i,j,jbas,j1;
        float sum;

        /* calculate the sum */

        sum = 0;

        for (i = 1; i <= npts; i++){
            if (nbasis[i] != 0.){
                jbas = 4*mpts*(i-1);
                for (j = 1; j <= mpts; j++){
                    if (mbasis[j] != 0.){
                        j1 = jbas + 4*(j-1) + 4;
                        sum = sum + b[j1]*nbasis[i]*mbasis[j];
                    }
                }
            }
        }
        return(sum);
    }

    void basis(int c, float t,int npts, int[] x, float [] n){
        int nplusc;
        int i,k;
        float d,e;
        float []temp = new float[36];

        nplusc = npts + c;
        //first order of basis function
        for (i = 1; i<= nplusc-1; i++){
            if (( t >= x[i]) && (t < x[i+1]))
                temp[i] = 1;
            else
                temp[i] = 0;
        }
        //high order of function
        for (k = 2; k <= c; k++){
            for (i = 1; i <= nplusc-k; i++){
                if (temp[i] != 0)    /* if the lower order basis function is zero skip the calculation */
                    d = ((t-x[i])*temp[i])/(x[i+k-1]-x[i]);
                else
                    d = 0;

                if (temp[i+1] != 0)     /* if the lower order basis function is zero skip the calculation */
                    e = ((x[i+k]-t)*temp[i+1])/(x[i+k]-x[i+1]);
                else
                    e = 0;

                temp[i] = d + e;
            }
        }
        if (t == (float)x[nplusc]){		/*    pick up last point	*/
            temp[npts] = 1;
        }

        for (i = 1; i <= npts; i++) {
            n[i] = temp[i];
        }
    }

    void knot(int n,int c,int []x){
        //c order
        //n vertices
        int nplusc = n + c;
        int nplus2 = n + 2;
        x[1] = 0;
        for(int i = 2;i <= nplusc;i++){
            if((i > c) && (i < nplus2)){
                x[i] = x[i-1] + 1;
            }else{
                x[i] = x[i-1];
            }
        }
    }
}
