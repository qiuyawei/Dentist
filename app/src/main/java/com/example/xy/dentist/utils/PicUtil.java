package com.example.xy.dentist.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2016/9/15.
 */
public class PicUtil {

    public static final int PHOTOZOOM = 0;
    public static final int PHOTOTAKE = 1;
    public static final int IMAGE_COMPLETE = 2; // 结果
    private static File f;

    public static void seletePic(String path){

    }


    /**
     * 1.质量压缩
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中


        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static String saveBitmap(Bitmap bm, String path,String picName) {
        try {

               f = new File(path, picName + ".jpg");
            if (f.exists()) {
                f.delete();
            }

            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  f.getPath();
    }

    public static Bitmap LoadBigImg(Bitmap bitmap) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100,baos);
        byte[] bs=baos.toByteArray();
        BitmapFactory.decodeByteArray(bs, 0, bs.length);
        int bitmapWidth = options.outWidth;
        int bitmapHeight = options.outHeight;
        int scale = Math.max(bitmapWidth / 640, bitmapHeight / 640);
        // 缩放的比例
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bs, 0, bs.length);
    }


    /**
     * 图片按比例大小压缩方法
     * @param srcPath  图片路径
     * @return
     */
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        //进行 质量压缩
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 得到临时图片路径
     * @param filePath
     * @return
     * @return
     * @throws IOException
     */
    public static String bitmapToPath(String filePath) throws IOException {

        Bitmap bm = getimage(filePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);

        //得到文件名
        String imgName=getfilepath(filePath);
        //得到存放路径
        String sdPath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/ImgTmp";
        //获取 sdcard的跟目录

        File parent =new File(sdPath);
        if(!parent.exists()){
            //创建路径
            parent.mkdirs();
        }
        //写入 临时文件
        File file =new File(parent,imgName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(baos.toByteArray());
        fos.flush();
        fos.close();
        baos.close();
        //返回图片路径
        return sdPath+"/"+imgName;

    }


    /**
     *
     * @param path
     * @return
     */
    private static String getfilepath(String path){
        return System.currentTimeMillis()+getExtensionName(path);
    }


    /*
* 获取文件名
*/
    public static String getName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }


    /*
     * 获取文件扩展名
     */
    private static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot, filename.length());
            }
        }
        return filename;
    }


    /**
     * 删除临时文件
     * @param imgs
     */
    public static void deleteImgTmp(List<String> imgs){

        for (String string : imgs) {
            File file=new File(string);
            if(file.exists()){
                file.delete();
            }
        }

    }

    /**
     *  decodeStream是直接读取图片资料的字节码了， 不会根据机器的各种分辨率来自动适应，使用了decodeStream之后，
     *  需要在hdpi和mdpi，ldpi中配置相应的图片资源，否则在不同分辨率机器上都是同样大小（像素点数量），显示出来的大小就不对了。
     * @param context
     * @param res
     * @return
     */


    public static BitmapDrawable compressImg(Context context,int  res){
        BitmapFactory.Options opt=new BitmapFactory.Options();
        opt.inPreferredConfig= Bitmap.Config.RGB_565;
        opt.inPurgeable=true;
        opt.inInputShareable=true;
        InputStream is=context.getResources().openRawResource(res);
        Bitmap bm= BitmapFactory.decodeStream(is, null, opt);
        BitmapDrawable bd=new BitmapDrawable(context.getResources(),bm);
        return bd;
    }

}
