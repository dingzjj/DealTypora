package com.dj.ParseService;

import java.io.File;
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

public class ParseServiceImpl implements ParseService {


    public int Parse(String url) throws IOException {

        //修改md中的图片地址

        //文件夹的位置
        Path mdDir = Paths.get(url);
        //文件的位置
        Path md = Paths.get((mdDir.toAbsolutePath().toString() + "\\" + mdDir.getFileName() + ".md"));
        //图片的位置
        Path photoDir = Paths.get((mdDir.toAbsolutePath().toString() + "\\assets"));

        List<String> lines = Files.readAllLines(md, StandardCharsets.UTF_8);
        // path ->    .....\DealTypora\DealingTypora\.  项目的src目录

        for (int i = 0; i < lines.size(); i++) {
//            //根据photo地址中后面的 echarts\assets\6.png来定位修改 ，修改地方后.后的目录不变
//
            Pattern photoUrlPattern = Pattern.compile("[a-zA-z^]+:\\\\[^\"\\s)]*");
            Matcher photoMatcher = photoUrlPattern.matcher(lines.get(i));
            boolean find = photoMatcher.find();
            if (!find) {
                continue;
            }

            //D:\Code\DealTypora\DealingTypora\src\main\resources\echarts\assets\6.png
            String afterMatchPhotoUrl = photoMatcher.group(0);
//
            String realPhotoUrl = (photoDir + "\\" + Paths.get(afterMatchPhotoUrl).getFileName());

            //匹配后的拷贝图片资源先找到，找到后就修改
            if (!Files.exists(Paths.get(realPhotoUrl))) {
                System.out.println(realPhotoUrl + " no find");
                continue;
            }
            //找到后进行文件替换和md中的替换
            String s = lines.get(i).replaceAll("[a-zA-z^]+:\\\\[^\"\\s)]*", realPhotoUrl.replace("\\", "\\\\"));
            lines.set(i, s);
        }
        Files.write(Paths.get(md.toAbsolutePath().toString()), lines, StandardCharsets.UTF_8);
        return ParseService.OK;
    }

    @Override
    public void ParseFileInResources(Path saveDir) throws IOException {
        Path resourcesDir = Paths.get("src/main/resources").toAbsolutePath();

        try (Stream<Path> entries = Files.list(resourcesDir)) {
            entries.forEach(path -> {
                if (new File(path.toString()).isDirectory()) {
                    File MDfile = new File(Paths.get(path.toString() + "\\" + path.getFileName().toString() + ".md").toUri());

                    if (MDfile.exists()) {
                        try {
                            //先将文件夹转移过去
                            TransferDir(path, saveDir);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
        }
        try (Stream<Path> entries = Files.list(saveDir)) {
            entries.forEach(path -> {
                if (new File(path.toString()).isDirectory()) {
                    File MDfile = new File(Paths.get(path.toString() + "\\" + path.getFileName().toString() + ".md").toUri());
                    if (MDfile.exists()) {
                        System.out.println("Parse find " + path);
                        try {
                            //先将文件夹转移过去
                            Parse(path.toString());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
        }


    }

    public void TransferDir(Path from, Path to) {
        //from -> path 即md文件夹的name
        //创建总文件夹
        try {
            Path MDdirectory = Files.createDirectory(Paths.get(to.toString() + "\\" + from.getFileName().toString()));
            if (MDdirectory != null) {
                System.out.println("create dir SUCCESS:" + to.toString() + "\\" + from.getFileName().toString());
            }
        } catch (IOException e) {
            System.out.println("Create dir ERROR:" + to.toString() + "\\" + from.getFileName().toString());
        }
        //将图片转移过去
        try {
            Path old_photoDir = Paths.get(from.toString() + "\\assets");
            Path new_photoDir = Paths.get(to.toString() + "\\" + from.getFileName().toString() + "\\assets");
            Files.createDirectory(new_photoDir);
//            开始转移图片
            Stream<Path> list = Files.list(old_photoDir);
            list.forEach(path -> {
                try {
                    Files.copy(path, Paths.get(new_photoDir.toString() + "\\" + path.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.println("tranfer ERROR:" + path);
                }
//                并且删除源图片
                try {
                    boolean deleted = Files.deleteIfExists(path);
                    if (!deleted) {
                        System.out.println("delete ERROR:" + path);
                    }
                } catch (IOException e) {
                    System.out.println("delete ERROR:" + path);
                }
            });


            //最后删除源图片的文件夹
            boolean b = Files.deleteIfExists(old_photoDir);
            if (!b) {
                System.out.println("delete ERROR:" + old_photoDir.toString());
            }

        } catch (IOException e) {
            System.out.println("create photoDir ERROR:" + to.toString() + "\\" + from.getFileName().toString() + "\\assets");

        }


        //将总文件夹下的md文件过去并且删除-> 修改md文件中的图片地址
        try {
            Path old_MD = Paths.get(from.toString() + "\\" + from.getFileName().toString() + ".md");
            Path new_MD = Paths.get(to.toString() + "\\" + from.getFileName().toString() + "\\" + from.getFileName().toString() + ".md");
            Files.copy(old_MD, new_MD, StandardCopyOption.REPLACE_EXISTING);
            boolean b = Files.deleteIfExists(old_MD);
            if (!b) {
                System.out.println("delete ERROR:" + old_MD.toString());
            }
        } catch (IOException e) {
            System.out.println("Create MDfile ERROR:" + to.toString() + "\\" + from.getFileName().toString() + "\\" + from.getFileName().toString() + ".md");
        }
        //删除总文件夹
        try {
            boolean b = Files.deleteIfExists(from);
            if (!b) {
                System.out.println("delete ERROR:" + from.toString());
            }
        } catch (IOException e) {
            System.out.println("delete ERROR:" + from.toString());
        }

    }


}
