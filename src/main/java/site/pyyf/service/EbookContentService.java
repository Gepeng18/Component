package site.pyyf.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import site.pyyf.dao.IebookMapper;
import site.pyyf.dao.IebookContentMapper;
import site.pyyf.entity.Ebook;
import site.pyyf.entity.EbookConent;

@Service
public class EbookContentService {

    @Autowired
    public IebookContentMapper iebookContentMapper;
    public String selectContentByContentId(String contentId){
        return iebookContentMapper.selectContentByContentId(contentId);
    }

    public void insertEbookContent(EbookConent ebookConent){
        iebookContentMapper.insertEbookContent(ebookConent);
    }


    public void updateContentByContentId(String contentId,String content){
        iebookContentMapper.updateContentByContentId(contentId,content);
    }

    public EbookConent selectByContentId(String contentId) {
        return iebookContentMapper.selectByContentId(contentId);
    }


}


