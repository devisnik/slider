/*
 * Created on 21.07.2006 by leck
 *
 */
package de.devisnik.sliding;

/**
 * A movable piece of the sliding puzzle.
 *
 */
public interface IPiece {
	String getLabel();

	Point getPosition();

	Point getHomePosition();

	boolean isAtHome();
}
