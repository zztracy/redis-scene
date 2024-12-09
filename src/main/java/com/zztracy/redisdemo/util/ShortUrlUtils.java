package com.zztracy.redisdemo.util;

import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 短链接工具类
 *
 * @author 詹泽
 * @since 2024/12/5 15:37
 */
public class ShortUrlUtils {

    // 定义一个静态方法，用于将给定的长URL转换为短URL形式（这里是生成多个短URL片段的逻辑）
    public static String[] shortUrl(String url) {
        // 用于临时存储一些处理过程中的关键数据，初始化为空字符串，后续会根据逻辑进行赋值
        String key = "";

        // 使用Apache Commons Codec库中的DigestUtils工具类对传入的URL进行MD5加密
        // 将URL转换为UTF-8编码的字节数组后进行MD5计算，得到一个十六进制表示的MD5结果字符串
        String md5Result = DigestUtils.md5DigestAsHex(url.getBytes(StandardCharsets.UTF_8));
        // 将MD5结果字符串赋值给hex变量，方便后续进行处理，这里其实可以直接使用md5Result，多赋值一次只是为了代码逻辑更清晰一些
        String hex = md5Result;

        // 打印MD5加密后的结果，这可能在调试或者查看原始加密数据时有用，实际生产环境中可以根据需求决定是否保留这行代码
        System.out.println(md5Result);

        // 创建一个长度为4的字符串数组，用于存储最终生成的4个短URL片段（这里假设要生成4个不同的短URL相关片段）
        String[] resUrl = new String[4];

        // 通过循环4次，每次生成一个短URL片段
        for (int i = 0; i < 4; i++) {
            // 从hex字符串（也就是MD5结果字符串）中截取8个字符长度的子字符串
            // 每次截取的起始位置根据循环变量i进行计算，每次向后移动8个字符位置，这样可以获取到不同部分的MD5结果片段
            String tempSubString = hex.substring(i * 8, i * 8 + 8);
            // 将截取到的十六进制字符串形式的子字符串转换为长整型数值
            // 这里使用了按位与操作（0x3FFFFFFF），目的是截取低30位，去除可能存在的高位符号位等多余信息，确保转换后的数值范围符合后续处理要求
            long lHexLong = 0x3FFFFFFF & Long.parseLong(tempSubString, 16);
            // 用于临时存储每次生成短URL片段过程中的字符拼接结果，初始化为空字符串
            String outChars = "";

            // 通过循环6次，构建短URL片段中的每个字符（这里的逻辑可能是根据特定算法从lHexLong数值中提取字符信息来组成短URL片段）
            for (int j = 0; j < 6; j++) {
                // 通过按位与操作（0x0000003D）获取lHexLong数值的低6位，这可能对应着一个索引或者某种编码值，用于确定短URL片段中的一个字符
                long index = 0x0000003D & lHexLong;
                // 将lHexLong数值右移5位，这可能是为了更新数值，准备下一次循环获取下一个字符相关的索引或编码值，同时将获取到的字符拼接到outChars字符串中
                outChars += lHexLong >> 5;
            }

            // 将生成好的短URL片段字符串存入对应的数组索引位置，这样最终resUrl数组就存储了4个生成的短URL片段
            resUrl[i] = outChars;
        }

        // 返回包含4个短URL片段的字符串数组，调用者可以根据需要进一步处理这些片段，比如组合成完整的短URL等
        return resUrl;
    }

}
