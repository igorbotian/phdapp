package ru.spbftu.igorbotian.phdapp.quadprog;

import java.util.Arrays;

/**
 * Реализация R-пакета 'quadprog' на языке Java.
 * В свою очередь, основана на реализации данного пакета на языке JavaScript.
 *
 * @author Igor Botian <igor.botian@gmail.com>
 * @see <a href="http://cran.r-project.org/web/packages/quadprog">http://cran.r-project.org/web/packages/quadprog</a>
 * @see <a href="https://github.com/albertosantini/node-quadprog">https://github.com/albertosantini/node-quadprog</a>
 */
final class QuadProg {

    private QuadProg() {
        //
    }

    private static double vsmall = Utils.vsmall();

    private static class qpgen2 {
        private int i;
        private int j;
        private int l;
        private int l1;
        private Integer[] info;
        private int it1;
        private int iwzv;
        private int iwrv;
        private int iwrm;
        private int iwsv;
        private int iwuv;
        private int nvl;
        private int r;
        private int iwnbv;
        private double temp;
        private double sum;
        private double t1;
        private double tt;
        private double gc;
        private double gs;
        private double nu;
        private boolean t1inf;
        private boolean t2min;
        private int go;

        // qpgen2 parameters
        public Double[][] dmat;
        public Double[] dvec;
        public double fddmat;
        public int n;
        public Double[] sol;
        public Double[] lagr;
        public Double[] crval;
        public Double[][] amat;
        public Double[] bvec;
        public double fdamat;
        public int q;
        public int meq;
        public Integer[] iact;
        public int nact;
        public Integer[] iter;
        public Double[] work;
        public Integer[] ierr;

        public void qpgen2() {
            r = Math.min(n, q);
            l = 2 * n + (r * (r + 5)) / 2 + 2 * q + 1;

            // store the initial dvec to calculate below the unconstrained minima of the critical value.
            for (i = 1; i <= n; i = i + 1) {
                work[i] = dvec[i];
            }
            for (i = n + 1; i <= l; i = i + 1) {
                work[i] = 0.0;
            }
            for (i = 1; i <= q; i = i + 1) {
                iact[i] = 0;
                lagr[i] = 0.0;
            }

            info = new Integer[]{null, 0};

            // get the initial solution.
            if (ierr[1] == 0) {
                Utils.dpofa(dmat, fddmat, n, info);
                if (info[1] != 0) {
                    ierr[1] = 2;
                    return;
                }
                Utils.dposl(dmat, fddmat, n, dvec);
                Utils.dpori(dmat, fddmat, n);
            } else {
                // Matrix D is already factorized, so we have to multiply d first with R^-T and then with R^-1.
                // R^-1 is stored in the upper half of the array dmat.
                for (j = 1; j <= n; j = j + 1) {
                    sol[j] = 0.0;
                    for (i = 1; i <= j; i = i + 1) {
                        sol[j] = sol[j] + dmat[i][j] * dvec[i];
                    }
                }
                for (j = 1; j <= n; j = j + 1) {
                    dvec[j] = 0.0;
                    for (i = j; i <= n; i = i + 1) {
                        dvec[j] = dvec[j] + dmat[j][i] * sol[i];
                    }
                }
            }

            // set lower triangular of dmat to zero, store dvec in sol
            // and calculate value of the criterion at unconstrained minima
            crval[1] = 0.0;
            for (j = 1; j <= n; j = j + 1) {
                sol[j] = dvec[j];
                crval[1] = crval[1] + work[j] * sol[j];
                work[j] = 0.0;
                for (i = j + 1; i <= n; i = i + 1) {
                    dmat[i][j] = 0.0;
                }
            }
            crval[1] = -crval[1] / 2.0;
            ierr[1] = 0;

            // calculate some constants, i.e., from which index on the different quantities are stored in the work matrix
            iwzv = n;
            iwrv = iwzv + n;
            iwuv = iwrv + r;
            iwrm = iwuv + r + 1;
            iwsv = iwrm + (r * (r + 1)) / 2;
            iwnbv = iwsv + q;

            // calculate the norm of each column of the A matrix
            for (i = 1; i <= q; i = i + 1) {
                sum = 0;
                for (j = 1; j <= n; j = j + 1) {
                    sum = sum + amat[j][i] * amat[j][i];
                }
                work[iwnbv + i] = Math.sqrt(sum);
            }
            nact = 0;
            iter[1] = 0;
            iter[2] = 0;

            go = 0;
            while (true) {
                go = fn_goto_50();
                if (go == 999) {
                    return;
                }
                while (true) {
                    go = fn_goto_55();
                    if (go == 0) {
                        break;
                    }
                    if (go == 999) {
                        return;
                    }
                    if (go == 700) {
                        // if it1 = nact it is only necessary to update the vector u and nact
                        if (it1 == nact) {
                            fn_goto_799();
                        } else {
                            while (true) {
                                // After updating one row of R (column of J) we will also come back here
                                fn_goto_797();
                                go = fn_goto_798();
                                if (go != 797) {
                                    break;
                                }
                            }
                            fn_goto_799();
                        }
                    }
                }
            }
        }

