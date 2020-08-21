package com.example.shatding_springboot_mybatis_generator.util;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ClassPathResource;

import com.alibaba.fastjson.JSONObject;
import com.example.shatding_springboot_mybatis_generator.config.SpringContextUtil;
/**
 * 滑块验证工具类
 * @author : spirit
 * @date : Created in 10:57 2019/9/05
 */
public class VerifyImageUtil {

    /** 源文件宽度 */
    private static final int ORI_WIDTH = 590;
    /** 源文件高度 */
    private static final int ORI_HEIGHT = 360;
     /** 抠图坐标x */
    private static int X;
     /** 抠图坐标y */
    private static int Y;
     /** 模板图宽度 */
    private static int WIDTH;
     /** 模板图高度 */
    private static int HEIGHT;
    //base64前缀
    private static String BASE64_CAPTURE = "data:image/jpeg|png|gif;base64,";
    //classPathUrl_template_target
    public static String CLASSPATHURL_TEMPLATE_EX_PNG = "png";
    public static String CLASSPATHURL_TARGET_EX_JPG = "jpg";
    public static String CLASSPATHURL_TEMPLATE = "static/template/%s.png";
    public static String CLASSPATHURL_TARGET = "static/target/%s.jpg";
    public static int CLASSPATHURL_TEMPLATE_LENGTH = 4;
    public static int CLASSPATHURL_TARGET_LENGTH = 20;
    public static int CLASSPATHURL_TEMPLATE_TARGET_LENGTH_START = 1;
    //存放最大10分钟
    private static int MINUTES_10 = 10;
    public static int getX() {
        return X;
    }

