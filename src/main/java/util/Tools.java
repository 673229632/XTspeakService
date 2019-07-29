package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import constant.TextToSpeechContent;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhangrui.i
 * @version V1.0
 * @Description: TODO
 * @date
 */
public class Tools {

    /**
     * 数据格式化.
     *
     * @param jsonString String
     * @return String
     */
    public static String getInfosByJsonStr(String jsonString) {

        if (!isblank(jsonString)) {
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            TextToSpeechContent textToSpeechContent = JSON.parseObject(
                    jsonObject.getString("textToSpeechContent"), TextToSpeechContent.class);
            return textToSpeechContent.toString();
        }
        return null;
    }


    /**
     * 判断字符串是否为空.
     *
     * @param param
     * @return boolean
     */
    public static boolean isblank(Object param) {
        if (null == param || "".equals(param.toString().trim())
                || "null".equals(param.toString().toLowerCase().trim())) {
            return true;
        }
        return false;
    }


    /**
     * 处理语音模板.
     *
     * @param textToSpeechContent
     * @return
     */
    public static String getTextToSpeechContentForTemplate(TextToSpeechContent textToSpeechContent) {
        // 返回的结果
        String resultStr = textToSpeechContent.getTemplate();
        // 模板
        String templateName = textToSpeechContent.getTemplate();
        Pattern p = Pattern.compile("\\{(\\w+)\\}");
        Matcher m = p.matcher(templateName);
        while (m.find()) {
            String filedname = m.group(1);
            Method[] methods = textToSpeechContent.getClass().getMethods();
            for (Method method : methods) {
                if (method.getName().toLowerCase().equals(("get" + filedname).toLowerCase())) {
                    try {
                        String value = (String) method.invoke(textToSpeechContent);
                        resultStr = resultStr.replaceAll("\\{" + filedname + "\\}", value);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return resultStr;
    }

    /**
     * 语音转文字并播放.
     *
     * @param text String
     */
    public static String textToSpeech(String text) {
        ActiveXComponent ax = null;
        try {
            ax = new ActiveXComponent("Sapi.SpVoice");

            // 运行时输出语音内容
            Dispatch spVoice = ax.getObject();
            // 音量 0-100
            ax.setProperty("Volume", new Variant(100));
            // 语音朗读速度 -10 到 +10
            ax.setProperty("Rate", new Variant(0));
            // 执行朗读
            Dispatch.call(spVoice, "Speak", new Variant(text));

            // 下面是构建文件流把生成语音文件

            /*ax = new ActiveXComponent("Sapi.SpFileStream");
            Dispatch spFileStream = ax.getObject();

            ax = new ActiveXComponent("Sapi.SpAudioFormat");
            Dispatch spAudioFormat = ax.getObject();

            // 设置音频流格式
            Dispatch.put(spAudioFormat, "Type", new Variant(22));
            // 设置文件输出流格式
            Dispatch.putRef(spFileStream, "Format", spAudioFormat);
            // 调用输出 文件流打开方法，创建一个.wav文件
            Dispatch.call(spFileStream, "Open", new Variant("./text.wav"), new Variant(3), new Variant(true));
            // 设置声音对象的音频输出流为输出文件对象
            Dispatch.putRef(spVoice, "AudioOutputStream", spFileStream);
            // 设置音量 0到100
            Dispatch.put(spVoice, "Volume", new Variant(100));
            // 设置朗读速度
            Dispatch.put(spVoice, "Rate", new Variant(0));
            // 开始朗读
            Dispatch.call(spVoice, "Speak", new Variant(text));

            // 关闭输出文件
            Dispatch.call(spFileStream, "Close");
            Dispatch.putRef(spVoice, "AudioOutputStream", null);

            spAudioFormat.safeRelease();
            spFileStream.safeRelease();*/
            spVoice.safeRelease();
            ax.safeRelease();

            System.out.println("播报完成:" + text);
            return "播报完成";
        } catch (Exception e) {
            e.printStackTrace();
            return "播报失败";
        }
    }
}
