package com.example.wxtools.controller;

import com.madgag.gif.fmsware.AnimatedGifEncoder;
import com.madgag.gif.fmsware.GifDecoder;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Picture {

    public static void main(String[] args) throws InterruptedException {
        reverseImage("D:\\programTools\\wechat-devtool\\pic\\pic.gif", "D:\\programTools\\wechat-devtool\\pic\\outReverse1.gif");
        Thread.sleep(5000);
    }

    public static void testResizeImage() {
        try {
            Picture.resizeImage("D:\\文件\\壁纸\\pic.gif", "D:\\文件\\壁纸\\picR.gif", 210, 160);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void resizeImage(String srcImgPath, String distImgPath, int width, int height) throws IOException {
        File srcFile = new File(srcImgPath);
        Image srcImg = ImageIO.read(srcFile);
        BufferedImage bufferedImage = null;
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_AREA_AVERAGING), 0, 0, null);
        ImageIO.write(bufferedImage, "JPEG", new File(distImgPath));
    }

    public static void testComposeImage() {
        File[] files = new File[3];
        files[0] = new File("D:\\文件\\壁纸\\1.jpg");
        files[1] = new File("D:\\文件\\壁纸\\2.jpg");
        files[2] = new File("D:\\文件\\壁纸\\3.jpeg");
        Picture.composeImage(files);
    }

    private static File composeImage(File[] files) {
        try {
            BufferedImage[] bufferedImage = null;
            bufferedImage = parse(files);
            Path path = Files.createTempFile("preview_", ".gif");
            System.out.println(path);
            AnimatedGifEncoder encoder = new AnimatedGifEncoder();
            encoder.setRepeat(0);
            encoder.start(new FileOutputStream(path.toFile()));
            int count = 1;
            int frequency = 2;
            for (BufferedImage image : bufferedImage) {
                if ((++count) % frequency == 0) {
                    encoder.setDelay(50 * frequency);
                    encoder.addFrame(image);
                }
            }
            encoder.finish();
            System.out.print("GIF创建成功： ");
            return path.toFile();
        } catch (IOException e) {
            System.out.println("failed to generate preview file");
            throw new RuntimeException("failed to generate preview file");
        }
    }

    private static BufferedImage[] parse(File[] files) {
        BufferedImage[] bufferedImages = new BufferedImage[files.length];
        try {
            for (int index = 0; index < files.length; ++index) {
                bufferedImages[index] = ImageIO.read(files[index]);
            }
        } catch (IOException e) {
            System.out.println("fail to parse template");
            throw new RuntimeException("fail to parse template", e);
        }
        return bufferedImages;
    }

    public static void testReverseImage() throws InterruptedException {
        reverseImage("D:\\programTools\\wechat-devtool\\pic\\pic.gif", "D:\\programTools\\wechat-devtool\\pic\\outReverse1.gif");
        Thread.sleep(5000);
    }

    static byte[] reverseImage(String src, String tar) {
        BufferedImage image = null;
        byte[] bytes = null;

        GifDecoder gifDecoder = new GifDecoder();
        gifDecoder.read(src);
        try {
            AnimatedGifEncoder animatedGifEncoder = new AnimatedGifEncoder();
            animatedGifEncoder.start(tar);
            animatedGifEncoder.setRepeat(gifDecoder.getLoopCount());
            for (int i = gifDecoder.getFrameCount() -1; i >= 0; i--) {
                animatedGifEncoder.setDelay(gifDecoder.getDelay(i));
                BufferedImage bufferedImage = gifDecoder.getFrame(i);
                int height = bufferedImage.getHeight();
                int width = bufferedImage.getWidth();
                BufferedImage zoomImage = new BufferedImage(width, height, bufferedImage.getType());
                Image tempImage = bufferedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                Graphics gc = zoomImage.getGraphics();
                gc.setColor(Color.WHITE);
                gc.drawImage(tempImage, 0, 0, null);
                animatedGifEncoder.addFrame(zoomImage);
            }

            animatedGifEncoder.finish();
            FileInputStream fileInputStream = new FileInputStream(new File(tar));
            bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes, 0, fileInputStream.available());
        }catch (Exception e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
