package de.eorg.aotearoa.lib.logic.ahp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import de.eorg.aotearoa.lib.jama.Matrix;
import de.eorg.aotearoa.lib.model.ahp.configuration.Alternative;
import de.eorg.aotearoa.lib.model.ahp.configuration.Criterion;
import de.eorg.aotearoa.lib.model.ahp.configuration.CriterionType;
import de.eorg.aotearoa.lib.model.ahp.configuration.Decision;
import de.eorg.aotearoa.lib.model.ahp.configuration.Goal;
import de.eorg.aotearoa.lib.model.ahp.configuration.GoalType;
import de.eorg.aotearoa.lib.model.ahp.values.AlternativeEvaluation;
import de.eorg.aotearoa.lib.model.ahp.values.AlternativeValuesMatrix;
import de.eorg.aotearoa.lib.model.ahp.values.AlternativeWeightsMatrix;
import de.eorg.aotearoa.lib.model.ahp.values.CriterionWeightsMatrix;
import de.eorg.aotearoa.lib.model.ahp.values.Evaluation;
import de.eorg.aotearoa.lib.model.ahp.values.EvaluationResult;
import de.eorg.aotearoa.lib.model.ahp.values.GoalWeightsMatrix;

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
 *         /aotearoa/server/logic/ahp/AnalyticHierarchyProcess.java $
 * 
 */

public class AnalyticHierarchyProcess {

	private class EigenVectorCallable implements Callable<EigenvectorResult> {

		private Matrix m;
		private int precision;
		private Goal g;
		private boolean saaty;

		public EigenVectorCallable(Matrix m, int precision, Goal g,
				boolean saaty) {
			this.m = m;
			this.precision = precision;
			this.g = g;
			this.saaty = saaty;
		}

		@Override
		public EigenvectorResult call() throws Exception {

			if (!saaty) {
				// Variablen zur Berechnung des Eigenvektors
				// int columnDimension = m.getColumnDimension();
				int rowDimension = m.getRowDimension();
				double reihenGesamtSumme = 0.0;
				double[] eigenVektorM = new double[rowDimension];
				Vector<Double> normalisierterEigenVektor = new Vector<Double>(
						rowDimension, 1);

				ExecutorService exec = Executors.newFixedThreadPool(16);
				List<Future<ReihenSummeResult>> futures = new LinkedList<Future<ReihenSummeResult>>();
				for (int i = 0; i < m.getRowDimension(); i++) {
					futures.add(exec.submit(new ReihenSummeCallable(i, m
							.getArray()[i])));
				}
				exec.shutdown();

				for (Future<ReihenSummeResult> f : futures) {
					ReihenSummeResult rsr = f.get();
					reihenGesamtSumme += rsr.getValue();
					eigenVektorM[rsr.getI()] = rsr.getValue();
				}

				// normalisiert den Eigenvektor
				for (int i = 0; i < eigenVektorM.length; i++) {
					double value = eigenVektorM[i] / reihenGesamtSumme;
					normalisierterEigenVektor.add(value);
				}
				return new EigenvectorResult(g, normalisierterEigenVektor);
			} else
				return new EigenvectorResult(g, calculateEigenvectorSaaty(m,
						precision));
		}
	}

	private class ReihenSummeCallable implements Callable<ReihenSummeResult> {

		int i;
		double[] row;

		/**
		 * @param row
		 */
		public ReihenSummeCallable(int i, double[] row) {
			super();
			this.i = i;
			this.row = row;
		}

		@Override
		public ReihenSummeResult call() throws Exception {
			// berechnet den Eigenvektor
			double reihenSumme = 0D;
			for (int j = 0; j < row.length; j++) {
				reihenSumme += row[j];
			}

			return new ReihenSummeResult(i, reihenSumme);
		}

	}

	private class ReihenSummeResult {

		private int i;
		private Double value;

		/**
		 * @param i
		 * @param value
		 */
		public ReihenSummeResult(int i, Double value) {
			super();
			this.i = i;
			this.value = value;
		}

		/**
		 * @return the i
		 */
		public int getI() {
			return i;
		}