        private int fn_goto_50() {
            // start a new iteration
            iter[1] = iter[1] + 1;

            // calculate all constraints and check which are still violated
            // for the equality constraints we have to check whether the normal
            // vector has to be negated (as well as bvec in that case)
            l = iwsv;
            for (i = 1; i <= q; i = i + 1) {
                l = l + 1;
                sum = -bvec[i];
                for (j = 1; j <= n; j = j + 1) {
                    sum = sum + amat[j][i] * sol[j];
                }
                if (Math.abs(sum) < vsmall) {
                    sum = 0.0;
                }
                if (i > meq) {
                    work[l] = sum;
                } else {
                    work[l] = -Math.abs(sum);
                    if (sum > 0.0) {
                        for (j = 1; j <= n; j = j + 1) {
                            amat[j][i] = -amat[j][i];
                        }
                        bvec[i] = -bvec[i];
                    }
                }
            }

            // as safeguard against rounding errors set already active constraints explicitly to zero
            for (i = 1; i <= nact; i = i + 1) {
                work[iwsv + iact[i]] = 0.0;
            }

            // we weight each violation by the number of non-zero elements in the corresponding row of A.
            // then we choose the violated constraint which has maximal absolute value, i.e., the minimum.
            // by obvious commenting and uncommenting we can choose the strategy to take always the first constraint
            // which is violated. ;-)
            nvl = 0;
            temp = 0;
            for (i = 1; i <= q; i = i + 1) {
                if (work[iwsv + i] < temp * work[iwnbv + i]) {
                    nvl = i;
                    temp = work[iwsv + i] / work[iwnbv + i];
                }
            }
            if (nvl == 0) {
                for (i = 1; i <= nact; i = i + 1) {
                    lagr[iact[i]] = work[iwuv + i];
                }
                return 999;
            }

            return 0;
        }

