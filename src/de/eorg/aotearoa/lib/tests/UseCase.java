package de.eorg.aotearoa.lib.tests;
/**
 * 
 */

/**
 * @author jonasprior
 *
 */

import java.util.ArrayList;
import java.util.List;

import de.eorg.aotearoa.lib.jama.Matrix;
import de.eorg.aotearoa.lib.logic.ahp.AnalyticHierarchyProcess;
import de.eorg.aotearoa.lib.model.ahp.configuration.Alternative;
import de.eorg.aotearoa.lib.model.ahp.configuration.Criterion;
import de.eorg.aotearoa.lib.model.ahp.configuration.Decision;
import de.eorg.aotearoa.lib.model.ahp.configuration.Goal;
import de.eorg.aotearoa.lib.model.ahp.values.Evaluation;
import de.eorg.aotearoa.lib.model.ahp.values.EvaluationResult;

public class UseCase {

	//@Test
	public void UseCaseTest() {
		Decision decision = new Decision();
		decision.setName("Cloud Computing Provider Selection");
		Goal goal = new Goal();
		goal.setName("Find the best Cloud Provider");
		goal.setWeight(1);
		Criterion c1 = new Criterion("Kosten");
		Criterion c2 = new Criterion("Usability");
		Criterion c3 = new Criterion("Reputation");
		Criterion c4 = new Criterion("Service Level Agreements");
		Criterion c5 = new Criterion("Architektur");

		// Kosten
		Criterion c11 = new Criterion("laufende Kosten");
		Criterion c12 = new Criterion("Abrechnungsmodell");
		Criterion c13 = new Criterion("Rabatte/Verguenstigungen");
		c1.addChild(c11);
		c1.addChild(c12);
		c1.addChild(c13);

		// Usability
		Criterion c21 = new Criterion("User Interface");
		Criterion c22 = new Criterion("Dokumentation");
		Criterion c23 = new Criterion("Online Support");
		c2.addChild(c21);
		c2.addChild(c22);
		c2.addChild(c23);

		// Reputation
		Criterion c31 = new Criterion("Gesamteindruck");
		Criterion c32 = new Criterion("Zertifizierung");
		c3.addChild(c31);
		c3.addChild(c32);

		// Service Level Agreements
		Criterion c41 = new Criterion("Verf???gbarkeit");
		Criterion c42 = new Criterion("Performanz");

		c4.addChild(c41);
		c4.addChild(c42);

		// Architektur
		Criterion c51 = new Criterion("Elastizit???t");
		Criterion c52 = new Criterion("Software Lock-In");
		Criterion c53 = new Criterion("Breite des Service Portfolios");
		c5.addChild(c51);
		c5.addChild(c52);
		c5.addChild(c53);

		// defining Alternatives
		Alternative a1 = new Alternative();
		a1.setName("Amazon Web Services");
		Alternative a2 = new Alternative();
		a2.setName("Rackspace UK");
		Alternative a3 = new Alternative();
		a3.setName("Terremark vCloud Express");

		goal.addChild(c1);
		goal.addChild(c2);
		goal.addChild(c3);
		goal.addChild(c4);
		goal.addChild(c5);

		decision.addGoal(goal);
		decision.addAlternative(a1);
		decision.addAlternative(a2);
		decision.addAlternative(a3);

		System.out.println("Baumebenen:");
		int i = 0;
		for (List<Criterion> list : goal.getCriteriaByLevels()) {
			System.out.println("level " + i);
			for (Criterion criterion : list) {
				System.out.println(criterion.getName());
			}
			i++;
		}
		AnalyticHierarchyProcess ahp = new AnalyticHierarchyProcess(decision);
		Evaluation ev = new Evaluation();

		System.out.println("\n Gewichtung der Kriterien");
		double[][] criteria = { { 1, 8, 2, 2, 2 },
				{ 1D / 8D, 1, 1D / 3D, 1D / 3D, 1D / 3D },
				{ 1D / 2D, 3, 1, 1, 2 }, { 1D / 2D, 3, 1, 1, 2 },
				{ 1D / 2D, 3, 1D / 2D, 1D / 2D, 1 } };
		Matrix cgoal = new Matrix(criteria);
		// cgoal.print(2,2);
		ahp.setChildrenCriteriaWeights(goal, cgoal, 15);

		System.out.println("\n Gewichtung der Kriterien Kosten");
		double[][] crit1 = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix ccrit1 = new Matrix(crit1);
		// ccrit1.print(2,2);
		ahp.setChildrenCriteriaWeights(c1, ccrit1, 15);

		System.out.println("\n Gewichtung der Kriterien Usability");
		double[][] crit2 = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix ccrit2 = new Matrix(crit2);
		// ccrit2.print(2,2);
		ahp.setChildrenCriteriaWeights(c2, ccrit2, 15);

		System.out.println("\n Gewichtung der Kriterien Reputation");
		double[][] crit3 = { { 1, 1 }, { 1, 1 } };
		Matrix ccrit3 = new Matrix(crit3);
		// ccrit3.print(2,2);
		ahp.setChildrenCriteriaWeights(c3, ccrit3, 15);

		System.out.println("\n Gewichtung der Kriterien SLA");
		double[][] crit4 = { { 1, 1 }, { 1, 1 } };
		Matrix ccrit4 = new Matrix(crit4);
		// ccrit4.print(2,2);
		ahp.setChildrenCriteriaWeights(c4, ccrit4, 15);

		System.out.println("\n Gewichtung der Kriterien Architektur");
		double[][] crit5 = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix ccrit5 = new Matrix(crit5);
		// ccrit5.print(2,2);
		ahp.setChildrenCriteriaWeights(c5, ccrit5, 15);

		System.out
				.println("Bewertung der Alternativen bez???glich der Kriterien");

		// Laufende Kosten
		double crit11[][] = { { 1, 2D / 3D, 4D / 5D }, { 3D / 2D, 1, 6D / 5D },
				{ 5D / 4D, 5D / 6D, 1 } };
		Matrix crit11M = new Matrix(crit11);
		ev.getEvaluations().add(crit11M);

		// Abrechnungsmodell
		double crit12[][] = { { 1, 6, 1D / 3D }, { 1D / 6D, 1, 1D / 8D },
				{ 3, 8, 1 } };
		Matrix crit12M = new Matrix(crit12);
		ev.getEvaluations().add(crit12M);
		// Rabatte
		double crit13[][] = { { 1, 8, 8 }, { 1D / 8D, 1, 1 }, { 1D / 8D, 1, 1 } };
		Matrix crit13M = new Matrix(crit13);
		ev.getEvaluations().add(crit13M);

		// User Interface
		double crit21[][] = { { 1, 4, 8 }, { 1D / 4D, 1, 1D / 2D },
				{ 1D / 8D, 1D / 2D, 1 } };
		Matrix crit21M = new Matrix(crit21);
		ev.getEvaluations().add(crit21M);

		// Dokumentation
		double crit22[][] = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix crit22M = new Matrix(crit22);
		ev.getEvaluations().add(crit22M);

		// Online Support
		double crit23[][] = { { 1, 3, 1 }, { 1D / 3D, 1, 1D / 3D }, { 1, 3, 1 } };
		Matrix crit23M = new Matrix(crit23);
		ev.getEvaluations().add(crit23M);

		// Gesamteindruck
		double crit31[][] = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix crit31M = new Matrix(crit31);
		ev.getEvaluations().add(crit31M);

		// Zertifikate
		double crit32[][] = { { 1, 1, 3 }, { 1, 1, 3 }, { 1D / 3D, 1 / 3D, 1 } };
		Matrix crit32M = new Matrix(crit32);
		ev.getEvaluations().add(crit32M);

		// Verfuegbarkeit
		double crit41[][] = { { 1, 1, 1 }, { 1, 1, 1 }, { 1, 1, 1 } };
		Matrix crit41M = new Matrix(crit41);
		ev.getEvaluations().add(crit41M);

		// Performanz
		double crit42[][] = { { 1, 1D / 4D, 1D / 7D }, { 4, 1, 1D / 3D },
				{ 7, 3, 1 } };
		Matrix crit42M = new Matrix(crit42);
		ev.getEvaluations().add(crit42M);

		// Skalierbarkeit
		double crit51[][] = { { 1, 1, 1D / 5D }, { 1, 1, 1D / 5D }, { 5, 5, 1 } };
		Matrix crit51M = new Matrix(crit51);
		ev.getEvaluations().add(crit51M);

		// Software Lock-In
		double crit52[][] = { { 1, 5, 5 }, { 1D / 5D, 1, 1 }, { 1D / 5D, 1, 1 } };
		Matrix crit52M = new Matrix(crit52);
		ev.getEvaluations().add(crit52M);

		// Breite des Service Portfolios
		double crit53[][] = { { 1, 6, 9 }, { 1D / 6D, 1, 4 },
				{ 1D / 9D, 1D / 4D, 1 } };
		Matrix crit53M = new Matrix(crit53);
		ev.getEvaluations().add(crit53M);

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
			List<Evaluation> evals = new ArrayList<Evaluation>();
			evals.add(ev);
			EvaluationResult results = ahp.evaluateFull(evals);
			System.out.println(results);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
