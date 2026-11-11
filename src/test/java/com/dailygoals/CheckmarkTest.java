package com.dailygoals;

import java.awt.Color;
import java.awt.image.BufferedImage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CheckmarkTest
{
	@Test
	public void drawsOverACopy()
	{
		BufferedImage icon = new BufferedImage(25, 25, BufferedImage.TYPE_INT_ARGB);
		BufferedImage checked = Checkmark.over(icon);

		assertEquals(25, checked.getWidth());
		assertEquals(25, checked.getHeight());
		// The icon is left alone
		assertEquals(0, icon.getRGB(10, 19));

		// The bottom point of the check is green
		Color bottom = new Color(checked.getRGB(10, 19), true);
		assertTrue(bottom.getGreen() > 150 && bottom.getRed() < 100);
		// A corner away from the check stays clear
		assertEquals(0, checked.getRGB(1, 1));
		assertNotEquals(0, checked.getRGB(20, 7));
	}
}
