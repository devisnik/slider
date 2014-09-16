package de.devisnik.sliding.animation;

public interface IInterpolator {

	/**
	 * @param timepoint
	 *            current time of animation, usually in [0.0, 1.0]
	 * @return interpolation factor, to be applied to current point in animation
	 */
	float getInterpolation(float timepoint);
}
