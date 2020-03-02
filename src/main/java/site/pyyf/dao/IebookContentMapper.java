package site.pyyf.dao;


import org.apache.ibatis.annotations.Mapper;
import site.pyyf.entity.Ebook;
import site.pyyf.entity.EbookConent;

@Mapper
public interface IebookContentMapper {

    String selectContentByContentId(String content_id);

    void insertEbookContent(EbookConent ebookConent);

    EbookConent selectByContentId(String contentId);

    void updateContentByContentId(String contentId,String content);



}