		/**
		 * @return the value
		 */
		public Double getValue() {
			return value;
		}

	}

	private class EigenvectorResult {

		private Goal goal;
		private Vector<Double> eigenvector;

		/**
		 * @param g
		 * @param v
		 */
		public EigenvectorResult(Goal g, Vector<Double> v) {
			super();
			this.goal = g;
			this.eigenvector = v;
		}

		/**
		 * @return the goal
		 */
		public Goal getGoal() {
			return goal;
		}

		/**
		 * @return the eigenvector
		 */
		public Vector<Double> getEigenvector() {
			return eigenvector;
		}

		/**
		 * @param goal
		 *            the goal to set
		 */
		public void setGoal(Goal goal) {
			this.goal = goal;
		}

		/**
		 * @param eigenvector
		 *            the eigenvector to set
		 */
		public void setEigenvector(Vector<Double> eigenvector) {
			this.eigenvector = eigenvector;
		}

	}

	/**
	 * @uml.property name="dEFAULT_PRECISION"
	 */
	private final int DEFAULT_PRECISION = 5;

	/**
	 * @uml.property name="decision"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
	private Decision<?> decision;

	private Map<Goal, List<Vector<Double>>> alternativeEigenvectors = new HashMap<Goal, List<Vector<Double>>>();

	/**
	 * @uml.property name="alternativeEvaluations"
	 */
	private Map<Goal, Set<AlternativeEvaluation>> alternativeEvaluations = new HashMap<Goal, Set<AlternativeEvaluation>>();

	/**
	 * @uml.property name="log"
	 */
	private Logger log = Logger.getLogger(AnalyticHierarchyProcess.class
			.getName());

	/**
	 * @return the alternativeEvaluations
	 */

	private synchronized Map<Goal, Set<AlternativeEvaluation>> getAlternativeEvaluations() {
		return alternativeEvaluations;
	}

	public AnalyticHierarchyProcess(Decision<?> decision) {
		super();
		this.decision = decision;
		Map<Goal, Set<AlternativeEvaluation>> altEvals = getAlternativeEvaluations();
		for (Goal g : decision.getGoals())
			synchronized (altEvals) {
				altEvals.put(g, new HashSet<AlternativeEvaluation>());
			}
		log.info("created new AHP with decision: " + decision);
	}

	public void calculateWeights() {
		calculateWeights(DEFAULT_PRECISION, true);
	}

	public void calculateWeights(boolean saaty) {
		calculateWeights(DEFAULT_PRECISION, saaty);
	}

	public void calculateWeights(int precision) {
		calculateWeights(precision, true);
	}

	public void calculateWeights(int precision, boolean saaty) {

		for (GoalType gt : GoalType.values()) {
			// GoalWeights
			GoalWeightsMatrix critM = new GoalWeightsMatrix(decision.getGoals(
					gt).size(), decision.getImportanceGoals(gt));
			Vector<Double> goalsWeights = saaty ? calculateEigenvectorSaaty(
					critM.getMatrix(), precision) : calculateEigenvector(
					critM.getMatrix(), precision);
			if (goalsWeights.size() < decision.getGoals(gt).size())
				for (int i = goalsWeights.size() - 1; i < decision.getGoals()
						.size(); i++)
					goalsWeights.add(0D);
			for (int i = 0; i < decision.getGoals(gt).size(); i++)
				decision.getGoals(gt).get(i).setWeight(goalsWeights.get(i));
			log.info("goals weights: " + goalsWeights);
		}

		for (Goal goal : decision.getGoals()) {

			log.info("weighting criteria of goal " + goal.getName());

			// ChildrenWeights
			setChildrenCriteriaWeights(goal);
			for (Criterion c : goal.getAllDescendants())
				setChildrenCriteriaWeights(c);
		}

	}

	public void calculateAlternativeValues(List<Evaluation> evals)
			throws Exception {
		calculateAlternativeValues(evals, DEFAULT_PRECISION, true);
	}

	public void calculateAlternativeValues(List<Evaluation> evals, boolean saaty)
			throws Exception {
		calculateAlternativeValues(evals, DEFAULT_PRECISION, saaty);
	}

