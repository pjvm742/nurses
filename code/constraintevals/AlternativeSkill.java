package constraintevals;

import nurses.*;

public class AlternativeSkill extends ConstraintEvaluator {
	int[] requiredSkill;
	boolean[] skillset;
	
	public AlternativeSkill(int[] requiredSkill, boolean[] skillset) {
		this.requiredSkill = requiredSkill;
		this.skillset = skillset;
	}

	public int Evaluate(int[] roster) {
		int violation = 0;
		for (int d = 0; d < Dim.D; d++) {
			int s = roster[d];
			if (s == 0) {
				continue;
			}
			int sk = requiredSkill[s];
			if (!skillset[sk]) {
				violation++;
			}
		}
		return violation;
	}

	public int Contribution(int[] roster, int pos) {
		int s = roster[pos];
		if (s == 0) {
			return 0;
		}
		int sk = requiredSkill[s];
		if (!skillset[sk]) {
			return 1;
		}
		return 0;
	}

	public void Enforce(int[] roster) {
		for (int d = 0; d < Dim.D; d++) {
			int s = roster[d];
			if (s == 0) {
				continue;
			}
			int sk = requiredSkill[s];
			if (!skillset[sk]) {
				roster[d] = 0;
			}
		}

	}
}
