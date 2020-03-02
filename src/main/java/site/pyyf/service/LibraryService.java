package site.pyyf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.pyyf.dao.IebookContentMapper;
import site.pyyf.dao.IlibraryMapper;
import site.pyyf.entity.Ebook;
import site.pyyf.entity.EbookConent;

import java.util.List;


@Service
public class LibraryService {



    @Autowired
    private IlibraryMapper ilibraryMapper;

    public List<Ebook> selectAllEbooks() {
        return ilibraryMapper.selectAllEbooks();

    }
    public void insertEbook(Ebook eBook){
        ilibraryMapper.insertEbook(eBook);
    }
    public Ebook selectByBookId(String ebookId) {
        return ilibraryMapper.selectByBookId(ebookId);
    }


    public void deleteByBookId(String ebookId) {
        ilibraryMapper.deleteByBookId(ebookId);
    }

    public void updateEbookNameByBookId(String ebookId,String ebookName) {
        ilibraryMapper.updateEbookNameByBookId(ebookId,ebookName);
    }
}
