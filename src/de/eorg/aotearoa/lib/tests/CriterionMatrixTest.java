package de.eorg.aotearoa.lib.tests;
import java.util.List;

import de.eorg.aotearoa.lib.model.ahp.configuration.Alternative;
import de.eorg.aotearoa.lib.model.ahp.configuration.Criterion;
import de.eorg.aotearoa.lib.model.ahp.configuration.Decision;
import de.eorg.aotearoa.lib.model.ahp.configuration.Goal;

public class CriterionMatrixTest {

	//@Test
	public void testAddMatrixWeights() {

		Decision decision = new Decision();
		Goal goal = new Goal();
		goal.setName("autokauf");
		Criterion c1 = new Criterion("styling");
		Criterion c2 = new Criterion("verlaesslichkeit");
		Criterion c3 = new Criterion("sparsamkeit");
		Criterion c11 = new Criterion("c11");
		Criterion c12 = new Criterion("c12");
		Criterion c31 = new Criterion("c31");
		c1.addChild(c11);
		c1.addChild(c12);
		c3.addChild(c31);
		Criterion c111 = new Criterion("c111");
		Criterion c112 = new Criterion("c112");
		Criterion c113 = new Criterion("c113");
		c11.addChild(c111);
		c11.addChild(c112);
		c11.addChild(c113);
		Criterion c121 = new Criterion("c121");
		Criterion c122 = new Criterion("c122");
		Criterion c123 = new Criterion("c123");
		c12.addChild(c121);
		c12.addChild(c122);
		c12.addChild(c123);
		Criterion c21 = new Criterion("c21");
		Criterion c22 = new Criterion("c22");
		c2.addChild(c21);
		c2.addChild(c22);

		Alternative a1 = new Alternative();
		a1.setName("golf");
		Alternative a2 = new Alternative();
		a2.setName("206");
		Alternative a3 = new Alternative();
		a3.setName("saxo");
		Alternative a4 = new Alternative();
		a4.setName("clio");

		goal.addChild(c1);
		goal.addChild(c2);
		goal.addChild(c3);
		// c1.setWeight(0.3196);
		// c2.setWeight(0.5584);
		// c3.setWeight(0.1220);
		decision.addGoal(goal);
		decision.addAlternative(a1);
		decision.addAlternative(a2);
		decision.addAlternative(a3);
		decision.addAlternative(a4);

		System.out.println("Baumebenen:");
		int i = 0;
		for (List<Criterion> list : goal.getCriteriaByLevels()) {
			System.out.println("level " + i);
			for (Criterion criterion : list) {
				System.out.println(criterion.getName());
			}
			i++;
		}

		/*
		 * WeightsMatrix criteriaMatrix = new WeightsMatrix();
		 * criteriaMatrix.getCriteriaList().add(c1);
		 * criteriaMatrix.getCriteriaList().add(c2);
		 * criteriaMatrix.getCriteriaList().add(c3);
		 * 
		 * //This values would be user input from sliders String[]
		 * criteriaValues = new String[3]; criteriaValues[0] = "-1";
		 * criteriaValues[1] = "2"; criteriaValues[2] = "3";
		 * 
		 * criteriaMatrix.addMatrixWeights(criteriaValues);
		 * 
		 * System.out.println("\n Criteria Matrix (by slider input): "); //
		 * criteriaMatrix.getMatrix().print(2, 2);
		 * System.out.println(criteriaMatrix.checkMatrix());
		 * //assertEquals("Matrix check negative!", "matrix ok", );
		 */

		//new AnalyticHierachyProcessTest().testEvaluate(criteriaMatrix.getMatrix());
	}

}
