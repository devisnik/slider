package de.devisnik.sliding.impl;

import de.devisnik.sliding.IPiece;
import de.devisnik.sliding.Point;

public class Piece implements IPiece {

	private String itsLabel;
	public int itsPosY;
	public int itsPosX;
	private Point itsHomePosition;
	private boolean itsIsHome;

	public Piece(int posX, int posY, String label) {
		itsLabel = label;
		itsHomePosition = new Point(posX, posY);
		setPosition(posX, posY);
	}

	public String getLabel() {
		return itsLabel;
	}

	public Point getPosition() {
		return new Point(itsPosX, itsPosY);
	}

	void setPosition(int posX, int posY) {
		itsPosX = posX;
		itsPosY = posY;
		itsIsHome = itsPosX == itsHomePosition.x && itsPosY == itsHomePosition.y;
	}

	@Override
	public int hashCode() {
		return itsHomePosition.hashCode();
	}

	public Point getHomePosition() {
		return itsHomePosition;
	}

	public boolean isAtHome() {
		return itsIsHome;
	}
}
