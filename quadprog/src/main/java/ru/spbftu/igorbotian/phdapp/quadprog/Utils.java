package ru.spbftu.igorbotian.phdapp.quadprog;

/**
 * Утилитарные функции из файла <tt>test.f</tt> из пакета quadprog
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see <a href="http://cran.r-project.org/web/packages/quadprog/">http://cran.r-project.org/web/packages/quadprog/</a>
 */
final class Utils {

    private Utils() {
        //
    }

    /**
     * Code gleaned from Powell's ZQPCVX routine to determine a small number that can be assumed to be
     * an upper bound on the relative precision of the computer arithmetic.
     */
    public static double vsmall() {
        double epsilon = 1.0e-60;
        double tmpa;
        double tmpb;

        do {
            epsilon = epsilon + epsilon;
            tmpa = 1 + 0.1 * epsilon;
            tmpb = 1 + 0.2 * epsilon;
        } while (tmpa <= 1 || tmpb <= 1);

        return epsilon;
    }

    /**
     * dpori computes the inverse of the factor of a
     * double precision symmetric positive definite matrix
     * using the factors computed by dpofa.
     * <p>
     * modification of dpodi by BaT 05/11/95
     * <p>
     * on entry
     * <p>
     * a       double precision(lda, n)
     * the output  a  from dpofa
     * <p>
     * lda     integer
     * the leading dimension of the array  a .
     * <p>
     * n       integer
     * the order of the matrix  a .
     * <p>
     * on return
     * <p>
     * a       if dpofa was used to factor  a  then
     * dpodi produces the upper half of inverse(a) .
     * elements of  a  below the diagonal are unchanged.
     * <p>
     * error condition
     * <p>
     * a division by zero will occur if the input factor contains
     * a zero on the diagonal and the inverse is requested.
     * it will not occur if the subroutines are called correctly
     * and if dpoco or dpofa has set info .eq. 0 .
     * <p>
     * linpack.  this version dated 08/14/78 .
     * cleve moler, university of new mexico, argonne national lab.
     * modified by Berwin A. Turlach 05/11/95
     */
    public static void dpori(Double[][] a, double lda, int n) {
        int i;
        int j;
        int k;
        int kp1;
        double t;

        // compute inverse(r)
        for (k = 1; k <= n; k = k + 1) {
            a[k][k] = 1.0 / a[k][k];
            t = -a[k][k];
            //dscal(k - 1, t, a[1], k - 1, 1);
            for (i = 1; i < k; i = i + 1) {
                a[i][k] = t * a[i][k];
            }

            kp1 = k + 1;

            if (n < kp1) {
                break;
            }

            for (j = kp1; j <= n; j = j + 1) {
                t = a[k][j];
                a[k][j] = 0.0;
                //daxpy(k, t, a[1], k - 1, 1, a[1], j - 1, 1);
                for (i = 1; i <= k; i = i + 1) {
                    a[i][j] = a[i][j] + (t * a[i][k]);
                }
            }
        }
    }

    /**
     * dposl solves the double precision symmetric positive definite
     * system a * x = b
     * using the factors computed by dpoco or dpofa.
     * <p>
     * on entry
     * <p>
     * a       double precision(lda, n)
     * the output from dpoco or dpofa.
     * <p>
     * lda     integer
     * the leading dimension of the array  a .
     * <p>
     * n       integer
     * the order of the matrix  a .
     * <p>
     * b       double precision(n)
     * the right hand side vector.
     * <p>
     * on return
     * <p>
     * b       the solution vector  x .
     * <p>
     * error condition
     * <p>
     * a division by zero will occur if the input factor contains
     * a zero on the diagonal.  technically this indicates
     * singularity but it is usually caused by improper subroutine
     * arguments.  it will not occur if the subroutines are called
     * correctly and  info .eq. 0 .
     * <p>
     * to compute  inverse(a) * c  where  c  is a matrix
     * with  p  columns
     * call dpoco(a,lda,n,rcond,z,info)
     * if (rcond is too small .or. info .ne. 0) go to ...
     * do 10 j = 1, p
     * call dposl(a,lda,n,c(1,j))
     * 10 continue
     * <p>
     * linpack.  this version dated 08/14/78 .
     * cleve moler, university of new mexico, argonne national lab.
     */
    public static void dposl(Double[][] a, double lda, int n, Double[] b) {
        int i;
        int k;
        int kb;
        double t;

        // solve trans(r)*y = b
        for (k = 1; k <= n; k = k + 1) {
            //t = ddot(k - 1, a[1], k - 1, 1, b, 0, 1);
            t = 0;
            for (i = 1; i < k; i = i + 1) {
                t = t + (a[i][k] * b[i]);
            }

            b[k] = (b[k] - t) / a[k][k];
        }

        // solve r*x = y
        for (kb = 1; kb <= n; kb = kb + 1) {
            k = n + 1 - kb;
            b[k] = b[k] / a[k][k];
            t = -b[k];
            //daxpy(k - 1, t, a[1], k - 1, 1, b, 0, 1);
            for (i = 1; i < k; i = i + 1) {
                b[i] = b[i] + (t * a[i][k]);
            }
        }
    }

    /**
     * dpofa factors a double precision symmetric positive definite matrix.
     * dpofa is usually called by dpoco, but it can be called
     * directly with a saving in time if  rcond  is not needed.
     * (time for dpoco) = (1 + 18/n)*(time for dpofa).
     * <p>
     * double precision(lda, n)
     * the symmetric matrix to be factored.  only the diagonal and upper triangle are used.
     * <p>
     * lda     integer
     * the leading dimension of the array  a.
     * <p>
     * n       integer
     * the order of the matrix  a.
     * <p>
     * on return
     * <p>
     * a       an upper triangular matrix  r  so that  a = trans(r)*r
     * where  trans(r)  is the transpose.
     * the strict lower triangle is unaltered.
     * if  info .ne. 0 , the factorization is not complete.
     * <p>
     * info    integer
     * = 0  for normal return.
     * = k  signals an error condition.
     * the leading minor of order  k  is not positive definite.
     */
    public static void dpofa(Double[][] a, double lda, int n, Integer[] info) {
        int i;
        int j;
        int jm1;
        int k;
        double t;
        double s;

        for (j = 1; j <= n; j = j + 1) {
            info[1] = j;
            s = 0.0;
            jm1 = j - 1;

            if (jm1 < 1) {
                s = a[j][j] - s;

                if (s <= 0.0) {
                    break;
                }

                a[j][j] = Math.sqrt(s);
            } else {
                for (k = 1; k <= jm1; k = k + 1) {
                    // t = a[k][j] - ddot(k - 1, a[1][k], 1, a[1][j], 1);
                    t = a[k][j];

                    for (i = 1; i < k; i = i + 1) {
                        t = t - (a[i][j] * a[i][k]);
                    }

                    t = t / a[k][k];
                    a[k][j] = t;
                    s = s + t * t;
                }

                s = a[j][j] - s;
                if (s <= 0) {
                    break;
                }

                a[j][j] = Math.sqrt(s);
            }
            info[1] = 0;
        }
    }
}
