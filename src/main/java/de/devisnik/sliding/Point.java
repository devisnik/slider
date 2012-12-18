package de.devisnik.sliding;

public class Point {

	public static Point diff(Point left, Point right) {
		return new Point(left.x - right.x, left.y - right.y);
	}

	public static Point divide(int width, int height, Point size) {
		return new Point(width / size.x, height / size.y);
	}

	public static Point divide(Point left, Point right) {
		return new Point(left.x / right.x, left.y / right.y);
	}

	public static Point divide(Point left, int number) {
		return new Point(left.x / number, left.y / number);
	}

	public static Point sum(Point point, int x, int y) {
		return new Point(point.x + x, point.y + y);
	}

	public static Point times(Point point, float factor) {
		return new Point(Math.round(factor * point.x), Math.round(factor * point.y));
	}

	public static Point times(Point point, int factor) {
		return new Point(factor * point.x, factor * point.y);
	}

	public static Point times(Point left, Point right) {
		return new Point(left.x * right.x, left.y * right.y);
	}

	public int x;
	public int y;

	public Point() {
		this(0, 0);
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point add(Point point) {
		x += point.x;
		y += point.y;
		return this;
	}

	public Point set(Point point) {
		this.x = point.x;
		this.y = point.y;
		return this;
	}

	public int min() {
		return Math.min(x, y);
	}

	public int max() {
		return Math.max(x, y);
	}

	public float ratio() {
		return ((float) x) / y;
	}

	public Point flip() {
		int mem = x;
		x = y;
		y = mem;
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Point other = (Point) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public Point divideBy(Point point) {
		x /= point.x;
		y /= point.y;
		return this;
	}

	public Point multiplyBy(Point point) {
		x *= point.x;
		y *= point.y;
		return this;
	}

	public Point minus(Point point) {
		x -= point.x;
		y -= point.y;
		return this;
	}

	public Point divideBy(int number) {
		x /= number;
		y /= number;
		return this;
	}

	public Point copy() {
		return new Point(x, y);
	}
}
