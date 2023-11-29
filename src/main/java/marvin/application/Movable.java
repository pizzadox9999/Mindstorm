package marvin.application;

public interface Movable {
    public void moveTo(double position);
    public void moveTo(double position, double velocity);
	/**
	 * @param position takes the position as mm
	 */
	void moveTo(double position, double velocity, boolean returnImediate);
}
