package de.eorg.aotearoa.lib.tests;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.eorg.aotearoa.lib.jama.Matrix;
import de.eorg.aotearoa.lib.logic.ahp.AnalyticHierarchyProcess;
import de.eorg.aotearoa.lib.model.ahp.configuration.Alternative;
import de.eorg.aotearoa.lib.model.ahp.configuration.Criterion;
import de.eorg.aotearoa.lib.model.ahp.configuration.Decision;
import de.eorg.aotearoa.lib.model.ahp.configuration.Goal;
import de.eorg.aotearoa.lib.model.ahp.values.Evaluation;
import de.eorg.aotearoa.lib.model.ahp.values.EvaluationResult;

public class AnalyticHierachyProcessTest {

	
	public void testEvaluate(Matrix criteriaMatrix) {

		Decision decision = new Decision();
		Goal goal = new Goal();
		goal.setName("autokauf");
		Criterion c1 = new Criterion();
		c1.setName("styling");
		Criterion c2 = new Criterion();
		c2.setName("verlaesslichkeit");
		Criterion c3 = new Criterion();
		c3.setName("sparsamkeit");

		Alternative a1 = new Alternative("golf");
		Alternative a2 = new Alternative("206");
		Alternative a3 = new Alternative("saxo");
		Alternative a4 = new Alternative("clio");


		goal.addChild(c1);
		goal.addChild(c2);
		goal.addChild(c3);
		c1.setWeight(0.3196);
		c2.setWeight(0.5584);
		c3.setWeight(0.1220);
		decision.addGoal(goal);
		decision.addAlternative(a1);
		decision.addAlternative(a2);
		decision.addAlternative(a3);
		decision.addAlternative(a4);

		AnalyticHierarchyProcess ahp = new AnalyticHierarchyProcess(decision);
		Evaluation ev = new Evaluation();

		if (criteriaMatrix == null) {
			double[][] criteria = { { 1, 1D / 2D, 3 }, { 2, 1, 4 },
					{ 1D / 3D, 1D / 4D, 1 } };
			criteriaMatrix = new Matrix(criteria);
		}
		ahp.setChildrenCriteriaWeights(goal, criteriaMatrix, 15);

		double[][] crit1 = { { 1, 1D / 4D, 4D, 1D / 6D },
				{ 4D, 1, 4D, 1D / 4D }, { 1D / 4D, 1D / 4D, 1, 1D / 5D },
				{ 6D / 1D, 4D / 1D, 5D / 1D, 1 } };
		ev.getEvaluations().add(new Matrix(crit1));
		double[][] crit2 = { { 1, 2D, 5D, 1 }, { 1D / 2D, 1, 3D, 2D },
				{ 1D / 5D, 1D / 3D, 1, 1D / 4D }, { 1, 1D / 2D, 4D, 1 } };
		ev.getEvaluations().add(new Matrix(crit2));
		double[][] crit3 = { { 1, 34D / 27D, 34D / 24D, 34D / 28D },
				{ 27D / 34D, 1, 27D / 24D, 27D / 28D },
				{ 24D / 34D, 24D / 27D, 1, 24D / 28D },
				{ 28D / 34D, 28D / 27D, 28D / 24D, 1 } };
		ev.getEvaluations().add(new Matrix(crit3));

		// Test Map
		Map<Criterion, Double> testMap = new HashMap<Criterion, Double>();
		testMap.put(c1, 0.3196);
		testMap.put(c2, 0.5584);
		testMap.put(c3, 0.1220);

		try {
			System.out.println(((Goal) decision.getGoals().iterator().next())
					.getLeafCriteria());
			List<Evaluation> evals = new ArrayList<Evaluation>();
			evals.add(ev);
			EvaluationResult results = ahp.evaluateFull(evals, 15);
			System.out.println(results);
			//Assert.assertEquals(testMap, results);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
