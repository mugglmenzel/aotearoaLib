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
 *           By Author: $Author: mugglmenzel@gmail.com $ 
 *         
 *           Revision: $Revision: 241 $ 
 *         
 *           Date: $Date: 2011-09-24 15:39:41 +0200 (Sa, 24 Sep 2011) $
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
 *         $HeadURL: https://aotearoadecisions.googlecode.com/svn/trunk/src/main/java/de/fzi/aotearoa/shared/model/ahp/values/AlternativeValue.java $
 *
 */

public class AlternativeValue implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2405585146889871944L;

	/**
	 * @uml.property  name="criterion"
	 * @uml.associationEnd  inverse="valuesAlternatives:de.eorg.cumulusgenius.shared.cloudmapping.model.ahp.configuration.Criterion"
	 */
	private Criterion criterion;

	/**
	 * @uml.property  name="alt"
	 */
	private int alt;

	/**
	 * @uml.property  name="value"
	 * @uml.associationEnd  
	 */
	private double value = 1D;
	
	/**
	 * @uml.property  name="description"
	 */
	private String description;

	/**
	 * 
	 */
	public AlternativeValue() {
		super();
	}

	/**
	 * @param alt
	 * @param value
	 */
	public AlternativeValue(int alt, Criterion c, double value, String description) {
		super();
		this.alt = alt;
		this.criterion = c;
		this.value = value;
		this.description = description;
	}


	/**
	 * @return  the criterion
	 * @uml.property  name="criterion"
	 */
	public Criterion getCriterion() {
		return criterion;
	}

	/**
	 * @param criterion  the criterion to set
	 * @uml.property  name="criterion"
	 */
	public void setCriterion(Criterion criterion) {
		this.criterion = criterion;
	}

	/**
	 * @return  the alt
	 * @uml.property  name="alt"
	 */
	public int getAlt() {
		return alt;
	}

	/**
	 * @param alt  the alt to set
	 * @uml.property  name="alt"
	 */
	public void setAlt(int alt) {
		this.alt = alt;
	}

	/**
	 * @return  the value
	 * @uml.property  name="value"
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value  the value to set
	 * @uml.property  name="value"
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return  the description
	 * @uml.property  name="description"
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description  the description to set
	 * @uml.property  name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
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
		result = prime * result + alt;
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AlternativeValue other = (AlternativeValue) obj;
		if (alt != other.alt) {
			return false;
		}
		if (criterion == null) {
			if (other.criterion != null) {
				return false;
			}
		} else if (!criterion.equals(other.criterion)) {
			return false;
		}
		return true;
	}

	
	public AlternativeValue clone() {
		return new AlternativeValue(getAlt(), getCriterion(), getValue(), getDescription());
	}

}
