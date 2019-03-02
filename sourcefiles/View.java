import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Color;

class View extends JPanel
{
	Model model;
	static BufferedImage bg = null;

	View(Controller c, Model m)
	{
		model = m;
		if(bg == null)
		{
			try
			{

				bg = ImageIO.read(new File("./images/bgClouds.png"));

			}
			catch(Exception e)
			{
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}

	}

	public void paintComponent(Graphics g)
	{
		//draw background
		for(int i = 0; i < 7680; i+=1920)
		{
			g.drawImage(bg, -model.scrollPos+i, 0, null);
		}

		// draw ground
		g.setColor(new Color(62,122,67));
	 g.fillRect(0, 595, 900, 700);


		//Sprite s = model.sprites.get(0);
		//System.out.println(s);
		for(int i = 0; i < model.sprites.size(); i++)
		{
			Sprite s = model.sprites.get(i);
			s.draw(g);
		}

	}
}