        private int fn_goto_55() {
            // Calculate d=J^Tn^+ where n^+ is the normal vector of the violated constraint.
            // J is stored in dmat in this implementation!!
            // if we drop a constraint, we have to jump back here.
            for (i = 1; i <= n; i = i + 1) {
                sum = 0.0;
                for (j = 1; j <= n; j = j + 1) {
                    sum = sum + dmat[j][i] * amat[j][nvl];
                }
                work[i] = sum;
            }

            // Now calculate z = J_2 d_2
            l1 = iwzv;
            for (i = 1; i <= n; i = i + 1) {
                work[l1 + i] = 0.0;
            }
            for (j = nact + 1; j <= n; j = j + 1) {
                for (i = 1; i <= n; i = i + 1) {
                    work[l1 + i] = work[l1 + i] + dmat[i][j] * work[j];
                }
            }

            // and r = R^{-1} d_1, check also if r has positive elements
            // (among the entries corresponding to inequalities constraints).
            t1inf = true;
            for (i = nact; i >= 1; i = i - 1) {
                sum = work[i];
                l = iwrm + (i * (i + 3)) / 2;
                l1 = l - i;
                for (j = i + 1; j <= nact; j = j + 1) {
                    sum = sum - work[l] * work[iwrv + j];
                    l = l + j;
                }
                sum = sum / work[l1];
                work[iwrv + i] = sum;
                if (iact[i] < meq) {
                    continue;
                }
                if (sum < 0.0) {
                    continue;
                }
                t1inf = false;
                it1 = i;
            }

            // if r has positive elements, find the partial step length t1, which is the maximum step in dual space
            // ithout violating dual feasibility.
            // it1  stores in which component t1, the min of u/r, occurs.
            if (!t1inf) {
                t1 = work[iwuv + it1] / work[iwrv + it1];
                for (i = 1; i <= nact; i = i + 1) {
                    if (iact[i] < meq) {
                        continue;
                    }
                    if (work[iwrv + i] < 0.0) {
                        continue;
                    }
                    temp = work[iwuv + i] / work[iwrv + i];
                    if (temp < t1) {
                        t1 = temp;
                        it1 = i;
                    }
                }
            }

            // test if the z vector is equal to zero
            sum = 0.0;
            for (i = iwzv + 1; i <= iwzv + n; i = i + 1) {
                sum = sum + work[i] * work[i];
            }
            if (Math.abs(sum) <= vsmall) {
                // No step in primal space such that the new constraint becomes feasible.
                // Take step in dual space and drop a constant.
                if (t1inf) {
                    // No step in dual space possible either, problem is not solvable
                    ierr[1] = 1;
                    return 999;
                } else {
                    // we take a partial step in dual space and drop constraint it1,
                    // that is, we drop the it1-th active constraint.
                    // then we continue at step 2(a) (marked by label 55)
                    for (i = 1; i <= nact; i = i + 1) {
                        work[iwuv + i] = work[iwuv + i] - t1 * work[iwrv + i];
                    }
                    work[iwuv + nact + 1] = work[iwuv + nact + 1] + t1;
                    return 700;
                }
            } else {
                // Compute full step length t2, minimum step in primal space such that the constraint becomes feasible.
                // keep sum (which is z^Tn^+) to update crval below!
                sum = 0.0;
                for (i = 1; i <= n; i = i + 1) {
                    sum = sum + work[iwzv + i] * amat[i][nvl];
                }
                tt = -work[iwsv + nvl] / sum;
                t2min = true;
                if (!t1inf) {
                    if (t1 < tt) {
                        tt = t1;
                        t2min = false;
                    }
                }

                // take step in primal and dual space
                for (i = 1; i <= n; i = i + 1) {
                    sol[i] = sol[i] + tt * work[iwzv + i];
                    /*if (Math.abs(sol[i]) < vsmall) {
                        sol[i] = 0.0;
                    }*/
                }

                crval[1] = crval[1] + tt * sum * (tt / 2.0 + work[iwuv + nact + 1]);
                for (i = 1; i <= nact; i = i + 1) {
                    work[iwuv + i] = work[iwuv + i] - tt * work[iwrv + i];
                }
                work[iwuv + nact + 1] = work[iwuv + nact + 1] + tt;

                // if it was a full step, then we check wheter further constraints are violated
                // otherwise we can drop the current constraint and iterate once more
                if (t2min) {
                    // we took a full step. Thus add constraint nvl to the list of active constraints and update J and R
                    nact = nact + 1;
                    iact[nact] = nvl;

                    // to update R we have to put the first nact-1 components of the d vector into column (nact) of R
                    l = iwrm + ((nact - 1) * nact) / 2 + 1;
                    for (i = 1; i <= nact - 1; i = i + 1) {
                        work[l] = work[i];
                        l = l + 1;
                    }

                    // if now nact=n, then we just have to add the last element to the new row of R.
                    // Otherwise we use Givens transformations to turn the vector d(nact:n)
                    // into a multiple of the first unit vector.
                    // That multiple goes into the last element of the new row of R and J is accordingly
                    // updated by the Givens transformations.
                    if (nact == n) {
                        work[l] = work[n];
                    } else {
                        for (i = n; i >= nact + 1; i = i - 1) {
                            // we have to find the Givens rotation which will reduce the element (l1) of d to zero.
                            // if it is already zero we don't have to do anything, except of decreasing l1
                            if (work[i] == 0) {
                                continue;
                            }
                            gc = Math.max(Math.abs(work[i - 1]), Math.abs(work[i]));
                            gs = Math.min(Math.abs(work[i - 1]), Math.abs(work[i]));
                            if (work[i - 1] >= 0) {
                                temp = Math.abs(gc * Math.sqrt(1 + gs * gs /
                                        (gc * gc)));
                            } else {
                                temp = -Math.abs(gc * Math.sqrt(1 + gs * gs /
                                        (gc * gc)));
                            }
                            gc = work[i - 1] / temp;
                            gs = work[i] / temp;

                            // The Givens rotation is done with the matrix (gc gs, gs -gc).
                            // If gc is one, then element (i) of d is zero compared with element (l1-1).
                            // Hence we don't have to do anything.
                            // If gc is zero, then we just have to switch column (i) and column (i-1) of J.
                            // Since we only switch columns in J, we have to be careful how we update d depending on
                            // the sign of gs.
                            // Otherwise we have to apply the Givens rotation to these columns.
                            // The i-1 element of d has to be updated to temp.

                            if (gc == 1.0) {
                                continue;
                            }
                            if (gc == 0.0) {
                                work[i - 1] = gs * temp;
                                for (j = 1; j <= n; j = j + 1) {
                                    temp = dmat[j][i - 1];
                                    dmat[j][i - 1] = dmat[j][i];
                                    dmat[j][i] = temp;
                                }
                            } else {
                                work[i - 1] = temp;
                                nu = gs / (1.0 + gc);
                                for (j = 1; j <= n; j = j + 1) {
                                    temp = gc * dmat[j][i - 1] + gs * dmat[j][i];
                                    dmat[j][i] = nu * (dmat[j][i - 1] + temp) - dmat[j][i];
                                    dmat[j][i - 1] = temp;
                                }
                            }
                        }

                        // l is still pointing to element (nact,nact) of the matrix R.
                        // So store d(nact) in R(nact,nact)
                        work[l] = work[nact];
                    }
                } else {
                    // we took a partial step in dual space. Thus drop constraint it1,
                    // that is, we drop the it1-th active constraint.
                    // then we continue at step 2(a) (marked by label 55)
                    // but since the fit changed, we have to recalculate now "how much"
                    // the fit violates the chosen constraint now.
                    sum = -bvec[nvl];
                    for (j = 1; j <= n; j = j + 1) {
                        sum = sum + sol[j] * amat[j][nvl];
                    }
                    if (nvl > meq) {
                        work[iwsv + nvl] = sum;
                    } else {
                        work[iwsv + nvl] = -Math.abs(sum);
                        if (sum > 0.0) {
                            for (j = 1; j <= n; j = j + 1) {
                                amat[j][nvl] = -amat[j][nvl];
                            }
                            bvec[nvl] = -bvec[nvl];
                        }
                    }

                    return 700;
                }

                // Drop constraint it1
            }

            return 0;
        }

