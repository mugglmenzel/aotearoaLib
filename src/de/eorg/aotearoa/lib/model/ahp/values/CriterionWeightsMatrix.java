package de.eorg.aotearoa.lib.model.ahp.values;


import de.eorg.aotearoa.lib.jama.Matrix;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author mugglmenzel
 *         <p/>
 *         Author: Michael Menzel (mugglmenzel)
 *         <p/>
 *         Last Change:
 *         <p/>
 *         By Author: $Author: mugglmenzel@gmail.com $
 *         <p/>
 *         Revision: $Revision: 221 $
 *         <p/>
 *         Date: $Date: 2011-09-19 10:55:30 +0200 (Mo, 19 Sep 2011) $
 *         <p/>
 *         License:
 *         <p/>
 *         Copyright 2011 Forschungszentrum Informatik FZI / Karlsruhe Institute
 *         of Technology
 *         <p/>
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 *         <p/>
 *         http://www.apache.org/licenses/LICENSE-2.0
 *         <p/>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 *         <p/>
 *         <p/>
 *         SVN URL: $HeadURL:
 *         https://aotearoadecisions.googlecode.com/svn/trunk/
 *         src/main/java/de/fzi
 *         /aotearoa/shared/model/ahp/values/CriterionWeightsMatrix.java $
 */

public class CriterionWeightsMatrix implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -1488435510945771277L;

    // the order of this list must not be changed
    /**
     * @uml.property name="numberOfCriteria"
     */
    private int numberOfCriteria = 1;

    /**
     * @uml.property name="values"
     */
    private final List<CriterionImportance> values = new ArrayList<CriterionImportance>();

    /**
     * @uml.property name="matrix"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    private Matrix matrix = Matrix.identity(1, 1);

    /**
     * @uml.property name="comparison"
     * @uml.associationEnd qualifier="valueOf:java.lang.Double java.lang.Double"
     */
    private final HashMap<Double, Double> comparison = new HashMap<Double, Double>();

    public CriterionWeightsMatrix(int numCriteria,
                                  List<CriterionImportance> weights) {
        super();
        comparison.put(-9D, 1D / 10D);
        comparison.put(-8D, 1D / 9D);
        comparison.put(-7D, 1D / 8D);
        comparison.put(-6D, 1D / 7D);
        comparison.put(-5D, 1D / 6D);
        comparison.put(-4D, 1D / 5D);
        comparison.put(-3D, 1D / 4D);
        comparison.put(-2D, 1D / 3D);
        comparison.put(-1D, 1D / 2D);
        comparison.put(0D, 1D);
        comparison.put(1D, 2D);
        comparison.put(2D, 3D);
        comparison.put(3D, 4D);
        comparison.put(4D, 5D);
        comparison.put(5D, 6D);
        comparison.put(6D, 7D);
        comparison.put(7D, 8D);
        comparison.put(8D, 9D);
        comparison.put(9D, 10D);

        numberOfCriteria = numCriteria;

        if (weights != null && weights.size() > 0)
            values.addAll(weights);

        if (!(values.size() >= numberOfCriteria * (numberOfCriteria - 1) / 2))
            for (int i = 0; i < numberOfCriteria - 1; i++)
                for (int j = i + 1; j < numberOfCriteria; j++)
                    values.add(new CriterionImportance(i, j, 0D, null));

        setMatrixWeights();

    }

    /**
     * @return
     * @uml.property name="matrix"
     */
    public Matrix getMatrix() {
        return matrix;
    }

    private void setMatrixWeights() {
        double[][] mat = Matrix.identity(numberOfCriteria, numberOfCriteria).getArray();
        for (CriterionImportance value : values) {
            double val = comparison.get(value.getComparisonAToB());
            mat[value.getCritA()][value.getCritB()] = val;
            if (val != 0D)
                mat[value.getCritB()][value.getCritA()] = 1D / val;
        }
        matrix = new Matrix(mat);

    }

    public String checkMatrix() {

        // check matrix size
        int size = numberOfCriteria;
        if (matrix.getColumnDimension() != size) {
            return "false column dimension";
        }

        if (matrix.getRowDimension() != size) {
            return "false row dimension";
        }

        // check diogonal in matrix (must be 1)
        for (int i = 0; i < size; i++) {
            if (matrix.get(i, i) != 1) {
                return "diagonal not 1";
            }
        }

        // check inverse
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix.get(i, j) != (1 / matrix.get(j, i))) {
                    return "false inverse";
                }
            }
        }

        return "matrix ok";
    }

}
