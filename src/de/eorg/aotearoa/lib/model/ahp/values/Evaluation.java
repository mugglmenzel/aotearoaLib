package de.eorg.aotearoa.lib.model.ahp.values;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.eorg.aotearoa.lib.jama.Matrix;
import de.eorg.aotearoa.lib.model.ahp.configuration.Decision;

/**
 * 
 * @author mugglmenzel
 *
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 *           
 *           By Author: $Author: mugglmenzel@gmail.com $ 
 *         
 *           Revision: $Revision: 220 $ 
 *         
 *           Date: $Date: 2011-09-16 18:58:00 +0200 (Fr, 16 Sep 2011) $
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
 *         SVN URL: 
 *         $HeadURL: https://aotearoadecisions.googlecode.com/svn/trunk/src/main/java/de/fzi/aotearoa/shared/model/ahp/values/Evaluation.java $
 *
 */

public class Evaluation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1786731159904287894L;
	
	
	// evaluations
	/**
	 * @uml.property  name="evaluations"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="de.eorg.cumulusgenius.shared.cloudmapping.model.jama.Matrix"
	 */
	private List<Matrix> evaluations = new ArrayList<Matrix>();

	
	
	/**
	 * 
	 */
	public Evaluation() {
		super();
	}

	/**
	 * @return the evaluations
	 */
	public List<Matrix> getEvaluations() {
		return evaluations;
	}

	public void update(Decision<?> decision) {
		if (decision.getGoals().size() > 0)
			for (int i = 0; i < decision.getGoals().get(0).getLeafCriteria()
					.size(); i++)
				if (getEvaluations().size() <= i)
					getEvaluations().add(
							new Matrix(decision.getAlternatives().size(),
									decision.getAlternatives().size()));
				else if (getEvaluations().get(i) == null)
					getEvaluations().set(
							i,
							new Matrix(decision.getAlternatives().size(),
									decision.getAlternatives().size()));
				else if (getEvaluations().get(i).getColumnDimension() != decision
						.getAlternatives().size()
						|| getEvaluations().get(i).getRowDimension() != decision
								.getAlternatives().size())
					getEvaluations().set(
							i,
							Matrix.identity(decision.getAlternatives().size(),
									decision.getAlternatives().size()));
	}

}
