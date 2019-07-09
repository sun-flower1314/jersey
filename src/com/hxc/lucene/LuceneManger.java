package com.hxc.lucene;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.surround.parser.ParseException;
import org.apache.lucene.queryparser.surround.parser.QueryParser;
import org.apache.lucene.queryparser.surround.query.SrndQuery;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;

import com.sun.org.apache.bcel.internal.classfile.Field;

import javafx.scene.control.TextField;

/**
 * @Description 索引维护
 * 增加
 * 删除
 * 修改
 * 查询 
 * 2019年5月6日  下午2:33:25
 * @Author Huangxiaocong
 */
public class LuceneManger {

	//获取IindexWriter对象
	public IndexWriter getIndexWriter() throws IOException {
		Path path = Paths.get("D:\\Workspace\\temp\\index");
		Directory directory = FSDirectory.open(path);
//		Analyzer analyzer = new CJKAnalyzer();
//		Analyzer analyzer = new IKAnalyzer();
//		Analyzer analyzer = new StandardAnalyzer();
		Analyzer analyzer = new SmartChineseAnalyzer();
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		return indexWriter;
	}

	//获取IndexReader对象
	public IndexSearcher getIndexSearcher() throws IOException {
		Path path = Paths.get("D:\\Workspace\\temp\\index");
		Directory directory = FSDirectory.open(path);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		return indexSearcher;
		
	}
	//多个默认域查询MultiFieldQueryParser
	//条件解释的对象查询
	@Test
	public void testQueryParser() throws IOException, ParseException {
		IndexSearcher indexSearcher = getIndexSearcher();
		QueryParser query = new QueryParser();
		SrndQuery parse = query.parse("*:*");	//查询所有
//		indexSearcher.search(parse, 20);
		
	}
	
	//组合查询
	@Test
	public void testBooleanQuery() throws IOException {
		IndexSearcher indexSearcher = getIndexSearcher();
//		BooleanQuery bQuery = new BooleanQuery();
//		Query query = new TermQuery(new Term("fileName", "tomcat"));
//		Query query2 = new TermQuery(new Term("fileContent", "tomcat"));
//		indexSearcher.search(query, 20);
	}
	//根据数值查询
	@Test 
	public void testNumericRangeQuery() throws IOException {
		IndexSearcher indexSearcher = getIndexSearcher();
//		Query query = NumericRangeQuery.newLongRange(1, 100, true, true);
//		TopDocs search = indexSearcher.search(query, 20);
	}
	//查询所有
	@Test
	public void testQueryAll() throws IOException {
		IndexSearcher indexSearcher = getIndexSearcher();
		Query query = new MatchAllDocsQuery();	//查询所有
		TopDocs search = indexSearcher.search(query, 20);
		ScoreDoc[] scoreDocs = search.scoreDocs;
		for(ScoreDoc scoreDoc : scoreDocs) {
			int doc = scoreDoc.doc;
			Document document = indexSearcher.doc(doc);
			String fileName = document.get("fileName");
			System.out.println(fileName);
		}
		indexSearcher.getIndexReader().close();
	}
	//修改
	@Test
	public void testUpdate() throws IOException {
		IndexWriter indexWriter = getIndexWriter();
		Term term = new Term("fileSize", "tomcat");
		Document document = new Document();
//		Field field = new Field();
//		document.add(field);
		indexWriter.updateDocument(term, document);
	}
	//删除部分
	@Test
	public void testDelete() throws IOException {
		IndexWriter indexWriter = getIndexWriter();
		Query query = new TermQuery(new Term("fileName", "xml"));
		indexWriter.deleteDocuments(query);
		System.out.println("部分删除成功");
		indexWriter.close();
	}
	//删除所有
	@Test
	public void testAllDelete() throws IOException {
		IndexWriter indexWriter = getIndexWriter();
		indexWriter.deleteAll();
		System.out.println("全部删除成功");
		indexWriter.close();
	}
	
	
	
}