	public void calculateAlternativeValues(List<Evaluation> evals, int precision)
			throws Exception {
		calculateAlternativeValues(evals, precision, true);
	}

	public void calculateAlternativeValues(List<Evaluation> evals,
			int precision, boolean saaty) throws Exception {
		if (!sanityCheck(evals))
			throw new Exception("Given decision model not complete.");

		getAlternativeEigenvectors().clear();

		ExecutorService exec = Executors.newFixedThreadPool(16);
		List<Callable<EigenvectorResult>> threads = new LinkedList<Callable<EigenvectorResult>>();

		int g = 0;
		for (Goal goal : decision.getGoals()) {

			log.info("calculating alternative values for goal "
					+ goal.getName());

			List<Criterion> leafCriteria = goal.getLeafCriteria();

			getAlternativeEigenvectors().put(goal,
					new ArrayList<Vector<Double>>());

			Evaluation evaluation = evals != null && evals.size() > g ? evals
					.get(g) : null;
			log.info("picked evaluation: " + evaluation);

			if (evaluation == null) {
				evaluation = new Evaluation();
				for (Criterion leaf : leafCriteria) {
					if (CriterionType.QUALITATIVE.equals(leaf.getType())) {
						AlternativeWeightsMatrix m = new AlternativeWeightsMatrix(
								decision.getAlternatives().size(),
								leaf.getImportanceAlternatives(), leaf);
						evaluation.getEvaluations().add(m.getMatrix());
						log.info("created matrix for " + leaf.getName());
						log.info(m.getMatrix().toString());
					} else {
						AlternativeValuesMatrix m = new AlternativeValuesMatrix(
								decision.getAlternatives().size(),
								leaf.getValuesAlternatives(), leaf);
						evaluation.getEvaluations().add(m.getMatrix());
						log.info("created matrix for " + leaf.getName());
						log.info(m.getMatrix().toString());
					}

				}
			} else {
				// TODO: check if evaluation is correct/complete - sanity
				log.info("no health check for manual evaluations, yet");
			}

			for (Matrix m : evaluation.getEvaluations()) {
				// System.out.print("Matrix to Eigen for "
				// + getDecision().getAlternatives().size()
				// + " alternatives, criterion " + leafCriteria.get(i) + ":");
				// m.print(2, 2);
				if (m != null) {
					threads.add(new EigenVectorCallable(m, precision, goal,
							saaty));
				}
			}
			g++;
		}
		Collections.reverse(threads);
		List<Future<EigenvectorResult>> futures = exec.invokeAll(threads);
		exec.shutdown();

		for (Future<EigenvectorResult> f : futures) {
			EigenvectorResult er = f.get();
			getAlternativeEigenvectors().get(er.getGoal()).add(
					er.getEigenvector());
		}
		log.info("eigenvectors: " + getAlternativeEigenvectors());
	}

	public void evaluate() throws Exception {
		this.evaluate(-1);
	}

	public void evaluateSingle(int alternativePosition) throws Exception {
		this.evaluate(alternativePosition);
	}

	// TODO: neuen Exceptiontyp einf??hren!
	public void evaluate(int alternativePos) throws Exception {

		log.info("---------- new evaluation ----------");

		for (Goal goal : decision.getGoals()) {

			log.info("processing goal " + goal.getName());

			List<Criterion> leafCriteria = goal.getLeafCriteria();

			int i = 0;
			for (Vector<Double> v : getAlternativeEigenvectors().get(goal)) {
				// System.out.print("Matrix to Eigen for "
				// + getDecision().getAlternatives().size()
				// + " alternatives, criterion " + leafCriteria.get(i) + ":");
				// m.print(2, 2);
				//
				if (v != null
						&& v.size() == getDecision().getAlternatives().size()
						&& i < leafCriteria.size()) {

					if (alternativePos > -1) {
						Set<AlternativeEvaluation> altEvals = getAlternativeEvaluations()
								.get(goal);
						synchronized (altEvals) {
							altEvals.add(createAlternativeEvaluation(
									getDecision().getAlternatives().get(
											alternativePos), alternativePos,
									leafCriteria.get(i), v));
						}
					} else
						for (int j = 0; j < getDecision().getAlternatives()
								.size(); j++) {
							Set<AlternativeEvaluation> altEvals = getAlternativeEvaluations()
									.get(goal);
							synchronized (altEvals) {
								System.out.println("adding eval criterion " + i
										+ ", alternative " + j + " with value " + v);
								altEvals.add(createAlternativeEvaluation(
										getDecision().getAlternatives().get(j),
										j, leafCriteria.get(i), v));

							}
						}
				}
				i++;
			}

		}

		log.info("---------- end evaluation ----------");
	}

