package com.dj.PackService;

import java.io.IOException;

public interface PackService {
    public  static int  OK= 1;
    public  static int  ERROR= 0;

    //int 为Status状态的返回值
    public int Pack(String url) throws IOException;

    public void PackFileInResources() throws IOException;

}
