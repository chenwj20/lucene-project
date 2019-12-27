package cn.cwj.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;

public class IndexSearch {
    private  IndexSearcher getIndexSearcher() throws Exception {
        Directory directory = FSDirectory.open(new File("F:\\a\\lucene\\index").toPath());
        IndexReader indexReader = DirectoryReader.open(directory);
        return new IndexSearcher(indexReader);
    }
    //使用Termquery查询
    @Test
    public void testTermQuery() throws Exception {
        Directory directory = FSDirectory.open(new File("F:\\a\\lucene\\index").toPath());
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        //创建查询对象
        Query query = new TermQuery(new Term("content", "lucene"));
        //执行查询
        TopDocs topDocs = indexSearcher.search(query, 10);
        //共查询到的document个数
        System.out.println("查询结果总数量：" + topDocs.totalHits);
        //遍历查询结果
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("filename"));
            //System.out.println(document.get("content"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
        }
        //关闭indexreader
        indexSearcher.getIndexReader().close();
    }
    @Test
    public void testRangeQuery() throws Exception {
        IndexSearcher indexSearcher = getIndexSearcher();
        Query query = LongPoint.newRangeQuery("size", 0l, 10000l);
        printResult(query, indexSearcher);
    }
    @Test
    public void testQueryParser() throws Exception {
        IndexSearcher indexSearcher = getIndexSearcher();
        //创建queryparser对象
        //第一个参数默认搜索的域
        //第二个参数就是分析器对象
        QueryParser queryParser = new QueryParser("content", new IKAnalyzer());
        Query query = queryParser.parse("Lucene是java开发的");
        //执行查询
        printResult(query, indexSearcher);
    }

    private void printResult(Query query, IndexSearcher indexSearcher) throws Exception {
        //执行查询
        TopDocs topDocs = indexSearcher.search(query, 10);
        //共查询到的document个数
        System.out.println("查询结果总数量：" + topDocs.totalHits);
        //遍历查询结果
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            System.out.println(document.get("filename"));
            //System.out.println(document.get("content"));
            System.out.println(document.get("path"));
            System.out.println(document.get("size"));
        }
        //关闭indexreader
        indexSearcher.getIndexReader().close();
    }
}