	public EvaluationResult calculateIndices() {

		Map<Alternative, Double> alternativeMultiplicativeMap = new HashMap<Alternative, Double>();
		Map<Alternative, Double> alternativeAdditiveMap = new HashMap<Alternative, Double>();
		Map<Alternative, Double> alternativePositiveMap = new HashMap<Alternative, Double>();
		Map<Alternative, Double> alternativeNegativeMap = new HashMap<Alternative, Double>();

		int g = 0;
		for (Goal goal : decision.getGoals()) {

			log.info("evaluations for goal " + g + ": "
					+ getAlternativeEvaluations().get(goal));

			Map<Alternative, Double> alternativeGoalMap = new HashMap<Alternative, Double>();

			for (AlternativeEvaluation ce : getAlternativeEvaluations().get(
					goal)) {
				alternativeGoalMap
						.put(ce.getAlternative(),
								new Double(
										(alternativeGoalMap.get(ce
												.getAlternative()) != null ? alternativeGoalMap
												.get(ce.getAlternative())
												.doubleValue() : 0D)
												+ (ce.getValue() * ce
														.getCriterion()
														.getGlobalWeight())));
			}
			log.info("goalMap: " + alternativeGoalMap);
			for (Alternative a : alternativeGoalMap.keySet()) {
				log.info("Aggregating for Alternative " + a);
				if (goal.getGoalType().equals(GoalType.POSITIVE))
					alternativePositiveMap
							.put(a,
									(alternativePositiveMap.get(a) != null ? alternativePositiveMap
											.get(a).doubleValue() : 0D)
											+ alternativeGoalMap.get(a));
				else
					alternativeNegativeMap
							.put(a,
									(alternativeNegativeMap.get(a) != null ? alternativeNegativeMap
											.get(a).doubleValue() : 0D)
											+ alternativeGoalMap.get(a));
			}
			g++;
		}

		log.info("Positive Map " + alternativePositiveMap);
		log.info("Negative Map " + alternativeNegativeMap);

		for (Alternative a : decision.getAlternatives()) {
			log.info("Generating Indices for Alternative " + a);
			alternativeMultiplicativeMap
					.put(a,
							(alternativePositiveMap.containsKey(a) ? alternativePositiveMap
									.get(a) : 1D)
									/ (alternativeNegativeMap.containsKey(a) ? alternativeNegativeMap
											.get(a) : 1D));

			alternativeAdditiveMap
					.put(a,
							(alternativePositiveMap.containsKey(a) ? alternativePositiveMap
									.get(a) : 0D)
									- (alternativeNegativeMap.containsKey(a) ? alternativeNegativeMap
											.get(a) : 0D));
		}

		return new EvaluationResult(decision, alternativeMultiplicativeMap,
				alternativeAdditiveMap, alternativePositiveMap,
				alternativeNegativeMap);
	}

	public EvaluationResult evaluateFull(List<Evaluation> evaluations)
			throws Exception {
		return this.evaluateFull(evaluations, DEFAULT_PRECISION);
	}

	public EvaluationResult evaluateFull(List<Evaluation> evaluations,
			boolean saaty) throws Exception {
		return this.evaluateFull(evaluations, DEFAULT_PRECISION, saaty);
	}

	public EvaluationResult evaluateFull(List<Evaluation> evaluations,
			int precision) throws Exception {
		return this.evaluateFull(evaluations, precision, true);
	}

	public EvaluationResult evaluateFull(List<Evaluation> evaluations,
			int precision, boolean saaty) throws Exception {
		calculateWeights(precision, saaty);
		calculateAlternativeValues(evaluations, precision, saaty);
		evaluate();
		return calculateIndices();
	}

