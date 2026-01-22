package cn.stylefeng.roses.kernel.security.captcha.util;

import cn.hutool.core.codec.Base64;
import cn.stylefeng.roses.kernel.security.api.pojo.DragCaptchaImageDTO;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 生成拖拽验证码的工具
 *
 * @author fengshuonan
 * @since 2021/7/5 14:06
 */
public class DragCaptchaImageUtil {

    /**
     * 验证码图片的base64编码
     */
    public static final String IMAGE_BASE64 = "/9j/4QAYRXhpZgAASUkqAAgAAAAAAAAAAAAAAP/sABFEdWNreQABAAQAAAACAAD/4QMvaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/PiA8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIiB4OnhtcHRrPSJBZG9iZSBYTVAgQ29yZSA2LjAtYzAwNiA3OS5kYWJhY2JiLCAyMDIxLzA0LzE0LTAwOjM5OjQ0ICAgICAgICAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnhtcE1NPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvbW0vIiB4bWxuczpzdFJlZj0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3NUeXBlL1Jlc291cmNlUmVmIyIgeG1wOkNyZWF0b3JUb29sPSJBZG9iZSBQaG90b3Nob3AgMjIuNCAoTWFjaW50b3NoKSIgeG1wTU06SW5zdGFuY2VJRD0ieG1wLmlpZDowNEFBRURENTg0RkMxMUVFODg4MkEzRDQ3MDZBRTUxMiIgeG1wTU06RG9jdW1lbnRJRD0ieG1wLmRpZDowNEFBRURENjg0RkMxMUVFODg4MkEzRDQ3MDZBRTUxMiI+IDx4bXBNTTpEZXJpdmVkRnJvbSBzdFJlZjppbnN0YW5jZUlEPSJ4bXAuaWlkOjA0QUFFREQzODRGQzExRUU4ODgyQTNENDcwNkFFNTEyIiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjA0QUFFREQ0ODRGQzExRUU4ODgyQTNENDcwNkFFNTEyIi8+IDwvcmRmOkRlc2NyaXB0aW9uPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiA8P3hwYWNrZXQgZW5kPSJyIj8+/+4ADkFkb2JlAGTAAAAAAf/bAIQAGhgYJhsmPCMjPEIvLi9C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";

    /**
     * 源文件宽度
     */
    private static final int ORI_WIDTH = 375;

    /**
     * 源文件高度
     */
    private static final int ORI_HEIGHT = 200;

    /**
     * 模板图宽度
     */
    private static final int CUT_WIDTH = 50;

    /**
     * 模板图高度
     */
    private static final int CUT_HEIGHT = 50;

    /**
     * 抠图凸起圆心
     */
    private static final int CIRCLE_R = 5;

    /**
     * 抠图内部矩形填充大小
     */
    private static final int RECTANGLE_PADDING = 8;

    /**
     * 抠图的边框宽度
     */
    private static final int SLIDER_IMG_OUT_PADDING = 1;

