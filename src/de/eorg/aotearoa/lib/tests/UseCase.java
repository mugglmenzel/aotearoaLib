package de.eorg.aotearoa.lib.tests;

/**
 * @author mugglmenzel
 *
 */

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.eorg.aotearoa.lib.jama.Matrix;
import de.eorg.aotearoa.lib.logic.ahp.AnalyticHierarchyProcess;
import de.eorg.aotearoa.lib.model.ahp.configuration.Alternative;
import de.eorg.aotearoa.lib.model.ahp.configuration.Criterion;
import de.eorg.aotearoa.lib.model.ahp.configuration.Decision;
import de.eorg.aotearoa.lib.model.ahp.configuration.Goal;
import de.eorg.aotearoa.lib.model.ahp.configuration.GoalType;
import de.eorg.aotearoa.lib.model.ahp.values.Evaluation;
import de.eorg.aotearoa.lib.model.ahp.values.EvaluationResult;
import de.eorg.aotearoa.lib.model.ahp.values.GoalImportance;

public class UseCase {

	@Test
	public void UseCaseTest() {
		Decision<Alternative> decision = new Decision<Alternative>();
		decision.setName("Cloud Computing Provider Selection");

		Goal g1 = new Goal("Costs");
		g1.setGoalType(GoalType.NEGATIVE);
		Goal g2 = new Goal("Usability");
		Goal g3 = new Goal("Reputation");
		Goal g4 = new Goal("Service Level Agreements");
		Goal g5 = new Goal("Architecture");

		// Costs
		Criterion c11 = new Criterion("Monthly Costs");
		Criterion c12 = new Criterion("Pricing Model");
		Criterion c13 = new Criterion("Rebates/Discounts");
		g1.addChild(c11);
		g1.addChild(c12);
		g1.addChild(c13);

		// Usability
		Criterion c21 = new Criterion("User Interface");
		Criterion c22 = new Criterion("Documentation");
		Criterion c23 = new Criterion("Online Support");
		g2.addChild(c21);
		g2.addChild(c22);
		g2.addChild(c23);

		// Reputation
		Criterion c31 = new Criterion("Subjective Rating");
		Criterion c32 = new Criterion("Certification");
		g3.addChild(c31);
		g3.addChild(c32);

		// Service Level Agreements
		Criterion c41 = new Criterion("Availability");
		Criterion c42 = new Criterion("Performance");

		g4.addChild(c41);
		g4.addChild(c42);

		// Architecture
		Criterion c51 = new Criterion("Elasticity");
		Criterion c52 = new Criterion("Lock-In");
		Criterion c53 = new Criterion("Extensiveness of Service Portfolio");
		g5.addChild(c51);
		g5.addChild(c52);
		g5.addChild(c53);

		// defining Alternatives
		Alternative a1 = new Alternative();
		a1.setName("Amazon Web Services");
		Alternative a2 = new Alternative();
		a2.setName("Rackspace UK");
		Alternative a3 = new Alternative();
		a3.setName("Terremark vCloud Express");

		decision.addGoal(g1);
		decision.addGoal(g2);
		decision.addGoal(g3);
		decision.addGoal(g4);
		decision.addGoal(g5);

		decision.addAlternative(a1);
		decision.addAlternative(a2);
		decision.addAlternative(a3);

		System.out.println("Tree:");
		int i = 0;
		for (List<Criterion> list : (List<List<Criterion>>) decision
				.getCriteriaByLevels()) {
			System.out.println("level " + i);
			for (Criterion criterion : list) {
				System.out.println(criterion);
			}
			i++;
		}
		AnalyticHierarchyProcess ahp = new AnalyticHierarchyProcess(decision);

		System.out.println("\n Weights of Goals");
		decision.getImportanceGoals(GoalType.POSITIVE).add(
				new GoalImportance(0, 1, 3D, null));
		decision.getImportanceGoals(GoalType.POSITIVE).add(
				new GoalImportance(0, 2, 3D, null));
		decision.getImportanceGoals(GoalType.POSITIVE).add(
				new GoalImportance(0, 3, 3D, null));
		decision.getImportanceGoals(GoalType.POSITIVE).add(
				new GoalImportance(1, 2, 2D, null));
		decision.getImportanceGoals(GoalType.POSITIVE).add(
				new GoalImportance(1, 3, 2D, null));
		decision.getImportanceGoals(GoalType.POSITIVE).add(
				new GoalImportance(2, 3, 1D, null));

		System.out.println("\n Weights of Criteria Costs");
		double[][] crit1 = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix ccrit1 = new Matrix(crit1);
		// ccrit1.print(2,2);
		ahp.setChildrenCriteriaWeights(g1, ccrit1, 15);

		System.out.println("\n Weights of Criteria Usability");
		double[][] crit2 = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix ccrit2 = new Matrix(crit2);
		// ccrit2.print(2,2);
		ahp.setChildrenCriteriaWeights(g2, ccrit2, 15);

		System.out.println("\n Weights of Criteria Reputation");
		double[][] crit3 = { { 1, 1 }, { 1, 1 } };
		Matrix ccrit3 = new Matrix(crit3);
		// ccrit3.print(2,2);
		ahp.setChildrenCriteriaWeights(g3, ccrit3, 15);

		System.out.println("\n Weights of Criteria SLA");
		double[][] crit4 = { { 1, 1 }, { 1, 1 } };
		Matrix ccrit4 = new Matrix(crit4);
		// ccrit4.print(2,2);
		ahp.setChildrenCriteriaWeights(g4, ccrit4, 15);

		System.out.println("\n Weights of Criteria Architecture");
		double[][] crit5 = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix ccrit5 = new Matrix(crit5);
		// ccrit5.print(2,2);
		ahp.setChildrenCriteriaWeights(g5, ccrit5, 15);

		System.out.println("Assessment of Alternatives regarding Criteria");

		List<Evaluation> evals = new ArrayList<Evaluation>();

		// Costs Goal Evaluation
		Evaluation ev = new Evaluation();
		// Monthly Costs
		double crit11[][] = { { 1, 2D / 3D, 4D / 5D }, { 3D / 2D, 1, 6D / 5D },
				{ 5D / 4D, 5D / 6D, 1 } };
		Matrix crit11M = new Matrix(crit11);
		ev.getEvaluations().add(crit11M);

		// Pricing Model
		double crit12[][] = { { 1, 6, 1D / 3D }, { 1D / 6D, 1, 1D / 8D },
				{ 3, 8, 1 } };
		Matrix crit12M = new Matrix(crit12);
		ev.getEvaluations().add(crit12M);
		// Rebates
		double crit13[][] = { { 1, 8, 8 }, { 1D / 8D, 1, 1 }, { 1D / 8D, 1, 1 } };
		Matrix crit13M = new Matrix(crit13);
		ev.getEvaluations().add(crit13M);

		evals.add(ev);

		// Usability Goal Evaluation
		ev = new Evaluation();

		// User Interface
		double crit21[][] = { { 1, 4, 8 }, { 1D / 4D, 1, 1D / 2D },
				{ 1D / 8D, 1D / 2D, 1 } };
		Matrix crit21M = new Matrix(crit21);
		ev.getEvaluations().add(crit21M);

		// Documentation
		double crit22[][] = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix crit22M = new Matrix(crit22);
		ev.getEvaluations().add(crit22M);

		// Online Support
		double crit23[][] = { { 1, 3, 1 }, { 1D / 3D, 1, 1D / 3D }, { 1, 3, 1 } };
		Matrix crit23M = new Matrix(crit23);
		ev.getEvaluations().add(crit23M);

		evals.add(ev);

		// Reputation Goal Evaluation
		ev = new Evaluation();

		// Subjective Rating
		double crit31[][] = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix crit31M = new Matrix(crit31);
		ev.getEvaluations().add(crit31M);

		// Certificates
		double crit32[][] = { { 1, 1, 3 }, { 1, 1, 3 }, { 1D / 3D, 1 / 3D, 1 } };
		Matrix crit32M = new Matrix(crit32);
		ev.getEvaluations().add(crit32M);

		evals.add(ev);

		// SLA Goal Evaluation
		ev = new Evaluation();

		// Availability
		double crit41[][] = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix crit41M = new Matrix(crit41);
		ev.getEvaluations().add(crit41M);

		// Performance
		double crit42[][] = { { 1, 1D / 4D, 1D / 7D }, { 4, 1, 1D / 3D },
				{ 7, 3, 1 } };
		Matrix crit42M = new Matrix(crit42);
		ev.getEvaluations().add(crit42M);

		evals.add(ev);

		// Architecture Goal Evaluation
		ev = new Evaluation();

		// Elasticity
		double crit51[][] = { { 1, 1, 1D / 5D }, { 1, 1, 1D / 5D }, { 5, 5, 1 } };
		Matrix crit51M = new Matrix(crit51);
		ev.getEvaluations().add(crit51M);

		// Lock-In
		double crit52[][] = { { 1, 5, 5 }, { 1D / 5D, 1, 1 }, { 1D / 5D, 1, 1 } };
		Matrix crit52M = new Matrix(crit52);
		ev.getEvaluations().add(crit52M);

		// Extensiveness of Service Portfolio
		double crit53[][] = { { 1, 6, 9 }, { 1D / 6D, 1, 4 },
				{ 1D / 9D, 1D / 4D, 1 } };
		Matrix crit53M = new Matrix(crit53);
		ev.getEvaluations().add(crit53M);

		evals.add(ev);

		/*
		 * String[] criteriaValues = new String[3]; CriteriaMatrix cMc11 = new
		 * CriteriaMatrix(); cMc11.getCriteriaList().add(c1);
		 * cMc11.getCriteriaList().add(c2); cMc11.getCriteriaList().add(c3);
		 * 
		 * criteriaValues[0] = "-4"; // Amazon wird mit Rackspace verglichen
		 * criteriaValues[1] = "-3"; // Amazon wird mit Terremark verglichen
		 * criteriaValues[2] = "1"; // Rackspace wird mit Terremark verglichen
		 * cMc11.addMatrixWeights(criteriaValues); cMc11.getMatrix().print(2,
		 * 2); ev.getEvaluations().add(cMc11);
		 */

		try {
			System.out.println(((Goal) decision.getGoals().iterator().next())
					.getLeafCriteria());

			EvaluationResult results = ahp.evaluateFull(evals);
			System.out.println("Multiplicative: "
					+ results.getResultMultiplicativeIndexMap());
			System.out.println("Additive: "
					+ results.getResultAdditiveIndexMap());
			System.out.println("Positive: "
					+ results.getResultPositiveGoalsMap());
			System.out.println("Negative: "
					+ results.getResultNegativeGoalsMap());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
