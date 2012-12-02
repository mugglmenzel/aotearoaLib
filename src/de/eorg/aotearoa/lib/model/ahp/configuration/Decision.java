package de.eorg.aotearoa.lib.model.ahp.configuration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.eorg.aotearoa.lib.model.ahp.values.GoalImportance;

/**
 * @author mugglmenzel A Decision is the main class of the data model. It
 *         represents a decision itself and encapsulates multiple Goals that are
 *         pursued and multiple Alternatives that are potential solutions.
 * 
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 * 
 *         By Author: $Author: mugglmenzel $
 * 
 *         Revision: $Revision: 234 $
 * 
 *         Date: $Date: 2011-09-21 14:59:50 +0200 (Mi, 21 Sep 2011) $
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
 *         /aotearoa/shared/model/ahp/configuration/Decision.java $
 * 
 */

public class Decision<T extends Alternative> implements Serializable, Cloneable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 4411517735777225339L;

	private static Logger log = Logger.getLogger(Decision.class.getName());

	/**
	 * @uml.property name="userId"
	 */
	protected String userId;

	/**
	 * @uml.property name="name"
	 */
	protected String name;

	/**
	 * @uml.property name="description"
	 */
	protected String description;

	/**
	 * @uml.property name="alternatives"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse=
	 *                     "decision:de.eorg.cumulusgenius.shared.cloudmapping.model.ahp.configuration.Alternative"
	 */
	protected List<T> alternatives = new ArrayList<T>();

	/**
	 * @uml.property name="goals"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse=
	 *                     "decision:de.eorg.cumulusgenius.shared.cloudmapping.model.ahp.configuration.Goal"
	 */
	protected List<Goal> goals = new ArrayList<Goal>();

	/**
	 * @uml.property name="importanceGoals"
	 * @uml.associationEnd multiplicity="(0 -1)" inverse=
	 *                     "decision:de.eorg.cumulusgenius.shared.cloudmapping.model.ahp.values.GoalImportance"
	 */
	protected Map<GoalType, List<GoalImportance>> importanceGoals = new HashMap<GoalType, List<GoalImportance>>();

	public Decision() {
		super();
	}

	public Decision(String decisionName) {
		super();
		this.name = decisionName;
	}

	public Decision(String userId, String name, String description) {
		super();
		this.userId = userId;
		this.name = name;
		this.description = description;
	}

	public void addAlternative(T alternative) {
		getAlternatives().add(alternative);
	}

	public void addGoal(Goal goal) {
		getGoals().add(goal);
	}

	public int countGoals() {
		return getGoals().size();
	}

	public Alternative getAlternative(String alternativeName) {
		for (Alternative a : getAlternatives())
			if (a.getName().equals(alternativeName))
				return a;
		return null;
	}

	public List<T> getAlternatives() {
		return alternatives;
	}

	public Criterion getCriterion(String criterionName) {
		if (getGoal(criterionName) != null)
			return getGoal(criterionName);
		for (Goal goal : getGoals()) {
			Criterion a = getCriterion(goal, criterionName);
			if (a != null)
				return a;
		}
		return null;
	}

	public Criterion getCriterion(String goalName, String criterionName) {
		if (goalName.equals(criterionName) && getGoal(criterionName) != null)
			return getGoal(criterionName);
		return getCriterion(getGoal(goalName), criterionName);
	}

	public Criterion getCriterion(Criterion goal, String criterionName) {
		if (goal.getName().equals(criterionName))
			return goal;
		return goal.getCriterion(criterionName);
	}

	public Criterion getParentCriterion(String criterionName) {
		if (getGoal(criterionName) != null)
			return null;
		for (Goal goal : getGoals()) {
			Criterion a = getParentCriterion(goal, criterionName);
			if (a != null)
				return a;
		}
		return null;
	}

	public Criterion getParentCriterion(String goalName, String criterionName) {
		if (goalName.equals(criterionName))
			return null;
		return getParentCriterion(getGoal(goalName), criterionName);
	}

	public Criterion getParentCriterion(Criterion goal, String criterionName) {
		if (goal.getName().equals(criterionName))
			return null;
		return goal.getParentCriterion(criterionName);
	}

	public List<List<Criterion>> getCriteriaByLevels() {
		List<List<Criterion>> result = new ArrayList<List<Criterion>>();
		result.add(new ArrayList<Criterion>(getGoals()));
		for (Goal g : getGoals())
			result.addAll(g.getCriteriaByLevels());

		return result;
	}

	public List<Criterion> getLeafCriteria() {
		List<Criterion> result = new ArrayList<Criterion>();
		for (Goal g : getGoals())
			result.addAll(g.getLeafCriteria());

		return result;
	}

	public List<Criterion> getLeafCriteria(CriterionType type) {
		List<Criterion> result = new ArrayList<Criterion>();
		for (Goal g : getGoals())
			result.addAll(g.getLeafCriteria(type));

		return result;
	}

	/**
	 * @return
	 * @uml.property name="description"
	 */
	public String getDescription() {
		return description;
	}

	public Goal getGoal(String name) {
		Iterator<Goal> iti = getGoals().iterator();
		Goal help = null;
		while (iti.hasNext()) {
			help = iti.next();
			if (help.getName().equals(name))
				return help;
		}
		return null;
	}

	public String deleteGoal(String goalName) {
		Iterator<Goal> iti = this.getGoals().iterator();
		while (iti.hasNext()) {
			if (goalName.equals(iti.next().getName())) {
				iti.remove();
				return "removed";
			}
		}
		return null;
	}

	public List<Goal> getGoals() {
		return goals;
	}

	public List<Goal> getGoals(GoalType type) {
		List<Goal> result = new ArrayList<Goal>();
		Iterator<Goal> git = getGoals().iterator();
		while (git.hasNext()) {
			Goal next = git.next();
			if (type.equals(next.getGoalType()))
				result.add(next);
		}
		return result;
	}

	/**
	 * @return
	 * @uml.property name="name"
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the userId
	 * @uml.property name="userId"
	 */
	public String getUserId() {
		return userId;
	}

	public void setAlternatives(List<T> alternatives) {
		this.alternatives = alternatives;
	}

	/**
	 * @param description
	 * @uml.property name="description"
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}

	/**
	 * @param name
	 * @uml.property name="name"
	 */
	public void setName(String name) {
		this.name = name;
	};

	/**
	 * @param userId
	 *            the userId to set
	 * @uml.property name="userId"
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName() + ", " + getAlternatives() + ", " + getGoals() + ", "
				+ getImportanceGoalsMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	private Map<GoalType, List<GoalImportance>> getImportanceGoalsMap() {
		return this.importanceGoals;
	}

	public Decision<T> clone() {
		Decision<T> dec = new Decision<T>(getName());
		dec.setDescription(getDescription());
		dec.setUserId(getUserId());

		for (T a : getAlternatives())
			dec.addAlternative((T) a.clone());
		for (Goal g : getGoals())
			dec.addGoal(g.clone());

		return dec;
	}

	/**
	 * @return the importanceGoals
	 */
	public List<GoalImportance> getImportanceGoals(GoalType gt) {
		if (importanceGoals.get(gt) == null)
			importanceGoals.put(gt, new ArrayList<GoalImportance>());
		return importanceGoals.get(gt);
	}

	/**
	 * @param importanceGoals
	 *            the importanceGoals to set
	 */
	public void setImportanceGoals(GoalType gt,
			List<GoalImportance> importanceGoals) {
		this.importanceGoals.put(gt, importanceGoals);
	}

	public GoalImportance getImportanceGoal(GoalType gt, int i, int j) {
		log.fine("goal importance set: " + getImportanceGoals(gt));
		GoalImportance test = new GoalImportance(i, j, 1D, null);
		test.setDecision(this);
		if (!getImportanceGoals(gt).contains(test))
			return null;
		for (GoalImportance gi : getImportanceGoals(gt))
			if (test.equals(gi))
				return gi;
		return null;
	}

	public void insertImportanceGoal(GoalType gt, GoalImportance gi) {
		if (getImportanceGoals(gt).contains(gi)) {
			for (GoalImportance goali : getImportanceGoals(gt))
				if (goali.equals(gi)) {
					goali.setComparisonAToB(gi.getComparisonAToB());
					goali.setComment(gi.getComment());
				}

		} else
			getImportanceGoals(gt).add(gi);
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
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		if (!(obj instanceof Decision))
			return false;
		Decision other = (Decision) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}

}
