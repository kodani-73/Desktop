
import java.awt.*;
import java.awt.geom.*;

public class Bullet extends BaseObject {

	public Bullet()
	{
		super();
	}

	public void Show(Graphics2D g2)
	{
		if(!isEnable) return;

		g2.setPaint(Color.orange);
		g2.fill(new Ellipse2D.Double(fX - 5f, fY - 5f, 10f, 10f));
	}
}
