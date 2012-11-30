package de.eorg.aotearoa.lib.model.ahp.values;

import java.io.Serializable;

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
 *         Revision: $Revision: 241 $
 * 
 *         Date: $Date: 2011-09-24 15:39:41 +0200 (Sa, 24 Sep 2011) $
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
 *         /aotearoa/shared/model/ahp/values/AlternativeImportance.java $
 * 
 */

public class AlternativeImportance implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8084248328316108208L;

	/**
	 * @uml.property name="criterion"
	 * @uml.associationEnd inverse=
	 *                     "importanceAlternatives:de.eorg.cumulusgenius.shared.cloudmapping.model.ahp.configuration.Criterion"
	 */
	private Criterion criterion;

	/**
	 * @uml.property name="altA"
	 */
	private int altA;

	/**
	 * @uml.property name="altB"
	 */
	private int altB;

	/**
	 * @uml.property name="comparisonAToB"
	 */
	private double comparisonAToB;

	/**
	 * @uml.property name="description"
	 */
	private String description;

	/**
	 * 
	 */
	public AlternativeImportance() {
		super();
	}

	/**
	 * 
	 * @param altA
	 * @param altB
	 * @param comparisonAToB
	 */
	public AlternativeImportance(int altA, int altB, Criterion c,
			double comparisonAToB, String description) {
		super();
		this.altA = altA;
		this.altB = altB;
		this.criterion = c;
		this.comparisonAToB = comparisonAToB;
		this.description = description;
	}

	/**
	 * @return the Alternative A
	 * @uml.property name="altA"
	 */
	public int getAltA() {
		return altA;
	}

	/**
	 * @param altA
	 *            the Alternative A to set
	 * @uml.property name="altA"
	 */
	public void setAltA(int altA) {
		this.altA = altA;
	}

	/**
	 * @return the Alternative B
	 * @uml.property name="altB"
	 */
	public int getAltB() {
		return altB;
	}

	/**
	 * @param altB
	 *            the Alternative B to set
	 * @uml.property name="altB"
	 */
	public void setAltB(int altB) {
		this.altB = altB;
	}

	/**
	 * @return the comparison value between alternative A and B
	 * @uml.property name="comparisonAToB"
	 */
	public double getComparisonAToB() {
		return comparisonAToB;
	}

	/**
	 * @param comparisonAToB
	 *            the comparison value between alternative A and B to set
	 * @uml.property name="comparisonAToB"
	 */
	public void setComparisonAToB(double comparisonAToB) {
		this.comparisonAToB = comparisonAToB;
	}

	/**
	 * @return the description
	 * @uml.property name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 * @uml.property name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param criterion
	 *            the criterion to set
	 * @uml.property name="criterion"
	 */
	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}

	/**
	 * @return the criterion
	 * @uml.property name="criterion"
	 */
	public Criterion getCriterion() {
		return criterion;
	}

	
	public AlternativeImportance clone() {
		return new AlternativeImportance(getAltA(), getAltB(), getCriterion(),
				getComparisonAToB(), getDescription());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + altA;
		result = prime * result + altB;
		result = prime * result
				+ ((criterion == null) ? 0 : criterion.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlternativeImportance other = (AlternativeImportance) obj;
		if (altA != other.altA)
			return false;
		if (altB != other.altB)
			return false;
		if (criterion == null) {
			if (other.criterion != null)
				return false;
		} else if (!criterion.equals(other.criterion))
			return false;
		return true;
	}

}
