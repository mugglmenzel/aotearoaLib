package de.eorg.aotearoa.lib.model.ahp.values;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import de.eorg.aotearoa.lib.jama.Matrix;
import de.eorg.aotearoa.lib.model.ahp.configuration.Criterion;

/**
 * 
 * @author mugglmenzel
 * 
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 * 
 *         By Author: $Author: mugglmenzel@gmail.com $
 * 
 *         Revision: $Revision: 244 $
 * 
 *         Date: $Date: 2011-09-26 02:27:36 +0200 (Mo, 26 Sep 2011) $
 * 
 *         License:
 * 
 *         Copyright 2011 Forschungszentrum Informatik FZI / Karlsruhe Institute
 *         of Technology
 * 
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 * 
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 * 
 * 
 *         SVN URL: $HeadURL:
 *         https://aotearoadecisions.googlecode.com/svn/trunk/
 *         src/main/java/de/fzi
 *         /aotearoa/shared/model/ahp/values/AlternativeValuesMatrix.java $
 * 
 * 
 */

public class AlternativeValuesMatrix implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2671684884977491386L;

	// the order of this list must not be changed
	/**
	 * @uml.property name="numberOfAlternatives"
	 */
	private int numberOfAlternatives = 1;

	/**
	 * @uml.property name="values"
	 */
	private final List<AlternativeValue> values = new ArrayList<AlternativeValue>();

	/**
	 * @uml.property name="matrix"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private Matrix matrix = Matrix.identity(1, 1);

	/**
	 * @param values
	 */
	public AlternativeValuesMatrix(int alternatives,
			List<AlternativeValue> values, Criterion c) {
		super();

		numberOfAlternatives = alternatives;

		if (values != null && values.size() > 0)
			this.values.addAll(values);

		if (!(values.size() >= numberOfAlternatives))
			for (int i = 0; i < numberOfAlternatives; i++)
				if (!values.contains(new AlternativeValue(i, c, 0D, null)))
					values.add(new AlternativeValue(i, c, 0D, null));

		matrix = Matrix.identity(numberOfAlternatives, numberOfAlternatives);
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

		Vector<Double> vals = new Vector<Double>(numberOfAlternatives);
		for (int i = 0; i < numberOfAlternatives; i++)
			vals.add(1D);

		for (AlternativeValue value : values) {
			vals.set(value.getAlt(), value.getValue());
		}

		for (int i = 0; i < vals.size() - 1; i++) {
			for (int j = i + 1; j < vals.size(); j++) {
				if (vals.get(i) != null && vals.get(j) != null) {
					matrix.set(i, j, vals.get(i) / vals.get(j));
					matrix.set(j, i, vals.get(j) / vals.get(i));
				}
			}
		}

	}

	public String checkMatrix() {

		// check matrix size
		int size = numberOfAlternatives;
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