	private AlternativeEvaluation createAlternativeEvaluation(
			Alternative alternative, int pos, Criterion crit,
			Vector<Double> eigenVector) {
		return new AlternativeEvaluation(crit, alternative,
				eigenVector.get(pos));
	}

	/**
	 * @param alternativeEvaluations
	 *            the alternativeEvaluations to set
	 */
	private void setAlternativeEvaluations(
			Map<Goal, Set<AlternativeEvaluation>> alternativeEvaluations) {
		this.alternativeEvaluations = alternativeEvaluations;
	}

	private boolean sanityCheck(List<Evaluation> evals) {
		if (evals == null || !(evals.size() > 0))
			return true;
		for (Evaluation eval : evals)
			if (!sanityCheck(eval))
				return false;
		return true;
	}

	// TODO: Sanity-Check implementieren
	private boolean sanityCheck(Evaluation eval) {
		boolean result = getDecision() != null;
		result &= getDecision().getAlternatives().size() > 0;
		result &= getDecision().getGoals().size() > 0;
		/*
		 * result &= eval.getEvaluations().size() ==
		 * getDecision().getGoals().get(0).getLeafCriteria().size(); for (Matrix
		 * m : eval.getEvaluations()) { result &= m.getColumnDimension() ==
		 * m.getRowDimension(); result &= m.getColumnDimension() ==
		 * getDecision().getAlternatives() .size(); }
		 */
		return result;
	}

	public void setChildrenCriteriaWeights(Criterion parent) {
		setChildrenCriteriaWeights(parent, DEFAULT_PRECISION);
	}

	public void setChildrenCriteriaWeights(Criterion parent, int precision) {
		if (parent.hasChildren()) {
			CriterionWeightsMatrix critM = new CriterionWeightsMatrix(parent
					.getChildren().size(), parent.getImportanceChildren());

			setChildrenCriteriaWeights(parent, critM.getMatrix(), precision);
		}
	}

	public void setChildrenCriteriaWeights(Criterion parent, Matrix m) {
		this.setChildrenCriteriaWeights(parent, m, DEFAULT_PRECISION);
	}

	public void setChildrenCriteriaWeights(Criterion parent, Matrix m,
			int precision) {
		Vector<Double> criteriaWeights = calculateEigenvector(m, precision);

		if (criteriaWeights.size() > 0
				&& criteriaWeights.size() >= parent.getChildren().size()) {
			Iterator<Criterion> itiLeaf = parent.getChildren().iterator();
			int i = 0;
			while (itiLeaf.hasNext()) {
				Criterion leafCriterion = itiLeaf.next();
				leafCriterion.setWeight(criteriaWeights.get(i));
				log.info(leafCriterion.getName() + ": local weight = "
						+ leafCriterion.getWeight() + ", global weight = "
						+ leafCriterion.getGlobalWeight());
				i++;
			}
		}
	}

	public void setGoalWeights(Matrix m) {
		this.setGoalWeights(m, DEFAULT_PRECISION);
	}

	public void setGoalWeights(Matrix m, int precision) {
		Vector<Double> goalWeights = calculateEigenvector(m, precision);

		if (goalWeights.size() > 0
				&& goalWeights.size() >= getDecision().getGoals().size()) {
			Iterator<Goal> itiGoal = getDecision().getGoals().iterator();
			int i = 0;
			while (itiGoal.hasNext()) {
				Goal goal = itiGoal.next();
				goal.setWeight(goalWeights.get(i));
				log.info(goal.getName() + ": local weight = "
						+ goal.getWeight() + ", global weight = "
						+ goal.getGlobalWeight());
				i++;
			}
		}
	}

