package view.util;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import view.MainView;

public class DUResource {
	private DUResource() {
	}

	public static ImageIcon getStartImage() {
		return new ImageIcon(DUResource.get("start.png"));
	}

	public static ImageIcon getStopImage() {
		return new ImageIcon(DUResource.get("stop.png"));
	}

	public static ImageIcon getResetImage() {
		return new ImageIcon(DUResource.get("reset.png"));
	}

	public static ImageIcon getExportImage() {
		return new ImageIcon(DUResource.get("export.png"));
	}

	public static ImageIcon getAddImage() {
		return new ImageIcon(DUResource.get("add.png"));
	}

	public static ImageIcon getDeleteImage() {
		return new ImageIcon(DUResource.get("delete.png"));
	}

	public static List<? extends Image> getLogoIcons() {
		final List<Image> images = new ArrayList<>();
		try {
			images.add(ImageIO.read(DUResource.get("logo_16.png")));
			images.add(ImageIO.read(DUResource.get("logo_32.png")));
			images.add(ImageIO.read(DUResource.get("logo_64.png")));
			images.add(ImageIO.read(DUResource.get("logo_128.png")));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return images;
	}

	private static URL get(final String resource) {
		final URL res = MainView.class.getResource("/img/" + resource);
		return res != null ? res : MainView.class.getResource("/img/notFound.png");
	}
}
