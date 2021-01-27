package refactoring;

import java.util.HashMap;
import java.util.Map;

import static refactoring.Rover.Heading.*;
import static refactoring.Rover.Order.*;

public class Rover {

	private Heading heading;
	private Position position;

	public Rover(String facing, int x, int y) {
		this(Heading.of(facing), x, y);
	}

	public Rover(Heading heading, int x, int y) {
		this.heading = heading;
		this.position = new Position(x,y);
	}

	public Rover(Heading heading, Position position) {
		this.heading = heading;
		this.position = position;
	}

	public Heading heading() {
		return heading;
	}

	public Position position() {
		return position;
	}

	public void go(Order... orders) {
		execute(orders[0]);
	}

	private void execute(Order order) {
		if (order == Left) heading = heading.turnLeft();
		if (order == Right) heading = heading.turnRight();
		if (order == Forward) position = position.forward(heading);
		if (order == Backward) position = position.backward(heading);
	}

	private Map<Order, Action> actions = new HashMap<>();
	{
		actions.put(Left, () -> heading = heading.turnLeft());
		actions.put(Right, () -> heading = heading.turnRight());
		actions.put(Forward, () -> position = position.forward(heading));
		actions.put(Backward, () -> position = position.backward(heading));
	}

	public void go(String orders) {
		go(ordersIn(orders));
	}

	private Order[] ordersIn(String instructions) {
		Order[] orders = new Order[instructions.length()];
		for (int i = 0; i < instructions.length(); i++) {
			orders[i] = Order.of(instructions.charAt(i));
		}
		return orders;
	}


	@FunctionalInterface
	public interface Action {
		void execute();
	}

	public static class Position {
		private final int x;
		private final int y;

		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public Position forward(Heading heading) {
			return new Position(x+dx(heading), y+dy(heading));
		}

		public Position backward(Heading heading) {
			return new Position(x-dx(heading), y-dy(heading));
		}

		private int dx(Heading heading) {
			return heading == East ? 1 : heading == West ? -1 : 0;
		}

		private int dy(Heading heading) {
			return heading == North ? 1 : heading == South ? -1 : 0;
		}

		@Override
		public String toString() {
			return "Position{" +
					"x=" + x +
					", y=" + y +
					'}';
		}

		@Override
		public boolean equals(Object object) {
			return isSameClass(object) && equals((Position) object);
		}

		private boolean equals(Position position) {
			return position == this || (x == position.x && y == position.y);
		}

		private boolean isSameClass(Object object) {
			return object != null && object.getClass() == Position.class;
		}

	}

	public enum Order {
		Forward, Backward, Left, Right;

		static Order of(char c) {
			if (c == 'L') return Left;
			if (c == 'R') return Right;
			if (c == 'F') return Forward;
			if (c == 'B') return Backward;
			return null;
		}
	}


	public enum Heading {
		North, East, South, West;

		public static Heading of(String label) {
			return of(label.charAt(0));
		}

		public static Heading of(char label) {
			if (label == 'N') return North;
			if (label == 'S') return South;
			if (label == 'W') return West;
			if (label == 'E') return East;
			return null;
		}

		public Heading turnRight() {
			return values()[add(+1)];
		}

		public Heading turnLeft() {
			return values()[add(-1)];
		}

		private int add(int offset) {
			return (this.ordinal() + offset + values().length) % values().length;
		}

	}


}