	private Vector<Double> calculateEigenvector(Matrix m, int precision) {

		// System.out.print("m: " );
		// m.print(3, 2);
		Matrix normalisierterEigenVektor = null;

		// Variablen zur Berechnung des Eigenvektors
		int columnDimension = m.getColumnDimension();
		int rowDimension = m.getRowDimension();
		double reihenGesamtSumme = 0.0;
		Matrix eigenVektorM = new Matrix(rowDimension, 1);
		normalisierterEigenVektor = new Matrix(rowDimension, 1);

		// berechnet den Eigenvektor
		for (int i = 0; i < rowDimension; i++) {
			double reihenSumme = 0.0;
			for (int j = 0; j < columnDimension; j++) {
				reihenSumme += m.get(i, j);
			}
			eigenVektorM.set(i, 0, reihenSumme);
			reihenGesamtSumme += reihenSumme;
		}

		// normalisiert den Eigenvektor
		for (int i = 0; i < eigenVektorM.getRowDimension(); i++) {
			double value = eigenVektorM.get(i, 0) / reihenGesamtSumme;
			normalisierterEigenVektor.set(i, 0, value);
		}

		/*
		 * // Variablen zur sp??teren Berechnung des normalisierten Eigenvektors
		 * Matrix normalisierterEigenVektorAlt = new Matrix(m.getRowDimension(),
		 * 1); for (int i = 0; i < m.getRowDimension(); i++) {
		 * normalisierterEigenVektorAlt.set(i, 0, 0.0); } Matrix
		 * normalisierterEigenVektor = null;
		 * 
		 * // die Matrix wird so oft quadriert bis nur noch eine geringe //
		 * Abweichungen // beim Eigenvektor auftreten (maximal 100 mal) for (int
		 * a = 0; a < 100; a++) {
		 * 
		 * // Quadratur der Matrix m = m.times(m); // System.out.print("m??: "
		 * ); // m.print(3, 2);
		 * 
		 * // Variablen zur Berechnung des Eigenvektors int columnDimension =
		 * m.getColumnDimension(); int rowDimension = m.getRowDimension();
		 * double reihenGesamtSumme = 0.0; Matrix eigenVektor = new
		 * Matrix(rowDimension, 1); normalisierterEigenVektor = new
		 * Matrix(rowDimension, 1);
		 * 
		 * // berechnet den Eigenvektor for (int i = 0; i < rowDimension; i++) {
		 * double reihenSumme = 0.0; for (int j = 0; j < columnDimension; j++) {
		 * reihenSumme += m.get(i, j); } eigenVektor.set(i, 0, reihenSumme);
		 * reihenGesamtSumme += reihenSumme; }
		 * 
		 * // normalisiert den Eigenvektor for (int i = 0; i <
		 * eigenVektor.getRowDimension(); i++) { double value =
		 * eigenVektor.get(i, 0) / reihenGesamtSumme;
		 * normalisierterEigenVektor.set(i, 0, value); } //
		 * System.out.print("eigenvektor: "); // eigenVektor.print(3, 2); //
		 * System.out.print(", normalisiert: "); //
		 * normalisierterEigenVektor.print(3, 2);
		 * 
		 * // Differenz zwischen neuen und altem Eigenvektor Matrix
		 * eigenVektorDifferenz = normalisierterEigenVektor
		 * .minus(normalisierterEigenVektorAlt);
		 * 
		 * // falls ein Wert der Eigenvektordifferenz kleiner als 0.0001 ist //
		 * wird die Schleife beendet boolean isFinished = true; for (int i = 0;
		 * i < rowDimension; i++) { if (Math.abs(eigenVektorDifferenz.get(i, 0))
		 * >= Math.pow(10, -1 precision)) { isFinished = false; break; } } if
		 * (isFinished) { break; }
		 * 
		 * normalisierterEigenVektorAlt = normalisierterEigenVektor; }
		 */
		Vector<Double> eigenVektor = new Vector<Double>();
		for (int i = 0; i < normalisierterEigenVektor.getRowDimension(); i++) {
			eigenVektor.add(normalisierterEigenVektor.get(i, 0));
		}

		return eigenVektor;

	}

	private Double calculateEigenvector(double[] row, int precision) {

		// berechnet den Eigenvektor
		double reihenSumme = 0.0;
		for (int j = 0; j < row.length; j++) {
			reihenSumme += row[j];
		}

		return reihenSumme;

	}

