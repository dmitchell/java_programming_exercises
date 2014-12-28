package com.placester.test;

/*
 * Implement a 6 sided die with weights on the sides, so that we don't have an even probability distribution, but it is 
 * weighted by a list of weights passed in at construction time
 * 
 * After 10k iterations of throwing this die, the results should closely match the desired distribution, and this should
 * be reproducible in the unit test in
 * 
 * src/test/com/placester/test/WeightedDiceTest
 */
public class SixSidedWeightedDie extends WeightedDie
{
	public double[] thresholds;  // the cutoff point for each side. each has the ceiling

	//NOTE: since these are weights on a probability distribution, these should sum to one, and the incoming array
    // should be of length 6. You should throw if either of these preconditions is false
    public SixSidedWeightedDie(float[] weights)
    {
        super(weights);

        if (weights.length != 6)
        	throw new IllegalArgumentException("You must supply 6 weights not " + weights.length);

        double sum = 0.0f;
        this.thresholds = new double[6];
        for (int i=0; i<6; i++) {
        	sum += weights[i];
        	this.thresholds[i] = sum;
        }
        if (Math.abs(sum - 1.0f) > 0.001) // handle floating point imprecision (decimal float to binary float conversion)
        	throw new IllegalArgumentException("The weights must sum to 1.0 not " + sum);
    }

    //Throw the die: this should produce a value in [1,6]
    @Override
    public int throwDie()
    {
    	double toss = Math.random();
    	// 6 is too small to bother w/ binary search or other efficiency mechanisms
    	int side=0;
    	for (; this.thresholds[side] < toss; side++) {}
    	return side + 1;
    }

}
