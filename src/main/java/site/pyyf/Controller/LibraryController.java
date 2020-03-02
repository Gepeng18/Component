package site.pyyf.Controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import site.pyyf.entity.Directory;
import site.pyyf.entity.Ebook;
import site.pyyf.service.LibraryService;
import site.pyyf.service.ResolveHeaderService;

import java.io.EOFException;
import java.util.List;

@Controller
public class LibraryController {

    @Autowired
    private ResolveHeaderService resolveHeaderService;

    @Autowired
    private LibraryService libraryService;

    @RequestMapping("/library")
    public String getLibraryPage(Model model){
        List<Ebook> ebooks = libraryService.selectAllEbooks();
        model.addAttribute("ebooks",ebooks);
        return "library";
    }


    @RequestMapping("/ebook/upload")
    public String upload(MultipartFile file) throws Exception {

        resolveHeaderService.readFile(file.getInputStream(),file.getOriginalFilename());
        return "redirect:/library";
    }


}