	private Vector<Double> calculateEigenvectorSaaty(Matrix m, int precision) {

		// Variablen zur sp?????teren Berechnung des normalisierten Eigenvektors
		Matrix normalisierterEigenVektorAlt = new Matrix(m.getRowDimension(), 1);
		for (int i = 0; i < m.getRowDimension(); i++) {
			normalisierterEigenVektorAlt.set(i, 0, 0.0);
		}
		Matrix normalisierterEigenVektor = null;

		// die Matrix wird so oft quadriert bis nur noch eine geringe
		// Abweichungen
		// beim Eigenvektor auftreten (maximal 1000 mal)
		for (int a = 0; a < 1000; a++) {

			// Quadratur der Matrix
			m = m.times(m);
			// System.out.print("m?????: " );
			// m.print(3, 2);

			// Variablen zur Berechnung des Eigenvektors
			int columnDimension = m.getColumnDimension();
			int rowDimension = m.getRowDimension();
			double reihenGesamtSumme = 0.0;
			Matrix eigenVektor = new Matrix(rowDimension, 1);
			normalisierterEigenVektor = new Matrix(rowDimension, 1);

			// berechnet den Eigenvektor
			for (int i = 0; i < rowDimension; i++) {
				double reihenSumme = 0.0;
				for (int j = 0; j < columnDimension; j++) {
					reihenSumme += m.get(i, j);
				}
				eigenVektor.set(i, 0, reihenSumme);
				reihenGesamtSumme += reihenSumme;
			}

			// normalisiert den Eigenvektor
			for (int i = 0; i < eigenVektor.getRowDimension(); i++) {
				double value = eigenVektor.get(i, 0) / reihenGesamtSumme;
				normalisierterEigenVektor.set(i, 0, value);
			}
			// System.out.print("eigenvektor: ");
			// eigenVektor.print(3, 2);
			// System.out.print(", normalisiert: ");
			// normalisierterEigenVektor.print(3, 2);

			// Differenz zwischen neuen und altem Eigenvektor
			Matrix eigenVektorDifferenz = normalisierterEigenVektor
					.minus(normalisierterEigenVektorAlt);

			// falls ein Wert der Eigenvektordifferenz kleiner als 0.0001 ist
			// wird die Schleife beendet
			boolean isFinished = true;
			for (int i = 0; i < rowDimension; i++) {
				if (Math.abs(eigenVektorDifferenz.get(i, 0)) >= Math.pow(10, -1
						* precision)) {
					isFinished = false;
				}
			}
			if (isFinished) {
				break;
			}

			normalisierterEigenVektorAlt = normalisierterEigenVektor;
		}

		Vector<Double> eigenVektor = new Vector<Double>();
		for (int i = 0; i < normalisierterEigenVektor.getRowDimension(); i++) {
			eigenVektor.add(normalisierterEigenVektor.get(i, 0));
		}

		return eigenVektor;

	}

	/**
	 * @return
	 * @uml.property name="decision"
	 */
	public Decision<?> getDecision() {
		return decision;
	}

	/**
	 * @param decision
	 * @uml.property name="decision"
	 */
	public void setDecision(Decision<?> decision) {
		this.decision = decision;
	}

	/**
	 * @return the alternativeEigenvectors
	 */
	private Map<Goal, List<Vector<Double>>> getAlternativeEigenvectors() {
		return alternativeEigenvectors;
	}

	/**
	 * @param alternativeEigenvectors
	 *            the alternativeEigenvectors to set
	 */
	private void setAlternativeEigenvectors(
			Map<Goal, List<Vector<Double>>> alternativeEigenvectors) {
		this.alternativeEigenvectors = alternativeEigenvectors;
	}

	// TODO: wegen nicht m??glicher JAMA lib Nutzung erstmal auskommentiert
	// public void calculateLocalCriteriaWeight(CriteriaMatrix criteriaMatrix) {
	//
	// Vector<Double> normalizedEigenVector =
	// calculateEigenvector(criteriaMatrix.getMatrix());
	// List<Criterion> criteriaList = criteriaMatrix.getCriteriaList();
	// for (int i = 0; i < criteriaList.size(); i++) {
	// criteriaList.get(i).setLocalWeight(normalizedEigenVector.get(i));
	// }
	//
	// }

}
