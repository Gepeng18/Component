package site.pyyf.utils;

/**
 * Created by "gepeng" on 2020-02-58 03:42:10.
 * 从markdown文件中提取标题
 * <p>
 * 检测本行以#开头时，下一行至下一个#截止中间的内容保存起来。
 * 保存过程中，当检测到```时，不检测，直至出现```则重新开始检测#
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import site.pyyf.dao.IebookContentMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
class Directory {
    String uuid;
    StringBuilder content = new StringBuilder();
    private Map<String, Directory> subNodes = new LinkedHashMap<>();

    // 添加子节点
    public void addSubNode(String fileName, Directory node) {
        subNodes.put(fileName, node);
    }

    // 获取子节点
    public Directory getSubNode(String fileName) {
        return subNodes.get(fileName);
    }

    public StringBuilder getContent() {
        return content;
    }

    public void setContent(StringBuilder content) {
        this.content = content;
    }

    // 获取子节点
    public Map<String, Directory> getAllNode() {
        return subNodes;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}

@Component
@Scope("prototype")
public class resolveHeaderFromMd//存储指定文件夹所有文件名的 树类
{

    @Autowired
    private IebookContentMapper iebookContentMapper;

    private String bookName;

    private Directory pre;
    private StringBuilder tmp = new StringBuilder();
    private Directory root;//树根（相当于链表的头指针）
    private boolean detect = true;
    public Directory getRoot()//获取树根
    {
        return root;
    }

    public resolveHeaderFromMd() {
        root = new Directory();//树根（相当于链表的头指针）
    }

    public boolean isNHeader(String buffer, int n) {


        final String trimed = buffer.trim();
        try {
            return (trimed.charAt(n - 1) == '#')&&(trimed.charAt(n) != '#');
        } catch (IndexOutOfBoundsException e) {
            //超过范围了，表明第n个数没有，则肯定不是n级标题
            return false;
        }

    }
//    public Map<String, Directory> directoryMap = new LinkedHashMap<>();

    public void resolveHeader(String buffer) {
        boolean isHeaderLine = false;
        if (detect) {
            Directory current = root;
            //一级目录直接加到根上去
            if (isNHeader(buffer, 1)) {
                isHeaderLine = true;
                final Directory newDir = new Directory();
                if(root.getAllNode().size()>0){
                    pre.setContent(tmp);
                    pre.setUuid(bookName+UUID.randomUUID().toString().replaceAll("-", ""));
                    tmp = new StringBuilder();
                }
                root.addSubNode(buffer.substring(1), newDir);
                pre = newDir;
            }
            for (int i = 2; i < 7; i++) {
                if (isNHeader(buffer, i)) {
                    isHeaderLine = true;
                    int times = i - 1;
                    while (times-- != 0) {
                        final String pre = (String) current.getAllNode().keySet().toArray()[current.getAllNode().size() - 1];
                        current = current.getAllNode().get(pre);
                    }
                    final Directory newDir = new Directory();
                    if(root.getAllNode().size()>0){
                        pre.setContent(tmp);
                        pre.setUuid(bookName+UUID.randomUUID().toString().replaceAll("-", ""));
                        tmp = new StringBuilder();
                    }

                    current.addSubNode(buffer.substring(i), newDir);
                    pre = newDir;
                    break;
                }
            }
        }

        if (buffer.contains("```"))
            detect = !detect;
        if(!isHeaderLine)
            tmp.append(buffer).append("\n");
    }


    /*
     * 函数名：getFile
     * 作用：实现将指定文件夹的所有文件存入树中
     */
    public void readFile(String path) throws Exception {
        if(path.contains("\\"))
            bookName= StringUtils.substringAfterLast(path,"\\");
        else
            bookName=path;
        BufferedReader bfr = new BufferedReader(new FileReader(path));
        StringBuilder content = new StringBuilder();
        String buffer = null;
        while ((buffer = bfr.readLine()) != null) {
            resolveHeader(buffer);
        }
    }


    /*
     * 函数名：printTree
     * 作用：输出树中的内容
     */
    private void printTree(Directory node, int deep) {
        for (Map.Entry<String, Directory> stringFileNodeEntry : node.getAllNode().entrySet()) {
            for (int j = 0; j < deep; j++)//输出前置空格
                System.out.print("       ");
            System.out.println(stringFileNodeEntry.getKey());
            printTree(stringFileNodeEntry.getValue(),deep+1);
        }
    }
    private void printTree(Directory node) {
        printTree(node,0);
    }

    private void printTree(){
        printTree(root,0);
    }

    public static void main(String[] args) throws Exception {
        final resolveHeaderFromMd resolveHeaderFromMd = new resolveHeaderFromMd();
        resolveHeaderFromMd.readFile("C:\\Users\\FHY-GP\\Desktop\\2.md");
        final String s = JSON.toJSONString(resolveHeaderFromMd.getRoot());
        System.out.println(s);
        final Directory directory = JSONObject.parseObject(s, Directory.class);
        new resolveHeaderFromMd().printTree(directory);
        resolveHeaderFromMd.printTree();
    }
}

