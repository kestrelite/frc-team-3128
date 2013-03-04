
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class GraphicsUtil {

    public static void antialias(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public static void antialias(Graphics2D g) {
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public static void alpha(Graphics g, float alpha) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    public static void alpha(Graphics2D g, float alpha) {
        g.setComposite(
                AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
    }

    public static Point getCenter(Rectangle2D r) {
        return new Point((int) r.getWidth() / 2, (int) r.getHeight() / 2);
    }

    public static Point getCenter(Rectangle2D parent, Rectangle2D child) {
        return new Point(
                ((int) parent.getWidth() / 2) - ((int) child.getWidth() / 2),
                ((int) parent.getHeight() / 2) - ((int) child.getHeight() / 2));
    }

    public static BufferedImage scaleImage(BufferedImage image, int w, int h) {
        if (image.getWidth() == w && image.getHeight() == h) {
            return image;
        }

        BufferedImage dest =
                new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dest.createGraphics();

        AffineTransform at =
                AffineTransform.getScaleInstance((double) w / image.getWidth(),
                (double) h / image.getHeight());

        g.drawRenderedImage(image, at);
        return dest;
    }

    public static BufferedImage aspectScale(BufferedImage image, int maxW, int maxH) {
        double ratioX = (double) maxW / image.getWidth();
        double ratioY = (double) maxH / image.getHeight();

        double ratio = Math.min(ratioX, ratioY);

        return scaleImage(
                image,
                (int) (image.getWidth() * ratio),
                (int) (image.getHeight() * ratio));
    }

    public static BufferedImage crop(BufferedImage image, Rectangle crop) {
        int width = crop.width;
        if (width <= 0) {
            width = 1;
        }

        int height = crop.height;
        if (height <= 0) {
            height = 1;
        }

        BufferedImage ret = new BufferedImage(
                width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = ret.createGraphics();
        g.drawImage(image,
                0, 0, width, height,
                crop.x, crop.y, crop.x + width, crop.y + height,
                null);

        return ret;
    }

    public static BufferedImage clone(BufferedImage image) {
        BufferedImage clone = new BufferedImage(image.getWidth(),
                image.getHeight(), image.getType());

        Graphics2D g2d = clone.createGraphics();
        g2d.drawImage(image, 0, 0, null);

        return clone;
    }

    public static BufferedImage rotate(BufferedImage image, float angle) {
        int size = Math.max(image.getWidth(), image.getHeight());

        BufferedImage clone = new BufferedImage(
                size, size, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = clone.createGraphics();
        AffineTransform at = new AffineTransform();

        // rotate
        at.setToRotation(Math.toRadians(angle),
                clone.getWidth() / 2, clone.getHeight() / 2);

        // center
        int cx = (clone.getWidth() / 2) - (image.getWidth() / 2);
        int cy = (clone.getHeight() / 2) - (image.getHeight() / 2);
        at.translate(cx, cy);

        // draw
        g2d.drawImage(image, at, null);
        g2d.dispose();

        return clone;
    }

    public static Rectangle center(Rectangle rect) {
        return new Rectangle(
                rect.x - (rect.width / 2),
                rect.y - (rect.height / 2),
                rect.width, rect.height);
    }

    public static boolean containsPartOf(Rectangle a, Rectangle b) {
        int x = b.x;
        int y = b.y;
        int w = b.width;
        int h = b.height;

        Point topLeft = new Point(x, y);
        Point topRight = new Point(x + w, y);
        Point bottomLeft = new Point(x, y + h);
        Point bottomRight = new Point(x + h, y + h);

        return a.contains(topLeft) || a.contains(topRight)
                || a.contains(bottomLeft) || a.contains(bottomRight);
    }

    public static boolean inRange(int a, int base, int range) {
        return a > (base - range)
                && a < (base + range);
    }

    public static boolean fuzzyColorCompare(int rgb, int color, int range) {
        int cr = (color >> 16) & 0xFF;
        int cg = (color >> 8) & 0xFF;
        int cb = (color >> 0) & 0xFF;

        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb >> 0) & 0xFF;

        return inRange(r, cr, range)
                && inRange(g, cg, range)
                && inRange(b, cb, range);
    }

    public static boolean fuzzyColorCompare(int rgb, int color,
            int rs, int bs, int gs) {
        int cr = (color >> 16) & 0xFF;
        int cg = (color >> 8) & 0xFF;
        int cb = (color >> 0) & 0xFF;

        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;
        int b = (rgb >> 0) & 0xFF;

        return inRange(r, cr, rs)
                && inRange(g, cg, gs)
                && inRange(b, cb, bs);
    }

    public static BufferedImage filter(BufferedImage image, int rgb, int sensitivity) {
        BufferedImage ret = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int c = image.getRGB(x, y);

                if (!fuzzyColorCompare(c, rgb, sensitivity)) {
                    // this pixel is not ~= filter color, keep it
                    ret.setRGB(x, y, c);
                }
            }
        }

        return ret;
    }

    public static BufferedImage filter(BufferedImage image, Point p, int sensitivity) {
        return filter(image, image.getRGB(p.x, p.y), sensitivity);
    }

    public static BufferedImage invFilter(BufferedImage image, int rgb,
            int rs, int gs, int bs) {
        BufferedImage ret = new BufferedImage(
                image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                int c = image.getRGB(x, y);

                if (fuzzyColorCompare(c, rgb, rs, gs, bs)) {
                    // this pixel is ~= filter color, keep it
                    ret.setRGB(x, y, c);
                }
            }
        }

        return ret;
    }

    public static BufferedImage invFilter(
            BufferedImage image, int rgb, int sensitivity) {
        return invFilter(image, rgb, sensitivity, sensitivity, sensitivity);
    }

    public static BufferedImage autocrop(BufferedImage image) {
        int left = 0;
        int right = 0;
        int top = 0;
        int bottom = 0;

        // check left -> right
        for (int x = 0; x < image.getWidth(); x++) {
            if (!columnValid(image, x)) {
                // column contains all non-zero pixels, remove
                left++;
            } else {
                break;
            }
        }

        if (left == image.getWidth()) {
            return null; // empty image
        }

        // check right -> left
        for (int x = image.getWidth() - 1; x >= 0; x--) {
            if (!columnValid(image, x)) {
                right++;
            } else {
                break;
            }
        }

        // top -> bottom
        for (int y = 0; y < image.getHeight(); y++) {
            if (!rowValid(image, y, left, image.getWidth() - right)) {
                top++;
            } else {
                break;
            }
        }

        // bottom -> top
        for (int y = image.getHeight() - 1; y >= 0; y--) {
            if (!rowValid(image, y, left, image.getWidth() - right)) {
                bottom++;
            } else {
                break;
            }
        }

        Rectangle crop = new Rectangle(
                left, top,
                image.getWidth() - left - right,
                image.getHeight() - top - bottom);

        return crop(image, crop);
    }

    private static boolean columnValid(BufferedImage image, int x) {
        boolean valid = false;
        for (int y = 0; y < image.getHeight(); y++) {
            if (image.getRGB(x, y) != 0) {
                valid = true;
                break;
            }
        }

        return valid;
    }

    private static boolean rowValid(BufferedImage image, int y, int min, int max) {
        boolean valid = false;
        for (int x = min; x < max; x++) {
            if (image.getRGB(x, y) != 0) {
                valid = true;
                break;
            }
        }

        return valid;
    }
}
