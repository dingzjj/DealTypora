package com.dj;
import com.dj.PackService.PackService;
import com.dj.PackService.PackServiceImpl;
import com.dj.ParseService.ParseService;
import com.dj.ParseService.ParseServiceImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * @author dj
 * @version 1.0
 **/

public class Main {
    private static ParseService parseService= new ParseServiceImpl();
    private static PackService packService = new PackServiceImpl();
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("selection function:pack(0) or parse(1)");
        int i = scanner.nextInt();
        if(i==0){
            System.out.println("=======pack=======");
            packService.PackFileInResources();
        }else if(i==1){
            System.out.println("=======parse=======");
            System.out.println("choose the dir :");
            String saveDir = scanner.next();
            if(new File(saveDir).exists()){
                parseService.ParseFileInResources(Paths.get(saveDir));
            }else {
                System.out.println("no such dir :"+saveDir);
            }
        }else {
            System.out.println("ERROR choose");
        }




    }
}
