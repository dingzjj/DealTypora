package com.dj.ParseService;

import java.io.IOException;
import java.nio.file.Path;

public interface ParseService {

    public  static int  OK= 1;
    public  static int  ERROR= 0;

    /**
     *
     * @param url
     * @return Status状态的返回值
     * @throws IOException
     */
    public int Parse(String url) throws IOException;

    /**
     *解resources下的包
     * @param saveDir 将resources下的文件解包后存放的地方
     * @throws IOException
     */
    public void ParseFileInResources(Path saveDir) throws IOException;

    /**
     * 转移文件夹
     * @param from 文件夹的旧地址
     * @param to   文件夹要转移的地方的父文件夹
     * @return    文件夹的新位置
     */
    public void TransferDir(Path from , Path to) throws IOException;
}
