package site.pyyf;

import org.junit.Test;


public class fileSuffix {

    @Test
    public void test(){
        String path = "F:\\Projects\\Java\\Community\\1.md";
        final String substring = path.substring(path.lastIndexOf(".") + 1);
        System.out.println(substring);
    }


}
