import edu.princeton.cs.algs4.Picture;
import java.awt.Color;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.function.DoubleUnaryOperator;

public class SeamCarver {
    int width;
    int height;
    Picture picture;
    public SeamCarver(Picture picture) {
        this.picture = picture;
        height = picture.height();
        width = picture.width();
    }
    public Picture picture(){
        // current picture
        return this.picture;
        }
    public int width() {
        // width of current picture
        return this.width;
    }
    public int height() {
        // height of current picture
        return height;
    }
    public double energy(int x, int y) {
        // energy of pixel at column x and row y
        if (x >= width || x < 0 || y >= height || y < 0) {
            throw new IndexOutOfBoundsException();
        }
        int[] pixel = new int[3];
        double dx;
        double dy;
        int x0, x1, y0, y1;
        if (x == 0) {
            x0 = 1;
            x1 = width - 1;
        } else if (x == width - 1) {
            x0 = x - 1;
            x1 = 0;
        } else {
            x0 = x - 1;
            x1 = x + 1;
        }
        if (y == 0) {
            y0 = height - 1;
            y1 = 1;
        } else if (y == height - 1) {
            y0 = 0;
            y1 = y - 1;
        } else {
            y0 = y - 1;
            y1 = y + 1;
        }
        Color color1 = picture.get(x0, y);
        Color color2 = picture.get(x1, y);
        dx = Math.pow(color2.getRed() - color1.getRed(), 2) +
                Math.pow(color2.getGreen() - color1.getGreen(), 2) +
                Math.pow(color2.getBlue() - color1.getBlue(), 2);
        Color color3 = picture.get(x, y0);
        Color color4 = picture.get(x, y1);
        dy = Math.pow(color3.getRed() - color4.getRed(), 2) +
                Math.pow(color3.getGreen() - color4.getGreen(), 2) +
                Math.pow(color3.getBlue() - color4.getBlue(), 2);
        double energy = dx + dy;

        return energy;
    }
    public int[] findHorizontalSeam() {
        // sequence of indices for horizontal seam
        int height = picture.height();
        int width = picture.width();
        PriorityQueue fringe = new PriorityQueue<>(new newComparator());
        for (int i = 0; i < picture.height(); i += 1) {
            Node node = new Node(energy(0, i), i, width);
            node.addToPath(i);
            fringe.add(node);
        }
        while (!fringe.isEmpty()) {
            Node curnode = (Node) fringe.poll();
            int col = curnode.getpos() / picture.height();
            int row = curnode.getpos() - col * picture.height();
            if (col == picture.width() - 1) {
                return curnode.path;
            }
            int minindex = 0;
            double min = Double.MAX_VALUE;
            for (int i = row - 1; i <= row + 1; i += 1) {
                if (i >= 0 && i <= height - 1) {
                    if (energy(col + 1, i) < min) {
                        min = energy(col + 1, i);
                        minindex = i;
                    }
                }
            }
            curnode.addToPath(minindex);
            curnode.setcost(min + curnode.getcost());
            curnode.setpos(curnode.getpos() + height - (row - minindex));
            fringe.add(curnode);
        }
        return null;
    }
    public int[] findVerticalSeam() {
        // sequence of indices for vertical seam
        int height = picture.height();
        int width = picture.width();
        PriorityQueue fringe = new PriorityQueue<>(new newComparator());
        for (int i = 0; i < picture.width(); i += 1) {
            Node node = new Node(energy(i, 0), i, height);
            node.addToPath(i);
            fringe.add(node);
        }
        while (!fringe.isEmpty()) {
            Node curnode = (Node) fringe.poll();
            int row = curnode.getpos() / picture.width();
            int col = curnode.getpos() - row * picture.width();
            if (row == picture.height() - 1) {
                return curnode.path;
            }
            int minindex = 0;
            double min = Double.MAX_VALUE;
            for (int i = col - 1; i <= col + 1; i += 1) {
                if (i >= 0 && i <= width - 1) {
                    if (energy(i, row + 1) < min) {
                        min = energy(i, row + 1);
                        minindex = i;
                    }
                }
            }
            curnode.addToPath(minindex);
            curnode.setcost(min + curnode.getcost());
            curnode.setpos(curnode.getpos() + width - (col - minindex));
            fringe.add(curnode);
        }
        return null;
    }
    public void removeHorizontalSeam(int[] seam) {
        // remove horizontal seam from picture
        if (seam.length != width) {
            throw new IllegalArgumentException();
        } else {
            SeamRemover.removeHorizontalSeam(picture, seam);
        }
    }
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height) {
            throw new IllegalArgumentException();
        } else {
            SeamRemover.removeVerticalSeam(picture, seam);
        }
    }
    class Node{
        double cost;
        int pos;
        int[] path;
        int i;
        private Node(double cost, int pos, int pathlen) {
            this.pos = pos;
            this.cost = cost;
            path = new int[pathlen];
            i = 0;
        }
        private void setpos(int j) {
            pos = j;
        }
        private int getpos() {
            return pos;
        }
        private void setcost(double cost) {
            this.cost += cost;
        }
        private double getcost() {
            return cost;
        }
        private void addToPath(int j) {
            path[i] = j;
            i += 1;
        }
    }
    class newComparator implements Comparator<Node> {
        @Override
        public int compare(Node o1, Node o2) {
            return Double.compare(o1.getcost(), o2.getcost());
        }
    }
}
