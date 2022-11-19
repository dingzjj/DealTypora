package com.dj.PackService;

import org.junit.Test;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author dj
 * @version 1.0
 **/

public class PackServiceImpl implements PackService {


    public int Pack(String url) throws IOException {
        //文件夹的创建
        Path old_md = Paths.get(url);
        String patternString = "([^.]+).([^.]+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(url);
        boolean b = matcher.find();
        if (!b) {
            System.out.println("match ERROR");
            return ERROR;
        }
        String totalDir = matcher.group(1);
        //总文件夹的创建
        Path totaldirectory = Files.createDirectory(Paths.get(totalDir));
        if (totaldirectory == null) {
            System.out.println("create totaldirectory ERROR");
            return ERROR;
        }
        String photoDir = matcher.group(1) + "\\assets";
        //图片文件夹的创建
        Path photodirectory = Files.createDirectory(Paths.get(photoDir));
        if (photodirectory == null) {
            System.out.println("create photodirectory ERROR");
            return ERROR;
        }
        //从markdown中获取图片地址然后下载到photodirectory中
//[a-zA-z]+:\\[^\s]*
        //匹配成功后能找到源文件的才进行替换
        List<String> lines = Files.readAllLines(old_md, StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            Pattern photoUrlPattern = Pattern.compile("[a-zA-z^]+:\\\\[^\"\\s)]*");
            Matcher photoMatcher = photoUrlPattern.matcher(lines.get(i));
            boolean find = photoMatcher.find();
            if (!find) {
                continue;
            }
            String afterMatchPhotoUrl = photoMatcher.group(0);

            //匹配后的拷贝图片资源先找到，找到后拷贝到photodirectory中，修改md中的地址，找不到就修改
            if (!Files.exists(Paths.get(afterMatchPhotoUrl))) {
                System.out.println(afterMatchPhotoUrl + " no find");
                continue;
            }
            Pattern photoPattern = Pattern.compile("([^.]+).([^.]+)");
            Matcher photomatcher = photoPattern.matcher(afterMatchPhotoUrl);
            photomatcher.find();
            if (!photomatcher.group(2).contains("png") && !photomatcher.group(2).contains("jpeg")) {
                continue;
            }
            //找到后进行文件替换和md中的替换
            String photo = photoDir + "\\" + i + "." + photomatcher.group(2);
            Files.copy(Paths.get(afterMatchPhotoUrl), Paths.get(photo), StandardCopyOption.REPLACE_EXISTING);

            String s = lines.get(i).replaceAll("[a-zA-z^]+:\\\\[^\"\\s)]*", photo.replace("\\", "\\\\"));
            lines.set(i, s);

        }
        String new_md = totalDir + "\\" + old_md.getFileName();
        Files.write(Paths.get(new_md), lines, StandardCharsets.UTF_8);

        Files.delete(old_md);

        return PackService.OK;
    }

    @Test
    public void PackFileInResources() throws IOException {
        Path resourcesDir = Paths.get("src/main/resources").toAbsolutePath();

        try (Stream<Path> entries = Files.list(resourcesDir)) {
            entries.forEach(path -> {
                System.out.println("Pack find " + path);
                if (endwithMD(path)) {
                    try {
                        Pack(path.toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public boolean endwithMD(Path path) {
        Pattern MDpattern = Pattern.compile("([^.]+).([^.]+)");
        Matcher MDmatcher = MDpattern.matcher(path.toString());
        MDmatcher.find();
        return MDmatcher.group(2).equals("md");
    }


}
