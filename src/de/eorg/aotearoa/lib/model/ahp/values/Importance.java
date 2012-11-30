package de.eorg.aotearoa.lib.model.ahp.values;

import java.io.Serializable;

/**
 * @author  mugglmenzel  Author: Michael Menzel (mugglmenzel)  Last Change:  By Author: $Author: mugglmenzel@gmail.com $   Revision: $Revision: 221 $   Date: $Date: 2011-09-19 10:55:30 +0200 (Mo, 19 Sep 2011) $  License:  Copyright 2011 Forschungszentrum Informatik FZI / Karlsruhe Institute  of Technology  Licensed under the Apache License, Version 2.0 (the "License"); you  may not use this file except in compliance with the License. You may  obtain a copy of the License at  http://www.apache.org/licenses/LICENSE-2.0  Unless required by applicable law or agreed to in writing, software  distributed under the License is distributed on an "AS IS" BASIS,  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or  implied. See the License for the specific language governing  permissions and limitations under the License.  SVN URL:   $HeadURL: https://aotearoadecisions.googlecode.com/svn/trunk/src/main/java/de/fzi/aotearoa/shared/model/ahp/values/Importance.java $
 */

public interface Importance extends Serializable {

	/**
	 * @return  the comparisonAToB
	 * @uml.property  name="comparisonAToB"
	 */
	public Double getComparisonAToB();

	/**
	 * @return the critA
	 */
	public int getCritA();

	/**
	 * @return the critB
	 */
	public int getCritB();

	/**
	 * @param comparisonAToB  the comparisonAToB to set
	 * @uml.property  name="comparisonAToB"
	 */
	public void setComparisonAToB(Double comparisonAToB);

	/**
	 * @param critA
	 *            the critA to set
	 */
	public void setCritA(int critA);

	/**
	 * @param critB
	 *            the critB to set
	 */
	public void setCritB(int critB);

	/**
	 * @param comment  the comment to set
	 * @uml.property  name="comment"
	 */
	public void setComment(String comment);

	/**
	 * @return  the comment
	 * @uml.property  name="comment"
	 */
	public String getComment();

}