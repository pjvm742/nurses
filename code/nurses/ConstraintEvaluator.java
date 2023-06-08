package nurses;

import java.util.Random;

	public abstract class ConstraintEvaluator {
		public abstract int Evaluate(int[] roster); // determines the number of violations of this constraint in the roster
		public abstract int Contribution(int[] roster, int position); // determines the number of violations of this constraint that one day is involved in
		public abstract void Enforce(int[] roster); // removes all violations of this constraint (works through side effects)

		public int ImpactOfChange(int[] roster, int index, int newshift) {
			int curshift = roster[index];
			int curval = this.Contribution(roster, index);
			roster[index] = newshift;
			int newval = this.Contribution(roster, index);
			roster[index] = curshift;

			return newval - curval;
		}

		public int ChooseShiftType() {
			Random r = new Random();
			return r.nextInt(Dim.S);
		}

	}
