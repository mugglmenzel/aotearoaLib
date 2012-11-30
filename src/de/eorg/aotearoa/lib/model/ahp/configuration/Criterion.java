package de.eorg.aotearoa.lib.model.ahp.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.eorg.aotearoa.lib.model.ahp.values.AlternativeImportance;
import de.eorg.aotearoa.lib.model.ahp.values.AlternativeValue;
import de.eorg.aotearoa.lib.model.ahp.values.CriterionImportance;

/**
 * 
 * @author menzel A Criterion is a node in an AHP hierarchy and also an aspect
 *         to evaluate an Alternative upon.
 * 
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 * 
 *         By Author: $Author: mugglmenzel@gmail.com $
 * 
 *         Revision: $Revision: 242 $
 * 
 *         Date: $Date: 2011-09-25 17:48:24 +0200 (So, 25 Sep 2011) $
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
 *         /aotearoa/shared/model/ahp/configuration/Criterion.java $
 * 
 */

public class Criterion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 949425245049027952L;

	/**
	 * @uml.property name="name"
	 */
	protected String name;

	/**
	 * @uml.property name="description"
	 */
	protected String description;

	/**
	 * @uml.property name="metric"
	 */
	protected String metric;

	/**
	 * @uml.property name="url"
	 */
	protected String url;

	/**
	 * @uml.property name="type"
	 * @uml.associationEnd
	 */
	protected CriterionType type;

	/**
	 * @uml.property name="weight"
	 */
	protected double weight = 1D;

	/**
	 * @uml.property name="parent"
	 * @uml.associationEnd inverse=
	 *                     "children:de.eorg.cumulusgenius.shared.cloudmapping.model.ahp.configuration.Criterion"
	 */
	private Criterion parent;

	// qualitative
	/**
	 * @uml.property name="importanceAlternatives"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse=
	 *                     "criterion:de.eorg.cumulusgenius.shared.cloudmapping.model.ahp.values.AlternativeImportance"
	 */
	protected List<AlternativeImportance> importanceAlternatives = new ArrayList<AlternativeImportance>();

	// quantitative
	/**
	 * @uml.property name="valuesAlternatives"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse=
	 *                     "criterion:de.eorg.cumulusgenius.shared.cloudmapping.model.ahp.values.AlternativeValue"
	 */
	protected List<AlternativeValue> valuesAlternatives = new ArrayList<AlternativeValue>();

	/**
	 * @uml.property name="importanceChildren"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse=
	 *                     "parent:de.eorg.cumulusgenius.shared.cloudmapping.model.ahp.values.CriterionImportance"
	 */
	protected List<CriterionImportance> importanceChildren = new ArrayList<CriterionImportance>();

	/**
	 * @uml.property name="children"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse=
	 *                     "parent:de.eorg.cumulusgenius.shared.cloudmapping.model.ahp.configuration.Criterion"
	 */
	protected List<Criterion> children = new ArrayList<Criterion>();

	public Criterion() {
		super();
	}

	public Criterion(String name) {
		super();
		this.name = name;
	}

	public void addChild(Criterion c) {
		getChildren().add(c);
		c.setParent(this);
	}

	public int countCriteria() {
		Iterator<Criterion> iti = children.iterator();
		int help = 0;
		while (iti.hasNext()) {
			iti.next();
			help++;
		}
		return help;
	}

	public String deleteCriterion(String criterionName) {
		Iterator<Criterion> iti = this.getChildren().iterator();
		Criterion help = null;
		while (iti.hasNext()) {
			help = iti.next();
			if (criterionName.equals(help.getName())) {
				iti.remove();
				return "removed";
			} else {
				if ((help.deleteCriterion(criterionName)) != null) {
					return "removed";
				}
			}
		}

		return null;
	}

	public ArrayList<Criterion> getAllDescendants() {
		Iterator<Criterion> iti = this.getChildren().iterator();
		ArrayList<Criterion> allDescendants = new ArrayList<Criterion>();
		Criterion help = null;
		while (iti.hasNext()) {
			help = iti.next();
			allDescendants.add(help);
			if (help.hasChildren()) {
				allDescendants.addAll(help.getAllDescendants());
			}
		}
		return allDescendants;
	}

	public List<Criterion> getChildren() {
		return children;
	}

	/**
	 * @author frauen
	 * @return
	 * 
	 *         This methods returns every level of the criteria tree as a
	 *         separate list as a whole list.
	 * 
	 */
	public List<List<Criterion>> getCriteriaByLevels() {
		List<List<Criterion>> criteriaByLevelList = new ArrayList<List<Criterion>>();

		if (getChildren() != null && getChildren().size() > 0) {
			criteriaByLevelList.add(this.getChildren());

			List<Criterion> childrenCriteria = new ArrayList<Criterion>();
			childrenCriteria.addAll(this.getChildren());

			List<Criterion> newChildrenCriteria = new ArrayList<Criterion>();
			newChildrenCriteria.addAll(childrenCriteria);

			boolean isLeafLeft = true;

			while (isLeafLeft) {
				isLeafLeft = false;
				childrenCriteria.clear();
				childrenCriteria.addAll(newChildrenCriteria);
				newChildrenCriteria.clear();
				for (Criterion criterion : childrenCriteria) {
					if (!criterion.isLeaf()) {
						criteriaByLevelList.add(criterion.getChildren());
						newChildrenCriteria.addAll(criterion.getChildren());
						for (Criterion criterion2 : criterion.getChildren()) {
							if (!criterion2.isLeaf()) {
								isLeafLeft = true;
							}
						}
					}
				}
			}

		}
		return criteriaByLevelList;
	}

	public Criterion getCriterion(String criterionName) {
		Iterator<Criterion> iti = this.getChildren().iterator();
		Criterion help = null;
		Criterion help2 = null;
		while (iti.hasNext()) {
			help = iti.next();
			if (criterionName.equals(help.getName()))
				return help;
			else if ((help2 = help.getCriterion(criterionName)) != null)
				return help2;
		}

		return null;
	}

	public Criterion getParentCriterion(String criterionName) {
		Iterator<Criterion> iti = this.getChildren().iterator();
		Criterion help = null;
		Criterion help2 = null;
		while (iti.hasNext()) {
			help = iti.next();
			if (criterionName.equals(help.getName()))
				return this;
			else if ((help2 = help.getParentCriterion(criterionName)) != null)
				return help2;
		}

		return null;
	}

	/**
	 * @return
	 * @uml.property name="description"
	 */
	public String getDescription() {
		return description;
	}

	public double getGlobalWeight() {
		return hasParent() ? getParent().getGlobalWeight() * getWeight()
				: getWeight();
	}

	/**
	 * @return the importanceAlternatives
	 */
	public List<AlternativeImportance> getImportanceAlternatives() {
		return importanceAlternatives;
	}

	/**
	 * @return the importanceChildren
	 */
	public List<CriterionImportance> getImportanceChildren() {
		return importanceChildren;
	}

	public List<Criterion> getLeafCriteria() {
		return getLeafCriteria(null);
	}

	public List<Criterion> getLeafCriteria(CriterionType type) {
		List<Criterion> leafCriteria = new ArrayList<Criterion>();

		if (isLeaf() && (type == null || type.equals(getType()))) {
			leafCriteria.add(this);
		} else {
			Iterator<Criterion> iti = getChildren().iterator();
			while (iti.hasNext()) {
				leafCriteria.addAll(iti.next().getLeafCriteria(type));
			}
		}
		return leafCriteria;
	}

	/**
	 * @return
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the parent
	 * @uml.property name="parent"
	 */
	public Criterion getParent() {
		return parent;
	}

	/**
	 * @return
	 * @uml.property name="type"
	 */
	public CriterionType getType() {
		return type;
	}

	/**
	 * @return the valuesAlternatives
	 */
	public List<AlternativeValue> getValuesAlternatives() {
		return valuesAlternatives;
	}

	/**
	 * @return
	 * @uml.property name="weight"
	 */
	public double getWeight() {
		return weight;
	}

	public boolean hasChildren() {
		return !getChildren().isEmpty();
	}

	public boolean hasParent() {
		return parent != null;
	}

	public boolean isLeaf() {
		return getChildren().isEmpty();
	}

	public void setChildren(List<Criterion> children) {
		this.children = children;
	}

	/**
	 * @param description
	 * @uml.property name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param importanceAlternatives
	 *            the importanceAlternatives to set
	 */
	public void setImportanceAlternatives(
			List<AlternativeImportance> importanceAlternatives) {
		this.importanceAlternatives = importanceAlternatives;
	}

	public void insertImportanceAlternative(AlternativeImportance ai) {
		if (getImportanceAlternatives().contains(ai)) {
			for (AlternativeImportance alti : getImportanceAlternatives())
				if (alti.equals(ai)) {
					alti.setComparisonAToB(ai.getComparisonAToB());
					alti.setDescription(ai.getDescription());
				}
		} else
			getImportanceAlternatives().add(ai);
	}

	/**
	 * @param importanceChildren
	 *            the importanceChildren to set
	 */
	public void setImportanceChildren(
			List<CriterionImportance> importanceChildren) {
		this.importanceChildren = importanceChildren;
	}

	public void insertImportanceChild(CriterionImportance ci) {
		if (getImportanceChildren().contains(ci)) {
			for (CriterionImportance impi : getImportanceChildren())
				if (impi.equals(ci)) {
					impi.setComparisonAToB(ci.getComparisonAToB());
					impi.setComment(ci.getComment());
				}
		} else
			getImportanceChildren().add(ci);
	}

	/**
	 * @param name
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param parent
	 *            the parent to set
	 * @uml.property name="parent"
	 */
	public void setParent(Criterion parent) {
		this.parent = parent;
	}

	/**
	 * @param type
	 * @uml.property name="type"
	 */
	public void setType(CriterionType type) {
		this.type = type;
	}

	/**
	 * @param valuesAlternatives
	 *            the valuesAlternatives to set
	 */
	public void setValuesAlternatives(List<AlternativeValue> valuesAlternatives) {
		this.valuesAlternatives = valuesAlternatives;
	}

	public void insertValueAlternative(AlternativeValue av) {
		if (getValuesAlternatives().contains(av)) {
			for (AlternativeValue alti : getValuesAlternatives())
				if (alti.equals(av)) {
					alti.setValue(av.getValue());
					alti.setDescription(av.getDescription());
				}
		} else
			getValuesAlternatives().add(av);
	}

	/**
	 * @param weight
	 * @uml.property name="weight"
	 */
	public void setWeight(double weight) {
		this.weight = weight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + "/parent:"
				+ (hasParent() ? getParent().getName() : "<no parent>")
				+ "/children:[ " + getChildren() + "]/importance:["
				+ importanceChildren + "]";
	}

	public CriterionImportance getImportanceChild(int i, int j) {
		CriterionImportance test = new CriterionImportance(i, j, 1D, null);
		test.setParent(this);
		if (!getImportanceChildren().contains(test))
			return null;
		for (CriterionImportance ci : getImportanceChildren())
			if (test.equals(ci))
				return ci;
		return null;
	}

	public AlternativeImportance getImportanceAlternative(int i, int j) {
		AlternativeImportance test = new AlternativeImportance(i, j, this, 1D,
				null);
		if (!getImportanceAlternatives().contains(test))
			return null;
		for (AlternativeImportance ci : getImportanceAlternatives())
			if (test.equals(ci))
				return ci;
		return null;
	}

	public AlternativeValue getValueAlternative(int i) {
		AlternativeValue test = new AlternativeValue(i, this, 1D, null);
		test.setCriterion(this);
		if (!getValuesAlternatives().contains(test))
			return null;
		for (AlternativeValue av : getValuesAlternatives())
			if (test.equals(av))
				return av;
		return null;
	}

	
	public Criterion clone() {
		Criterion c = new Criterion(getName());
		c.setDescription(getDescription());
		c.setType(getType());
		c.setWeight(getWeight());
		for (Criterion ch : getChildren())
			c.addChild(ch.clone());
		for (CriterionImportance ci : getImportanceChildren())
			c.getImportanceChildren().add(ci.clone());
		for (AlternativeImportance ai : getImportanceAlternatives())
			c.getImportanceAlternatives().add(ai.clone());
		for (AlternativeValue av : getValuesAlternatives())
			c.getValuesAlternatives().add(av.clone());

		return c;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
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
		Criterion other = (Criterion) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}

	/**
	 * @return the metric
	 * @uml.property name="metric"
	 */
	public String getMetric() {
		return metric;
	}

	/**
	 * @param metric
	 *            the metric to set
	 * @uml.property name="metric"
	 */
	public void setMetric(String metric) {
		this.metric = metric;
	}

	/**
	 * @return the url
	 * @uml.property name="url"
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 * @uml.property name="url"
	 */
	public void setUrl(String url) {
		this.url = url;
	}

}
