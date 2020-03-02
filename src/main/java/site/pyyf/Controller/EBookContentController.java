package site.pyyf.Controller;

import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.pyyf.entity.Directory;
import site.pyyf.entity.Ebook;
import site.pyyf.entity.EbookConent;
import site.pyyf.service.EbookContentService;
import site.pyyf.service.LibraryService;
import site.pyyf.service.ResolveHeaderService;
import site.pyyf.utils.CommunityUtil;
import site.pyyf.utils.MarkdownUtils;

@Controller
public class EBookContentController {

    @Autowired
    public EbookContentService ebookContentService;

    @Autowired
    public ResolveHeaderService resolveHeaderService;

    @Autowired
    private LibraryService libraryService;

//    @RequestMapping("/ebook")
//    public String fun(Model model) {
////        final InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("容器_ossmd.md");
////        resolveHeaderService.readFile(resourceAsStream,"容器_ossmd.md");
//        String header = ebookService.selectHeaderByFileName("容器_ossmd.md");
//        final Directory directory = JSONObject.parseObject(header, Directory.class);
//        model.addAttribute("headers", directory);
//        return "index";
//    }

    @RequestMapping(path = "/ebook/getbook/{bookId}")
    public String getEbook(@PathVariable(value = "bookId") String bookId, Model model) {

        Ebook ebook = libraryService.selectByBookId(bookId);

        final Directory directory = JSONObject.parseObject(ebook.getHeader(), Directory.class);
        model.addAttribute("headers", directory);
        return "ebook";
    }


    @ResponseBody
    @RequestMapping(value = "/ebook/getcontent", method = RequestMethod.POST)
    public String getcontent(@RequestParam("contentId") String contentId) {
        final String content = ebookContentService.selectContentByContentId(contentId);
        final String htmlContent = MarkdownUtils.markdownToHtmlExtensions(content);
        StringBuilder processedContent = new StringBuilder();
        int i = 0;
        char buf;
        while (i < htmlContent.length()) {
            if ((buf = htmlContent.charAt(i)) == '\n' && i != 0) {
                if (CommunityUtil.isChineseNumberAlpha(htmlContent.charAt(i - 1)))
                    processedContent.append("<br>");
            }
            if (htmlContent.charAt(i) == '~')
                processedContent.append("<br>");
            else
                processedContent.append(buf);
            i++;
        }
        return CommunityUtil.getJSONString(200, processedContent.toString());
    }

    @RequestMapping(value = "/ebook/goupdate", method = RequestMethod.POST)
    public String goUpdatePage(@RequestParam("contentId") String contentId,
                               Model model) {
        final String content = ebookContentService.selectContentByContentId(contentId);
        model.addAttribute("content", content);
        model.addAttribute("contentId", contentId);
        return "update";
    }

    @RequestMapping(value = "/ebook/update", method = RequestMethod.POST)
    public String updateContent(@RequestParam("content") String content,
                                @RequestParam("contentId") String contentId,
                                Model model) {
        ebookContentService.updateContentByContentId(contentId, content);
        EbookConent ebookConent = ebookContentService.selectByContentId(contentId);
        return "redirect:/ebook/getbook/"+ebookConent.getEbookId();
    }

}
