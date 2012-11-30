/**
 * 
 */
package de.eorg.aotearoa.lib.model.ahp.configuration;

import java.io.Serializable;

/**
 * @author mugglmenzel
 *
 *         Author: Michael Menzel (mugglmenzel)
 * 
 *         Last Change:
 *           
 *           By Author: $Author: mugglmenzel@gmail.com $ 
 *         
 *           Revision: $Revision: 220 $ 
 *         
 *           Date: $Date: 2011-09-16 18:58:00 +0200 (Fr, 16 Sep 2011) $
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
 *         $HeadURL: https://aotearoadecisions.googlecode.com/svn/trunk/src/main/java/de/fzi/aotearoa/shared/model/ahp/configuration/GoalType.java $
 *
 */
public enum GoalType implements Serializable {

	POSITIVE(1), NEGATIVE(-1);
	
	
	/**
	 * @uml.property  name="factor"
	 */
	int factor;
	
	GoalType(int i) {
		this.setFactor(i);
	}

	/**
	 * @param factor  the factor to set
	 * @uml.property  name="factor"
	 */
	public void setFactor(int factor) {
		this.factor = factor;
	}

	/**
	 * @return  the factor
	 * @uml.property  name="factor"
	 */
	public int getFactor() {
		return factor;
	}
}