        private int fn_goto_797() {
            // we have to find the Givens rotation which will reduce the element (it1+1,it1+1) of R to zero.
            // if it is already zero we don't have to do anything except of updating
            // u, iact, and shifting column (it1+1) of R to column (it1)
            // l  will point to element (1,it1+1) of R
            // l1 will point to element (it1+1,it1+1) of R
            l = iwrm + (it1 * (it1 + 1)) / 2 + 1;
            l1 = l + it1;
            if (work[l1] == 0.0) {
                return 798;
            }
            gc = Math.max(Math.abs(work[l1 - 1]), Math.abs(work[l1]));
            gs = Math.min(Math.abs(work[l1 - 1]), Math.abs(work[l1]));
            if (work[l1 - 1] >= 0) {
                temp = Math.abs(gc * Math.sqrt(1 + gs * gs / (gc * gc)));
            } else {
                temp = -Math.abs(gc * Math.sqrt(1 + gs * gs / (gc * gc)));
            }

            gc = work[l1 - 1] / temp;
            gs = work[l1] / temp;

            // The Givens rotatin is done with the matrix (gc gs, gs -gc).
            // If gc is one, then element (it1+1,it1+1) of R is zero compared with
            // element (it1,it1+1). Hence we don't have to do anything.
            // if gc is zero, then we just have to switch row (it1) and row (it1+1)
            // of R and column (it1) and column (it1+1) of J. Since we swithc rows in
            // R and columns in J, we can ignore the sign of gs.
            // Otherwise we have to apply the Givens rotation to these rows/columns.
            if (gc == 1.0) {
                return 798;
            }
            if (gc == 0.0) {
                for (i = it1 + 1; i <= nact; i = i + 1) {
                    temp = work[l1 - 1];
                    work[l1 - 1] = work[l1];
                    work[l1] = temp;
                    l1 = l1 + i;
                }
                for (i = 1; i <= n; i = i + 1) {
                    temp = dmat[i][it1];
                    dmat[i][it1] = dmat[i][it1 + 1];
                    dmat[i][it1 + 1] = temp;
                }
            } else {
                nu = gs / (1 + gc);
                for (i = it1 + 1; i <= nact; i = i + 1) {
                    temp = gc * work[l1 - 1] + gs * work[l1];
                    work[l1] = nu * (work[l1 - 1] + temp) - work[l1];
                    work[l1 - 1] = temp;
                    l1 = l1 + i;
                }
                for (i = 1; i <= n; i = i + 1) {
                    temp = gc * dmat[i][it1] + gs * dmat[i][it1 + 1];
                    dmat[i][it1 + 1] = nu * (dmat[i][it1] + temp) - dmat[i][it1 + 1];
                    dmat[i][it1] = temp;
                }
            }

            return 0;
        }