    /**
     * 根据传入的路径生成指定验证码图片
     *
     * @author fengshuonan
     * @since 2021/7/5 14:08
     */
    public static DragCaptchaImageDTO getVerifyImage(InputStream inputStream) throws IOException {
        BufferedImage srcImage = ImageIO.read(inputStream);
        int locationX = CUT_WIDTH + new Random().nextInt(srcImage.getWidth() - CUT_WIDTH * 3);
        int locationY = CUT_HEIGHT + new Random().nextInt(srcImage.getHeight() - CUT_HEIGHT) / 2;
        BufferedImage markImage = new BufferedImage(CUT_WIDTH, CUT_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        int[][] data = getBlockData();
        cutImgByTemplate(srcImage, markImage, data, locationX, locationY);
        return new DragCaptchaImageDTO(getImageBASE64(srcImage), getImageBASE64(markImage), locationX, locationY);
    }

    /**
     * 生成随机滑块形状
     * <p>
     * 0 透明像素
     * 1 滑块像素
     * 2 阴影像素
     *
     * @return int[][]
     */
    private static int[][] getBlockData() {
        int[][] data = new int[CUT_WIDTH][CUT_HEIGHT];
        Random random = new Random();
        //(x-a)²+(y-b)²=r²
        //x中心位置左右5像素随机
        double x1 = RECTANGLE_PADDING + (CUT_WIDTH - 2 * RECTANGLE_PADDING) / 2.0 - 5 + random.nextInt(10);
        //y 矩形上边界半径-1像素移动
        double y1_top = RECTANGLE_PADDING - random.nextInt(3);
        double y1_bottom = CUT_HEIGHT - RECTANGLE_PADDING + random.nextInt(3);
        double y1 = random.nextInt(2) == 1 ? y1_top : y1_bottom;


        double x2_right = CUT_WIDTH - RECTANGLE_PADDING - CIRCLE_R + random.nextInt(2 * CIRCLE_R - 4);
        double x2_left = RECTANGLE_PADDING + CIRCLE_R - 2 - random.nextInt(2 * CIRCLE_R - 4);
        double x2 = random.nextInt(2) == 1 ? x2_right : x2_left;
        double y2 = RECTANGLE_PADDING + (CUT_HEIGHT - 2 * RECTANGLE_PADDING) / 2.0 - 4 + random.nextInt(10);

        double po = Math.pow(CIRCLE_R, 2);
        for (int i = 0; i < CUT_WIDTH; i++) {
            for (int j = 0; j < CUT_HEIGHT; j++) {
                //矩形区域
                boolean fill;
                if ((i >= RECTANGLE_PADDING && i < CUT_WIDTH - RECTANGLE_PADDING)
                        && (j >= RECTANGLE_PADDING && j < CUT_HEIGHT - RECTANGLE_PADDING)) {
                    data[i][j] = 1;
                    fill = true;
                } else {
                    data[i][j] = 0;
                    fill = false;
                }
                //凸出区域
                double d3 = Math.pow(i - x1, 2) + Math.pow(j - y1, 2);
                if (d3 < po) {
                    data[i][j] = 1;
                } else {
                    if (!fill) {
                        data[i][j] = 0;
                    }
                }
                //凹进区域
                double d4 = Math.pow(i - x2, 2) + Math.pow(j - y2, 2);
                if (d4 < po) {
                    data[i][j] = 0;
                }
            }
        }
        //边界阴影
        for (int i = 0; i < CUT_WIDTH; i++) {
            for (int j = 0; j < CUT_HEIGHT; j++) {
                //四个正方形边角处理
                for (int k = 1; k <= SLIDER_IMG_OUT_PADDING; k++) {
                    //左上、右上
                    if (i >= RECTANGLE_PADDING - k && i < RECTANGLE_PADDING
                            && ((j >= RECTANGLE_PADDING - k && j < RECTANGLE_PADDING)
                            || (j >= CUT_HEIGHT - RECTANGLE_PADDING - k && j < CUT_HEIGHT - RECTANGLE_PADDING + 1))) {
                        data[i][j] = 2;
                    }

                    //左下、右下
                    if (i >= CUT_WIDTH - RECTANGLE_PADDING + k - 1 && i < CUT_WIDTH - RECTANGLE_PADDING + 1) {
                        for (int n = 1; n <= SLIDER_IMG_OUT_PADDING; n++) {
                            if (((j >= RECTANGLE_PADDING - n && j < RECTANGLE_PADDING)
                                    || (j >= CUT_HEIGHT - RECTANGLE_PADDING - n && j <= CUT_HEIGHT - RECTANGLE_PADDING))) {
                                data[i][j] = 2;
                            }
                        }
                    }
                }

                if (data[i][j] == 1 && j - SLIDER_IMG_OUT_PADDING > 0 && data[i][j - SLIDER_IMG_OUT_PADDING] == 0) {
                    data[i][j - SLIDER_IMG_OUT_PADDING] = 2;
                }
                if (data[i][j] == 1 && j + SLIDER_IMG_OUT_PADDING > 0 && j + SLIDER_IMG_OUT_PADDING < CUT_HEIGHT && data[i][j + SLIDER_IMG_OUT_PADDING] == 0) {
                    data[i][j + SLIDER_IMG_OUT_PADDING] = 2;
                }
                if (data[i][j] == 1 && i - SLIDER_IMG_OUT_PADDING > 0 && data[i - SLIDER_IMG_OUT_PADDING][j] == 0) {
                    data[i - SLIDER_IMG_OUT_PADDING][j] = 2;
                }
                if (data[i][j] == 1 && i + SLIDER_IMG_OUT_PADDING > 0 && i + SLIDER_IMG_OUT_PADDING < CUT_WIDTH && data[i + SLIDER_IMG_OUT_PADDING][j] == 0) {
                    data[i + SLIDER_IMG_OUT_PADDING][j] = 2;
                }
            }
        }
        return data;
    }

    /**
     * 裁剪区块
     * 根据生成的滑块形状，对原图和裁剪块进行变色处理
     *
     * @param oriImage    原图
     * @param targetImage 裁剪图
     * @param blockImage  滑块
     * @param x           裁剪点x
     * @param y           裁剪点y
     */
    private static void cutImgByTemplate(BufferedImage oriImage, BufferedImage targetImage, int[][] blockImage, int x, int y) {
        for (int i = 0; i < CUT_WIDTH; i++) {
            for (int j = 0; j < CUT_HEIGHT; j++) {
                int _x = x + i;
                int _y = y + j;
                int rgbFlg = blockImage[i][j];
                int rgb_ori = oriImage.getRGB(_x, _y);
                // 原图中对应位置变色处理
                if (rgbFlg == 1) {
                    //抠图上复制对应颜色值
                    targetImage.setRGB(i, j, rgb_ori);
                    //原图对应位置颜色变化
                    oriImage.setRGB(_x, _y, Color.LIGHT_GRAY.getRGB());
                } else if (rgbFlg == 2) {
                    targetImage.setRGB(i, j, Color.WHITE.getRGB());
                    oriImage.setRGB(_x, _y, Color.GRAY.getRGB());
                } else if (rgbFlg == 0) {
                    //int alpha = 0;
                    targetImage.setRGB(i, j, rgb_ori & 0x00ffffff);
                }
            }

        }
    }

    /**
     * 随机获取一张图片对象
     *
     * @author fengshuonan
     * @since 2021/7/5 14:07
     */
    public static BufferedImage getRandomImage(String path) throws IOException {
        File files = new File(path);
        File[] fileList = files.listFiles();
        List<String> fileNameList = new ArrayList<>();
        if (fileList != null && fileList.length != 0) {
            for (File tempFile : fileList) {
                if (tempFile.isFile() && tempFile.getName().endsWith(".jpg")) {
                    fileNameList.add(tempFile.getAbsolutePath().trim());
                }
            }
        }
        Random random = new Random();
        File imageFile = new File(fileNameList.get(random.nextInt(fileNameList.size())));
        return ImageIO.read(imageFile);
    }

    /**
     * 将IMG输出为文件
     *
     * @author fengshuonan
     * @since 2021/7/5 14:07
     */
    public static void writeImg(BufferedImage image, String file) throws Exception {
        byte[] imagedata = null;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        ImageIO.write(image, "png", bao);
        imagedata = bao.toByteArray();
        FileOutputStream out = new FileOutputStream(new File(file));
        out.write(imagedata);
        out.close();
    }

    /**
     * 将图片转换为BASE64
     *
     * @author fengshuonan
     * @since 2021/7/5 14:07
     */
    public static String getImageBASE64(BufferedImage image) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);
        //转成byte数组
        byte[] bytes = out.toByteArray();
        //生成BASE64编码
        return Base64.encode(bytes);
    }

    /**
     * 生成 375×200 透明底图，并在随机位置绘制 50×50 滑块切块
     * 阴影 4 像素渐变，滑块与底图异色
     *
     * @author fengshuonan
     * @since 2025/10/29 19:08
     */
    public static DragCaptchaImageDTO getMaskBackground() throws IOException {
        int bgW = ORI_WIDTH;
        int bgH = ORI_HEIGHT;
        int x = new Random().nextInt(bgW - CUT_WIDTH);
        int y = new Random().nextInt(bgH - CUT_HEIGHT);

        BufferedImage bg = new BufferedImage(bgW, bgH, BufferedImage.TYPE_4BYTE_ABGR);
        BufferedImage tile = new BufferedImage(CUT_WIDTH, CUT_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);

        // 1. 生成随机滑块形状
        int[][] shape = getBlockData();

        // 2. 先画柔和阴影
        drawSmoothShadow(bg, tile, shape, x, y);

        // 3. 再画滑块主体（亮蓝）与底图抠空（随机柔和色）
        Color slideColor = new Color(50, 150, 255);          // 高对比蓝
        Color holeColor = randomSoftColor();                // 底图填充柔和色
        for (int i = 0; i < CUT_WIDTH; i++) {
            for (int j = 0; j < CUT_HEIGHT; j++) {
                int flg = shape[i][j];
                if (flg != 1) {
                    // 只处理主体区
                    continue;
                }

                // 底图：抠空并填色
                bg.setRGB(x + i, y + j, holeColor.getRGB());

                // 滑块：异色
                tile.setRGB(i, j, slideColor.getRGB());
            }
        }

        return new DragCaptchaImageDTO(
                getImageBASE64(bg),
                getImageBASE64(tile),
                x, y);
    }

    /**
     * 阴影往外晕开4像素，渐变阴影：黑→灰→白，alpha 递减
     *
     * @author fengshuonan
     * @since 2025/10/30 10:56
     */
    private static void drawSmoothShadow(BufferedImage bg, BufferedImage tile, int[][] shape, int baseX, int baseY) {
        int shadowRadius = 4;
        for (int dist = shadowRadius; dist >= 1; dist--) {
            int alpha = (int) (80 * (dist / (double) shadowRadius)); // 越远越淡
            Color shade = new Color(0, 0, 0, alpha);
            for (int i = 0; i < CUT_WIDTH; i++) {
                for (int j = 0; j < CUT_HEIGHT; j++) {
                    if (shape[i][j] != 1)
                        continue;
                    // 向四周扩 dist 像素
                    for (int di = -dist; di <= dist; di++) {
                        for (int dj = -dist; dj <= dist; dj++) {
                            if (Math.abs(di) != dist && Math.abs(dj) != dist)
                                continue;
                            int xi = i + di, yj = j + dj;
                            if (xi < 0 || xi >= CUT_WIDTH || yj < 0 || yj >= CUT_HEIGHT)
                                continue;
                            if (shape[xi][yj] == 1)
                                continue;          // 已有主体，跳过
                            bg.setRGB(baseX + xi, baseY + yj, shade.getRGB());
                            tile.setRGB(xi, yj, shade.getRGB());
                        }
                    }
                }
            }
        }
    }

    /**
     * 随机柔和色，用于底图抠空区
     *
     * @author fengshuonan
     * @since 2025/10/30 10:54
     */
    private static Color randomSoftColor() {
        Random r = new Random();
        return new Color(
                200 + r.nextInt(55),
                180 + r.nextInt(75),
                180 + r.nextInt(75));
    }

}