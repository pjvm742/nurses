package nurses;

import java.util.Random;

public class DynamicParams {
	int N;
	int C;

	Random r;
	double[][] pdist;
	double max;
	int[][] ntries;
	int[][] nsuccess;

	public int kN = 0;
	public int kC = 0;

	public DynamicParams(int N, int C) {
		this.N = N;
		this.C = C;
		
		this.r = new Random();
		this.pdist = new double[N][C];
		this.ntries = new int[N][C];
		this.nsuccess = new int[N][C];
		double maxval = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < C; j++) {
				ntries[i][j] = 1;
				nsuccess[i][j] = 1;
				double val = (double) nsuccess[i][j] / (double) ntries[i][j] / (double) ((i+1)*(j+1));
				pdist[i][j] = val;
				if (val > maxval) {
					maxval = val;
				}
			}
		}
		this.max = maxval;
	}

	public void sample() {
		int i = -1;
		int j = -1;
		boolean accept = false;
		while (!accept) {
			i = r.nextInt(N);
			j = r.nextInt(C);
			double p = pdist[i][j] / max;
			if (r.nextDouble() < p) {
				accept = true;
			}
		}

		kN = i+1;
		kC = j+1;
	}

	public void update(boolean success) {
		int i = kN -1;
		int j = kC -1;
		ntries[i][j]++;
		if (success) {
			nsuccess[i][j]++;
		}
		double oldval = pdist[i][j];
		double newval = (double) nsuccess[i][j] / (double) ntries[i][j] / (double) (kN*kC);
		pdist[i][j] = newval;
		if (newval > max) {
			max = newval;
		}
		if (oldval == max && newval < max) {
			double maxval = 0;
			for (int a = 0; a < N; a++) {
				for (int b = 0; b < C; b++) {
					double val = pdist[a][b];
					if (val > maxval) {
						maxval = val;
					}
				}
			}
			max = maxval;
		}
	}
}