    public static int getY() {
        return Y;
    }
    public static int getTemplateIndex() {
         Random random = new Random();
         return random.nextInt(CLASSPATHURL_TEMPLATE_LENGTH)%(CLASSPATHURL_TEMPLATE_LENGTH-CLASSPATHURL_TEMPLATE_TARGET_LENGTH_START + 1) + CLASSPATHURL_TEMPLATE_TARGET_LENGTH_START;
    }
    public static int getTargetIndex() {
    	 Random random = new Random();
         return random.nextInt(CLASSPATHURL_TARGET_LENGTH)%(CLASSPATHURL_TARGET_LENGTH-CLASSPATHURL_TEMPLATE_TARGET_LENGTH_START + 1) + CLASSPATHURL_TEMPLATE_TARGET_LENGTH_START;
    }
    /**
     * 根据模板切图
     * @param templateFile 模板文件
     * @param targetFile 目标文件
     * @param templateType 模板文件类型
     * @param targetType 目标文件类型
     * @return 切图map集合
     * @throws Exception 异常
     */
	public static Map<String, Object> pictureTemplatesCut(File templateFile, File targetFile, String templateType, String targetType) throws Exception {
        Map<String, Object> pictureMap = new HashMap<>(2);
        if (templateType == null || targetType == null) {
            throw new RuntimeException("file type is empty");
        }
        InputStream targetIs = new FileInputStream(targetFile);
        // 模板图
        BufferedImage imageTemplate = ImageIO.read(templateFile);
        WIDTH = imageTemplate.getWidth();
        HEIGHT = imageTemplate.getHeight();
        // 随机生成抠图坐标
        generateCutoutCoordinates();
        // 最终图像
        BufferedImage newImage = new BufferedImage(WIDTH, HEIGHT, imageTemplate.getType());
        Graphics2D graphics = newImage.createGraphics();
        graphics.setBackground(Color.white);

        int bold = 5;
        // 获取感兴趣的目标区域
        BufferedImage targetImageNoDeal = getTargetArea(X, Y, WIDTH, HEIGHT, targetIs, targetType);

        // 根据模板图片抠图
        newImage = dealCutPictureByTemplate(targetImageNoDeal, imageTemplate, newImage);

        // 设置“抗锯齿”的属性
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics.setStroke(new BasicStroke(bold, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        graphics.drawImage(newImage, 0, 0, null);
        graphics.dispose();

        //新建流。
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流。
        ImageIO.write(newImage, "png", os);
        byte[] newImages = os.toByteArray();
        pictureMap.put("newImage", BASE64_CAPTURE + Base64.encodeBase64String(newImages));

        // 源图生成遮罩
        BufferedImage oriImage = ImageIO.read(targetFile);
        byte[] oriCopyImages = dealOriPictureByTemplate(oriImage, imageTemplate, X, Y);
        pictureMap.put("oriCopyImage", BASE64_CAPTURE + Base64.encodeBase64String(oriCopyImages));
//        System.out.println("X="+X+";y="+Y);
        //位置
        String acptureUuid = UUID.randomUUID().toString();
        pictureMap.put("acptureUuid", acptureUuid);
        //当前产生的图片X偏移存入redis,10分钟有效
        RedisUtil redisUtil = SpringContextUtil.getBean(RedisUtil.class);
        redisUtil.set(acptureUuid, X, TimeUnit.MINUTES.toSeconds(MINUTES_10));
        return pictureMap;
    }

    /**
     * 抠图后原图生成
     * @param oriImage 原始图片
     * @param templateImage 模板图片
     * @param x 坐标X
     * @param y 坐标Y
     * @return 添加遮罩层后的原始图片
     * @throws Exception 异常
     */
    private static byte[] dealOriPictureByTemplate(BufferedImage oriImage, BufferedImage templateImage, int x,
                                                   int y) throws Exception {
        // 源文件备份图像矩阵 支持alpha通道的rgb图像
        BufferedImage oriCopyImage = new BufferedImage(oriImage.getWidth(), oriImage.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        // 源文件图像矩阵
        int[][] oriImageData = getData(oriImage);
        // 模板图像矩阵
        int[][] templateImageData = getData(templateImage);

        //copy 源图做不透明处理
        for (int i = 0; i < oriImageData.length; i++) {
            for (int j = 0; j < oriImageData[0].length; j++) {
                int rgb = oriImage.getRGB(i, j);
                int r = (0xff & rgb);
                int g = (0xff & (rgb >> 8));
                int b = (0xff & (rgb >> 16));
                //无透明处理
                rgb = r + (g << 8) + (b << 16) + (255 << 24);
                oriCopyImage.setRGB(i, j, rgb);
            }
        }

        for (int i = 0; i < templateImageData.length; i++) {
            for (int j = 0; j < templateImageData[0].length - 5; j++) {
                int rgb = templateImage.getRGB(i, j);
                //对源文件备份图像(x+i,y+j)坐标点进行透明处理
                if (rgb != 16777215 && rgb <= 0) {
                    int rgb_ori = oriCopyImage.getRGB(x + i, y + j);
                    int r = (0xff & rgb_ori);
                    int g = (0xff & (rgb_ori >> 8));
                    int b = (0xff & (rgb_ori >> 16));
                    rgb_ori = r + (g << 8) + (b << 16) + (150 << 24);
                    oriCopyImage.setRGB(x + i, y + j, rgb_ori);
                } else {
                    //do nothing
                }
            }
        }
        //新建流
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        //利用ImageIO类提供的write方法，将bi以png图片的数据模式写入流
        ImageIO.write(oriCopyImage, "png", os);
        //从流中获取数据数组
        return os.toByteArray();
    }


    /**
     * 根据模板图片抠图
     * @param oriImage 原始图片
     * @param templateImage 模板图片
     * @return 扣了图片之后的原始图片
     */
    private static BufferedImage dealCutPictureByTemplate(BufferedImage oriImage, BufferedImage templateImage,
                                                          BufferedImage targetImage) throws Exception {
        // 源文件图像矩阵
        int[][] oriImageData = getData(oriImage);
        // 模板图像矩阵
        int[][] templateImageData = getData(templateImage);
        // 模板图像宽度

        for (int i = 0; i < templateImageData.length; i++) {
            // 模板图片高度
            for (int j = 0; j < templateImageData[0].length; j++) {
                // 如果模板图像当前像素点不是白色 copy源文件信息到目标图片中
                int rgb = templateImageData[i][j];
                if (rgb != 16777215 && rgb <= 0) {
                    targetImage.setRGB(i, j, oriImageData[i][j]);
                }
            }
        }
        return targetImage;
    }

    /**
     * 获取目标区域
     * @param x            随机切图坐标x轴位置
     * @param y            随机切图坐标y轴位置
     * @param targetWidth  切图后目标宽度
     * @param targetHeight 切图后目标高度
     * @param ois          源文件输入流
     * @return 返回目标区域
     * @throws Exception 异常
     */
    private static BufferedImage getTargetArea(int x, int y, int targetWidth, int targetHeight, InputStream ois,
                                               String fileType) throws Exception {
        Iterator<ImageReader> imageReaderList = ImageIO.getImageReadersByFormatName(fileType);
        ImageReader imageReader = imageReaderList.next();
        // 获取图片流
        ImageInputStream iis = ImageIO.createImageInputStream(ois);
        // 输入源中的图像将只按顺序读取
        imageReader.setInput(iis, true);

        ImageReadParam param = imageReader.getDefaultReadParam();
        Rectangle rec = new Rectangle(x, y, targetWidth, targetHeight);
        param.setSourceRegion(rec);
        return imageReader.read(0, param);
    }

    /**
     * 生成图像矩阵
     * @param bufferedImage 图片流
     * @return 图像矩阵
     */
    private static int[][] getData(BufferedImage bufferedImage){
        int[][] data = new int[bufferedImage.getWidth()][bufferedImage.getHeight()];
        for (int i = 0; i < bufferedImage.getWidth(); i++) {
            for (int j = 0; j < bufferedImage.getHeight(); j++) {
                data[i][j] = bufferedImage.getRGB(i, j);
            }
        }
        return data;
    }

    /**
     * 随机生成抠图坐标
     */
    private static void generateCutoutCoordinates() {
        Random random = new Random();
        // ORI_WIDTH：590  ORI_HEIGHT：360
        // WIDTH：93 HEIGHT：360
        int widthDifference = ORI_WIDTH - WIDTH;
        int heightDifference = ORI_HEIGHT - HEIGHT;

        if (widthDifference <= 0) {
            X = 5;
        } else {
            X = random.nextInt(ORI_WIDTH - 3*WIDTH) + 2*WIDTH + 5;
        }

        if (heightDifference <= 0) {
            Y = 5;
        } else {
            Y = random.nextInt(ORI_HEIGHT - HEIGHT) + 5;
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
    }
}