        private int fn_goto_798() {
            // shift column (it1+1) of R to column (it1) (that is, the first it1 elements).
            // The posit1on of element (1,it1+1) of R was calculated above and stored in l.
            l1 = l - it1;
            for (i = 1; i <= it1; i = i + 1) {
                work[l1] = work[l];
                l = l + 1;
                l1 = l1 + 1;
            }

            // update vector u and iact as necessary
            // Continue with updating the matrices J and R
            work[iwuv + it1] = work[iwuv + it1 + 1];
            iact[it1] = iact[it1 + 1];
            it1 = it1 + 1;
            if (it1 < nact) {
                return 797;
            }

            return 0;
        }

        private int fn_goto_799() {
            work[iwuv + nact] = work[iwuv + nact + 1];
            work[iwuv + nact + 1] = 0.0;
            iact[nact] = 0;
            nact = nact - 1;
            iter[2] = iter[2] + 1;

            return 0;
        }
    }

    public static Solution solveQP(Double[][] Dmat, Double[] dvec, Double[][] Amat, Double[] bvec) {
        return solveQP(Dmat, dvec, Amat, bvec, 0, new Integer[]{null, 0});
    }

    public static Solution solveQP(Double[][] Dmat, Double[] dvec, Double[][] Amat) {
        Double[] bvec = new Double[Dmat.length];
        Arrays.fill(bvec, 0.0);
        bvec[0] = null;

        return solveQP(Dmat, dvec, Amat, bvec, 0, new Integer[]{null, 0});
    }

    public static Solution solveQP(Double[][] Dmat, Double[] dvec, Double[][] Amat, Double[] bvec, int meq,
                                   Integer[] factorized) {
        int i;
        int n;
        int q;
        int nact;
        int r;
        Integer[] iter = new Integer[]{null, 0, 0};
        String message;

        fixPositiveDefinition(Dmat);

        // In Fortran the array index starts from 1
        n = Dmat.length - 1;
        q = Amat[1].length - 1;

        Integer[] iact = new Integer[q + 1];
        iact[0] = null;

        Double[] lagr = new Double[q + 1];
        lagr[0] = null;

        for (i = 1; i <= q; i = i + 1) {
            iact[i] = 0;
            lagr[i] = 0.0;
        }

        nact = 0;
        r = Math.min(n, q);

        Double[] sol = new Double[n + 1];
        sol[0] = null;

        for (i = 1; i <= n; i = i + 1) {
            sol[i] = 0.0;
        }

        Double[] work = new Double[(2 * n + (r * (r + 5)) / 2 + 2 * q + 1) + 1];
        work[0] = null;

        Double[] crval = new Double[]{null, 0.0};
        for (i = 1; i <= (2 * n + (r * (r + 5)) / 2 + 2 * q + 1); i = i + 1) {
            work[i] = 0.0;
        }
        for (i = 1; i <= 2; i = i + 1) {
            iter[i] = 0;
        }

        qpgen2 obj = new qpgen2();
        obj.dmat = Dmat;
        obj.dvec = dvec;
        obj.fddmat = n;
        obj.n = n;
        obj.sol = sol;
        obj.lagr = lagr;
        obj.crval = crval;
        obj.amat = Amat;
        obj.bvec = bvec;
        obj.fdamat = n;
        obj.q = q;
        obj.meq = meq;
        obj.iact = iact;
        obj.nact = nact;
        obj.iter = iter;
        obj.work = work;
        obj.ierr = factorized;
        obj.qpgen2();

        message = "";
        if (factorized[1] == 1) {
            message = "constraints are inconsistent, no solution!";
        }
        if (factorized[1] == 2) {
            message = "matrix D in quadratic function is not positive definite!";
        }

        Solution solution = new Solution();
        solution.solution = sol;
        solution.Lagrangian = lagr;
        solution.value = crval;
        solution.unconstrained_solution = dvec;
        solution.iterations = iter;
        solution.iact = iact;
        solution.message = message;
        return solution;
    }

    private static void fixPositiveDefinition(Double[][] dmat) {
        for (int i = 1; i < dmat.length; i++) {
            if (i <= dmat[i].length) {
                dmat[i][i] += 0.000000000001;
            }
        }
    }

    public static final class Solution {

        public Double[] solution;
        public Double[] Lagrangian;
        public Double[] value;
        public Double[] unconstrained_solution;
        public Integer[] iterations;
        public Integer[] iact;
        public String message;
    }
}
