package com.hxc.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * @Description 对非结构化数据的索引 
 * 2019年5月5日  上午9:22:59
 * @Author Huangxiaocong
 */
public class MyFirstLucene {
	/**
	 * Store:是否将数据存放到索引库的数据区域
	 * 只有将数据存放到索引库的数据区域，检索的时候才能查到结果，否则查到的结果是null
	 * Index:是否将数据更新到索引库的目录区域
	 * @param args
	 * @Author Huangxiaocong 2019年5月5日 下午5:31:29
	 */
	public static void main(String[] args) {
		
	}
	/**
	 * 查询索引
	 * 步骤：
	 * 第一步：创建IndexReader，由DirectoryReader生成
	 * 第二步：创建IndexSearch
	 * 第三步：创建TermQuery，设置query值
	 * 第四步：执行查询，处理查询结果
	 * 第五步：关闭流
	 * @throws IOException
	 * @Author Huangxiaocong 2019年5月6日 上午10:46:50
	 */
	@Test
	public void testQueryField() throws IOException {
		Path path = Paths.get("D:\\Workspace\\temp\\index");
		Directory directory = FSDirectory.open(path);
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		Query query = new TermQuery(new Term("fileName", "xml"));
		TopDocs searchResult = indexSearcher.search(query, 10);	//查询10条数据
		ScoreDoc[] scoreDocs = searchResult.scoreDocs;
		for(ScoreDoc scoreDoc : scoreDocs) {
			int docNum = scoreDoc.doc;
			Document doc = indexSearcher.doc(docNum);
			String fileName = doc.get("fileName");
			System.out.println("文件的名字为：" + fileName);
			String filePath = doc.get("filePath");
			System.out.println("文件的路径为：" + filePath);
			String fileContent = doc.get("fileContent");
			System.out.println(fileContent);
			System.out.println("====================================");
		}
		System.out.println(scoreDocs.length);
		indexReader.close();
	}
	
	@Test
	public void testCreateField() throws IOException {
		//设置存放索引库的位置
		Path path = Paths.get("D:\\Workspace\\temp\\index");
		Directory directory = FSDirectory.open(path);
		Analyzer analyzer = new SmartChineseAnalyzer();
//		Analyzer analyzer = new StandardAnalyzer();
//		Analyzer analyzer = new IKAnalyzer();
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
		IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
		File file = new File("D:\\Workspace\\JavaWeb笔记");
		File[] listFiles = file.listFiles();
		//各个Field的类型说明：
		/**
		 * StringField 不分析 索引 可选存储
		 * LongField 分析 索引 可选存储
		 * StoreField 不分析 不索引  存储
		 * TextField 分析 索引 可选索引
		 */
		for(File ff : listFiles) {
			Document document = new Document();
			String fileName = ff.getName();
			Field fileNameField = new TextField("fileName", fileName, Store.YES);	//TextField为
			String filePath = ff.getPath();
			Field filePathField = new StringField("filePath", filePath, Store.NO);	//StringField为不分析，索引，可选存储
			//long fileSize = FileUtils.sizeOf(ff);
			String fileContent = FileUtils.readFileToString(ff);
			Field fileContext = new TextField("fileContent", fileContent, Store.YES);
			
			document.add(filePathField);
			document.add(fileNameField);
			document.add(fileContext);
			indexWriter.addDocument(document);
		}
		System.out.println("执行成功");
		indexWriter.close();
	}
	
	public void testQueryAll() {
		
	}
	
	
}
