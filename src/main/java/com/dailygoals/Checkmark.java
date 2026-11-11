package com.dailygoals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;

/**
 * Draws a green checkmark over a goal's icon, for infoboxes of reached goals.
 */
final class Checkmark
{
	private static final Color FILL = new Color(0, 230, 0);
	private static final Color OUTLINE = Color.BLACK;

	private Checkmark()
	{
	}

	/**
	 * A copy of the icon with a checkmark across its lower right. The icon itself isn't changed.
	 */
	static BufferedImage over(BufferedImage icon)
	{
		int width = icon.getWidth();
		int height = icon.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		try
		{
			g.drawImage(icon, 0, 0, null);
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			// Short stroke down to the bottom point, long stroke up to the top right
			Path2D.Float check = new Path2D.Float();
			check.moveTo(width * 0.22f, height * 0.55f);
			check.lineTo(width * 0.42f, height * 0.78f);
			check.lineTo(width * 0.82f, height * 0.28f);

			float size = Math.min(width, height);
			g.setColor(OUTLINE);
			g.setStroke(new BasicStroke(size * 0.24f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.draw(check);
			g.setColor(FILL);
			g.setStroke(new BasicStroke(size * 0.13f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.draw(check);
		}
		finally
		{
			g.dispose();
		}
		return image;
	}
}
