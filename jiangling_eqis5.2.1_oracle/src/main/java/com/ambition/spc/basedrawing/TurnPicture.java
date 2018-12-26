package com.ambition.spc.basedrawing;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**    
 * TurnPicture.java
 * @authorBy wanglf
 *
 */
public class TurnPicture
{
  public static BufferedImage turnPicture(BufferedImage image, boolean leftTurn)
  {
    int width = image.getWidth();
    int height = image.getHeight();

    BufferedImage retImage = new BufferedImage(height, width, image.getType());

    Graphics2D g2 = (Graphics2D)retImage.getGraphics();

    if (leftTurn) {
      g2.rotate(Math.toRadians(270.0D), width / 2, width / 2);
    }
    else {
      g2.rotate(Math.toRadians(90.0D), height / 2, height / 2);
    }

    g2.drawImage(image, 0, 0, null);

    return retImage;
  }
}