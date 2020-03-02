package site.pyyf.dao;

import org.apache.ibatis.annotations.Mapper;
import site.pyyf.entity.Ebook;

import java.util.List;

@Mapper
public interface IlibraryMapper {
    List<Ebook> selectAllEbooks();

    Ebook selectByBookId(String bookId);



    void insertEbook(Ebook eBook);

    String selectHeaderByBookId(String ebookId);

    void deleteByBookId(String ebookId);

    void updateEbookNameByBookId(String ebookId,String ebookName);
}
